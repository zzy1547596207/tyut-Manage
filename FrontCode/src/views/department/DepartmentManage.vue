<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>院系管理</span>
          <el-button type="primary" @click="handleAdd">新增院系</el-button>
        </div>
      </template>

      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="code" label="院系编号" width="100" />
        <el-table-column prop="name" label="院系名称" width="150" />
        <el-table-column prop="manager" label="负责人" width="100" />
        <el-table-column prop="description" label="描述" min-width="180" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
        <el-form :model="form" label-width="80px">
          <el-form-item label="院系编号"><el-input v-model="form.code" /></el-form-item>
          <el-form-item label="院系名称"><el-input v-model="form.name" /></el-form-item>
          <el-form-item label="负责人"><el-input v-model="form.manager" /></el-form-item>
          <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({})

async function fetchData() {
  loading.value = true
  try {
    const res = await request.get('/department')
    tableData.value = res.data || []
  } finally { loading.value = false }
}

function handleAdd() {
  dialogTitle.value = '新增院系'
  Object.keys(form).forEach(k => delete form[k])
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑院系'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleSave() {
  if (form.id) {
    await request.put(`/department/${form.id}`, form)
  } else {
    await request.post('/department', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  fetchData()
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request.delete(`/department/${id}`)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>