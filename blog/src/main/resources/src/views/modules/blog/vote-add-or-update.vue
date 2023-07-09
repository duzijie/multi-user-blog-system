<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯"></el-input>
    </el-form-item>
    <el-form-item label="鍒涘缓鏃堕棿锛岄粯璁ょ敱鏁版嵁搴撳垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="鍒涘缓鏃堕棿锛岄粯璁ょ敱鏁版嵁搴撳垱寤"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲blog琛?紝缁欐煇鍗氭枃鐐硅禐" prop="blogId">
      <el-input v-model="dataForm.blogId" placeholder="澶栭敭瀵瑰簲blog琛?紝缁欐煇鍗氭枃鐐硅禐"></el-input>
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
          userId: '',
          createTime: '',
          blogId: ''
        },
        dataRule: {
          userId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '鍒涘缓鏃堕棿锛岄粯璁ょ敱鏁版嵁搴撳垱寤不能为空', trigger: 'blur' }
          ],
          blogId: [
            { required: true, message: '澶栭敭瀵瑰簲blog琛?紝缁欐煇鍗氭枃鐐硅禐不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/vote/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userId = data.vote.userId
                this.dataForm.createTime = data.vote.createTime
                this.dataForm.blogId = data.vote.blogId
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
              url: this.$http.adornUrl(`/blogsystem/vote/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'userId': this.dataForm.userId,
                'createTime': this.dataForm.createTime,
                'blogId': this.dataForm.blogId
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
