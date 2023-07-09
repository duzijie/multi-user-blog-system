<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="鏍囬?锛岄暱搴︽渶灏忎负2锛屾渶澶т负50" prop="title">
      <el-input v-model="dataForm.title" placeholder="鏍囬?锛岄暱搴︽渶灏忎负2锛屾渶澶т负50"></el-input>
    </el-form-item>
    <el-form-item label="鎽樿?锛岄暱搴︽渶灏忎负2锛屾渶澶т负300" prop="summary">
      <el-input v-model="dataForm.summary" placeholder="鎽樿?锛岄暱搴︽渶灏忎负2锛屾渶澶т负300"></el-input>
    </el-form-item>
    <el-form-item label="鍐呭?锛岄暱搴︽渶灏忎负2" prop="content">
      <el-input v-model="dataForm.content" placeholder="鍐呭?锛岄暱搴︽渶灏忎负2"></el-input>
    </el-form-item>
    <el-form-item label="灏唌d杞?垚html鐨勫唴瀹癸紝闀垮害鏈?皬涓?" prop="htmlContent">
      <el-input v-model="dataForm.htmlContent" placeholder="灏唌d杞?垚html鐨勫唴瀹癸紝闀垮害鏈?皬涓?"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯"></el-input>
    </el-form-item>
    <el-form-item label="鏂囩珷鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="鏂囩珷鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤"></el-input>
    </el-form-item>
    <el-form-item label="鏂囩珷鏈?悗涓??淇?敼鏃堕棿锛屽垵濮嬪?涓虹┖" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="鏂囩珷鏈?悗涓??淇?敼鏃堕棿锛屽垵濮嬪?涓虹┖"></el-input>
    </el-form-item>
    <el-form-item label="璁块棶閲忋?闃呰?閲忥紝鍒濆?鍊间负0" prop="readSize">
      <el-input v-model="dataForm.readSize" placeholder="璁块棶閲忋?闃呰?閲忥紝鍒濆?鍊间负0"></el-input>
    </el-form-item>
    <el-form-item label="璇勮?閲忥紝鍒濆?鍊间负0" prop="commentSize">
      <el-input v-model="dataForm.commentSize" placeholder="璇勮?閲忥紝鍒濆?鍊间负0"></el-input>
    </el-form-item>
    <el-form-item label="鐐硅禐閲忥紝鍒濆?鍊间负0" prop="voteSize">
      <el-input v-model="dataForm.voteSize" placeholder="鐐硅禐閲忥紝鍒濆?鍊间负0"></el-input>
    </el-form-item>
    <el-form-item label="澶栭敭瀵瑰簲鍒嗙被琛?紝涓??涓?叧绯" prop="catalogId">
      <el-input v-model="dataForm.catalogId" placeholder="澶栭敭瀵瑰簲鍒嗙被琛?紝涓??涓?叧绯"></el-input>
    </el-form-item>
    <el-form-item label="鐘舵?锛?-鍙戝竷 1-鑽夌? 2-澶勪簬琚?妇鎶ョ殑瀹℃牳鐘舵? 3-纭??涓鸿繚瑙勬枃绔" prop="status">
      <el-input v-model="dataForm.status" placeholder="鐘舵?锛?-鍙戝竷 1-鑽夌? 2-澶勪簬琚?妇鎶ョ殑瀹℃牳鐘舵? 3-纭??涓鸿繚瑙勬枃绔"></el-input>
    </el-form-item>
    <el-form-item label="鏉冮檺锛?-鍏?紑 1-濂藉弸鍙?? 2-绉佸瘑" prop="authority">
      <el-input v-model="dataForm.authority" placeholder="鏉冮檺锛?-鍏?紑 1-濂藉弸鍙?? 2-绉佸瘑"></el-input>
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
          summary: '',
          content: '',
          htmlContent: '',
          userId: '',
          createTime: '',
          updateTime: '',
          readSize: '',
          commentSize: '',
          voteSize: '',
          catalogId: '',
          status: '',
          authority: ''
        },
        dataRule: {
          title: [
            { required: true, message: '鏍囬?锛岄暱搴︽渶灏忎负2锛屾渶澶т负50不能为空', trigger: 'blur' }
          ],
          summary: [
            { required: true, message: '鎽樿?锛岄暱搴︽渶灏忎负2锛屾渶澶т负300不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '鍐呭?锛岄暱搴︽渶灏忎负2不能为空', trigger: 'blur' }
          ],
          htmlContent: [
            { required: true, message: '灏唌d杞?垚html鐨勫唴瀹癸紝闀垮害鏈?皬涓?不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '鏂囩珷鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '鏂囩珷鏈?悗涓??淇?敼鏃堕棿锛屽垵濮嬪?涓虹┖不能为空', trigger: 'blur' }
          ],
          readSize: [
            { required: true, message: '璁块棶閲忋?闃呰?閲忥紝鍒濆?鍊间负0不能为空', trigger: 'blur' }
          ],
          commentSize: [
            { required: true, message: '璇勮?閲忥紝鍒濆?鍊间负0不能为空', trigger: 'blur' }
          ],
          voteSize: [
            { required: true, message: '鐐硅禐閲忥紝鍒濆?鍊间负0不能为空', trigger: 'blur' }
          ],
          catalogId: [
            { required: true, message: '澶栭敭瀵瑰簲鍒嗙被琛?紝涓??涓?叧绯不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '鐘舵?锛?-鍙戝竷 1-鑽夌? 2-澶勪簬琚?妇鎶ョ殑瀹℃牳鐘舵? 3-纭??涓鸿繚瑙勬枃绔不能为空', trigger: 'blur' }
          ],
          authority: [
            { required: true, message: '鏉冮檺锛?-鍏?紑 1-濂藉弸鍙?? 2-绉佸瘑不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/blogsystem/blog/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.title = data.blog.title
                this.dataForm.summary = data.blog.summary
                this.dataForm.content = data.blog.content
                this.dataForm.htmlContent = data.blog.htmlContent
                this.dataForm.userId = data.blog.userId
                this.dataForm.createTime = data.blog.createTime
                this.dataForm.updateTime = data.blog.updateTime
                this.dataForm.readSize = data.blog.readSize
                this.dataForm.commentSize = data.blog.commentSize
                this.dataForm.voteSize = data.blog.voteSize
                this.dataForm.catalogId = data.blog.catalogId
                this.dataForm.status = data.blog.status
                this.dataForm.authority = data.blog.authority
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
              url: this.$http.adornUrl(`/blogsystem/blog/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'summary': this.dataForm.summary,
                'content': this.dataForm.content,
                'htmlContent': this.dataForm.htmlContent,
                'userId': this.dataForm.userId,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime,
                'readSize': this.dataForm.readSize,
                'commentSize': this.dataForm.commentSize,
                'voteSize': this.dataForm.voteSize,
                'catalogId': this.dataForm.catalogId,
                'status': this.dataForm.status,
                'authority': this.dataForm.authority
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
