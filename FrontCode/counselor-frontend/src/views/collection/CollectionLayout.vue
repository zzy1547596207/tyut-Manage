<template>
  <div class="collection-app">
    <div class="status-bar">
      <span class="nav-title">信息采集批次</span>
    </div>

    <div class="main-content">
      <router-view />
    </div>

    <div class="tab-bar">
      <div class="tab-item" :class="{ active: activeTab === 'batch' }" @click="switchTab('batch')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" />
          <rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" />
          <line x1="8" y1="10" x2="8" y2="14" /><line x1="5" y1="12" x2="11" y2="12" />
        </svg>
        <span>采集批次</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'profile' }" @click="switchTab('profile')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-dasharray="2 2">
          <circle cx="12" cy="8" r="4" /><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
        </svg>
        <span>个人信息</span>
      </div>
      <div class="tab-item" :class="{ active: activeTab === 'more' }" @click="switchTab('more')">
        <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-dasharray="1.5 1.5">
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

const router = useRouter()
const route = useRoute()

const activeTab = computed(() => {
  const path = route.path
  if (path.includes('/collection/approval/')) return 'more'
  if (path.includes('/collection/batch')) return 'batch'
  if (path.includes('/collection/profile')) return 'profile'
  if (path.includes('/collection/more')) return 'more'
  return 'batch'
})

function switchTab(tab) {
  router.push('/collection/' + tab)
}
</script>

<style scoped lang="scss">
.collection-app { width: 100vw; height: 100vh; display: flex; flex-direction: column; background: #f5f5f5; }
.status-bar { display: flex; justify-content: center; align-items: center; padding: 12px 20px 8px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; color: #333; } }
.main-content { flex: 1; overflow-y: auto; position: relative; }
.tab-bar { display: flex; background: #fff; border-top: 1px solid #e8e8e8; flex-shrink: 0; padding: 6px 0; }
.tab-item { flex: 1; display: flex; flex-direction: column; align-items: center; gap: 3px; padding: 6px 0; cursor: pointer; color: #999; font-size: 11px; transition: color .2s; .tab-icon { width: 22px; height: 22px; } &.active { color: #e74c3c; } }
</style>