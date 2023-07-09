<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('blogsystem:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('blogsystem:user:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="涓婚敭">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="濮撳悕锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负20">
      </el-table-column>
      <el-table-column
        prop="email"
        header-align="center"
        align="center"
        label="閭??锛屾渶澶ч暱搴︿负50锛屼笖unique">
      </el-table-column>
      <el-table-column
        prop="username"
        header-align="center"
        align="center"
        label="鐢ㄦ埛璐﹀彿锛屾渶灏忛暱搴︿负3锛屾渶澶ч暱搴︿负20锛屼笖unique">
      </el-table-column>
      <el-table-column
        prop="password"
        header-align="center"
        align="center"
        label="瀵嗙爜锛屾渶澶ч暱搴︿负100">
      </el-table-column>
      <el-table-column
        prop="avatar"
        header-align="center"
        align="center"
        label="澶村儚鍥剧墖鍦板潃锛屾渶澶ч暱搴︿负200">
      </el-table-column>
      <el-table-column
        prop="phone"
        header-align="center"
        align="center"
        label="鐢佃瘽鍙风爜">
      </el-table-column>
      <el-table-column
        prop="sex"
        header-align="center"
        align="center"
        label="鎬у埆 0-濂?1-鐢">
      </el-table-column>
      <el-table-column
        prop="birthday"
        header-align="center"
        align="center"
        label="">
      </el-table-column>
      <el-table-column
        prop="address"
        header-align="center"
        align="center"
        label="鍦板潃">
      </el-table-column>
      <el-table-column
        prop="islock"
        header-align="center"
        align="center"
        label="璐﹀彿閿佸畾锛?-姝ｅ父 1-閿佸畾">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="璐︽埛鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤">
      </el-table-column>
      <el-table-column
        prop="introduction"
        header-align="center"
        align="center"
        label="涓?汉绠?粙">
      </el-table-column>
      <el-table-column
        prop="motto"
        header-align="center"
        align="center"
        label="涓??绛惧悕">
      </el-table-column>
      <el-table-column
        prop="styleId"
        header-align="center"
        align="center"
        label="澶栭敭瀵瑰簲style琛?紝澶氬?涓?叧绯伙紝涓?釜style瀵瑰簲澶氫釜user">
      </el-table-column>
      <el-table-column
        prop="roles"
        header-align="center"
        align="center"
        label="鐢ㄦ埛瑙掕壊锛孯OLE_USER-鏅??鐢ㄦ埛 ROLE_ADMIN-绠＄悊鍛?ROLE_SUPER-瓒呯骇绠＄悊鍛">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './user-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/blogsystem/user/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/blogsystem/user/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      }
    }
  }
</script>
