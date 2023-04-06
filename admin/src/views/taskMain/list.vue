<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-refresh" v-if="hasPermission('taskMain:list')"
            @click.native.prevent="getTaskMainList">刷新</el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" v-if="hasPermission('taskMain:add')"
            @click.native.prevent="showAddRoleDialog">添加任务</el-button>

          <!-- <el-button type="primary" size="mini" icon="el-icon-download" v-if="hasPermission('taskMain:list')"
            @click.native.prevent="downTmp">下载模板</el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" v-if="hasPermission('taskMain:list')"
            @click.native.prevent="showUploadDialog = true">一键导入</el-button> -->
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="taskMainList" v-loading.body="listLoading" element-loading-text="loading" border fit
      highlight-current-row>
      <el-table-column label="#" align="center" prop="id" width="80">
      </el-table-column>
      <el-table-column label="任务号" align="center" prop="taskNo" />
      <el-table-column label="任务名称" align="center" prop="name" />
      <el-table-column label="任务类型" align="center" prop="type">

        <template slot-scope="scope">

          <el-tag v-if="scope.row.type === 1" type="success" disable-transitions>日报</el-tag>
          <el-tag v-if="scope.row.type === 2" type="success" disable-transitions>周报</el-tag>
          <el-tag v-if="scope.row.type === 3" type="success" disable-transitions>年报</el-tag>

          <!-- <el-button v-if="scope.row.status === 0" size="mini" @click.native.prevent="showCmdTaskDialog(scope.row)"
            disable-transitions>未发布（点击发布）</el-button> -->
        </template>
      </el-table-column>
      <el-table-column label="模板号" align="center" prop="tempId">
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status">

        <template slot-scope="scope">

          <el-tag v-if="scope.row.status === 1" type="success" disable-transitions>已下发</el-tag>

          <el-button v-if="scope.row.status === 0" size="mini" @click.native.prevent="toCmdTaskDialog(scope.row)"
            disable-transitions>未下发（点击下发）</el-button>
        </template>
      </el-table-column>
      <el-table-column label="截至时间" align="center" prop="endTime">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.endTime) }}</template>
      </el-table-column>
      <el-table-column label="管理" align="center"
        v-if="hasPermission('taskMain:detail') || hasPermission('taskMain:update') || hasPermission('taskMain:delete')">
        <template slot-scope="scope">
          <!-- <el-button
            type="info"
            size="mini"
            v-if="hasPermission('taskMain:detail')"
            @click.native.prevent="showRoleDialog(scope.$index)"
          >查看</el-button> -->
          <el-button type="warning" size="mini" v-if="hasPermission('taskMain:reset')"
            @click.native.prevent="resetPassword(scope.row.id)">重置密码</el-button>
          <el-button type="danger" size="mini" v-if="hasPermission('taskMain:delete')"
            @click.native.prevent="removeRole(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
      :page-size="listQuery.size" :total="total" :page-sizes="[9, 18, 36, 72]"
      layout="total, sizes, prev, pager, next, jumper"></el-pagination>

    <!-- 任务添加开始 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogTaskMainFormVisible">
      <el-form status-icon class="small-space" label-position="left" label-width="100px"
        style="width: 500px; margin-left:50px;" :model="tempTaskMain" :rules="createRules" ref="tempTaskMain">
        <el-form-item label="发起人" prop="fromUser" required>
          <el-input placeholder="" v-model="tempTaskMain.fromUser" :disabled="true">
          </el-input>

        </el-form-item>
        <el-form-item label="接收企业" prop="toCom">
          <el-select v-model="tempTaskMain.toCom" @change="selectComChange" multiple placeholder="请选择">
            <el-option v-for="item in optionsToCom" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="接收人" prop="toUser">

          <el-option-group v-for="group in optionsToUser" :key="group.label" :label="group.label">
            <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-option-group>

          <el-select v-model="tempTaskMain.toUser" multiple collapse-tags placeholder="请选择">
            <el-option v-for="item in optionsToUser" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="任务号" prop="code" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskMain.code"></el-input>
        </el-form-item> -->
        <el-form-item label="任务名" prop="name" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskMain.name"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="tempTaskMain.type">
            <el-radio :label="1">日报</el-radio>
            <el-radio :label="2">周报</el-radio>
            <el-radio :label="3">年报</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="模板" prop="tempId" required>
          <el-select v-model="tempTaskMain.tempId" collapse-tags placeholder="请选择">
            <el-option v-for="item in optionsTempId" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="截至日期" prop="endTime" required>
          <el-date-picker v-model="tempTaskMain.endTime" type="date" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogTaskMainFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus === 'add'" type="success" :loading="btnLoading"
          @click.native.prevent="addTaskMain">添加</el-button>
        <el-button v-if="dialogStatus === 'update'" type="primary" :loading="btnLoading"
          @click.native.prevent="updateRole">更新</el-button>
      </div>
    </el-dialog>
    <!-- 任务添加结束 -->



  </div>
