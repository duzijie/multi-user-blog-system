<template>
  <div class="err404">
    <div class="box">
      <div class="logo">404</div>
      <div class="say">不见了哦，请返回登录页</div>
      
      <router-link to="/">
        <el-button type="primary">返回</el-button>
      </router-link>
      
    </div>
  </div>
</template>

<script>
export default {
  name: "Err404",
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped>
.err404 {
  width: 100%;
  height: 100%;
  background-color: var(--background);
}
.err404 .box {
  width: 672px;
  height: 450px;
  padding: 50px 0;
  box-sizing: border-box;
  margin: -225px -336px;
  position: absolute;
  left: 50%;
  top: 50%;
}
.err404 .box .logo {
  font-family: logo;
  font-size: 100px;
  color: var(--primary);
  letter-spacing: 8px;
}
.err404 .box .say {
  font-family: logo;
  font-size: 24px;
  color: var(--primary);
  margin-bottom: 50px;
}
.err404 .box a {
  display: inline-block;
  margin: 0 12px;
}
</style>