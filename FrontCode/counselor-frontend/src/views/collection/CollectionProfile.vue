<template>
  <div class="profile-page">
    <div class="top-bar">
      <span class="update-time">更新时间：{{ updateTime }}</span>
      <button v-if="isReviewing" class="edit-btn disabled" disabled>审核中</button>
      <button v-else class="edit-btn" @click="handleEdit">编辑信息</button>
    </div>

    <div class="profile-body">
      <div class="section"><div class="section-title">证件照片</div>
        <div class="id-photo-wrap"><img v-if="data.idPhoto" :src="data.idPhoto" class="id-photo" /><div v-else class="no-photo">暂无证件照</div></div>
      </div>
      <div class="section"><div class="section-title">生活照片</div><div class="section-sub">最多5张，单张≤10M</div>
        <div class="life-photo-grid">
          <div v-for="(_, i) in 5" :key="i" class="life-photo-slot" :class="{ filled: data.lifePhotos && data.lifePhotos[i] }">
            <img v-if="data.lifePhotos && data.lifePhotos[i]" :src="data.lifePhotos[i]" class="photo-preview" /><div v-else class="slot-empty"></div>
          </div>
        </div>
      </div>
      <div class="section"><div class="section-title">基本信息</div>
        <div class="info-list">
          <div class="info-item"><span class="label">辅导员姓名</span><span class="value">{{ data.name || '未填写' }}</span></div>
          <div class="info-item"><span class="label">最高学历</span><span class="value">{{ data.education || '未填写' }}</span></div>
        </div>
      </div>
      <div class="section"><div class="section-title">学工信息</div>
        <div class="info-list">
          <div class="info-item"><span class="label">校区</span><span class="value">{{ data.campus || '未填写' }}</span></div>
          <div class="info-item"><span class="label">办公地点</span><span class="value">{{ data.office || '未填写' }}</span></div>
        </div>
      </div>
      <div class="section"><div class="section-title">工作经历</div>
        <div v-if="!data.workList || data.workList.length === 0" class="empty-hint">暂无工作经历</div>
        <div v-for="(item, i) in (data.workList || [])" :key="i" class="exp-card">
          <div class="exp-title">工作经历 {{ i + 1 }}</div>
          <div class="exp-row"><span class="el">任职单位</span><span class="ev">{{ item.company || '-' }}</span></div>
          <div class="exp-row two-col"><span class="el">开始日期</span><span class="ev">{{ item.startDate || '-' }}</span><span class="el">结束日期</span><span class="ev">{{ item.endDate || '-' }}</span></div>
          <div class="exp-row"><span class="el">任职类别</span><span class="ev">{{ item.category || '-' }}</span></div>
          <div class="exp-row two-col"><span class="el">所带学生类别</span><span class="ev">{{ item.studentType || '-' }}</span><span class="el">所带学生总数</span><span class="ev">{{ item.studentCount || '-' }}</span></div>
          <div class="exp-row"><span class="el">所带学生年级</span><span class="ev">{{ item.grade || '-' }}</span></div>
        </div>
      </div>
      <div class="section"><div class="section-title">学习经历</div>
        <div v-for="(item, i) in (data.studyList || [])" :key="i" class="exp-card">
          <div class="exp-title">学习经历 {{ i + 1 }}</div>
          <div class="exp-row"><span class="el">学历类型</span><span class="ev">{{ item.degreeType || '-' }}</span></div>
          <div class="exp-row"><span class="el">毕业院校</span><span class="ev">{{ item.school || '-' }}</span></div>
          <div class="exp-row"><span class="el">专业</span><span class="ev">{{ item.major || '-' }}</span></div>
          <div class="exp-row two-col"><span class="el">入学日期</span><span class="ev">{{ item.entryDate || '-' }}</span><span class="el">毕业日期</span><span class="ev">{{ item.gradDate || '-' }}</span></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const updateTime = ref('')
const isReviewing = ref(false)

const data = ref({ idPhoto:'',lifePhotos:[],name:'',education:'',campus:'',office:'',workList:[],studyList:[] })

