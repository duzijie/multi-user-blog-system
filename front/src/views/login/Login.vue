<template>
  <div class="login">
    <div class="nav">
      <router-link to="/">Yilan</router-link>
      <router-link to="/regist">注册（预览版不可用）</router-link>
      <router-link to="/reset">找回</router-link>
    </div>
    <div class="box">
      <div class="logo">Yilan</div>
      <div class="say">易览博客，展示你的风采</div>
      <el-form ref="form" :model="login" :inline="inline" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="login.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="login.password"
            placeholder="密码"
            type="password"
            @keydown.enter.native="submitLogin"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      login: {
        username: "",
        password: "",
      },
      inline: true,
      rules: {
        username: this.rules.username,
        password: this.rules.password,
      },
    };
  },
  methods: {
    submitLogin() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          localStorage.removeItem("login");
          localStorage.removeItem("token");
          this.$store.commit("setLogin", false);
          this.axios
            .post("/api/login", this.login)
            .then((res) => {
              // console.info(res.data);
              if (res.data.code == 0) {
                this.$message.success("登录成功");
                this.$store.commit("setLogin", true);
                this.$store.commit("setToken", res.data.token);
                localStorage.setItem("login", "true");
                localStorage.setItem("time", Date.now());
                localStorage.setItem("token", res.data.token);
                setTimeout(() => {
                  this.$router.push("/home");
                }, 1000);
              } else
                this.$message.error("登录失败，请检查账号密码是否填写正确");
            })
            .catch(() => {
              this.$message.error("登录失败，请检查账号密码是否填写正确");
            });
        } else {
          this.$message.warning("请完善登录信息");
        }
      });
    },
  },
};
</script>

<style scoped>
.login {
  width: 100%;
  height: 100%;
  background-color: var(--background);
  user-select: none;
}
.login .nav {
  width: 100%;
  height: 70px;
  padding: 0 100px;
  box-sizing: border-box;
  text-align: right;
}
.login .nav a {
  font-size: 18px;
  text-decoration: none;
  color: var(--white);
  line-height: 50px;
  padding: 10px 12px;
  cursor: pointer;
  font-weight: 500;
  display: inline-block;
}
.login .nav a:first-child {
  font-family: logo;
  font-size: 24px;
  float: left;
}
.login .box {
  width: 672px;
  height: 450px;
  padding: 50px 0;
  box-sizing: border-box;
  margin: -210px -336px;
  position: absolute;
  left: 50%;
  top: 50%;
}
.login .box .logo {
  font-family: logo;
  font-size: 100px;
  color: var(--primary);
  letter-spacing: 8px;
}
.login .box .say {
  font-family: logo;
  font-size: 24px;
  color: var(--primary);
  margin-bottom: 50px;
}
.login .box .el-input {
  width: 150px;
  display: inline-block;
}
</style>
<style>
.login .box .el-form-item.is-error .el-input__inner {
  border-color: var(--primary);
}
.login .box .el-form-item__error {
  color: var(--primary);
}
</style>