<template>
  <div class="add-container">
    <el-card>
      <template #header>
        <div class="header">添加教材</div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="教材名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入教材名称" />
        </el-form-item>

        <el-form-item label="教材编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入教材编码" />
        </el-form-item>

        <el-form-item label="出版社" prop="publisher">
          <el-input v-model="form.publisher" placeholder="请输入出版社名称" />
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者姓名" />
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model.number="form.price" type="number" placeholder="请输入教材价格" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="上架" value="上架" />
            <el-option label="下架" value="下架" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useTextbookStore } from '@/store/modules/textbook'

const textbookStore = useTextbookStore()

const form = ref({
  name: '',
  code: '',
  publisher: '',
  author: '',
  price: 0,
  status: '',
})

const formRef = ref()

const rules = {
  name: [{ required: true, message: '请输入教材名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入教材编码', trigger: 'blur' }],
  publisher: [{ required: true, message: '请输入出版社', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

const submitForm = async () => {
  await formRef.value.validate()
  try {
    await textbookStore.addTextbook(form.value)
    ElMessage.success('添加成功')
    resetForm()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const resetForm = () => {
  form.value = {
    name: '',
    code: '',
    publisher: '',
    author: '',
    price: 0,
    status: '',
  }
  formRef.value.resetFields()
}
</script>

<style scoped>
.add-container {
  max-width: 600px;
  margin: auto;
  padding: 20px;
}
.header {
  font-size: 18px;
  font-weight: bold;
}
</style>
