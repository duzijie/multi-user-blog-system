<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="鏍囬?" prop="title">
      <el-input v-model="dataForm.title" placeholder="鏍囬?"></el-input>
    </el-form-item>
    <el-form-item label="鍐呭?" prop="content">
      <el-input v-model="dataForm.content" placeholder="鍐呭?"></el-input>
    </el-form-item>
    <el-form-item label="鍏?憡鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="鍏?憡鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤"></el-input>
    </el-form-item>
    <el-form-item label="鍙戝竷鐘舵?锛?-涓嶅睍绀猴紙涓嶇?浠?箞鍘熷洜锛?1-灞曠ず" prop="status">
      <el-input v-model="dataForm.status" placeholder="鍙戝竷鐘舵?锛?-涓嶅睍绀猴紙涓嶇?浠?箞鍘熷洜锛?1-灞曠ず"></el-input>
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
          title: '',
          content: '',
          createTime: '',
          status: ''
        },
        dataRule: {
          title: [
            { required: true, message: '鏍囬?不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '鍐呭?不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '鍏?憡鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '鍙戝竷鐘舵?锛?-涓嶅睍绀猴紙涓嶇?浠?箞鍘熷洜锛?1-灞曠ず不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/notice/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.title = data.notice.title
                this.dataForm.content = data.notice.content
                this.dataForm.createTime = data.notice.createTime
                this.dataForm.status = data.notice.status
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
              url: this.$http.adornUrl(`/blogsystem/notice/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'content': this.dataForm.content,
                'createTime': this.dataForm.createTime,
                'status': this.dataForm.status
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
