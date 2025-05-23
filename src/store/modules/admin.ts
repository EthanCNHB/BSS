// src/store/admin.ts
import { defineStore } from 'pinia'
import { get } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { Admin } from './type'

/**
 * 管理员模块状态管理
 */
export const useAdminStore = defineStore('admin', {
  state: () => ({
    /** 管理员个人信息 */
    adminInfo: null as Admin | null,
  }),

  getters: {
    /** 是否已加载管理员信息 */
    hasInfo: (state) => state.adminInfo !== null,
  },

  actions: {
    // 更新管理员信息
    setAdminInfo(info: Admin) {
      this.adminInfo = info
    },

    /**
     * 拉取当前管理员信息
     * GET /admin/info
     */
    async fetchAdminInfo(): Promise<void> {
      try {
        const info = await get<Admin>('/admin/info')
        this.adminInfo = info
      } catch (err: any) {
        ElMessage.error('获取管理员信息失败: ' + (err.message || '未知错误'))
      }
    },
  },
})
