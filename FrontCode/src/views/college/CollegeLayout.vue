<template>
  <div class="college-app">
    <div class="top-bar"><span class="nav-title">{{ pageTitle }}</span></div>
    <div class="main-content"><router-view /></div>
    <div class="tab-bar">
      <div v-if="!isAdmin" class="tab-item" :class="{ active: activeTab === 'review' }" @click="switchTab('review')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>
        <span>信息审核</span>
      </div>
      <div v-if="isAdmin" class="tab-item" :class="{ active: activeTab === 'adminStats' }" @click="switchTab('admin-stats')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2"/><line x1="7" y1="15" x2="7" y2="10"/><line x1="12" y1="15" x2="12" y2="7"/><line x1="17" y1="15" x2="17" y2="12"/></svg>
        <span>数据统计</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'contacts' }" @click="switchTab('contacts')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
        <span>通讯录</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'batch' }" @click="switchTab('batch')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>
        <span>采集批次</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'more' }" @click="switchTab('more')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>
        <span>更多功能</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../../stores/user'
const router = useRouter(); const route = useRoute(); const userStore = useUserStore(); const isAdmin = computed(() => userStore.role === 'ADMIN')

const titleMap = { review: '信息审核', contacts: '通讯录', batch: '采集批次', more: '更多功能', adminStats: '数据统计' }

const activeTab = computed(() => {
  const p = route.path
  if (p.includes('/college/review')) return 'review'
  if (p.includes('/college/admin-stats')) return 'adminStats'
  if (p.includes('/college/contacts')) return 'contacts'
  if (p.includes('/college/more') || p.includes('/college/profile') || p.includes('/college/approval') || p.includes('/college/stats')) return 'more'
  if (p.includes('/college/batch') || p.includes('/college/form') || p.includes('/college/detail') || p.includes('/college/work') || p.includes('/college/study')) return 'batch'
  return isAdmin.value ? 'adminStats' : 'review'
})

const pageTitle = computed(() => titleMap[activeTab.value] || '信息审核')

function switchTab(tab) { router.push('/college/' + tab) }
</script>

<style>
.college-app {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  display: flex !important;
  flex-direction: column !important;
  background: #f5f5f5;
  z-index: 1;
}
.college-app .top-bar {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px 20px 8px;
  background: #fff;
  z-index: 2;
}
.college-app .top-bar .nav-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}
.college-app .main-content {
  flex: 1 1 0%;
  min-height: 0;
  overflow-y: auto;
}
.college-app .tab-bar {
  flex-shrink: 0;
  display: flex !important;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  padding: 8px 0 6px;
  z-index: 99;
}
.college-app .tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  padding: 4px 0;
  cursor: pointer;
  color: #999;
  font-size: 11px;
}
.college-app .tab-item .tab-icon {
  width: 22px;
  height: 22px;
}
.college-app .tab-item.active {
  color: #e74c3c;
}
</style>