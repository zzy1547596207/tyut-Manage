<template>
  <div class="add-page">
    <div class="top-nav">
      <div class="back-btn" @click="$router.back()">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">新增学习经历</span>
      <div class="nav-spacer"></div>
    </div>

    <div class="form-body">
      <div class="field required">
        <label>学历类型<span class="star">*</span></label>
        <select v-model="form.degreeType">
          <option value="">请选择学历类型</option>
          <option value="全日制博士">全日制博士</option>
          <option value="全日制硕士">全日制硕士</option>
          <option value="全日制本科">全日制本科</option>
          <option value="全日制大专">全日制大专</option>
          <option value="非全日制博士">非全日制博士</option>
          <option value="非全日制硕士">非全日制硕士</option>
          <option value="非全日制本科">非全日制本科</option>
        </select>
      </div>

      <div class="field required">
        <label>毕业院校<span class="star">*</span></label>
        <input v-model="form.school" type="text" placeholder="请输入毕业院校标准全称" />
      </div>

      <div class="field required">
        <label>专业<span class="star">*</span></label>
        <input v-model="form.major" type="text" placeholder="请输入专业" />
      </div>

      <div class="field required">
        <label>入学日期<span class="star">*</span></label>
        <input v-model="form.entryDate" type="date" placeholder="请选择入学日期" />
      </div>

      <div class="field required">
        <label>毕业日期<span class="star">*</span></label>
        <input v-model="form.gradDate" type="date" placeholder="请选择毕业日期" />
      </div>

      <div class="bottom-actions">
        <button class="cancel-btn" @click="$router.back()">取消</button>
        <button class="submit-btn" @click="handleSubmit">提交</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const batchId = route.query.batchId || ''
const source = route.query.source || ''
const STORAGE_KEY = source === 'profile' ? ('profile_edit_' + (localStorage.getItem('username') || '')) : ('collection_form_' + batchId)

const form = reactive({ degreeType: '', school: '', major: '', entryDate: '', gradDate: '' })

function handleSubmit() {
  const required = [
    { key: 'degreeType', label: '学历类型' },
    { key: 'school', label: '毕业院校' },
    { key: 'major', label: '专业' },
    { key: 'entryDate', label: '入学日期' },
    { key: 'gradDate', label: '毕业日期' }
  ]
  for (const r of required) {
    if (!form[r.key]) { ElMessage.warning('请' + (r.key.includes('Date') ? '选择' : '输入') + r.label); return }
  }
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    const data = raw ? JSON.parse(raw) : {}
    if (!data.studyList || !Array.isArray(data.studyList)) data.studyList = []
    data.studyList.push({ ...form })
    localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
  } catch (e) {}
  ElMessage.success('新增成功')
  router.back()
}
</script>

<style scoped lang="scss">
.add-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.form-body { flex: 1; overflow-y: auto; padding: 14px; padding-bottom: 80px; }
.field { background: #fff; border-radius: 10px; padding: 14px; margin-bottom: 10px; label { display: block; font-size: 13px; color: #333; margin-bottom: 8px; font-weight: 500; } .star { color: #e74c3c; font-weight: bold; } select, input { width: 100%; height: 40px; border: 1px solid #eee; border-radius: 6px; padding: 0 12px; font-size: 13px; color: #333; background: #fff; outline: none; appearance: none; &:focus { border-color: #e74c3c; } } input[type="date"] { color-scheme: light; } }
.bottom-actions { position: sticky; bottom: 0; padding: 10px 14px; background: #fff; display: flex; gap: 12px; border-top: 1px solid #eee; .cancel-btn, .submit-btn { flex: 1; height: 42px; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; } .cancel-btn { background: #fff; border: 1px solid #ddd; color: #333; } .submit-btn { background: #e74c3c; border: none; color: #fff; } }
</style>