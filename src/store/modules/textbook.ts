import { defineStore } from 'pinia'
import { get, post, put, del } from '@/utils/request'
import { useUserStore } from './user'
import type { Textbook } from './type'
import { ElMessage } from 'element-plus'

/**
 * 教材模块状态管理
 */
export const useTextbookStore = defineStore('textbook', {
  state: () => ({
    textbooks: [] as Textbook[],
    currentTextbook: null as Textbook | null,
    searchKeyword: '',
  }),

  getters: {
    /** 教材总数 */
    count: (state) => state.textbooks.length,

    /**
     * 根据关键字从本地列表里过滤教材
     * 后端搜索走的是 searchTextbooks，如果要用本地过滤就用这个
     */
    filteredTextbooks: (state) => {
      const kw = state.searchKeyword.trim().toLowerCase()
      if (!kw) return state.textbooks
      return state.textbooks.filter(
        (tb) => tb.name.toLowerCase().includes(kw) || tb.code.toLowerCase().includes(kw) || tb.author?.toLowerCase().includes(kw),
      )
    },
  },

  actions: {
    /**
     * 拉取所有教材
     */
    async fetchTextbooks(): Promise<void> {
      try {
        await useUserStore().restoreSession() // 确保 token 可用
        const list = await get<Textbook[]>('/textbook/list')
        this.textbooks = list
      } catch (err: any) {
        ElMessage.error('获取教材列表失败: ' + (err.message || '未知错误'))
      }
    },

    /**
     * 根据 ID 获取单个教材
     * @param id - 教材 ID
     */
    async fetchTextbookById(id: number): Promise<void> {
      try {
        const tb = await get<Textbook>(`/textbook/${id}`)
        this.currentTextbook = tb
      } catch (err: any) {
        ElMessage.error('获取教材失败: ' + (err.message || '未知错误'))
      }
    },

    /**
     * 添加新教材
     * @param newTb - 教材信息
     */
    async addTextbook(newTb: Textbook): Promise<void> {
      try {
        await post<void>('/textbook', newTb)
        ElMessage.success('添加教材成功')
        await this.fetchTextbooks()
      } catch (err: any) {
        ElMessage.error('添加教材失败: ' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 更新教材
     * @param tb - 包含 textbookId 的完整教材对象
     */
    async updateTextbook(tb: Textbook): Promise<void> {
      try {
        await put<void>('/textbook', tb)
        ElMessage.success('更新教材成功')
        await this.fetchTextbooks()
      } catch (err: any) {
        ElMessage.error('更新教材失败: ' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 删除教材
     * @param id - 教材 ID
     */
    async deleteTextbook(id: number): Promise<void> {
      try {
        await del<void>(`/textbook/${id}`)
        ElMessage.success('删除教材成功')
        await this.fetchTextbooks()
      } catch (err: any) {
        ElMessage.error('删除教材失败: ' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 给课程分配教材
     * @param textbookId - 教材ID
     * @param courseIds  - 课程ID列表
     */
    async assignTextbookToCourse(textbookId: number, courseIds: number[]): Promise<void> {
      if (!textbookId || courseIds.length === 0) {
        ElMessage.error('请选择教材和课程')
        return
      }

      try {
        await post(`/textbook/${textbookId}/courses`, courseIds)
        ElMessage.success('教材分配成功')
      } catch {
        ElMessage.error('教材分配失败')
      }
    },

    /**
     * 解绑教材与课程
     * @param textbookId - 教材ID
     * @param courseId   - 课程ID
     */
    async unassignTextbookFromCourse(textbookId: number, courseId: number): Promise<void> {
      try {
        await del<void>(`/textbook/${textbookId}/courses/${courseId}`)
        ElMessage.success('教材解绑成功')
        // 操作完成后刷新列表展示最新状态
        await this.fetchTextbooks()
      } catch (err: any) {
        ElMessage.error('教材解绑失败: ' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 根据条件从后端查询教材列表
     * GET /textbook/search?name=&publisher=&minPrice=&maxPrice=
     */
    async searchTextbooks(conditions: Record<string, any>): Promise<void> {
      try {
        await useUserStore().restoreSession()
        const list = await get<Textbook[]>('/textbook/search', { params: conditions })
        this.textbooks = list
      } catch (err: any) {
        ElMessage.error('查询教材失败: ' + (err.response?.data?.message || err.message || '未知错误'))
      }
    },

    /** 设置关键字并触发后端查询 */
    async doSearchByKeyword(kw: string) {
      this.searchKeyword = kw.trim()
      await this.searchTextbooks({ name: this.searchKeyword })
    },
  },
})
