import type { RouteRecordRaw } from 'vue-router'
export interface UserState {
  token: string | null
  menuRoutes: RouteRecordRaw[]
  username: string
  avatar: string
}

// 教材接口
export interface Textbook {
  textbookId: number // 教材ID
  name: string // 教材名称
  code: string // 教材编号
  publisher: string // 出版社
  author: string // 作者
  price: number // 价格
  status: string | number // 状态（如在售、下架等）
}
