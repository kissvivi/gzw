import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/task/main',
    method: 'get',
    params
  })
}

export function add(taskMainForm) {
  return request({
    url: '/task/main',
    method: 'post',
    data: taskMainForm
  })
}

export function cmd(taskMainForm) {
  return request({
    url: '/task/main/cmd',
    method: 'post',
    data: taskMainForm
  })
}

export function update(taskMainForm) {
  return request({
    url: '/task/main',
    method: 'put',
    data: taskMainForm
  })
}

export function remove(taskMainId) {
  return request({
    url: '/task/main/' + taskMainId,
    method: 'delete'
  })
}



export function resetPassword(taskMainId) {
  return request({
    url: '/task/main/resetPassword/' + taskMainId,
    method: 'put'
  })
}
