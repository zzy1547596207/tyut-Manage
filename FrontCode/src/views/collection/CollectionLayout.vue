<template>
  <div class="collection-app">
    <div class="top-bar"><span class="nav-title">信息采集批次</span></div>
    <div class="main-content"><router-view /></div>
    <div class="tab-bar">
      <div class="tab-item" :class="{ active: activeTab === 'batch' }" @click="switchTab('batch')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" />
          <rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" />
        </svg>
        <span>采集批次</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'profile' }" @click="switchTab('profile')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="8" r="4" /><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
        </svg>
        <span>个人信息</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'more' }" @click="switchTab('more')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" />
          <rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" />
        </svg>
        <span>更多功能</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const router = useRouter(); const route = useRoute()

const activeTab = computed(() => {
  const path = route.path
  if (path.includes('/collection/approval/')) return 'more'
  if (path.includes('/collection/batch')) return 'batch'
  if (path.includes('/collection/profile')) return 'profile'
  if (path.includes('/collection/more')) return 'more'
  return 'batch'
})

function switchTab(tab) { router.push('/collection/' + tab) }
</script>

<style>
.collection-app {
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
.collection-app .top-bar {
  flex-shrink: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px 20px 8px;
  background: #fff;
  z-index: 2;
}
.collection-app .top-bar .nav-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}
.collection-app .main-content {
  flex: 1 1 0%;
  min-height: 0;
  overflow-y: auto;
}
.collection-app .tab-bar {
  flex-shrink: 0;
  display: flex !important;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  padding: 8px 0 6px;
  z-index: 99;
}
.collection-app .tab-item {
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
.collection-app .tab-item .tab-icon {
  width: 22px;
  height: 22px;
}
.collection-app .tab-item.active {
  color: #e74c3c;
}
</style>