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
        <el-descriptions-item label="用户名">
          {{ teacherInfo?.username }}
        </el-descriptions-item>
        <el-descriptions-item label="姓名">
          {{ teacherInfo?.teacherName }}
        </el-descriptions-item>
        <!-- 依然显示文本名称，而不是 ID -->
        <el-descriptions-item label="学院">
          {{ collegeStore.nameById(teacherInfo?.collegeId ?? -1) }}
        </el-descriptions-item>
        <el-descriptions-item label="专业">
          {{ majorStore.nameById(teacherInfo?.majorId ?? -1) }}
        </el-descriptions-item>
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
    <el-dialog title="编辑个人资料" v-model="showEditDialog" width="500px" :destroy-on-close="true" style="z-index: 9999">
      <el-form :model="editForm" ref="editFormRef" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>

        <el-form-item label="姓名" prop="teacherName" :rules="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
          <el-input v-model="editForm.teacherName" />
        </el-form-item>

        <!-- 把学院输入框改为下拉列表 -->
        <el-form-item label="学院" prop="collegeId" :rules="[{ required: true, message: '请选择学院', trigger: 'change' }]">
          <el-select v-model="editForm.collegeId" placeholder="请选择学院" filterable :loading="collegesLoading">
            <el-option v-for="col in collegeStore.colleges" :key="col.collegeId" :label="col.collegeName" :value="col.collegeId" />
          </el-select>
        </el-form-item>

        <!-- 把专业输入框改为下拉列表 -->
        <el-form-item label="专业" prop="majorId" :rules="[{ required: true, message: '请选择专业', trigger: 'change' }]">
          <el-select v-model="editForm.majorId" placeholder="请选择专业" filterable :loading="majorsLoading">
            <el-option v-for="maj in majorStore.majors" :key="maj.majorId" :label="maj.majorName" :value="maj.majorId" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog title="修改密码" v-model="showPwdDialog" width="400px" :destroy-on-close="true">
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
import { useCollegeStore } from '@/store/modules/college'
import { useMajorStore } from '@/store/modules/major'
import { useRouter } from 'vue-router'
import type { TeacherInfo, Course } from '@/store/modules/type'

const teacherStore = useTeacherStore()
const collegeStore = useCollegeStore()
const majorStore = useMajorStore()
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

// 下拉列表的加载状态指示（可选）
const collegesLoading = ref(false)
const majorsLoading = ref(false)

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
    // store 会提示错误信息
  }
}

// 提交密码修改
async function submitPassword() {
  try {
    await pwdFormRef.value.validate()
    await teacherStore.updatePassword(pwdForm.value.oldPwd, pwdForm.value.newPwd, pwdForm.value.rePwd)
    showPwdDialog.value = false
    // 密码修改成功后可重置表单
    pwdForm.value.oldPwd = ''
    pwdForm.value.newPwd = ''
    pwdForm.value.rePwd = ''
  } catch {
    // store 会提示错误信息
  }
}

// 拉取教师信息
async function fetchInfo() {
  await teacherStore.fetchTeacherInfo()
  teacherInfo.value = teacherStore.teacherInfo
  // 把后端返回的字段赋到 editForm，用于弹窗编辑时的初始值
  editForm.value = { ...teacherStore.teacherInfo }
}

// 拉取我的课程
async function fetchCourses() {
  await teacherStore.fetchBasicCourses()
  courses.value = teacherStore.teacherInfo?.courses || []
}

onMounted(async () => {
  if (!teacherStore.isLoggedIn) {
    router.push({ name: 'login' })
    return
  }

  // 先拉取所有学院列表和专业列表
  collegesLoading.value = true
  await collegeStore.fetchColleges()
  collegesLoading.value = false

  majorsLoading.value = true
  await majorStore.fetchMajors()
  majorsLoading.value = false

  // 再拉取教师信息和课程
  await fetchInfo()
  await fetchCourses()
})
</script>

<style scoped>
.mb-6 {
  margin-bottom: 1.5rem;
}
</style>
