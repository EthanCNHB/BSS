<template>
  <div>
    <div v-if="collegeStore.collegeInfo">
      <el-card>
        <template #header>
          <div class="header">欢迎来到{{ collegeStore.collegeInfo.collegeName }}后台</div>
        </template>
      </el-card>

      <el-card>
        <template #header>
          <div class="header">专业列表</div>
        </template>
        <el-table :data="formattedMajors" style="width: 100%">
          <el-table-column label="专业名称" prop="majorName" width="180" />
          <el-table-column label="描述" prop="majorDescription" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useCollegeStore } from '@/store/modules/college'
import { useMajorStore } from '@/store/modules/major'

const collegeStore = useCollegeStore()
const majorStore = useMajorStore()

// 格式化专业列表中的创建时间
const formattedMajors = computed(() => {
  return majorStore.majors.map((major) => {
    return {
      ...major,
    }
  })
})

// 获取当前学院信息
onMounted(async () => {
  const collegeInfo = collegeStore.collegeInfo
  if (collegeInfo) {
    // 根据当前学院的 collegeId 获取对应的专业列表
    await majorStore.fetchMajorsByCollege(collegeInfo.collegeId)
  }
})
</script>

<style scoped>
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
