<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button type="success" size="mini" icon="el-icon-refresh" v-if="hasPermission('templates:list')"
            @click.native.prevent="getTemplatesList">刷新</el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" v-if="hasPermission('templates:add')"
            @click.native.prevent="showAddTempDialog">添加模板</el-button>

          <!-- <el-button type="primary" size="mini" icon="el-icon-download" v-if="hasPermission('templates:list')"
            @click.native.prevent="downTmp">下载模板</el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" v-if="hasPermission('templates:list')"
            @click.native.prevent="showUploadDialog = true">一键导入</el-button> -->
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="templatesList" v-loading.body="listLoading" element-loading-text="loading" border fit
      highlight-current-row>
      <el-table-column label="#" align="center" prop="id" width="80">
      </el-table-column>
      <el-table-column label="模板名" align="center" prop="name" />
      <el-table-column label="模板地址" align="center" prop="fileUrl" />
      <el-table-column label="模板类型" align="center" prop="type">

        <template slot-scope="scope">

          <el-tag v-if="scope.row.type === 1" type="success" disable-transitions>ecxel</el-tag>

          <el-button v-if="scope.row.type === 0" size="mini" @click.native.prevent="showTestsDialog(scope.row)"
            disable-transitions>xls</el-button>
        </template>
      </el-table-column>
      <el-table-column label="模板号" align="center" prop="id">
      </el-table-column>
      <!-- <el-table-column label="模板状态" align="center" prop="status">

        <template slot-scope="scope">

          <el-tag v-if="scope.row.status === 1" type="success" disable-transitions>已发布</el-tag>

          <el-button v-if="scope.row.status === 0" size="mini" @click.native.prevent="showTestsDialog(scope.row)"
            disable-transitions>未发布（点击发布）</el-button>
        </template>
      </el-table-column> -->
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="管理" align="center"
        v-if="hasPermission('templates:detail') || hasPermission('templates:update') || hasPermission('templates:delete')">
        <template slot-scope="scope">
          <!-- <el-button
            type="info"
            size="mini"
            v-if="hasPermission('templates:detail')"
            @click.native.prevent="showRoleDialog(scope.$index)"
          >查看</el-button> -->
          <el-button type="warning" size="mini" v-if="hasPermission('templates:reset')"
            @click.native.prevent="resetPassword(scope.row.id)">重置密码</el-button>
          <el-button type="danger" size="mini" v-if="hasPermission('templates:delete')"
            @click.native.prevent="removeRole(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
      :page-size="listQuery.size" :total="total" :page-sizes="[9, 18, 36, 72]"
      layout="total, sizes, prev, pager, next, jumper"></el-pagination>

    <!-- 模板添加开始 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogTemplatesFormVisible">
      <el-form status-icon class="small-space" label-position="left" label-width="100px"
        style="width: 500px; margin-left:50px;" :model="tempTemplates" :rules="createRules" ref="tempTemplates">
        <el-form-item label="模板名称" prop="name" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTemplates.name"></el-input>
        </el-form-item>
        <!-- <el-form-item label="接收人" prop="code" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTemplates.code"></el-input>
        </el-form-item> -->
        <el-form-item label="模板类型" prop="type" required>
          <el-select v-model="tempTemplates.type" clearable auto-complete="off" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板名称" prop="name" required>
          <el-upload class="upload-demo" drag action="http://localhost:8080/templates/upload" :on-success="sucessUplod"
            multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件，且不超过500kb</div>
          </el-upload>

        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogTemplatesFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus === 'add'" type="success" :loading="btnLoading"
          @click.native.prevent="addTemplates">添加</el-button>
        <el-button v-if="dialogStatus === 'update'" type="primary" :loading="btnLoading"
          @click.native.prevent="updateRole">更新</el-button>
      </div>
    </el-dialog>
    <!-- 模板添加结束 -->



  </div>
</template>
<script>
import {
  list as getTemplatesList,
  add as addTemplates,
  resetPassword,
  remove,
} from '@/api/templates'
import {
  listRoleWithPermission,
  listResourcePermission,
  add as addRole,
  update as updateRole,
} from '@/api/role'
import { unix2CurrentTime } from '@/utils'
import { mapGetters } from 'vuex'

