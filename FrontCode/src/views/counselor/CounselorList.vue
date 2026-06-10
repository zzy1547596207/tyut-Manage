<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>辅导员列表</span>
          <el-button v-if="isAdmin" type="primary" @click="handleAdd">新增辅导员</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="search-form">
        <el-form-item label="姓名"><el-input v-model="query.name" placeholder="搜索姓名" clearable /></el-form-item>
        <el-form-item label="工号"><el-input v-model="query.employeeNo" placeholder="搜索工号" clearable /></el-form-item>
        <el-form-item label="学历">
          <el-select v-model="query.education" placeholder="全部" clearable>
            <el-option label="博士" value="博士" />
            <el-option label="硕士" value="硕士" />
            <el-option label="本科" value="本科" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="employeeNo" label="工号" width="100" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="education" label="学历" width="80" />
        <el-table-column prop="title" label="职称" width="100" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="160" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button v-if="isAdmin" size="small" type="danger" @click="handleDelete(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.size"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @change="fetchData"
        style="margin-top:20px;justify-content:flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="工号"><el-input v-model="form.employeeNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="学历"><el-input v-model="form.education" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职称"><el-input v-model="form.title" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="isAdmin" type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../../stores/user'
import request from '../../utils/request'

const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'ADMIN')

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 10, name: '', employeeNo: '', education: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({})

async function fetchData() {
  loading.value = true
  try {
    const params = {}
    Object.keys(query).forEach(k => { if (query[k] !== '' && query[k] != null) params[k] = query[k] })
    const res = await request.get('/counselor/page', { params })
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.name = ''; query.employeeNo = ''; query.education = ''
  query.page = 1
  fetchData()
}

function handleAdd() {
  dialogTitle.value = '新增辅导员'
  Object.keys(form).forEach(k => delete form[k])
  form.gender = 1
  dialogVisible.value = true
}

function handleView(row) {
  dialogTitle.value = '查看辅导员'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleSave() {
  if (form.id) {
    await request.put(`/counselor/${form.id}`, form)
  } else {
    await request.post('/counselor', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  fetchData()
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await request.delete(`/counselor/${id}`)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-form { margin-bottom: 16px; }
</style>