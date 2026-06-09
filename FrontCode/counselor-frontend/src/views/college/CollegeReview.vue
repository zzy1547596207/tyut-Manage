<template>
  <div class="review-page">
    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="请输入姓名/工号查询" @keyup.enter="doSearch" />
      <button class="search-btn" @click="doSearch">搜索</button>
    </div>

    <div class="list-body">
      <div v-if="filteredList.length === 0" class="empty-hint">暂无待审核申请</div>

      <div v-for="item in filteredList" :key="item.id" class="apply-card">
        <div class="card-top">
          <div class="checkbox" :class="{ checked: item.selected }" @click="item.selected = !item.selected"></div>
          <div class="avatar">&#x1f464;</div>
          <div class="user-info">
            <div class="name-row">{{ item.counselorName }} <span class="emp-no">{{ item.employeeNo }}</span></div>
          </div>
          <div class="phone-icon">&#x1f4de;</div>
        </div>

        <div class="card-fields">
          <div class="field"><span>所在单位：</span>{{ item.department }}</div>
          <div class="field"><span>辅导员类型：</span>{{ item.type }}</div>
          <div class="field"><span>辅导员职务：</span>{{ item.position }}</div>
          <div class="field"><span>申请时间：</span>{{ item.applyTime }}</div>
          <div class="field"><span>申请类型：</span>{{ item.updateType }}</div>
        </div>

        <div class="change-section" @click="item.expanded = !item.expanded">
          <span>变更内容</span>
          <span class="expand-icon">{{ item.expanded ? '收起' : '展开' }}</span>
        </div>
        <div v-if="item.expanded" class="change-detail">
          <div class="detail-item" v-for="(ch, ci) in item.changes" :key="ci">
            <div class="detail-label">{{ ch.label }}</div>
            <div class="detail-row"><span>原：</span>{{ ch.old }}</div>
            <div class="detail-row"><span>新：</span>{{ ch.new }}</div>
          </div>
        </div>

        <div class="card-actions">
          <button class="reject-btn" @click="handleReject(item)">驳回</button>
          <button class="approve-btn" @click="handleApprove(item)">审核通过</button>
        </div>
      </div>
    </div>

    <div v-if="filteredList.length > 0" class="bottom-bar">
      <label class="select-all"><input type="checkbox" v-model="selectAll" @change="toggleAll" /> 全选</label>
      <button class="batch-btn" @click="handleBatchApprove">批量审核</button>
    </div>

    <div v-if="rejectDialog.show" class="overlay" @click.self="rejectDialog.show = false">
      <div class="dialog">
        <div class="dialog-title">驳回原因</div>
        <textarea v-model="rejectDialog.reason" placeholder="请输入驳回原因" rows="3"></textarea>
        <div class="dialog-btns">
          <button class="cancel-btn" @click="rejectDialog.show = false">取消</button>
          <button class="confirm-btn" @click="confirmReject">确认驳回</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const keyword = ref('')
const selectAll = ref(false)

const applications = ref([])

const rejectDialog = reactive({ show: false, reason: '', item: null })

const filteredList = computed(() => {
  if (!keyword.value.trim()) return applications.value
  const kw = keyword.value.trim()
  return applications.value.filter(a => a.counselorName.includes(kw) || a.employeeNo.includes(kw))
})

function doSearch() {}

function toggleAll() {
  applications.value.forEach(a => a.selected = selectAll.value)
}

function handleReject(item) {
  rejectDialog.item = item
  rejectDialog.reason = ''
  rejectDialog.show = true
}

function confirmReject() {
  if (!rejectDialog.reason.trim()) { ElMessage.warning('请输入驳回原因'); return }
  const item = rejectDialog.item
  if (item.updateType === '采集填报') {
    try { const sm = JSON.parse(localStorage.getItem('batch_status') || '{}'); sm[String(item.batchId || '1')] = 'rejected'; localStorage.setItem('batch_status', JSON.stringify(sm)) } catch (e) {}
  } else {
    try { const raw = localStorage.getItem('profile_data'); if (raw) { const p = JSON.parse(raw); p.status = 'rejected'; p.rejectReason = rejectDialog.reason; localStorage.setItem('profile_data', JSON.stringify(p)) } } catch (e) {}
  }
  applications.value = applications.value.filter(a => a.id !== item.id)
  try { localStorage.setItem('college_applications', JSON.stringify(applications.value)) } catch (e) {}
  rejectDialog.show = false
  ElMessage.success('已驳回')
}

