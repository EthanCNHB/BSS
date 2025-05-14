<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="header">我的教材征订记录</div>
      </template>

      <el-table :data="reservationStore.reservations" border stripe style="margin-top: 20px">
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column label="征订编号" prop="reservationId" />
        <el-table-column label="教材ID" prop="textbookId" />
        <el-table-column label="用户ID" prop="userId" />
        <el-table-column label="征订时间" prop="reservationTime" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="deleteReservation(row.reservationId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useReservationStore } from '@/store/modules/textbookReservation'

// 获取征订仓库实例
const reservationStore = useReservationStore()

// 页面加载时获取当前用户的征订记录
onMounted(() => {
  reservationStore.fetchReservations()
})

// 删除征订记录
const deleteReservation = async (id: number) => {
  try {
    await reservationStore.deleteReservation(id)
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.order-container {
  padding: 20px;
  max-width: 1000px;
  margin: auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
