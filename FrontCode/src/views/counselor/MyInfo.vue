<template>
  <div>
    <el-card>
      <template #header><span>我的基本信息</span></template>
      <el-form :model="form" label-width="100px" :disabled="!editing">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号"><el-input v-model="form.employeeNo" /></el-form-item>
            <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期"><el-date-picker v-model="form.birthDate" type="date" /></el-form-item>
            <el-form-item label="政治面貌"><el-input v-model="form.politicalStatus" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历"><el-input v-model="form.education" /></el-form-item>
            <el-form-item label="职称"><el-input v-model="form.title" /></el-form-item>
            <el-form-item label="入职日期"><el-date-picker v-model="form.hireDate" type="date" /></el-form-item>
            <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
            <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const editing = ref(false)
const form = ref({})

onMounted(async () => {
  const res = await request.get('/counselor/my')
  form.value = res.data || {}
})
</script>