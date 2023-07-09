<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="鍒嗙被鍚嶇О锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负30" prop="name">
      <el-input v-model="dataForm.name" placeholder="鍒嗙被鍚嶇О锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负30"></el-input>
    </el-form-item>
    <el-form-item label="鐖跺垎绫伙紝鑻ョ?A琛屾暟鎹?殑parent_id鍊肩瓑浜庣?B琛屾暟鎹?殑id鍊硷紝璇存槑B琛屾暟鎹?槸A琛屾暟鎹?殑鐖跺垎绫" prop="parentId">
      <el-input v-model="dataForm.parentId" placeholder="鐖跺垎绫伙紝鑻ョ?A琛屾暟鎹?殑parent_id鍊肩瓑浜庣?B琛屾暟鎹?殑id鍊硷紝璇存槑B琛屾暟鎹?槸A琛屾暟鎹?殑鐖跺垎绫"></el-input>
    </el-form-item>
    <el-form-item label="涓夌骇鍒嗙被鐨勫眰绾э紝鍙?彇3涓?暟瀛楋細锛?銆?銆?锛?1浠ｈ〃涓?骇锛堥《绾э級鍒嗙被锛?浠ｈ〃璇ユ暟鎹?槸涓?骇鍒嗙被鐨勫効瀛" prop="catLevel">
      <el-input v-model="dataForm.catLevel" placeholder="涓夌骇鍒嗙被鐨勫眰绾э紝鍙?彇3涓?暟瀛楋細锛?銆?銆?锛?1浠ｈ〃涓?骇锛堥《绾э級鍒嗙被锛?浠ｈ〃璇ユ暟鎹?槸涓?骇鍒嗙被鐨勫効瀛"></el-input>
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
          parentId: '',
          catLevel: ''
        },
        dataRule: {
          name: [
            { required: true, message: '鍒嗙被鍚嶇О锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负30不能为空', trigger: 'blur' }
          ],
          parentId: [
            { required: true, message: '鐖跺垎绫伙紝鑻ョ?A琛屾暟鎹?殑parent_id鍊肩瓑浜庣?B琛屾暟鎹?殑id鍊硷紝璇存槑B琛屾暟鎹?槸A琛屾暟鎹?殑鐖跺垎绫不能为空', trigger: 'blur' }
          ],
          catLevel: [
            { required: true, message: '涓夌骇鍒嗙被鐨勫眰绾э紝鍙?彇3涓?暟瀛楋細锛?銆?銆?锛?1浠ｈ〃涓?骇锛堥《绾э級鍒嗙被锛?浠ｈ〃璇ユ暟鎹?槸涓?骇鍒嗙被鐨勫効瀛不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/catalog/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.catalog.name
                this.dataForm.parentId = data.catalog.parentId
                this.dataForm.catLevel = data.catalog.catLevel
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
              url: this.$http.adornUrl(`/blogsystem/catalog/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'parentId': this.dataForm.parentId,
                'catLevel': this.dataForm.catLevel
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
