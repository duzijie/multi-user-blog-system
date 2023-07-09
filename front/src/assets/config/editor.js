/* eslint-disable no-unused-vars */

let editorOption = {
  modules: {
    toolbar: [
      // 加粗，斜体，下划线，删除线
      ['bold', 'italic', 'underline', 'strike'],
      //引用，代码块
      ['blockquote', 'code-block','link','image'],
      [ // 几级标题
        {
          'header': 1
        },
        {
          'header': 2
        }
      ],
      [ // 有序列表，无序列表
        {
          'list': 'ordered'
        },
        {
          'list': 'bullet'
        }
      ],
      [ // 下角标，上角标
        {
          'script': 'sub'
        },
        {
          'script': 'super'
        }
      ],
      [ // 缩进
        {
          'indent': '-1'
        },
        {
          'indent': '+1'
        }
      ],
      [ // 标题
        {
          'header': [1, 2, 3, 4, false]
        }
      ],
      [ // 颜色选择
        {
          'color': []
        }, {
          'background': []
        }
      ],
      [ // 居中
        {
          'align': []
        }
      ],
      // 清除样式
      ['clean']
    ]
  }
}

export default editorOption