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

    <!-- 类型分布 - 环形饼图 -->
    <div class="chart-section">
      <div class="section-header">
        <span class="section-title">辅导员类型分布</span>
        <button class="filter-btn" @click="showFilter=!showFilter">+ 添加条件</button>
      </div>
      <div class="pie-wrap">
        <svg viewBox="0 0 160 160" class="donut-chart">
          <circle cx="80" cy="80" r="54" fill="none" stroke="#f0f0f0" stroke-width="18" />
          <template v-for="(item, i) in typeData" :key="'t'+i">
            <circle cx="80" cy="80" r="54" fill="none" :stroke="typeColors[i%typeColors.length]"
              stroke-width="18" :stroke-dasharray="item.dash + ' ' + (339 - item.dash)"
              :stroke-dashoffset="typeDashOffset(i)" stroke-linecap="round"
              transform="rotate(-90 80 80)" />
          </template>
        </svg>
        <div class="pie-legend">
          <div v-for="(item, i) in typeData" :key="'tl'+i" class="legend-item">
            <span class="legend-dot" :style="{ background: typeColors[i%typeColors.length] }"></span>
            <span class="legend-label">{{ item.name }}</span>
            <span class="legend-pct">{{ item.percent }}%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 学历分布 -->
    <div class="chart-section">
      <div class="section-title">辅导员学历分布</div>
      <div class="pie-wrap">
        <svg viewBox="0 0 160 160" class="donut-chart">
          <circle cx="80" cy="80" r="54" fill="none" stroke="#f0f0f0" stroke-width="18" />
          <template v-for="(item, i) in eduData" :key="'e'+i">
            <circle cx="80" cy="80" r="54" fill="none" :stroke="eduColors[i%eduColors.length]"
              stroke-width="18" :stroke-dasharray="item.dash + ' ' + (339 - item.dash)"
              :stroke-dashoffset="eduDashOffset(i)" stroke-linecap="round"
              transform="rotate(-90 80 80)" />
          </template>
        </svg>
        <div class="pie-legend">
          <div v-for="(item, i) in eduData" :key="'el'+i" class="legend-item">
            <span class="legend-dot" :style="{ background: eduColors[i%eduColors.length] }"></span>
            <span class="legend-label">{{ item.name }}</span>
            <span class="legend-pct">{{ item.percent }}%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 学院分布（按筛选维度） -->
    <div class="chart-section">
      <div class="section-title">各学院辅导员情况</div>
      <div class="dept-filter-bar">
        <span v-for="dim in dims" :key="dim" class="dept-filter-tag" :class="{ active: deptFilter === dim }" @click="deptFilter = dim">{{ dim }}</span>
      </div>
      <div v-for="(item, i) in deptData" :key="i" class="dept-card" @click="viewDeptDetail(item)">
        <span class="dept-name">{{ item.name }}</span>
        <span class="dept-count">{{ item.count }} 人</span>
      </div>
    </div>

    <!-- 筛选弹窗 -->
    <div v-if="showFilter" class="dialog-overlay" @click.self="showFilter=false">
      <div class="dialog-box">
        <div class="dialog-title">条件选择</div>
        <div class="filter-options">
          <div v-for="dim in filterDims" :key="dim.key" class="filter-option" :class="{ active: filterKey === dim.key }" @click="selectFilterDim(dim.key)">
            {{ dim.label }}
            <span class="opt-count">{{ getDimCount(dim.key) }} 条</span>
          </div>
        </div>
        <!-- 二级选择：维度值 -->
        <div v-if="filterValues.length > 0" class="filter-values">
          <div class="fv-title">选择{{ filterDims.find(d=>d.key===filterKey)?.label || '' }}</div>
          <div class="fv-list">
            <div v-for="val in filterValues" :key="val" class="fv-item" :class="{ active: filterVal === val }" @click="filterVal = val">
              {{ val }} <span class="fv-count">{{ getValCount(filterKey, val) }}</span>
            </div>
          </div>
        </div>
        <div class="dialog-btns">
          <button class="btn-cancel" @click="showFilter=false; filterKey='gender'; filterVal=''; filterValues=[]">取消</button>
          <button class="btn-confirm" @click="doFilter">提交</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '@/utils/request'

