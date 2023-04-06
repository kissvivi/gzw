<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('taskDetail:list')"
            @click.native.prevent="getaskDetailList"
          >刷新</el-button>
          <!-- <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('taskDetail:add')"
            @click.native.prevent="showAddtaskDetailDialog"
          >添加题目</el-button>
</el-form-item>
<el-form-item class="submit-templates">
          <el-button

            type="warning"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('taskDetail:add')"

            @click.native.prevent="showAddtemplatesDialog"
          >组卷/生成问卷</el-button> -->
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="taskDetailList"
     @selection-change="handleSelectionChange"
      ref="multipleTable"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
    <el-table-column
          type="selection"
          width="55">
        </el-table-column>
      <el-table-column label="#" align="center" prop="id" width="80">
      </el-table-column>
      <el-table-column label="任务名" align="center" prop="name" />
      <el-table-column :filters="[{ text: '未填报', value: 1 },{ text: '已填报', value: 1 }, { text: '已过期', value: 2 }]" :filter-method="filterType" label="状态" align="center" >
      <template slot-scope="scope" >
        <el-tag v-if="scope.row.status==1" type="success">{{ getTypeName(scope.row.status) }}</el-tag>
        <el-tag v-if="scope.row.status==0" type="warning">{{ getTypeName(scope.row.status) }}</el-tag>
      </template>
      </el-table-column>
      <el-table-column label="填写企业" align="center" prop="comId" />
      <el-table-column label="实际填写人" align="center" prop="userId" />
      <el-table-column label="文件地址" align="center" prop="fileUrl" />
      <el-table-column label="上传时间" align="center" prop="uploadTime">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.uploadTime) }}</template>
      </el-table-column>
      <el-table-column
        label="管理"
        align="center"
        v-if="hasPermission('taskDetail:detail') || hasPermission('taskDetail:update') || hasPermission('taskDetail:delete')"
      >
        <template slot-scope="scope">
           <el-button
            type="info"
            size="mini"
            v-if="hasPermission('taskDetail:download')"
            @click.native.prevent="downloadTemp(scope.row)"
          >下载模板</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('taskDetail:upload')"
            @click.native.prevent="showUploadDialog(scope.row)"
          >上传文件</el-button>
          <!--<el-button
            type="warning"
            size="mini"
            v-if="hasPermission('taskDetail:update')"
            @click.native.prevent="showUpdateRoleDialog(scope.$index)"
          >修改</el-button> -->
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('taskDetail:delete')"
            @click.native.prevent="removetaskDetail(scope.row.id)"
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

    <!-- <div class="submit-templates">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('taskDetail:add')"
            @click.native.prevent="showAddtaskDetailDialog"
          >组卷/生成调查问卷</el-button>
        </el-form-item>
      </el-form>
    </div> -->

    
    <!-- 文件上传开始 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogTaskDetailFormVisible">
      <el-form status-icon class="small-space" label-position="left" label-width="100px"
        style="width: 500px; margin-left:50px;" :model="tempTaskDetail" :rules="createRules" ref="tempTaskDetail">
        <el-form-item label="任务名称" prop="name" :disabled="true" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskDetail.name"></el-input>
        </el-form-item>
        <el-form-item label="填写企业" prop="comId" :disabled="true" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskDetail.comId"></el-input>
        </el-form-item>
        <el-form-item label="实际填写人" prop="userId" :disabled="true" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskDetail.userId"></el-input>
        </el-form-item>
        <!-- <el-form-item label="接收人" prop="code" required>
          <el-input :disabled="dialogStatus === 'show'" type="text" prefix-icon="el-icon-edit" auto-complete="off"
            v-model="tempTaskDetail.code"></el-input>
        </el-form-item> -->
        <el-form-item label="文件上传" prop="name" required>
          <el-upload class="upload-demo" drag action="http://localhost:8080/templates/upload" :on-success="sucessUplod"
            multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件，且不超过500kb</div>
          </el-upload>

        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogTaskDetailFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus === 'add'" type="success" :loading="btnLoading"
          @click.native.prevent="addTaskDetail">添加</el-button>
        <el-button v-if="dialogStatus === 'update'" type="primary" :loading="btnLoading"
          @click.native.prevent="updateTaskDetail">更新</el-button>
      </div>
    </el-dialog>
    <!-- 文件上传结束 -->

  </div>
