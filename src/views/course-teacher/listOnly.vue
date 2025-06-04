<template>
  <div class="teacher-course-container">
    <el-card>
      <template #header>
        <div class="header">我授课的课程</div>
      </template>

      <el-table v-if="!loading" :data="teacherCourses" border style="width: 100%">
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="courseId" label="课程ID" />
        <el-table-column prop="courseCode" label="课程代码" />
        <el-table-column prop="courseType" label="类型" />
        <el-table-column prop="courseName" label="课程名称" />

        <!-- 已绑定教材列 -->
        <el-table-column label="已绑定教材">
          <template #default="{ row }">
            <span>{{ row.textbooks?.[0]?.name || '无教材' }}</span>
          </template>
        </el-table-column>

        <!-- 在“已绑定教材”后面再加一个展开列 -->
        <el-table-column type="expand">
          <template #default="{ row }">
            <div v-if="row.textbooks?.length">
              <!-- 列出所有教材，每个都可关闭解绑 -->
              <el-tag v-for="tb in row.textbooks" :key="tb.textbookId" closable @close="unbindTextbook(row, tb)" style="margin: 4px">
                {{ tb.name }}({{ tb.code }})
              </el-tag>
            </div>
            <div v-else>无教材</div>
          </template>
        </el-table-column>

        <!-- 操作列：始终提供“添加教材” -->
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openAddTextbookDialog(row)">添加教材</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="loading" class="empty-wrapper">
        <el-empty description="加载中..." />
      </div>
      <div v-else-if="!teacherCourses.length" class="empty-wrapper">
        <el-empty description="暂无授课课程" />
      </div>
    </el-card>

    <!-- 添加教材对话框 -->
    <el-dialog v-model="dialogVisible" title="选择教材" width="50%" :before-close="() => (dialogVisible = false)">
      <el-select v-model="selectedTextbookId" placeholder="请选择教材" style="width: 100%">
        <el-option v-for="tb in textbooks" :key="tb.textbookId" :label="tb.name" :value="tb.textbookId" />
      </el-select>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignTextbookToCourse">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useTeacherStore } from '@/store/modules/teacher'
import { useTextbookStore } from '@/store/modules/textbook'
import type { Course, Textbook } from '@/store/modules/type'

const teacherStore = useTeacherStore()
const textbookStore = useTextbookStore()

const teacherCourses = ref<Course[]>([])
const textbooks = ref<Textbook[]>([])
const selectedTextbookId = ref<number>(0)
const selectedCourse = ref<Course | null>(null)
const dialogVisible = ref(false)
const loading = ref(true)

onMounted(async () => {
  try {
    if (!teacherStore.isLoggedIn) {
      ElMessage.error('请先登录')
      return
    }
    await teacherStore.fetchCoursesWithTextbooks()
    teacherCourses.value = teacherStore.teacherInfo?.courses || []

    await textbookStore.fetchTextbooks()
    textbooks.value = textbookStore.textbooks
  } catch {
    ElMessage.error('初始化失败，请重试')
  } finally {
    loading.value = false
  }
})

function openAddTextbookDialog(row: Course) {
  selectedCourse.value = row
  selectedTextbookId.value = row.textbooks?.[0]?.textbookId || 0
  dialogVisible.value = true
}

async function assignTextbookToCourse() {
  if (!selectedTextbookId.value || !selectedCourse.value) {
    ElMessage.error('请选择教材和课程')
    return
  }

  try {
    await textbookStore.assignTextbookToCourse(selectedTextbookId.value, [selectedCourse.value.courseId])
    ElMessage.success('教材分配成功')

    const tb = textbooks.value.find((t) => t.textbookId === selectedTextbookId.value)
    if (tb) {
      teacherCourses.value = teacherCourses.value.map((c) =>
        c.courseId === selectedCourse.value?.courseId ? { ...c, textbooks: [tb] } : c,
      )
    }
  } catch {
    ElMessage.error('分配教材失败，请稍后重试')
  } finally {
    dialogVisible.value = false
  }
}

async function unbindTextbook(row: Course, tb: Textbook) {
  try {
    await textbookStore.unassignTextbookFromCourse(tb.textbookId, row.courseId)
    ElMessage.success('教材解绑成功')

    teacherCourses.value = teacherCourses.value.map((c) =>
      c.courseId === row.courseId ? { ...c, textbooks: c.textbooks?.filter((x) => x.textbookId !== tb.textbookId) } : c,
    )
  } catch {
    ElMessage.error('解绑失败，请稍后重试')
  }
}
</script>

<style scoped>
.teacher-course-container {
  padding: 24px;
  background-color: #f5f7fa;
}
.header {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}
.empty-wrapper {
  padding: 40px 0;
  text-align: center;
}
.el-dialog__footer {
  text-align: right;
}
</style>
