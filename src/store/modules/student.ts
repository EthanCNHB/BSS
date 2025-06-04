// src/store/modules/student.ts
import type { StudentInfo, ApiResponse, Course } from './type'
import { defineStore } from 'pinia'
import { ElMessage } from 'element-plus'
import { get, post, put, patch, del } from '@/utils/request'
import { GET_TOKEN, REMOVE_TOKEN, SET_TOKEN } from '@/utils/token'

/**
 * 学生模块状态管理
 */
export const useStudentStore = defineStore('student', {
  state: () => ({
    /** JWT 登录凭证 */
    token: GET_TOKEN() as string,
    /** 当前登录学生信息 */
    studentInfo: null as StudentInfo | null,
  }),

  getters: {
    /** 是否已登录 */
    isLoggedIn: (state): boolean => !!state.token,
    /** 当前学生信息 */
    getStudentInfo: (state): StudentInfo | null => state.studentInfo,
    /** 已选课程列表 */
    selectedCourses: (state): Course[] => state.studentInfo?.courses || [],
    /** 判断是否已选择指定课程 */
    isCourseSelected:
      (state) =>
      (id: number): boolean =>
        state.studentInfo?.courses?.some((c) => c.courseId === id) ?? false,
  },

  actions: {
    /**
     * 设置学生信息到 state 中
     * @param info - 从后端获取到的 StudentInfo 对象
     */
    setStudentInfo(info: StudentInfo) {
      this.studentInfo = info
    },

    /**
     * ============================
     * 一、注册新学生
     * ============================
     * - 对应后端：POST /student/register
     * - 参数格式：{ username, password }
     */
    async register(username: string, password: string): Promise<void> {
      try {
        await post<void>('/student/register', {
          username,
          password,
        })
        ElMessage.success('注册成功，请使用新账号登录')
      } catch (err: any) {
        ElMessage.error(err.message || '注册失败')
      }
    },

    /**
     * ============================
     * 二、获取当前学生详细信息
     * ============================
     * - 对应后端：GET /student/studentInfo
     * - 无需传参，后端通过 Token 中的用户名来查询
     * - 成功后把完整 StudentInfo（包含 userId）存入 state.studentInfo
     */
    async fetchStudentInfo(): Promise<void> {
      try {
        const info = await get<StudentInfo>('/student/studentInfo')
        this.studentInfo = info
      } catch (err: any) {
        ElMessage.error(err.message || '获取个人信息失败')
      }
    },

    /**
     * ============================
     * 三、更新学生基本信息
     * ============================
     * - 对应后端：PUT /student/update
     * - 后端接口签名：public Result<Void> update(@Valid @RequestBody Student student)
     *   需要传递完整 Student 对象，其中 userId 必须存在
     * - 我们从 this.studentInfo 中拿到 userId，然后与 updated 字段合并
     */
    async updateStudentInfo(updated: Partial<StudentInfo>): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        const payload = {
          userId: this.studentInfo.userId,
          studentName: updated.studentName,
          collegeId: updated.collegeId,
          majorId: updated.majorId,
          avatar: updated.avatar,
          // 如果 Student 实体里有更多字段，可继续补充
        }
        await put<void>('/student/update', payload)
        ElMessage.success('信息更新成功')
        await this.fetchStudentInfo()
      } catch (err: any) {
        ElMessage.error(err.message || '更新失败')
      }
    },

    /**
     * ============================
     * 四、修改登录密码
     * ============================
     * - 对应后端：PATCH /student/updatePwd
     * - 参数格式：{ old_pwd, new_pwd, re_pwd }
     */
    async updatePassword(oldPwd: string, newPwd: string, rePwd: string): Promise<void> {
      try {
        await patch<void>('/student/updatePwd', {
          old_pwd: oldPwd,
          new_pwd: newPwd,
          re_pwd: rePwd,
        })
        ElMessage.success('密码修改成功')
      } catch (err: any) {
        ElMessage.error(err.message || '密码修改失败')
      }
    },

    /**
     * ============================
     * 五、退出登录
     * ============================
     */
    logout(): void {
      REMOVE_TOKEN()
      this.token = ''
      this.studentInfo = null
      ElMessage.success('已退出登录')
    },

    /**
     * ============================
     * 六、（可选）获取某个学生信息（管理端使用）
     * ============================
     * - 对应后端：GET /student/{userId}
     */
    async fetchStudentById(userId: number): Promise<StudentInfo | null> {
      try {
        const info = await get<StudentInfo>(`/student/${userId}`)
        return info
      } catch (err: any) {
        ElMessage.error(err.message || '查询学生信息失败')
        return null
      }
    },

    /**
     * ============================
     * 七、（可选）查询所有学生列表（管理端使用）
     * ============================
     * - 对应后端：GET /student/list
     */
    async fetchAllStudents(): Promise<StudentInfo[]> {
      try {
        const list = await get<StudentInfo[]>('/student/list')
        return list
      } catch (err: any) {
        ElMessage.error(err.message || '获取学生列表失败')
        return []
      }
    },

    /**
     * ============================
     * 八、拉取当前学生已选课程（方案 B，需要传 userId）
     * - 对应后端：GET /student/{studentId}/courses
     */
    async fetchSelectedCourses(): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        const userId = this.studentInfo.userId
        const list = await get<Course[]>(`/student/${userId}/courses`)
        this.studentInfo.courses = list
      } catch (err: any) {
        ElMessage.error(err.message || '获取课程列表失败')
      }
    },

    /**
     * ============================
     * 九、选课（单门，方案 B，传 userId）
     * - 对应后端：POST /student/{studentId}/courses
     * - 参数格式： [courseId]
     */
    async enrollCourse(courseId: number): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        const userId = this.studentInfo.userId
        await post<void>(`/student/${userId}/courses`, [courseId])
        ElMessage.success('选课成功')
        await this.fetchSelectedCourses()
      } catch (err: any) {
        ElMessage.error(err.message || '选课失败')
      }
    },

    /**
     * ============================
     * 十、退选课程（方案 B，传 userId）
     * - 对应后端：DELETE /student/{studentId}/courses/{courseId}
     */
    async dropCourse(courseId: number): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        const userId = this.studentInfo.userId
        await del<void>(`/student/${userId}/courses/${courseId}`)
        ElMessage.success('退选成功')
        await this.fetchSelectedCourses()
      } catch (err: any) {
        ElMessage.error(err.message || '退选失败')
      }
    },

    /**
     * ============================
     * （可选）批量选课（方案 B，如果后端支持）
     * - 对应后端：POST /student/{studentId}/courses
     */
    async selectCourses(courseIds: number[]): Promise<void> {
      if (!this.studentInfo) {
        ElMessage.error('请先登录')
        return
      }
      try {
        const userId = this.studentInfo.userId
        await post<void>(`/student/${userId}/courses`, { courseIds })
        ElMessage.success('选课成功')
        await this.fetchSelectedCourses()
      } catch (err: any) {
        ElMessage.error(err.message || '选课失败')
      }
    },
  },
})
