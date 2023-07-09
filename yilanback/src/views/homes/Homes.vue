<template>
  <el-container class="home">
    <el-aside :width="left">
      <el-header class="logo" :style="logoStyle">
        <span>Yilan</span>
        <span>管理后台</span>
      </el-header>
      <el-scrollbar style="height: calc(100vh - 60px)">
        <el-menu
          router
          class="menu"
          :default-active="active"
          :collapse="menu.collapse"
          background-color="#303030"
          text-color="#fff"
          active-text-color="#71ACE2"
        >
          <el-menu-item index="/homes">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
          <el-menu-item index="/homes/blog">
            <i class="el-icon-tickets"></i>
            <span slot="title">文章管理</span>
          </el-menu-item>
          <el-menu-item index="/homes/notice">
            <i class="el-icon-bell"></i>
            <span slot="title">公告管理</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header>
        <div class="btn" @click="menu.collapse = !menu.collapse">
          <i class="el-icon-menu"></i>
        </div>
        <el-dropdown class="mine" placement="bottom">
          <span class="el-dropdown-link">
            Admin
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <!-- <el-dropdown-item>
              <router-link to=""></router-link>
              修改密码</el-dropdown-item
            > -->
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "",
  data() {
    return {
      left: "unset",
      menu: {
        collapse: false,
      },
      active: "/homes",
    };
  },
  computed: {
    logoStyle() {
      return {
        width: this.menu.collapse ? "64px" : "200px",
      };
    },
  },
  mounted() {
    this.active = this.$route.path;
  },
  methods: {
    logout() {
      console.info(1234);
      sessionStorage.setItem("login", "false");
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.home {
  width: 100%;
  height: 100%;
  background: var(--background2);
  text-align: left;
}
.home .el-header {
  background: var(--background3);
  line-height: 60px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  user-select: none;
}
.home .el-header .btn {
  width: 60px;
  height: 60px;
  display: inline-block;
  text-align: center;
  margin-left: -20px;
  cursor: pointer;
}
.home .el-header .mine {
  float: right;
  cursor: pointer;
}
.home .el-aside {
  background: var(--background3);
  line-height: 60px;
  user-select: none;
}
.home .menu {
  min-height: calc(100vh - 60px);
  height: 100%;
}
.home .menu:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.home .logo {
  background: var(--background);
  box-sizing: border-box;
  text-align: center;
  color: var(--white);
  font-size: 18px;
  padding: 0 9px;
  overflow: hidden;
  transition: width 0.3s ease-in-out;
}
.home .logo span:first-child {
  font-family: logo;
  font-size: 20px;
}
.home .logo span:nth-child(2) {
  padding-left: 4px;
}
</style>
<style>
.el-scrollbar__wrap {
  overflow-x: hidden;
}
.el-scrollbar__view {
  height: 100%;
}
</style>