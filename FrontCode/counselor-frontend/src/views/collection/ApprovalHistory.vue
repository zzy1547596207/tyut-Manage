<template>
  <div class="approval-page">
    <div class="top-nav">
      <div class="back-btn" @click="goBack">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">信息采集计划审批记录</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="list-body">
      <div v-if="records.length === 0" class="empty-hint">暂无审批记录</div>

      <div v-for="item in records" :key="item.id" class="batch-card">
        <div class="card-header">
          <svg class="doc-icon" viewBox="0 0 24 24" fill="none" stroke="#e74c3c" stroke-width="2" width="18" height="18">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/>
          </svg>
          <span class="batch-name">{{ item.name }}</span>
        </div>
        <div class="card-info">
          <div class="info-row"><span class="label">开始时间：</span><span class="value">{{ item.startTime }}</span></div>
          <div class="info-row"><span class="label">结束时间：</span><span class="value">{{ item.endTime }}</span></div>
          <div class="info-row"><span class="label">批次时长：</span><span class="value">{{ item.duration }}</span></div>
        </div>
        <div class="card-footer">
          <template v-if="item.status === 'approved'">
            <span class="status-tag approved">审核通过</span>
          </template>
          <template v-else-if="item.status === 'rejected'">
            <span class="rejected-text">审核不通过，<a class="resubmit-link" @click="handleResubmit(item)">重新提交 &gt;&gt;</a></span>
          </template>
          <template v-else-if="item.status === 'reviewing'">
            <span class="status-tag">审核中</span>
          </template>
          <template v-else>
            <span class="status-tag dim">未提交</span>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const base = computed(() => route.path.startsWith('/college') ? '/college' : '/collection')

function goBack() { router.push(base.value + '/more') }

const records = ref([])

onMounted(() => {
  let batches = []
  try {
    const raw = localStorage.getItem('batch_list')
    batches = raw ? JSON.parse(raw) : [{ id: 1, name: '2024 年辅导员信息采集', startTime: '2024-09-01', endTime: '2024-09-30', duration: '30 天' }]
  } catch (e) {
    batches = [{ id: 1, name: '2024 年辅导员信息采集', startTime: '2024-09-01', endTime: '2024-09-30', duration: '30 天' }]
  }
  try {
    const statusMap = JSON.parse(localStorage.getItem('batch_status') || '{}')
    records.value = batches.map(b => ({
      ...b,
      status: statusMap[b.id] || null
    }))
  } catch (e) {
    records.value = batches
  }
})

function handleResubmit(item) {
  router.push(base.value + '/form/' + item.id)
}
</script>

<style scoped lang="scss">
.approval-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 14px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.list-body { flex: 1; overflow-y: auto; padding: 12px; }
.batch-card { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }
.card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; .batch-name { font-size: 15px; font-weight: 600; color: #333; } }
.card-info { margin-bottom: 14px; }
.info-row { font-size: 12px; color: #666; margin-bottom: 4px; .label { color: #999; } .value { color: #333; } }
.card-footer { display: flex; justify-content: flex-end; }
.status-tag { font-size: 13px; font-weight: 600; color: #e6a23c; &.approved { color: #67c23a; } &.dim { color: #ccc; } }
.rejected-text { font-size: 13px; color: #f56c6c; .resubmit-link { color: #e74c3c; cursor: pointer; font-weight: 600; text-decoration: none; } }
.empty-hint { text-align: center; color: #ccc; font-size: 14px; padding: 60px 0; }
</style>