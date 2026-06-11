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
        </div>
        <div class="card-info">
          <div class="info-row"><span class="label">所在单位：</span><span>{{ item.department || '-' }}</span></div>
          <div class="info-row"><span class="label">申请类型：</span><span :class="item.type==='profile_change'?'type-blue':'type-orange'">{{ item.updateType || '-' }}</span></div>
          <div class="info-row"><span class="label">申请时间：</span><span>{{ item.applyTime || '-' }}</span></div>
        </div>

        <div v-if="item.expanded" class="detail-section">
          <div v-if="item.loading" class="detail-loading">加载中...</div>
          <template v-else>
            <div class="detail-title">证件照片</div>
            <div v-if="item.idPhoto" class="photo-area">
              <img :src="item.idPhoto" class="id-photo" />
            </div>
            <div v-else class="detail-row"><span class="d-value">未上传</span></div>

            <div class="detail-title" style="margin-top:10px">生活照片</div>
            <div v-if="item.lifePhotos && item.lifePhotos.length > 0" class="life-photo-row">
              <img v-for="(lp, lpi) in item.lifePhotos" :key="'lp'+lpi" :src="lp" class="life-photo" />
            </div>
            <div v-else class="detail-row"><span class="d-value">未上传</span></div>

            <div class="detail-title" style="margin-top:10px">基本信息</div>
            <div class="detail-row"><span class="d-label">最高学历</span><span class="d-value">{{ getContent(item).education || '-' }}</span></div>

            <template v-if="getWorkList(item).length > 0">
              <div class="detail-subtitle">工作经历</div>
              <div v-for="(w, wi) in getWorkList(item)" :key="'w'+wi" class="detail-block">
                <div class="detail-row"><span class="d-label">单位</span><span class="d-value">{{ w.company || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">起止</span><span class="d-value">{{ w.startDate || '-' }} ~ {{ w.endDate || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">类别</span><span class="d-value">{{ w.category || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">学生类别</span><span class="d-value">{{ w.studentType || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">学生数</span><span class="d-value">{{ w.studentCount || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">年级</span><span class="d-value">{{ w.grade || '-' }}</span></div>
              </div>
            </template>

            <template v-if="getStudyList(item).length > 0">
              <div class="detail-subtitle">学习经历</div>
              <div v-for="(s, si) in getStudyList(item)" :key="'s'+si" class="detail-block">
                <div class="detail-row"><span class="d-label">学历</span><span class="d-value">{{ s.degreeType || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">院校</span><span class="d-value">{{ s.school || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">专业</span><span class="d-value">{{ s.major || '-' }}</span></div>
                <div class="detail-row"><span class="d-label">起止</span><span class="d-value">{{ s.entryDate || '-' }} ~ {{ s.gradDate || '-' }}</span></div>
              </div>
            </template>
          </template>
        </div>

        <div class="card-actions">
          <button class="btn-detail" @click="toggleDetail(item)">{{ item.expanded ? '收起' : '查看变更内容' }}</button>
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

function getContent(item) {
  const c = item.content
  if (!c) return {}
  try { return typeof c === 'string' ? JSON.parse(c) : c } catch { return {} }
}
function getWorkList(item) {
  const c = getContent(item)
  if (!c || !c.workList) return []
  try { return typeof c.workList === 'string' ? JSON.parse(c.workList) : c.workList } catch { return [] }
}
function getStudyList(item) {
  const c = getContent(item)
  if (!c || !c.studyList) return []
  try { return typeof c.studyList === 'string' ? JSON.parse(c.studyList) : c.studyList } catch { return [] }
}

async function toggleDetail(item) {
  if (item.expanded) {
    item.expanded = false
    return
  }
  item.expanded = true
  if (item.idPhoto !== undefined) return
  if (item.type !== 'batch_submit' || !item.batchId) return
  item.loading = true
  try {
    const res = await request.get('/submission/detail', { params: { batchId: item.batchId, userId: item.userId } })
    if (res.data) {
      item.idPhoto = res.data.idPhoto || null
      item.lifePhotos = (() => {
        try { const arr = JSON.parse(res.data.lifePhotos || '[]'); return Array.isArray(arr) ? arr : [] } catch { return [] }
      })()
    }
  } catch (e) { console.error('加载照片失败', e) }
  item.loading = false
}

async function loadApplications() {
  try {
    const res = await request.get('/review/pending')
    if (res.data) {
      applications.value = res.data.map(a => ({ ...a, selected: false, expanded: false }))
    }
  } catch (e) { console.error('加载待审核列表失败', e) }
}

onMounted(loadApplications)
onActivated(loadApplications)

function toggleAll() { applications.value.forEach(a => a.selected = selectAll.value) }

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
    for (const item of selected) { await request.post('/review/approve/' + item.id) }
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
.card-info { margin-bottom: 8px; }
.info-row { font-size: 12px; color: #666; margin-bottom: 3px; .label { color: #999; } }
.type-blue { color: #409eff; }
.type-orange { color: #e6a23c; }
.detail-section { background: #fafafa; border-radius: 8px; padding: 10px 12px; margin-bottom: 10px; }
.detail-title { font-size: 12px; font-weight: 600; color: #333; margin-bottom: 6px; }
.detail-loading { text-align: center; color: #999; font-size: 12px; padding: 10px 0; }
.detail-body { display: flex; flex-direction: column; gap: 4px; }
.detail-row { display: flex; font-size: 12px; .d-label { color: #999; min-width: 50px; } .d-value { color: #333; } }
.detail-subtitle { font-size: 12px; font-weight: 600; color: #e74c3c; margin-top: 6px; margin-bottom: 2px; padding-top: 4px; border-top: 1px solid #eee; }
.detail-block { background: #fff; border-radius: 6px; padding: 6px 8px; margin-bottom: 4px; }
.photo-area { margin-bottom: 6px; }
.id-photo { width: 120px; height: 150px; object-fit: cover; border-radius: 6px; border: 1px solid #eee; }
.life-photo-row { display: flex; gap: 6px; flex-wrap: wrap; margin-bottom: 6px; }
.life-photo { width: 70px; height: 70px; object-fit: cover; border-radius: 6px; border: 1px solid #eee; }
.card-actions { display: flex; justify-content: flex-end; gap: 8px; flex-wrap: wrap; }
.btn-detail { padding: 6px 12px; border: 1px solid #ddd; border-radius: 6px; background: #fff; color: #333; font-size: 12px; cursor: pointer; }
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