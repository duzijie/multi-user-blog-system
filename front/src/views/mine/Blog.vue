<template>
  <div class="blog">
    <el-input
      v-model="search"
      placeholder="筛选"
      class="search"
      size="small"
    ></el-input>
    <el-table :data="searchFilter(blogs).reverse()" style="width: 100%">
      <el-table-column type="index" label="序"></el-table-column>
      <el-table-column label="时间" width="120">
        <template slot-scope="scope">
          {{ scope.row.createTime | timeFormat }}
        </template>
      </el-table-column>
      <el-table-column
        label="标题"
        prop="title"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="right" width="190">
        <template slot-scope="scope">
          <router-link
            :to="'/show/' + scope.row.id"
            target="_blank"
            style="margin-right: 10px"
          >
            <el-button plain size="small" type="primary" icon="el-icon-view">
            </el-button>
          </router-link>
          <router-link
            :to="'/editor/' + scope.row.id"
            target="_blank"
            style="margin-right: 10px"
          >
            <el-button plain size="small" type="warning" icon="el-icon-edit">
            </el-button>
          </router-link>
          <el-button
            plain
            size="small"
            type="danger"
            icon="el-icon-delete"
            @click="deleteUserBlog(scope.row)"
          >
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Blog",
  props: {
    type: {
      type: String,
      default: "publish",
    },
  },
  data() {
    return {
      blogs: [],
      search: "",
    };
  },
  created() {
    this.getUserBlog();
  },
  watch: {
    type() {
      this.getUserBlog();
    },
  },
  methods: {
    searchFilter(val) {
      let search = this.search;
      return val.filter(
        (data) => !search || (data.title + "").includes(search)
      );
    },
    getUserBlog() {
      let path1 = "/api2/blogsystem/blog/userBlog";
      let path2 = "/api2/blogsystem/blog/userDraftBlog";
      let path3 = "/api2/blogsystem/blog/userCheckBlog";
      let path = path1;
      if (this.type == "publish") path = path1;
      if (this.type == "draft") path = path2;
      if (this.type == "check") path = path3;
      this.axios.get(path).then((res) => {
        if (res.data.code == 0) {
          // console.info(res.data.entities);
          this.blogs = res.data.entities;
        }
      });
    },
    deleteUserBlog(blog) {
      this.$confirm("此操作将永久删除该文章, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.axios
            .get("/api2/blogsystem/blog/delete/" + blog.id)
            .then((res) => {
              if (res.data.code == 0) {
                let index = this.blogs.indexOf(blog);
                if (index != -1) this.blogs.splice(index, 1);
                this.$message.success("删除成功");
              }
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
  },
  filters: {
    timeFormat(val) {
      val = (val + "").replace("T", " ");
      val = val.slice(0, 10);
      return val;
    },
    numberFormat(val) {
      if (!val) return 0;
      return parseInt(val);
    },
    lengthFormat(val) {
      let length = val.length;
      let maxLen = 80;
      val = length > maxLen ? val.slice(0, maxLen) + "..." : val;
      return val;
    },
  },
};
</script>

<style scoped>
.blog {
  padding: 20px 30px;
  box-sizing: border-box;
  text-align: left;
  background: var(--background3);
  position: relative;
}
.blog .search {
  width: 153px;
  position: absolute;
  right: 40px;
  top: 25px;
  z-index: 10;
}
</style>