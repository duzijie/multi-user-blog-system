<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="濮撳悕锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负20" prop="name">
      <el-input v-model="dataForm.name" placeholder="濮撳悕锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负20"></el-input>
    </el-form-item>
    <el-form-item label="閭??锛屾渶澶ч暱搴︿负50锛屼笖unique" prop="email">
      <el-input v-model="dataForm.email" placeholder="閭??锛屾渶澶ч暱搴︿负50锛屼笖unique"></el-input>
    </el-form-item>
    <el-form-item label="鐢ㄦ埛璐﹀彿锛屾渶灏忛暱搴︿负3锛屾渶澶ч暱搴︿负20锛屼笖unique" prop="username">
      <el-input v-model="dataForm.username" placeholder="鐢ㄦ埛璐﹀彿锛屾渶灏忛暱搴︿负3锛屾渶澶ч暱搴︿负20锛屼笖unique"></el-input>
    </el-form-item>
    <el-form-item label="瀵嗙爜锛屾渶澶ч暱搴︿负100" prop="password">
      <el-input v-model="dataForm.password" placeholder="瀵嗙爜锛屾渶澶ч暱搴︿负100"></el-input>
    </el-form-item>
    <el-form-item label="澶村儚鍥剧墖鍦板潃锛屾渶澶ч暱搴︿负200" prop="avatar">
      <el-input v-model="dataForm.avatar" placeholder="澶村儚鍥剧墖鍦板潃锛屾渶澶ч暱搴︿负200"></el-input>
    </el-form-item>
    <el-form-item label="鐢佃瘽鍙风爜" prop="phone">
      <el-input v-model="dataForm.phone" placeholder="鐢佃瘽鍙风爜"></el-input>
    </el-form-item>
    <el-form-item label="鎬у埆 0-濂?1-鐢" prop="sex">
      <el-input v-model="dataForm.sex" placeholder="鎬у埆 0-濂?1-鐢"></el-input>
    </el-form-item>
    <el-form-item label="" prop="birthday">
      <el-input v-model="dataForm.birthday" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="鍦板潃" prop="address">
      <el-input v-model="dataForm.address" placeholder="鍦板潃"></el-input>
    </el-form-item>
    <el-form-item label="璐﹀彿閿佸畾锛?-姝ｅ父 1-閿佸畾" prop="islock">
      <el-input v-model="dataForm.islock" placeholder="璐﹀彿閿佸畾锛?-姝ｅ父 1-閿佸畾"></el-input>
    </el-form-item>
    <el-form-item label="璐︽埛鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="璐︽埛鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤"></el-input>
    </el-form-item>
    <el-form-item label="涓?汉绠?粙" prop="introduction">
      <el-input v-model="dataForm.introduction" placeholder="涓?汉绠?粙"></el-input>
    </el-form-item>
    <el-form-item label="涓??绛惧悕" prop="motto">
      <el-input v-model="dataForm.motto" placeholder="涓??绛惧悕"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲style琛?紝澶氬?涓?叧绯伙紝涓?釜style瀵瑰簲澶氫釜user" prop="styleId">
      <el-input v-model="dataForm.styleId" placeholder="澶栭敭瀵瑰簲style琛?紝澶氬?涓?叧绯伙紝涓?釜style瀵瑰簲澶氫釜user"></el-input>
    </el-form-item>
    <el-form-item label="鐢ㄦ埛瑙掕壊锛孯OLE_USER-鏅??鐢ㄦ埛 ROLE_ADMIN-绠＄悊鍛?ROLE_SUPER-瓒呯骇绠＄悊鍛" prop="roles">
      <el-input v-model="dataForm.roles" placeholder="鐢ㄦ埛瑙掕壊锛孯OLE_USER-鏅??鐢ㄦ埛 ROLE_ADMIN-绠＄悊鍛?ROLE_SUPER-瓒呯骇绠＄悊鍛"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          name: '',
          email: '',
          username: '',
          password: '',
          avatar: '',
          phone: '',
          sex: '',
          birthday: '',
          address: '',
          islock: '',
          createTime: '',
          introduction: '',
          motto: '',
          styleId: '',
          roles: ''
        },
        dataRule: {
          name: [
            { required: true, message: '濮撳悕锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负20不能为空', trigger: 'blur' }
          ],
          email: [
            { required: true, message: '閭??锛屾渶澶ч暱搴︿负50锛屼笖unique不能为空', trigger: 'blur' }
          ],
          username: [
            { required: true, message: '鐢ㄦ埛璐﹀彿锛屾渶灏忛暱搴︿负3锛屾渶澶ч暱搴︿负20锛屼笖unique不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '瀵嗙爜锛屾渶澶ч暱搴︿负100不能为空', trigger: 'blur' }
          ],
          avatar: [
            { required: true, message: '澶村儚鍥剧墖鍦板潃锛屾渶澶ч暱搴︿负200不能为空', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '鐢佃瘽鍙风爜不能为空', trigger: 'blur' }
          ],
          sex: [
            { required: true, message: '鎬у埆 0-濂?1-鐢不能为空', trigger: 'blur' }
          ],
          birthday: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '鍦板潃不能为空', trigger: 'blur' }
          ],
          islock: [
            { required: true, message: '璐﹀彿閿佸畾锛?-姝ｅ父 1-閿佸畾不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '璐︽埛鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤不能为空', trigger: 'blur' }
          ],
          introduction: [
            { required: true, message: '涓?汉绠?粙不能为空', trigger: 'blur' }
          ],
          motto: [
            { required: true, message: '涓??绛惧悕不能为空', trigger: 'blur' }
          ],
          styleId: [
            { required: true, message: '澶栭敭瀵瑰簲style琛?紝澶氬?涓?叧绯伙紝涓?釜style瀵瑰簲澶氫釜user不能为空', trigger: 'blur' }
          ],
          roles: [
            { required: true, message: '鐢ㄦ埛瑙掕壊锛孯OLE_USER-鏅??鐢ㄦ埛 ROLE_ADMIN-绠＄悊鍛?ROLE_SUPER-瓒呯骇绠＄悊鍛不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/blogsystem/user/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.user.name
                this.dataForm.email = data.user.email
                this.dataForm.username = data.user.username
                this.dataForm.password = data.user.password
                this.dataForm.avatar = data.user.avatar
                this.dataForm.phone = data.user.phone
                this.dataForm.sex = data.user.sex
                this.dataForm.birthday = data.user.birthday
                this.dataForm.address = data.user.address
                this.dataForm.islock = data.user.islock
                this.dataForm.createTime = data.user.createTime
                this.dataForm.introduction = data.user.introduction
                this.dataForm.motto = data.user.motto
                this.dataForm.styleId = data.user.styleId
                this.dataForm.roles = data.user.roles
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/blogsystem/user/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'email': this.dataForm.email,
                'username': this.dataForm.username,
                'password': this.dataForm.password,
                'avatar': this.dataForm.avatar,
                'phone': this.dataForm.phone,
                'sex': this.dataForm.sex,
                'birthday': this.dataForm.birthday,
                'address': this.dataForm.address,
                'islock': this.dataForm.islock,
                'createTime': this.dataForm.createTime,
                'introduction': this.dataForm.introduction,
                'motto': this.dataForm.motto,
                'styleId': this.dataForm.styleId,
                'roles': this.dataForm.roles
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
