<template>
  <div class="college-management-container">
    <!-- 学院列表 -->
    <el-card>
      <template #header>
        <div class="header">学院管理</div>
      </template>

      <!-- 新增按钮 -->
      <div class="add-college-button">
        <el-button type="primary" @click="openAddDialog">新增学院</el-button>
      </div>

      <!-- 学院表格 -->
      <el-table :data="collegeStore.colleges" border style="width: 100%; margin-top: 20px">
        <el-table-column label="学院ID" prop="collegeId" width="100" />
        <el-table-column label="学院名称" prop="collegeName" width="200" />
        <el-table-column label="创建时间" prop="creationTime" width="180" />
        <el-table-column label="学院描述" prop="collegeDescription" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEditCollege(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteCollege(row.collegeId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑学院弹窗 -->
    <el-dialog title="编辑学院" v-model="editDialogVisible">
      <el-form :model="form" label-width="100px">
        <el-form-item label="学院名称">
          <el-input v-model="form.collegeName"></el-input>
        </el-form-item>
        <el-form-item label="创建时间">
          <input v-model="form.creationTime" type="datetime-local" step="1" />
        </el-form-item>
        <el-form-item label="学院描述">
          <el-input v-model="form.collegeDescription"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新增学院弹窗 -->
    <el-dialog title="新增学院" v-model="addDialogVisible">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="学院名称">
          <el-input v-model="addForm.collegeName" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-input v-model="addForm.creationTime" />
        </el-form-item>
        <el-form-item label="学院描述">
          <el-input v-model="addForm.collegeDescription" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveNewCourse">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useCollegeStore } from '@/store/modules/college'
import { ElMessage, ElMessageBox } from 'element-plus'

const collegeStore = useCollegeStore()

// 当前编辑对象
const form = ref<any>({})

// 控制编辑弹窗显示
const editDialogVisible = ref(false)

// 控制新增学院弹窗显示
const addDialogVisible = ref(false)

// 新增学院表单对象
const addForm = ref<any>({
  collegeName: '',
  creationTime: '',
  collegeDescription: '',
})

// 初始化数据
onMounted(() => {
  collegeStore.fetchColleges()
})

// 打开编辑弹窗，并加载当前行的学院数据
const openEditCollege = (row: any) => {
  form.value = { ...row }
  editDialogVisible.value = true
}

// 编辑弹窗保存按钮
const saveEdit = () => {
  const index = collegeStore.colleges.findIndex((t) => t.collegeId === form.value.collegeId)
  if (index !== -1) {
    collegeStore.updateCollege(form.value)
    editDialogVisible.value = false
    collegeStore.fetchColleges() // 刷新学院列表
  } else {
    ElMessage.error('保存失败')
  }
}

// 删除学院
const deleteCollege = (collegeId: number) => {
  ElMessageBox.confirm('确定删除该学院吗？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        await collegeStore.deleteCollege(collegeId)
        collegeStore.fetchColleges() // 刷新学院列表
      } catch (error) {
        ElMessage.error('删除学院失败')
      }
    })
    .catch(() => {
      ElMessage.info('取消删除')
    })
}

// 打开新增课程保存按钮
const openAddDialog = () => {
  addForm.value = { collegeName: '', creationTime: '', collegeDescription: '' }
  addDialogVisible.value = true
}

// 新增课程保存按钮
const saveNewCourse = async () => {
  try {
    await collegeStore.addCollege(addForm.value)
    ElMessage.success('新增学院成功')
    addDialogVisible.value = false
    collegeStore.fetchColleges() // 更新课程列表
  } catch (err) {
    ElMessage.error('新增课程失败')
  }
}
</script>

<style scoped>
.college-management-container {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}

.header {
  font-size: 18px;
  font-weight: bold;
}

.add-college-button {
  margin-bottom: 20px;
}
</style>
