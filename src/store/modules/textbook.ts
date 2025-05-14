import { defineStore } from 'pinia'
import axios from 'axios'
import useStudentStore from '@/store/modules/student' // 引入用户store
import { Textbook } from './type'

export const useTextbookStore = defineStore('textbook', {
  state: () => ({
    textbooks: [] as Array<any>,
    currentTextbook: null as any,
  }),

  actions: {
    // 请求前添加 Authorization Header
    async fetchTextbooks() {
      try {
        const userStore = useStudentStore()
        const token = userStore.token // 获取 token

        // 添加 Authorization 头部
        const response = await axios.get('http://localhost:8080/textbook/list', {
          headers: {
            Authorization: `Bearer ${token}`, // 在请求头中携带 token
          },
        })
        this.textbooks = response.data.data
      } catch (error) {
        console.error('获取教材列表失败', error)
      }
    },

    // 获取单个教材
    async fetchTextbookById(textbookId: number) {
      try {
        const userStore = useStudentStore()
        const token = userStore.token // 获取 token

        const response = await axios.get(`http://localhost:8080/textbook/${textbookId}`, {
          headers: {
            Authorization: `Bearer ${token}`, // 在请求头中携带 token
          },
        })
        this.currentTextbook = response.data.data
      } catch (error) {
        console.error('获取教材失败', error)
      }
    },

    async addTextbook(newTextbook: Textbook) {
      try {
        const userStore = useStudentStore()
        const token = userStore.token // 获取 token
        const response = await axios.post(`http://localhost:8080/textbook`, newTextbook, {
          headers: {
            Authorization: `Bearer ${token}`, // 在请求头中携带 token
          },
        })
        console.log(Headers)
        this.textbooks.push(response.data.data)
      } catch (error) {
        console.error('', error)
        throw error
      }
    },
    // 删除教材
    async deleteTextbook(textbookId: number) {
      try {
        const userStore = useStudentStore()
        const token = userStore.token // 获取 token

        await axios.delete(`http://localhost:8080/textbook/${textbookId}`, {
          headers: {
            Authorization: `Bearer ${token}`, // 在请求头中携带 token
          },
        })
        this.fetchTextbooks() // 更新教材列表
      } catch (error) {
        console.error('删除教材失败', error)
      }
    },
  },
})
