<template>
  <div class="contacts-page">
    <div class="search-bar">
      <input v-model="keyword" class="search-input" placeholder="请输入姓名/工号查询" @keyup.enter="doSearch" />
      <button class="search-btn" @click="doSearch">搜索</button>
    </div>
    <div class="list-body">
      <div v-if="filteredList.length === 0" class="empty-hint">暂无辅导员数据</div>
      <div v-for="item in filteredList" :key="item.id" class="person-card">
        <div class="card-top">
          <div class="avatar">&#x1f464;</div>
          <div class="user-info">
            <div class="name-row">{{ item.name }} <span class="emp-no">{{ item.employeeNo }}</span></div>
            <div class="sub-row">{{ item.department }} · {{ item.type }}</div>
          </div>
          <div class="phone-icon">&#x1f4de;</div>
        </div>
        <div class="card-fields">
          <div class="field"><span>职务：</span>{{ item.position }}</div>
          <div class="field"><span>校区：</span>{{ item.campus }}</div>
          <div class="field"><span>最高学历：</span>{{ item.education }}</div>
          <div class="field"><span>办公地点：</span>{{ item.office }}</div>
        </div>
        <div class="card-arrow" @click="viewDetail(item)">查看档案 &gt;</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const keyword = ref('')
const list = ref([])

const filteredList = computed(() => {
  if (!keyword.value.trim()) return list.value
  const kw = keyword.value.trim()
  return list.value.filter(item => item.name.includes(kw) || item.employeeNo.includes(kw))
})

function doSearch() {}
function viewDetail(item) { ElMessage.info('档案详情功能开发中：' + item.name) }

onMounted(() => {
  list.value = [
    { id: 1, name: '张三', employeeNo: '11004', department: 'A学院', type: '专职辅导员', position: '副书记', campus: '主校区', education: '硕士', office: '行政楼301' },
    { id: 2, name: '李四', employeeNo: '11005', department: 'A学院', type: '专职辅导员', position: '辅导员', campus: '东校区', education: '本科', office: '教学楼B201' },
    { id: 3, name: '王五', employeeNo: '11006', department: 'A学院', type: '兼职辅导员', position: '辅导员', campus: '主校区', education: '博士', office: '行政楼205' }
  ]
})
</script>

<style scoped lang="scss">
.contacts-page { }
.search-bar { display: flex; gap: 8px; padding: 10px 14px; background: #fff; }
.search-input { flex: 1; height: 36px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; outline: none; background: #fff; &:focus { border-color: #e74c3c; } }
.search-btn { width: 56px; height: 36px; border: none; border-radius: 6px; background: #e74c3c; color: #fff; font-size: 13px; cursor: pointer; }
.list-body { padding: 10px 14px; }
.empty-hint { text-align: center; color: #ccc; font-size: 14px; padding: 60px 0; }
.person-card { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }
.card-top { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; }
.avatar { font-size: 28px; flex-shrink: 0; }
.user-info { flex: 1; .name-row { font-size: 14px; font-weight: 600; color: #333; } .emp-no { font-size: 12px; color: #999; font-weight: 400; margin-left: 6px; } .sub-row { font-size: 11px; color: #999; margin-top: 2px; } }
.phone-icon { font-size: 20px; cursor: pointer; }
.card-fields { margin-bottom: 8px; .field { font-size: 12px; color: #666; margin-bottom: 3px; span { color: #999; } } }
.card-arrow { text-align: right; font-size: 12px; color: #e74c3c; cursor: pointer; }
</style>