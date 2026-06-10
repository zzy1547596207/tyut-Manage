<template>
  <div class="history-page">
    <div class="top-nav">
      <div class="back-btn" @click="goBack"><svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg></div>
      <span class="nav-title">信息采集计划审批记录</span>
      <div class="nav-spacer"></div>
    </div>

    <div v-if="records.length > 0" class="record-list">
      <div v-for="item in records" :key="item.batchId" class="record-card">
        <div class="card-header">
          <svg class="doc-icon" viewBox="0 0 24 24" fill="none" stroke="#e67e22" stroke-width="2" width="20" height="20"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
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
            <span class="status-tag reviewing">审核中</span>
          </template>
          <template v-else>
            <span class="status-tag">未提交</span>
          </template>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <p class="empty-text">暂无审批记录</p>
      <p class="empty-sub">提交采集任务后，审批记录会显示在此处</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const base = computed(() => route.path.startsWith('/college') ? '/college' : '/collection')
function goBack() { router.push(base.value + '/more') }

const records = ref([])

async function loadData() {
  try {
    const [batchRes, subRes] = await Promise.all([
      request.get('/batch/list'),
      request.get('/submission/my')
    ])
    const batches = batchRes.data || []
    const subs = subRes.data || []

    const batchMap = {}
    batches.forEach(b => { batchMap[b.id] = b })

    records.value = subs.map(s => ({
      batchId: s.batchId,
      name: (batchMap[s.batchId] && batchMap[s.batchId].name) || '未知批次',
      startTime: (batchMap[s.batchId] && batchMap[s.batchId].startTime) || '-',
      endTime: (batchMap[s.batchId] && batchMap[s.batchId].endTime) || '-',
      duration: (batchMap[s.batchId] && batchMap[s.batchId].duration) || '-',
      status: s.status
    }))
  } catch (e) { console.error('加载审批记录失败', e) }
}

onMounted(loadData)

function handleResubmit(item) {
  router.push(base.value + '/form/' + item.batchId)
}
</script>

<style scoped lang="scss">
.history-page { min-height: 100%; background: #f5f5f5; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; }
.top-nav .nav-title { font-size: 15px; font-weight: 600; }
.top-nav .nav-spacer { width: 20px; }
.top-nav .back-btn { cursor: pointer; display: flex; align-items: center; }
.record-list { padding: 12px; display: flex; flex-direction: column; gap: 12px; }
.record-card { background: #fff; border-radius: 12px; padding: 14px; }
.card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.card-header .batch-name { font-size: 15px; font-weight: 600; color: #333; }
.card-info { margin-bottom: 14px; }
.info-row { font-size: 12px; color: #666; margin-bottom: 4px; .label { color: #999; } .value { color: #333; } }
.card-footer { display: flex; justify-content: flex-end; }
.status-tag { font-size: 13px; color: #999; }
.status-tag.approved { color: #67c23a; font-weight: 600; }
.status-tag.reviewing { color: #e6a23c; font-weight: 600; }
.rejected-text { font-size: 13px; color: #f56c6c; }
.rejected-text .resubmit-link { color: #e74c3c; cursor: pointer; font-weight: 600; }
.empty-state { text-align: center; padding-top: 80px; }
.empty-text { font-size: 15px; color: #333; margin-bottom: 4px; }
.empty-sub { font-size: 12px; color: #999; }
</style>