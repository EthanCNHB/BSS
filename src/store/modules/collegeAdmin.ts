// src/store/modules/collegeAdmin.ts
import { defineStore } from 'pinia'
import { get, post, put, del, patch } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { CollegeAdmin } from './type'

/**
 * 学院管理员模块状态管理
 */
export const useCollegeAdminStore = defineStore('collegeAdmin', {
  state: () => ({
    /** 当前登录的学院管理员信息 */
    collegeAdminInfo: null as CollegeAdmin | null,

    /** 管理员列表 */
    collegeAdminList: [] as CollegeAdmin[],
  }),

  getters: {
    /** 是否已加载管理员信息 */
    hasInfo: (state) => state.collegeAdminInfo !== null,

    /** 列表总数 */
    count: (state) => state.collegeAdminList.length,

    /** 按 userId 索引的映射 */
    byId: (state) =>
      state.collegeAdminList.reduce<Record<number, CollegeAdmin>>((map, a) => {
        map[a.userId] = a
        return map
      }, {}),
  },

  actions: {
    /**
     * 注册新管理员
     * POST /collegeAdmin/register
     */
    async register(payload: { collegeId: number; username: string; password: string }): Promise<void> {
      try {
        await post<void>('/collegeAdmin/register', payload)
        ElMessage.success('添加成功')
      } catch (err: any) {
        ElMessage.error('添加失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 设置管理员信息（内部使用）
     */
    setCollegeAdminInfo(info: CollegeAdmin) {
      this.collegeAdminInfo = info
    },

    /**
     * 拉取当前登录管理员详情
     * GET /collegeAdmin/me
     */
    async fetchProfile(): Promise<void> {
      try {
        const info = await get<CollegeAdmin>('/collegeAdmin/me')
        this.setCollegeAdminInfo(info)
      } catch (err: any) {
        ElMessage.error('获取管理员信息失败：' + (err.response?.data?.message || err.message || '未知错误'))
      }
    },

    /**
     * 拉取所有学院管理员列表
     * GET /collegeAdmin/list
     */
    async fetchAllAdmins(): Promise<void> {
      try {
        const list = await get<CollegeAdmin[]>('/collegeAdmin/list')
        this.collegeAdminList = list
      } catch (err: any) {
        ElMessage.error('获取管理员列表失败：' + (err.response?.data?.message || err.message || '未知错误'))
      }
    },

    /**
     * 更新指定管理员（包括密码）
     * PUT /collegeAdmin/update
     * @param admin - 完整的管理员对象，userId必填
     */
    async updateAdmin(admin: CollegeAdmin): Promise<void> {
      try {
        await put<void>('/collegeAdmin/update', admin)
        ElMessage.success('更新成功')
      } catch (err: any) {
        ElMessage.error('更新失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 删除指定管理员
     * DELETE /collegeAdmin/{userId}
     * @param userId - 要删除的管理员ID
     */
    async deleteAdmin(userId: number): Promise<void> {
      try {
        await del<void>(`/collegeAdmin/${userId}`)
        ElMessage.success('删除成功')
      } catch (err: any) {
        ElMessage.error('删除失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 更新登录密码
     * PATCH /collegeAdmin/password
     */
    async changePassword(oldPwd: string, newPwd: string, rePwd: string): Promise<void> {
      try {
        await patch<void>('/collegeAdmin/password', { oldPwd, newPwd, rePwd })
        ElMessage.success('密码修改成功')
      } catch (err: any) {
        ElMessage.error('密码修改失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },
  },
})
