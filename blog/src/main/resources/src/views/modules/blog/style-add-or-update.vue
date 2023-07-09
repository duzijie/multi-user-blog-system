<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="涓婚?鍚嶇О" prop="name">
      <el-input v-model="dataForm.name" placeholder="涓婚?鍚嶇О"></el-input>
    </el-form-item>
    <el-form-item label="鑳屾櫙鍥剧墖鍦板潃" prop="background">
      <el-input v-model="dataForm.background" placeholder="鑳屾櫙鍥剧墖鍦板潃"></el-input>
    </el-form-item>
    <el-form-item label="瀵艰埅鑹" prop="nav">
      <el-input v-model="dataForm.nav" placeholder="瀵艰埅鑹"></el-input>
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
          background: '',
          nav: ''
        },
        dataRule: {
          name: [
            { required: true, message: '涓婚?鍚嶇О不能为空', trigger: 'blur' }
          ],
          background: [
            { required: true, message: '鑳屾櫙鍥剧墖鍦板潃不能为空', trigger: 'blur' }
          ],
          nav: [
            { required: true, message: '瀵艰埅鑹不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/style/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.style.name
                this.dataForm.background = data.style.background
                this.dataForm.nav = data.style.nav
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
              url: this.$http.adornUrl(`/blogsystem/style/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'background': this.dataForm.background,
                'nav': this.dataForm.nav
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
