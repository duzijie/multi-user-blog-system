<template>
  <div class="blog">
    <el-row>
      <div class="filter-container">
        <el-input
          v-model="listQuery.userId"
          placeholder="博主ID"
          style="width: 80px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />

        <el-input
          v-model="listQuery.title"
          placeholder="文章标题"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />

        <el-select
          v-model="listQuery.status"
          placeholder="状态"
          clearable
          class="filter-item"
          style="width: 100px"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>

        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleFilter"
          >查询</el-button
        >
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-circle-close"
          @click="cleanFilter"
          >清空</el-button
        >

        <el-button
          class="filter-item"
          style="margin-left: 10px"
          type="primary"
          icon="el-icon-refresh"
          @click="handleRestFilter"
          >重置</el-button
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
            label="博主ID"
            prop="userId"
            sortable="custom"
            align="center"
            width="100"
            :class-name="getSortClass('userId')"
          >
            <template slot-scope="{ row }">
              <span>{{ row.userId }}</span>
            </template>
          </el-table-column>

          <el-table-column label="文章标题" prop="title" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.title }}</span>
            </template>
          </el-table-column>

          <!-- 日期处理 使用插槽slot -->

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

          <el-table-column label="查看文章" width="120px" align="center">
            <template slot-scope="{ row }">
              <el-button
                size="mini"
                type="primary"
                class="el-icon-view el-icon--right"
                @click="viewDetailed(row)"
                >查看详细</el-button
              >
            </template>
          </el-table-column>

          <el-table-column label="状态" prop="status" align="center" width="80">
            <template slot-scope="{ row }">
              <el-tag
                :type="row.status === '2' ? '' : 'info'"
                disable-transitions
                ><div>{{ row.status | statusFilter}}</div></el-tag
              >
            </template>
          </el-table-column>

          <!-- 操作 绑定事件 -->
          <el-table-column label="操作" align="left" fixed="right" width="180">
            <template slot-scope="{ row }">
              <el-button
                v-if="row.status === '3'"
                size="mini"
                type="success"
                @click="handleModifyStatus2(row, '2')"
                >解封</el-button
              >
              <el-button
                v-if="row.status === '2'"
                size="mini"
                type="warning"
                @click="handleModifyStatus0(row, '0')"
                >通过</el-button
              >
              <el-button
                v-if="row.status === '2'"
                size="mini"
                type="danger"
                @click="handleModifyStatus3(row, '3')"
                >封禁</el-button
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

    <el-dialog :title="textMep" :visible.sync="dialogAnno">
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

        <el-form-item label="文章内容" prop="content">
          <el-input
            readonly
            type="textarea"
            :rows="20"
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
  name: "Blog",
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
        //id: undefined,
        title: "",
        //summary: undefined,
        //content: undefined,
        userId: "",
        // createTime: undefined,
        // readSize: undefined,
        // commentSize: undefined,
        // voteSize: undefined,
        // catalogId: undefined,
        status: "",
        //authority: undefined,
        // page: 1,
        // limit: 20,
      },
      dialogAnno: false,
      textMep: "文章详细",
      temp: {
        title: undefined,
        content: undefined,
      },
      statusOptions: [
        { key: "3", display_name: "封禁" },
        { key: "2", display_name: "被举报" },
      ],
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
    //查询
    handleFilter: function () {
      this.getList();
    },
    //清空
    cleanFilter: function () {
      this.listQuery = {
        id: undefined,
        title: "",
        summary: undefined,
        content: undefined,
        userId: "",
        createTime: undefined,
        readSize: undefined,
        commentSize: undefined,
        voteSize: undefined,
        catalogId: undefined,
        status: "",
        authority: undefined,
        page: 1,
        limit: 20,
      };
    },
    //重置
    handleRestFilter: function () {
      this.listQuery = {
        id: undefined,
        title: "",
        summary: undefined,
        content: undefined,
        userId: "",
        createTime: undefined,
        readSize: undefined,
        commentSize: undefined,
        voteSize: undefined,
        catalogId: undefined,
        status: "",
        authority: undefined,
        page: 1,
        limit: 20,
      };
      this.getList();
    },
    getList() {
      this.listLoading = true;
      this.axios
        .get(
          "/api2/blogsystem/blog/illegalFuzzyQuery?userId=" +
            this.listQuery.userId +
            "&title=" +
            this.listQuery.title +
            "&status=" +
            this.listQuery.status,
          this.listQuery
        )
        .then((res) => {
          console.info(res.data);
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
    //查看文章
    viewDetailed: function (row) {
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
    //修改状态
    handleModifyStatus3: function (row, status) {
      row.status = status;
      this.axios
        .get("/api2/blogsystem/blog/banBlog?blogId=" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("封禁成功");
            this.getList();
          }
        })
        .catch(() => {});
    },

    handleModifyStatus2: function (row, status) {
      row.status = status;
      this.axios
        .get("/api2/blogsystem/blog/deblockBlog?blogId=" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("解封成功");
            this.getList();
          }
        })
        .catch(() => {});
    },
    handleModifyStatus0: function (row, status) {
      row.status = status;
      this.axios
        .get("/api2/blogsystem/blog/recoverBlog?blogId=" + row.id)
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("审核通过");
            this.getList();
          }
        })
        .catch(() => {});
    },
  },

  filters: {
    statusFilter(status) {
      const statusMap = {
        3: "封禁",
        2: "被举报",
      };
      return statusMap[status];
    },
  },
};
</script>

<style scoped>
.blog {
  width: 100%;
  min-height: 100%;
  background: var(--background3);
  padding: 20px 25px;
  box-sizing: border-box;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}
</style>