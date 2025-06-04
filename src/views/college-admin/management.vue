<!-- src/views/admin/ManageCollegeAdmins.vue -->
<template>
  <div class="college-admin-management-container">
    <el-card>
      <template #header>
        <div class="header">学院管理员管理</div>
      </template>

      <!-- 新增按钮 -->
      <div class="add-admin-button">
        <el-button type="primary" @click="openAddDialog">新增管理员</el-button>
      </div>

      <!-- 管理员表格 -->
      <el-table :data="adminStore.collegeAdminList" border style="width: 100%; margin-top: 20px">
        <el-table-column label="管理员ID" prop="userId" width="100" />
        <el-table-column label="用户名" prop="username" width="180" />
        <el-table-column label="所属学院" width="200">
          <template #default="{ row }">
            {{ collegeMap[row.collegeId] || '—' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteAdmin(row.userId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑管理员弹窗 -->
    <el-dialog title="编辑管理员" v-model="editDialogVisible" width="480px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px">
        <!-- 学院下拉，value 为 collegeId -->
        <el-form-item label="所属学院" prop="collegeId">
          <el-select v-model="editForm.collegeId" placeholder="请选择学院">
            <el-option v-for="c in colleges" :key="c.collegeId" :label="c.collegeName" :value="c.collegeId" />
          </el-select>
        </el-form-item>
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <!-- 新增管理员弹窗 -->
    <el-dialog title="新增管理员" v-model="addDialogVisible" width="480px">
      <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="100px">
        <!-- 学院下拉 -->
        <el-form-item label="所属学院" prop="collegeId">
          <el-select v-model="addForm.collegeId" placeholder="请选择学院">
            <el-option v-for="c in colleges" :key="c.collegeId" :label="c.collegeName" :value="c.collegeId" />
          </el-select>
        </el-form-item>
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" placeholder="5-16位，不含空格" />
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" type="password" placeholder="5-16位，不含空格" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveNewAdmin">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCollegeStore } from '@/store/modules/college'
import { useCollegeAdminStore } from '@/store/modules/collegeAdmin'
import type { College, CollegeAdmin } from '@/store/modules/type'

// Store 实例
const collegeStore = useCollegeStore()
const adminStore = useCollegeAdminStore()

// 弹窗显隐
const editDialogVisible = ref(false)
const addDialogVisible = ref(false)

// 学院列表 & 映射
const colleges = ref<College[]>([])
const collegeMap = computed<Record<number, string>>(() =>
  colleges.value.reduce((m, c) => {
    m[c.collegeId] = c.collegeName
    return m
  }, {} as Record<number, string>),
)

// 编辑表单引用与模型
const editFormRef = ref()
const editForm = ref<Partial<CollegeAdmin>>({
  userId: undefined,
  collegeId: undefined,
  username: '',
})

// 新增表单引用与模型
const addFormRef = ref()
const addForm = ref<{ collegeId: number | null; username: string; password: string }>({
  collegeId: null,
  username: '',
  password: '',
})

// 校验规则
const baseRule = { required: true, message: '此项不能为空', trigger: 'blur' }
const nameRule = { pattern: /^\S{5,16}$/, message: '5-16位，不含空格', trigger: 'blur' }

const editRules = {
  collegeId: [baseRule],
  username: [baseRule, nameRule],
}
const addRules = {
  collegeId: [baseRule],
  username: [baseRule, nameRule],
  password: [baseRule, nameRule],
}

// 初始化：拉取学院 & 管理员列表
onMounted(async () => {
  await collegeStore.fetchColleges()
  colleges.value = collegeStore.colleges
  await adminStore.fetchAllAdmins()
})

// 打开编辑弹窗，填充表单
function openEditDialog(row: CollegeAdmin) {
  editForm.value = {
    userId: row.userId,
    collegeId: row.collegeId,
    username: row.username,
  }
  editDialogVisible.value = true
}

// 保存编辑：直接提交 collegeId、username、userId
async function saveEdit() {
  editFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    try {
      await adminStore.updateAdmin(editForm.value as CollegeAdmin)
      ElMessage.success('保存成功')
      editDialogVisible.value = false
      await adminStore.fetchAllAdmins()
    } catch {
      // 错误已在 store 中提示
    }
  })
}

// 打开新增弹窗
function openAddDialog() {
  addForm.value = { collegeId: null, username: '', password: '' }
  addDialogVisible.value = true
}

// 保存新增：提交 collegeId、username、password
async function saveNewAdmin() {
  addFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    try {
      await adminStore.register({
        collegeId: addForm.value.collegeId!,
        username: addForm.value.username,
        password: addForm.value.password,
      })
      ElMessage.success('新增成功')
      addDialogVisible.value = false
      await adminStore.fetchAllAdmins()
    } catch {
      // 错误已在 store 中提示
    }
  })
}

// 删除管理员
function deleteAdmin(userId: number) {
  ElMessageBox.confirm('确定删除该管理员吗？', '警告', {
    confirmButtonText: '删除',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      await adminStore.deleteAdmin(userId)
      ElMessage.success('删除成功')
      await adminStore.fetchAllAdmins()
    })
    .catch(() => {
      ElMessage.info('已取消')
    })
}
</script>

<style scoped>
.college-admin-management-container {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
.add-admin-button {
  margin-bottom: 20px;
}
</style>
