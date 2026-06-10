import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue'), meta: { title: '登录', noAuth: true } },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '首页' } },
      { path: 'counselor/my', name: 'MyInfo', component: () => import('../views/counselor/MyInfo.vue'), meta: { title: '我的信息', roles: ['COUNSELOR'] } },
      { path: 'application/my', name: 'MyApplications', component: () => import('../views/application/MyApplications.vue'), meta: { title: '我的申请', roles: ['COUNSELOR'] } },
      { path: 'counselor/list', name: 'CounselorList', component: () => import('../views/counselor/CounselorList.vue'), meta: { title: '辅导员列表', roles: ['DEPARTMENT', 'ADMIN'] } },
      { path: 'application/pending', name: 'PendingReview', component: () => import('../views/application/PendingReview.vue'), meta: { title: '待审核申请', roles: ['DEPARTMENT', 'ADMIN'] } },
      { path: 'application/approve', name: 'PendingApprove', component: () => import('../views/application/PendingApprove.vue'), meta: { title: '待审批申请', roles: ['ADMIN'] } },
      { path: 'department/manage', name: 'DepartmentManage', component: () => import('../views/department/DepartmentManage.vue'), meta: { title: '院系管理', roles: ['ADMIN'] } },
      { path: 'user/manage', name: 'UserManage', component: () => import('../views/admin/UserManage.vue'), meta: { title: '用户管理', roles: ['ADMIN'] } }
    ]
  },
  {
    path: '/collection',
    component: () => import('../views/collection/CollectionLayout.vue'),
    redirect: '/collection/batch',
    children: [
      { path: 'batch', name: 'CollectionBatch', component: () => import('../views/collection/CollectionBatch.vue'), meta: { title: '采集批次' } },
      { path: 'profile', name: 'CollectionProfile', component: () => import('../views/collection/CollectionProfile.vue'), meta: { title: '个人信息' } },
      { path: 'profile/edit', name: 'ProfileEdit', component: () => import('../views/collection/ProfileEdit.vue'), meta: { title: '编辑个人信息' } },
      { path: 'approval/history', name: 'ApprovalHistory', component: () => import('../views/collection/ApprovalHistory.vue'), meta: { title: '审批记录' } },
      { path: 'more', name: 'CollectionMore', component: () => import('../views/collection/CollectionMore.vue'), meta: { title: '更多功能' } },
      { path: 'form/:batchId', name: 'CollectionForm', component: () => import('../views/collection/CollectionForm.vue'), meta: { title: '信息采集表单' } },
      { path: 'detail/:batchId', name: 'CollectionDetail', component: () => import('../views/collection/CollectionDetail.vue'), meta: { title: '信息采集详情' } },
      { path: 'work/add', name: 'WorkAdd', component: () => import('../views/collection/WorkAdd.vue'), meta: { title: '新增工作经历' } },
      { path: 'study/add', name: 'StudyAdd', component: () => import('../views/collection/StudyAdd.vue'), meta: { title: '新增学习经历' } }
    ]
  },
  {
    path: '/college',
    component: () => import('../views/college/CollegeLayout.vue'),
    redirect: '/college/review',
    children: [
      { path: 'review', name: 'CollegeReview', component: () => import('../views/college/CollegeReview.vue'), meta: { title: '信息审核' } },
      { path: 'batch', name: 'CollegeBatch', component: () => import('../views/collection/CollectionBatch.vue'), meta: { title: '采集批次' } },
      { path: 'batch/publish', name: 'CollegePublish', component: () => import('../views/college/CollegePublish.vue'), meta: { title: '发布采集批次' } },
      { path: 'stats', name: 'CollegeStats', component: () => import('../views/college/CollegeStats.vue'), meta: { title: '数据统计' } },
      { path: 'admin-stats', name: 'AdminStats', component: () => import('../views/admin/AdminStats.vue'), meta: { title: '数据统计' } },
      { path: 'more', name: 'CollegeMore', component: () => import('../views/college/CollegeMore.vue'), meta: { title: '更多功能' } },
      { path: 'contacts', name: 'CollegeContacts', component: () => import('../views/college/CollegeContacts.vue'), meta: { title: '通讯录' } },
      { path: 'form/:batchId', name: 'CollegeForm', component: () => import('../views/collection/CollectionForm.vue'), meta: { title: '信息采集表单' } },
      { path: 'detail/:batchId', name: 'CollegeDetail', component: () => import('../views/collection/CollectionDetail.vue'), meta: { title: '信息采集详情' } },
      { path: 'work/add', name: 'CollegeWorkAdd', component: () => import('../views/collection/WorkAdd.vue'), meta: { title: '新增工作经历' } },
      { path: 'study/add', name: 'CollegeStudyAdd', component: () => import('../views/collection/StudyAdd.vue'), meta: { title: '新增学习经历' } },
      { path: 'approval/history', name: 'CollegeApprovalHistory', component: () => import('../views/collection/ApprovalHistory.vue'), meta: { title: '审批记录' } },
      { path: 'profile', name: 'CollegeProfile', component: () => import('../views/collection/CollectionProfile.vue'), meta: { title: '个人信息' } },
      { path: 'profile/edit', name: 'CollegeProfileEdit', component: () => import('../views/collection/ProfileEdit.vue'), meta: { title: '编辑个人信息' } }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? to.meta.title + ' - 辅导员管理系统' : '辅导员管理系统'
  const token = localStorage.getItem('token')
  if (!token && !to.meta.noAuth) {
    next('/login')
  } else {
    next()
  }
})

export default router