<template>
  <div class="edit-page">
    <div class="top-nav">
      <div class="back-btn" @click="handleCancel">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">个人信息编辑</span>
      <div class="nav-spacer"></div>
    </div>
    <div class="update-bar">更新时间：{{ updateTime }}</div>
    <div class="edit-body">
      <div class="section"><div class="section-title">证件照片</div><div class="section-sub">仅限1张，图片清晰，单张大小≤10M</div>
        <div class="id-photo-upload" @click="uploadIdPhoto">
          <template v-if="form.idPhoto"><img :src="form.idPhoto" class="photo-preview" /><div class="photo-mask" @click.stop="form.idPhoto = ''">×</div></template>
          <template v-else><svg viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5" width="28" height="28"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg></template>
        </div>
      </div>
      <div class="section"><div class="section-title">生活照片</div><div class="section-sub">最多5张，单张≤10M</div>
        <div class="life-photo-grid">
          <div v-for="(_, i) in 5" :key="i" class="life-photo-slot" :class="{ filled: form.lifePhotos[i] }" @click="!form.lifePhotos[i] ? uploadLifePhoto(i) : null">
            <template v-if="form.lifePhotos[i]"><img :src="form.lifePhotos[i]" class="photo-preview" /><div class="photo-mask" @click.stop="form.lifePhotos[i] = null">×</div></template>
            <template v-else><svg viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5" width="22" height="22"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg></template>
          </div>
        </div>
      </div>
      <div class="section"><div class="section-title">基本信息</div>
        <div class="field"><input v-model="form.name" placeholder="姓名" /></div>
        <div class="field"><input v-model="form.employeeNo" placeholder="工号" /></div>
        <div class="field"><input v-model="form.gender" placeholder="性别" /></div>
        <div class="field"><input v-model="form.birth" placeholder="出生年月" /></div>
        <div class="field"><input v-model="form.politicalStatus" placeholder="政治面貌" /></div>
        <div class="field"><input v-model="form.department" placeholder="所在单位" /></div>
        <div class="field">
          <select v-model="form.type" class="select-input">
            <option value="">辅导员类型</option>
            <option value="专职辅导员">专职辅导员</option>
            <option value="兼职辅导员">兼职辅导员</option>
            <option value="专任教师">专任教师</option>
          </select>
        </div>
        <div class="field"><input v-model="form.position" placeholder="职务" /></div>
        <div class="field">
          <select v-model="form.education" class="select-input">
            <option value="">请选择最高学历</option>
            <option value="博士">博士</option>
            <option value="硕士">硕士</option>
            <option value="本科">本科</option>
            <option value="大专">大专</option>
          </select>
        </div>
        <div class="field">
          <select v-model="form.degree" class="select-input">
            <option value="">请选择最高学位</option>
            <option value="博士">博士</option>
            <option value="硕士">硕士</option>
            <option value="学士">学士</option>
          </select>
        </div>
      </div>
      <div class="section"><div class="section-title">学工信息</div>
        <div class="field"><input v-model="form.campus" placeholder="校区" /></div>
        <div class="field"><input v-model="form.office" placeholder="办公地点" /></div>
        <div class="field"><input v-model="form.phone" placeholder="手机号" /></div>
      </div>
      <div class="section">
        <div class="section-header"><span class="section-title">工作经历</span><span class="add-btn" @click="goAddWork">+ 新增</span></div>
        <div v-if="form.workList.length===0" class="empty-hint">暂无工作经历，点击右上角新增</div>
        <div v-for="(item,i) in form.workList" :key="i" class="experience-card">
          <div class="exp-header"><span>工作经历 {{i+1}}</span><span class="del-btn" @click="removeWork(i)">删除</span></div>
          <div class="exp-row"><div class="exp-field"><label>任职单位</label><input v-model="item.company" placeholder="请输入" /></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>开始日期</label><input v-model="item.startDate" type="date" /></div><div class="exp-field"><label>结束日期</label><input v-model="item.endDate" type="date" /></div></div>
          <div class="exp-row"><div class="exp-field"><label>任职类别</label><input v-model="item.category" placeholder="如：专职辅导员" /></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>所带学生类别</label><select v-model="item.studentType"><option value="">请选择</option><option value="本科生">本科生</option><option value="硕士生">硕士生</option><option value="博士生">博士生</option></select></div><div class="exp-field"><label>所带学生总数</label><input v-model="item.studentCount" type="number" placeholder="请输入" /></div></div>
          <div class="exp-row"><div class="exp-field"><label>所带学生年级</label><input v-model="item.grade" placeholder="如：2024级" /></div></div>
        </div>
      </div>
      <div class="section">
        <div class="section-header"><span class="section-title">学习经历</span><span class="add-btn" @click="goAddStudy">+ 新增</span></div>
        <div v-if="form.studyList.length===0" class="empty-hint">暂无学习经历，点击右上角新增</div>
        <div v-for="(item,i) in form.studyList" :key="i" class="experience-card">
          <div class="exp-header"><span>学习经历 {{i+1}}</span><span class="del-btn" @click="removeStudy(i)">删除</span></div>
          <div class="exp-row"><div class="exp-field"><label>学历类型</label><select v-model="item.degreeType"><option value="">请选择</option><option value="博士">博士</option><option value="硕士">硕士</option><option value="本科">本科</option><option value="大专">大专</option></select></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>入学日期</label><input v-model="item.entryDate" type="date" /></div><div class="exp-field"><label>毕业日期</label><input v-model="item.gradDate" type="date" /></div></div>
          <div class="exp-row two-col"><div class="exp-field"><label>毕业院校</label><input v-model="item.school" placeholder="请输入" /></div><div class="exp-field"><label>专业</label><input v-model="item.major" placeholder="请输入" /></div></div>
        </div>
      </div>
      <div class="bottom-actions">
        <button class="cancel-btn" @click="handleCancel">取消</button>
        <button class="submit-btn" @click="handleSubmit">提交审核</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted, onActivated, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const router = useRouter(); const route = useRoute()
