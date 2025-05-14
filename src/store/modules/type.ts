import type { RouteRecordRaw } from 'vue-router'

// 用户状态接口
export interface UserState {
  token: string | null // 用户token
  menuRoutes: RouteRecordRaw[] // 用户可访问的路由
  username: string // 用户名
  avatar: string // 用户头像
}

// 教材接口
export interface Textbook {
  name: string // 教材名称
  code: string // 教材编码
  publisher: string // 出版社
  author: string // 作者
  price: number // 价格
  status: string | number // 状态（如在售、下架等）
}

// 征订记录接口
export interface TextbookReservation {
  reservationId: number // 征订记录ID
  userId: number // 用户ID
  textbookId: number // 教材ID
  textbookName: string // 教材名称
  quantity: number // 预定数量
  reservationDate: string // 征订日期
  status: 'Pending' | 'Completed' | 'Cancelled' // 订单状态
}

// 角色接口
export type UserRole = 'student' | 'teacher' | 'admin' | 'college-admin'

// 权限接口
export interface Permission {
  role: UserRole // 角色类型
  permissions: string[] // 权限列表（例如，'view-textbook', 'edit-textbook'）
}

// 统计分析接口
export interface Statistics {
  totalTextbooks: number // 总教材数量
  totalReservations: number // 总征订数量
  completedReservations: number // 已完成征订数量
  pendingReservations: number // 待处理征订数量
}
