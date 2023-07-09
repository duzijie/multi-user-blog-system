<template>
  <div class="help">
    <yilan-nav></yilan-nav>
    <el-row
      v-for="(val, key, index) in help"
      :key="index"
      :gutter="20"
      class="box"
    >
      <el-col :offset="3" :span="18">
        <div class="title">{{ key }}</div>
        <div class="item" v-for="(item, i) in val" :key="i">
          <div class="question">
            <span>Q: {{ item.question }}</span>
          </div>
          <div class="answer">
            <span>A: {{ item.answer }}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <yilan-foo></yilan-foo>
  </div>
</template>

<script>
import nav from "../../components/Nav";
import foo from "../../components/Foo";

export default {
  name: "Help",
  components: {
    "yilan-nav": nav,
    "yilan-foo": foo,
  },
  data() {
    return {
      help: {
        文章相关: [
          {
            question: "如何添加文章？",
            answer:
              "用户登录系统后，点击右上角[ + ]进入编辑器，按Markdown语法编写正文，点击保存按钮或（ctrl s）保存文章，弹出表单补充相关信息，然后可发布文章或存为草稿。",
          },
          {
            question: "如何修改文章？",
            answer:
              "用户登录系统后，鼠标悬浮右上角用户头像，在下拉菜单中点击[ 我的 ]进入我的管理界面，选择左侧菜单中对应的文章管理模块，找到需要修改的文章，点击[ 编辑 ]进入编辑器编辑。",
          },
          {
            question: "如何删除文章？",
            answer:
              "用户登录系统后，鼠标悬浮右上角用户头像，在下拉菜单中点击[ 我的 ]进入我的管理界面，选择左侧菜单中对应的文章管理模块，找到需要删除的文章，点击[ 删除 ]弹出询问界面，点击确认即可删除文章。",
          },
        ],
        评论相关: [
          {
            question: "如何评论文章？",
            answer:
              "用户登录系统后，在查看某篇文章详细内容的时候，在文章底部可以看到输入框，输入内容后点击[ 发布 ]即可完成回复。",
          },
          {
            question: "如何回复文章评论？",
            answer:
              "用户登录系统后，进入自己的文章，在对于评论可看到[ 回复 ]文字按钮，点击即可看到输入框，输入内容后点击[ 发布 ]即可完成回复。",
          },
          {
            question: "如何删除文章评论？",
            answer:
              "用户登录系统后，进入自己评论的文章，在底部评论区域，可以看到自己评论的内容后面显示有[ 删除 ]文字按钮，点击[ 删除 ]弹出询问界面，点击确认即可删除对应评论。",
          },
        ],
        私信相关: [
          {
            question: "如何给其他用户发私信？",
            answer:
              "用户登录系统后，进入其他用户的博客界面，找到私信模块，输入内容，点击[ 发送 ]即可。",
          },
          {
            question: "如何回复私信？",
            answer:
              "用户登录系统后，在顶部信息列表可以查看到自己的私信，进入[ 我的 ]界面，在私信管理可以看到跟回复私信。",
          }
        ],
      },
    };
  },
  methods: {},
};
</script>

<style scoped>
.help {
  width: 100%;
}
.help .box {
  width: calc(100% - 200px);
  margin: 20px 90px !important;
  border-radius: 4px;
  text-align: left;
  line-height: 30px;
}
.help .box .el-col {
  padding: 20px 0;
  background: var(--background3);
}
.help .title {
  padding: 0 30px;
  box-sizing: border-box;
  font-size: 20px;
  color: var(--background);
}
.help .item {
  line-height: 30px;
  padding: 10px 30px;
  box-sizing: border-box;
}
.help .item .question {
  color: var(--warning);
}
.help .item .answer {
  color: var(--background);
}
</style>