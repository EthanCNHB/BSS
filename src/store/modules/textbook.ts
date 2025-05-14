// store/modules/textbook.ts
import { defineStore } from 'pinia'
import axios from 'axios'

export const useTextbookStore = defineStore('textbook', {
  state: () => ({
    textbooks: [] as Array<any>, // 存储教材列表
    currentTextbook: null as any, // 存储当前选中的教材
  }),

  actions: {
    // 获取所有教材
    async fetchTextbooks() {
      try {
        const response = await axios.get('http://localhost:8080/textbook/list')
        this.textbooks = response.data.data
      } catch (error) {
        console.error('获取教材列表失败', error)
      }
    },

    // 获取单个教材
    async fetchTextbookById(textbookId: number) {
      try {
        const response = await axios.get(`http://localhost:8080/textbook/${textbookId}`)
        this.currentTextbook = response.data.data
      } catch (error) {
        console.error('获取教材失败', error)
      }
    },

    // 添加教材
    async addTextbook(textbook: any) {
      try {
        await axios.post('http://localhost:8080/textbook', textbook)
        this.fetchTextbooks() // 更新教材列表
      } catch (error) {
        console.error('添加教材失败', error)
      }
    },

    // 编辑教材
    async updateTextbook(textbook: any) {
      try {
        await axios.put('http://localhost:8080/textbook', textbook)
        this.fetchTextbooks() // 更新教材列表
      } catch (error) {
        console.error('更新教材失败', error)
      }
    },

    // 删除教材
    async deleteTextbook(textbookId: number) {
      try {
        await axios.delete(`http://localhost:8080/textbook/${textbookId}`)
        this.fetchTextbooks() // 更新教材列表
      } catch (error) {
        console.error('删除教材失败', error)
      }
    },
  },
})
