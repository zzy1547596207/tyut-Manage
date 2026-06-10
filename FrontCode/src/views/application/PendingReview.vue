<template>
  <div>
    <el-card>
      <template #header><span>待审核申请</span></template>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="counselorName" label="申请人" width="100" />
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="content" label="内容" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }"><el-tag type="warning">待审核</el-tag></template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="handleAudit(row, 'APPROVE')">通过</el-button>
            <el-button size="small" type="danger" @click="handleAudit(row, 'REJECT')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialogVisible" title="审核意见" width="400px">
        <el-input v-model="comment" type="textarea" :rows="3" placeholder="请输入审核意见" />
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAudit">确认</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const comment = ref('')
let currentApp = null
let currentAction = ''

async function fetchData() {
  loading.value = true
  try {
    const res = await request.get('/application/pending')
    tableData.value = res.data || []
  } finally { loading.value = false }
}

function handleAudit(row, action) {
  currentApp = row
  currentAction = action
  comment.value = ''
  dialogVisible.value = true
}

async function confirmAudit() {
  await request.put(`/application/audit/${currentApp.id}`, {
    action: currentAction,
    comment: comment.value
  })
  ElMessage.success('审核完成')
  dialogVisible.value = false
  fetchData()
}

onMounted(fetchData)
</script>