</template>
<script>
  import {
    list as gettaskDetailList,
    add as addtaskDetail,
    remove,
    update as updateTaskDetail
  } from '@/api/taskDetail'
  import {
    add as addtemplates
  } from '@/api/templates'
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
    // if (this.hasPermission('taskDetail:update')) {
    //   this.getPermissionList()
    // }
    // if (this.hasPermission('taskDetail:list')) {
      this.getaskDetailList()
    // }
  },
  data() {
    /**
     * 验证任务详情名
     * @param rule 规则
     * @param value 任务详情名
     * @param callback 回调
     */
    // const validateRoleName = (rule, value, callback) => {
    //   if (value === '') {
    //     callback(new Error('任务详情名不能为空'))
    //   } else {
    //     callback()
    //   }
    // }
    return {
      dialogTaskDetailFormVisible:false,
      multipleSelection:[],
      options: [{
                value: 101,
                label: '试题'
              }, {
                value: 102,
                label: '问卷调查'
              }],
      taskDetailList: [],
      permissionList: [],
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        size: 9
      },
      dialogStatus: 'add',
      dialogFormVisible: false,
      dialogtemplatesFormVisible: false,
      textMap: {
        add: '添加题目',
        addtemplates: '添加考试/问卷'
      },
      btnLoading: false,
      temptemplates:{},
      tempTaskDetail: {
        id: '',
        name: '',
      },
      // createRules: {
      //   name: [{ required: true, trigger: 'blur', validator: validateRoleName }]
      // }
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
        this.tempTaskDetail.fileUrl = file.data
      }
      
    },
    downloadTemp(e){
      window.location.href="http://localhost:8080/templates/download/"+e.taskId
      console.log(e)
    },

    /**
     * 显示上传对话框
     */
     showUploadDialog(e) {
      console.log(e)
      this.dialogStatus = 'update';
      this.tempTaskDetail.name = e.name
      this.tempTaskDetail.id = e.id
      this.tempTaskDetail.fileUrl = ''
      this.tempTaskDetail.comId = e.comId
      this.tempTaskDetail.status = 1
      this.tempTaskDetail.userId = this.account.accountId
      this.dialogTaskDetailFormVisible = true
    },
    /**
     * 根据code 得到类型
     * @param {Object} type
     */
    getTypeName(type){
      if(type == 0)
      return '未填报'
      else if (type == 1)
      return '已填报'
      else
      return '已过期'
    },
    /**
     * 获取任务详情列表
     */
    getaskDetailList() {
      this.listLoading = true
      gettaskDetailList(this.listQuery).then(response => {
        this.taskDetailList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载任务详情列表失败')
      })
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.listQuery.page = 1
      this.listQuery.size = size
      this.getaskDetailList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.listQuery.page = page
      this.getaskDetailList()
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
     * 显示新增题目对话框
     */
    showAddtaskDetailDialog() {
      this.dialogFormVisible = true
      this.dialogStatus = 'add'
      // this.tempTaskDetail.name = ''
      // this.tempTaskDetail.id = ''
    },

    /**
     * 显示新增考试
     */
    showAddtemplatesDialog() {
      this.dialogtemplatesFormVisible = true
      this.dialogStatus = 'addtemplates'

      // const _selectData = this.$refs.multipleTable.selection

      // console.log("_selectData："+this.$refs.multipleTable.selection.title)

      // this.$message.success('_selectData:'+_selectData.title+_selectData.id)

      // this.tempTaskDetail.name = ''
      // this.tempTaskDetail.id = ''
    },

//监听选择器变化
    handleSelectionChange(val) {
        this.multipleSelection = val;
        console.log('multipleSelection:'+val[0].title)
        this.temptemplates.tbIds =[]
        val.forEach(row => {
              if (row) {
                this.temptemplates.tbIds.push(row.id)
              }
        })
        this.temptemplates.tbIds = String(this.temptemplates.tbIds)
        },

    //过滤类型
     filterType(value, row) {
            console.log("value:"+value)
            //console.log("row.type:"+row.type)
            return row.type === value;
          },
    /**
     * 添加题目
     */
    addtaskDetail() {

      //this.$message.success('添加成功' +this.tempTaskDetail.type )

      this.$refs.tempTaskDetail.validate(valid => {
        // if (
        //   valid &&
        //   this.isRoleNameUnique(this.tempTaskDetail.id, this.tempTaskDetail.name)
        // ) {
          this.btnLoading = true
          addtaskDetail(this.tempTaskDetail).then(() => {
            this.$message.success('添加成功')
            this.getaskDetailList()
            this.dialogFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加题目失败')
          })
        // } else {
        //   console.log('表单无效')
        // }
      })
    },

    addtemplates(){
      addtemplates(this.temptemplates).then(() => {
        this.$message.success('添加成功')
        this.getaskDetailList()
        this.dialogtemplatesFormVisible = false
        this.btnLoading = false
      }).catch(res => {
        this.$message.error('添加考试失败')
      })


    },

    /**
     * 上传文件
     */
    updateTaskDetail() {
      this.$refs.tempTaskDetail.validate(valid => {
          this.btnLoading = true
          updateTaskDetail(this.tempTaskDetail).then(() => {
            this.$message.success('更新成功')
            this.getaskDetailList()
            this.dialogTaskDetailFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('更新任务详情失败')
          })
      })
    },
    /**
     * 校验任务详情名是否已经存在
     * @param id 任务详情id
     * @param name 任务详情名
     * @returns {boolean}
     */
    isRoleNameUnique(id, name) {
      for (let i = 0; i < this.taskDetailList.length; i++) {
        if (this.taskDetailList[i].id !== id && this.taskDetailList[i].name === name) {
          this.$message.error('题目名已存在')
          return false
        }
      }
      return true
    },
    /**
     * 移除题目
     * @param index 任务详情下标
     * @returns {boolean}
     */
    removetaskDetail(id) {
      this.$confirm('删除该题目？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const tbId = id
        remove(tbId).then(() => {
          this.$message.success('删除成功')
          this.getaskDetailList()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * 判断任务详情菜单内的权限是否一个都没选
     * @param index 下标
     * @returns {boolean}
     */
    isMenuNone(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTaskDetail.permissionIdList.indexOf(handleList[i].id) > -1) {
          return false
        }
      }
      return true
    },
    /**
     * 判断任务详情菜单内的权限是否全选了
     * @param index 下标
     * @returns {boolean}
     */
    isMenuAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        if (this.tempTaskDetail.permissionIdList.indexOf(handleList[i].id) < 0) {
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
        this.addUnique(handleList[i].id, this.tempTaskDetail.permissionIdList)
      }
    },
    /**
     * checkbox全部取消选中
     * @param index 下标
     */
    cancelAll(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        const idIndex = this.tempTaskDetail.permissionIdList.indexOf(handleList[i].id)
        if (idIndex > -1) {
          this.tempTaskDetail.permissionIdList.splice(idIndex, 1)
        }
      }
    },
    /**
     * 本方法会在勾选状态改变之后触发
     * @param item 选项
     * @param index 对应下标
     */
    handleChecked(item, index) {
      if (this.tempTaskDetail.permissionIdList.indexOf(item.id) > -1) {
        // 选中事件
        // 如果之前未勾选本权限,现在勾选完之后,tempTaskDetail里就会包含本id
        // 那么就要将必选的权限勾上
        this.makePermissionChecked(index)
      } else {
        // 取消选中事件
        this.cancelAll(index)
      }
    },
    /**
     * 将任务详情菜单必选的权限勾上（这里并没有做必选的数据库字段）
     * @param index 权限对应下标
     */
    makePermissionChecked(index) {
      const handleList = this.permissionList[index].handleList
      for (let i = 0; i < handleList.length; i++) {
        this.addUnique(handleList[i].id, this.tempTaskDetail.permissionIdList)
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
<style>
  .submit-templates{

        position: absolute;
        right: 18px;
  }
  .el-button--mini {
    margin: 8px;
    padding: 7px 15px;
    font-size: 12px;
    border-radius: 3px;
}
</style>