const dims = ['性别', '类型', '政治面貌', '学历']
const filterDims = [
  { key: 'gender', label: '性别' },
  { key: 'politicalStatus', label: '政治面貌' },
  { key: 'education', label: '最高学历' },
  { key: 'degree', label: '最后学位' }
]
const typeColors = ['#e74c3c', '#3498db', '#f39c12', '#2ecc71', '#9b59b6']
const eduColors = ['#3498db', '#2ecc71', '#f39c12', '#e74c3c']

const allCounselors = ref([])
const filterKey = ref('gender')
const filterVal = ref('')
const filterValues = ref([])
const showFilter = ref(false)
const deptFilter = ref('性别')
const activeFilter = ref({ key: '', val: '' })

const displayData = ref([])
const totalCount = computed(() => displayData.value.length)
const maleCount = computed(() => displayData.value.filter(c => c.gender === '男').length)
const femaleCount = computed(() => displayData.value.filter(c => c.gender === '女').length)

const CIRCUM = 2 * Math.PI * 54

const typeData = computed(() => buildSegments('type'))
const eduData = computed(() => buildSegments('education'))
const deptData = computed(() => {
  const key = deptFilterMap[deptFilter.value] || 'department'
  const map = {}
  displayData.value.forEach(c => {
    const val = c[key] || '未知'
    map[val] = (map[val] || 0) + 1
  })
  return Object.entries(map).map(([name, count]) => ({ name, count })).sort((a,b) => b.count - a.count)
})

const deptFilterMap = { '性别': 'department', '类型': 'type', '政治面貌': 'politicalStatus', '学历': 'education' }

function buildSegments(field) {
  const map = {}
  displayData.value.forEach(c => {
    const val = c[field] || '未知'
    map[val] = (map[val] || 0) + 1
  })
  const items = Object.entries(map).map(([name, count]) => ({
    name, count,
    percent: totalCount.value > 0 ? Math.round(count / totalCount.value * 100) : 0,
    dash: 0
  }))
  items.forEach(item => {
    item.dash = item.percent / 100 * CIRCUM
    if (item.percent >= 99.5) item.dash = CIRCUM - 0.3
  })
  return items
}

function typeDashOffset(i) {
  let acc = 0
  for (let j = 0; j < i; j++) acc += typeData.value[j].dash
  return -acc
}
function eduDashOffset(i) {
  let acc = 0
  for (let j = 0; j < i; j++) acc += eduData.value[j].dash
  return -acc
}

function getDimCount(key) {
  return new Set(allCounselors.value.map(c => c[key]).filter(Boolean)).size
}

function getValCount(key, val) {
  return allCounselors.value.filter(c => c[key] === val).length
}

function selectFilterDim(key) {
  filterKey.value = key
  filterVal.value = ''
  const vals = new Set(allCounselors.value.map(c => c[key]).filter(Boolean))
  filterValues.value = [...vals]
}

async function loadData() {
  try {
    const res = await request.get('/counselors/list')
    allCounselors.value = res.data || []
    displayData.value = [...allCounselors.value]
  } catch (e) { console.error('加载数据失败', e) }
}

onMounted(loadData)

function doFilter() {
  showFilter.value = false
  if (filterVal.value) {
    displayData.value = allCounselors.value.filter(c => c[filterKey.value] === filterVal.value)
    activeFilter.value = { key: filterKey.value, val: filterVal.value }
  } else {
    displayData.value = [...allCounselors.value]
  }
  // Reset secondary selection
  filterValues.value = []
  filterVal.value = ''
}

function viewDeptDetail(item) {
  // 点击学院卡片可进一步查看详情（预留）
}
</script>

