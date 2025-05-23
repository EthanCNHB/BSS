<!-- src/views/admin/AddCollegeAdmin.vue -->
<template>
  <div class="add-college-admin-container p-4 max-w-md mx-auto">
    <el-card shadow="hover">
      <template #header>
        <h2 class="text-xl font-semibold">添加学院管理员</h2>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="mt-4">
        <!-- 学院选择 -->
        <el-form-item label="所属学院" prop="collegeId">
          <el-select v-model="form.collegeId" placeholder="请选择学院">
            <el-option v-for="c in colleges" :key="c.collegeId" :label="c.collegeName" :value="c.collegeId" />
          </el-select>
        </el-form-item>

        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="5-16位，不能包含空格" />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="5-16位，不能包含空格" />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit">添加</el-button>
          <el-button @click="onReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

import { useCollegeStore } from '@/store/modules/college'
import { useCollegeAdminStore } from '@/store/modules/collegeAdmin'
import type { College } from '@/store/modules/type'

const formRef = ref()
const form = ref({
  collegeId: null as number | null,
  username: '',
  password: '',
})

const rules = {
  collegeId: [{ required: true, message: '请选择学院', trigger: 'change' }],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^\S{5,16}$/, message: '5-16位，不含空格', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^\S{5,16}$/, message: '5-16位，不含空格', trigger: 'blur' },
  ],
}

const router = useRouter()
const collegeStore = useCollegeStore()
const adminStore = useCollegeAdminStore()
const colleges = ref<College[]>([])

onMounted(async () => {
  await collegeStore.fetchColleges()
  colleges.value = collegeStore.colleges
})

const onSubmit = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    try {
      // 直接调用 Store 的 register 方法
      await adminStore.register({
        collegeId: form.value.collegeId!,
        username: form.value.username,
        password: form.value.password,
      })
      router.back()
    } catch {
      // 错误提示已在 store 内部处理
    }
  })
}

const onReset = () => {
  formRef.value.resetFields()
}
</script>
