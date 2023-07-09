<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="澶栭敭瀵瑰簲user琛" prop="fromUserId">
      <el-input v-model="dataForm.fromUserId" placeholder="澶栭敭瀵瑰簲user琛"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲user琛" prop="toUserId">
      <el-input v-model="dataForm.toUserId" placeholder="澶栭敭瀵瑰簲user琛"></el-input>
    </el-form-item>
    <el-form-item label="鐢ㄦ埛涔嬮棿鐨勫叧绯荤姸鎬佸?锛?-濂藉弸锛?-鐢宠?涓?紝2-fromUser灞忚斀toUser" prop="status">
      <el-input v-model="dataForm.status" placeholder="鐢ㄦ埛涔嬮棿鐨勫叧绯荤姸鎬佸?锛?-濂藉弸锛?-鐢宠?涓?紝2-fromUser灞忚斀toUser"></el-input>
    </el-form-item>
    <el-form-item label="璁板綍鐨勫垱寤烘椂闂达紝榛樿?鐢辨暟鎹?簱鍒涘缓" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="璁板綍鐨勫垱寤烘椂闂达紝榛樿?鐢辨暟鎹?簱鍒涘缓"></el-input>
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
          fromUserId: '',
          toUserId: '',
          status: '',
          createTime: ''
        },
        dataRule: {
          fromUserId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛不能为空', trigger: 'blur' }
          ],
          toUserId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '鐢ㄦ埛涔嬮棿鐨勫叧绯荤姸鎬佸?锛?-濂藉弸锛?-鐢宠?涓?紝2-fromUser灞忚斀toUser不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '璁板綍鐨勫垱寤烘椂闂达紝榛樿?鐢辨暟鎹?簱鍒涘缓不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/usertouser/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.fromUserId = data.userToUser.fromUserId
                this.dataForm.toUserId = data.userToUser.toUserId
                this.dataForm.status = data.userToUser.status
                this.dataForm.createTime = data.userToUser.createTime
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
              url: this.$http.adornUrl(`/blogsystem/usertouser/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'fromUserId': this.dataForm.fromUserId,
                'toUserId': this.dataForm.toUserId,
                'status': this.dataForm.status,
                'createTime': this.dataForm.createTime
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
