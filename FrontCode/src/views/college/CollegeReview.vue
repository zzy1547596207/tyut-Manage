<template>
  <div class="review-page">
    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="请输入姓名/工号查询" />
      <button class="search-btn" @click="doSearch">搜索</button>
    </div>

    <div v-if="filteredList.length > 0" class="review-list">
      <div v-for="item in filteredList" :key="item.id" class="review-card">
        <div class="card-top">
          <span class="radio-dot" :class="{ checked: item.selected }" @click="item.selected=!item.selected"></span>
          <span class="avatar-circle">&#x1f468;</span>
          <span class="counselor-name">{{ item.userName || '辅导员' }}</span>
          <span class="employee-no">{{ item.employeeNo || '-' }}</span>
          <span class="phone-icon" @click="ElMessage.info('联系电话功能')">&#x1f4de;</span>
        </div>
        <div class="card-info">
          <div class="info-row"><span class="label">所在单位：</span><span>{{ item.department || '-' }}</span></div>
          <div class="info-row"><span class="label">申请类型：</span><span :class="item.type==='profile_change'?'type-blue':'type-orange'">{{ item.updateType || '-' }}</span></div>
          <div class="info-row"><span class="label">申请时间：</span><span>{{ item.applyTime || '-' }}</span></div>
        </div>
        <div class="card-actions">
          <button class="btn-pass" @click="handleApprove(item)">审核通过</button>
          <button class="btn-reject" @click="handleReject(item)">驳回</button>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <p class="empty-text">暂无待审核申请</p>
      <p class="empty-sub">辅导员提交的变更申请会出现在这里</p>
    </div>

    <div class="bottom-bar">
      <label class="select-all"><input type="checkbox" v-model="selectAll" @change="toggleAll" /> 全选</label>
      <button class="batch-btn" @click="batchApprove">批量审核</button>
    </div>

    <!-- 驳回弹窗 -->
    <div v-if="rejectDialog.show" class="dialog-overlay" @click.self="rejectDialog.show=false">
      <div class="dialog-box">
        <div class="dialog-title">驳回原因</div>
        <textarea v-model="rejectDialog.reason" placeholder="请填写驳回原因" rows="3" class="dialog-textarea"></textarea>
        <div class="dialog-btns">
          <button class="btn-cancel" @click="rejectDialog.show=false">取消</button>
          <button class="btn-confirm" @click="confirmReject">确认驳回</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onActivated } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const keyword = ref('')
const selectAll = ref(false)
const applications = ref([])
const rejectDialog = reactive({ show: false, reason: '', item: null })

const filteredList = computed(() => {
  if (!keyword.value.trim()) return applications.value
  const kw = keyword.value.trim()
  return applications.value.filter(a =>
    (a.userName && a.userName.includes(kw)) ||
    (a.employeeNo && a.employeeNo.includes(kw))
  )
})

function doSearch() {}

async function loadApplications() {
  try {
    const res = await request.get('/review/pending')
    if (res.data) {
      applications.value = res.data.map(a => ({ ...a, selected: false }))
    }
  } catch (e) { console.error('加载待审核列表失败', e) }
}

onMounted(loadApplications)
onActivated(loadApplications)

function toggleAll() {
  applications.value.forEach(a => a.selected = selectAll.value)
}

function handleReject(item) {
  rejectDialog.item = item
  rejectDialog.reason = ''
  rejectDialog.show = true
}

async function confirmReject() {
  if (!rejectDialog.reason.trim()) { ElMessage.warning('请输入驳回原因'); return }
  try {
    await request.post('/review/reject/' + rejectDialog.item.id, { reason: rejectDialog.reason })
    ElMessage.success('已驳回')
    rejectDialog.show = false
    loadApplications()
  } catch (e) { ElMessage.error('操作失败: ' + (e.message || '')) }
}

async function handleApprove(item) {
  try {
    await request.post('/review/approve/' + item.id)
    ElMessage.success('审核通过')
    loadApplications()
  } catch (e) { ElMessage.error('操作失败: ' + (e.message || '')) }
}

async function batchApprove() {
  const selected = applications.value.filter(a => a.selected)
  if (selected.length === 0) { ElMessage.warning('请选择需要审核的单据'); return }
  try {
    for (const item of selected) {
      await request.post('/review/approve/' + item.id)
    }
    ElMessage.success('批量审核完成')
    loadApplications()
  } catch (e) { ElMessage.error('批量审核失败: ' + (e.message || '')) }
}
</script>

<style scoped lang="scss">
.review-page { padding: 12px; padding-bottom: 70px; }
.search-bar { display: flex; gap: 8px; margin-bottom: 14px; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; }
.search-input:focus { border-color: #e74c3c; }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.review-list { display: flex; flex-direction: column; gap: 12px; }
.review-card { background: #fff; border-radius: 12px; padding: 14px; }
.card-top { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.radio-dot { width: 18px; height: 18px; border: 2px solid #ddd; border-radius: 50%; cursor: pointer; flex-shrink: 0; }
.radio-dot.checked { border-color: #e74c3c; background: #e74c3c; }
.avatar-circle { font-size: 18px; }
.counselor-name { font-size: 14px; font-weight: 600; color: #333; }
.employee-no { font-size: 12px; color: #999; }
.phone-icon { margin-left: auto; cursor: pointer; font-size: 16px; }
.card-info { margin-bottom: 10px; }
.info-row { font-size: 12px; color: #666; margin-bottom: 3px; .label { color: #999; } }
.type-blue { color: #409eff; }
.type-orange { color: #e6a23c; }
.card-actions { display: flex; justify-content: flex-end; gap: 8px; }
.btn-pass { padding: 6px 18px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.btn-reject { padding: 6px 18px; border: 1px solid #ddd; border-radius: 6px; background: #fff; color: #666; font-size: 13px; cursor: pointer; }
.empty-state { text-align: center; padding-top: 60px; }
.empty-text { font-size: 15px; color: #333; margin-bottom: 4px; }
.empty-sub { font-size: 12px; color: #999; }
.bottom-bar { position: sticky; bottom: 0; display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; border-top: 1px solid #eee; }
.select-all { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #333; cursor: pointer; }
.batch-btn { padding: 8px 24px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 14px; cursor: pointer; }
.dialog-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 999; }
.dialog-box { background: #fff; border-radius: 12px; padding: 20px; width: 300px; }
.dialog-title { font-size: 16px; font-weight: 600; margin-bottom: 12px; text-align: center; }
.dialog-textarea { width: 100%; border: 1px solid #ddd; border-radius: 6px; padding: 10px; font-size: 13px; outline: none; resize: none; }
.dialog-btns { display: flex; gap: 12px; margin-top: 12px; }
.btn-cancel, .btn-confirm { flex: 1; height: 38px; border-radius: 6px; font-size: 14px; cursor: pointer; }
.btn-cancel { background: #fff; border: 1px solid #ddd; color: #333; }
.btn-confirm { background: #e74c3c; border: none; color: #fff; }
</style>