const base = computed(() => route.path.startsWith('/college') ? '/college' : '/collection')
const STORAGE_KEY = 'profile_edit_' + (localStorage.getItem('username') || '')

const updateTime = ref(new Date().toLocaleString())

const form = reactive({
  idPhoto: '', lifePhotos: [],
  name: '', employeeNo: '', gender: '', birth: '', politicalStatus: '',
  department: '', type: '', position: '', education: '', degree: '',
  campus: '', office: '', phone: '',
  workList: [], studyList: []
})

function saveToStorage() {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify({ ...form })) } catch (e) {}
}

function loadFromStorage() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (raw) {
      const data = JSON.parse(raw)
      Object.keys(data).forEach(k => { if (k in form) form[k] = data[k] })
    }
  } catch (e) {}
}

async function loadProfile() {
  try {
    const res = await request.get('/profile/my')
    if (res.data) {
      const p = res.data
      form.name = p.name || ''
      form.employeeNo = p.employeeNo || ''
      form.gender = p.gender || ''
      form.birth = p.birth || ''
      form.politicalStatus = p.politicalStatus || ''
      form.department = p.department || ''
      form.type = p.type || ''
      form.position = p.position || ''
      form.education = p.education || ''
      form.degree = p.degree || ''
      form.campus = p.campus || ''
      form.office = p.office || ''
      form.phone = p.phone || ''
      form.idPhoto = p.idPhoto || ''
      try { form.lifePhotos = JSON.parse(p.lifePhotos || '[]') } catch { form.lifePhotos = [] }
      try { form.workList = JSON.parse(p.workList || '[]') } catch { form.workList = [] }
      try { form.studyList = JSON.parse(p.studyList || '[]') } catch { form.studyList = [] }
      if (p.updateTime) updateTime.value = p.updateTime
    }
  } catch (e) { console.error('加载个人信息失败', e) }
}

onMounted(() => { loadProfile().then(() => loadFromStorage()) })
onActivated(() => loadFromStorage())

watch(form, () => saveToStorage(), { deep: true })

function uploadIdPhoto() {
  const inp = document.createElement('input'); inp.type = 'file'; inp.accept = 'image/*'
  inp.onchange = (e) => { const f = e.target.files[0]; if (f) { const r = new FileReader(); r.onload = (ev) => { form.idPhoto = ev.target.result }; r.readAsDataURL(f) } }
  inp.click()
}

function uploadLifePhoto(i) {
  const inp = document.createElement('input'); inp.type = 'file'; inp.accept = 'image/*'
  inp.onchange = (e) => { const f = e.target.files[0]; if (f) { const r = new FileReader(); r.onload = (ev) => { form.lifePhotos[i] = ev.target.result }; r.readAsDataURL(f) } }
  inp.click()
}

function removeWork(i) { form.workList.splice(i, 1) }
function removeStudy(i) { form.studyList.splice(i, 1) }

function goAddWork() { saveToStorage(); router.push(base.value + '/work/add?source=profile') }
function goAddStudy() { saveToStorage(); router.push(base.value + '/study/add?source=profile') }

