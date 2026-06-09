<template>
  <div class="profile-page">
    <!-- 审核中状态 -->
    <div v-if="profileStatus === 'reviewing'" class="status-page">
      <div class="illustration">
        <svg viewBox="0 0 200 180" xmlns="http://www.w3.org/2000/svg">
          <rect x="50" y="90" width="55" height="75" rx="6" fill="#e0e0e0" stroke="#ccc" stroke-width="1.5"/>
          <rect x="58" y="98" width="39" height="50" rx="3" fill="#fff" stroke="#ddd" stroke-width="1"/>
          <line x1="65" y1="110" x2="90" y2="110" stroke="#e0e0e0" stroke-width="2" stroke-linecap="round"/>
          <line x1="65" y1="118" x2="85" y2="118" stroke="#e0e0e0" stroke-width="2" stroke-linecap="round"/>
          <line x1="65" y1="126" x2="82" y2="126" stroke="#e0e0e0" stroke-width="2" stroke-linecap="round"/>
          <circle cx="48" cy="130" r="12" fill="#FFD5B8" stroke="#D4A574" stroke-width="1.5"/>
          <ellipse cx="45" cy="128" rx="2" ry="2.5" fill="#333"/>
          <ellipse cx="51" cy="125" rx="4" ry="3" fill="#f88"/>
          <ellipse cx="46" cy="120" rx="14" ry="7" fill="#555"/>
          <path d="M38 140 Q40 155 55 158" stroke="#FFD5B8" stroke-width="5" stroke-linecap="round" fill="none"/>
          <path d="M58 140 Q56 152 50 155" stroke="#FFD5B8" stroke-width="5" stroke-linecap="round" fill="none"/>
          <rect x="105" y="55" width="18" height="28" rx="2" fill="#90CAF9" stroke="#64B5F6" stroke-width="1"/>
          <rect x="108" y="62" width="12" height="14" rx="1" fill="#BBDEFB"/>
          <line x1="114" y1="52" x2="114" y2="42" stroke="#90CAF9" stroke-width="2.5" stroke-linecap="round"/>
          <circle cx="114" cy="38" r="6" fill="#42A5F5" opacity="0.3"/>
        </svg>
      </div>
      <p class="status-title">基本信息审核中</p>
      <p class="status-sub">审核通过展示最新数据</p>
      <a class="action-link" @click="handleRevoke">撤销申请</a>
    </div>

    <!-- 审核不通过状态 -->
    <div v-else-if="profileStatus === 'rejected'" class="status-page">
      <div class="illustration">
        <svg viewBox="0 0 200 180" xmlns="http://www.w3.org/2000/svg">
          <rect x="55" y="95" width="50" height="70" rx="5" fill="#e0e0e0" stroke="#ccc" stroke-width="1.5"/>
          <rect x="62" y="102" width="36" height="48" rx="3" fill="#fff" stroke="#ddd" stroke-width="1"/>
          <line x1="68" y1="112" x2="92" y2="112" stroke="#f5a0a0" stroke-width="2" stroke-linecap="round"/>
          <line x1="68" y1="120" x2="88" y2="120" stroke="#e0e0e0" stroke-width="2" stroke-linecap="round"/>
          <rect x="78" y="52" width="44" height="28" rx="4" fill="#fff" stroke="#f56c6c" stroke-width="2"/>
          <circle cx="100" cy="66" r="12" fill="none" stroke="#f56c6c" stroke-width="3"/>
          <line x1="92" y1="58" x2="108" y2="74" stroke="#f56c6c" stroke-width="3" stroke-linecap="round"/>
          <circle cx="100" cy="66" r="16" fill="#fff3f3" opacity="0.6"/>
          <circle cx="53" cy="135" r="10" fill="#FFD5B8" stroke="#D4A574" stroke-width="1.5"/>
          <ellipse cx="48" cy="128" rx="6" ry="3" fill="#333"/>
          <ellipse cx="55" cy="125" rx="4" ry="3" fill="#f88"/>
          <path d="M43 142 Q45 155 58 156" stroke="#FFD5B8" stroke-width="5" stroke-linecap="round" fill="none"/>
          <path d="M60 138 Q58 150 52 153" stroke="#FFD5B8" stroke-width="5" stroke-linecap="round" fill="none"/>
        </svg>
      </div>
      <p class="status-title">基本信息审核不通过</p>
      <p class="status-sub">{{ rejectReason || '审核不通过原因审核不通过原因' }}</p>
      <a class="action-link" @click="handleResubmit">重新提交</a>
    </div>

    <!-- 正常状态：完整档案 -->
    <template v-else>
      <div class="top-bar">
        <span class="update-time">更新时间：{{ updateTime }}</span>
        <button class="edit-btn" @click="handleEdit">编辑信息</button>
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
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter(); const route = useRoute(); const base = computed(() => route.path.startsWith('/college') ? '/college' : '/collection')
const updateTime = ref('')
const profileStatus = ref('')
const rejectReason = ref('')
const data = ref({ idPhoto:'',lifePhotos:[],name:'',education:'',campus:'',office:'',workList:[],studyList:[] })

