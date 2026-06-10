<template>
  <div class="more-page">
    <div class="page-title">更多功能</div>

    <div class="func-grid">
      <div class="func-item" v-for="item in functions" :key="item.label" @click="item.action">
        <div class="func-icon" :style="{ background: item.color }">{{ item.icon }}</div>
        <span>{{ item.label }}</span>
      </div>
    </div>

    <div class="tip">更多功能开发中…</div>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const functions = [
  { label: '数据导出', icon: '📥', color: '#e3f2fd', action: () => ElMessage.info('功能开发中') },
  { label: '通知公告', icon: '📢', color: '#fff3e0', action: () => ElMessage.info('功能开发中') },
  { label: '操作记录', icon: '📋', color: '#e8f5e9', action: () => ElMessage.info('功能开发中') },
  { label: '系统设置', icon: '⚙️', color: '#f3e5f5', action: () => ElMessage.info('功能开发中') },
  { label: '意见反馈', icon: '💬', color: '#fce4ec', action: () => ElMessage.info('功能开发中') },
  { label: '帮助中心', icon: '❓', color: '#e0f7fa', action: () => ElMessage.info('功能开发中') }
]
</script>

<style scoped lang="scss">
.more-page { padding: 16px; }
.page-title { font-size: 16px; font-weight: 600; color: #333; margin-bottom: 16px; }

.func-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.func-item {
  background: #fff;
  border-radius: 12px;
  padding: 16px 8px;
  text-align: center;
  cursor: pointer;
  font-size: 12px;
  color: #333;
  .func-icon {
    width: 44px;
    height: 44px;
    margin: 0 auto 8px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 22px;
  }
}

.tip {
  text-align: center;
  color: #ccc;
  font-size: 11px;
  margin-top: 40px;
}
</style>