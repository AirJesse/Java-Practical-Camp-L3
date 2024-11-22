import { login, getInfo, logout, refreshToken } from '@/api/login'
import { getToken, getRefreshToken, setToken, setRefreshToken, removeToken, removeRefreshToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    refreshToken: getRefreshToken(),
    user: {},
    roles: [],
    // 第一次加载菜单时用到
    loadMenus: false
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_REFRESHTOKEN: (state, refresh_token) => {
      state.refreshToken = refresh_token
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_LOAD_MENUS: (state, loadMenus) => {
      state.loadMenus = loadMenus
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const rememberMe = userInfo.rememberMe
      return new Promise((resolve, reject) => {
        login(userInfo.username, userInfo.password, userInfo.type).then(res => {
          setToken(res.data.accessToken, rememberMe)
          setRefreshToken(res.data.refreshToken, rememberMe)
          commit('SET_TOKEN', res.data.accessToken)
          commit('SET_REFRESHTOKEN', res.data.refreshToken)
          // 第一次加载菜单时用到， 具体见 src 目录下的 permission.js
          commit('SET_LOAD_MENUS', true)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 刷新token
    RefreshToken({ commit }) {
      return new Promise((resolve, reject) => {
        refreshToken().then(res => {
          setToken(res.data, false)
          commit('SET_TOKEN', res.data)
          // 第一次加载菜单时用到， 具体见 src 目录下的 permission.js
          commit('SET_LOAD_MENUS', false)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          setUserInfo(res, commit)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(res => {
          logOut(commit)
          resolve()
        }).catch(error => {
          logOut(commit)
          reject(error)
        })
      })
    },

    updateLoadMenus({ commit }) {
      return new Promise((resolve, reject) => {
        commit('SET_LOAD_MENUS', false)
      })
    }
  }
}

export const logOut = (commit) => {
  commit('SET_TOKEN', '')
  commit('SET_REFRESHTOKEN', '')
  commit('SET_ROLES', [])
  removeToken()
  removeRefreshToken()
}

export const setUserInfo = (res, commit) => {
  // 如果没有任何权限，则赋予一个默认的权限，避免请求死循环
  if (res.data.roles.length === 0) {
    commit('SET_ROLES', ['ROLE_SYSTEM_DEFAULT'])
  } else {
    commit('SET_ROLES', res.data.roles)
  }
  commit('SET_USER', res.data.user)
}

export default user
