<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #e8f5e9;">📚</div>
          <div class="stat-info">
            <p class="stat-label">书籍总数</p>
            <h2 class="stat-value">{{ stats.bookCount }}</h2>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #e3f2fd;">📋</div>
          <div class="stat-info">
            <p class="stat-label">订单总数</p>
            <h2 class="stat-value">{{ stats.orderCount }}</h2>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #fff3e0;">👥</div>
          <div class="stat-info">
            <p class="stat-label">用户数</p>
            <h2 class="stat-value">{{ stats.userCount }}</h2>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #fce4ec;">⏳</div>
          <div class="stat-info">
            <p class="stat-label">待审核</p>
            <h2 class="stat-value">{{ stats.pendingCount }}</h2>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card>
      <template #header>
        <span style="font-weight: 600;">欢迎使用二手书交易平台管理后台</span>
      </template>
      <p style="color: #666; line-height: 2;">
        当前登录角色：<el-tag :type="currentUser.type === 'ADMIN' ? 'danger' : 'success'" size="small">{{ currentUser.type === 'ADMIN' ? '管理员' : '卖家' }}</el-tag>
        &nbsp;&nbsp;
        用户名：<strong>{{ currentUser.nickname || currentUser.username }}</strong>
      </p>
      <el-alert title="管理提示" type="info" :closable="false" show-icon style="margin-top: 12px;">
        请及时审核待审核的书籍，确保平台内容质量。如有违规书籍请及时下架处理。
      </el-alert>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import request from "@/utils/http.js";
import tools from "@/utils/tools.js";

const currentUser = ref(tools.getCurrentUser())
const stats = ref({ bookCount: 0, orderCount: 0, userCount: 0, pendingCount: 0 })

onMounted(() => {
  request.get("/book/page", { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    stats.value.bookCount = res.data.total || 0
  }).catch(() => {})

  request.get("/bookOrder/page", { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    stats.value.orderCount = res.data.total || 0
  }).catch(() => {})

  request.get("/book/page", { params: { pageNum: 1, pageSize: 1, reviewStatus: '待审核' } }).then(res => {
    stats.value.pendingCount = res.data.total || 0
  }).catch(() => {})
})
</script>

<style scoped>
.stat-card { border-radius: 12px; }
.stat-card :deep(.el-card__body) { display: flex; align-items: center; gap: 16px; padding: 20px; }
.stat-icon { width: 56px; height: 56px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 28px; }
.stat-info { flex: 1; }
.stat-label { margin: 0; color: #999; font-size: 13px; }
.stat-value { margin: 4px 0 0; font-size: 28px; color: #333; }
</style>
