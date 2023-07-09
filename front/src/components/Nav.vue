<template>
  <div class="nav">
    <router-link to="/" class="logo">Yilan</router-link>
    <router-link to="/home" class="div">首页</router-link>
    <router-link to="/type" class="div">分类</router-link>
    <router-link to="/blog/me/intro" class="div">博客</router-link>

    <el-input
      class="search"
      placeholder="搜索文章"
      v-model="search.content"
      @keydown.enter.native="goSearch"
    ></el-input>

    <el-dropdown placement="bottom-start" class="message">
      <i class="icon el-icon-bell"></i>
      <i class="badge"></i>
      <el-dropdown-menu slot="dropdown" class="message-menu">
        <el-dropdown-item class="tabs">
          <el-col :span="24" :class="getMessageActiveClass('notice')">
            <span @click="setMessageActive('notice', $event)">通知</span>
          </el-col>
          <!-- <el-col :span="12" :class="getMessageActiveClass('letter')">
            <span @click="setMessageActive('letter', $event)">私信</span>
          </el-col> -->
        </el-dropdown-item>
        <el-dropdown-item style="height: 42px"></el-dropdown-item>
        <el-dropdown-item v-for="item in notices" :key="item.id" class="item">
          <router-link to="/mine/notice">
            <ul>
              <li>{{ item.content | lengthFormat }}</li>
            </ul>
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item class="btn">
          <!-- <el-col :span="12">标记为已读</el-col> -->
          <el-col :span="12" :offset="12" style="text-align: right">
            <router-link to="/mine/notice">查看更多</router-link>
          </el-col>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <router-link v-if="!login" to="/login" class="login">登录(微信：java_developer_1获取账号)</router-link>
    <span v-if="!login" class="divider">/</span>
    <router-link v-if="!login" to="/regist" class="regist">注册（预览版不可用）</router-link>

    <router-link
      to="/editor"
      v-if="login"
      class="el-icon-plus write"
      target="_blank"
    ></router-link>

    <el-dropdown v-if="login" placement="bottom-start" class="mine">
      <el-avatar :size="30" class="avatar" :src="userinfo.avatar"></el-avatar>
      <el-dropdown-menu slot="dropdown" class="mine-menu">
        <el-dropdown-item>
          <router-link to="/mine/info">
            <i class="el-icon-user"></i>我的
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <router-link to="/help">
            <i class="el-icon-discover"></i>帮助
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <router-link to="/about">
            <i class="el-icon-tickets"></i>关于
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item @click.native="logout">
          <i class="el-icon-switch-button"></i>退出
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Nav",
  components: {},
  data() {
    return {
      search: {
        content: "",
      },
      message: {
        active: "notice",
      },
      userinfo: {
        avatar: null,
      },
      notices: [{}],
    };
  },
  created() {
    if (this.$route.path == "/search") {
      this.search.content = this.stateSearch;
    }
    if (this.login) this.getUserInfo();
    this.getMessageNotice();
  },
  watch: {
    "$store.state.login"(newV) {
      if (newV) this.getUserInfo();
    },
  },
  computed: {
    login() {
      return this.$store.state.login;
    },
    stateSearch() {
      return this.$store.state.search;
    },
  },
  methods: {
    getUserInfo() {
      let path = "/api/blogsystem/user/searchInfo";
      this.axios.post(path).then((res) => {
        if (res.data.code == 0) {
          this.userinfo = res.data.userEntity;
        }
      });
    },
    setMessageActive(val, event) {
      this.message.active = val;
      event.preventDefault();
      event.stopPropagation();
    },
    getMessageActiveClass(val) {
      return this.message.active == val ? "active" : "";
    },
    getMessageNotice() {
      this.axios.get("/api2/blogsystem/notice/noticeList").then((res) => {
        // console.info(res.data);
        if (res.data.code == 0) {
          this.notices = res.data.entities;
          // console.info(this.notices);
        }
      });
    },
    goSearch() {
      if (this.search.content == "") return;
      if (this.$route.path == "/search") {
        this.$store.commit("setSearch", this.search.content);
        return;
      } else {
        this.$store.commit("setSearch", this.search.content);
        this.$router.push("/search");
      }
    },
    logout() {
      localStorage.removeItem("login");
      localStorage.removeItem("token");
      this.$store.commit("setLogin", false);
      this.$router.push("/login");
    },
  },
  filters: {
    lengthFormat(val) {
      let length = (val + "").length;
      let maxLen = 30;
      val = length > maxLen ? val.slice(0, maxLen) + "..." : val;
      return val;
    },
  },
};
</script>

