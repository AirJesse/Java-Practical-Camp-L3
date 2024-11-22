import request from '@/utils/request'

export function getDepts(params) {
  return request.get('sys/dept/tree', params)
}

export function getAllDeptSuperior(param) {
  return request.get('sys/dept/allTree', { params: param })
}

export function getChildrenDept(param) {
  return request.post('sys/deptForVue', param)
}

export function add(data) {
  return request.post('sys/dept', data)
}

export function del(ids) {
  return request.post('sys/dept/delete', ids)
}

export function edit(data) {
  return request.put('sys/dept', data)
}

export default { add, edit, del, getDepts, getChildrenDept, getAllDeptSuperior }
