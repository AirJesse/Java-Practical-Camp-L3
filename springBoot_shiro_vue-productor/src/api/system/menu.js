import request from '@/utils/request'

export function getChildrenMenus(param) {
  return request.post('sys/permissionsForVue', param)
}

export function getMenus(params) {
  return request.get('sys/permission/tree', params)
}

export function getChild(id) {
  return request.get('sys/permission/getChildrenIds', { params: { 'permId': id }})
}

export function getAllMenuSuperior(param) {
  return request.get('sys/permission/allTree', { params: param })
}

export function buildMenus() {
  return request.get('sys/homeForVue')
}

export function add(data) {
  return request.post('sys/permission', data)
}

export function del(ids) {
  return request.post('sys/permission/delete', ids)
}

export function edit(data) {
  return request.put('sys/permission', data)
}

export default { add, edit, del, getMenus, getChild, getChildrenMenus, getAllMenuSuperior }
