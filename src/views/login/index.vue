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
            <el-icon>
              <User />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="密码" show-password>
          <template #prefix>
            <el-icon>
              <Lock />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="login_btn" type="primary" size="large" @click="login">
          <el-icon><Unlock /></el-icon>
          &nbsp; 登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import logoImg from '@/assets/images/logo.png'
import useUserStore from '@/store/modules/user'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { getTime } from '@/utils/time'

// 创建用户仓库的实例
const userStore = useUserStore()
// 收集登录表单数据
const loginForm = reactive({
  username: '20212035',
  password: 'a123456',
})

let loginForms = ref()

let loading = ref(false)

const router = useRouter()

// 登录方法定义
const login = async () => {
  await loginForms.value.validate()

  loading.value = true // 显示加载动画

  try {
    // 通知仓库发起登录请求
    const loginResult = await userStore.userLogin(loginForm)

    // 根据返回的状态码处理不同情况
    if (loginResult === 200) {
      // 登录成功后，跳转到首页
      router.push('/')

      ElNotification({
        title: 'Hi,' + getTime() + '好！',
        message: '欢迎回来！',
        type: 'success',
      })
    } else {
      // 登录失败，提示错误信息
      let errorMessage
      switch (loginResult) {
        case 201:
          errorMessage = '用户名或密码错误' // 登录失败提示
          break
        case 500:
          errorMessage = '服务器错误，请稍后再试' // 处理服务器错误
          break
        default:
          errorMessage = '登录失败，请检查输入' // 默认提示
      }
      ElNotification({
        title: '登录失败',
        message: errorMessage,
        type: 'error',
        duration: 2000,
      })
    }
  } catch (error) {
    // 处理登录请求时的异常
    console.error('登录发生错误:' + error)
    ElNotification({
      title: '登录发生错误',
      message: '请稍后重试.',
      type: 'error',
      duration: 2000,
    })
  }
}

//表单检验规则
const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { required: true, min: 5, max: 16, message: '用户名长度在5到16个字符之间', trigger: 'change' },
  ],
  password: [{ required: true, min: 5, max: 1000, message: '密码长度至少为5位最多是1000位', trigger: 'change' }],
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
