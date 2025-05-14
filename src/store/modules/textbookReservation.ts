import { defineStore } from 'pinia'
import axios from 'axios'
import { useUserStore } from './user'

export const useReservationStore = defineStore('reservation', {
  state: () => ({
    reservations: [] as any[], // 存储用户的征订记录
  }),

  actions: {
    // 获取当前用户的所有征订记录
    async fetchReservations() {
      try {
        const userStore = useUserStore()
        const token = userStore.token // 通过 .value 访问 token

        const res = await axios.get('http://localhost:8080/reservation/list', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        this.reservations = [res.data.data] // 后端返回的是单条记录对象，封装为数组
      } catch (err) {
        console.error('获取征订记录失败:', err)
      }
    },

    // 根据 ID 查询单个征订记录
    async fetchReservationById(id: number) {
      try {
        const userStore = useUserStore()
        const token = userStore.token // 通过 .value 访问 token

        const res = await axios.get(`http://localhost:8080/reservation/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        return res.data.data
      } catch (err) {
        console.error('获取单个征订失败:', err)
        throw err
      }
    },

    // 添加新的征订记录
    async addReservation(textbookId: number) {
      try {
        const userStore = useUserStore()
        const token = userStore.token // 通过 .value 访问 token

        await axios.post(
          'http://localhost:8080/reservation/add',
          { textbookId },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        )
        await this.fetchReservations() // 添加后重新加载
      } catch (err) {
        console.error('添加征订失败:', err)
        throw err
      }
    },

    // 删除征订记录
    async deleteReservation(reservationId: number) {
      try {
        const userStore = useUserStore()
        const token = userStore.token // 通过 .value 访问 token

        await axios.delete(`http://localhost:8080/reservation/${reservationId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        await this.fetchReservations()
      } catch (err) {
        console.error('删除征订失败:', err)
        throw err
      }
    },

    // 更新征订记录
    async updateReservation(tbr: any) {
      try {
        const userStore = useUserStore()
        const token = userStore.token // 通过 .value 访问 token

        await axios.put('http://localhost:8080/reservation/update', tbr, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        await this.fetchReservations()
      } catch (err) {
        console.error('更新征订失败:', err)
        throw err
      }
    },
  },
})
