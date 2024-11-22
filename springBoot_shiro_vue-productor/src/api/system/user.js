import request from '@/utils/request'
export function add(data) {
  return request.post('sys/user', data)
}

export function del(ids) {
  return request.delete('sys/user', { data: ids })
}

export function edit(data) {
  return request.put('sys/user', data)
}

export function editUser(data) {
  return request.put('sys/user/info', data)
}
export function updatePass(user) {
  const data = {
    oldPwd: user.oldPass,
    newPwd: user.newPass
  }
  return request.put('sys//user/pwd', data)
}

export default { add, edit, del }

