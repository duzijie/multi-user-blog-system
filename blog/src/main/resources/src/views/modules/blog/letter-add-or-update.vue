<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯" prop="fromUserId">
      <el-input v-model="dataForm.fromUserId" placeholder="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯" prop="toUserId">
      <el-input v-model="dataForm.toUserId" placeholder="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯"></el-input>
    </el-form-item>
    <el-form-item label="鐣欒█鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撳垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="鐣欒█鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撳垱寤"></el-input>
    </el-form-item>
    <el-form-item label="鐣欒█鐘舵?锛?-鐣欒█涓嶅睍绀猴紙琚?妇鎶ユ垨鑷??鍒犻櫎绛夊師鍥狅級 1-鍙戝竷鎴愬姛锛" prop="status">
      <el-input v-model="dataForm.status" placeholder="鐣欒█鐘舵?锛?-鐣欒█涓嶅睍绀猴紙琚?妇鎶ユ垨鑷??鍒犻櫎绛夊師鍥狅級 1-鍙戝竷鎴愬姛锛"></el-input>
    </el-form-item>
    <el-form-item label="鐣欒█鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负250" prop="content">
      <el-input v-model="dataForm.content" placeholder="鐣欒█鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负250"></el-input>
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
          createTime: '',
          status: '',
          content: ''
        },
        dataRule: {
          fromUserId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯不能为空', trigger: 'blur' }
          ],
          toUserId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '鐣欒█鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撳垱寤不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '鐣欒█鐘舵?锛?-鐣欒█涓嶅睍绀猴紙琚?妇鎶ユ垨鑷??鍒犻櫎绛夊師鍥狅級 1-鍙戝竷鎴愬姛锛不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '鐣欒█鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负250不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/letter/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.fromUserId = data.letter.fromUserId
                this.dataForm.toUserId = data.letter.toUserId
                this.dataForm.createTime = data.letter.createTime
                this.dataForm.status = data.letter.status
                this.dataForm.content = data.letter.content
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
              url: this.$http.adornUrl(`/blogsystem/letter/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'fromUserId': this.dataForm.fromUserId,
                'toUserId': this.dataForm.toUserId,
                'createTime': this.dataForm.createTime,
                'status': this.dataForm.status,
                'content': this.dataForm.content
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
