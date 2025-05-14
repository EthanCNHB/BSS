import { defineStore } from 'pinia'
import { reactive, computed } from 'vue'
import axios from 'axios'
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from '@/utils/token'
import { ElMessage } from 'element-plus'

const useStudentStore = defineStore('student', {
  state: () => ({
    token: GET_TOKEN(), // 从 localStorage 获取 token
    username: '',
    avatar: '',
    studentInfo: null, // 存储学生信息
  }),

  actions: {
    // 用户登录
    async userLogin(username: string, password: string) {
      try {
        const response = await axios.post('http://localhost:8080/student/login', { username, password })
        if (response.data.code === 0) {
          this.token = response.data.data
          SET_TOKEN(this.token) // 将 token 存储到 localStorage
          ElMessage.success('登录成功')
        } else {
          ElMessage.error('登录失败：' + response.data.message)
        }
      } catch (error) {
        ElMessage.error('登录请求错误')
        console.error(error)
      }
    },

    // 获取学生信息
    async fetchStudentInfo() {
      try {
        const response = await axios.get('http://localhost:8080/student/studentInfo', {
          headers: { Authorization: `Bearer ${this.token}` },
        })
        if (response.data.code === 0) {
          this.studentInfo = response.data.data
        } else {
          ElMessage.error('获取个人信息失败')
        }
      } catch (error) {
        ElMessage.error('获取个人信息失败')
        console.error(error)
      }
    },

    // 更新学生资料
    async updateStudentInfo(updatedInfo: any) {
      try {
        const response = await axios.put('http://localhost:8080/student/update', updatedInfo, {
          headers: { Authorization: `Bearer ${this.token}` },
        })
        if (response.data.code === 0) {
          ElMessage.success('信息更新成功')
        } else {
          ElMessage.error('更新失败：' + response.data.message)
        }
      } catch (error) {
        ElMessage.error('更新请求错误')
        console.error(error)
      }
    },

    // 修改密码
    async updatePassword(oldPassword: string, newPassword: string, confirmPassword: string) {
      try {
        const response = await axios.patch(
          'http://localhost:8080/student/updatePwd',
          { old_pwd: oldPassword, new_pwd: newPassword, re_pwd: confirmPassword },
          { headers: { Authorization: `Bearer ${this.token}` } },
        )
        if (response.data.code === 0) {
          ElMessage.success('密码修改成功')
        } else {
          ElMessage.error('密码修改失败：' + response.data.message)
        }
      } catch (error) {
        ElMessage.error('修改密码请求错误')
        console.error(error)
      }
    },

    // 用户退出
    userLogout() {
      this.token = ''
      this.studentInfo = null
      REMOVE_TOKEN() // 移除 token
      ElMessage.success('退出登录成功')
    },
  },

  getters: {
    isLoggedIn: (state) => !!state.token,
    getStudentInfo: (state) => state.studentInfo,
  },
})

export default useStudentStore
