<template>
  <div class="intro">
    <el-form label-position="left" label-width="72px">
      <el-form-item label="简介">
        <span>{{ userinfo.introduction | nullFormat }}</span>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Intro",
  props: {
    userId: {
      type: String,
      default: "me",
    },
  },
  data() {
    return {
      userinfo: {},
    };
  },
  created() {
    this.getUserInfo();
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
  },
  filters: {
    nullFormat(val) {
      return val ? val : "null";
    },
  },
};
</script>

<style scoped>
.intro {
  padding: 30px 50px;
  box-sizing: border-box;
  text-align: left;
  background: var(--background3);
  color: var(--black);
  border-radius: 4px;
}
.intro .el-form-item {
  margin-bottom: 0;
}
.intro .el-form-item span {
  padding-right: 20%;
  box-sizing: border-box;
  display: inline-block;
  text-align: justify;
  line-height: 30px;
  margin: 5px 0;
}
</style>