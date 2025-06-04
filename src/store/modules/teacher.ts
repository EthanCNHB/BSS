/**
 * @file 教师模块状态管理
 * @description 提供教师登录、登出、获取信息、基础/带教材课程列表、课程分配等功能的 Pinia Store
 * @author
 * @date 2025-05-23
 */

import type { TeacherInfo, Course } from '@/store/modules/type'
import { defineStore } from 'pinia'
import { ElMessage } from 'element-plus'
import { get, post, del, put, patch } from '@/utils/request'
import { GET_TOKEN, SET_TOKEN, REMOVE_TOKEN } from '@/utils/token'

export const useTeacherStore = defineStore('teacher', {
  state: () => ({
    /** JWT 登录凭证 */
    token: GET_TOKEN() as string,
    /** 当前登录教师信息 */
    teacherInfo: null as TeacherInfo | null,
  }),

  getters: {
    /** 判断是否已登录 */
    isLoggedIn: (state) => Boolean(state.token),
    /** 获取教师信息 */
    getTeacherInfo: (state) => state.teacherInfo,
  },

  actions: {
    /**
     * 直接设置教师信息
     */
    setTeacherInfo(info: TeacherInfo) {
      this.teacherInfo = info
    },

    /**
     * 教师登录并存储 JWT
     * @param username 教师用户名
     * @param password 教师密码
     */
    async login(username: string, password: string): Promise<void> {
      try {
        const token = await post<string>('/teacher/login', { username, password })
        this.token = token
        SET_TOKEN(token)
        ElMessage.success('登录成功')
      } catch {
        ElMessage.error('登录失败')
        throw new Error('Login failed')
      }
    },

    /** 教师登出，清除状态与本地存储 */
    logout(): void {
      this.token = ''
      this.teacherInfo = null
      REMOVE_TOKEN()
      ElMessage.success('退出登录成功')
    },

    /** 获取当前登录教师的详细信息 */
    async fetchTeacherInfo(): Promise<void> {
      try {
        const info = await get<TeacherInfo>('/teacher/me')
        this.teacherInfo = info
      } catch {
        ElMessage.error('获取教师信息失败')
      }
    },

    /**
     * 查询基础课程列表（不含教材信息）
     */
    async fetchBasicCourses(): Promise<void> {
      if (!this.token) {
        ElMessage.error('未登录')
        return
      }
      if (!this.teacherInfo) {
        ElMessage.error('教师信息加载失败')
        return
      }
      try {
        const list = await get<Course[]>(`/teacher/${this.teacherInfo.userId}/courses`)
        this.teacherInfo.courses = list
      } catch {
        ElMessage.error('获取基础课程列表失败')
      }
    },

    /**
     * 查询带教材信息的课程列表
     */
    async fetchCoursesWithTextbooks(): Promise<void> {
      if (!this.token) {
        ElMessage.error('未登录')
        return
      }
      if (!this.teacherInfo) {
        ElMessage.error('教师信息加载失败')
        return
      }
      try {
        const list = await get<Course[]>(`/teacher/${this.teacherInfo.userId}/courses-with-textbooks`)
        this.teacherInfo.courses = list
      } catch {
        ElMessage.error('获取课程及教材列表失败')
      }
    },

    /**
     * 批量分配课程，并刷新带教材列表
     * @param courseIds 课程 ID 列表
     */
    async assignCourses(courseIds: number[]): Promise<void> {
      if (!this.teacherInfo) {
        ElMessage.error('请先登录')
        return
      }
      // 先拉基础列表，避免重复分配
      await this.fetchBasicCourses()
      for (const id of courseIds) {
        if (this.teacherInfo.courses?.some((c) => c.courseId === id)) {
          ElMessage.warning(`课程 ${id} 已分配`)
          continue
        }
        try {
          await post<void>(`/teacher/${this.teacherInfo.userId}/courses`, [id])
          ElMessage.success(`课程 ${id} 分配成功`)
        } catch {
          ElMessage.error(`分配课程 ${id} 失败`)
        }
      }
      // 分配完成后刷新带教材信息的列表
      await this.fetchCoursesWithTextbooks()
    },

    /**
     * 撤销一门课程分配，并刷新带教材列表
     * @param courseId 课程 ID
     */
    async unassignCourse(courseId: number): Promise<void> {
      if (!this.teacherInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        await del<void>(`/textbook/${this.teacherInfo.userId}/courses/${courseId}`)
        ElMessage.success('撤销成功')
        // 撤销后刷新带教材信息的列表
        await this.fetchCoursesWithTextbooks()
      } catch {
        ElMessage.error('撤销失败')
      }
    },

    /**
     * 获取所有可选课程
     * @returns 课程列表
     */
    async getAllCourses(): Promise<Course[]> {
      try {
        return await get<Course[]>('/courses/list')
      } catch (err: any) {
        ElMessage.error('获取可选课程列表失败：' + err.message)
        throw err
      }
    },

    /**
     * 更新教师基本信息（teacherName、collegeId、majorId 等字段）
     * @param updatedInfo 待更新的 TeacherInfo 对象，必须包含 userId
     */
    async updateTeacherInfo(updatedInfo: TeacherInfo): Promise<void> {
      if (!this.teacherInfo) {
        ElMessage.error('教师信息加载失败')
        return
      }
      try {
        // PUT /teacher/update
        await put<void>('/teacher/update', updatedInfo)
        ElMessage.success('更新教师资料成功')
        // 更新本地缓存的 teacherInfo
        // 注意：如果后端返回的是更新后的完整对象，也可改为 this.teacherInfo = res
        this.teacherInfo = { ...this.teacherInfo, ...updatedInfo }
      } catch (err: any) {
        ElMessage.error('更新教师资料失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 修改教师密码
     * @param oldPwd 原密码
     * @param newPwd 新密码
     * @param rePwd  重复新密码
     */
    async updatePassword(oldPwd: string, newPwd: string, rePwd: string): Promise<void> {
      if (!this.teacherInfo) {
        ElMessage.error('教师信息加载失败')
        return
      }
      try {
        // PATCH /teacher/password
        await patch<void>('/teacher/password', { oldPwd, newPwd, rePwd })
        ElMessage.success('修改密码成功')
      } catch (err: any) {
        ElMessage.error('修改密码失败：' + (err.message || '未知错误'))
        throw err
      }
    },
  },
})
