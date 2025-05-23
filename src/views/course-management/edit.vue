<template>
  <div class="edit-container">
    <el-card>
      <template #header>
        <div class="header">课程管理</div>
      </template>

      <!-- 新增按钮 -->
      <div class="add-course-button">
        <el-button type="primary" @click="openAddDialog">新增课程</el-button>
      </div>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input v-model="searchName" placeholder="请输入课程名称" clearable />
        <el-button type="primary" @click="searchCourse">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 课程列表 -->
      <el-table :data="filteredCourses" border style="margin-top: 20px">
        <!-- 课程表格 -->
        <el-table-column type="index" label="序号" />
        <el-table-column label="课程ID" prop="courseId" />
        <el-table-column label="课程名称" prop="courseName" />
        <el-table-column label="课程代码" prop="courseCode" />
        <el-table-column label="学分" prop="credit" />
        <el-table-column label="类型" prop="courseType" />
        <el-table-column label="课程描述" prop="description" />

        <!-- 操作 -->
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="confirmDelete(row.courseId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog title="编辑课程" v-model="editDialogVisible">
      <el-form :model="form" label-width="100px">
        <el-form-item label="课程名称">
          <el-input v-model="form.courseName" />
        </el-form-item>
        <el-form-item label="课程代码">
          <el-input v-model="form.courseCode" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="form.credit" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="form.courseType" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="form.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新增课程弹窗 -->
    <el-dialog title="新增课程" v-model="addDialogVisible">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="课程名称">
          <el-input v-model="addForm.courseName" />
        </el-form-item>
        <el-form-item label="课程代码">
          <el-input v-model="addForm.courseCode" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="addForm.credit" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="addForm.courseType" />
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="addForm.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveNewCourse">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCourseStore } from '@/store/modules/course'

// 获取课程仓库
const courseStore = useCourseStore()

// 搜索关键词
const searchName = ref('')

// 控制编辑弹窗显示
const editDialogVisible = ref(false)

// 控制新增课程弹窗显示
const addDialogVisible = ref(false)

// 当前编辑的课程对象
const form = ref<any>({})

// 新增课程的表单对象
const addForm = ref<any>({
  courseName: '',
  courseCode: '',
  credit: '',
  courseType: '',
  description: '',
})

// 页面加载时候获取课程列表
onMounted(() => {
  courseStore.fetchCourses()
})

// 搜索过滤
const filteredCourses = computed(() => {
  if (!searchName.value) return courseStore.courses
  return courseStore.courses.filter((t) => t.courseName.includes(searchName.value))
})

// 搜索按钮操作
const searchCourse = () => {}

// 重置搜索栏
const resetSearch = () => {
  searchName.value = ''
}

// 打开编辑弹窗，并加载当前行教材数据
const openEditDialog = (row: any) => {
  form.value = { ...row }
  editDialogVisible.value = true
}

// 编辑保存按钮
const saveEdit = () => {
  const index = courseStore.courses.findIndex((t) => t.courseId === form.value.courseId)
  if (index !== -1) {
    courseStore.updateCourse(form.value)
    ElMessage.success('保存成功')
    editDialogVisible.value = false
  } else {
    ElMessage.error('保存失败')
  }
}

// 删除确认
const confirmDelete = (courseId: number) => {
  ElMessageBox.confirm('确认删除此课程吗？', '删除课程', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      await deleteCourse(courseId)
    })
    .catch(() => {
      ElMessage.info('删除已取消')
    })
}

// 删除课程
const deleteCourse = async (courseId: number) => {
  try {
    await courseStore.deleteCourse(courseId)
    ElMessage.success('删除成功')
  } catch {
    ElMessage.error('删除失败')
  }
}

// 打开新增课程弹窗
const openAddDialog = () => {
  addForm.value = { courseName: '', courseCode: '', credit: '', courseType: '', description: '' }
  addDialogVisible.value = true
}

// 新增课程保存按钮
const saveNewCourse = async () => {
  try {
    await courseStore.addCourse(addForm.value)
    ElMessage.success('新增课程成功')
    addDialogVisible.value = false
    courseStore.fetchCourses() // 更新课程列表
  } catch (err) {
    ElMessage.error('新增课程失败')
  }
}
</script>

<style scoped>
.edit-container {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}
.add-course-button {
  margin-bottom: 10px;
}
</style>
