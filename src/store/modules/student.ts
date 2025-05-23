// src/store/student.ts
import type { StudentInfo, Course, ApiResponse } from './type'
import { defineStore } from 'pinia'
import { ElMessage } from 'element-plus'
import { get, post, put, patch } from '@/utils/request'
import { GET_TOKEN, REMOVE_TOKEN, SET_TOKEN } from '@/utils/token'

/**
 * 学生模块状态管理
 */
export const useStudentStore = defineStore('student', {
  state: () => ({
    /** JWT 登录凭证 */
    token: GET_TOKEN() as string,
    /** 当前登录学生信息 */
    studentInfo: null as StudentInfo | null,
  }),

  getters: {
    /** 是否已登录 */
    isLoggedIn: (state): boolean => !!state.token,
    /** 当前学生信息 */
    getStudentInfo: (state): StudentInfo | null => state.studentInfo,
  },

  actions: {
    // 更新学生信息
    setStudentInfo(info: StudentInfo) {
      this.studentInfo = info
    },

    /** 获取当前学生的详细信息 */
    async fetchStudentInfo(): Promise<void> {
      try {
        const info = await get<StudentInfo>('/student/studentInfo')
        this.studentInfo = info
      } catch {
        ElMessage.error('获取个人信息失败')
      }
    },

    /**
     * 更新学生基本信息
     * @param updated - 要更新的字段集合
     */
    async updateStudentInfo(updated: Partial<StudentInfo>): Promise<void> {
      try {
        await put<void>('/student/update', updated)
        ElMessage.success('信息更新成功')
        await this.fetchStudentInfo()
      } catch (err: any) {
        ElMessage.error(err.message || '更新失败')
      }
    },

    /**
     * 修改登录密码
     * @param oldPwd - 旧密码
     * @param newPwd - 新密码
     * @param rePwd  - 重复新密码
     */
    async updatePassword(oldPwd: string, newPwd: string, rePwd: string): Promise<void> {
      try {
        await patch<void>('/student/updatePwd', {
          old_pwd: oldPwd,
          new_pwd: newPwd,
          re_pwd: rePwd,
        })
        ElMessage.success('密码修改成功')
      } catch (err: any) {
        ElMessage.error(err.message || '密码修改失败')
      }
    },

    /**
     * 学生批量选课
     * @param courseIds - 课程编号数组
     */
    async selectCourses(courseIds: number[]): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        await post<void>(`/students/${this.studentInfo.userId}/courses`, { courseIds })
        ElMessage.success('选课成功')
      } catch (err: any) {
        ElMessage.error(err.message || '选课失败')
      }
    },
  },
})
