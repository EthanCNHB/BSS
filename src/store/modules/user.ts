import { College } from './type'
// src/store/user.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { get, post } from '@/utils/request'
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from '@/utils/token'
import { SET_ROLE, GET_ROLE, REMOVE_ROLE } from '@/utils/role'
import { ElMessage } from 'element-plus'
import { constantRoute } from '@/router/routes'
import type { LoginResult, StudentInfo, TeacherInfo, Admin, CollegeAdmin } from './type'
import { getTime } from '@/utils/time'

// 导入角色仓库
import { useTeacherStore } from './teacher'
import { useAdminStore } from './admin'
import { useStudentStore } from './student'
import { useCollegeAdminStore } from './collegeAdmin'
import { useCollegeStore } from './college'

/** 按角色过滤可访问路由 */
const filterRoutesByRole = (role: string) => constantRoute.filter((route) => !route.meta?.roles || route.meta.roles.includes(role))

export const useUserStore = defineStore('user', () => {
  // —— state —— //
  const token = ref<string | null>(GET_TOKEN())
  const role = ref<string>(GET_ROLE() || '')
  const username = ref<string>(localStorage.getItem('USERNAME') || '')
  const avatar = ref<string>('')
  const menuRoutes = ref<any[]>([])

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const greeting = computed(() => `${getTime()}好，${username.value}`)

  // —— 内部方法 —— //

  // 登录成功后设置 token/role/username，并更新菜单路由
  function applyLogin(newToken: string, newRole: string, name: string) {
    token.value = newToken
    role.value = newRole
    username.value = name

    SET_TOKEN(newToken)
    SET_ROLE(newRole)
    localStorage.setItem('USERNAME', name)

    menuRoutes.value = filterRoutesByRole(newRole) // 根据角色过滤可访问的路由
  }

  /** 获取用户详细信息 */
  async function fetchProfile(): Promise<void> {
    // 获取对应角色的 store
    const teacherStore = useTeacherStore()
    const adminStore = useAdminStore()
    const studentStore = useStudentStore()
    const collegeAdminStore = useCollegeAdminStore()
    const collegeStore = useCollegeStore()

    const roleApiMap: Record<string, () => Promise<void>> = {
      admin: async () => {
        const info = await get<Admin>('/admin/info')
        avatar.value = '@/assets/images/manager.jpg' // 管理员头像
        username.value = info.username
        adminStore.setAdminInfo(info) // 将管理员信息存储到 adminStore
      },
      teacher: async () => {
        const info = await get<TeacherInfo>('/teacher/me')
        avatar.value = info.teacherPic || ''
        username.value = info.username
        teacherStore.setTeacherInfo(info) // 将教师信息存储到 teacherStore
      },
      student: async () => {
        const info = await get<StudentInfo>('/student/studentInfo')
        avatar.value = info.avatar || ''
        username.value = info.username
        studentStore.setStudentInfo(info) // 将学生信息存储到 studentStore
      },
      collegeAdmin: async () => {
        const info = await get<CollegeAdmin>('/collegeAdmin/me')
        const collegeInfo = await get<College>(`/college/${info.collegeId}`)
        avatar.value = '@/assets/images/manager.jpg'
        username.value = info.username
        collegeAdminStore.setCollegeAdminInfo(info) // 将学院管理员信息存储到 collegeAdminStore
        collegeStore.setCollegeInfo(collegeInfo)
      },
    }

    try {
      const fetchInfo = roleApiMap[role.value]
      if (fetchInfo) {
        await fetchInfo() // 根据角色调用相应的接口
      } else {
        throw new Error('Invalid role') // 如果角色无效，抛出错误
      }
    } catch (err) {
      console.error('获取用户信息失败', err)
      ElMessage.error('获取用户信息失败')
    }
  }

  // —— actions —— //

  // 登录方法，成功后获取用户信息
  async function doLogin(usernameInput: string, password: string): Promise<boolean> {
    try {
      const res = await post<LoginResult>('/user/login', {
        username: usernameInput,
        password,
      })
      applyLogin(res.token, res.role, usernameInput)
      await fetchProfile()
      return true
    } catch (err) {
      console.error('登录失败', err)
      return false
    }
  }

  // 登出方法，清空状态并清理本地存储
  function logout(): void {
    token.value = null
    role.value = ''
    username.value = ''
    avatar.value = ''
    menuRoutes.value = []
    REMOVE_TOKEN()
    REMOVE_ROLE()
    localStorage.removeItem('USERNAME')
  }

  // 页面刷新后恢复登录状态并拉取个人信息
  async function restoreSession(): Promise<void> {
    const t = GET_TOKEN()
    const r = GET_ROLE()
    const name = localStorage.getItem('USERNAME') || ''
    if (t && r) {
      applyLogin(t, r, name)
      await fetchProfile()
    }
  }

  // —— 初始化 —— //
  // 页面加载时尝试恢复登录状态
  if (token.value && role.value) {
    restoreSession()
  }

  return {
    token,
    role,
    username,
    avatar,
    menuRoutes,
    greeting,
    isLoggedIn,
    doLogin,
    logout,
    restoreSession,
    fetchProfile,
  }
})
