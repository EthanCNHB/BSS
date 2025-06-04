// src/types/index.ts

/** 通用接口返回结构 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/** 登录返回体 */
export interface LoginResult {
  token: string
  role: 'admin' | 'collegeAdmin' | 'teacher' | 'student'
}

/** 教材信息 */
export interface Textbook {
  textbookId: number
  name: string
  code: string
  publisher?: string
  author?: string
  price: number
  status?: string
  stockQuantity: number
}

/** 课程类型，新增 textbooks 字段 */
export interface Course {
  courseId: number
  courseCode: string
  courseName: string
  credit?: number
  courseType?: string
  description?: string
  majorId: number

  /** 新增：关联的教材列表 */
  textbooks?: Textbook[]
}

/** 学生个人信息 */
export interface StudentInfo {
  userId: number
  username: string
  studentName: string
  collegeId: number
  majorId: number
  avatar?: string
  class?: string
  courses?: Course[]
}

/** 教师个人信息 */
export interface TeacherInfo {
  userId: number
  username: string
  teacherName: string
  collegeId: number
  majorId: number
  teacherPic?: string

  /** 教师所授课程，也会带上 textbooks 字段 */
  courses?: Course[]
}

/** 管理员信息 */
export interface Admin {
  userId: number
  username: string
}

export interface TextbookReservation {
  reservationId: number
  textbookId: number
  userId: number
  reservationQuantity: number
  orderDate: string // 或者 Date
  textbookName: string // 新增：教材名称
  textbookPrice: number // 新增：教材单价
  studentName: string // 新增：学生姓名（仅管理员视角）
  collegeName: string // 新增：学院名称
  majorName: string // 新增：专业名称
}

/** 专业（Major）类型 */
export interface Major {
  majorId: number
  majorName: string
  majorDescription?: string
}

/** 学院（College）类型 */
export interface College {
  collegeId: number
  collegeName: string
  creationTime: string
  collegeDescription?: string
}

/** 学院管理员信息 */
export interface CollegeAdmin {
  userId: number
  username: string
  collegeId: number
}
