// src/store/modules/textbookReservation.ts

import { defineStore } from 'pinia'
import { get, post, put, del } from '@/utils/request'
import type { Textbook, TextbookReservation } from './type'
import { ElMessage } from 'element-plus'

/**
 * 征订记录 Store
 */
export const useReservationStore = defineStore('reservation', {
  state: () => ({
    /** 当前学生或管理员视角下的所有征订记录 */
    reservations: [] as TextbookReservation[],
  }),

  getters: {
    /** 总记录数 */
    count: (state) => state.reservations.length,
  },

  actions: {
    /**
     * 拉取当前学生的所有征订记录
     * GET /reservation/list
     */
    async fetchReservations(): Promise<void> {
      try {
        const list = await get<TextbookReservation[]>('/reservation/list')
        this.reservations = list
      } catch (err: any) {
        ElMessage.error('获取征订记录失败：' + (err.response?.data?.message || err.message || '未知错误'))
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
        ElMessage.error('获取记录失败：' + (err.response?.data?.message || err.message || '未知错误'))
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
        if (quantity <= 0) {
          ElMessage.error('征订数量必须大于 0')
          return
        }
        // 先获取教材详情，仅用于展示或确认
        const tb = await get<Textbook>(`/textbook/${textbookId}`)

        // 只需 textbookId 和 reservationQuantity
        const payload: Partial<TextbookReservation> = {
          textbookId: tb.textbookId,
          reservationQuantity: quantity,
        }

        await post<void>('/reservation/add', payload)
        ElMessage.success('添加预订成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('添加预订失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 修改一条征订记录的数量
     * @param reservationId       - 征订记录 ID
     * @param newQuantity         - 新的征订数量（必须 > 0）
     */
    async updateQuantity(reservationId: number, newQuantity: number): Promise<void> {
      try {
        if (newQuantity <= 0) {
          ElMessage.error('征订数量必须大于 0')
          return
        }
        const payload = {
          reservationId,
          reservationQuantity: newQuantity,
        }
        await put<void>('/reservation/updateQuantity', payload)
        ElMessage.success('更新数量成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('更新数量失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 更新一条征订记录（可扩展）
     * @param rec - 包含 reservationId 与待更新字段的对象
     */
    async updateReservation(rec: Partial<TextbookReservation> & { reservationId: number }): Promise<void> {
      try {
        if (rec.reservationQuantity != null && rec.reservationQuantity <= 0) {
          ElMessage.error('征订数量必须大于 0')
          return
        }
        await put<void>('/reservation/update', rec)
        ElMessage.success('更新预订成功')
        await this.fetchReservations()
      } catch (err: any) {
        ElMessage.error('更新预订失败：' + (err.response?.data?.message || err.message || '未知错误'))
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
        ElMessage.error('删除预订失败：' + (err.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 拉取所有征订记录（管理员视角，需要包含“学生姓名、学院、专业”等字段”）
     * GET /reservation/allList
     */
    async fetchAllRec(): Promise<void> {
      try {
        const list = await get<TextbookReservation[]>('/reservation/allList')
        this.reservations = list
      } catch (err: any) {
        ElMessage.error('获取所有征订记录失败：' + (err.response?.data?.message || err.message || '未知错误'))
      }
    },
  },
})
