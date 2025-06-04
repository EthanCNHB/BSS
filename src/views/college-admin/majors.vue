<template>
  <div v-if="collegeStore.collegeInfo">
    <el-card>
      <template #header>
        <div class="header">专业列表</div>
      </template>

      <!-- 操作区：新增专业、一键展开/收起 -->
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="openAddDialog">新增专业</el-button>
        <div class="collapse-controls">
          <el-button @click="expandAll" size="small" type="primary" class="expand-btn">一键展开</el-button>
          <el-button @click="collapseAll" size="small" type="warning" class="collapse-btn">一键收起</el-button>
        </div>
      </div>

      <!-- 使用 el-collapse 来显示每个专业 -->
      <el-collapse v-model="activeNames" class="custom-collapse">
        <el-collapse-item v-for="major in formattedMajors" :key="major.majorId" :title="major.majorName" :name="String(major.majorId)">
          <div class="button-container">
            <el-button @click="openAddCourseDialog(major.majorId)" size="small" type="success">添加课程</el-button>
            <el-button @click="confirmDeleteMajor(major.majorId)" size="small" type="danger">删除专业</el-button>
          </div>
          <!-- 展示该专业下的课程 -->
          <el-table v-if="major.courses.length > 0" :data="major.courses" style="width: 100%; margin-top: 10px" class="custom-table">
            <el-table-column label="课程名称" prop="courseName" />
            <el-table-column label="课程编号" prop="courseCode" />
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
          <p v-else class="no-courses-text">该专业暂无课程</p>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <!-- 添加课程弹窗 -->
    <el-dialog title="添加课程" v-model="addCourseDialogVisible" width="400px">
      <el-form :model="newCourse" ref="addCourseForm" label-width="100px">
        <el-form-item label="课程名称" prop="courseName" :rules="requiredRule">
          <el-input v-model="newCourse.courseName" />
        </el-form-item>
        <el-form-item label="课程编号" prop="courseCode" :rules="requiredRule">
          <el-input v-model="newCourse.courseCode" />
        </el-form-item>
        <el-form-item label="学分" prop="credit" :rules="requiredRule">
          <el-input v-model="newCourse.credit" />
        </el-form-item>
        <el-form-item label="课程类型" prop="courseType">
          <el-input v-model="newCourse.courseType" />
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input type="textarea" v-model="newCourse.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addCourseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddCourse">保存</el-button>
      </template>
    </el-dialog>

    <!-- 编辑课程弹窗 -->
    <el-dialog title="编辑课程" v-model="editDialogVisible" width="400px">
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

    <!-- 新增专业弹窗 -->
    <el-dialog title="新增专业" v-model="addDialogVisible" width="400px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="专业名称">
          <el-input v-model="addForm.majorName" />
        </el-form-item>

        <el-form-item label="专业描述">
          <el-input v-model="addForm.majorDescription" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveNewMajor">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import { useCollegeStore } from '@/store/modules/college'
import { useMajorStore } from '@/store/modules/major'
import { useCourseStore } from '@/store/modules/course'
import { ElMessage, ElMessageBox } from 'element-plus'

const collegeStore = useCollegeStore()
const majorStore = useMajorStore()
const courseStore = useCourseStore()

// 存储展开的专业的 ID
const activeNames = ref<string[]>([])

// 当前编辑的课程对象
const form = ref<any>({})

// 控制编辑弹窗显示
const editDialogVisible = ref(false)

// 存储添加课程的表单数据
const newCourse = ref({
  courseName: '',
  courseCode: '',
  description: '',
  courseType: '',
  credit: 0,
  majorId: 0, // 绑定专业ID
})

// 控制新增专业弹窗显示
const addDialogVisible = ref(false)

// 控制添加课程弹窗显示
const addCourseDialogVisible = ref(false)

const requiredRule = { required: true, message: '此项不能为空', trigger: 'blur' }

// 新增专业的表单对象
const addForm = ref<any>({
  collegeId: 0,
  majorName: '',
  majorDescription: '',
})

// 格式化专业列表中的创建时间，并为每个专业关联课程
const formattedMajors = computed(() => {
  return majorStore.majors.map((major) => {
    return {
      ...major,
      courses: courseStore.courses.filter((course) => course.majorId === major.majorId),
    }
  })
})

