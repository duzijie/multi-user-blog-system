<template>
  <div class="regist">
    <div class="nav">
      <router-link class="back el-icon-back" to="/login">返回</router-link>
    </div>
    <div class="box">
      <div class="logo">Yilan</div>
      <div class="say">易览博客，展示你的风采</div>
    </div>
    <el-form ref="form" :model="regist" :rules="rules" label-width="70px" size="small">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="regist.username" placeholder="自定义用户名"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮 箱">
        <el-input v-model="regist.email" placeholder="邮箱地址"></el-input>
      </el-form-item>
      <el-form-item prop="vcode" label="验证码">
        <el-col :span="17">
          <el-input v-model="regist.vcode" placeholder="验证码"></el-input>
        </el-col>
        <el-col :span="6" :offset="1">
          <el-button type="primary" @click="getVCode" :disabled="email.disabled">
            <span>{{ email.button }}</span>
          </el-button>
        </el-col>
      </el-form-item>
      <el-form-item prop="password" label="密 码">
        <el-input v-model="regist.password" placeholder="用户密码" type="password"></el-input>
      </el-form-item>
      <el-form-item prop="password2" label="密 码">
        <el-input
          v-model="regist.password2"
          placeholder="确认密码"
          type="password"
          @keydown.native.enter="submitRegist"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitRegist">立即注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Regist",
  data() {
    return {
      regist: {
        username: "",
        email: "",
        password: "",
        password2: "",
        vcode: "",
      },
      rules: {
        username: this.rules.username.concat([
          {
            validator: (rule, value, callback) => {
              if (value.length < 3) {
                callback();
                return;
              }
              this.axios
                .get("/api/blogsystem/user/register/info/username", {
                  params: {
                    username: value,
                  },
                })
                .then((res) => {
                  if (!res.data.code) {
                    callback(new Error("该用户名已被注册"));
                  } else {
                    callback();
                  }
                });
            },
            trigger: "blur",
          },
        ]),
        email: this.rules.email,
        password: this.rules.password,
        password2: this.rules.password.concat([
          {
            validator: (rule, value, callback) => {
              if (this.regist.password2 == this.regist.password) callback();
              else callback(new Error("密码不一致"));
            },
            trigger: "blur",
          },
        ]),
        vcode: this.rules.vcode,
      },
      email: {
        button: "获取",
        disabled: false,
      },
    };
  },
  watch: {
    "email.button"() {
      // 将变化保存，防止刷新重置
      localStorage.setItem("email-button", this.email.button);
    },
    "regist.email"() {
      this.checkEmail();
    },
  },
  created() {
    this.initEmailButton();
  },
  methods: {
    // 保证倒计时不被刷新
    initEmailButton() {
      let val = localStorage.getItem("email-button");
      if (val) {
        if (val != "获取") {
          this.email.button = Number(val);
          this.email.disabled = true;
          this.countDown();
        } else this.email.button = val;
      }
    },
    // 用户提交注册
    submitRegist() {
      this.$refs.form.validate((valid) => {
        // 判断表单是否通过验证
        if (valid) {
          this.axios
            .get(
              "/api/blogsystem/user/register/verifying/" +
                this.regist.email +
                "/" +
                this.regist.vcode
            )
            .then((res) => {
              if (res.data.code) {
                // 提交注册
                this.axios
                  .post("/api/blogsystem/user/register/submitInfo", {
                    email: this.regist.email,
                    username: this.regist.username,
                    password: this.regist.password,
                  })
                  .then((res) => {
                    if (res.data.code) {
                      this.$message("已成功注册，3s后前往登录");
                      setTimeout(() => {
                        this.$router.push("/login");
                      }, 3000);
                    } else {
                      this.$message.error("注册失败,请稍后再试");
                    }
                  });
              } else {
                this.$message.error("验证码错误，请重新填写");
              }
            });
        } else {
          this.$message.warning("请完善注册信息");
        }
      });
    },
    // 获取邮箱验证码
    getVCode() {
      this.$refs.form.validateField("email", (valid) => {
        // 判断邮箱是否符合格式
        if (!valid) {
          // 判断邮箱是否已被使用
          this.axios
            .get("/api/blogsystem/user/register/info/email", {
              params: {
                email: this.regist.email,
              },
            })
            .then((res) => {
              if (res.data.code) {
                // 判断是否允许点击，点击后60s倒计时
                if (!this.email.disabled) {
                  this.email.button = 60;
                  this.email.disabled = true;
                } else return;
                this.countDown();
                // 向服务器请求发送验证码
                this.axios
                  .post("/api/blogsystem/user/register/verification", {
                    email: this.regist.email,
                  })
                  .then((res) => {
                    console.info(res.data);
                    if (res.data.code) {
                      this.$message("验证码已发送，请注意查收");
                    } else {
                      this.$message.error("验证码获取失败,请稍后再试");
                    }
                  });
              } else {
                this.$message.error("该邮箱已被注册");
              }
            });
        }
      });
    },
    // 60s倒计时
    countDown() {
      let interval = setInterval(() => {
        this.email.button -= 1;
        if (this.email.button <= 0) {
          clearInterval(interval);
          this.email.button = "获取";
          this.email.disabled = false;
        }
      }, 1000);
    },
    checkEmail() {
      // 判断是否符合邮箱格式
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
      if (!mailReg.test(this.regist.email)) return;
      // 检验邮箱是否注册
      this.axios
        .get("/api/blogsystem/user/register/info/email", {
          params: {
            email: this.regist.email,
          },
        })
        .then((res) => {
          if (!res.data.code) {
            this.$message.error("该邮箱已被注册");
          }
        });
    },
  },
};
</script>

<style scoped>
.regist {
  width: 100%;
  height: 100%;
  background-color: var(--background);
  user-select: none;
}
.regist .nav {
  width: 100%;
  height: 70px;
  padding: 0 100px;
  box-sizing: border-box;
  text-align: right;
}
.regist .nav a {
  font-size: 18px;
  text-decoration: none;
  color: var(--white);
  line-height: 50px;
  padding: 10px 12px;
  cursor: pointer;
  font-weight: 500;
  display: inline-block;
}
.regist .nav .back {
  width: 64px;
  height: 40px;
  text-align: center;
  line-height: 40px;
  padding: 0;
  box-sizing: border-box;
  margin: 15px 0;
  border-radius: 4px;
  display: block;
  opacity: 0.8;
  float: left;
}
.regist .box {
  height: 250px;
  width: 360px;
  padding: 50px 0;
  text-align: right;
  box-sizing: border-box;
  margin: -200px -180px;
  position: absolute;
  left: 26%;
  top: 50%;
}
.regist .box .logo {
  font-family: logo;
  font-size: 100px;
  color: var(--primary);
  letter-spacing: 8px;
}
.regist .box .say {
  font-family: logo;
  font-size: 24px;
  color: var(--primary);
  margin-bottom: 30px;
}
</style>
<style>
.regist .el-form {
  width: 360px;
  padding: 40px 36px 20px 26px;
  box-sizing: border-box;
  position: absolute;
  margin: -230px -210px;
  border-radius: 8px;
  background: white;
  position: absolute;
  right: 29%;
  top: 50%;
}
.regist .el-form .el-button {
  width: 100%;
}
.regist .box .el-form-item.is-error .el-input__inner {
  border-color: var(--primary);
}
.regist .box .el-form-item__error {
  color: var(--primary);
}
</style>