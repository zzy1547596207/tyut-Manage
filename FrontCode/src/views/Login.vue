<template>
  <div class="login-page">
    <div class="login-box">
      <h1 class="title">辅导员管理系统</h1>
      <p class="subtitle">基本信息采集与管理</p>

      <div v-if="roleHint" class="role-hint">{{ roleHint }}</div><div class="form-group">
        <input v-model="form.username" class="input" placeholder="请输入用户名" @keyup.enter="focusPassword" />
      </div>
      <div v-if="roleHint" class="role-hint">{{ roleHint }}</div><div class="form-group">
        <input ref="pwdRef" v-model="form.password" type="password" class="input" placeholder="请输入密码" @keyup.enter="handleLogin" />
      </div>

      <button class="login-btn" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登 录' }}
      </button>

      <div class="role-tags">
        <span class="tag tag-red">发展辅导员</span>
        <span class="tag tag-blue">发展学院</span>
        <span class="tag tag-orange">发展管理员</span>
      </div>

      <div class="help-text">演示账号：counselor / college / admin，密码均为 123456</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter(); const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)
const pwdRef = ref(null); const roleHint = ref(''); onMounted(() => { const r = route.query.role; if (r === 'COUNSELOR') roleHint.value = '请输入辅导员账号'; else if (r === 'DEPARTMENT') roleHint.value = '请输入学院管理员账号'; else if (r === 'ADMIN') roleHint.value = '请输入校级管理员账号' })

const form = reactive({ username: '', password: '' })

function focusPassword() { pwdRef.value && pwdRef.value.focus() }

async function handleLogin() {
  if (!form.username.trim()) { ElMessage.warning('请输入用户名'); return }
  if (!form.password.trim()) { ElMessage.warning('请输入密码'); return }

  loading.value = true
  try {
    await userStore.login({ username: form.username, password: form.password })
    const role = userStore.role
    if (role === 'COUNSELOR') router.push('/collection/batch')
    else if (role === 'DEPARTMENT') router.push('/college/review')
    else router.push('/college/admin-stats')
  } catch (e) {
    ElMessage.error(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  padding: 20px;
}
.login-box {
  width: 340px;
  padding: 36px 28px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0,0,0,0.25);
  text-align: center;
}
.title { font-size: 22px; font-weight: 700; color: #333; margin-bottom: 4px; }
.subtitle { font-size: 13px; color: #999; margin-bottom: 28px; }
.form-group { margin-bottom: 14px; }
.input {
  width: 100%; height: 44px; border: 1.5px solid #e0e0e0; border-radius: 8px;
  padding: 0 14px; font-size: 14px; outline: none; box-sizing: border-box;
  transition: border-color 0.2s;
  &:focus { border-color: #e74c3c; }
}
.login-btn {
  width: 100%; height: 44px; border: none; border-radius: 8px;
  background: #e74c3c; color: #fff; font-size: 16px; font-weight: 600;
  cursor: pointer; margin-top: 8px; transition: opacity 0.2s;
  &:disabled { opacity: 0.6; cursor: not-allowed; }
  &:active { opacity: 0.9; }
}
.role-tags { display: flex; gap: 8px; justify-content: center; margin-top: 20px; }
.tag { padding: 3px 10px; border-radius: 4px; font-size: 11px; }
.tag-red { background: #fef0f0; color: #e74c3c; border: 1px solid #f5c6cb; }
.tag-blue { background: #f0f4fe; color: #409eff; border: 1px solid #b3d8ff; }
.tag-orange { background: #fef6f0; color: #e6a23c; border: 1px solid #f5dab1; }
.role-hint { text-align: center; font-size: 12px; color: #e74c3c; margin-bottom: 12px; padding: 6px; background: #fef0f0; border-radius: 6px; } .help-text { font-size: 11px; color: #bbb; margin-top: 16px; }
</style>