<template>
  <div class="edit-container">
    <el-card>
      <template #header>
        <div class="header">教材管理</div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input v-model="searchName" placeholder="请输入教材名称" clearable />
        <el-button type="primary" @click="searchTextbooks">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <!-- 教材列表 -->
      <el-table :data="filteredTextbooks" border style="margin-top: 20px">
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column label="教材名称" prop="name" width="180" />
        <el-table-column label="编码" prop="code" width="150" />
        <el-table-column label="出版社" prop="publisher" width="180" />
        <el-table-column label="作者" prop="author" width="150" />
        <el-table-column label="价格" prop="price" width="100" />
        <el-table-column label="状态" prop="status" width="120" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteTextbook(row.textbookId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog title="编辑教材" v-model="editDialogVisible">
      <el-form :model="form" label-width="100px">
        <el-form-item label="教材名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="教材编码">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="form.publisher" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model.number="form.price" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="上架" value="上架" />
            <el-option label="下架" value="下架" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useTextbookStore } from '@/store/modules/textbook'

// 获取教材仓库
const textbookStore = useTextbookStore()

// 搜索关键词
const searchName = ref('')

// 控制编辑弹窗显示
const editDialogVisible = ref(false)

// 当前编辑的教材对象
const form = ref<any>({})

// 页面加载时获取教材列表
onMounted(() => {
  textbookStore.fetchTextbooks()
})

// 根据搜索关键词过滤教材列表
const filteredTextbooks = computed(() => {
  if (!searchName.value) return textbookStore.textbooks
  return textbookStore.textbooks.filter((t) => t.name.includes(searchName.value))
})

// 搜索按钮操作（实时过滤无需逻辑）
const searchTextbooks = () => {
  // 无逻辑，仅触发响应式更新
}

// 重置搜索框
const resetSearch = () => {
  searchName.value = ''
}

// 打开编辑弹窗，并加载当前行教材数据
const openEditDialog = (row: any) => {
  form.value = { ...row }
  editDialogVisible.value = true
}

// 保存编辑内容到 store 中
const saveEdit = () => {
  // 查找被编辑教材的索引
  const index = textbookStore.textbooks.findIndex((t) => t.textbookId === form.value.textbookId)
  if (index !== -1) {
    // 更新教材内容
    textbookStore.textbooks[index] = { ...form.value }
    ElMessage.success('保存成功')
    editDialogVisible.value = false
  } else {
    ElMessage.error('保存失败：教材未找到')
  }
}

// 删除指定教材（重要操作：需确认后执行）
const deleteTextbook = async (id: number) => {
  try {
    await textbookStore.deleteTextbook(id)
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.edit-container {
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
  margin-bottom: 10px;
}
</style>
