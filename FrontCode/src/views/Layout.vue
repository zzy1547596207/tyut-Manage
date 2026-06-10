<template>
  <el-container class="layout">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <span v-if="!isCollapse">辅导员管理系统</span>
        <span v-else>辅管</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <template v-if="role === 'COUNSELOR'">
          <el-menu-item index="/counselor/my">
            <el-icon><User /></el-icon>
            <span>我的信息</span>
          </el-menu-item>
          <el-menu-item index="/application/my">
            <el-icon><Document /></el-icon>
            <span>我的申请</span>
          </el-menu-item>
        </template>

        <template v-if="role === 'DEPARTMENT'">
          <el-menu-item index="/counselor/list">
            <el-icon><List /></el-icon>
            <span>辅导员列表</span>
          </el-menu-item>
          <el-menu-item index="/application/pending">
            <el-icon><Checked /></el-icon>
            <span>待审核申请</span>
          </el-menu-item>
        </template>

        <template v-if="role === 'ADMIN'">
          <el-menu-item index="/counselor/list">
            <el-icon><List /></el-icon>
            <span>辅导员管理</span>
          </el-menu-item>
          <el-menu-item index="/department/manage">
            <el-icon><OfficeBuilding /></el-icon>
            <span>院系管理</span>
          </el-menu-item>
          <el-menu-item index="/application/pending">
            <el-icon><Checked /></el-icon>
            <span>待审核申请</span>
          </el-menu-item>
          <el-menu-item index="/application/approve">
            <el-icon><CircleCheck /></el-icon>
            <span>待审批申请</span>
          </el-menu-item>
          <el-menu-item index="/user/manage">
            <el-icon><Setting /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse" :size="22">
            <Fold v-if="!isCollapse" /><Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-tag :type="roleTagType" size="small">{{ roleName }}</el-tag>
          <span class="username">{{ nickname }}</span>
          <el-button text @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const userStore = useUserStore()
const isCollapse = ref(false)

const activeMenu = computed(() => route.path)
const role = computed(() => userStore.role)
const nickname = computed(() => userStore.nickname)

const roleName = computed(() => {
  const map = { COUNSELOR: '发展辅导员', DEPARTMENT: '发展学院', ADMIN: '发展管理员' }
  return map[role.value] || role.value
})
const roleTagType = computed(() => {
  const map = { COUNSELOR: '', DEPARTMENT: 'success', ADMIN: 'warning' }
  return map[role.value] || ''
})

function handleLogout() {
  userStore.logout()
}
</script>

<style scoped lang="scss">
.layout { height: 100vh; }
.aside {
  background: #304156;
  overflow: hidden;
  transition: width .3s;
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    white-space: nowrap;
  }
  .el-menu { border-right: none; }
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
  height: 60px;
  .collapse-btn { cursor: pointer; }
  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;
  }
}
.main { background: #f0f2f5; padding: 20px; }
</style>