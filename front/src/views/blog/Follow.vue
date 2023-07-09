<template>
  <div class="follow">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in userList" :key="item.id">
        <router-link :to="'/blog/' + item.id + '/intro'" class="item">
          <el-avatar :size="50" :src="item.avatar"></el-avatar>
          <div class="name">
            {{ item.username }}
          </div>
        </router-link>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Follow",
  props: {
    userId: {
      type: String,
      default: "me",
    },
  },
  data() {
    return {
      userList: [],
    };
  },
  created() {
    this.getUserList();
  },
  methods: {
    getUserList() {
      let path = "/api/blogsystem/user/searchToUserByUserId?id=" + this.userId;
      let path2 = "/api/blogsystem/user/searchInfo";

      if (this.userId == "me") {
        this.axios.post(path2).then((res) => {
          if (res.data.code == 0) {
            let userId = res.data.userEntity.id;
            path = "/api/blogsystem/user/searchToUserByUserId?id=" + userId;
            this.axios.get(path).then((res) => {
              if (res.data.code == 0) {
                this.userList = res.data.userEntityList;
                console.info(this.userList);
              }
            });
          }
        });
      } else {
        this.axios.get(path).then((res) => {
          if (res.data.code == 0) {
            this.userList = res.data.userEntityList;
            console.info(this.userList);
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.follow {
  box-sizing: border-box;
  text-align: left;
  color: var(--black);
  border-radius: 4px;
}
.follow .item {
  width: 100%;
  height: 100px;
  display: block;
  border-radius: 4px;
  background: var(--background3);
}
.follow .item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.follow .item .el-avatar {
  margin: 25px 16px;
  display: inline-block;
  vertical-align: top;
}
.follow .item .name {
  display: inline-block;
  line-height: 100px;
  font-size: 19px;
  color: var(--background);
}
</style>