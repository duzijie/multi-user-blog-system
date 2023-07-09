<template>
  <el-row :gutter="20" class="default">
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
      <el-button type="primary" v-if="more" @click="readMore">
        查看更多
      </el-button>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "",
  data() {
    return {
      blogs: [
        {
          authority: null,
          catalogId: 45,
          commentSize: null,
          content: "afljalfjal jivoziu1ojenl",
          createTime: "2020-09-19T03:14:31.000+00:00",
          htmlContent: "ag sfaffsao j;sfl;tjleoptest",
          id: 22,
          readSize: null,
          status: null,
          summary: "this is a  test",
          title: "test",
          updateTime: null,
          userId: 1,
          voteSize: null,
        },
      ],
      more: false,
    };
  },
  created() {
    this.getBlogList();
    setTimeout(() => {
      this.more = true;
    }, 800);
  },
  methods: {
    getBlogList() {
      this.axios.get("/api2/blogsystem/blog/homelist").then((res) => {
        if (res.data.code == 0) {
          this.blogs = res.data.entities;
          for (let key in this.blogs) {
            this.getBlogUserName(this.blogs[key], key);
          }
          // console.info("blogs", res.data);
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
    readMore() {
      this.$router.push("/type");
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
.default {
  width: 100%;
  min-height: 100vh;
  padding: 0 100px;
  box-sizing: border-box;
  margin: 0;
}
.default .left .item {
  width: 100%;
  min-height: 100px;
  background: var(--background3);
  text-align: left;
  padding: 20px 25px 12px;
  box-sizing: border-box;
  margin: 20px 0;
  border-radius: 4px;
}
.default .left .item .title {
  font-weight: 600;
  line-height: 32px;
  cursor: pointer;
  color: var(--background);
}
.default .left .item .title:hover {
  text-decoration: underline;
}
.default .left .item .create-time {
  line-height: 32px;
  text-align: right;
  opacity: 0.64;
}
.default .left .item .summary {
  font-size: 15px;
  color: var(--black);
  line-height: 26px;
  text-align: justify;
  opacity: 0.64;
}
.default .left .item .bottom {
  line-height: 32px;
  opacity: 0.64;
  user-select: none;
}
.default .left .item .author {
  text-align: right;
}
.default .left .item .author span {
  cursor: pointer;
}
.default .left .item .author span:hover {
  text-decoration: underline;
}
.default .left .item .author i {
  vertical-align: middle;
  margin: -2px 5px 0 0;
}
.default .left .el-button {
  margin-bottom: 20px;
}
.default .right .hot-tabs {
  background: var(--background3);
  margin: 20px 0;
  padding: 20px 25px;
  text-align: left;
  color: var(--background);
  border-radius: 4px;
}
.default .right .hot-tabs div {
  margin-bottom: 12px;
}
.default .right .hot-tabs span {
  padding: 5px 8px;
  margin: 3px 8px 3px 0;
  border-radius: 4px;
  display: inline-block;
  border: 1px solid var(--background);
  opacity: 0.64;
  cursor: pointer;
}
</style>
