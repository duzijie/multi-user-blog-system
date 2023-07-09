<template>
  <div class="letter">
    <div class="title">私信</div>
    <div class="content">
      <!-- <p>对于不正当留言，会删除处理。</p> -->
      <p># 欢迎各种留言，技术交流、生活趣事、以及各种吐槽。</p>
      <p># 忌不正当留言，辱骂、不健康、不文明等危险言论。</p>
      <p># 做社会主义新时代好青年！！！</p>
    </div>
    <el-input
      v-if="login && userId != 'me'"
      type="textarea"
      v-model="letter.content"
      :autosize="{ minRows: 5 }"
      placeholder="有什么要跟博主交流吗？"
    ></el-input>
    <el-button
      v-if="login && userId != 'me'"
      type="warning"
      size="medium"
      class="btn"
      @click="addLetter"
    >
      发 送
    </el-button>
    <div v-if="!login && userId != 'me'">请登录后查看</div>
  </div>
</template>

<script>
export default {
  name: "Letter",
  props: {
    userId: {
      type: String,
      default: "me",
    },
  },
  data() {
    return {
      letter: {
        content: "",
        toUserId: "",
      },
    };
  },
  computed: {
    login() {
      return this.$store.state.login;
    },
  },
  methods: {
    addLetter() {
      this.letter.toUserId = this.userId;
      // console.info(this.letter);
      this.axios
        .post("/api/blogsystem/letter/addLetterToUserId", this.letter)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code) {
            this.$message.success("私信发送成功");
            this.letter = {
              content: "",
              toUserId: "",
            };
          } else this.$message.error("私信发送失败");
        })
        .catch(() => {
          this.$message.error("私信发送失败");
        });
    },
  },
};
</script>

<style scoped>
.letter {
  padding: 30px 50px 80px;
  box-sizing: border-box;
  text-align: left;
  background: var(--background3);
  color: var(--black);
  border-radius: 4px;
}
.letter .title {
  color: var(--background);
  font-weight: bold;
}
.letter .content {
  padding: 2px 4px;
  box-sizing: border-box;
  margin: 18px 0;
  border-radius: 0 4px 4px 0;
  font-size: 14px;
}
.letter .content p {
  margin: 8px 0;
}
.letter .btn {
  margin: 10px 0;
  float: right;
}
</style>
<style>
.letter .el-textarea__inner {
  background: var(--background2) !important;
  border: 1px var(--background) solid;
  opacity: 0.8;
}
</style>