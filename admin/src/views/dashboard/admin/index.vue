<template>
  <div class="dashboard-editor-container">
    <!-- <github-corner class="github-corner" /> -->

    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row> -->

    <el-table
      :data="taskDetailList"
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
      <el-table-column :filters="[{ text: '已填报', value: 1 }, { text: '已过期', value: 2 }]" :filter-method="filterType" label="状态" align="center" >
      <template slot-scope="scope" >{{ getTypeName(scope.row.status) }}</template>
      </el-table-column>
      <el-table-column label="实际填写企业" align="center" prop="comId" />
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
            v-if="hasPermission('taskDetail:detail')"
            @click.native.prevent="downloadTemp(scope.row)"
          >下载</el-button>
          <!-- <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('taskDetail:update')"
            @click.native.prevent="showUpdateRoleDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('taskDetail:delete')"
            @click.native.prevent="removetaskDetail(scope.row.id)"
          >删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
    </el-row> -->

    <!-- <el-row :gutter="8">
      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">
        <transaction-table />
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <todo-list />
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <box-card />
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import TransactionTable from './components/TransactionTable'
import TodoList from './components/TodoList'
import BoxCard from './components/BoxCard'
import {
    list as gettaskDetailList,
    add as addtaskDetail,
    remove,
    update as updateTaskDetail
  } from '@/api/taskDetail'
  import {
    listBy as getMsgList,
  } from '@/api/msg'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'
import { mapState } from 'vuex'
const lineChartData = {
  
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'DashboardAdmin',
  components: {
    GithubCorner,
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    TransactionTable,
    TodoList,
    BoxCard
  },
  data() {
    return {
      taskDetailList:[],
      msgList:[],
      listQuery: {
        page: 1,
        size: 9
      },
      listMsgQuery: {
        page: 1,
        size: 9,
        uid:0,
      },
      lineChartData: lineChartData.newVisitis
    }
  },
  created(){
    this.getaskDetailList()
  },
  mounted() {
        this.getMsgList();
        this.timer = window.setInterval(() => {
            setTimeout(() => {
                this.getMsgList()
            },0)
        },15000)
    },
  computed: {
    ...mapGetters(['roleName']),
    ...mapState({
      account: state => state.account
    })
  },
  methods: {
    unix2CurrentTime,
    downloadTemp(e){

      if (!e.fileUrl){
        this.$message.error('无下载地址')
      }else{
        this.$message.success("正在下载")
        window.location.href="http://localhost:8080/task/detail/download/"+e.id
      console.log(e)
      }
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

    /** 获取通知情列表
     */
     getMsgList() {
      this.listLoading = true
      this.listMsgQuery.uid = this.account.accountId
      getMsgList(this.listMsgQuery).then(response => {
        this.msgList = response.data.list


        this.msgList.forEach(element => {

          this.timer = window.setTimeout(() => {
            this.$notify({
          title: '消息提示',
          message: element.msg,
          type:"warning",
          // duration: 0
        });
      }, 500)

          
        });

        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载任务详情列表失败')
      })
    },
    //过滤类型
    filterType(value, row) {
            console.log("value:"+value)
            //console.log("row.type:"+row.type)
            return row.type === value;
          },
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
