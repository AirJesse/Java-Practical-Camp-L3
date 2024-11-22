import request from '@/utils/request'
export function getAuthorize() {
  return request.get('authorize/info')
}

export default { getAuthorize }

