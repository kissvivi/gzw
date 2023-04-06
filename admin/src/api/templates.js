import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/templates',
    method: 'get',
    params
  })
}

export function add(templatesForm) {
  return request({
    url: '/templates',
    method: 'post',
    data: templatesForm
  })
}

export function update(templatesForm) {
  return request({
    url: '/templates',
    method: 'put',
    data: templatesForm
  })
}

export function remove(templatesId) {
  return request({
    url: '/templates/' + templatesId,
    method: 'delete'
  })
}


export function updateStatus(templatesForm) {
  return request({
    url: '/templates/updateStatus',
    method: 'put',
    data: templatesForm
  })
}
