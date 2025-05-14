export const constantRoute = [
  // 登录路由
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'), // 登录页面
    name: 'login',
    meta: {
      title: '登录',
      hidden: true, // 是否在菜单中隐藏
      icon: 'Promotion',
    },
  },
  // 注册路由
  {
    path: '/register',
    component: () => import('@/views/register/index.vue'), // 注册页面组件路径
    name: 'register',
    meta: {
      title: '注册',
      hidden: true, // 不显示在菜单中
      icon: 'Edit',
    },
  },

  // 主要布局路由
  {
    path: '/',
    component: () => import('@/layout/index.vue'), // 布局组件
    name: '主页',
    meta: {
      title: '',
      hidden: false,
      icon: '',
    },
    redirect: '/home', // 默认重定向到首页
    children: [
      // 首页子路由
      {
        path: '/home',
        component: () => import('@/views/home/index.vue'), // 首页组件
        name: 'home',
        meta: {
          title: '首页',
          hidden: false,
          icon: 'House',
        },
      },
    ],
  },

  // 用户管理路由
  {
    path: '/user-management',
    component: () => import('@/layout/index.vue'), // 布局组件
    name: '用户管理',
    meta: {
      title: '用户管理',
      icon: 'User',
      roles: ['admin'], // 仅管理员角色可见
    },
    children: [
      {
        path: '/user-management',
        component: () => import('@/views/user-management/index.vue'), // 用户管理组件
        name: 'userManagement',
        meta: {
          title: '用户管理',
          icon: 'User',
          hidden: false,
        },
      },
    ],
  },

  // 教材管理路由
  {
    path: '/textbook-management',
    component: () => import('@/layout/index.vue'), // 教材管理布局
    name: 'textbookManagement',
    meta: {
      title: '教材管理',
      icon: 'Reading',
      roles: ['admin', 'teacher'], // 仅管理员和教师角色可见
    },
    redirect: '/textbook-management/add',
    children: [
      {
        path: '/textbook-management/add',
        component: () => import('@/views/textbook-management/add/index.vue'),
        name: 'textbookAdd',
        meta: {
          title: '添加教材',
          hidden: false,
          icon: 'CirclePlus',
        },
      },
      {
        path: '/textbook-management/edit',
        component: () => import('@/views/textbook-management/edit/index.vue'),
        name: 'textbookEdit',
        meta: {
          title: '编辑教材',
          hidden: false, // 在菜单中隐藏
          icon: 'Edit',
        },
      },
    ],
  },

  // 订单管理路由
  {
    path: '/subscription-management',
    component: () => import('@/layout/index.vue'),
    name: '订单管理',
    meta: {
      title: '订单管理',
      icon: 'Tickets',
      roles: ['teacher', 'admin'], // 仅教师和管理员角色可见
    },
    children: [
      {
        path: '/subscription-management',
        component: () => import('@/views/subscription-management/index.vue'),
        name: 'subscriptionManagement',
        meta: {
          title: '征订管理',
          icon: 'Tickets',
          hidden: false,
        },
      },
    ],
  },

  // 学院管理员填报预定、教材界面
  {
    path: '/college-admin',
    component: () => import('@/layout/index.vue'),
    name: '学院管理员后台',
    meta: {
      title: '学院管理员后台',
      icon: 'School',
      roles: ['admin'], // 仅管理员角色可见
    },
    children: [
      {
        path: '/college-admin/report-reservation',
        component: () => import('@/views/college-admin/report-reservation.vue'),
        name: 'ReportReservation',
        meta: {
          title: '征订管理填报',
          hidden: false,
        },
      },
      {
        path: '/college-admin/inventory',
        component: () => import('@/views/college-admin/inventory.vue'),
        name: 'InventoryManagement',
        meta: {
          title: '教材库存管理',
          hidden: false,
        },
      },
    ],
  },

  // 统计分析路由
  {
    path: '/statistics',
    component: () => import('@/layout/index.vue'),
    name: '统计分析',
    meta: {
      title: '统计分析',
      icon: 'Histogram',
      roles: ['admin', 'teacher'], // 仅管理员和教师角色可见
    },
    children: [
      {
        path: '/statistics',
        component: () => import('@/views/statistics/index.vue'),
        name: 'statisticsPage',
        meta: {
          title: '统计分析',
          hidden: false,
        },
      },
    ],
  },

  // 404页面路由
  {
    path: '/404',
    component: () => import('@/views/404/index.vue'),
    name: '404',
    meta: {
      title: '404',
      hidden: true,
    },
  },
  {
    path: '/403',
    component: () => import('@/views/403/index.vue'),
    name: '403',
    meta: {
      title: '403',
      hidden: true,
    },
  },
  // 捕获所有未匹配的路由并重定向到404页面
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404',
    name: 'Any',
    meta: {
      title: '任意路由',
      hidden: true,
    },
  },
]
