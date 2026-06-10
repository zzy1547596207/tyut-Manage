<template>
  <div class="dashboard">
    <h3>欢迎回来，{{ userStore.nickname || userStore.username }}</h3>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8" v-for="card in cards" :key="card.title">
        <el-card shadow="hover">
          <div class="card-content">
            <el-icon :size="36" :color="card.color"><component :is="card.icon" /></el-icon>
            <div>
              <div class="card-title">{{ card.title }}</div>
              <div class="card-desc">{{ card.desc }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

const cards = computed(() => {
  const role = userStore.role
  if (role === 'COUNSELOR') {
    return [
      { title: '我的信息', desc: '查看和编辑个人信息', icon: 'User', color: '#409EFF' },
      { title: '提交申请', desc: '提交信息变更等申请', icon: 'Document', color: '#67C23A' },
      { title: '申请进度', desc: '查看申请审核进度', icon: 'Clock', color: '#E6A23C' }
    ]
  } else if (role === 'DEPARTMENT') {
    return [
      { title: '辅导员列表', desc: '查看本院系辅导员信息', icon: 'List', color: '#409EFF' },
      { title: '审核申请', desc: '审核辅导员的申请', icon: 'Checked', color: '#67C23A' },
      { title: '院系统计', desc: '本院系辅导员统计报表', icon: 'PieChart', color: '#E6A23C' }
    ]
  } else {
    return [
      { title: '辅导员管理', desc: '全校辅导员增删改查', icon: 'List', color: '#409EFF' },
      { title: '院系管理', desc: '院系信息维护', icon: 'OfficeBuilding', color: '#67C23A' },
      { title: '用户管理', desc: '系统账号管理', icon: 'Setting', color: '#E6A23C' }
    ]
  }
})
</script>

<style scoped lang="scss">
.dashboard h3 { font-size: 20px; color: #303133; }
.card-content {
  display: flex;
  align-items: center;
  gap: 16px;
  .card-title { font-size: 16px; font-weight: bold; }
  .card-desc { color: #909399; font-size: 13px; margin-top: 4px; }
}
</style>