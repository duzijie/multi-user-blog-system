import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import rules from './assets/config/rules'
import './assets/style/style.css'
import './assets/config/element'


Vue.config.productionTip = false
Vue.prototype.axios = axios
Vue.prototype.rules = rules

axios.interceptors.request.use(
  config => {
    let token = sessionStorage.getItem('token');
    if (token) { //判断token是否存在
      config.headers.Authorization = token; //将token设置成请求头
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

window.addEventListener('storage', (e) => {
  sessionStorage.setItem(e.key, e.oldValue);
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
