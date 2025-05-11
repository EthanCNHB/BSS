// 对外暴露的常量路由
export const constantRoute = [
  // 登录路由
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'), // 登录页面
    name: 'login',
    meta: {
      title: '登录', // 页面标题
      hidden: true, // 是否在菜单中隐藏
      icon: 'Promotion',
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
    path: '/',
    component: () => import('@/layout/index.vue'), // 布局组件
    name: '用户管理',
    meta: {
      title: '',
      hidden: false,
    },

    children: [
      {
        path: '/user-management',
        component: () => import('@/views/user-management/index.vue'), // 用户管理组件
        name: 'userManagement',
        meta: {
          title: '用户管理',
          hidden: false,
          icon: 'User',
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
      hidden: false,
      icon: 'Reading',
    },
    redirect: '/textbook-management/add',
    children: [
      // 添加教材
      {
        path: '/textbook-management/add',
        component: () => import('@/views/textbook-management/add/index.vue'), // 添加教材组件
        name: 'textbookAdd',
        meta: {
          title: '添加教材',
          hidden: false,
          icon: 'CirclePlus',
        },
      },
      // 编辑教材
      {
        path: '/textbook-management/edit',
        component: () => import('@/views/textbook-management/edit/index.vue'), // 编辑教材组件
        name: 'textbookEdit',
        meta: {
          title: '编辑教材',
          hidden: false, // 在菜单中隐藏
          icon: 'Edit',
        },
      },
    ],
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'), // 布局组件
    name: '订单管理',
    meta: {
      title: '',
      hidden: false,
    },
    children: [
      // 征订管理路由
      {
        path: '/subscription-management',
        component: () => import('@/views/subscription-management/index.vue'), // 征订管理组件
        name: 'subscription',
        meta: {
          title: '征订管理',
          icon: 'Tickets',
          hidden: false,
        },
      },
    ],
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'), // 布局组件
    name: '统计分析',
    meta: {
      title: '',
      hidden: false,
    },

    children: [
      // 统计分析路由
      {
        path: '/statistics',
        component: () => import('@/views/statistics/index.vue'), // 统计分析组件
        name: 'statistics',
        meta: {
          title: '统计分析',
          hidden: false,
          icon: 'Histogram',
        },
      },
    ],
  },

  // 404页面路由
  {
    path: '/404',
    component: () => import('@/views/404/index.vue'), // 404 页面组件
    name: '404',
    meta: {
      title: '404', // 页面标题
      hidden: true, // 在菜单中隐藏
    },
  },
  // 捕获所有未匹配的路由并重定向到404页面
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404', // 默认重定向到404页面
    name: 'Any',
    meta: {
      title: '任意路由',
      hidden: true,
    },
  },
]
