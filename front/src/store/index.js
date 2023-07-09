import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    login: false,
    search: '',
    token: '',
  },
  mutations: {
    setLogin(state, val) {
      state.login = val;
    },
    setSearch(state, val) {
      state.search = val;
    },
    setToken(state, val) {
      state.token = val;
    }
  },
  actions: {},
  modules: {}
})