import request from '@/utils/request'
export function add(data) {
  return request.post('bank/add', data)
}
export function find() {
  return request.post('bank/pageInfo')
}

export default { add, find }

