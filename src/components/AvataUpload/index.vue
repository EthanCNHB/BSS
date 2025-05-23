<template>
  <div class="flex items-center">
    <el-avatar :src="props.src" size="large" />
    <el-upload class="ml-4" :action="props.uploadUrl" :show-file-list="false" :before-upload="beforeUpload" :on-success="handleSuccess">
      <el-button size="small">上传头像</el-button>
    </el-upload>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'

// 接收头像地址和上传接口
const props = defineProps<{ src: string; uploadUrl: string }>()
// 定义更新事件
const emit = defineEmits(['update'])

// 上传前校验
function beforeUpload(file: File) {
  const isImage = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片')
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB')
  }
  return isImage && isLt2M
}

// 上传成功回调
function handleSuccess() {
  ElMessage.success('头像更新成功')
  emit('update')
}
</script>

<style scoped>
.ml-4 {
  margin-left: 1rem;
}
</style>