function handleCancel() { localStorage.removeItem(STORAGE_KEY); router.push(base.value + '/profile') }

async function handleSubmit() {
  if (!form.name) { ElMessage.warning('请填写姓名'); return }
  if (!form.education) { ElMessage.warning('请选择最高学历'); return }

  const data = {
    name: form.name, employeeNo: form.employeeNo, gender: form.gender, birth: form.birth,
    politicalStatus: form.politicalStatus, department: form.department,
    type: form.type, position: form.position, education: form.education,
    degree: form.degree, campus: form.campus, office: form.office,
    phone: form.phone, idPhoto: form.idPhoto,
    lifePhotos: JSON.stringify(form.lifePhotos.filter(Boolean)),
    workList: JSON.stringify(form.workList),
    studyList: JSON.stringify(form.studyList)
  }

  try {
    await request.post('/profile/submit', data)
    localStorage.removeItem(STORAGE_KEY)
    await ElMessageBox.alert('档案修改已提交审核，请等待学院管理员审核。', '提交成功', { type: 'success' })
    router.push(base.value + '/profile')
  } catch (e) {
    ElMessage.error('提交失败: ' + (e.message || '未知错误'))
  }
}
</script>

<style scoped lang="scss">
.edit-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.update-bar { background: #fff; padding: 6px 14px; font-size: 11px; color: #999; border-top: 1px solid #f5f5f5; }
.edit-body { flex: 1; overflow-y: auto; padding: 10px 14px; padding-bottom: 80px; }
.section { background: #fff; border-radius: 12px; padding: 14px; margin-bottom: 10px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 10px; }
.section-sub { font-size: 11px; color: #999; margin-bottom: 8px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; .add-btn { font-size: 12px; color: #e74c3c; cursor: pointer; font-weight: 500; } }
.id-photo-upload { width: 100px; height: 130px; border: 1.5px dashed #ddd; border-radius: 8px; display: flex; align-items: center; justify-content: center; cursor: pointer; position: relative; .photo-preview { width: 100%; height: 100%; object-fit: cover; border-radius: 8px; } .photo-mask { position: absolute; top: -6px; right: -6px; width: 22px; height: 22px; background: #e74c3c; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 14px; cursor: pointer; } }
.life-photo-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; .life-photo-slot:nth-child(4), .life-photo-slot:nth-child(5) { justify-self: center; } }
.life-photo-slot { width: 80px; height: 80px; border: 1.5px dashed #ddd; border-radius: 6px; display: flex; align-items: center; justify-content: center; cursor: pointer; position: relative; &.filled { border-style: solid; border-color: #e0e0e0; } .photo-preview { width: 100%; height: 100%; object-fit: cover; border-radius: 6px; } .photo-mask { position: absolute; top: -6px; right: -6px; width: 20px; height: 20px; background: #e74c3c; color: #fff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 12px; cursor: pointer; } }
.field { margin-bottom: 10px; input, select { width: 100%; height: 38px; border: 1px solid #eee; border-radius: 6px; padding: 0 10px; font-size: 13px; outline: none; background: #fff; &:focus { border-color: #e74c3c; } } }
.select-input { width: 100%; height: 40px; border: 1px solid #ddd; border-radius: 6px; padding: 0 12px; font-size: 13px; color: #333; background: #fff; outline: none; appearance: none; &:focus { border-color: #e74c3c; } }
.experience-card { border: 1px solid #eee; border-radius: 8px; padding: 12px; margin-bottom: 10px; }
.exp-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; font-size: 13px; font-weight: 500; color: #333; }
.del-btn { font-size: 11px; color: #e74c3c; cursor: pointer; }
.exp-row { margin-bottom: 8px; &.two-col { display: flex; gap: 10px; .exp-field { flex: 1; } } }
.exp-field { label { display: block; font-size: 11px; color: #999; margin-bottom: 3px; } input, select { width: 100%; height: 34px; border: 1px solid #eee; border-radius: 6px; padding: 0 10px; font-size: 12px; outline: none; background: #fff; &:focus { border-color: #e74c3c; } } }
.empty-hint { text-align: center; color: #ccc; font-size: 12px; padding: 16px 0; }
.bottom-actions { position: sticky; bottom: 0; padding: 10px 14px; background: #fff; display: flex; gap: 12px; border-top: 1px solid #eee; .cancel-btn, .submit-btn { flex: 1; height: 42px; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; } .cancel-btn { background: #fff; border: 1px solid #ddd; color: #333; } .submit-btn { background: #e74c3c; border: none; color: #fff; } }
</style>