function loadData() {
  try {
    const raw = localStorage.getItem('profile_data')
    if (raw) {
      const saved = JSON.parse(raw)
      data.value.idPhoto = saved.idPhoto || ''
      data.value.lifePhotos = saved.lifePhotos || []
      data.value.name = saved.name || ''
      data.value.education = saved.education || ''
      data.value.campus = saved.campus || ''
      data.value.office = saved.office || ''
      data.value.workList = saved.workList || []
      data.value.studyList = saved.studyList || []
      updateTime.value = saved.updateTime || ''
      isReviewing.value = saved.status === 'reviewing'
      return
    }
  } catch (e) {}

  try {
    const raw = localStorage.getItem('batch_data_1')
    if (raw) {
      const saved = JSON.parse(raw)
      data.value.idPhoto = saved.idPhoto || ''
      data.value.lifePhotos = saved.lifePhotos || []
      data.value.education = saved.education || ''
      data.value.workList = saved.workList || []
      data.value.studyList = saved.studyList || []
      updateTime.value = saved.submitTime || ''
    }
  } catch (e) {}
}

function handleEdit() {
  router.push('/collection/profile/edit')
}

onMounted(() => {
  loadData()
  if (!localStorage.getItem('profile_data') && !localStorage.getItem('batch_data_1')) {
    const demo = {
      name:'张三',campus:'主校区',office:'行政楼302室',education:'硕士',updateTime:'2024-09-15 14:30:00',
      workList:[{ company:'XX大学',startDate:'2023-09-01',endDate:'',category:'专职辅导员',studentType:'本科生',studentCount:'200',grade:'2023级' }],
      studyList:[{ degreeType:'硕士',school:'北京大学',major:'教育学',entryDate:'2020-09-01',gradDate:'2023-07-01' },{ degreeType:'本科',school:'武汉大学',major:'心理学',entryDate:'2016-09-01',gradDate:'2020-07-01' }]
    }
    localStorage.setItem('profile_data', JSON.stringify(demo))
    loadData()
  }
})
</script>

<style scoped lang="scss">
.profile-page { height: 100%; display: flex; flex-direction: column; }
.top-bar { display: flex; justify-content: space-between; align-items: center; padding: 10px 14px; background: #fff; flex-shrink: 0; .update-time { font-size: 11px; color: #999; } .edit-btn { padding: 5px 14px; border: 1.5px solid #e74c3c; border-radius: 6px; background: #fff; color: #e74c3c; font-size: 12px; cursor: pointer; &.disabled { border-color: #e6a23c; color: #e6a23c; background: #fdf6ec; cursor: not-allowed; } } }
.profile-body { flex: 1; overflow-y: auto; padding: 10px 14px 20px; }
.section { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 8px; }
.section-sub { font-size: 11px; color: #999; margin-bottom: 8px; }
.id-photo-wrap { width: 80px; height: 105px; border-radius: 8px; overflow: hidden; border: 1px solid #eee; .id-photo { width: 100%; height: 100%; object-fit: cover; } .no-photo { display: flex; align-items: center; justify-content: center; height: 100%; color: #ccc; font-size: 12px; background: #fafafa; } }
.life-photo-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; .life-photo-slot:nth-child(4), .life-photo-slot:nth-child(5) { justify-self: center; } }
.life-photo-slot { width: 70px; height: 70px; border-radius: 6px; overflow: hidden; border: 1px solid #eee; .photo-preview { width: 100%; height: 100%; object-fit: cover; } .slot-empty { width: 100%; height: 100%; background: #fafafa; } }
.info-list { .info-item { display: flex; padding: 8px 0; border-bottom: 1px solid #f8f8f8; &:last-child { border: none; } .label { font-size: 12px; color: #999; width: 80px; flex-shrink: 0; } .value { font-size: 13px; color: #333; } } }
.exp-card { background: #fafafa; border-radius: 8px; padding: 10px; margin-bottom: 8px; }
.exp-title { font-size: 12px; font-weight: 600; color: #333; margin-bottom: 6px; }
.exp-row { margin-bottom: 4px; font-size: 12px; display: flex; align-items: baseline; &.two-col { .el { width: 60px; flex-shrink: 0; } .ev { width: 90px; flex-shrink: 0; } } .el { color: #999; margin-right: 4px; } .ev { color: #555; } }
.empty-hint { text-align: center; color: #ccc; font-size: 12px; padding: 12px 0; }
</style>