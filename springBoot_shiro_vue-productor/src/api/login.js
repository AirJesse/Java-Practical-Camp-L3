import request from '@/utils/request'

export function login(username, password, type) {
  const data = {
    username,
    password,
    type
  }
  return request.post('sys/user/login', data)
}

export function getInfo() {
  return request.post('sys/user/getUserForVUE')
}

export function refreshToken() {
  return request.get('sys/user/token')
}

export function logout() {
  return request.get('sys/user/logout')
}
