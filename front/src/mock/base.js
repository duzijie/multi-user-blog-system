
import Mock from 'mockjs'

Mock.setup({
  timeout: '200-600'
});

Mock.mock("/api/login", "post", (option) => {
  console.info(option);
});
// 用户名查重
Mock.mock("/api/blogsystem/user/register/info/username", "get", {
  'msg|1': ['success'],
  'code|1': true,
});
// 邮箱查重
Mock.mock("/api/blogsystem/user/register/info/email", "get", {
  'msg|1': ['success'],
  'code|1': true,
});
// 邮箱验证码
Mock.mock("/api/blogsystem/user/register/verification", "post", {
  'msg|1': ['success'],
  'code|1': true,
});
// 邮箱验证码检验
Mock.mock(/\/api\/blogsystem\/user\/register\/verifying/, "get", {
  'msg|1': ['success'],
  'code|1': true,
});
// 用户注册
Mock.mock("/api/blogsystem/user/register/submitInfo", "post", {
  'msg|1': ['success'],
  'code|1': true,
});