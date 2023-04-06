import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/companies',
    method: 'get',
    params
  })
}

export function getQRCode(params) {
  return request({
    url: '/companies/getQRCode',
    method: 'get',
    params
  })
}

export function add(companiesForm) {
  return request({
    url: '/companies',
    method: 'post',
    data: companiesForm
  })
}

export function update(companiesForm) {
  return request({
    url: '/companies',
    method: 'put',
    data: companiesForm
  })
}

export function remove(companiesId) {
  return request({
    url: '/companies/' + companiesId,
    method: 'delete'
  })
}
