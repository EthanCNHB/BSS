<template>
  <div class="student-profile">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <div>
            <el-button type="primary" @click="editVisible = true">修改资料</el-button>
            <el-button type="warning" @click="pwdVisible = true">修改密码</el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">{{ info?.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ info?.name }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ info?.collegeName }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ info?.majorName }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ info?.phone || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ info?.email || '暂无' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 修改资料弹窗 -->
    <el-dialog title="修改资料" v-model="editVisible" width="500px">
      <el-form :model="editForm" label-width="100px" :rules="rules" ref="editFormRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="学院" prop="collegeName">
          <el-input v-model="editForm.collegeName" />
        </el-form-item>
        <el-form-item label="专业" prop="majorName">
          <el-input v-model="editForm.majorName" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog title="修改密码" v-model="pwdVisible" width="500px">
      <el-form :model="pwdForm" label-width="100px" :rules="pwdRules" ref="pwdFormRef">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPwd">修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useStudentStore } from '@/store/modules/student'
import { ElMessage } from 'element-plus'
import type { StudentInfo } from '@/store/modules/type'

const studentStore = useStudentStore()

const info = computed<StudentInfo | null>(() => studentStore.getStudentInfo)

onMounted(() => {
  studentStore.fetchStudentInfo()
})

// 修改资料弹窗状态与数据
const editVisible = ref(false)
const editForm = ref({
  name: '',
  collegeName: '',
  majorName: '',
  phone: '',
  email: '',
})
const editFormRef = ref()
const rules = {
  name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
}
const submitEdit = async () => {
  await editFormRef.value.validate()
  await studentStore.updateStudentInfo(editForm.value)
  editVisible.value = false
  studentStore.fetchStudentInfo()
  ElMessage.success('资料更新成功')
}

// 弹窗打开时预填表单
watch(editVisible, (val) => {
  if (val && info.value) {
    Object.assign(editForm.value, info.value)
  }
})

// 修改密码弹窗
const pwdVisible = ref(false)
const pwdForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})
const pwdFormRef = ref()
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    {
      required: true,
      validator: (_: any, val: string) => (val === pwdForm.value.newPassword ? Promise.resolve() : Promise.reject('两次密码不一致')),
      trigger: 'blur',
    },
  ],
}
const submitPwd = async () => {
  await pwdFormRef.value.validate()
  await studentStore.updatePassword(pwdForm.value.oldPassword, pwdForm.value.newPassword, pwdForm.value.confirmPassword)
  pwdVisible.value = false
}
</script>

<style scoped>
.student-profile {
  padding: 30px;
  max-width: 800px;
  margin: auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