function loadData() {
  try{const raw=localStorage.getItem('profile_data');if(raw){const s=JSON.parse(raw);data.value.idPhoto=s.idPhoto||'';data.value.lifePhotos=s.lifePhotos||[];data.value.name=s.name||'';data.value.education=s.education||'';data.value.campus=s.campus||'';data.value.office=s.office||'';data.value.workList=s.workList||[];data.value.studyList=s.studyList||[];updateTime.value=s.updateTime||'';profileStatus.value=s.status||'';rejectReason.value=s.rejectReason||'';return}}catch(e){}
  try{const raw=localStorage.getItem('batch_data_1');if(raw){const s=JSON.parse(raw);data.value.idPhoto=s.idPhoto||'';data.value.lifePhotos=s.lifePhotos||[];data.value.education=s.education||'';data.value.workList=s.workList||[];data.value.studyList=s.studyList||[];updateTime.value=s.submitTime||''}}catch(e){}
}

function handleEdit(){ router.push(base.value + '/profile/edit') }
function handleResubmit(){ router.push(base.value + '/profile/edit') }

async function handleRevoke() {
  await ElMessageBox.confirm('确定撤销本次审核申请吗？撤销后可重新编辑提交。','撤销确认',{confirmButtonText:'确认撤销',cancelButtonText:'暂不撤销',type:'warning'})
  try{const p=JSON.parse(localStorage.getItem('profile_data')||'{}');delete p.status;delete p.rejectReason;localStorage.setItem('profile_data',JSON.stringify(p))}catch(e){}
  profileStatus.value = ''
  try{const raw=localStorage.getItem('college_applications');if(raw){const apps=JSON.parse(raw).filter(a=>a.updateType!=='自主变更');localStorage.setItem('college_applications',JSON.stringify(apps))}}catch(e){}
  ElMessage.success('已撤销审核，可重新编辑')
}

onMounted(()=>{
  loadData()
  if(!localStorage.getItem('profile_data')&&!localStorage.getItem('batch_data_1')){
    const d={name:'张三',campus:'主校区',office:'行政楼302室',education:'硕士',updateTime:'2024-09-15 14:30:00',workList:[{company:'XX大学',startDate:'2023-09-01',endDate:'',category:'专职辅导员',studentType:'本科生',studentCount:'200',grade:'2023级'}],studyList:[{degreeType:'硕士',school:'北京大学',major:'教育学',entryDate:'2020-09-01',gradDate:'2023-07-01'},{degreeType:'本科',school:'武汉大学',major:'心理学',entryDate:'2016-09-01',gradDate:'2020-07-01'}]}
    localStorage.setItem('profile_data',JSON.stringify(d));loadData()
  }
})
</script>

<style scoped lang="scss">
.profile-page { }
/* 审核状态页 */
.status-page { text-align: center; padding-top: 60px; }
.status-page .illustration { width: 160px; margin: 0 auto 16px; }
.status-page .illustration svg { width: 100%; height: auto; }
.status-title { font-size: 16px; color: #333; font-weight: 500; margin-bottom: 6px; }
.status-sub { font-size: 12px; color: #999; margin-bottom: 20px; padding: 0 40px; }
.action-link { color: #e74c3c; font-size: 14px; cursor: pointer; text-decoration: none; font-weight: 500; }
/* 正常档案 */
.top-bar { display: flex; justify-content: space-between; align-items: center; padding: 10px 14px; background: #fff; }
.top-bar .update-time { font-size: 11px; color: #999; }
.top-bar .edit-btn { padding: 5px 14px; border: 1.5px solid #e74c3c; border-radius: 6px; background: #fff; color: #e74c3c; font-size: 12px; cursor: pointer; }
.profile-body { padding: 10px 14px 20px; }
.section { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 8px; }
.section-sub { font-size: 11px; color: #999; margin-bottom: 8px; }
.id-photo-wrap { width: 80px; height: 105px; border-radius: 8px; overflow: hidden; border: 1px solid #eee; }
.id-photo-wrap .id-photo { width: 100%; height: 100%; object-fit: cover; }
.id-photo-wrap .no-photo { display: flex; align-items: center; justify-content: center; height: 100%; color: #ccc; font-size: 12px; background: #fafafa; }
.life-photo-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.life-photo-grid .life-photo-slot:nth-child(4), .life-photo-grid .life-photo-slot:nth-child(5) { justify-self: center; }
.life-photo-slot { width: 70px; height: 70px; border-radius: 6px; overflow: hidden; border: 1px solid #eee; }
.life-photo-slot .photo-preview { width: 100%; height: 100%; object-fit: cover; }
.life-photo-slot .slot-empty { width: 100%; height: 100%; background: #fafafa; }
.info-list .info-item { display: flex; padding: 8px 0; border-bottom: 1px solid #f8f8f8; }
.info-list .info-item:last-child { border: none; }
.info-list .info-item .label { font-size: 12px; color: #999; width: 80px; flex-shrink: 0; }
.info-list .info-item .value { font-size: 13px; color: #333; }
.exp-card { background: #fafafa; border-radius: 8px; padding: 10px; margin-bottom: 8px; }
.exp-title { font-size: 12px; font-weight: 600; color: #333; margin-bottom: 6px; }
.exp-row { margin-bottom: 4px; font-size: 12px; display: flex; align-items: baseline; }
.exp-row.two-col .el { width: 60px; flex-shrink: 0; }
.exp-row.two-col .ev { width: 90px; flex-shrink: 0; }
.exp-row .el { color: #999; margin-right: 4px; }
.exp-row .ev { color: #555; }
.empty-hint { text-align: center; color: #ccc; font-size: 12px; padding: 12px 0; }
</style>