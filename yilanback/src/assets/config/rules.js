
window.console.warn = () => {};

let checkEmail = (rule, value, callback) => {
  const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
  setTimeout(() => {
    if (mailReg.test(value)) {
      callback();
    } else {
      callback(new Error("请输入正确的邮箱格式"));
    }
  }, 100);
};

let username = [ // 用户名验证
  {
    required: true,
    message: "请输入用户名",
    trigger: "blur"
  },
  {
    min: 3,
    max: 20,
    message: "请输入3-20位用户名",
    trigger: "blur"
  },
]
let email = [ // 邮箱验证
  {
    required: true,
    message: "请输入邮箱地址",
    trigger: "blur"
  },
  {
    validator: checkEmail,
    trigger: "blur"
  },
]
let password = [ // 密码验证
  {
    required: true,
    message: "请输入密码",
    trigger: "blur"
  },
  {
    min: 6,
    max: 16,
    message: "请输入6-30位密码",
    trigger: "blur"
  },
]
let vcode = [ // 验证码验证
  {
    required: true,
    message: "请输入验证码",
    trigger: "blur"
  },
  // {
  //   min: 6,
  //   max: 6,
  //   message: "请输入6位验证码",
  //   trigger: "blur"
  // },
]

export default {
  username,
  email,
  password,
  vcode,
}