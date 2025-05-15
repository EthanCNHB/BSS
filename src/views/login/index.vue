<template>
  <div class="login_container">
    <el-form class="login_form" @submit.prevent="login" :model="loginForm" :rules="rules" ref="loginForms">
      <h1 class="logo-container">
        <img :src="logoImg" class="logo" />
        欢迎
      </h1>

      <el-form-item prop="username">
        <el-input v-model="loginForm.username" placeholder="用户名">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="密码" show-password>
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button class="login_btn" type="primary" size="large" @click="login">
          <el-icon><Unlock /></el-icon>
          &nbsp; 登录
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="text" @click="goToRegister">没有账号？去注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { User, Lock, Unlock } from '@element-plus/icons-vue'
import logoImg from '@/assets/images/logo.png'
import { useUserStore } from '@/store/modules/user'
import { jumpByRole } from '@/utils/jump'

const userStore = useUserStore()
const router = useRouter()

const loginForm = reactive({
  username: '20212035',
  password: 'a123456',
})

const loginForms = ref()
const loading = ref(false)

const login = async () => {
  await loginForms.value.validate()
  loading.value = true

  try {
    const success = await userStore.doLogin(loginForm.username, loginForm.password)
    if (success) {
      jumpByRole(router, userStore.role)
      ElNotification({
        title: 'Hi, 欢迎回来！',
        message: '登录成功，欢迎！',
        type: 'success',
      })
    } else {
      ElNotification({
        title: '登录失败',
        message: '未知角色或权限错误！',
        type: 'error',
        duration: 2000,
      })
    }
  } catch (error: any) {
    console.error('登录出错:', error)
    ElNotification({
      title: '登录失败',
      message: error.response?.data?.msg || '服务器异常，请稍后重试',
      type: 'error',
      duration: 2000,
    })
  } finally {
    loading.value = false
  }
}

const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 5, max: 16, message: '用户名长度在5到16个字符之间', trigger: 'change' },
  ],
  password: [{ required: true, min: 5, message: '密码长度至少为5位', trigger: 'change' }],
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped lang="scss">
.login_container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-image: url('@/assets/images/login_bg.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.login_form {
  width: 100%;
  max-width: 500px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.1);
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.logo {
  width: 80px;
  height: 80px;
  margin-right: 10px;
  border-radius: 50%;
  border: 2px solid #409eff;
  padding: 5px;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #333;
  font-size: 32px;
  text-align: center;
  margin: 0;
  font-weight: 600;
}

.login_btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #409eff;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s;
}

.login_btn:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
}

.login_btn:active {
  transform: translateY(0);
}
</style>