<style scoped lang="scss">
.admin-stats { background: #f5f5f5; min-height: 100vh; padding-bottom: 80px; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; position: sticky; top: 0; z-index: 10; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; } }
.header-bar { padding: 10px 14px; background: #fdf0f0; }
.total-info { font-size: 13px; color: #666; b { color: #e74c3c; } }
.metric-cards { display: flex; gap: 10px; padding: 12px 14px; background: #fff; }
.metric-card { flex: 1; border-radius: 12px; padding: 14px 10px; text-align: center; }
.metric-card.pink { background: #fef0f0; } .metric-card.blue { background: #f0f4fe; } .metric-card.green { background: #f0fef0; }
.metric-num { font-size: 26px; font-weight: 700; }
.metric-card.pink .metric-num { color: #e74c3c; } .metric-card.blue .metric-num { color: #3498db; } .metric-card.green .metric-num { color: #2ecc71; }
.metric-label { font-size: 11px; color: #999; margin-top: 4px; }
.chart-section { background: #fff; margin: 10px 14px; border-radius: 12px; padding: 14px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 12px; }
.filter-btn { padding: 4px 12px; border: 1px solid #e74c3c; border-radius: 14px; background: #fff; color: #e74c3c; font-size: 12px; cursor: pointer; }
.pie-wrap { display: flex; align-items: center; gap: 20px; }
.donut-chart { width: 120px; height: 120px; flex-shrink: 0; }
.pie-legend { flex: 1; display: flex; flex-direction: column; gap: 8px; }
.legend-item { display: flex; align-items: center; gap: 8px; font-size: 12px; }
.legend-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
.legend-label { color: #666; flex: 1; }
.legend-pct { color: #333; font-weight: 600; min-width: 32px; text-align: right; }
.dept-filter-bar { display: flex; gap: 6px; margin-bottom: 12px; flex-wrap: wrap; }
.dept-filter-tag { padding: 4px 12px; border: 1px solid #ddd; border-radius: 14px; font-size: 12px; color: #666; cursor: pointer; }
.dept-filter-tag.active { border-color: #e74c3c; color: #e74c3c; background: #fef0f0; }
.dept-card { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #f5f5f5; cursor: pointer; }
.dept-name { font-size: 13px; color: #333; }
.dept-count { font-size: 13px; color: #e74c3c; font-weight: 600; }
.dialog-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 999; }
.dialog-box { background: #fff; border-radius: 12px; padding: 20px; width: 320px; max-height: 80vh; overflow-y: auto; }
.dialog-title { font-size: 16px; font-weight: 600; text-align: center; margin-bottom: 16px; }
.filter-options { display: flex; flex-direction: column; gap: 8px; }
.filter-option { padding: 10px 14px; border: 1.5px solid #eee; border-radius: 8px; font-size: 14px; cursor: pointer; display: flex; justify-content: space-between; }
.filter-option.active { border-color: #e74c3c; color: #e74c3c; background: #fef0f0; }
.opt-count { color: #999; font-size: 12px; }
.filter-values { margin-top: 12px; border-top: 1px solid #eee; padding-top: 12px; }
.fv-title { font-size: 13px; color: #666; margin-bottom: 8px; }
.fv-list { display: flex; flex-wrap: wrap; gap: 6px; }
.fv-item { padding: 6px 14px; border: 1px solid #ddd; border-radius: 16px; font-size: 12px; cursor: pointer; display: flex; align-items: center; gap: 4px; }
.fv-item.active { border-color: #e74c3c; color: #e74c3c; background: #fef0f0; }
.fv-count { color: #999; font-size: 11px; }
.dialog-btns { display: flex; gap: 12px; margin-top: 16px; }
.btn-cancel, .btn-confirm { flex: 1; height: 38px; border-radius: 6px; font-size: 14px; cursor: pointer; }
.btn-cancel { background: #fff; border: 1px solid #ddd; color: #333; }
.btn-confirm { background: #e74c3c; border: none; color: #fff; }
</style>