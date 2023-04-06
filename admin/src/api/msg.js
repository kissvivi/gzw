import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/msg',
    method: 'get',
    params
  })
}

export function listBy(params) {
  return request({
    url: '/msg/userMsg',
    method: 'get',
    params
  })
}

export function add(msgForm) {
  return request({
    url: '/msg',
    method: 'post',
    data: msgForm
  })
}

export function update(msgForm) {
  return request({
    url: '/msg',
    method: 'put',
    data: msgForm
  })
}

export function remove(msgId) {
  return request({
    url: '/msg/' + msgId,
    method: 'delete'
  })
}


export function updateStatus(msgForm) {
  return request({
    url: '/msg/updateStatus',
    method: 'put',
    data: msgForm
  })
}
