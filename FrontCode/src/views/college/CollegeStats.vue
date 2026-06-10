<template>
  <div class="stats-page">
    <div class="top-nav">
      <div class="back-btn" @click="$router.push('/college/more')">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">数据统计</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="按姓名、工号、部门搜索" />
      <button class="search-btn" @click="doSearch">搜索</button>
    </div>

    <div v-if="filteredList.length > 0" class="counselor-list">
      <div v-for="item in filteredList" :key="item.id" class="counselor-card">
        <div class="card-top">
          <div class="avatar">{{ (item.name || '?')[0] }}</div>
          <div class="name-info">
            <span class="name">{{ item.name }}</span>
            <span class="dept">{{ item.department }}</span>
          </div>
        </div>
        <div class="card-info">
          <div class="info-item"><span class="label">工号</span><span>{{ item.employeeNo || '-' }}</span></div>
          <div class="info-item"><span class="label">性别</span><span>{{ item.gender || '-' }}</span></div>
          <div class="info-item"><span class="label">类型</span><span>{{ item.type || '-' }}</span></div>
          <div class="info-item"><span class="label">职务</span><span>{{ item.position || '-' }}</span></div>
          <div class="info-item"><span class="label">学历</span><span>{{ item.education || '-' }}</span></div>
          <div class="info-item"><span class="label">电话</span><span>{{ item.phone || '-' }}</span></div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <p class="empty-text">暂无辅导员信息</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '@/utils/request'

const keyword = ref('')
const counselors = ref([])

async function loadData() {
  try {
    const res = await request.get('/counselors/list')
    counselors.value = res.data || []
  } catch (e) { console.error('加载数据失败', e) }
}

onMounted(loadData)

const filteredList = computed(() => {
  if (!keyword.value.trim()) return counselors.value
  const kw = keyword.value.trim()
  return counselors.value.filter(c =>
    (c.name && c.name.includes(kw)) ||
    (c.employeeNo && c.employeeNo.includes(kw)) ||
    (c.department && c.department.includes(kw))
  )
})

function doSearch() {}
</script>

<style scoped lang="scss">
.stats-page { background: #f5f5f5; min-height: 100%; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; } }
.search-bar { display: flex; gap: 8px; padding: 10px 14px; background: #fff; border-top: 1px solid #f0f0f0; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; }
.search-input:focus { border-color: #e74c3c; }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.counselor-list { padding: 12px; display: flex; flex-direction: column; gap: 12px; }
.counselor-card { background: #fff; border-radius: 12px; padding: 14px; }
.card-top { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.avatar { width: 40px; height: 40px; border-radius: 50%; background: #e74c3c; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: 600; }
.name-info { display: flex; flex-direction: column; .name { font-size: 15px; font-weight: 600; color: #333; } .dept { font-size: 11px; color: #999; } }
.card-info { display: grid; grid-template-columns: 1fr 1fr; gap: 6px; }
.info-item { font-size: 12px; .label { color: #999; margin-right: 4px; } }
.empty-state { text-align: center; padding-top: 80px; }
.empty-text { font-size: 14px; color: #999; }
</style>