<style scoped>
.nav {
  width: 100%;
  height: 70px;
  padding: 0 100px;
  box-sizing: border-box;
  text-align: right;
  color: var(--white);
  background: var(--background);
  user-select: none;
  /* border-bottom: 1px rgba(255, 255, 255, 0.05) solid; */
}
.nav a {
  font-size: 18px;
  text-decoration: none;
  color: var(--white);
  line-height: 50px;
  padding: 10px 12px;
  cursor: pointer;
  font-weight: 500;
  display: inline-block;
}
.nav * {
  vertical-align: top;
}
.nav .logo {
  font-family: logo;
  font-size: 24px;
  float: left;
  margin-right: 64px;
  color: var(--primary);
}
.nav .div {
  float: left;
  padding: 10px 16px;
}
.nav .search {
  width: 250px;
  margin: 15px 0;
  display: inline-block;
  margin-right: 40px;
}
.nav .write {
  font-size: 24px;
  vertical-align: top;
  line-height: 70px;
  cursor: pointer;
  display: inline-block;
  padding: 0 12px;
}
.nav .divider {
  line-height: 70px;
}
.nav .login,
.nav .regist {
  padding: 0 12px;
  line-height: 70px;
}
</style>
<style>
.nav .el-input__inner {
  background: var(--background);
  border: 1px var(--white) solid;
  outline: var(--white);
  color: var(--white);
}
.el-dropdown-menu {
  background: var(--background) !important;
  color: var(--white) !important;
  border: none;
}
.el-dropdown-menu__item {
  line-height: 42px !important;
  background: var(--background) !important;
  color: var(--white) !important;
}
.el-dropdown-menu__item:hover {
  background: rgba(255, 255, 255, 0.05) !important;
  color: var(--white) !important;
}
.popper__arrow::after {
  border-bottom-color: var(--background) !important;
}
.message {
  width: 48px;
}
.message .icon {
  height: 60px;
  line-height: 70px;
  padding: 0 12px;
  margin-left: -12px;
  font-size: 24px;
  float: left;
  margin-right: 70px;
  color: var(--white);
}
.message .badge {
  height: 8px;
  width: 8px;
  line-height: 18px;
  font-size: 12px;
  font-style: normal;
  color: white;
  padding: 0 !important;
  text-align: center;
  border-radius: 10px;
  background-color: var(--danger);
  border: 1px var(--background) solid;
  transform: scale(0.8);
  position: absolute;
  left: 13px;
  top: 17px;
}
.message-menu {
  width: 300px;
  margin-left: -35px !important;
  padding: 0 !important;
  background: var(--background) !important;
  color: var(--white) !important;
  overflow: hidden;
}
.message-menu .popper__arrow {
  margin-left: 136px !important;
}
.message-menu .tabs {
  padding: 0;
  text-align: center;
  user-select: none;
}
.message-menu .tabs:hover {
  background: var(--background) !important;
}
.message-menu .tabs span {
  width: 100%;
  display: inline-block;
}
.message-menu .tabs .active {
  background: rgba(255, 255, 255, 0.1) !important;
}
.message-menu .item {
  line-height: 22px !important;
  padding-left: 0px !important;
  font-size: 13px !important;
  opacity: 0.9 !important;
  text-align: justify !important;
}
.message-menu .item a {
  display: block;
  width: 100%;
  padding-top: 10px !important;
  padding-bottom: 10px !important;
}
.message-menu .btn {
  opacity: 0.6;
  font-weight: 300;
  border-top: 1px rgba(255, 255, 255, 0.16) solid;
  user-select: none;
}
.message-menu .btn:hover {
  background: var(--background) !important;
}
.message-menu .btn *:nth-child(2) {
  text-align: right;
}
.mine {
  width: 76px;
}
.mine .avatar {
  font-size: 24px;
  vertical-align: top;
  cursor: pointer;
  display: inline-block;
  margin: 20px 20px 10px;
}
.mine-menu {
  width: 92px;
  margin-left: -3px;
  user-select: none;
}
.mine-menu .popper__arrow {
  margin-left: 15px !important;
}
.mine-menu a {
  display: inline-block;
  width: calc(100% + 40px);
  height: 100%;
  margin: 0 -20px;
  padding: 0 20px;
}
</style>
