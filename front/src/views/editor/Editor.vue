<template>
  <div class="editor">
    <div class="box">
      <quill-editor v-model="quillEditor.content" :options="editor" @ready="onEditorReady($event)"></quill-editor>

      <el-button class="btn1" type="danger" icon="el-icon-delete" circle></el-button>
      <el-button class="btn2" type="success" icon="el-icon-check" circle></el-button>
    </div>
  </div>
</template>

<script>
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.snow.css";
import "../../assets/style/editor.css";

export default {
  name: "Editor",
  components: {
    quillEditor,
  },
  data() {
    return {
      quillEditor: {
        content: "",
      },
    };
  },
  watch: {
    // 'editor.content'(newV) {
    //   console.log(newV);
    // }
  },
  mounted() {
    // console.log(this.editor);
  },
  methods: {
    onEditorReady() {
      let div = document.querySelector(".ql-editor");
      div.setAttribute("data-placeholder", "");
    },
  },
  beforeDestroy() {
    // alert(1)
  },
};
</script>

<style scoped>
.editor {
  text-align: left;
  width: 100%;
  padding: 20px 100px;
  box-sizing: border-box;
  background: var(--background2);
  position: relative;
}
.editor .box {
  width: 90%;
  max-width: 900px;
  min-height: 500px;
  padding: 20px 25px;
  box-sizing: border-box;
  margin: 0 auto;
  background: var(--background3);
  position: relative;
}
.editor .btn1 {
  position: absolute;
  left: -50px;
  top: 21px;
}
.editor .btn2 {
  position: absolute;
  right: -50px;
  top: 21px;
}
</style>