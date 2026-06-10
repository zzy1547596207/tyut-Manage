<template>
  <div class="detail-page">
    <div class="top-nav">
      <div class="back-btn" @click="goBack">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">信息采集批次</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="detail-body" v-if="data">
      <div class="section"><div class="section-title">证件照片</div><div class="section-sub">审核中不可修改</div>
        <div class="id-photo-upload readonly">
          <img v-if="data.idPhoto" :src="data.idPhoto" class="photo-preview" />
          <span v-else class="no-photo">未上传</span>
        </div>
      </div>
      <div class="section"><div class="section-title">生活照片</div><div class="section-sub">审核中不可修改</div>
        <div class="life-photo-grid">
          <div v-for="(_, i) in 5" :key="i" class="life-photo-slot" :class="{ filled: lifePhotoList[i] }">
            <img v-if="lifePhotoList[i]" :src="lifePhotoList[i]" class="photo-preview" />
            <span v-else class="slot-empty"></span>
          </div>
        </div>
      </div>
      <div class="section"><div class="section-title">必填信息</div>
        <div class="readonly-field"><span class="rl">最高学历</span><span class="rv">{{ data.education || '未填写' }}</span></div>
      </div>
      <div class="section"><div class="section-header"><span class="section-title">工作经历</span><span class="locked-tag">审核中不可编辑</span></div>
        <div v-if="!workList.length" class="empty-hint">暂无工作经历</div>
        <div v-for="(item, i) in workList" :key="i" class="experience-card readonly">
          <div class="exp-header"><span>工作经历 {{ i + 1 }}</span></div>
          <div class="exp-row"><div class="exp-field"><label>任职单位</label><span class="readonly-value">{{ item.company || '-' }}</span></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>开始日期</label><span class="readonly-value">{{ item.startDate || '-' }}</span></div><div class="exp-field"><label>结束日期</label><span class="readonly-value">{{ item.endDate || '-' }}</span></div></div>
          <div class="exp-row"><div class="exp-field"><label>任职类别</label><span class="readonly-value">{{ item.category || '-' }}</span></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>所带学生类别</label><span class="readonly-value">{{ item.studentType || '-' }}</span></div><div class="exp-field"><label>所带学生总数</label><span class="readonly-value">{{ item.studentCount || '-' }}</span></div></div>
          <div class="exp-row"><div class="exp-field"><label>所带学生年级</label><span class="readonly-value">{{ item.grade || '-' }}</span></div></div>
        </div>
      </div>
      <div class="section"><div class="section-header"><span class="section-title">学习经历</span><span class="locked-tag">审核中不可编辑</span></div>
        <div v-if="!studyList.length" class="empty-hint">暂无学习经历</div>
        <div v-for="(item, i) in studyList" :key="i" class="experience-card readonly">
          <div class="exp-header"><span>学习经历 {{ i + 1 }}</span></div>
          <div class="exp-row"><div class="exp-field"><label>学历类型</label><span class="readonly-value">{{ item.degreeType || '-' }}</span></div></div>
          <div class="exp-row"><div class="exp-field"><label>毕业院校</label><span class="readonly-value">{{ item.school || '-' }}</span></div></div>
          <div class="exp-row"><div class="exp-field"><label>专业</label><span class="readonly-value">{{ item.major || '-' }}</span></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>入学日期</label><span class="readonly-value">{{ item.entryDate || '-' }}</span></div><div class="exp-field"><label>毕业日期</label><span class="readonly-value">{{ item.gradDate || '-' }}</span></div></div>
        </div>
      </div>
      <div class="section"><div class="info-row"><span class="label">提交时间：</span>{{ data.submitTime || '-' }}</div></div>
      <div class="bottom-action"><button class="revoke-btn" @click="handleRevoke">撤销提醒</button></div>
    </div>

    <div v-else class="no-data">暂无提交数据，<a @click="goBack">返回列表</a></div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const data = ref(null)
const lifePhotoList = ref([])
const workList = ref([])
const studyList = ref([])

