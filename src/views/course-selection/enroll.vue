<template>
  <div class="enroll">
    <el-card>
      <template #header>可选课程</template>

      <el-table :data="courses" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="courseCode" label="课程代码" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" :disabled="isSelected(row.courseId)" @click="enroll(row.courseId)">
              {{ isSelected(row.courseId) ? '已选择' : '选课' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useCourseStore } from '@/store/modules/course'
import { useStudentStore } from '@/store/modules/student'

const courseStore = useCourseStore()
const studentStore = useStudentStore()

onMounted(async () => {
  // 确保先拉取到 studentInfo（已经在登录后调用过 fetchStudentInfo）
  // 再拉取课程列表和已选列表
  await courseStore.fetchCourses()
  await studentStore.fetchSelectedCourses()
})

const courses = computed(() => courseStore.courses)
const isSelected = studentStore.isCourseSelected

const enroll = async (id: number) => {
  await studentStore.enrollCourse(id)
}
</script>

<style scoped>
.enroll {
  padding: 20px;
}
</style>
