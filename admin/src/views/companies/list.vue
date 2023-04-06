<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('companies:list')"
            @click.native.prevent="getcompaniesList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('companies:add')"
            @click.native.prevent="showAddRoleDialog"
          >添加公司</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="companiesList"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="#" align="center" prop="id" width="80">
      </el-table-column>
      <el-table-column label="公司代码" align="center" prop="comNo" />
      <el-table-column label="公司名" align="center" prop="name" />
      <el-table-column label="联系人" align="center" prop="userName" />
      <el-table-column label="联系电话" align="center" prop="userPhone" />
      <!-- <el-table-column label="创建时间" align="center" prop="time">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.time) }}</template>
      </el-table-column> -->
      <el-table-column
        label="管理"
        align="center"
        v-if="hasPermission('companies:detail') || hasPermission('companies:update') || hasPermission('companies:delete')"
      >
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('companies:delete') && scope.row.name !== '超级管理员'"
            @click.native.prevent="removecompanies(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.page"
      :page-size="listQuery.size"
      :total="total"
      :page-sizes="[9, 18, 36, 72]"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>

    <!-- 添加公司开始 -->
    <el-dialog :title="textMap[dialogStatus]"  :visible.sync="dialogcompaniesFormVisible">


          <el-form
            status-icon
            class="small-space"
            label-position="left"
            label-width="100px"
            style="width: 500px; margin-left:50px;"
            :model="tempcompanies"
            :rules="createRules"
            ref="tempcompanies"
          >
          <el-form-item label="公司代号" prop="comNo" required>
              <el-input
                :disabled="dialogStatus === 'show'"
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                v-model="tempcompanies.comNo"
              ></el-input>
            </el-form-item>
            <el-form-item label="公司名" prop="name" required>
              <el-input
                :disabled="dialogStatus === 'show'"
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                v-model="tempcompanies.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="联系人" prop="userName" required>
              <el-input
                :disabled="dialogStatus === 'show'"
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                v-model="tempcompanies.userName"
              ></el-input>
            </el-form-item>
            <el-form-item label="联系方式" prop="userPhone" required>
              <el-input
                :disabled="dialogStatus === 'show'"
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                v-model="tempcompanies.userPhone"
              ></el-input>
            </el-form-item>
            <!-- <el-form-item label="课程名" prop="course" required>
              <el-input
                :disabled="dialogStatus === 'show'"
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                v-model="tempcompanies.course"
              ></el-input>
            </el-form-item> -->
          </el-form>


      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogcompaniesFormVisible = false">取消</el-button>
        <el-button
          v-if="dialogStatus === 'add'"
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addcompanies"
        >添加</el-button>
        </div>
    </el-dialog>
    <!-- 生成二维码结束 -->
  </div>
</template>
<script>
import {
  list as getcompaniesList,
  getQRCode,
  add as addcompanies,
  remove
} from '@/api/companies'
import {
  add as addRole,
  update as updateRole,
} from '@/api/role'
import { unix2CurrentTime } from '@/utils'
import { mapGetters } from 'vuex'