export default {
  created() {
    // if (this.hasPermission('templates:update')) {
    //   this.getPermissionList()
    // }
    // if (this.hasPermission('templates:list')) {
    this.getTemplatesList()
    // }
  },
  data() {
    /**
     * 验证模板名
     * @param rule 规则
     * @param callback 回调
     */
    const validateTemplatesName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('模板名不能为空'))
      } else {
        callback()
      }
    }
    /**
     * 验证学号
     * @param rule 规则
     * @param callback 回调
     */
    const validateTemplatesCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('学号不能为空'))
      } else {
        callback()
      }
    }
    return {
      templatesList: [],
      permissionList: [],
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        size: 9
      },
      dialogStatus: 'add',
      dialogFormVisible: false,
      showUploadDialog: false,
      dialogTemplatesFormVisible: false,
      textMap: {
        reset: '重置密码',
        add: '添加模板'
      },
      btnLoading: false,
      tempTemplates: {
        name: '',
        fileUrl: '',
        type: '',
      },
      createRules: {
        name: [{ required: true, trigger: 'blur', validator: validateTemplatesName }],
      },
      options: [{
          value: 1,
          label: "xls/xlsx"
        }],
        testName:"",
        
    }
  },
  computed: {
    ...mapGetters(['roleName'])
  },
  methods: {
    unix2CurrentTime,

    /**
     * 上传回调
     */
    sucessUplod(file) {
      console.log(file.data, "file name")
      if (file.code != 200){
        this.$notify({
          title: '警告',
          message: '文件格式不正确',
          type: 'warning'
        });
      }else{
        this.tempTemplates.fileUrl = file.data
      }
      
    },

    /**
     * 获取x模板列表
     */
    getTemplatesList() {
      this.listLoading = true
      getTemplatesList(this.listQuery).then(response => {
        this.templatesList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载模板列表失败')
      })
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.listQuery.page = 1
      this.listQuery.size = size
      this.getTemplatesList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.listQuery.page = page
      this.getTemplatesList()
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
     * 显示新增模板对话框
     */
    showAddTempDialog() {
      this.dialogStatus = 'add';
      this.tempTemplates.name = ''
      this.tempTemplates.fileUrl = ''
      this.tempTemplates.type = ''
      this.dialogTemplatesFormVisible = true
    },
    downTmp() {
      window.location.href = process.env.BASE_API + '/templates/exportTemp'
    },
    /**
     * 添加模板
     */
    addTemplates() {
      this.$refs.tempTemplates.validate(valid => {
        if (
          valid &&
          this.isTemplatesNameUnique(this.tempTemplates.id, this.tempTemplates.name)
        ) {
          this.btnLoading = true
          addTemplates(this.tempTemplates).then(() => {
            this.$message.success('添加成功')
            this.getTemplatesList()
            this.dialogTemplatesFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加模板失败')
          })
        } else {
          console.log('表单无效')
        }
      })
    },
    // /**
    //  * 修改模板
    //  */
    // updateRole() {
    //   this.$refs.tempTemplates.validate(valid => {
    //     if (
    //       valid &&
    //       this.isRoleNameUnique(this.tempTemplates.id, this.tempTemplates.name)
    //     ) {
    //       this.btnLoading = true
    //       updateRole(this.tempTemplates).then(() => {
    //         this.$message.success('更新成功')
    //         this.getTemplatesList()
    //         this.dialogFormVisible = false
    //         this.btnLoading = false
    //       }).catch(res => {
    //         this.$message.error('更新模板失败')
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
        this.getTemplatesList()
        this.btnLoading = false
      }).catch(res => {
        this.$message.error('重置密码失败')
      })
    },

    /**
     * 校验学号是否已经存在
     * @param id 模板id
     * @param name 学号
     * @returns {boolean}
     */
    isTemplatesNameUnique(id, name) {
      for (let i = 0; i < this.templatesList.length; i++) {
        if (this.templatesList[i].id !== id && this.templatesList[i].name === name) {
          this.$message.error('name已存在')
          return false
        }
      }
      return true
    },
    /**
     * 移除模板
     * @param id 模板id
     * @returns {boolean}
     */
    removeRole(id) {
      this.$confirm('删除该模板？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const templatesId = id
        remove(templatesId).then(() => {
          this.$message.success('删除成功')
          this.getTemplatesList()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * 判断模板菜单内的权限是否一个都没选
     * @param index 下标
     * @returns {boolean}
     */
    isMenuNone(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTemplates.permissionIdList.indexOf(handleList[i].id) > -1) {
          return false
        }
      }
      return true
    },
    /**
     * 判断模板菜单内的权限是否全选了
     * @param index 下标
     * @returns {boolean}
     */
    isMenuAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTemplates.permissionIdList.indexOf(handleList[i].id) < 0) {
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
        this.addUnique(handleList[i].id, this.tempTemplates.permissionIdList)
      }
    },
    /**
     * checkbox全部取消选中
     * @param index 下标
     */
    cancelAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        const idIndex = this.tempTemplates.permissionIdList.indexOf(handleList[i].id)
        if (idIndex > -1) {
          this.tempTemplates.permissionIdList.splice(idIndex, 1)
        }
      }
    },
    /**
     * 本方法会在勾选状态改变之后触发
     * @param item 选项
     * @param index 对应下标
     */
    handleChecked(item, index) {
      if (this.tempTemplates.permissionIdList.indexOf(item.id) > -1) {
        // 选中事件
        // 如果之前未勾选本权限,现在勾选完之后,tempTemplates里就会包含本id
        // 那么就要将必选的权限勾上
        this.makePermissionChecked(index)
      } else {
        // 取消选中事件
        this.cancelAll(index)
      }
    },
    /**
     * 将模板菜单必选的权限勾上（这里并没有做必选的数据库字段）
     * @param index 权限对应下标
     */
    makePermissionChecked(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        this.addUnique(handleList[i].id, this.tempTemplates.permissionIdList)
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