// 获取当前学院信息并加载数据
onMounted(async () => {
  // 1. 拿到当前学院信息
  const collegeInfo = collegeStore.collegeInfo!
  // 2. 拉取该学院下的专业列表
  await majorStore.fetchMajorsByCollege(collegeInfo.collegeId)
  // 3. 一次性拉所有课程
  await courseStore.fetchCourses()
  // 4. 全部展开
  expandAll()
})

// 一键展开所有专业
const expandAll = () => {
  activeNames.value = formattedMajors.value.map((major) => String(major.majorId))
}

// 一键收起所有专业
const collapseAll = () => {
  activeNames.value = []
}

// 打开添加课程对话框
const openAddCourseDialog = (majorId: number) => {
  newCourse.value.majorId = majorId
  addCourseDialogVisible.value = true
}

// 提交添加课程
const submitAddCourse = async () => {
  const { courseName, courseCode, credit, courseType, description, majorId } = newCourse.value
  if (!courseName || !courseCode || !credit) {
    ElMessage.error('请填写完整的课程信息')
    return
  }

  try {
    await courseStore.addCourse({
      courseName,
      courseCode,
      credit,
      courseType,
      description,
      majorId,
    })
    addCourseDialogVisible.value = false
    ElMessage.success('课程添加成功')
  } catch (err) {
    ElMessage.error('添加课程失败')
  }
}

// 打开编辑弹窗，并加载当前行课程数据
const openEditDialog = (row: any) => {
  form.value = { ...row }
  editDialogVisible.value = true
}

// 编辑保存按钮
const saveEdit = async () => {
  const index = courseStore.courses.findIndex((t) => t.courseId === form.value.courseId)
  if (index !== -1) {
    try {
      await courseStore.updateCourse(form.value)
      ElMessage.success('保存成功')
      editDialogVisible.value = false
    } catch {
      ElMessage.error('保存失败')
    }
  } else {
    ElMessage.error('保存失败')
  }
}

// 删除课程确认
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

// 删除专业确认
const confirmDeleteMajor = (majorId: number) => {
  ElMessageBox.confirm('确认删除此专业及其所有课程吗？', '删除专业', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      await deleteMajor(majorId)
    })
    .catch(() => {
      ElMessage.info('删除已取消')
    })
}

// 删除专业
const deleteMajor = async (majorId: number) => {
  try {
    await majorStore.deleteMajor(majorId)
    ElMessage.success('删除专业成功')
    // 刷新专业列表
    const collegeInfo = collegeStore.collegeInfo!
    await majorStore.fetchMajorsByCollege(collegeInfo.collegeId)
    // 同时可以重置展开状态
    collapseAll()
  } catch {
    ElMessage.error('删除专业失败')
  }
}

// 打开新增专业弹窗
const openAddDialog = () => {
  addForm.value = {
    collegeId: collegeStore.collegeInfo?.collegeId,
    majorName: '',
    majorDescription: '',
  }
  addDialogVisible.value = true
}

// 新增专业保存按钮
const saveNewMajor = async () => {
  try {
    await majorStore.addMajor(addForm.value)
    ElMessage.success('新增专业成功')
    addDialogVisible.value = false
    const collegeInfo = collegeStore.collegeInfo!
    await majorStore.fetchMajorsByCollege(collegeInfo.collegeId)
    expandAll()
  } catch (err) {
    ElMessage.error('新增专业失败')
  }
}
</script>

<style scoped>
.header {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.collapse-controls {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.collapse-controls .el-button {
  margin-left: 10px;
}

.expand-btn {
  background-color: #409eff;
  color: white;
}

.collapse-btn {
  background-color: #ff8c00;
  color: white;
}

.el-table {
  margin-top: 20px;
}

.el-table-column {
  text-align: center;
}

.no-courses-text {
  color: #999;
  text-align: center;
  margin-top: 10px;
}

/* 将按钮容器放到右侧排列 */
.button-container {
  display: flex;
  justify-content: flex-end;
  gap: 10px; /* 按钮间距 */
  margin-top: 10px;
}

/* 分割线改为深色 */
.custom-collapse ::v-deep .el-collapse-item__header {
  border-bottom: 1px solid #999;
}

/* 表格边框改为深色 */
.custom-table ::v-deep .el-table__header,
.custom-table ::v-deep .el-table__body {
  border-color: #999;
}
</style>
