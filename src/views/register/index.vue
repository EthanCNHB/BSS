<template>
  <div class="register_container">
    <el-form class="register_form" ref="registerFormRef" :model="registerForm" :rules="rules" @submit.prevent="onRegister">
      <h1 class="logo-container">
        <img :src="logoImg" class="logo" />
        欢迎注册
      </h1>

      <!-- 用户名 -->
      <el-form-item prop="username">
        <el-input v-model="registerForm.username" placeholder="用户名">
          <template #prefix>
            <el-icon>
              <User />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 密码 -->
      <el-form-item prop="password">
        <el-input type="password" v-model="registerForm.password" placeholder="密码" show-password>
          <template #prefix>
            <el-icon>
              <Lock />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <!-- 注册按钮 -->
      <el-form-item>
        <el-button class="register_btn" type="primary" size="large" :loading="loading" @click="onRegister">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import logoImg from '@/assets/images/logo.png'
import type { FormInstance } from 'element-plus'

import { useStudentStore } from '@/store/modules/student'

// 注册表单数据（只保留用户名和密码）
const registerForm = reactive({
  username: '',
  password: '',
})

// 表单引用
const registerFormRef = ref<FormInstance>()

// 路由实例
const router = useRouter()

// Pinia 实例
const studentStore = useStudentStore()

// 注册按钮 loading 状态
const loading = ref(false)

/**
 * 注册函数：利用 Pinia 中的 register 方法
 */
const onRegister = async () => {
  if (loading.value) return

  // 先校验表单字段
  try {
    await registerFormRef.value?.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    // 调用 studentStore.register，将参数传给后端
    await studentStore.register(registerForm.username.trim(), registerForm.password.trim())

    ElNotification({
      title: '注册成功',
      message: '请前往登录页面',
      type: 'success',
    })

    setTimeout(() => {
      router.push('/login')
    }, 800)
  } catch (err: any) {
    const msg = err?.message || '注册失败，请稍后再试'
    if (msg.includes('已存在')) {
      ElNotification({
        title: '注册失败',
        message: '用户名已存在，请更换后重试',
        type: 'warning',
      })
    } else {
      ElNotification({
        title: '注册失败',
        message: msg,
        type: 'error',
      })
    }
  } finally {
    loading.value = false
  }
}

// 表单校验规则，与后端保持一致：用户名/密码 5~16 个非空白字符
const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    {
      min: 5,
      max: 16,
      message: '用户名长度应为 5~16 个字符（不能含空格）',
      trigger: ['blur', 'change'],
    },
    {
      pattern: /^\S{5,16}$/,
      message: '用户名只能是 5~16 个非空白字符',
      trigger: ['blur', 'change'],
    },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    {
      min: 5,
      max: 16,
      message: '密码长度应为 5~16 个字符（不能含空格）',
      trigger: ['blur', 'change'],
    },
    {
      pattern: /^\S{5,16}$/,
      message: '密码只能是 5~16 个非空白字符',
      trigger: ['blur', 'change'],
    },
  ],
}
</script>

<style scoped lang="scss">
.register_container {
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

.register_form {
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

.register_btn {
  width: 100%;
  height: 50px;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #67c23a;
  border-radius: 5px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s;
}

.register_btn:hover {
  background-color: #85ce61;
  transform: translateY(-2px);
}

.register_btn:active {
  transform: translateY(0);
}
</style>
