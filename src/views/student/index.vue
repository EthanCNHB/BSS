<template>
  <div class="student-profile">
    <!-- 个人信息卡片 -->
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <div>
            <el-button type="primary" @click="openEditDialog">修改资料</el-button>
            <el-button type="warning" @click="openPwdDialog">修改密码</el-button>
          </div>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">
          {{ info?.username }}
        </el-descriptions-item>
        <el-descriptions-item label="姓名">
          {{ info?.studentName }}
        </el-descriptions-item>
        <el-descriptions-item label="学院">
          {{ info?.collegeId != null ? collegeStore.nameById(info.collegeId) : '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="专业">
          {{ info?.majorId != null ? majorStore.nameById(info.majorId) : '暂无' }}
        </el-descriptions-item>
        <!-- 不再显示联系方式和邮箱 -->
      </el-descriptions>
    </el-card>

    <!-- 修改资料弹窗 -->
    <el-dialog title="修改资料" v-model="editVisible" width="500px" :destroy-on-close="true">
      <el-form :model="editForm" label-width="100px" :rules="editRules" ref="editFormRef">
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="editForm.studentName" />
        </el-form-item>

        <el-form-item label="学院" prop="collegeId">
          <el-select v-model="editForm.collegeId" placeholder="请选择学院" filterable :loading="collegesLoading">
            <el-option v-for="col in collegeStore.colleges" :key="col.collegeId" :label="col.collegeName" :value="col.collegeId" />
          </el-select>
        </el-form-item>

        <el-form-item label="专业" prop="majorId">
          <el-select v-model="editForm.majorId" placeholder="请选择专业" filterable :loading="majorsLoading">
            <el-option v-for="maj in majorStore.majors" :key="maj.majorId" :label="maj.majorName" :value="maj.majorId" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog title="修改密码" v-model="pwdVisible" width="500px" :destroy-on-close="true">
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
import { ref, onMounted, computed } from 'vue'
import type { FormItemRule } from 'element-plus'
import { ElMessage } from 'element-plus'

import { useStudentStore } from '@/store/modules/student'
import { useCollegeStore } from '@/store/modules/college'
import { useMajorStore } from '@/store/modules/major'
import type { StudentInfo } from '@/store/modules/type'

// —— Pinia Stores —— //
const studentStore = useStudentStore()
const collegeStore = useCollegeStore()
const majorStore = useMajorStore()

// —— 计算属性：拿到最新的 studentInfo —— //
const info = computed<StudentInfo | null>(() => studentStore.getStudentInfo)

// —— 下拉列表加载状态 —— //
const collegesLoading = ref(false)
const majorsLoading = ref(false)

// —— 页面挂载时：先拉学院/专业列表，再拉学生信息 —— //
onMounted(async () => {
  collegesLoading.value = true
  await collegeStore.fetchColleges()
  collegesLoading.value = false

  majorsLoading.value = true
  await majorStore.fetchMajors()
  majorsLoading.value = false

  await studentStore.fetchStudentInfo()
})

// ===========================
// —— 修改资料部分 ——
// ===========================
const editVisible = ref(false)

// editForm 类型：Partial<StudentInfo> 允许字段缺省，初始可写 undefined
const editForm = ref<Partial<StudentInfo>>({
  studentName: '',
  collegeId: undefined,
  majorId: undefined,
})
const editFormRef = ref()

// 校验规则：姓名、学院、专业 三项必填
const editRules = {
  studentName: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
  collegeId: [{ required: true, message: '请选择学院', trigger: 'change' }],
  majorId: [{ required: true, message: '请选择专业', trigger: 'change' }],
}

// 打开“修改资料”对话框，将后端原始数据赋值到 editForm
const openEditDialog = () => {
  if (info.value) {
    editForm.value = {
      studentName: info.value.studentName,
      collegeId: info.value.collegeId,
      majorId: info.value.majorId,
    }
  }
  editVisible.value = true
}

// 提交“修改资料”
const submitEdit = async () => {
  await editFormRef.value.validate()
  // 调用 studentStore.updateStudentInfo，传完整的学生对象
  await studentStore.updateStudentInfo({
    ...info.value!,
    studentName: editForm.value.studentName!,
    collegeId: editForm.value.collegeId!,
    majorId: editForm.value.majorId!,
  })
  editVisible.value = false
  await studentStore.fetchStudentInfo()
  ElMessage.success('资料更新成功')
}

// ===========================
// —— 修改密码部分 ——
// ===========================
const pwdVisible = ref(false)

const pwdForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})
const pwdFormRef = ref()

// 在 <script setup> 内部先定义校验器函数，消除 TS 报红
const confirmPasswordValidator = (_rule: FormItemRule, val: string) => {
  return val === pwdForm.value.newPassword ? Promise.resolve() : Promise.reject(new Error('两次密码不一致'))
}

// 密码修改字段的校验规则
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [
    {
      required: true,
      validator: confirmPasswordValidator,
      trigger: 'blur',
    },
  ],
}

// 打开“修改密码”对话框，同时重置表单
const openPwdDialog = () => {
  pwdForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  }
  pwdVisible.value = true
}

// 提交“修改密码”
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
