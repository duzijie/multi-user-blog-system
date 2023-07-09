<template>
  <div class="article">
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
    <div class="right"></div>
  </div>
</template>

<script>
export default {
  name: "Article",
  props: {
    userId: {
      type: String,
      default: "me",
    },
  },
  data() {
    return {
      blogs: [],
    };
  },
  created() {
    this.getBlogList();
  },
  watch: {
    userId() {
      this.getBlogList();
    },
  },
  methods: {
    getBlogList() {
      let path = "/api2/blogsystem/blog/searchUserBlog?userId=" + this.userId;
      if (this.userId == "me") path = "/api2/blogsystem/blog/userBlog";
      this.axios.get(path).then((res) => {
        if (res.data.code == 0) {
          this.blogs = res.data.entities;
          for (let key in this.blogs) {
            this.getBlogUserName(this.blogs[key], key);
          }
          // console.info('blog',res.data);
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
.article {
  text-align: left;
  color: var(--black);
  position: relative;
}
.article .item {
  width: 75%;
  min-height: 100px;
  background: var(--background3);
  text-align: left;
  padding: 20px 25px 12px;
  box-sizing: border-box;
  margin-bottom: 20px;
  border-radius: 4px;
}
.article .item .title {
  font-weight: 600;
  line-height: 32px;
  cursor: pointer;
  color: var(--background);
}
.article .item .title:hover {
  text-decoration: underline;
}
.article .item .create-time {
  line-height: 32px;
  text-align: right;
  opacity: 0.64;
}
.article .item .summary {
  font-size: 15px;
  color: var(--black);
  line-height: 26px;
  text-align: justify;
  opacity: 0.64;
}
.article .item .bottom {
  line-height: 32px;
  opacity: 0.64;
  user-select: none;
}
.article .item .author {
  text-align: right;
}
.article .item .author span {
  cursor: pointer;
}
.article .item .author span:hover {
  text-decoration: underline;
}
.article .item .author i {
  vertical-align: middle;
  margin: -2px 5px 0 0;
}
.article .right {
  width: calc(25% - 20px);
  min-height: 360px;
  border-radius: 4px;
  background: var(--background3);
  position: absolute;
  right: 0;
  top: 0;
}
</style>