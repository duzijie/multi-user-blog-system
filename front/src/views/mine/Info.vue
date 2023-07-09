<template>
  <div class="info">
    <el-button type="primary" size="medium" class="edit-btn" @click="toEditInfo"
      >编辑</el-button
    >
    <el-form label-position="left" label-width="70px">
      <el-form-item label="头像">
        <img v-if="userinfo.avatar" :src="userinfo.avatar" />
        <span v-else>null</span>
      </el-form-item>
      <el-form-item label="名称">
        {{ userinfo.name | nullFormat }}
      </el-form-item>
      <el-form-item label="邮箱">
        {{ userinfo.email | nullFormat }}
      </el-form-item>
      <el-form-item label="账号">
        {{ userinfo.username | nullFormat }}
      </el-form-item>
      <!-- <el-form-item label="密码">************</el-form-item> -->
      <el-form-item label="简介">
        <span>{{ userinfo.introduction | nullFormat }}</span>
      </el-form-item>
      <el-form-item label="签名">
        <span>{{ userinfo.motto | nullFormat }}</span>
      </el-form-item>
      <el-form-item label="性别">
        {{ userinfo.sex | nullFormat | sexFormat }}
      </el-form-item>
      <el-form-item label="生日">
        {{ userinfo.birthday | nullFormat | birthdayFormat }}
      </el-form-item>
      <el-form-item label="地址">
        {{ userinfo.address | nullFormat }}
      </el-form-item>
    </el-form>

    <el-dialog
      title="修改个人资料"
      :visible.sync="editVisible"
      width="50%"
      top="8vh"
    >
      <el-form
        label-position="left"
        :model="editinfo"
        label-width="70px"
        size="medium"
      >
        <el-form-item label="头像">
          <el-upload
            ref="upload"
            action="/api/blogsystem/user/updateInfo"
            :show-file-list="false"
            :on-change="uploadChange"
            :before-upload="beforeUpload"
            :http-request="UploadImage"
            :auto-upload="false"
          >
            <img v-if="editinfo.avatar" :src="editinfo.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="editinfo.name"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 5 }"
            v-model="editinfo.introduction"
          ></el-input>
        </el-form-item>
        <el-form-item label="签名">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 5 }"
            maxlength="50"
            show-word-limit
            v-model="editinfo.motto"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="editinfo.sex" :label="1">男</el-radio>
          <el-radio v-model="editinfo.sex" :label="0">女</el-radio>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker
            v-model="editinfo.birthday"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="editinfo.address"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false" size="medium">取 消</el-button>
        <el-button type="primary" @click="submitEdit()" size="medium">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Info",
  data() {
    return {
      userinfo: {},
      editVisible: false,
      editinfo: {},
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.axios.post("/api/blogsystem/user/searchInfo").then((res) => {
        if (res.data.code == 0) {
          this.userinfo = res.data.userEntity;
          this.editinfo = JSON.parse(JSON.stringify(this.userinfo));
          // console.info(this.userinfo);
        }
      });
    },
    toEditInfo() {
      this.editVisible = true;
    },
    submitEdit() {
      if (this.editinfo.avatar) this.$refs.upload.submit();
      else this.submitEditInfo();
    },
    submitEditInfo() {
      this.axios
        .post("/api/blogsystem/user/updateInfo", this.editinfo)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code) {
            this.userinfo = Object.assign(this.userinfo, this.editinfo);
            this.editVisible = false;
            this.$message.success("修改信息成功");
          } else this.$message.error("修改信息失败");
        })
        .catch(() => {
          this.$message.error("修改信息失败");
        });
    },
    uploadChange(file) {
      this.editinfo.avatar = URL.createObjectURL(file.raw);
    },
    UploadImage(param) {
      const formData = new FormData();
      formData.append("file", param.file);
      this.axios
        .post("/api/blogsystem/user/uploadAvatar", formData)
        .then((res) => {
          // console.info("res", res.data);
          if (res.data.code) {
            this.$refs.upload.clearFiles();
            this.editinfo.avatar = res.data.avatar;
            param.onSuccess();
            this.submitEditInfo();
          }
        })
        .catch(() => {
          param.onError();
        });
    },
    beforeUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
  },
  filters: {
    nullFormat(val) {
      return val ? val : "null";
    },
    sexFormat(val) {
      return val == 1 ? "男" : val == 0 ? "女" : val;
    },
    birthdayFormat(val) {
      val = (val + "").replace("T", " ");
      val = val.slice(0, 10);
      return val;
    },
  },
};
</script>

<style scoped>
.info {
  padding: 50px 30px;
  box-sizing: border-box;
  text-align: left;
  background: var(--background3);
}
.info .el-form-item {
  line-height: 30px;
  margin-bottom: 10px;
}
.info .el-form-item span {
  padding-right: 20%;
  box-sizing: border-box;
  display: inline-block;
  text-align: justify;
  line-height: 30px;
  margin: 5px 0;
}
.info .el-form-item img {
  max-width: 100px;
  max-height: 100px;
}
.info .edit-btn {
  z-index: 10;
  position: absolute;
  right: 40px;
  top: 52px;
}
.info .el-dialog__wrapper {
  overflow-y: scroll;
}
.info .avatar {
  margin: 0;
}
</style>
<style>
.info .el-input__count {
  line-height: 16px !important;
}
</style>