<template>
  <div class="batch-page">
    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="请输入批次名称关键词搜索" @keyup.enter="handleSearch" />
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <div v-if="isCollege" class="publish-bar">
      <button class="publish-btn" @click="router.push('/college/batch/publish')">+ 发布采集批次</button>
    </div>

    <div v-if="filteredList.length > 0" class="batch-list">
      <div v-for="item in filteredList" :key="item.id" class="batch-card" @click="handleCardClick(item)">
        <div class="card-header">
          <svg class="doc-icon" viewBox="0 0 24 24" fill="none" stroke="#e74c3c" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/>
          </svg>
          <span class="batch-name">{{ item.name }}</span>
        </div>
        <div class="card-info">
          <div class="info-row"><span class="label">开始时间：</span><span class="value">{{ item.startTime }}</span></div>
          <div class="info-row"><span class="label">结束时间：</span><span class="value">{{ item.endTime }}</span></div>
          <div class="info-row"><span class="label">批次时长：</span><span class="value">{{ item.duration }}</span></div>
        </div>
        <div class="card-footer">
          <template v-if="item.status === 'reviewing'">
            <span class="status-tag">审核中</span><span class="click-hint">&gt;</span>
          </template>
          <template v-else-if="item.status === 'approved'">
            <span class="status-tag approved">审核通过</span>
          </template>
          <template v-else-if="item.status === 'rejected'">
            <span class="rejected-text">审核不通过，<a class="resubmit-link" @click.stop="handleStart(item)">重新提交 &gt;&gt;</a></span>
          </template>
          <template v-else>
            <button class="fill-btn" @click.stop="handleStart(item)">开始填写</button>
          </template>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="illustration">
        <svg viewBox="0 0 200 180" xmlns="http://www.w3.org/2000/svg">
          <rect x="45" y="80" width="110" height="75" rx="4" fill="#D4A574" stroke="#B8860B" stroke-width="2"/>
          <polygon points="45,80 60,55 100,75 100,80" fill="#DEB887" stroke="#B8860B" stroke-width="2"/>
          <polygon points="155,80 140,55 100,75 100,80" fill="#C4A06A" stroke="#B8860B" stroke-width="2"/>
          <circle cx="100" cy="62" r="18" fill="#FFD5B8"/><circle cx="94" cy="60" r="2.5" fill="#333"/><circle cx="106" cy="60" r="2.5" fill="#333"/>
          <ellipse cx="100" cy="69" rx="3.5" ry="2.5" fill="#E88"/><ellipse cx="100" cy="47" rx="20" ry="8" fill="#333"/>
          <g transform="translate(72,72)"><line x1="0" y1="0" x2="-22" y2="15" stroke="#FFD5B8" stroke-width="6" stroke-linecap="round"/><circle cx="-24" cy="17" r="5" fill="#FFD5B8"/></g>
          <g transform="translate(128,72)"><line x1="0" y1="0" x2="22" y2="15" stroke="#FFD5B8" stroke-width="6" stroke-linecap="round"/><circle cx="24" cy="17" r="5" fill="#FFD5B8"/></g>
          <rect x="86" y="74" width="28" height="12" rx="6" fill="#5B9BD5"/><circle cx="145" cy="35" r="18" fill="#fff" stroke="#ddd" stroke-width="1.5"/>
          <text x="145" y="41" text-anchor="middle" font-size="22" font-weight="bold" fill="#999">?</text>
        </svg>
      </div>
      <p class="empty-text">暂无进行中批次</p>
      <p class="empty-sub">当前没有需要填报的信息采集任务</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onActivated } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const isCollege = computed(() => route.path.startsWith('/college'))
const keyword = ref('')

const batchList = ref([])
const submissionMap = ref({})

async function loadData() {
  try {
    const [batchRes, subRes] = await Promise.all([
      request.get('/batch/list'),
      request.get('/submission/my')
    ])
    batchList.value = batchRes.data || []
    const map = {}
    if (subRes.data) {
      subRes.data.forEach(s => { map[s.batchId] = s })
    }
    submissionMap.value = map
  } catch (e) {
    console.error('加载数据失败', e)
  }
}

onMounted(loadData)
onActivated(loadData)

const filteredList = computed(() => {
  let result = batchList.value.map(item => {
    const sub = submissionMap.value[item.id]
    return { ...item, status: sub ? sub.status : null, submissionId: sub ? sub.id : null }
  })
  if (keyword.value.trim()) {
    result = result.filter(item => item.name.includes(keyword.value.trim()))
  }
  return result
})

function handleSearch() {}

function handleStart(item) {
  const prefix = isCollege.value ? '/college' : '/collection'
  router.push(prefix + '/form/' + item.id)
}

function handleCardClick(item) {
  if (item.status === 'reviewing') {
    const prefix = isCollege.value ? '/college' : '/collection'
    router.push(prefix + '/detail/' + item.batchId)
  }
}
</script>

<style scoped lang="scss">
.batch-page { padding: 12px; }
.search-bar { display: flex; gap: 8px; margin-bottom: 14px; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; background: #fff; }
.search-input:focus { border-color: #e74c3c; }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.publish-bar { margin-bottom: 14px; }
.publish-btn { width: 100%; height: 40px; border: 1.5px dashed #e74c3c; border-radius: 8px; background: #fff; color: #e74c3c; font-size: 14px; font-weight: 500; cursor: pointer; }
.batch-list { display: flex; flex-direction: column; gap: 12px; }
.batch-card { background: #fff; border-radius: 12px; padding: 14px; cursor: pointer; }
.card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.card-header .doc-icon { width: 20px; height: 20px; flex-shrink: 0; }
.card-header .batch-name { font-size: 15px; font-weight: 600; color: #333; }
.card-info { margin-bottom: 14px; }
.info-row { font-size: 12px; color: #666; margin-bottom: 4px; }
.info-row .label { color: #999; }
.info-row .value { color: #333; }
.card-footer { display: flex; justify-content: flex-end; align-items: center; gap: 6px; }
.fill-btn { padding: 6px 20px; border: 1.5px solid #e74c3c; border-radius: 6px; background: #fff; color: #e74c3c; font-size: 13px; font-weight: 500; cursor: pointer; }
.status-tag { font-size: 13px; font-weight: 600; color: #e6a23c; }
.status-tag.approved { color: #67c23a; }
.click-hint { color: #ccc; font-size: 14px; }
.rejected-text { font-size: 13px; color: #f56c6c; }
.rejected-text .resubmit-link { color: #e74c3c; cursor: pointer; font-weight: 600; text-decoration: none; }
.empty-state { text-align: center; padding-top: 40px; }
.illustration { width: 180px; margin: 0 auto 12px; }
.illustration svg { width: 100%; height: auto; }
.empty-text { font-size: 15px; color: #333; margin-bottom: 4px; font-weight: 500; }
.empty-sub { font-size: 12px; color: #999; }
</style>