<template>
  <el-row :gutter="20" class="search">
    <el-col :offset="3" :span="18" class="left">
      <div v-for="item in blogs" :key="item.id" class="item">
        <el-row :gutter="20">
          <el-col :span="17" class="title">
            <router-link target="_blank" :to="'/show/' + item.id">
              {{ item.title }}
            </router-link>
          </el-col>
          <el-col :span="7" class="create-time">
            {{ item.createTime | timeFormat }}
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin: 5px -10px">
          <el-col class="summary">{{ item.summary | lengthFormat }}</el-col>
        </el-row>
        <el-row :gutter="20" class="bottom">
          <el-col :span="3">
            <span class="el-icon-view"></span>
            {{ item.readSize | numberFormat }}
          </el-col>
          <el-col :span="3">
            <span class="el-icon-chat-line-round"></span>
            {{ item.commentSize | numberFormat }}
          </el-col>
          <el-col :span="3">
            <span class="el-icon-sugar"></span>
            {{ item.voteSize | numberFormat }}
          </el-col>
          <el-col :span="15" class="author">
            <span>
              <i class="el-icon-user"></i>
              <router-link
                :to="'/blog/' + item.userId + '/intro'"
                target="_blank"
              >
                {{ item.username }}
              </router-link>
            </span>
          </el-col>
        </el-row>
      </div>
      <div v-if="none" class="none">暂没有相关搜索结果</div>
      <div v-if="nosearch" class="none">请输入搜索内容</div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "",
  data() {
    return {
      blogs: [],
      none: false,
      nosearch: false,
    };
  },
  computed: {
    search() {
      return this.$store.state.search;
    },
  },
  watch: {
    search() {
      this.getSearch();
    },
    blogs() {},
  },
  created() {
    this.getSearch();
  },
  methods: {
    getSearch() {
      if (this.search == "") {
        this.nosearch = true;
        return;
      } else this.nosearch = false;
      this.axios
        .get("/api2/blogsystem/blog/fuzzyQuery?queryString=" + this.search)
        .then((res) => {
          if (res.data.code == 0) {
            this.blogs = res.data.entities;
            for (let key in this.blogs) {
              this.getBlogUserName(this.blogs[key], key);
            }
            if (!this.blogs.length) this.none = true;
            else this.none = false;
          } else this.$message.error(res.data.msg);
        });
    },
    getBlogUserName(item, key) {
      this.axios
        .get("/api/blogsystem/user/profilePage?id=" + item.userId)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code == 0) {
            item.username = res.data.username + "";
            // console.info(key, item)
            this.$set(this.blogs, key, JSON.parse(JSON.stringify(item)));
          }
        });
    },
  },
  filters: {
    timeFormat(val) {
      val = (val + "").replace("T", " ");
      val = val.slice(0, 16);
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
.search {
  width: 100%;
  min-height: 100vh;
  padding: 0 100px;
  box-sizing: border-box;
  margin: 0;
}
.search .left .item {
  width: 100%;
  min-height: 100px;
  background: var(--background3);
  text-align: left;
  padding: 20px 25px 12px;
  box-sizing: border-box;
  margin: 20px 0;
  border-radius: 4px;
}
.search .left .item .title {
  font-weight: 600;
  line-height: 32px;
  cursor: pointer;
  color: var(--background);
}
.search .left .item .title:hover {
  text-decoration: underline;
}
.search .left .item .create-time {
  line-height: 32px;
  text-align: right;
  opacity: 0.64;
}
.search .left .item .summary {
  font-size: 15px;
  color: var(--black);
  line-height: 26px;
  text-align: justify;
  opacity: 0.64;
}
.search .left .item .bottom {
  line-height: 32px;
  opacity: 0.64;
  user-select: none;
}
.search .left .item .author {
  text-align: right;
}
.search .left .item .author span {
  cursor: pointer;
}
.search .left .item .author span:hover {
  text-decoration: underline;
}
.search .left .item .author i {
  vertical-align: middle;
  margin: -2px 5px 0 0;
}
.search .left .el-button {
  margin-bottom: 20px;
}
.search .left .none {
  margin: 20px 0;
  line-height: 50px;
  border-radius: 4px;
  color: var(--background);
}
</style>