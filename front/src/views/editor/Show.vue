<template>
  <div class="show">
    <yilan-nav></yilan-nav>
    <el-row :gutter="20" class="box">
      <el-col :offset="3" :span="18" class="left">
        <!-- 标题 -->
        <h1 class="title">{{ blog.title }}</h1>
        <el-row :gutter="20">
          <el-col class="info" :span="6">
            {{ blog.createTime | timeFormat }}
          </el-col>
          <el-col :span="18" style="text-align: right">
            <div class="number">
              <span class="el-icon-view"></span>
              {{ blog.readSize | numberFormat }}
            </div>
            <div class="number">
              <span class="el-icon-chat-line-round"></span>
              {{ blog.commentSize | numberFormat }}
            </div>
            <div class="number">
              <span class="el-icon-sugar"></span>
              {{ blog.voteSize | numberFormat }}
            </div>
          </el-col>
        </el-row>
        <!-- 正文 -->
        <mavon-editor
          v-model="blog.content"
          :boxShadow="false"
          :tabSize="2"
          :editable="false"
          defaultOpen="preview"
          :toolbarsFlag="false"
          :subfield="false"
        >
        </mavon-editor>
        <!-- 按钮 -->
        <div style="text-align: center; padding: 30px 0 10px" v-if="login">
          <el-button type="primary" style="margin: 20px" @click="viteUserBlog">
            点赞
          </el-button>
          <el-button type="danger" @click="reportUserBlog"> 举报 </el-button>
        </div>
        <!-- 输入框 -->
        <div class="input" v-if="login">
          <h4 style="font-weight: 500">评论</h4>
          <el-button
            size="small"
            type="primary"
            class="btn"
            @click="submitComment"
          >
            发布
          </el-button>
          <el-input
            type="textarea"
            placeholder="说说你的看法吧~"
            v-model="comment.content"
            maxlength="200"
            show-word-limit
            :autosize="{ minRows: 3, maxRows: 10 }"
          ></el-input>
        </div>
        <!-- 评论列表 -->
        <div
          class="comment"
          v-for="(item1, key1) in commentList"
          :key="item1.id"
        >
          <div class="comment-info">
            <el-avatar :size="24" :src="item1.avatar"></el-avatar>
            <span class="name">{{ item1.username }}</span>
            <span
              v-if="
                item1.role == 'ROLE_ADMIN' ||
                item1.role == 'ROLE_SUPER' ||
                item1.userId == userId
              "
              style="float: right; color: var(--danger); cursor: pointer"
              @click="deleteComment(item1, key1 + '')"
            >
              删除
            </span>
            <span
              v-if="blogUserId == userId"
              style="float: right; color: var(--primary); cursor: pointer"
              @click="onComment(item1.id, key1)"
            >
              回复
            </span>
            <span style="float: right">
              {{ item1.createTime | timeFormat }}
            </span>
          </div>
          <!-- 评论内容 -->
          <div class="comment-content" style="margin-bottom: 10px">
            <span>{{ item1.content }}</span>
            <!-- 回复输入框 -->
            <div class="input" v-if="item1.id == reply.parentId">
              <h4 style="font-weight: 500">回复</h4>
              <el-button
                size="small"
                type="warning"
                class="btn"
                @click="submitCommentReply"
              >
                发布
              </el-button>
              <el-input
                type="textarea"
                placeholder="回复点什么~"
                v-model="reply.content"
                maxlength="200"
                show-word-limit
                :autosize="{ minRows: 3, maxRows: 10 }"
              ></el-input>
            </div>
          </div>
          <!-- 回复列表 -->
          <div
            v-for="(item2, key2) in item1.children"
            :key="item2.id"
            style="padding: 0 0 0 32px; box-sizing: border-box"
          >
            <div class="comment-info">
              <span class="name">{{ item2.username }}</span>
              <span style="max-width: calc(100% - 50px)">{{
                item2.content
              }}</span>
              <span
                v-if="
                  item2.role == 'ROLE_ADMIN' ||
                  item2.role == 'ROLE_SUPER' ||
                  item2.userId == userId
                "
                style="float: right; color: var(--danger); cursor: pointer"
                @click="deleteComment(item2, key1 + '', key2 + '')"
              >
                删除
              </span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <yilan-foo></yilan-foo>
  </div>
