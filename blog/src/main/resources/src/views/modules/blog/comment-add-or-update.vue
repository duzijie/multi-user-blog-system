<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="璇勮?鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负200" prop="content">
      <el-input v-model="dataForm.content" placeholder="璇勮?鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负200"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲鐢ㄦ埛琛?紝涓??涓" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="澶栭敭瀵瑰簲鐢ㄦ埛琛?紝涓??涓"></el-input>
    </el-form-item>
    <el-form-item label="璇勮?鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撹嚜鍔ㄥ垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="璇勮?鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撹嚜鍔ㄥ垱寤"></el-input>
    </el-form-item>
    <el-form-item label="鐖惰瘎璁篿d锛堝洖澶嶈瘎璁虹殑鏃跺?鐢ㄥ埌锛夛紝褰揂鏁版嵁鐨刾arent_id绛変簬B鏁版嵁鐨刬d鏃讹紝鍒橞鏁版嵁鏄疉鏁版嵁鐨勭埗璇勮?" prop="parentId">
      <el-input v-model="dataForm.parentId" placeholder="鐖惰瘎璁篿d锛堝洖澶嶈瘎璁虹殑鏃跺?鐢ㄥ埌锛夛紝褰揂鏁版嵁鐨刾arent_id绛変簬B鏁版嵁鐨刬d鏃讹紝鍒橞鏁版嵁鏄疉鏁版嵁鐨勭埗璇勮?"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲blog琛?紝澶氬?涓??搴" prop="blogId">
      <el-input v-model="dataForm.blogId" placeholder="澶栭敭瀵瑰簲blog琛?紝澶氬?涓??搴"></el-input>
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
          content: '',
          userId: '',
          createTime: '',
          parentId: '',
          blogId: ''
        },
        dataRule: {
          content: [
            { required: true, message: '璇勮?鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负200不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '澶栭敭瀵瑰簲鐢ㄦ埛琛?紝涓??涓不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '璇勮?鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撹嚜鍔ㄥ垱寤不能为空', trigger: 'blur' }
          ],
          parentId: [
            { required: true, message: '鐖惰瘎璁篿d锛堝洖澶嶈瘎璁虹殑鏃跺?鐢ㄥ埌锛夛紝褰揂鏁版嵁鐨刾arent_id绛変簬B鏁版嵁鐨刬d鏃讹紝鍒橞鏁版嵁鏄疉鏁版嵁鐨勭埗璇勮?不能为空', trigger: 'blur' }
          ],
          blogId: [
            { required: true, message: '澶栭敭瀵瑰簲blog琛?紝澶氬?涓??搴不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/comment/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.content = data.comment.content
                this.dataForm.userId = data.comment.userId
                this.dataForm.createTime = data.comment.createTime
                this.dataForm.parentId = data.comment.parentId
                this.dataForm.blogId = data.comment.blogId
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
              url: this.$http.adornUrl(`/blogsystem/comment/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'content': this.dataForm.content,
                'userId': this.dataForm.userId,
                'createTime': this.dataForm.createTime,
                'parentId': this.dataForm.parentId,
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