const base = computed(() => route.path.startsWith('/college') ? '/college' : '/collection')

function goBack() { router.push(base.value + '/batch') }

async function loadData() {
  try {
    const res = await request.get('/submission/my')
    if (res.data) {
      const batchId = Number(route.params.batchId)
      const sub = res.data.find(s => s.batchId === batchId)
      if (sub) {
        data.value = sub
        try { lifePhotoList.value = JSON.parse(sub.lifePhotos || '[]') } catch { lifePhotoList.value = [] }
        try { workList.value = JSON.parse(sub.workList || '[]') } catch { workList.value = [] }
        try { studyList.value = JSON.parse(sub.studyList || '[]') } catch { studyList.value = [] }
      }
    }
  } catch (e) { console.error('加载数据失败', e) }
}

onMounted(loadData)

async function handleRevoke() {
  try {
    await ElMessageBox.confirm('确定撤销本次审核申请吗？撤销后可重新编辑提交。', '撤销确认', {
      confirmButtonText: '确认撤销', cancelButtonText: '暂不撤销', type: 'warning'
    })
  } catch { return }

  if (!data.value || !data.value.id) {
    ElMessage.error('未找到提交记录')
    return
  }
  try {
    await request.post('/submission/revoke/' + data.value.id)
    ElMessage.success('已撤销审核，可重新填写')
    router.push(base.value + '/batch')
  } catch (e) {
    ElMessage.error('撤销失败: ' + (e.message || '未知错误'))
  }
}
</script>

<style scoped lang="scss">
.detail-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.detail-body { flex: 1; overflow-y: auto; padding: 12px; padding-bottom: 80px; }
.section { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 12px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; }
.section-sub { font-size: 11px; color: #999; margin-top: 4px; margin-bottom: 10px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.locked-tag { font-size: 11px; color: #e6a23c; background: #fdf6ec; padding: 2px 8px; border-radius: 4px; }
.id-photo-upload.readonly { width: 100px; height: 130px; border: 1.5px solid #e0e0e0; border-radius: 8px; display: flex; align-items: center; justify-content: center; overflow: hidden; .photo-preview { width: 100%; height: 100%; object-fit: cover; } .no-photo { color: #ccc; font-size: 12px; } }
.life-photo-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; .life-photo-slot:nth-child(4), .life-photo-slot:nth-child(5) { justify-self: center; } }
.life-photo-slot { width: 80px; height: 80px; border: 1.5px solid #e0e0e0; border-radius: 8px; display: flex; align-items: center; justify-content: center; overflow: hidden; .photo-preview { width: 100%; height: 100%; object-fit: cover; } .slot-empty { width: 100%; height: 100%; background: #fafafa; } }
.readonly-field { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; .rl { font-size: 13px; color: #999; } .rv { font-size: 13px; color: #333; } }
.experience-card.readonly { border: 1px solid #eee; border-radius: 8px; padding: 12px; margin-bottom: 10px; background: #fafafa; }
.exp-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; font-size: 13px; font-weight: 500; color: #333; }
.exp-row { margin-bottom: 8px; &.two-col { display: flex; gap: 10px; .exp-field { flex: 1; } } }
.exp-field { label { display: block; font-size: 11px; color: #999; margin-bottom: 3px; } .readonly-value { display: block; font-size: 12px; color: #666; padding: 6px 0; } }
.empty-hint { text-align: center; color: #ccc; font-size: 12px; padding: 16px 0; }
.info-row { font-size: 12px; .label { color: #999; } }
.bottom-action { position: sticky; bottom: 0; padding: 10px 14px; background: #fff; border-top: 1px solid #eee; }
.revoke-btn { width: 100%; height: 42px; border: 1.5px solid #e74c3c; border-radius: 8px; background: #fff; color: #e74c3c; font-size: 14px; font-weight: 500; cursor: pointer; }
.no-data { display: flex; align-items: center; justify-content: center; height: 200px; color: #999; font-size: 14px; a { color: #e74c3c; cursor: pointer; margin-left: 4px; } }
</style>