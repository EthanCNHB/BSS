<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="header">用户管理</div>
      </template>

      <el-table :data="userList" border stripe style="width: 100%">
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="role" label="角色" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="editUser(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteUser(row.userId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import axios from 'axios'

const userList = ref([])

const fetchUsers = async () => {
  try {
    const res = await axios.get('http://localhost:8080/admin/users')
    userList.value = res.data.data
  } catch (err) {
    ElMessage.error('获取用户列表失败')
  }
}

const deleteUser = async (userId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await axios.delete(`http://localhost:8080/admin/users/${userId}`)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (err) {
    ElMessage.info('取消删除')
  }
}

const editUser = (user: any) => {
  // 你可以跳转到编辑页面或打开弹窗修改用户信息
  console.log('编辑用户:', user)
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
