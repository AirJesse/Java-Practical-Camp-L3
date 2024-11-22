import request from '@/utils/request'

export function add(data) {
  return request.post('client/add', data)
}

export function del(ids) {
  return request.delete('client/delete/' + ids[0])
}

export function edit(data) {
  return request.put('client/update', data)
}

export default { add, edit, del }
