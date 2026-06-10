<template>
  <div class="add-page">
    <div class="top-nav">
      <div class="back-btn" @click="$router.back()">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">新增工作经历</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="form-body">
      <!-- 任职所在单位 -->
      <div class="field required">
        <label>任职所在单位 <span class="star">*</span></label>
        <select v-model="form.company">
          <option value="">请选择任职所在单位</option>
          <option value="XX大学">XX大学</option>
          <option value="YY学院">YY学院</option>
          <option value="ZZ大学">ZZ大学</option>
        </select>
      </div>

      <!-- 任职开始时间 -->
      <div class="field required">
        <label>任职开始时间 <span class="star">*</span></label>
        <input v-model="form.startDate" type="date" placeholder="请选择任职开始时间" />
      </div>

      <!-- 任职结束时间 -->
      <div class="field required">
        <label>任职结束时间 <span class="star">*</span></label>
        <input v-model="form.endDate" type="date" placeholder="请选择任职结束时间" />
      </div>

      <!-- 任职类别 -->
      <div class="field required">
        <label>任职类别 <span class="star">*</span></label>
        <select v-model="form.category">
          <option value="">请选择任职类别</option>
          <option value="行政干部">行政干部</option>
          <option value="专职辅导员">专职辅导员</option>
          <option value="兼职辅导员">兼职辅导员</option>
          <option value="团委干部">团委干部</option>
        </select>
      </div>

      <!-- 所带学生类别 -->
      <div class="field required">
        <label>任职所带学生类别 <span class="star">*</span></label>
        <select v-model="form.studentType">
          <option value="">请选择任职所带学生类别</option>
          <option value="本科生">本科生</option>
          <option value="硕士生">硕士生</option>
          <option value="博士生">博士生</option>
        </select>
      </div>

      <!-- 所带学生总数 -->
      <div class="field required">
        <label>任职所带学生总数 <span class="star">*</span></label>
        <select v-model="form.studentCount">
          <option value="">请选择任职所带学生总数</option>
          <option value="50人以下">50人以下</option>
          <option value="50-100人">50-100人</option>
          <option value="100-200人">100-200人</option>
          <option value="200-500人">200-500人</option>
          <option value="500人以上">500人以上</option>
        </select>
      </div>

      <!-- 所带学生年级 -->
      <div class="field required">
        <label>任职所带学生年级 <span class="star">*</span></label>
        <select v-model="form.grade">
          <option value="">请选择任职所带学生年级</option>
          <option value="2024级">2024级</option>
          <option value="2023级">2023级</option>
          <option value="2022级">2022级</option>
          <option value="2021级">2021级</option>
          <option value="2020级">2020级</option>
        </select>
      </div>

      <!-- 底部按钮 -->
      <div class="bottom-actions">
        <button class="cancel-btn" @click="$router.back()">取消</button>
        <button class="submit-btn" @click="handleSubmit">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const form = reactive({
  company: '',
  startDate: '',
  endDate: '',
  category: '',
  studentType: '',
  studentCount: '',
  grade: ''
})

function handleSubmit() {
  const required = [
    { key: 'company', label: '任职所在单位' },
    { key: 'startDate', label: '任职开始时间' },
    { key: 'endDate', label: '任职结束时间' },
    { key: 'category', label: '任职类别' },
    { key: 'studentType', label: '任职所带学生类别' },
    { key: 'studentCount', label: '任职所带学生总数' },
    { key: 'grade', label: '任职所带学生年级' }
  ]

  for (const r of required) {
    if (!form[r.key]) {
      ElMessage.warning('请选择' + r.label)
      return
    }
  }

  // 写入 profile_data
  try {
    const raw = localStorage.getItem('profile_data')
    const profile = raw ? JSON.parse(raw) : {}
    profile.workList = profile.workList || []
    profile.workList.push({ ...form })
    localStorage.setItem('profile_data', JSON.stringify(profile))
  } catch (e) {}

  ElMessage.success('新增成功')
  router.back()
}
</script>

<style scoped lang="scss">
.add-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.form-body { flex: 1; overflow-y: auto; padding: 14px; padding-bottom: 80px; }

.field {
  background: #fff; border-radius: 10px; padding: 14px; margin-bottom: 10px;
  label { display: block; font-size: 13px; color: #333; margin-bottom: 8px; font-weight: 500; }
  .star { color: #e74c3c; font-weight: bold; }
  select, input {
    width: 100%; height: 40px; border: 1px solid #eee; border-radius: 6px; padding: 0 12px;
    font-size: 13px; color: #333; background: #fff; outline: none; appearance: none;
    &:focus { border-color: #e74c3c; }
  }
  input[type="date"] { color-scheme: light; }
}

.bottom-actions { position: sticky; bottom: 0; padding: 10px 14px; background: #fff; display: flex; gap: 12px; border-top: 1px solid #eee; }
.cancel-btn, .submit-btn { flex: 1; height: 42px; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; }
.cancel-btn { background: #fff; border: 1px solid #ddd; color: #333; }
.submit-btn { background: #e74c3c; border: none; color: #fff; }
</style>