function handleApprove(item) {
  if (item.updateType === '采集填报') {
    try { const sm = JSON.parse(localStorage.getItem('batch_status') || '{}'); sm[String(item.batchId || '1')] = 'approved'; localStorage.setItem('batch_status', JSON.stringify(sm)) } catch (e) {}
  } else {
    try { const raw = localStorage.getItem('profile_data'); if (raw) { const p = JSON.parse(raw); delete p.status; delete p.rejectReason; p.updateTime = new Date().toLocaleString(); localStorage.setItem('profile_data', JSON.stringify(p)) } } catch (e) {}
  }
  applications.value = applications.value.filter(a => a.id !== item.id)
  try { localStorage.setItem('college_applications', JSON.stringify(applications.value)) } catch (e) {}
  ElMessage.success('审核通过，档案已更新')
}

function handleBatchApprove() {
  const selected = applications.value.filter(a => a.selected)
  if (selected.length === 0) { ElMessage.warning('请先勾选待审核单据'); return }
  selected.forEach(item => handleApprove(item))
}

onMounted(() => {
  try {
    const raw = localStorage.getItem('college_applications')
    if (raw) { applications.value = JSON.parse(raw); return }
  } catch (e) {}
  applications.value = [
    { id: 1, counselorName: '张三', employeeNo: '11004', department: 'A学院', type: '专职辅导员', position: '副书记', applyTime: '2025-03-11 00:00:00', updateType: '自主变更', selected: true, expanded: false, changes: [{ label: '最高学历', old: '本科', new: '硕士' }, { label: '校区', old: '东校区', new: '主校区' }] },
    { id: 2, counselorName: '李四', employeeNo: '11005', department: 'A学院', type: '专职辅导员', position: '辅导员', applyTime: '2025-03-12 00:00:00', updateType: '自主变更', selected: false, expanded: false, changes: [{ label: '最高学历', old: '硕士', new: '博士' }] }
  ]
  localStorage.setItem('college_applications', JSON.stringify(applications.value))
})
</script>

<style scoped lang="scss">
.review-page { }
.search-bar { display: flex; gap: 8px; padding: 10px 14px; background: #fff; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; &:focus { border-color: #e74c3c; } }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.list-body { padding: 10px 14px; }
.empty-hint { text-align: center; color: #ccc; font-size: 14px; padding: 60px 0; }
.apply-card { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }

.card-top { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.checkbox { width: 20px; height: 20px; border: 2px solid #ddd; border-radius: 50%; flex-shrink: 0; cursor: pointer; &.checked { border-color: #e74c3c; background: #e74c3c; position: relative; &::after { content: ''; position: absolute; top: 3px; left: 5px; width: 5px; height: 9px; border: solid #fff; border-width: 0 2px 2px 0; transform: rotate(45deg); } } }
.avatar { font-size: 28px; }
.user-info { flex: 1; .name-row { font-size: 14px; font-weight: 600; color: #333; } .emp-no { font-size: 12px; color: #999; font-weight: 400; margin-left: 6px; } }
.phone-icon { font-size: 20px; cursor: pointer; }

.card-fields { margin-bottom: 8px; }
.card-fields .field { font-size: 12px; color: #666; margin-bottom: 3px; span { color: #999; } }

.change-section { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-top: 1px solid #f5f5f5; font-size: 13px; color: #e74c3c; cursor: pointer; .expand-icon { font-size: 12px; color: #999; } }
.change-detail { background: #fafafa; border-radius: 8px; padding: 10px; margin-bottom: 8px; }
.detail-item { margin-bottom: 8px; &:last-child { margin-bottom: 0; } }
.detail-label { font-size: 12px; font-weight: 600; color: #333; margin-bottom: 4px; }
.detail-row { font-size: 11px; color: #666; span { color: #999; } }

.card-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 8px; }
.reject-btn, .approve-btn { padding: 6px 16px; border-radius: 6px; font-size: 12px; cursor: pointer; }
.reject-btn { border: 1px solid #ddd; background: #fff; color: #666; }
.approve-btn { border: none; background: #e74c3c; color: #fff; }

.bottom-bar { position: sticky; bottom: 0; display: flex; justify-content: space-between; align-items: center; padding: 10px 14px; background: #fff; border-top: 1px solid #eee; }
.select-all { font-size: 13px; color: #333; cursor: pointer; display: flex; align-items: center; gap: 6px; input { width: 16px; height: 16px; } }
.batch-btn { padding: 6px 16px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }

.overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,.5); z-index: 100; display: flex; align-items: center; justify-content: center; }
.dialog { width: 300px; background: #fff; border-radius: 12px; padding: 20px; .dialog-title { font-size: 15px; font-weight: 600; margin-bottom: 12px; } textarea { width: 100%; border: 1px solid #eee; border-radius: 6px; padding: 10px; font-size: 13px; resize: none; outline: none; } .dialog-btns { display: flex; gap: 10px; margin-top: 12px; justify-content: flex-end; } .cancel-btn, .confirm-btn { padding: 6px 16px; border-radius: 6px; font-size: 13px; cursor: pointer; } .cancel-btn { border: 1px solid #ddd; background: #fff; color: #333; } .confirm-btn { border: none; background: #e74c3c; color: #fff; } }
</style>