</template>

<script>
import nav from "../../components/Nav";
import foo from "../../components/Foo";

export default {
  name: "Show",
  components: {
    "yilan-nav": nav,
    "yilan-foo": foo,
  },
  props: {
    blogId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      userId: -1,
      blogUserId: -2,
      blog: {},
      commentList: [],
      comment: {
        content: "",
        parentId: 0,
        blogId: "",
      },
      reply: {
        content: "",
        parentId: 0,
        blogId: "",
      },
    };
  },
  watch: {
    blogId() {
      this.getBlogDetail();
    },
  },
  created() {
    this.getBlogDetail();
    this.getCommentList();
    this.getUserId();
  },
  computed: {
    login() {
      return this.$store.state.login;
    },
  },
  methods: {
    getBlogDetail() {
      if (this.blogId == "") {
        this.$router.push("/404");
      }
      this.axios
        .get("/api2/blogsystem/blog/info/" + this.blogId)
        .then((res) => {
          if (res.data.code == 0) {
            this.blog = res.data.blog;
            this.blogUserId = res.data.blog.userId;
          }
        });
    },
    getCommentList() {
      if (this.blogId == "") {
        this.$router.push("/404");
      }
      this.axios
        .get("/api/blogsystem/comment/listWithTree?blogId=" + this.blogId)
        .then((res) => {
          if (res.data.code == 0) {
            this.commentList = res.data.commentEntityList;
            for (let key1 in this.commentList) {
              let item1 = this.commentList[key1];
              this.getUserInfo(item1, key1);
              if (!item1.children) return;
              for (let key2 in item1.children) {
                let item2 = item1.children[key2];
                this.getUserInfo(item2, key1, key2);
              }
            }
          } else this.$message.error("获取评论失败");
        })
        .catch(() => {
          this.$message.error("获取评论失败");
        });
    },
    getUserInfo(item, key1, key2) {
      this.axios
        .post("/api/blogsystem/comment/searchUserById", {
          id: item.id,
        })
        .then((res) => {
          if (res.data.code == 0) {
            item.role = res.data.role;
            // item.role = "ROLE_ADMIN";
            if (key2 || key2 == 0)
              this.$set(this.commentList[key1].children, key2, item);
            else this.$set(this.commentList, key1, item);
          }
        });
      this.axios
        .post("/api/blogsystem/comment/searchUsernameAndAvatarByUserId", {
          id: item.id,
        })
        .then((res) => {
          if (res.data.code == 0) {
            item.avatar = res.data.avatar;
            item.username = res.data.username;
            if (key2 || key2 == 0)
              this.$set(this.commentList[key1].children, key2, item);
            else this.$set(this.commentList, key1, item);
          }
        });
    },
    submitComment() {
      if (this.comment.content == "") return;
      this.comment.blogId = this.blogId;
      this.axios
        .post("/api/blogsystem/comment/saveCommentByBlogId", this.comment)
        .then((res) => {
          if (res.data.code) {
            this.$message.success("发布评论成功");
            this.comment = {
              content: "",
              parentId: 0,
              blogId: "",
            };
            let item = res.data.commentEntity;
            item.children = [];
            this.getUserInfo(item, this.commentList.length);
          }
        })
        .catch(() => {
          this.$message.error("发布评论失败");
        });
    },
    submitCommentReply() {
      if (this.reply.content == "") return;
      this.reply.blogId = this.blogId;
      this.axios
        .post("/api/blogsystem/comment/saveCommentByBlogId", this.reply)
        .then((res) => {
          if (res.data.code) {
            this.$message.success("回复评论成功");
            let item = res.data.commentEntity;
            let key1 = this.reply.index;
            let key2 = this.commentList[key1].children.length;
            this.getUserInfo(item, key1, key2);
            this.reply = {
              content: "",
              parentId: 0,
              blogId: "",
              index: -1,
            };
          }
        })
        .catch(() => {
          this.$message.error("回复评论失败");
        });
    },
    onComment(id, index) {
      this.reply.parentId = id;
      this.reply.index = index;
    },
    getUserId() {
      if (!this.login) return;
      this.axios.post("/api//blogsystem/user/searchInfo").then((res) => {
        this.userId = res.data.userEntity.id;
      });
    },
    deleteComment(item, key1, key2) {
      this.$confirm("此操作将永久删除该评论, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.axios
            .post("/api/blogsystem/comment/deleteComment", {
              id: item.id,
            })
            .then((res) => {
              if (res.data.code) {
                this.$message.success("删除评论成功");
                if (key2 != null) {
                  this.commentList[key1].children.splice(key2, 1);
                } else {
                  this.commentList.splice(key1, 1);
                }
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    viteUserBlog() {
      if (this.blogId == "") return;
      this.axios
        .get("/api2/blogsystem/vote/save?blogId=" + this.blogId)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code == 0) this.$message.success("点赞成功");
          if (res.data.code == -1) this.$message.warning("你已经点过赞了");
        });
    },
    reportUserBlog() {
      if (this.blogId == "") return;
      this.$confirm("你确定要举报该文章吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.axios
            .get("/api2/blogsystem/blog/reportBlog?blogId=" + this.blogId)
            .then((res) => {
              // console.info(res.data);
              if (res.data.code == 0) this.$message.success("举报成功");
            })
            .catch(() => {
              this.$message.error("意外错误");
            });
        })
        .catch(() => {
          this.$message.info("已取消操作");
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
.show {
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
}
.show .box {
  width: calc(100% - 200px);
  height: calc(100% - 110px);
  margin: 20px 90px !important;
}
.show .box .left {
  background: var(--background3);
  min-height: 100px;
  border-radius: 4px;
  padding: 20px 36px !important;
  box-sizing: border-box;
  text-align: justify;
}
.show .box .left .title {
  color: var(--background);
  text-align: justify;
}
.show .box .left .info {
  text-align: left;
  margin-bottom: 15px;
}
.show .box .left .number {
  display: inline-block;
  padding: 0 16px;
  margin-bottom: 15px;
  cursor: default;
  user-select: none;
}
.show .box .left .content {
  line-height: 24px;
}
.show .box .left .input {
  padding: 0 0 20px 0;
}
.show .box .left .input .btn {
  margin: -42px 0;
  float: right;
}
.show .box .left .comment {
  padding: 10px 0;
  box-sizing: border-box;
  margin-top: 12px;
  border-bottom: 1px var(--background2) solid;
}
.show .box .left .comment .comment-info {
  vertical-align: top;
  margin-bottom: 8px;
}
.show .box .left .comment .comment-info * {
  vertical-align: top;
  line-height: 22px;
  display: inline-block;
  margin-right: 8px;
}
.show .box .left .comment .comment-info .name {
  color: var(--primary);
}
.show .box .left .comment .comment-content {
  padding-left: 32px;
  box-sizing: border-box;
  margin-top: -5px;
}
</style>
<style>
.content-input-wrapper {
  padding: 16px 30px !important;
}
.markdown-body {
  margin-left: -37px;
  width: calc(100% + 72px);
}
.v-show-content {
  padding: 12px 35px 15px !important;
}
.v-show-content h1,
.v-show-content h2,
.v-show-content h3,
.v-show-content h4,
.v-show-content h5,
.v-show-content h6 {
  color: var(--background) !important;
}
</style>