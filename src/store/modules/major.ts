import { defineStore } from 'pinia'
import { get, post, put, del } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { Major } from './type'

/**
 * 专业模块状态管理
 */
export const useMajorStore = defineStore('major', {
  state: () => ({
    /** 专业列表 */
    majors: [] as Major[],
  }),

  getters: {
    /** 专业总数 */
    count: (state) => state.majors.length,

    /** 按 majorId 索引的映射 */
    byId: (state) =>
      state.majors.reduce<Record<number, Major>>((map, m) => {
        map[m.majorId] = m
        return map
      }, {}),

    /**
     * 直接根据 majorId 拿到专业名称
     * 如果不存在则返回 `未知专业`
     */
    nameById: (state) => {
      return (id: number) => {
        const m = state.majors.find((x) => x.majorId === id)
        return m ? m.majorName : '未知专业'
      }
    },
  },

  actions: {
    /**
     * 拉取所有专业
     * GET /major/list
     */
    async fetchMajors(): Promise<void> {
      try {
        const list = await get<Major[]>('/major/list')
        this.majors = list
      } catch (err: any) {
        const errorMessage = err?.response?.data?.message || err?.message || '未知错误'
        ElMessage.error(`获取专业列表失败：${errorMessage}`)
      }
    },

    /**
     * 根据 collegeId 获取专业列表
     * GET /major/listByCollege/{collegeId}
     */
    async fetchMajorsByCollege(collegeId: number): Promise<void> {
      try {
        const list = await get<Major[]>(`/major/listByCollege/${collegeId}`)
        this.majors = list
      } catch (err: any) {
        const errorMessage = err?.response?.data?.message || err?.message || '未知错误'
        ElMessage.error(`获取学院专业列表失败：${errorMessage}`)
      }
    },

    /**
     * 添加新专业
     * POST /major/add
     */
    async addMajor(major: Major): Promise<void> {
      try {
        await post<void>('/major/add', major)
        ElMessage.success('新增专业成功')
        await this.fetchMajors()
      } catch (err: any) {
        const errorMessage = err?.response?.data?.message || err?.message || '新增失败'
        ElMessage.error(`新增专业失败：${errorMessage}`)
      }
    },

    /**
     * 更新专业
     * PUT /major/update
     */
    async updateMajor(major: Major): Promise<void> {
      try {
        await put<void>('/major/update', major)
        ElMessage.success('更新专业成功')
        await this.fetchMajors()
      } catch (err: any) {
        const errorMessage = err?.response?.data?.message || err?.message || '更新失败'
        ElMessage.error(`更新专业失败：${errorMessage}`)
      }
    },

    /**
     * 删除专业
     * DELETE /major/delete/{majorId}
     */
    async deleteMajor(majorId: number): Promise<void> {
      try {
        await del<void>(`/major/delete/${majorId}`)
        ElMessage.success('删除专业成功')
        await this.fetchMajors()
      } catch (err: any) {
        const errorMessage = err?.response?.data?.message || err?.message || '删除失败'
        ElMessage.error(`删除专业失败：${errorMessage}`)
      }
    },
  },
})
