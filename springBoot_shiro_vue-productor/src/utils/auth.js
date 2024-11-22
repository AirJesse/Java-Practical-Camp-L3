import Cookies from 'js-cookie'
import Config from '@/settings'

const TokenKey = Config.TokenKey
const refreshTokenKey = Config.refreshTokenKey

export function getToken() {
  return Cookies.get(TokenKey)
}
export function getRefreshToken() {
  return Cookies.get(refreshTokenKey)
}

export function setToken(token, rememberMe) {
  if (rememberMe) {
    return Cookies.set(TokenKey, token, { expires: Config.tokenCookieExpires })
  } else return Cookies.set(TokenKey, token)
}

export function setRefreshToken(refreshtoken, rememberMe) {
  if (rememberMe) {
    return Cookies.set(refreshTokenKey, refreshtoken, { expires: Config.tokenCookieExpires })
  } else return Cookies.set(refreshTokenKey, refreshtoken)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function removeRefreshToken() {
  return Cookies.remove(refreshTokenKey)
}

