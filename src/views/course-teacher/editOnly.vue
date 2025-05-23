<template>
  <div class="add-course-container">
    <el-card>
      <template #header>
        <div class="header">所有可选课程</div>
      </template>

      <!-- 显示所有课程 -->
      <el-table :data="courses" border style="width: 100%">
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="courseId" label="课程ID" width="100" />
        <el-table-column prop="courseCode" label="课程代码" width="150" />
        <el-table-column prop="courseName" label="课程名称" width="200" />
        <el-table-column prop="courseType" label="课程类型" width="150" />
        <el-table-column prop="credit" label="学分" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleAddCourse(row)">添加到我的课程</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useTeacherStore } from '@/store/modules/teacher'
import { useCourseStore } from '@/store/modules/course'
// 获取教师 store 和课程 store
const teacherStore = useTeacherStore()
const courseStore = useCourseStore()

// 存储所有可选课程
const courses = ref<Array<{ courseId: number; courseCode: string; courseName: string; courseType: string; credit: number }>>([])

onMounted(async () => {
  // 判断教师是否登录
  if (!teacherStore.isLoggedIn) {
    ElMessage.error('请先登录')
    return
  }

  try {
    // 拉取所有课程
    const coursesList = await courseStore.getAllCourses()

    courses.value = coursesList.map((course: any) => {
      const { students, teachers, ...restCourse } = course // 排除 students 和 teachers
      return restCourse // 返回只包含课程字段的数据
    })
  } catch (err) {
    ElMessage.error('获取课程列表失败')
  }
})

// 添加课程到教师课程列表
const handleAddCourse = async (course: any) => {
  if (!teacherStore.teacherInfo) {
    ElMessage.error('教师信息未找到')
    return
  }
  try {
    // 将课程添加到教师的课程列表中
    await teacherStore.assignCourses([course.courseId])
  } catch (err) {
    ElMessage.error('添加课程失败')
  }
}
</script>

<style scoped>
.add-course-container {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