</template>
<script>
import {
  list as getTaskMainList,
  add as addTaskMain,
  resetPassword,
  remove,
  cmd as toCmd
} from '@/api/taskMain'
import {
  list as getcompaniesList,
} from '@/api/companies'

import {
  list as getTemplatesList,
} from '@/api/templates'
import { search as searchUser } from '@/api/account'
import {
  listRoleWithPermission,
  listResourcePermission,
  add as addRole,
  update as updateRole,
} from '@/api/role'
import { unix2CurrentTime } from '@/utils'
import { mapGetters } from 'vuex'
import { mapState } from 'vuex'
export default {
  created() {
    // if (this.hasPermission('taskMain:update')) {
    //   this.getPermissionList()
    // }
    // if (this.hasPermission('taskMain:list')) {
    this.getTaskMainList()
    this.getcompaniesList()
    this.getTemplatesList()
    // }
  },
  data() {
    /**
     * 验证任务名
     * @param rule 规则
     * @param callback 回调
     */
    const validateTaskMainName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('任务名不能为空'))
      } else {
        callback()
      }
    }
    /**
     * 验证任务号
     * @param rule 规则
     * @param callback 回调
     */
    const validateTaskMainCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('任务号不能为空'))
      } else {
        callback()
      }
    }
    return {
      searchUserTemp: {
        page: 1,
        size: 999,
        comId: null,
      },
      templatesList: [],
      taskMainList: [],
      permissionList: [],
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        size: 9
      },
      listComQuery: {
        page: 1,
        size: 999
      },
      listTempQuery: {
        page: 1,
        size: 999
      },
      dialogStatus: 'add',
      dialogFormVisible: false,
      showUploadDialog: false,
      dialogTaskMainFormVisible: false,
      textMap: {
        reset: '重置密码',
        add: '添加任务'
      },
      btnLoading: false,
      tempTaskMain: {
        id: '',
        name: '',
      },
      createRules: {
        name: [{ required: true, trigger: 'blur', validator: validateTaskMainName }],
        code: [{ required: true, trigger: 'blur', validator: validateTaskMainCode }]
      },
      optionsToCom: [

      ],
      optionsToUser: [{
        label: "",
        options: []
      }
      ],
      optionsTempId: [

      ],
      companiesList: [],
      userList: []
    }
  },
  computed: {
    ...mapGetters(['roleName']),
    ...mapState({
      account: state => state.account
    })
  },
  methods: {
    unix2CurrentTime,
    toCmdTaskDialog(e){
      console.log(e)

      toCmd(e).then(() => {
            this.$message.success('下发成功')
            this.getTaskMainList()
            this.dialogTaskMainFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('下发任务失败')
          })
    },
    /**
 * 根据公司获取用户
 */
    // searchByUser(com) {

    //   com.forEach(co => {

    //     this.btnLoading = true
    //     this.listLoading = true
    //     this.searchUserTemp.comId = co.id


    //     searchUser(this.searchUserTemp).then(response => {
    //       if (response.data.list.length > 0) {
    //         this.userList = response.data.list
    //         this.optionsToUser = []
    //         let toUserList = []
    //         var toUser = {}
    //         toUser.label = com.name
    //         console.log(toUser+"toUser")
    //         this.userList.forEach(element => {
    //           let opt = {}
    //           opt.label = element.name
    //           opt.value = element.id
    //           toUser.options.push(opt)
    //         });
    //       }
    //       console.log(toUser+"toUser")

    //       this.optionsToUser.push(toUser)
    //       this.listLoading = false
    //       this.btnLoading = false
    //     }).catch(res => {
    //       this.listLoading = false
    //       this.btnLoading = false
    //       this.$message.error('搜索用户失败' + res)
    //     })

    //   });
    //   console.log( this.optionsToUser)
    // },
    selectComChange(e) {
      console.log("选择变化", e)
      // let com = []
      // e.forEach(el => {
      //   this.companiesList.forEach(element => {
      //     if (element.id == el) {
      //       com.push(element)
      //     }
      //   })
      // });
      // console.log(com)
      // this.searchByUser(com)
    },
    /**
    * 获取x模板列表
    */
    getTemplatesList() {
      this.listLoading = true
      getTemplatesList(this.listTempQuery).then(response => {
        this.templatesList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载模板列表失败')
      })
    },

    // /**
    //  * 根据公司获取用户
    //  */
    //  getUserByCompanies(e) {
    //   this.listLoading = true
    //   getUserByCompanies(e).then(response => {
    //     this.userList = response.data
    //     this.listLoading = false
    //   }).catch(res => {
    //     this.$message.error('根据公司获取用户失败')
    //   })
    // },

    /**
    * 获取公司列表
    */
    getcompaniesList() {
      this.listLoading = true
      getcompaniesList(this.listComQuery).then(response => {
        this.companiesList = response.data.list
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载公司列表失败')
      })
    },

    /**
     * 获取x任务列表
     */
    getTaskMainList() {
      this.listLoading = true
      getTaskMainList(this.listQuery).then(response => {
        this.taskMainList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载任务列表失败')
      })
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.listQuery.page = 1
      this.listQuery.size = size
      this.getTaskMainList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.listQuery.page = page
      this.getTaskMainList()
    },
    /**
     * 表格序号
     * @param index 数据下标
     * @returns 表格序号
     */
    getTableIndex(index) {
      return (this.listQuery.page - 1) * this.listQuery.size + index + 1
    },
    /**
     * 显示新增任务对话框
     */
    showAddRoleDialog() {
      this.dialogTaskMainFormVisible = true
      this.dialogStatus = 'add'
      this.tempTaskMain.fromUser = this.account.accountId
      this.optionsToCom = []
      this.optionsToUser = []
      this.optionsTempId = []
      this.companiesList.forEach(element => {
        let tmpCom = {}
        tmpCom.value = element.id
        tmpCom.label = element.name
        this.optionsToCom.push(tmpCom)
      });

      this.templatesList.forEach(element => {
        let tmpTemp = {}
        tmpTemp.value = element.id
        tmpTemp.label = element.name
        this.optionsTempId.push(tmpTemp)
      });
    },
    downTmp() {
      window.location.href = process.env.BASE_API + '/taskMain/exportTemp'
    },
    /**
     * 添加任务
     */
    addTaskMain() {
      this.$refs.tempTaskMain.validate(valid => {
        if (
          valid &&
          this.isTaskMainNameUnique(this.tempTaskMain.id)
        ) {
          this.btnLoading = true
          this.tempTaskMain.toCom = this.tempTaskMain.toCom.toString()
          addTaskMain(this.tempTaskMain).then(() => {
            this.$message.success('添加成功')
            this.getTaskMainList()
            this.dialogTaskMainFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加任务失败')
          })
        } else {
          console.log('表单无效')
        }
      })
    },
    // /**
    //  * 修改角色
    //  */
    // updateRole() {
    //   this.$refs.tempTaskMain.validate(valid => {
    //     if (
    //       valid &&
    //       this.isRoleNameUnique(this.tempTaskMain.id, this.tempTaskMain.name)
    //     ) {
    //       this.btnLoading = true
    //       updateRole(this.tempTaskMain).then(() => {
    //         this.$message.success('更新成功')
    //         this.getTaskMainList()
    //         this.dialogFormVisible = false
    //         this.btnLoading = false
    //       }).catch(res => {
    //         this.$message.error('更新角色失败')
    //       })
    //     } else {
    //       console.log('表单无效')
    //     }
    //   })
    // },
    /**
     * 重置密码
     * @param {Object} id
     */
    resetPassword(id) {
      resetPassword(id).then(() => {
        this.$message.success('重置密码成功')
        this.getTaskMainList()
        this.btnLoading = false
      }).catch(res => {
        this.$message.error('重置密码失败')
      })
    },

    /**
     * 校验任务号是否已经存在
     * @param id 任务id
     * @param code 任务号
     * @returns {boolean}
     */
    isTaskMainNameUnique(id, code) {
      for (let i = 0; i < this.taskMainList.length; i++) {
        if (this.taskMainList[i].id == id) {
          this.$message.error('任务号已存在')
          return false
        }
      }
      return true
    },
    /**
     * 移除任务
     * @param id 任务id
     * @returns {boolean}
     */
    removeRole(id) {
      this.$confirm('删除该角色？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const taskMainId = id
        remove(taskMainId).then(() => {
          this.$message.success('删除成功')
          this.getTaskMainList()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * 判断角色菜单内的权限是否一个都没选
     * @param index 下标
     * @returns {boolean}
     */
    isMenuNone(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTaskMain.permissionIdList.indexOf(handleList[i].id) > -1) {
          return false
        }
      }
      return true
    },
    /**
     * 判断角色菜单内的权限是否全选了
     * @param index 下标
     * @returns {boolean}
     */
    isMenuAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTaskMain.permissionIdList.indexOf(handleList[i].id) < 0) {
          return false
        }
      }
      return true
    },
    /**
     * 根据菜单状态check所有checkbox
     * @param index 下标
     */
    checkAll(index) {
      if (this.isMenuAll(index)) {
        // 如果已经全选了,则全部取消
        this.cancelAll(index)
      } else {
        // 如果尚未全选,则全选
        this.selectAll(index)
      }
    },
    /**
     * checkbox全部选中
     * @param index 下标
     */
    selectAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        this.addUnique(handleList[i].id, this.tempTaskMain.permissionIdList)
      }
    },
    /**
     * checkbox全部取消选中
     * @param index 下标
     */
    cancelAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        const idIndex = this.tempTaskMain.permissionIdList.indexOf(handleList[i].id)
        if (idIndex > -1) {
          this.tempTaskMain.permissionIdList.splice(idIndex, 1)
        }
      }
    },
    /**
     * 本方法会在勾选状态改变之后触发
     * @param item 选项
     * @param index 对应下标
     */
    handleChecked(item, index) {
      if (this.tempTaskMain.permissionIdList.indexOf(item.id) > -1) {
        // 选中事件
        // 如果之前未勾选本权限,现在勾选完之后,tempTaskMain里就会包含本id
        // 那么就要将必选的权限勾上
        this.makePermissionChecked(index)
      } else {
        // 取消选中事件
        this.cancelAll(index)
      }
    },
    /**
     * 将角色菜单必选的权限勾上（这里并没有做必选的数据库字段）
     * @param index 权限对应下标
     */
    makePermissionChecked(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        this.addUnique(handleList[i].id, this.tempTaskMain.permissionIdList)
      }
    },
    /**
     * 数组内防重复地添加元素
     * @param val 值
     * @param arr 数组
     */
    addUnique(val, arr) {
      const _index = arr.indexOf(val)
      if (_index < 0) {
        arr.push(val)
      }
    }
  }
}
</script>
