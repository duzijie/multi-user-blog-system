<template>
  <div class="letter">
    <el-row :gutter="10">
      <el-col :span="6">
        <div class="list">
          <div
            class="item"
            :class="select == item ? 'active' : ''"
            v-for="item in users"
            :key="item.id"
            @click="selectUser(item)"
          >
            {{ item.username }}
          </div>
        </div>
      </el-col>
      <el-col :span="18">
        <div class="room">
          <div class="header">
            {{ select.username }}
            <span>我</span>
          </div>
          <div class="body">
            <div
              class="item"
              v-for="item in letters"
              :key="item.id"
              :class="item.toUserId == select.id ? 'right' : ''"
              :style="{ textAlign: item.toUserId == select.id ? 'right' : '' }"
            >
              <span>{{ item.content }}</span>
            </div>
          </div>
          <div class="input">
            <el-input
              type="textarea"
              resize="none"
              v-model="letter.content"
              :autosize="{ minRows: 3, maxRows: 3 }"
              placeholder="Shift Enter发送"
              @keydown.shift.enter.native="addLetter"
            ></el-input>
          </div>
          <div class="default" v-if="select.username == 'Yilan'">
            <span>Hello {{ user.username }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Letter",
  data() {
    return {
      users: [
        {
          username: "Yilan",
        },
      ],
      select: {
        username: "Yilan",
      },
      letters: [],
      letter: {
        content: "",
        toUserId: "",
      },
      user: {},
      interval: "",
    };
  },
  created() {
    this.getLetterUser();
    this.getUserInfo();
    this.setInterval();
  },
  methods: {
    setInterval() {
      this.interval = setInterval(() => {
        this.getLetterDetail();
      }, 60 * 1000);
    },
    addLetter() {
      this.letter.toUserId = this.select.id;
      // console.info(this.letter);
      this.axios
        .post("/api/blogsystem/letter/addLetterToUserId", this.letter)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code) {
            this.$message.success("私信发送成功");
            this.getLetterDetail();
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
    getLetterUser() {
      this.axios
        .post("/api/blogsystem/letter/searchUsersByToken")
        .then((res) => {
          // console.info(res.data);
          if (res.data.code == 0) {
            this.users = res.data.res;
            if (this.users) {
              this.select = this.users[0];
              this.getLetterDetail();
            }
            // console.info(this.users);
          }
        });
    },
    selectUser(item) {
      if (this.select == item) return;
      this.select = item;
      this.getLetterDetail();
    },
    getLetterDetail() {
      let path =
        "/api/blogsystem/letter/showLettersByUserId?userId=" + this.select.id;
      // console.info(this.select.id);
      this.axios.post(path).then((res) => {
        // console.info(res.data);
        if (res.data.code == 0) {
          this.letters = res.data.res;
          // console.info(this.letters);
        }
      });
    },
    getUserInfo() {
      this.axios.post("/api/blogsystem/user/searchInfo").then((res) => {
        if (res.data.code == 0) {
          this.user = res.data.userEntity;
          // console.info(this.user);
        }
      });
    },
  },
  destroyed() {
    clearInterval(this.interval);
  },
};
</script>

<style scoped>
.letter {
  box-sizing: border-box;
  text-align: left;
  font-size: 15px;
}
.letter .list {
  background: var(--background3);
  border-radius: 4px;
  box-sizing: border-box;
  padding: 10px 10px;
  user-select: none;
}
.letter .list .item {
  padding: 10px 10px;
  box-sizing: border-box;
  cursor: pointer;
}
.letter .list .item.active {
  color: var(--primary);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.letter .room {
  height: calc(100vh - 120px);
  background: var(--background3);
  border-radius: 4px;
  box-sizing: border-box;
  padding: 50px 0 96px;
  position: relative;
}
.letter .room .header {
  width: 100%;
  height: 50px;
  line-height: 30px;
  font-size: 16px;
  box-sizing: border-box;
  padding: 10px 20px;
  /* border-radius: 4px 4px 0 0; */
  background: var(--background);
  color: whitesmoke;
  position: absolute;
  left: 0;
  top: 0;
}
.letter .room .header span {
  float: right;
}
.letter .room .body {
  width: 100%;
  height: 100%;
  padding: 10px 20px;
  box-sizing: border-box;
  overflow-y: scroll;
  overflow-x: hidden;
  scrollbar-width: thin;
}
.letter .room .item span {
  background: var(--background2);
  display: inline-block;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 8px 10px;
  margin: 5px 0;
}
.letter .room .item.right span {
  color: whitesmoke;
  background: var(--background);
  opacity: 0.64;
}
.letter .room .input {
  width: 100%;
  padding: 10px 30px 10px 20px;
  box-sizing: border-box;
  background: var(--background3);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  position: absolute;
  bottom: 0;
  left: 0;
}
.letter .room .default {
  width: 100%;
  height: 100%;
  text-align: center;
  font-size: 46px;
  color: var(--background);
  line-height: 400px;
  position: absolute;
  left: 0;
  top: 0;
}
</style>