import axios from 'axios'
import router from '@/router/routers'
import { Notification } from 'element-ui'
import store from '../store'
import { getToken, getRefreshToken, removeToken, removeRefreshToken } from '@/utils/auth'
import Config from '@/settings'
import Cookies from 'js-cookie'
import api from '../store/modules/api'

// 创建axios实例
const service = axios.create({
  baseURL: api.state.baseApi, // api 的 base_url
  timeout: Config.timeout // 请求超时时间
})
axios.defaults.crossDomain = true
// request拦截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    if (getRefreshToken()) {
      config.headers['refresh_token'] = getRefreshToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    config.headers['Content-Type'] = 'application/json; charset=UTF-8'
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    let code = 0
    code = response.data.code
    if (code === 401002) { // 刷新token
      store.dispatch('RefreshToken').then(() => {
        return service(response.config)
      }).catch(() => {
        removeToken()
        removeRefreshToken()
        Notification.error({
          title: '刷新token失败，请重新登录!',
          duration: 5000
        })
      })
    } else if (code === 0) {
      return response.data
    } else if (code === 401001) { // 凭证过期重新登录
      removeToken()
      removeRefreshToken()
      window.location.hash = '/'
    } else { // 其余的全部重新登录
      Notification.error({
        title: response.data.msg || '系统异常，请重新登录',
        duration: 5000
      })
    }
  },
  error => {
    console.log(error)
    // 兼容blob下载出错json提示
    if (error.response.data instanceof Blob && error.response.data.type.toLowerCase().indexOf('json') !== -1) {
      const reader = new FileReader()
      reader.readAsText(error.response.data, 'utf-8')
      reader.onload = function(e) {
        const errorMsg = JSON.parse(reader.result).message
        Notification.error({
          title: errorMsg,
          duration: 5000
        })
      }
    } else {
      let code = 0
      try {
        code = error.response.data.status
      } catch (e) {
        if (error.toString().indexOf('Error: timeout') !== -1) {
          Notification.error({
            title: '网络请求超时',
            duration: 5000
          })
          return Promise.reject(error)
        }
      }
      console.log(code)
      if (code) {
        if (code === 401) {
          store.dispatch('LogOut').then(() => {
            // 用户登录界面提示
            Cookies.set('point', 401)
            location.reload()
          })
        } else if (code === 403) {
          router.push({ path: '/401' })
        } else {
          const errorMsg = error.response.data.message
          if (errorMsg !== undefined) {
            Notification.error({
              title: errorMsg,
              duration: 5000
            })
          }
        }
      } else {
        Notification.error({
          title: '接口请求失败',
          duration: 5000
        })
      }
    }
    return Promise.reject(error)
  }
)
export default service
