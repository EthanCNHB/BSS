<!-- src/views/report/ReservationReport.vue -->
<template>
  <div class="reservation-report-container">
    <el-card>
      <template #header>
        <div class="header">征订报告</div>
      </template>

      <el-table :data="reservationStore.reservations" border style="width: 100%; margin-top: 20px">
        <!-- 序号 -->
        <el-table-column type="index" label="序号" />
        <!-- 订单ID -->
        <el-table-column prop="reservationId" label="订单ID" />
        <!-- 教材名称 -->
        <el-table-column prop="textbookName" label="教材名称" />
        <!-- 订购数量 -->
        <el-table-column prop="reservationQuantity" label="订购数量" />
        <!-- 教材单价 -->
        <el-table-column prop="textbookPrice" label="教材单价">
          <template #default="{ row }">¥{{ row.textbookPrice?.toFixed(2) }}</template>
        </el-table-column>
        <!-- 订购日期 -->
        <el-table-column label="订购日期" width="180">
          <template #default="{ row }">
            {{ formatDate(row.orderDate) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useReservationStore } from '@/store/modules/textbookReservation'
import { dayjs } from 'element-plus'
import { useCollegeStore } from '@/store/modules/college'
import { useMajorStore } from '@/store/modules/major'
import { useStudentStore } from '@/store/modules/student'

// 实例化各个 Store
const reservationStore = useReservationStore()
const collegeStore = useCollegeStore()
const majorStore = useMajorStore()
const studentStore = useStudentStore()

// 页面加载后拉取所有预订记录
onMounted(() => {
  reservationStore.fetchAllRec()
})

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
</style>
