<template>
  <div class="admin-stats">
    <div class="top-nav">
      <div class="back-btn" @click="$router.push('/college/more')">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">数据统计</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="header-bar">
      <span class="total-info">辅导员基本信息在库数据量：<b>{{ totalCount }}</b> 条</span>
    </div>

    <div class="metric-cards">
      <div class="metric-card pink">
        <div class="metric-num">{{ totalCount }}</div>
        <div class="metric-label">辅导员总人数</div>
      </div>
      <div class="metric-card blue">
        <div class="metric-num">{{ maleCount }}</div>
        <div class="metric-label">男生人数</div>
      </div>
      <div class="metric-card green">
        <div class="metric-num">{{ femaleCount }}</div>
        <div class="metric-label">女生人数</div>
      </div>
    </div>

    <div class="chart-section">
      <div class="section-header">
        <span class="section-title">辅导员类型分布</span>
        <button class="filter-btn" @click="showFilter=!showFilter">+ 添加条件</button>
      </div>
      <div class="chart-box">
        <div v-for="(item, i) in typeData" :key="i" class="bar-row">
          <span class="bar-label">{{ item.name }}</span>
          <div class="bar-track"><div class="bar-fill" :style="{ width: (item.count/totalCount*100||0)+'%', background: typeColors[i%3] }"></div></div>
          <span class="bar-num">{{ item.count }}</span>
        </div>
      </div>
    </div>

    <div class="chart-section">
      <div class="section-title">学历分布</div>
      <div class="chart-box">
        <div v-for="(item, i) in eduData" :key="i" class="bar-row">
          <span class="bar-label">{{ item.name }}</span>
          <div class="bar-track"><div class="bar-fill" :style="{ width: (item.count/totalCount*100||0)+'%', background: eduColors[i%4] }"></div></div>
          <span class="bar-num">{{ item.count }}</span>
        </div>
      </div>
    </div>

    <div class="chart-section">
      <div class="section-title">各学院辅导员分布</div>
      <div v-for="(item, i) in deptData" :key="i" class="dept-card">
        <span class="dept-name">{{ item.name }}</span>
        <span class="dept-count">{{ item.count }} 人</span>
      </div>
    </div>

    <!-- 筛选弹窗 -->
    <div v-if="showFilter" class="dialog-overlay" @click.self="showFilter=false">
      <div class="dialog-box">
        <div class="dialog-title">条件选择</div>
        <div class="filter-options">
          <div v-for="dim in dims" :key="dim.key" class="filter-option" :class="{ active: selectedDim === dim.key }" @click="selectedDim = dim.key">
            {{ dim.label }}
            <span class="opt-count">{{ getDimCount(dim.key) }}</span>
          </div>
        </div>
        <div class="dialog-btns">
          <button class="btn-cancel" @click="showFilter=false">取消</button>
          <button class="btn-confirm" @click="applyFilter">提交</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '@/utils/request'

const dims = [
  { key: 'gender', label: '性别' },
  { key: 'politicalStatus', label: '政治面貌' },
  { key: 'education', label: '最高学历' },
  { key: 'degree', label: '最后学位' }
]
const eduColors = ['#3498db', '#2ecc71', '#f39c12', '#e74c3c']
const typeColors = ['#e74c3c', '#3498db', '#f39c12']

const counselors = ref([])
const totalCount = ref(0)
const maleCount = ref(0)
const femaleCount = ref(0)
const showFilter = ref(false)
const selectedDim = ref('gender')

const typeData = computed(() => groupBy('type'))
const eduData = computed(() => groupBy('education'))
const deptData = computed(() => groupBy('department'))

function groupBy(field) {
  const map = {}
  counselors.value.forEach(c => {
    const val = c[field] || '未知'
    map[val] = (map[val] || 0) + 1
  })
  return Object.entries(map).map(([name, count]) => ({ name, count }))
}

function getDimCount(dim) {
  return new Set(counselors.value.map(c => c[dim]).filter(Boolean)).size
}

async function loadData() {
  try {
    const res = await request.get('/counselors/list')
    counselors.value = res.data || []
    totalCount.value = counselors.value.length
    maleCount.value = counselors.value.filter(c => c.gender === '男').length
    femaleCount.value = counselors.value.filter(c => c.gender === '女').length
  } catch (e) { console.error('加载数据失败', e) }
}

onMounted(loadData)

function applyFilter() {
  // Apply filter logic if needed
  showFilter.value = false
}
</script>

<style scoped lang="scss">
.admin-stats { background: #f5f5f5; min-height: 100%; padding-bottom: 30px; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; } }
.header-bar { padding: 10px 14px; background: #fdf0f0; }
.total-info { font-size: 13px; color: #666; b { color: #e74c3c; } }
.metric-cards { display: flex; gap: 10px; padding: 10px 14px; background: #fff; }
.metric-card { flex: 1; border-radius: 10px; padding: 12px; text-align: center; }
.metric-card.pink { background: #fef0f0; }
.metric-card.blue { background: #f0f4fe; }
.metric-card.green { background: #f0fef0; }
.metric-num { font-size: 24px; font-weight: 700; }
.metric-card.pink .metric-num { color: #e74c3c; }
.metric-card.blue .metric-num { color: #3498db; }
.metric-card.green .metric-num { color: #2ecc71; }
.metric-label { font-size: 11px; color: #999; margin-top: 4px; }
.chart-section { background: #fff; margin: 10px 14px; border-radius: 12px; padding: 14px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; }
.filter-btn { padding: 4px 12px; border: 1px solid #e74c3c; border-radius: 14px; background: #fff; color: #e74c3c; font-size: 12px; cursor: pointer; }
.chart-box { display: flex; flex-direction: column; gap: 8px; }
.bar-row { display: flex; align-items: center; gap: 8px; }
.bar-label { width: 60px; font-size: 12px; color: #666; text-align: right; }
.bar-track { flex: 1; height: 16px; background: #f0f0f0; border-radius: 8px; overflow: hidden; }
.bar-fill { height: 100%; border-radius: 8px; transition: width 0.5s; }
.bar-num { width: 30px; font-size: 12px; color: #333; font-weight: 600; }
.dept-card { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #f5f5f5; }
.dept-name { font-size: 13px; color: #333; }
.dept-count { font-size: 13px; color: #e74c3c; font-weight: 600; }
.dialog-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 999; }
.dialog-box { background: #fff; border-radius: 12px; padding: 20px; width: 300px; }
.dialog-title { font-size: 16px; font-weight: 600; text-align: center; margin-bottom: 16px; }
.filter-options { display: flex; flex-direction: column; gap: 8px; }
.filter-option { padding: 10px 14px; border: 1.5px solid #eee; border-radius: 8px; font-size: 14px; cursor: pointer; display: flex; justify-content: space-between; }
.filter-option.active { border-color: #e74c3c; color: #e74c3c; background: #fef0f0; }
.opt-count { color: #999; font-size: 12px; }
.dialog-btns { display: flex; gap: 12px; margin-top: 16px; }
.btn-cancel, .btn-confirm { flex: 1; height: 38px; border-radius: 6px; font-size: 14px; cursor: pointer; }
.btn-cancel { background: #fff; border: 1px solid #ddd; color: #333; }
.btn-confirm { background: #e74c3c; border: none; color: #fff; }
</style>