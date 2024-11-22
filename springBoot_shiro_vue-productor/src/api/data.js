import request from '@/utils/request'
import qs from 'qs'

export function initData(url, params, method) {
  return request({
    url: url,
    method: method || 'post',
    data: params
  })
}

export function download(url, params, method) {
  return request({
    url: url + '?' + qs.stringify(params, { indices: false }),
    method: method || 'post',
    responseType: 'blob'
  })
}
