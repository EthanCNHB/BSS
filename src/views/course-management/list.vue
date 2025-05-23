<template>
  <div class="home-container">
    <el-card>
      <template #header>
        <div class="header">课程列表</div>
      </template>

      <!-- 查询区域 -->
      <div class="search-bar">
        <el-input v-model="searchName" placeholder="请输入课程名称" clearable />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 课程表格 -->
      <el-table :data="paginatedData" border style="width: 100%; margin-top: 20px">
        <el-table-column type="index" label="序号" />
        <el-table-column label="课程ID" prop="courseId" />
        <el-table-column label="课程名称" prop="courseName" />
        <el-table-column label="课程代码" prop="courseCode" />
        <el-table-column label="学分" prop="credit" />
        <el-table-column label="课程描述" prop="description" />
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        background
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredCourses.length"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useCourseStore } from '@/store/modules/course'
const courseStore = useCourseStore()
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(5)

// 根据窗口高度动态设置每页显示条数
const calculatePageSize = () => {
  const windowHeight = window.innerHeight
  // 估算每行表格高度为50px，减去上部空白和分页高度
  const availableHeight = windowHeight - 300
  pageSize.value = Math.floor(availableHeight / 50)
}

onMounted(() => {
  courseStore.fetchCourses() // 拉取课程数据
  calculatePageSize()
  window.addEventListener('resize', calculatePageSize)
})

const handleSearch = () => {
  currentPage.value = 1
}

const resetSearch = () => {
  searchName.value = ''
  currentPage.value = 1
}

const handlePageChange = (newPage: number) => {
  currentPage.value = newPage
}

const filteredCourses = computed(() => {
  if (!searchName.value) return courseStore.courses
  return courseStore.courses.filter((course: any) => course.courseName.includes(searchName.value))
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredCourses.value.slice(start, end)
})
</script>

<style scoped>
.home-container {
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
  align-items: center;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
