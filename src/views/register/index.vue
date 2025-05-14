<template>
  <div class="register_container">
    <el-form class="register_form" @submit.prevent="register" :model="registerForm" :rules="rules" ref="registerForms">
      <h1 class="logo-container">
        <img :src="logoImg" class="logo" />
        欢迎注册
      </h1>

      <el-form-item prop="username">
        <el-input v-model="registerForm.username" placeholder="用户名">
          <template #prefix>
            <el-icon>
              <User />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input type="password" v-model="registerForm.password" placeholder="密码" show-password>
          <template #prefix>
            <el-icon>
              <Lock />
            </el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item prop="avatar">
        <el-input v-model="registerForm.avatar" placeholder="头像链接 (可选)" />
      </el-form-item>

      <el-form-item>
        <el-button class="register_btn" type="primary" size="large" @click="register">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import axios from 'axios'
import { User, Lock } from '@element-plus/icons-vue'
import logoImg from '@/assets/images/logo.png'

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  avatar: '',
})

// 表单引用
const registerForms = ref()

// 路由实例
const router = useRouter()

// 注册逻辑
const register = async () => {
  await registerForms.value.validate()
  try {
    await axios.post('http://localhost:8080/student/register', null, {
      params: {
        username: registerForm.username,
        password: registerForm.password,
      },
    })

    ElNotification({
      title: '注册成功',
      message: '请前往登录',
      type: 'success',
    })

    router.push('/login')
  } catch (error: any) {
    const msg = error?.response?.data?.message || '注册失败，请稍后重试'
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
  }
}

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 5, max: 16, message: '用户名长度应为 5~16 位', trigger: 'change' },
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 5, max: 1000, message: '密码至少为 5 位', trigger: 'change' },
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
