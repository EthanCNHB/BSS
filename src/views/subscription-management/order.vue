<template>
  <div class="reservation-container">
    <el-card>
      <template #header>
        <div class="header">我的订单</div>
      </template>
      <el-table :data="reservations" border stripe style="width: 100%">
        <el-table-column prop="reservationId" label="订单编号" width="100" />
        <el-table-column prop="textbookId" label="教材ID" width="100" />
        <el-table-column prop="textbookName" label="教材名称" />
        <el-table-column prop="textbookPrice" label="教材价格（元）" />
        <el-table-column prop="reservationQuantity" label="订购数量" />
        <el-table-column label="下单时间">
          <template #default="{ row }">
            {{ formatTime(row.orderDate) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="deleteReservation(row.reservationId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'
import { formatTime } from '@/utils/time'

const reservationStore = useReservationStore()

// 使用 computed 让它具备响应式
const reservations = computed(() => reservationStore.reservations)

onMounted(async () => {
  await reservationStore.fetchReservations()
})

const deleteReservation = async (id: number) => {
  try {
    await reservationStore.deleteReservation(id)
    ElMessage.success('删除成功')
  } catch (err) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.reservation-container {
  padding: 20px;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
