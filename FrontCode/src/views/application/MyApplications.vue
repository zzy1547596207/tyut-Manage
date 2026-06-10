<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的申请</span>
          <el-button type="primary" @click="dialogVisible = true">提交申请</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="type" label="申请类型" width="120" />
        <el-table-column prop="content" label="申请内容" min-width="200" />
        <el-table-column prop="status" label="状态" width="130">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'PENDING'" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 'DEPARTMENT_APPROVED'" type="primary">学院已审核</el-tag>
            <el-tag v-else-if="row.status === 'ADMIN_APPROVED'" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 'REJECTED'" type="danger">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
      </el-table>

      <el-dialog v-model="dialogVisible" title="提交申请" width="500px">
        <el-form :model="form" label-width="80px">
          <el-form-item label="申请类型">
            <el-select v-model="form.type" style="width:100%">
              <el-option label="信息变更" value="INFO_CHANGE" />
              <el-option label="请假申请" value="LEAVE" />
              <el-option label="调动申请" value="TRANSFER" />
            </el-select>
          </el-form-item>
          <el-form-item label="申请内容">
            <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请描述申请内容" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ type: 'INFO_CHANGE', content: '' })

async function fetchData() {
  const res = await request.get('/application/my')
  tableData.value = res.data || []
}

async function handleSubmit() {
  if (!form.content.trim()) {
    ElMessage.warning('请输入申请内容')
    return
  }
  await request.post('/application/submit', form)
  ElMessage.success('提交成功')
  dialogVisible.value = false
  form.content = ''
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>