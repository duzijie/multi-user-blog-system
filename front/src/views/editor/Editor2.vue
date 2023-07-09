<template>
  <div class="editor">
    <yilan-nav></yilan-nav>
    <div class="box">
      <mavon-editor
        ref="editor"
        :toolbars="toolbars"
        v-model="editor.content"
        @imgAdd="imgAdd"
        @imgDel="imgDel"
        @save="saveEdit"
        :boxShadow="false"
        placeholder="开始编辑正文..."
        :tabSize="2"
      >
        <!-- 
        :editable="false"
        :toolbarsFlag="false"
        :subfield="false"
        -->
      </mavon-editor>
    </div>
    <el-dialog
      title="添加文章"
      :visible.sync="dialogVisible"
      width="50%"
      top="10vh"
    >
      <el-form
        label-position="left"
        :model="editor"
        label-width="70px"
        size="medium"
        :rules="rules"
        ref="form"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            type="textarea"
            v-model="editor.title"
            :autosize="{ minRows: 2 }"
          ></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
            type="textarea"
            v-model="editor.summary"
            :autosize="{ minRows: 2 }"
          ></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-cascader
            filterable
            v-model="editor.catalogId"
            :props="optionProps"
            :options="types"
            :show-all-levels="false"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="权限">
          <el-radio v-model="editor.authority" :label="0">公开</el-radio>
          <el-radio v-model="editor.authority" :label="1">好友可见</el-radio>
          <el-radio v-model="editor.authority" :label="2">仅自己</el-radio>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" @click="dialogVisible = false">
          取 消
        </el-button>
        <el-button
          v-if="!blogId"
          size="medium"
          type="warning"
          @click="saveBlog()"
        >
          草 稿
        </el-button>
        <el-button
          v-if="!blogId"
          size="medium"
          type="primary"
          @click="submitBlog()"
        >
          发 布
        </el-button>
        <el-button
          v-if="blogId"
          size="medium"
          type="primary"
          @click="submitBlog()"
        >
          修 改
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import nav from "../../components/Nav";

export default {
  name: "Editor",
  components: {
    "yilan-nav": nav,
  },
  props: {
    blogId: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      editor: {
        title: "",
        summary: "",
        content: "",
        htmlContent: "",
        catalogId: "",
        status: 0,
        authority: 0,
      },
      rules: {
        title: [
          {
            required: true,
            message: "请输入标题",
            trigger: "blur",
          },
        ],
        summary: [
          {
            required: true,
            message: "请输入摘要",
            trigger: "blur",
          },
        ],
      },
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: false, // 全屏编辑
        readmodel: false, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      dialogVisible: false,
      optionProps: {
        value: "id",
        label: "name",
      },
      types: [],
    };
  },
  created() {
    this.getType();
    this.getBlogDetail();
  },
  methods: {
    imgAdd(pos, file) {
      // console.info(pos, file);
      let formdata = new FormData();
      formdata.append("file", file);
      this.axios
        .post("/api/blogsystem/blog/uploadBlogImg", formdata)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code) {
            let url = res.data.imgUrl;
            this.$refs.editor.$img2Url(pos, url);
          }
        });
    },
    imgDel() {},
    saveEdit(val, ran) {
      this.editor.content = val;
      this.editor.htmlContent = ran;
      this.dialogVisible = true;
    },
    getType() {
      this.axios.get("/api/blogsystem/catalog/list/tree").then((res) => {
        if (res.data.code == 0) {
          this.types = res.data.catalogEntityList;
          for (let item1 of this.types) {
            for (let item2 of item1.children) {
              for (let item3 of item2.children) {
                delete item3.children;
              }
            }
          }
        } else this.$message.error(res.data.msg);
      });
    },
    saveBlog() {
      this.editor.status = 1;
      this.submitBlog();
    },
    submitBlog() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (typeof this.editor.catalogId != "number")
            this.editor.catalogId = this.editor.catalogId[2];
          if (!this.editor.catalogId) {
            this.$message.error("请选择类别");
            return;
          }
          // console.info(this.editor);
          if (!this.blogId) {
            this.axios
              .post("/api2/blogsystem/blog/save", this.editor)
              .then((res) => {
                // console.info(res.data);
                if (res.data.code == 0) {
                  if (this.editor.status == 1)
                    this.$message.success("添加草稿成功");
                  else this.$message.success("添加文章成功");
                  this.dialogVisible = false;
                  this.editor = {
                    title: "",
                    summary: "",
                    content: "",
                    htmlContent: "",
                    catalogId: "",
                    status: 0,
                    authority: 0,
                  };
                } else this.$message.error("添加文章失败");
              })
              .catch(() => {
                this.$message.error("添加文章失败");
              });
          } else {
            this.axios
              .post("/api2/blogsystem/blog/update", this.editor)
              .then((res) => {
                // console.info(res.data);
                if (res.data.code == 0) {
                  this.$message.success("修改文章成功");
                  this.dialogVisible = false;
                } else this.$message.error("修改文章失败");
              })
              .catch(() => {
                this.$message.error("修改文章失败");
              });
          }
        } else {
          this.$message.warning("请完善相关信息");
        }
      });
    },
    getBlogDetail() {
      console.info(this.blogId);
      if (!this.blogId) return;
      this.axios
        .get("/api2/blogsystem/blog/info/" + this.blogId)
        .then((res) => {
          if (res.data.code == 0) {
            this.editor = res.data.blog;
            this.editor.authority = Number(this.editor.authority);
          }
        });
    },
  },
};
</script>

<style scoped>
.editor {
  width: 100%;
  height: 100vh;
  box-sizing: border-box;
}
.editor .box {
  width: calc(100% - 200px);
  height: calc(100% - 110px);
  padding: 20px 25px;
  box-sizing: border-box;
  margin: 20px 100px;
  background: var(--background3);
}
</style>
<style>
.markdown-body {
  height: 100% !important;
  width: 100% !important;
  margin: auto !important;
}
.content-input-wrapper {
  padding: 16px 20px !important;
}
.v-show-content {
  padding: 12px 35px 15px !important;
}
.editor .el-dialog {
  text-align: left;
  padding: 10px 15px;
  box-sizing: border-box;
}
</style>