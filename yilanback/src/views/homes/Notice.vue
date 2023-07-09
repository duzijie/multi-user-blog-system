<template>
  <div class="notice">
    <el-row>
      <div class="filter-container">
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-circle-plus-outline"
          @click="addFilter"
          >发布公告</el-button
        >
      </div>
    </el-row>

    <el-row>
      <el-col>
        <el-table
          height="460"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%"
          @sort-change="sortChange"
        >
          <el-table-column
            label="公告ID"
            prop="id"
            sortable="custom"
            align="center"
            width="100"
            :class-name="getSortClass('id')"
          >
            <template slot-scope="{ row }">
              <span>{{ row.id }}</span>
            </template>
          </el-table-column>

          <el-table-column label="标题" prop="title" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.title }}</span>
            </template>
          </el-table-column>

          <el-table-column label="查看公告" width="140px" align="center">
            <template slot-scope="{ row }">
              <el-button
                size="mini"
                type="primary"
                class="el-icon-view el-icon--right"
                @click="viewContent(row)"
                >查看详细</el-button
              >
            </template>
          </el-table-column>

          <el-table-column
            label="发布时间"
            prop="createTime"
            width="180px"
            align="center"
          >
            <template slot-scope="{ row }">
              <span>{{ dateFormat(row.createTime) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="状态" prop="status" align="center" width="80">
            <template slot-scope="{ row }">
              <el-tag
                v-if="row.status != '0'"
                :type="'primary'"
                disable-transitions
                >{{ row.status | statusFilter }}</el-tag
              >

              <el-tag
                v-if="row.status != '1'"
                :type="'success'"
                disable-transitions
                >{{ row.status | statusFilter }}</el-tag
              >
            </template>
          </el-table-column>

          <!-- 操作 绑定事件 -->
          <el-table-column label="操作" align="left" fixed="right" width="220">
            <template slot-scope="{ row }">
              <el-button
                v-if="row.status != '1'"
                size="mini"
                align="left"
                type="success"
                @click="handleModifyStatus1(row, '1')"
                >发布</el-button
              >

              <el-button
                v-if="row.status != '0'"
                size="mini"
                align="left"
                type="warning"
                @click="handleModifyStatus0(row, '0')"
                >撤销</el-button
              >
              <el-button
                size="mini"
                align="left"
                type="danger"
                @click="upgradePermissions(row)"
                >删除</el-button
              >
              <el-button
                v-if="row.status != '1'"
                size="mini"
                align="right"
                type="primary"
                @click="updateFilter(row)"
                >编辑</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <!-- 使用分页组件 -->
        <div class="block">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            :page-sizes="limit"
            :page-size="limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </div>
      </el-col>
    </el-row>

    <!-- 增加弹出框 - 内置 新增和修改 表单 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 80%; margin-left: 50px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <el-input
            type="textarea"
            :rows="10"
            class="big"
            v-model="temp.content"
          />
        </el-form-item>

        <el-form-item
          v-if="dialogStatus === 'create'"
          label="状态"
          prop="status"
        >
          <el-select
            v-model="temp.status"
            class="status"
            placeholder="Please select"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.key"
              :label="item.display_name"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'create' ? createData() : updateData()"
          >确定</el-button
        >
      </div>
    </el-dialog>
    <!-- </el-header> -->
    <el-dialog :title="textsMap" :visible.sync="dialogAnno">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 80%; margin-left: 50px"
      >
        <el-form-item label="标题" prop="title">
          <el-input readonly v-model="temp.title" />
        </el-form-item>

        <el-form-item label="公告内容" prop="content">
          <el-input
            readonly
            type="textarea"
            :rows="10"
            class="big"
            v-model="temp.content"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="lookData()">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Notice",
  data() {
    return {
      // 假数据
      listall: [],
      list: [],
      total: 0,
      limit: 5,
      currentPage: 1,
      listLoading: false,
      listQuery: {
        id: undefined,
        title: undefined,
        content: undefined,
        createTime: undefined,
        status: undefined,
        page: 1,
        limit: 20,
      },
      currentTime: new Date(),
      statusOptions: [
        { key: "1", display_name: "发布" },
        { key: "0", display_name: "草稿" },
      ],
      dialogAnno: false,
      dialogFormVisible: false,
      dialogStatus: undefined,
      textMap: {
        update: "编辑公告",
        create: "填写公告",
      },
      textsMap: "公告详情",
      temp: {
        id: undefined,
        title: undefined,
        content: undefined,
        status: "0",
      },
      rules: {
        title: [
          { required: true, message: "title is required", trigger: "blur" },
        ],
        content: [
          { required: true, message: "content is required", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    sortChange(data) {
      const { prop, order } = data;
      this.sortFilter(prop, order);
    },
    getSortClass: function (key) {
      const sort = this.listQuery.sort;
      if (key === "id") {
        return sort === `+${key}` ? "ascending" : "descending";
      }
    },
    //排序
    sortFilter: function (prop, order) {
      if (prop === "id") {
        this.proptype = prop;
        if (order === "descending") {
          this.listall.sort(this.my_desc_sort);
        } else if (order === "ascending") {
          this.listall.sort(this.my_asc_sort);
        }
      }
      this.currentPage=1;
      this.list = this.listall.filter(
        (item, index) => index < this.limit && index >= 0
      );
    },
    //逆序
    my_desc_sort(a, b) {
      return b[this.proptype] - a[this.proptype];
    },
    //正序
    my_asc_sort(a, b) {
      return a[this.proptype] - b[this.proptype];
    },
    // handleSizeChange(val) {
    //   console.log(`每页 ${val} 条`);
    // },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.list = this.listall.filter(
        (item, index) =>
          index < this.limit * val && index >= this.limit * (val - 1)
      );
    },
    //新加
    resetTemp: function () {
      this.temp = {
        id: undefined,
        title: undefined,
        content: undefined,
        time: undefined,
        status: "0",
      };
    },

    updateFilter: function (row) {
      this.temp = Object.assign({}, row); // copy obj
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    addFilter: function () {
      this.resetTemp();
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    //编辑公告
    updateData: function () {
      this.dialogFormVisible = false;
      this.axios
        .post("/api2/blogsystem/notice/update", this.temp)
        .then((res) => {
          console.info(res.data);
          if (res.data.code == 0) {
            this.$message.success("修改成功");
            this.getList();
          }
        })
        .catch(() => {});
      
    },

    //发布公告
    createData: function () {
      this.dialogFormVisible = false;
      this.temp.time = "2";
      this.axios
        .post("/api2/blogsystem/notice/save", this.temp)
        .then((res) => {
          console.info(res.data);
          if (res.data.code == 0) {
            this.$message.success("发布成功");
            this.getList();
          }
        })
        .catch(() => {});
    },

    //发布公告草稿
    handleModifyStatus1: function (row, status) {
      row.status = status;
      this.axios
        .get("/api2/blogsystem/notice/releaseNotice?noticeId=" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("发布成功");
            this.getList();
          }
        })
        .catch(() => {});
    },
    //撤销发布
    handleModifyStatus0: function (row, status) {
      row.status = status;
      this.axios
        .get("/api2/blogsystem/notice/cancelNotice?noticeId=" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("撤销成功");
            this.getList();
          }
        })
        .catch(() => {});
    },
    //删除公告
    upgradePermissions: function (row) {
      this.axios
        .get("/api2/blogsystem/notice/delete/" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("删除成功");
            this.getList();
          }
        })
        .catch(() => {});
    },
    //时间格式化
    dateFormat: function (time) {
      var date = new Date(time);
      var year = date.getFullYear();
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hours =
        date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
      var minutes =
        date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      var seconds =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      // 拼接
      return (
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds
      );
    },
    //查看公告
    viewContent: function (row) {
      this.temp = Object.assign({}, row);
      this.dialogAnno = true;
    },
    lookData: function () {
      this.dialogAnno = false;
      this.$message({
        message: "阅读完毕",
        type: "success",
      });
    },
    getList() {
      this.listLoading = true;
      this.axios
        .get("/api2/blogsystem/notice/getAllNotice")
        .then((res) => {
          this.listall = res.data.entities;
          this.total = this.listall.length;
          this.list = this.listall.filter(
            (item, index) => index < this.limit && index >= 0
          );
          setTimeout(() => {
            this.listLoading = false;
          }, 0.5 * 1000);
        })
        .catch(() => {});
    },
  },

  filters: {
    statusFilter(status) {
      const statusMap = {
        1: "发布",
        0: "草稿",
      };
      return statusMap[status];
    },
  },
};
</script>

<style scoped>
.notice {
  width: 100%;
  min-height: 100%;
  background: var(--background3);
  padding: 20px 25px;
  box-sizing: border-box;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}
</style>