<template>
  <div class="mine">
    <yilan-nav></yilan-nav>
    <div class="box">
      <el-row :gutter="20" class="default">
        <el-col :span="5" class="left">
          <el-menu router :default-active="$route.path">
            <el-menu-item-group>
              <template slot="title">
                <span>消息中心</span>
              </template>
              <el-menu-item index="/mine/notice">
                <i class="el-icon-bell"></i>
                <span slot="title">公告通知</span>
              </el-menu-item>
              <el-menu-item index="/mine/letter">
                <i class="el-icon-message"></i>
                <span slot="title">私信交流</span>
              </el-menu-item>
            </el-menu-item-group>

            <el-menu-item-group>
              <template slot="title">
                <span>基本设置</span>
              </template>
              <el-menu-item index="/mine/info">
                <i class="el-icon-user"></i>
                <span slot="title">个人资料</span>
              </el-menu-item>
              <el-menu-item index="/mine/account">
                <i class="el-icon-postcard"></i>
                <span slot="title">账号管理</span>
              </el-menu-item>
            </el-menu-item-group>

            <el-menu-item-group>
              <template slot="title">
                <span>文章管理</span>
              </template>
              <el-menu-item index="/mine/blog/publish">
                <i class="el-icon-reading"></i>
                <span slot="title">已发布</span>
              </el-menu-item>
              <el-menu-item index="/mine/blog/draft">
                <i class="el-icon-edit-outline"></i>
                <span slot="title">草稿箱</span>
              </el-menu-item>
              <el-menu-item index="/mine/blog/check">
                <i class="el-icon-coordinate"></i>
                <span slot="title">审核中</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-menu>
        </el-col>
        <el-col :span="19" class="right">
          <router-view></router-view>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import nav from "../../components/Nav";

export default {
  name: "Mine",
  components: {
    "yilan-nav": nav,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped>
.mine {
  width: 100%;
}
.mine .box {
  width: 100%;
  padding: 20px 100px;
  box-sizing: border-box;
}
.mine .left .el-menu {
  padding: 8px 0 20px;
  text-align: left;
  border-radius: 4px;
  border: none;
}
.mine .right {
  min-height: 100px;
  border-radius: 4px;
}
</style>