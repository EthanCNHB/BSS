<!-- src/views/report/ReservationReport.vue -->
<template>
  <div class="reservation-report-container">
    <el-card>
      <template #header>
        <div class="header">征订报告</div>
      </template>

      <!-- 搜索栏：按学生姓名、专业、教材名称 搜索 -->
      <div class="search-bar">
        <el-input v-model="searchStudent" placeholder="按学生姓名搜索" clearable style="width: 200px" />
        <el-input v-model="searchMajor" placeholder="按专业搜索" clearable style="width: 200px; margin-left: 10px" />
        <el-input v-model="searchTextbook" placeholder="按教材名称搜索" clearable style="width: 200px; margin-left: 10px" />
        <el-button type="primary" @click="handleSearch" style="margin-left: 10px">搜索</el-button>
        <el-button @click="resetSearch" style="margin-left: 5px">重置</el-button>
      </div>

      <!-- 显示过滤后的订单数据 -->
      <el-table :data="filteredReservations" border style="width: 100%; margin-top: 20px">
        <!-- 序号 -->
        <el-table-column type="index" label="序号" width="60" />

        <!-- 订单ID -->
        <el-table-column prop="reservationId" label="订单ID" width="100" />

        <!-- 学生ID -->
        <el-table-column prop="userId" label="学生ID" width="80" />

        <!-- 学生姓名 -->
        <el-table-column prop="studentName" label="学生姓名" width="120" />

        <!-- 专业 -->
        <el-table-column prop="majorName" label="专业" width="150" />

        <!-- 教材ID -->
        <el-table-column prop="textbookId" label="教材ID" width="80" />

        <!-- 教材名称 -->
        <el-table-column prop="textbookName" label="教材名称" />

        <!-- 订购数量 -->
        <el-table-column prop="reservationQuantity" label="订购数量" width="100" />

        <!-- 教材单价 -->
        <el-table-column prop="textbookPrice" label="教材单价" width="100">
          <template #default="{ row }">¥{{ row.textbookPrice?.toFixed(2) }}</template>
        </el-table-column>

        <!-- 下单时间 -->
        <el-table-column label="下单时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.orderDate) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { dayjs } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'
import type { TextbookReservation } from '@/store/modules/type'

const reservationStore = useReservationStore()

// 搜索关键字
const searchStudent = ref('')
const searchMajor = ref('')
const searchTextbook = ref('')

// 拉取所有订单
onMounted(async () => {
  await reservationStore.fetchAllRec()
})

// 点击“搜索”按钮时，直接触发 computed 更新
function handleSearch() {
  // 这里只重置分页或其他逻辑，如果没有分页可留空
}

// 重置搜索条件并显示全部
function resetSearch() {
  searchStudent.value = ''
  searchMajor.value = ''
  searchTextbook.value = ''
}

// 过滤后的订单列表
const filteredReservations = computed<TextbookReservation[]>(() => {
  const studentKey = searchStudent.value.trim().toLowerCase()
  const majorKey = searchMajor.value.trim().toLowerCase()
  const textbookKey = searchTextbook.value.trim().toLowerCase()

  return reservationStore.reservations.filter((r) => {
    const nameMatch = !studentKey || r.studentName?.toLowerCase().includes(studentKey)
    const majorMatch = !majorKey || r.majorName?.toLowerCase().includes(majorKey)
    const textbookMatch = !textbookKey || r.textbookName?.toLowerCase().includes(textbookKey)
    return nameMatch && majorMatch && textbookMatch
  })
})

/**
 * 将后端返回的 ISO 时间字符串格式化为 "YYYY-MM-DD HH:mm:ss"
 */
function formatDate(val: string) {
  return val ? dayjs(val).format('YYYY-MM-DD HH:mm:ss') : ''
}
</script>

<style scoped>
.reservation-report-container {
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
  align-items: center;
  margin-bottom: 10px;
}
</style>
