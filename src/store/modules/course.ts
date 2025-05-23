import { defineStore } from 'pinia'
import { del, get, post, put } from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { Course } from './type'

export const useCourseStore = defineStore('course', {
  state: () => ({
    /** 所有课程列表 */
    courses: [] as Course[],
  }),

  getters: {
    /** 课程总数 */
    count: (state) => state.courses.length,

    /** 按 courseId 索引的映射 */
    byId: (state) =>
      state.courses.reduce<Record<number, Course>>((map, c) => {
        map[c.courseId] = c
        return map
      }, {}),
  },

  actions: {
    /** 拉取所有课程 */
    async fetchCourses(): Promise<void> {
      try {
        const list = await get<Course[]>('/courses/list')
        this.courses = list
      } catch (err: any) {
        ElMessage.error('获取课程列表失败：' + (err?.response?.data?.message || err.message || '未知错误'))
      }
    },

    /**
     * 通过majorId获得该专业所有的课程
     * GET /courses/{majorId}
     */
    async getCoursesByMajorId(majorId: number): Promise<Course[]> {
      try {
        const list = await get<Course[]>(`/courses/majorCourses/${majorId}`)
        this.courses = list
        return list
      } catch (err: any) {
        ElMessage.error('获取专业课程失败：' + (err.message || '未知错误'))
        throw err
      }
    },

    /**
     * 获取所有可选课程
     * GET /courses/list
     */
    async getAllCourses(): Promise<Course[]> {
      try {
        const list = await get<Course[]>('/courses/list')
        return list
      } catch (err: any) {
        ElMessage.error('获取可选课程列表失败：' + (err.message || '未知错误'))
        throw err
      }
    },
    /** 获取课程详情 */
    async fetchCourseById(id: number): Promise<Course> {
      try {
        return await get<Course>(`/courses/${id}`)
      } catch (err: any) {
        ElMessage.error('获取课程详情失败：' + (err?.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /** 添加课程 */
    async addCourse(course: Partial<Course>): Promise<void> {
      try {
        console.log(course)
        await post('/courses/add', course)
        ElMessage.success('添加课程成功')
        await this.fetchCourses()
      } catch (err: any) {
        ElMessage.error('添加课程失败：' + (err?.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /** 更新课程 */
    async updateCourse(course: Course): Promise<void> {
      try {
        await put('/courses/update', course)
        ElMessage.success('更新课程成功')
        await this.fetchCourses()
      } catch (err: any) {
        ElMessage.error('更新课程失败：' + (err?.response?.data?.message || err.message || '未知错误'))
        throw err
      }
    },

    /** 删除课程 */
    async deleteCourse(id: number): Promise<void> {
      try {
        await del<void>(`/courses/${id}`)
        ElMessage.success('删除课程成功')
        await this.fetchCourses()
      } catch (err: any) {
        ElMessage.error('删除课程失败: ' + (err.message || '未知错误'))
        throw err
      }
    },
  },
})
