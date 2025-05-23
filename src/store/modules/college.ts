import { defineStore } from 'pinia'
import { get, post, put, del } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { College } from './type'

export const useCollegeStore = defineStore('college', {
  state: () => ({
    /** 所有学院列表 */
    colleges: [] as College[],

    collegeInfo: null as College | null,
  }),

  getters: {
    /** 学院总数 */
    count: (state) => state.colleges.length,

    /** 按 collegeId 索引的映射 */
    byId: (state) =>
      state.colleges.reduce<Record<number, College>>((map, c) => {
        map[c.collegeId] = c
        return map
      }, {}),
  },

  actions: {
    /**
     * 设置学院信息
     */
    setCollegeInfo(info: College) {
      this.collegeInfo = info
    },

    /**
     * 拉取所有学院
     * GET /college/list
     */
    async fetchColleges(): Promise<void> {
      try {
        const list = await get<College[]>('/college/list')
        console.log(list)
        this.colleges = list
      } catch (err: any) {
        const msg = err?.response?.data?.message || err.message || '未知错误'
        ElMessage.error(`获取学院列表失败：${msg}`)
      }
    },

    /**
     * 获取所有可选学院（与 fetchColleges 区分，返回数据而不写入 state）
     * GET /college/list
     */
    async getAllColleges(): Promise<College[]> {
      try {
        return await get<College[]>('/college/list')
      } catch (err: any) {
        ElMessage.error('获取可选学院列表失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 获取单个学院详情
     * GET /college/{id}
     */
    async fetchCollegeById(id: number): Promise<College> {
      try {
        return await get<College>(`/college/${id}`)
      } catch (err: any) {
        const msg = err?.response?.data?.message || err.message || '未知错误'
        ElMessage.error(`获取学院详情失败：${msg}`)
        throw err
      }
    },

    /**
     * 添加新学院
     * POST /college/add
     */
    async addCollege(newCollege: Omit<College, 'collegeId'>): Promise<void> {
      try {
        await post('/college/add', newCollege)
        ElMessage.success('新增学院成功')
        await this.fetchColleges()
      } catch (err: any) {
        const msg = err?.response?.data?.message || err.message || '未知错误'
        ElMessage.error(`新增学院失败：${msg}`)
        throw err
      }
    },

    /**
     * 更新学院信息
     * PUT /college/update
     */
    async updateCollege(college: College): Promise<void> {
      try {
        await put('/college/update', college)
        ElMessage.success('更新学院成功')
        await this.fetchColleges()
      } catch (err: any) {
        const msg = err?.response?.data?.message || err.message || '未知错误'
        ElMessage.error(`更新学院失败：${msg}`)
        throw err
      }
    },

    /**
     * 删除学院
     * DELETE /college/delete/{id}
     */
    async deleteCollege(id: number): Promise<void> {
      try {
        await del(`/college/delete/${id}`)
        ElMessage.success('删除学院成功')
        await this.fetchColleges()
      } catch (err: any) {
        const msg = err?.response?.data?.message || err.message || '未知错误'
        ElMessage.error(`删除学院失败：${msg}`)
        throw err
      }
    },
  },
})
