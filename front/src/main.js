import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import rules from './assets/config/rules'
import editor from './assets/config/editor'
import './assets/config/element'
import './assets/style/style.css'
import 'element-ui/lib/theme-chalk/index.css'
import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";
// import './mock/index.js'

Vue.use(mavonEditor);

Vue.config.productionTip = false
Vue.prototype.axios = axios
Vue.prototype.rules = rules
Vue.prototype.editor = editor

// http request 拦截器
axios.interceptors.request.use(
  config => {
    let token = localStorage.getItem('token');
    if (token) { //判断token是否存在
      config.headers.Authorization = token; //将token设置成请求头
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

// 监听localStorage修改
window.addEventListener('storage', (e) => {
  localStorage.setItem(e.key, e.oldValue);
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')