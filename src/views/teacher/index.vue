<template>
  <div class="teacher-home p-4 max-w-4xl mx-auto">
    <!-- 个人信息卡片 -->
    <el-card shadow="hover" class="mb-6">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-semibold">个人信息</h2>
          <div class="space-x-2">
            <el-button type="primary" @click="showEditDialog = true">修改资料</el-button>
            <el-button type="warning" @click="showPwdDialog = true">修改密码</el-button>
          </div>
        </div>
      </template>

      <el-descriptions class="mt-4" :column="2" border>
        <el-descriptions-item label="用户名">{{ teacherInfo?.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ teacherInfo?.teacherName }}</el-descriptions-item>
        <el-descriptions-item label="学院ID">{{ teacherInfo?.collegeId }}</el-descriptions-item>
        <el-descriptions-item label="专业ID">{{ teacherInfo?.majorId }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 我的课程卡片 -->
    <el-card shadow="hover" class="mb-6">
      <template #header>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-semibold">我的课程</h2>
        </div>
      </template>
      <el-table :data="courses" stripe>
        <el-table-column prop="courseCode" label="课程代码" />
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="mini" type="danger" @click="unassign(row.courseId)">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑资料弹窗 -->
    <el-dialog title="编辑个人资料" v-model:visible="showEditDialog" width="500px" :destroy-on-close="true" style="z-index: 9999">
      <el-form :model="editForm" ref="editFormRef" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>
        <el-form-item label="姓名" prop="teacherName" :rules="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
          <el-input v-model="editForm.teacherName" />
        </el-form-item>
        <el-form-item label="学院ID" prop="collegeId">
          <el-input-number v-model="editForm.collegeId" :min="1" />
        </el-form-item>
        <el-form-item label="专业ID" prop="majorId">
          <el-input-number v-model="editForm.majorId" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog title="修改密码" v-model:visible="showPwdDialog" width="400px" :destroy-on-close="true">
      <el-form :model="pwdForm" ref="pwdFormRef" label-width="100px">
        <el-form-item label="旧密码" prop="oldPwd" :rules="[{ required: true, message: '请输入旧密码', trigger: 'blur' }]">
          <el-input v-model="pwdForm.oldPwd" type="password" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd" :rules="[{ required: true, message: '请输入新密码', trigger: 'blur' }]">
          <el-input v-model="pwdForm.newPwd" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="rePwd" :rules="[{ required: true, message: '请确认新密码', trigger: 'blur' }]">
          <el-input v-model="pwdForm.rePwd" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPwdDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPassword">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useTeacherStore } from '@/store/modules/teacher'
import { useRouter } from 'vue-router'
import type { TeacherInfo, Course } from '@/store/modules/type'

const teacherStore = useTeacherStore()
const router = useRouter()

// 本地状态
const teacherInfo = ref<TeacherInfo | null>(null)
const courses = ref<Course[]>([])

// 编辑资料
const showEditDialog = ref(false)
const editForm = ref<Partial<TeacherInfo>>({})
const editFormRef = ref()

// 修改密码
const showPwdDialog = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', rePwd: '' })
const pwdFormRef = ref()

// 批量分配
const allCourses = ref<Course[]>([])

// 撤销课程
async function unassign(courseId: number) {
  await teacherStore.unassignCourse(courseId)
  await fetchCourses()
}

// 提交编辑
async function submitEdit() {
  try {
    await editFormRef.value.validate()
    await teacherStore.updateTeacherInfo(editForm.value)
    showEditDialog.value = false
    await fetchInfo()
  } catch {
    // store 会提示
  }
}

// 提交密码修改
async function submitPassword() {
  try {
    await pwdFormRef.value.validate()
    await teacherStore.updatePassword(pwdForm.value.oldPwd, pwdForm.value.newPwd, pwdForm.value.rePwd)
    showPwdDialog.value = false
  } catch {
    // store 会提示
  }
}

// 拉取教师信息
async function fetchInfo() {
  await teacherStore.fetchTeacherInfo()
  teacherInfo.value = teacherStore.teacherInfo
  editForm.value = { ...teacherStore.teacherInfo }
}

// 拉取我的课程
async function fetchCourses() {
  await teacherStore.fetchCourses()
  courses.value = teacherStore.teacherInfo?.courses || []
}

async function fetchAllCourses() {
  try {
    const list = await teacherStore.getAllCourses()
    allCourses.value = list
  } catch (err) {
    // 错误处理
    console.error('获取所有可选课程失败', err)
  }
}

onMounted(async () => {
  if (!teacherStore.isLoggedIn) {
    router.push({ name: 'login' })
    return
  }
  await fetchInfo()
  await fetchCourses()
  await fetchAllCourses()
})
</script>

<style scoped>
.mb-6 {
  margin-bottom: 1.5rem;
}
</style>
