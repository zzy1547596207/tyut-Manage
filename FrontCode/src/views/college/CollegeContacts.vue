<template>
  <div class="contacts-page">
    <div class="top-nav">
      <div class="back-btn" @click="$router.back()">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">通讯录</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="按姓名、工号、职务搜索" />
      <button class="search-btn" @click="doSearch">搜索</button>
    </div>

    <div v-if="filteredList.length > 0" class="contact-list">
      <div v-for="item in filteredList" :key="item.id" class="contact-card">
        <div class="contact-name">{{ item.name }}</div>
        <div class="contact-info">
          <div class="info-item"><span class="label">职务</span><span>{{ item.position || '-' }}</span></div>
          <div class="info-item"><span class="label">工号</span><span>{{ item.employeeNo || '-' }}</span></div>
          <div class="info-item"><span class="label">办公地点</span><span>{{ item.office || '-' }}</span></div>
          <div class="info-item"><span class="label">手机号</span><span class="phone">{{ item.phone || '-' }}</span></div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <p class="empty-text">暂无通讯录数据</p>
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
  } catch (e) { console.error('加载通讯录失败', e) }
}

onMounted(loadData)

const filteredList = computed(() => {
  if (!keyword.value.trim()) return counselors.value
  const kw = keyword.value.trim()
  return counselors.value.filter(c =>
    (c.name && c.name.includes(kw)) ||
    (c.employeeNo && c.employeeNo.includes(kw)) ||
    (c.position && c.position.includes(kw)) ||
    (c.office && c.office.includes(kw))
  )
})

function doSearch() {}
</script>

<style scoped lang="scss">
.contacts-page { background: #f5f5f5; min-height: 100%; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; } }
.search-bar { display: flex; gap: 8px; padding: 10px 14px; background: #fff; border-top: 1px solid #f0f0f0; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; }
.search-input:focus { border-color: #e74c3c; }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.contact-list { padding: 12px; display: flex; flex-direction: column; gap: 10px; }
.contact-card { background: #fff; border-radius: 12px; padding: 14px; }
.contact-name { font-size: 15px; font-weight: 600; color: #333; margin-bottom: 8px; }
.contact-info { display: grid; grid-template-columns: 1fr 1fr; gap: 6px; }
.info-item { font-size: 12px; .label { color: #999; margin-right: 4px; } .phone { color: #409eff; } }
.empty-state { text-align: center; padding-top: 80px; }
.empty-text { font-size: 14px; color: #999; }
</style>