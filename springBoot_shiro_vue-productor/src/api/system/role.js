import request from '@/utils/request'

// 获取所有的Role
export function getAll() {
  return request.get('sys/role/getAll')
}

export function add(data) {
  return request.post('sys/role', data)
}

export function get(id) {
  return request.get('sys/role/' + id)
}
export function del(id) {
  return request.delete('sys/role/' + id[0])
}

export function edit(data) {
  return request.put('sys/role', data)
}

export function editMenu(data) {
  return request.post('sys/role/permission', data)
}

export default { add, edit, del, get, editMenu }
