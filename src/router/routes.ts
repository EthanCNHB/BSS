export const constantRoute = [
  // —— 公共页 —— //
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

  // —— 主框架 & 首页 —— //
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    name: 'layout',
    redirect: '/home',
    meta: { hidden: false },
    children: [
      {
        path: '/home',
        component: () => import('@/views/home/index.vue'),
        name: 'home',
        meta: { title: '首页', icon: 'House', roles: 'teacher,student,admin' },
      },
    ],
  },
  //学院管理员主页
  {
    path: '/collge-management',
    component: () => import('@/layout/index.vue'),
    name: 'collegeManagementHome',
    meta: { title: '主页', icon: 'House', roles: ['collegeAdmin'] },
    children: [
      {
        path: '/collegeHome',
        component: () => import('@/views/college-admin/home.vue'),
        name: 'collegeHome',
        meta: { title: '管理员首页', icon: 'House' },
      },
    ],
  },
  // —— 课程管理（教师） —— //
  {
    path: '/course-teacher',
    component: () => import('@/layout/index.vue'),
    name: 'teacherCourseManagement',
    meta: { title: '课程管理', icon: 'Notebook', roles: ['teacher'] },
    children: [
      {
        path: '/list',
        component: () => import('@/views/course-teacher/listOnly.vue'),
        name: 'teacherCourseList',
        meta: { title: '我的课程', icon: 'List' },
      },
      {
        path: '/edit',
        component: () => import('@/views/course-teacher/editOnly.vue'),
        name: 'teacherCourseEdit',
        meta: { title: '新增课程', icon: 'CirclePlus' },
      },
    ],
  },

  // —— 课程管理（管理员） —— //
  {
    path: '/course-management',
    component: () => import('@/layout/index.vue'),
    name: 'courseManagement',
    redirect: '/course-management/list',
    meta: { title: '课程管理', icon: 'Notebook', roles: ['admin', 'collegeAdmin'] },
    children: [
      {
        path: 'list',
        component: () => import('@/views/course-management/list.vue'),
        name: 'courseList',
        meta: { title: '课程列表', icon: 'List' },
      },
      {
        path: 'edit',
        component: () => import('@/views/course-management/edit.vue'),
        name: 'courseEdit',
        meta: { title: '新增/编辑课程', icon: 'CirclePlus' },
      },
    ],
  },

  // —— 教材管理（管理员、教师） —— //
  {
    path: '/textbook-management',
    component: () => import('@/layout/index.vue'),
    name: 'textbookManagement',
    redirect: '/textbook-management/add',
    meta: { title: '教材管理', icon: 'Reading', roles: ['admin', 'teacher'] },
    children: [
      {
        path: 'add',
        component: () => import('@/views/textbook-management/add/index.vue'),
        name: 'textbookAdd',
        meta: { title: '添加教材', icon: 'CirclePlus' },
      },
      {
        path: 'edit',
        component: () => import('@/views/textbook-management/edit/index.vue'),
        name: 'textbookEdit',
        meta: { title: '编辑教材', icon: 'Edit' },
      },
      {
        path: 'list',
        component: () => import('@/views/textbook-management/list/index.vue'),
        name: 'textbookList',
        meta: { title: '教材列表', icon: 'List' },
      },
    ],
  },

  // —— 征订管理（学生） —— //
  {
    path: '/subscription-management',
    component: () => import('@/layout/index.vue'),
    name: 'subscriptionManagement',
    redirect: '/subscription-management/list',
    meta: { title: '征订管理', icon: 'Tickets', roles: ['student'] },
    children: [
      {
        path: 'list',
        component: () => import('@/views/subscription-student/list.vue'),
        name: 'subscriptionList',
        meta: { title: '征订列表', icon: 'ZoomIn' },
      },
      {
        path: 'order',
        component: () => import('@/views/subscription-student/order.vue'),
        name: 'orderManagement',
        meta: { title: '订单管理', icon: 'Ticket' },
      },
    ],
  },

  // —— 选课管理（学生） —— //
  {
    path: '/course-selection',
    component: () => import('@/layout/index.vue'),
    name: 'courseSelection',
    redirect: '/course-selection/list',
    meta: { title: '选课管理', icon: 'Select', roles: ['student'] },
    children: [
      {
        path: 'list',
        component: () => import('@/views/course-selection/list.vue'),
        name: 'courseSelectionList',
        meta: { title: '我的课程', icon: 'List' },
      },
      {
        path: 'enroll',
        component: () => import('@/views/course-selection/enroll.vue'),
        name: 'courseEnroll',
        meta: { title: '选课', icon: 'DocumentAdd' },
      },
    ],
  },

  // —— 学生信息（学生） —— //
  {
    path: '/student',
    component: () => import('@/layout/index.vue'),
    name: 'studentInfo',
    meta: { title: '个人信息', icon: 'User', roles: ['student'] },
    children: [
      {
        path: 'profile',
        component: () => import('@/views/student/index.vue'),
        name: 'studentProfile',
        meta: { title: '个人资料', icon: 'UserFilled' },
      },
    ],
  },

  // —— 教师信息（教师） —— //
  {
    path: '/teacher',
    component: () => import('@/layout/index.vue'),
    name: 'teacherInfo',
    meta: { title: '个人信息', icon: 'User', roles: ['teacher'] },
    children: [
      {
        path: 'profile',
        component: () => import('@/views/teacher/index.vue'),
        name: 'teacherProfile',
        meta: { title: '个人资料', icon: 'UserFilled' },
      },
    ],
  },

  // —— 管理员后台（xueyuan管理员） —— //
  {
    path: '/admin',
    component: () => import('@/layout/index.vue'),
    name: 'Admin',
    meta: { title: '管理员后台', icon: 'School', roles: ['admin'] },
    children: [
      {
        path: 'report',
        component: () => import('@/views/admin/report-reservation.vue'),
        name: 'ReportReservation',
        meta: { title: '征订报告', icon: 'DocumentAdd' },
      },
      {
        path: 'inventory',
        component: () => import('@/views/admin/InventoryManagement.vue'),
        name: 'InventoryManagement',
        meta: { title: '库存管理', icon: 'Box' },
      },

      {
        path: 'colleges',
        component: () => import('@/views/admin/colleges.vue'),
        name: 'CollegesManagement',
        meta: { title: '学院管理', icon: 'OfficeBuilding' },
      },
    ],
  },

  {
    path: '/college-admin',
    component: () => import('@/layout/index.vue'),
    name: 'collegeAdmin',
    meta: { title: '学院管理员后台', icon: 'School', roles: ['collegeAdmin'] },
    children: [
      {
        path: 'majors',
        component: () => import('@/views/college-admin/majors.vue'),
        name: 'MajorsManagement',
        meta: { title: '专业管理', icon: 'School' },
      },
    ],
  },

  {
    path: '/admin-collegeAdmin',
    component: () => import('@/layout/index.vue'),
    name: 'AdminManagement',
    meta: { title: '学院管理员管理', icon: 'User', roles: ['admin'] },
    children: [
      {
        path: 'CollegeAdminAdd',
        component: () => import('@/views/college-admin/add.vue'),
        name: 'CollegeAdminAdd',
        meta: { title: '注册学院管理员', icon: 'DocumentAdd' },
      },
      {
        path: 'collegemanage',
        component: () => import('@/views/college-admin/management.vue'),
        name: 'CollegeManagement',
        meta: { title: '管理学院管理员', icon: 'Box' },
      },
    ],
  },
  // // —— 统计分析（管理员、教师） —— //
  // {
  //   path: '/statistics',
  //   component: () => import('@/layout/index.vue'),
  //   name: 'statistics',
  //   meta: { title: '统计分析', icon: 'Histogram', roles: ['admin', 'teacher'] },
  //   children: [
  //     {
  //       path: '',
  //       component: () => import('@/views/statistics/index.vue'),
  //       name: 'statisticsPage',
  //       meta: { title: '统计分析', icon: 'Histogram' },
  //     },
  //   ],
  // },

  // —— 错误页面 —— //
  { path: '/403', component: () => import('@/views/403/index.vue'), name: '403', meta: { hidden: true } },
  { path: '/404', component: () => import('@/views/404/index.vue'), name: '404', meta: { hidden: true } },
  { path: '/:pathMatch(.*)*', redirect: '/404', name: 'Any', meta: { hidden: true } },
]
