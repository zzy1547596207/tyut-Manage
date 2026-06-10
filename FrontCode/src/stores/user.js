import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'
import router from '../router'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const role = ref(localStorage.getItem('role') || 'COUNSELOR')

  async function login(loginForm) {
    const res = await request.post('/login', loginForm)
    const data = res.data
    token.value = data.token
    username.value = loginForm.username
    nickname.value = data.nickname
    role.value = data.role
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', loginForm.username)
    localStorage.setItem('nickname', data.nickname)
    localStorage.setItem('role', data.role)
    return data
  }

  function switchRole(newRole) {
    // 清除当前登录状态，跳转到登录页
    token.value = ''
    username.value = ''
    nickname.value = ''
    localStorage.clear()
    router.push('/login')
  }

  function logout() {
    token.value = ''
    username.value = ''
    nickname.value = ''
    role.value = 'COUNSELOR'
    localStorage.clear()
    router.push('/login')
  }

  return { token, username, nickname, role, login, switchRole, logout }
})