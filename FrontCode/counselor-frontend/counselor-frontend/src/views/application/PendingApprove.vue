<template>
  <div>
    <el-card>
      <template #header><span>待审批申请（学院已审核）</span></template>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="counselorName" label="申请人" width="100" />
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="content" label="内容" min-width="180" />
        <el-table-column prop="reviewerName" label="审核人" width="100" />
        <el-table-column prop="reviewComment" label="审核意见" min-width="160" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="handleApprove(row, 'APPROVE')">通过</el-button>
            <el-button size="small" type="danger" @click="handleApprove(row, 'REJECT')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialogVisible" title="审批意见" width="400px">
        <el-input v-model="comment" type="textarea" :rows="3" placeholder="请输入审批意见" />
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmApprove">确认</el-button>
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

function handleApprove(row, action) {
  currentApp = row
  currentAction = action
  comment.value = ''
  dialogVisible.value = true
}

async function confirmApprove() {
  await request.put(`/application/approve/${currentApp.id}`, {
    action: currentAction,
    comment: comment.value
  })
  ElMessage.success('审批完成')
  dialogVisible.value = false
  fetchData()
}

onMounted(fetchData)
</script>