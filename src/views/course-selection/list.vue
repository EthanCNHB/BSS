<template>
  <div class="course-list">
    <el-card>
      <template #header>我的课程</template>

      <el-table :data="courses" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="courseCode" label="课程代码" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="drop(row.courseId)">退选</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useStudentStore } from '@/store/modules/student'

const studentStore = useStudentStore()

onMounted(() => {
  // 确保先 fetchStudentInfo，接着 fetchSelectedCourses
  studentStore.fetchSelectedCourses()
})

const courses = computed(() => studentStore.selectedCourses)

const drop = async (id: number) => {
  await studentStore.dropCourse(id)
}
</script>

<style scoped>
.course-list {
  padding: 20px;
}
</style>
