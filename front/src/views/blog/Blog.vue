<template>
  <div class="blog">
    <yilan-nav></yilan-nav>
    <div class="banner">
      <el-avatar class="avatar" :size="96" :src="userinfo.avatar">
        {{ (userinfo.username+"").slice(0, 1) }}
      </el-avatar>
      <div class="info">
        <div class="name">{{ userinfo.username | nullFormat }}</div>
        <div class="fan">
          <span>{{ userinfo.fansNum | numberFormat }} 粉丝</span>
          <span>|</span>
          <span>{{ userinfo.follow | numberFormat }} 关注</span>
        </div>
        <div class="btn" v-if="userId != 'me'">
          <el-button
            v-if="!isFollow"
            size="small"
            type="primary"
            @click="followUser"
          >
            关注
          </el-button>
          <el-button v-else size="small" type="danger" @click="unFollowUser">
            不关注
          </el-button>
          <router-link
            :to="'/blog/' + userId + '/letter'"
            style="margin-left: 10px"
          >
            <el-button size="small" type="warning"> 私信 </el-button>
          </router-link>
        </div>
        <div v-else class="btn">@_@</div>
      </div>
      <span class="motto" v-if="userinfo.motto">
        {{ userinfo.motto | lengthFormat }}
        <!-- 我亦可贪恋烟火，殷实人家，几间瓦房，四方小院，守着流年，幸福安康。——白落梅 -->
      </span>
      <span class="motto" v-else>博主有点懒，什么都没留下~</span>
    </div>
    <div class="nav-blog">
      <router-link :to="'/blog/' + userId + '/intro'">简介</router-link>
      <router-link :to="'/blog/' + userId + '/article'">文章</router-link>
      <router-link :to="'/blog/' + userId + '/follow'">关注</router-link>
      <router-link :to="'/blog/' + userId + '/letter'">私信</router-link>
    </div>
    <div class="box">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import nav from "../../components/Nav";

export default {
  name: "Blog",
  components: {
    "yilan-nav": nav,
  },
  props: {
    userId: {
      type: String,
      default: "me",
    },
  },
  data() {
    return {
      userinfo: {},
      isFollow: false,
    };
  },
  computed: {
    login() {
      return this.$store.state.login;
    },
  },
  created() {
    this.getUserInfo();
    this.getFollow();
  },
  watch: {
    userId() {
      this.getUserInfo();
      this.getFollow();
    },
  },
  methods: {
    getUserInfo() {
      let path = "/api/blogsystem/user/profilePage?id=" + this.userId;
      if (this.userId == "me") path = "/api/blogsystem/user/searchInfo";
      this.axios.post(path).then((res) => {
        if (res.data.code == 0) {
          if (this.userId == "me") {
            this.userinfo = res.data.userEntity;
          } else this.userinfo = res.data;
          // console.info(this.userinfo);
        }
      });
    },
    followUser() {
      if(!this.login) {
        this.$message.error("请登录后关注");
        return;
      }
      let path = "/api/blogsystem/user/followOne?to_user_id=" + this.userId;
      this.axios.get(path).then((res) => {
        // console.info(res.data);
        if (res.data.code == 0) {
          this.isFollow = true;
          this.$message.success("关注成功");
        }
      });
    },
    unFollowUser() {
      let path =
        "/api/blogsystem/usertouser/deleteFollow?to_user_id=" + this.userId;
      this.axios.get(path).then((res) => {
        // console.info(res.data);
        if (res.data.code) {
          this.isFollow = false;
          this.$message.success("取消关注成功");
        }
      });
    },
    getFollow() {
      if (this.userId == "me") return;
      if (!this.login) return;
      let path =
        "/api/blogsystem/usertouser/isFollow?to_user_id=" + this.userId;
      this.axios.get(path).then((res) => {
        // console.info(res.data);
        if (res.data.code == 0) {
          this.isFollow = res.data.res != "未关注";
        }
      });
    },
  },
  filters: {
    nullFormat(val) {
      return val ? val : "null";
    },
    numberFormat(val) {
      return val ? Number(val) : 0;
    },
    lengthFormat(val) {
      let length = (val+"").length;
      let maxLen = 50;
      val = length > maxLen ? val.slice(0, maxLen) + "..." : val;
      return val;
    },
  },
};
</script>

<style scoped>
.blog {
  width: 100%;
  min-height: 150vh;
  text-align: left;
  color: var(--white);
}
.blog .banner {
  width: 100%;
  height: 230px;
  padding: 0 100px;
  box-sizing: border-box;
  background-color: var(--background);
  background-size: 100%;
  user-select: none;
}
.blog .banner .avatar {
  border-radius: 50%;
  border: 4px solid var(--background);
  margin: 50px;
  font-size: 28px;
  line-height: 92px !important;
}
.blog .banner .info {
  width: 140px;
  display: inline-block;
  margin-top: 50px;
  line-height: 28px;
  vertical-align: top;
}
.blog .banner .info .name {
  font-size: 18px;
}
.blog .banner .info .fan {
  font-size: 15px;
}
.blog .banner .info .fan span:nth-child(2) {
  font-size: 15px;
  padding: 0 6px;
}
.blog .banner .info .btn {
  margin-top: 8px;
}
.blog .banner .motto {
  width: calc(100% - 550px);
  padding: 8px 0;
  box-sizing: border-box;
  margin-top: 42px;
  margin-left: 120px;
  font-size: 20px;
  line-height: 36px;
  vertical-align: top;
  display: inline-block;
  word-break: break-all;
  opacity: 0.7;
}
.blog .nav-blog {
  width: calc(100% - 200px);
  height: 50px;
  line-height: 50px;
  padding: 0 30px;
  box-sizing: border-box;
  margin: -25px auto;
  border-radius: 4px;
  background: var(--background3);
  color: var(--black);
  user-select: none;
}
.blog .nav-blog a {
  padding: 0 20px;
}
.blog .box {
  width: 100%;
  padding: 45px 100px 20px;
  box-sizing: border-box;
}
</style>