export default {
  created() {
    // if (this.hasPermission('companies:update')) {
    //   this.getPermissionList()
    // }
    // if (this.hasPermission('companies:list')) {
      this.getcompaniesList()
    // }
  },
  data() {
    /**
     * 验证公司
     * @param rule 规则
     * @param value 公司名
     * @param callback 回调
     */
    const validateRoleName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('公司名不能为空'))
      } else {
        callback()
      }
    }
    return {
      src: 'http://localhost:8080/QRImage/1619958783025.png',
      companiesList: [],
      permissionList: [],
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        size: 9
      },
      params:{
        companiesId:1,
      },
      dialogStatus: 'add',
      dialogFormVisible: false,
      dialogcompaniesFormVisible:false,
      textMap: {
        add: '添加公司',
        qr: '生成二维码'
      },
      btnLoading: false,
      tempcompanies: {
        name: '',
        course:''
      },
      createRules: {
        name: [{ required: true, trigger: 'blur', validator: validateRoleName }]
      }
    }
  },
  computed: {
    ...mapGetters(['roleName'])
  },
  methods: {
    unix2CurrentTime,
    /**
     * 获取公司列表
     */
    getcompaniesList() {
      this.listLoading = true
      getcompaniesList(this.listQuery).then(response => {
        this.companiesList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载公司列表失败')
      })
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.listQuery.page = 1
      this.listQuery.size = size
      this.getcompaniesList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.listQuery.page = page
      this.getcompaniesList()
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
     * 显示添加公司对话框
     */
    showAddRoleDialog() {
      this.dialogcompaniesFormVisible = true
      this.dialogStatus = 'add'
      this.tempcompanies.name = ''
      this.tempcompanies.course = ''
    },
    /**
     * 显示更新角色的对话框
     * @param index 角色下标
     */
    // showUpdateRoleDialog(index) {
    //   this.dialogFormVisible = true
    //   this.dialogStatus = 'update'
    //   const role = this.roleList[index]
    //   this.tempRole.name = role.name
    //   this.tempRole.id = role.id
    //   this.tempRole.permissionIdList = []
    //   for (let i = 0; i < role.resourceList.length; i++) {
    //     const handleList = role.resourceList[i].handleList
    //     for (let j = 0; j < handleList.length; j++) {
    //       const handle = handleList[j]
    //       this.tempRole.permissionIdList.push(handle.id)
    //     }
    //   }
    // },
    /**
     * 生成二维码
     * @param id 公司id
     */
    showQRDialog(id) {
      //this.$message.success("ttttttt"+id)
      this.dialogStatus = 'qr'
      this.params.companiesId = id
      getQRCode(this.params).then(res=>{
        this.src = "http://localhost:8080/QRImage/"+res.data.url
      }).catch(res => {
        this.$message.error('生成二维码失败')
      })
      this.dialogFormVisible = true
      this.dialogStatus = 'show'

    },
    /**
     * 添加新公司
     */
    addcompanies() {
      this.$refs.tempcompanies.validate(valid => {
        if (
          valid &&
          this.iscompaniesNameUnique(this.tempcompanies.id, this.tempcompanies.name)
        ) {
          this.btnLoading = true
          addcompanies(this.tempcompanies).then(() => {
            this.$message.success('添加成功')
            this.getcompaniesList()
            this.dialogcompaniesFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加公司失败')
          })
        } else {
          console.log('表单无效')
        }
      })
    },
    /**
     * 修改角色
     */
    // updateRole() {
    //   this.$refs.tempRole.validate(valid => {
    //     if (
    //       valid &&
    //       this.isRoleNameUnique(this.tempRole.id, this.tempRole.name)
    //     ) {
    //       this.btnLoading = true
    //       updateRole(this.tempRole).then(() => {
    //         this.$message.success('更新成功')
    //         this.getcompaniesList()
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
     * 校验公司名是否已经存在
     * @param id 公司id
     * @param name 公司名
     * @returns {boolean}
     */
    iscompaniesNameUnique(id, name) {
      for (let i = 0; i < this.companiesList.length; i++) {
        if (this.companiesList[i].id !== id && this.companiesList[i].name === name) {
          this.$message.error('角色名已存在')
          return false
        }
      }
      return true
    },
    /**
     * 移除公司
     * @returns {boolean}
     */
    removecompanies(id) {
      this.$confirm('删除该公司？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const companiesId = id
        remove(companiesId).then(() => {
          this.$message.success('删除成功')
          this.getcompaniesList()
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
        if (this.tempRole.permissionIdList.indexOf(handleList[i].id) > -1) {
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
        if (this.tempRole.permissionIdList.indexOf(handleList[i].id) < 0) {
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
        this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
      }
    },
    /**
     * checkbox全部取消选中
     * @param index 下标
     */
    cancelAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        const idIndex = this.tempRole.permissionIdList.indexOf(handleList[i].id)
        if (idIndex > -1) {
          this.tempRole.permissionIdList.splice(idIndex, 1)
        }
      }
    },
    /**
     * 本方法会在勾选状态改变之后触发
     * @param item 选项
     * @param index 对应下标
     */
    handleChecked(item, index) {
      if (this.tempRole.permissionIdList.indexOf(item.id) > -1) {
        // 选中事件
        // 如果之前未勾选本权限,现在勾选完之后,tempRole里就会包含本id
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
        this.addUnique(handleList[i].id, this.tempRole.permissionIdList)
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

<style rel="stylesheet/scss" lang="scss" scoped>
.QRCode{
  text-align: center;
  //margin-top: 38px;
  margin-left: 38px;
  margin-bottom: 20px;
  width: 360px;
   height: 300px;

}
</style>
