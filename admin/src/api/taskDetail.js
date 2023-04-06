import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/task/detail',
    method: 'get',
    params
  })
}

export function add(taskDetailForm) {
  return request({
    url: '/task/detail',
    method: 'post',
    data: taskDetailForm
  })
}

export function update(taskDetailForm) {
  return request({
    url: '/task/detail',
    method: 'put',
    data: taskDetailForm
  })
}

export function remove(taskDetailId) {
  return request({
    url: '/task/detail/' + taskDetailId,
    method: 'delete'
  })
}
