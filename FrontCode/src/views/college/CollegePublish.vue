<template>
  <div class="publish-page">
    <div class="top-nav">
      <div class="back-btn" @click="handleCancel">
        <svg viewBox="0 0 24 24" fill="none" stroke="#333" stroke-width="2.5" width="20" height="20"><polyline points="15 18 9 12 15 6"/></svg>
      </div>
      <span class="nav-title">发布采集批次</span>
      <div class="nav-spacer"></div>
    </div>
    <div class="form-body">
      <div class="section"><div class="section-title">批次信息</div>
        <div class="field"><label>批次名称</label><input v-model="form.name" placeholder="请输入批次名称" /></div>
        <div class="field"><label>开始时间</label><input v-model="form.startTime" type="date" /></div>
        <div class="field"><label>结束时间</label><input v-model="form.endTime" type="date" /></div>
        <div class="field"><label>批次时长</label><input v-model="form.duration" placeholder="如：30 天" /></div>
      </div>
    </div>
    <div class="bottom-actions">
      <button class="cancel-btn" @click="handleCancel">取消</button>
      <button class="submit-btn" @click="handleSubmit">确认发布</button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const form = reactive({ name: '', startTime: '', endTime: '', duration: '' })

function handleCancel() { router.push('/college/batch') }

async function handleSubmit() {
  if (!form.name) { ElMessage.warning('请输入批次名称'); return }
  if (!form.startTime) { ElMessage.warning('请选择开始时间'); return }
  if (!form.endTime) { ElMessage.warning('请选择结束时间'); return }
  if (!form.duration) { ElMessage.warning('请输入批次时长'); return }

  try {
    await request.post('/batch/publish', {
      name: form.name,
      startTime: form.startTime,
      endTime: form.endTime,
      duration: form.duration
    })
    ElMessage.success('发布成功')
    router.push('/college/batch')
  } catch (e) {
    ElMessage.error('发布失败: ' + (e.message || '未知错误'))
  }
}
</script>

<style scoped lang="scss">
.publish-page { min-height: 100%; background: #f5f5f5; display: flex; flex-direction: column; }
.top-nav { display: flex; align-items: center; justify-content: space-between; padding: 10px 14px; background: #fff; flex-shrink: 0; .nav-title { font-size: 15px; font-weight: 600; } .nav-spacer { width: 20px; } .back-btn { cursor: pointer; display: flex; align-items: center; } }
.form-body { flex: 1; padding: 12px; }
.section { background: #fff; border-radius: 12px; padding: 14px; }
.section-title { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 12px; }
.field { margin-bottom: 12px; label { display: block; font-size: 12px; color: #999; margin-bottom: 4px; } input { width: 100%; height: 40px; border: 1px solid #eee; border-radius: 6px; padding: 0 10px; font-size: 13px; outline: none; &:focus { border-color: #e74c3c; } } }
.bottom-actions { position: sticky; bottom: 0; padding: 10px 14px; background: #fff; display: flex; gap: 12px; border-top: 1px solid #eee; .cancel-btn, .submit-btn { flex: 1; height: 42px; border-radius: 8px; font-size: 14px; font-weight: 500; cursor: pointer; } .cancel-btn { background: #fff; border: 1px solid #ddd; color: #333; } .submit-btn { background: #e74c3c; border: none; color: #fff; } }
</style>