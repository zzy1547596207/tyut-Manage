<template>
  <div class="more-page">
    <div class="menu-list">
      <div class="menu-item" @click="goStats">
        <div class="menu-icon" style="background:#e3f2fd">
          <svg viewBox="0 0 24 24" fill="none" stroke="#1976D2" stroke-width="2" width="22" height="22">
            <rect x="3" y="3" width="18" height="18" rx="2"/><line x1="7" y1="15" x2="7" y2="10"/><line x1="12" y1="15" x2="12" y2="7"/><line x1="17" y1="15" x2="17" y2="12"/>
          </svg>
        </div>
        <span class="menu-label">数据统计</span>
        <span class="menu-arrow">&gt;</span>
      </div>

      <div class="menu-item" @click="router.push('/college/profile')">
        <div class="menu-icon" style="background:#f3e5f5">
          <svg viewBox="0 0 24 24" fill="none" stroke="#8E24AA" stroke-width="2" width="22" height="22">
            <circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
          </svg>
        </div>
        <span class="menu-label">个人信息</span>
        <span class="menu-arrow">&gt;</span>
      </div>

      <div class="menu-item" @click="showRoleSheet = true">
        <div class="menu-icon" style="background:#e3f2fd">
          <svg viewBox="0 0 24 24" fill="none" stroke="#1976D2" stroke-width="2" width="22" height="22">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
        <span class="menu-label">角色切换</span>
        <span class="menu-arrow">&gt;</span>
      </div>

      <div class="menu-item" @click="router.push('/college/approval/history')">
        <div class="menu-icon" style="background:#fff3e0">
          <svg viewBox="0 0 24 24" fill="none" stroke="#F57C00" stroke-width="2" width="22" height="22">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/>
          </svg>
        </div>
        <span class="menu-label">信息采集计划审批记录</span>
        <span class="menu-arrow">&gt;</span>
      </div>
    </div>

    <div v-if="showRoleSheet" class="overlay" @click.self="showRoleSheet = false">
      <div class="role-sheet">
        <div class="sheet-header"><span class="sheet-title">角色切换</span><span class="sheet-close" @click="showRoleSheet = false">×</span></div>
        <div class="sheet-body">
          <div class="role-btn" :class="{ active: currentRole === 'COUNSELOR' }" @click="doSwitch('COUNSELOR')">发展辅导员角色</div>
          <div class="role-btn" :class="{ active: currentRole === 'DEPARTMENT' }" @click="doSwitch('DEPARTMENT')">发展学院角色</div>
          <div class="role-btn" :class="{ active: currentRole === 'ADMIN' }" @click="doSwitch('ADMIN')">发展管理员角色</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const userStore = useUserStore()
const showRoleSheet = ref(false)
function goStats() { if (userStore.role === 'ADMIN') router.push('/college/admin-stats'); else router.push('/college/stats') }
const currentRole = computed(() => userStore.role)
const names = { COUNSELOR: '发展辅导员', DEPARTMENT: '发展学院', ADMIN: '发展管理员' }

function doSwitch(role) {
  userStore.switchRole(role)
  showRoleSheet.value = false
  ElMessage.success('已切换为：' + names[role])
  if (role === 'DEPARTMENT' || role === 'ADMIN') { router.push('/college/review') }
  else { router.push('/collection/batch') }
}
</script>

<style scoped lang="scss">
.more-page { }
.menu-list { background: #fff; }
.menu-item { display: flex; align-items: center; padding: 14px 16px; border-bottom: 1px solid #f5f5f5; cursor: pointer; &:active { background: #fafafa; } }
.menu-icon { width: 40px; height: 40px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.menu-label { flex: 1; font-size: 14px; color: #333; margin-left: 12px; }
.menu-arrow { color: #ccc; font-size: 16px; }
.overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,.5); z-index: 100; display: flex; align-items: flex-end; }
.role-sheet { width: 100%; background: #fff; border-radius: 16px 16px 0 0; padding: 16px 16px 24px; animation: slideUp .25s ease; }
@keyframes slideUp { from { transform: translateY(100%); } to { transform: translateY(0); } }
.sheet-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.sheet-title { font-size: 16px; font-weight: 600; color: #333; }
.sheet-close { font-size: 22px; color: #999; cursor: pointer; padding: 0 4px; }
.sheet-body { display: flex; flex-direction: column; gap: 10px; }
.role-btn { height: 46px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-size: 14px; font-weight: 500; cursor: pointer; border: 1.5px solid #e74c3c; background: #fff; color: #e74c3c; &.active { background: #e74c3c; color: #fff; } }
</style>