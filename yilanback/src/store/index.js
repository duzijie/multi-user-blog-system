import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    login: false,
    token: '',
  },
  mutations: {
    setLogin(state, val) {
      state.login = val;
    },
    setToken(state, val) {
      state.token = val;
    },
  },
  actions: {},
  modules: {}
})