import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'
import router from '../router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || 'test-mode')
  const username = ref(localStorage.getItem('username') || 'demo')
  const nickname = ref(localStorage.getItem('nickname') || '演示用户')
  const role = ref(localStorage.getItem('role') || 'COUNSELOR')

  async function login(loginForm) {
    const res = await request.post('/auth/login', loginForm)
    const data = res.data
    token.value = data.token
    username.value = data.username
    nickname.value = data.nickname
    role.value = data.role
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('nickname', data.nickname)
    localStorage.setItem('role', data.role)
    return data
  }

  function switchRole(newRole) {
    role.value = newRole
    localStorage.setItem('role', newRole)
    const names = { COUNSELOR: '发展辅导员', DEPARTMENT: '发展学院', ADMIN: '发展管理员' }
    nickname.value = names[newRole] || newRole
    localStorage.setItem('nickname', nickname.value)
  }

  function logout() {
    token.value = 'test-mode'
    username.value = 'demo'
    nickname.value = '演示用户'
    role.value = 'COUNSELOR'
    localStorage.clear()
    router.push('/login')
  }

  return { token, username, nickname, role, login, switchRole, logout }
})