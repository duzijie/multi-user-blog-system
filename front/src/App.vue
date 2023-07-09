<template>
  <div id="app" ref="app">
    <router-view></router-view>
    <div ref="backtop" id="backtop" class="backtop" @click="backtop">
      <span class="el-icon-arrow-up"></span>
    </div>
  </div>
</template>

<script>
export default {
  name: "app",
  data() {
    return {
      timeout: null,
    };
  },
  created() {
    this.initState();
  },
  mounted() {
    this.initScroll();
  },
  methods: {
    initState() {
      let token = localStorage.getItem("token");
      let login = localStorage.getItem("login");
      let time = localStorage.getItem("time");
      if (!time) time = 0;
      if (token) {
        this.$store.commit("setToken", token);
        let diff = Date.now() - time;
        let okTime = 25 * 60000;
        let noTime = 30 * 60000;
        if (diff < okTime) {
          this.timeout = setTimeout(() => {
            this.getNewToken();
          }, okTime - diff);
        }
        if (diff > okTime && diff < noTime) {
          this.getNewToken();
        }
        if (diff > noTime) {
          if (this.$route.paht != "/login");
          this.$router.push("/login");
        }
      }
      if (login) {
        this.$store.commit("setLogin", login);
      }
    },
    initScroll() {
      let div = this.$refs.app;
      div.addEventListener("scroll", () => {
        let backtop = this.$refs.backtop;
        if (backtop) {
          if (div.scrollTop < 70) {
            backtop.style.display = "none";
          } else {
            backtop.style.display = "block";
          }
        }
      });
    },
    backtop() {
      let div = this.$refs.app;
      let speed = div.scrollTop / 40;
      speed += (100 + speed) / (1 + speed);
      let interval = setInterval(() => {
        if (div.scrollTop == 0) {
          clearInterval(interval);
        } else {
          div.scrollTop -= speed;
        }
      }, 20);
    },
    getNewToken() {
      this.axios.get("/api/refreshToken").then((res) => {
        if (res.data.code) {
          localStorage.setItem("time", Date.now());
          localStorage.setItem("token", res.data.newToken);
          this.timeout = setTimeout(() => {
            this.getNewToken();
          }, 25 * 60000);
        }
      });
    },
  },
  destroyed() {
    if (this.timeout) clearTimeout(this.timeout);
  },
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: var(--black);
  margin-top: 60px;
}
#app .backtop {
  width: 40px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  display: none;
  color: var(--white);
  border-radius: 50%;
  background: var(--background);
  position: fixed;
  bottom: 50px;
  right: 50px;
}
</style>
