// src/store/reservation.ts
import { defineStore } from 'pinia'
import { get, post, put, del } from '@/utils/request'
import { useUserStore } from './user'
import type { Textbook, TextbookReservation } from './type'
import { ElMessage } from 'element-plus'

/**
 * 征订记录 Store
 */
export const useReservationStore = defineStore('reservation', {
  state: () => ({
    /** 当前学生的所有预订记录 */
    reservations: [] as TextbookReservation[],
  }),

  getters: {
    /** 总记录数 */
    count: (state) => state.reservations.length,
  },

  actions: {
    /**
     * 拉取当前学生的所有征订记录
     */
    async fetchReservations(): Promise<void> {
      const userStore = useUserStore()
      try {
        const list = await get<TextbookReservation[]>('/reservation/list')
        this.reservations = list
      } catch (err: any) {
        ElMessage.error('获取征订记录失败：' + (err.message || '未知错误'))
      }
    },

    /**
     * 根据 ID 查询单条征订记录
     * @param id - 征订记录 ID
     * @returns 单条记录
     */
    async fetchReservationById(id: number): Promise<TextbookReservation> {
      try {
        const rec = await get<TextbookReservation>(`/reservation/${id}`)
        return rec
      } catch (err: any) {
        ElMessage.error('获取记录失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 新增一条教材征订
     * @param textbookId - 教材 ID
     * @param quantity   - 征订数量，默认 1
     */
    async addReservation(textbookId: number, quantity = 1): Promise<void> {
      try {
        // 先获取教材详情，补全名称和价格
        const tb = await get<Textbook>(`/textbook/${textbookId}`)
        const payload: Partial<TextbookReservation> = {
          textbookId: tb.textbookId,
          reservationQuantity: quantity,
          textbookName: tb.name,
          textbookPrice: tb.price,
        }
        await post<void>('/reservation/add', payload)
        ElMessage.success('添加预订成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('添加预订失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 更新一条征订记录
     * @param rec - 完整的征订对象（含 reservationId）
     */
    async updateReservation(rec: TextbookReservation): Promise<void> {
      try {
        await put<void>('/reservation/update', rec)
        ElMessage.success('更新预订成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('更新预订失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 删除一条征订记录
     * @param reservationId - 征订记录 ID
     */
    async deleteReservation(reservationId: number): Promise<void> {
      try {
        await del<void>(`/reservation/${reservationId}`)
        ElMessage.success('删除预订成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('删除预订失败：' + (err.message || '未知错误'))
        throw err
      }
    },
    /**
     * 拉取所有征订记录
     * GET /reservation/allList
     */
    async fetchAllRec(): Promise<void> {
      try {
        // 调用接口拿到数组
        const list = await get<TextbookReservation[]>('/reservation/allList')
        this.reservations = list
      } catch (err: any) {
        // 错误提示
        ElMessage.error('获取所有征订记录失败：' + (err.response?.data?.message || err.message || '未知错误'))
      }
    },
  },
})
