import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)

Vue.use(Router)

/**
 * icon : the icon show in the sidebar
 * hidden : if `hidden:true` will not show in the sidebar
 * redirect : if `redirect:noRedirect` will not redirect in the levelBar
 * noDropDown : if `noDropDown:true` will not has submenu in the sidebar
 * meta : `{ permission: ['a:xx'] }`  will control the page permission
 **/
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/404', component: _import('errorPage/404'), hidden: true },
  { path: '/401', component: _import('errorPage/401'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    icon: 'dashboard',
    noDropDown: true,
    children: [{
      path: 'dashboard',
      name: '控制台',
      component: _import('dashboard/index'),
      meta: { title: 'dashboard', noCache: true }
    }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/companies',
    component: Layout,
    redirect: '/companies/list',
    icon: 'com',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '企业管理',
      component: _import('companies/list'),
      meta: { permission: ['companies:list'] }
    }]
  },
  {
    path: '/taskMain',
    component: Layout,
    redirect: '/taskMain/list',
    icon: 'task1',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '任务管理',
      component: _import('taskMain/list'),
      meta: { permission: ['taskMain:list'] }
    }]
  },
  {
    path: '/taskDetail',
    component: Layout,
    redirect: '/taskDetail/list',
    icon: 'task2',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '任务详情',
      component: _import('taskDetail/list'),
      meta: { permission: ['taskDetail:list'] }
    }]
  },
  {
    path: '/templates',
    component: Layout,
    redirect: '/templates/list',
    icon: 'temp',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '模板管理',
      component: _import('templates/list'),
      meta: { permission: ['templates:list'] }
    }]
  },
  {
    path: '/account',
    component: Layout,
    redirect: '/account/list',
    icon: 'name',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '账户管理',
      component: _import('account/list'),
      meta: { permission: ['account:list'] }
    }]
  },

  {
    path: '/account',
    component: Layout,
    redirect: '/account/detail',
    hidden: true,
    children: [{
      path: 'detail',
      name: '账户中心',
      component: _import('account/detail')
    }]
  },

  {
    path: '/role',
    component: Layout,
    redirect: '/role/list',
    icon: 'role',
    noDropDown: true,
    children: [{
      path: 'list',
      name: '角色管理',
      component: _import('role/list'),
      meta: { permission: ['role:list'] }
    }]
  }
]
