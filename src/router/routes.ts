export const constantRoute = [
  // 登录 & 注册
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    name: 'login',
    meta: { title: '登录', hidden: true, icon: 'Promotion' },
  },
  {
    path: '/register',
    component: () => import('@/views/register/index.vue'),
    name: 'register',
    meta: { title: '注册', hidden: true, icon: 'Edit' },
  },

  // 所有人统一主页
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    name: 'layout',
    meta: { title: '', hidden: false, icon: '' },
    redirect: '/home',
    children: [
      {
        path: '/home',
        component: () => import('@/views/home/index.vue'),
        name: 'home',
        meta: { title: '首页', hidden: false, icon: 'House' },
      },
    ],
  },

  // 教材管理（管理员、教师）
  {
    path: '/textbook-management',
    component: () => import('@/layout/index.vue'),
    name: 'textbookManagement',
    meta: { title: '教材管理', icon: 'Reading', roles: ['admin', 'teacher'] },
    redirect: '/textbook-management/add',
    children: [
      {
        path: '/textbook-management/add',
        component: () => import('@/views/textbook-management/add/index.vue'),
        name: 'textbookAdd',
        meta: { title: '添加教材', hidden: false, icon: 'CirclePlus' },
      },
      {
        path: '/textbook-management/edit',
        component: () => import('@/views/textbook-management/edit/index.vue'),
        name: 'textbookEdit',
        meta: { title: '编辑教材', hidden: false, icon: 'Edit' },
      },
    ],
  },

  // 征订管理
  {
    path: '/subscription-management',
    component: () => import('@/layout/index.vue'),
    name: '征订管理',
    meta: { title: '征订管理', icon: 'Tickets', roles: ['admin', 'student'] },
    redirect: '/subscription-management/index',
    children: [
      {
        path: '/subscription-management/index',
        component: () => import('@/views/subscription-management/index.vue'),
        name: 'subscriptionManagement',
        meta: { title: '征订', icon: 'DocumentAdd', hidden: false },
      },
      {
        path: '/subscription-management/order',
        component: () => import('@/views/subscription-management/order.vue'),
        name: 'orderManagement',
        meta: { title: '订单', icon: 'Ticket', hidden: false },
      },
    ],
  },

  // 学院管理员（实为管理员角色下的功能模块）
  {
    path: '/college-admin',
    component: () => import('@/layout/index.vue'),
    name: '学院管理员后台',
    meta: { title: '学院管理员后台', icon: 'School', roles: ['admin'] },
    children: [
      {
        path: '/college-admin/report-reservation',
        component: () => import('@/views/college-admin/report-reservation.vue'),
        name: 'ReportReservation',
        meta: { title: '征订管理填报', hidden: false },
      },
      {
        path: '/college-admin/inventory',
        component: () => import('@/views/college-admin/inventory.vue'),
        name: 'InventoryManagement',
        meta: { title: '教材库存管理', hidden: false },
      },
    ],
  },

  // 统计分析（管理员、教师）
  {
    path: '/statistics',
    component: () => import('@/layout/index.vue'),
    name: '统计分析',
    meta: { title: '统计分析', icon: 'Histogram', roles: ['admin', 'teacher'] },
    children: [
      {
        path: '/statistics',
        component: () => import('@/views/statistics/index.vue'),
        name: 'statisticsPage',
        meta: { title: '统计分析', icon: 'Histogram', hidden: false },
      },
    ],
  },
  //学生信息管理
  {
    path: '/student',
    component: () => import('@/layout/index.vue'),
    name: 'student',
    meta: { title: '个人信息', icon: 'User', roles: ['student'] },
    children: [
      {
        path: '/student/index',
        component: () => import('@/views/student/index.vue'),
        name: 'studentInfo',
        meta: { title: '个人信息', icon: 'UserFilled', hidden: false },
      },
    ],
  },

  // 通用错误页面
  {
    path: '/404',
    component: () => import('@/views/404/index.vue'),
    name: '404',
    meta: { title: '404', hidden: true },
  },
  {
    path: '/403',
    component: () => import('@/views/403/index.vue'),
    name: '403',
    meta: { title: '403', hidden: true },
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    name: 'Any',
    meta: { title: '任意路由', hidden: true },
  },
]
