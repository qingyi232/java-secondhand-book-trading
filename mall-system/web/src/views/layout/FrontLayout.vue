<template>
  <el-container class="my-container">
    <el-header class="my-header">
      <div class="nav-inner">
        <div class="nav-brand" @click="router.push('/')">
          <span class="brand-icon">📚</span>
          <span class="brand-text">二手书交易平台</span>
        </div>
        <el-menu
            :default-active="useRoute().path"
            mode="horizontal"
            router
            class="nav-menu"
        >
          <el-menu-item index="/index">首页</el-menu-item>
          <el-menu-item index="/bookList">书籍列表</el-menu-item>
          <el-menu-item index="/shoppingCart">购物车</el-menu-item>
          <el-menu-item index="/bookOrder">我的订单</el-menu-item>
          <el-menu-item index="/personalCenter">个人中心</el-menu-item>
        </el-menu>
        <div class="nav-user">
          <el-dropdown v-if="isUserLogin">
            <div class="user-info">
              <el-avatar :size="28" :src="currentUser.avatarUrl"></el-avatar>
              <span class="username">{{ currentUser.nickname || currentUser.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item><span @click="editCurrentUser">个人信息</span></el-dropdown-item>
                <el-dropdown-item><span @click="editPassword">修改密码</span></el-dropdown-item>
                <el-dropdown-item><span @click="balanceInfo">余额/充值</span></el-dropdown-item>
                <el-dropdown-item divided><span @click="logout">退出登录</span></el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    <el-main class="my-main">
      <router-view/>
    </el-main>
    <el-footer class="my-footer">
      二手书交易平台 © 2026 — 让每一本好书都找到新主人
    </el-footer>
  </el-container>
</template>

<script setup>
import tools from "@/utils/tools.js";
import {ref} from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {useRoute} from "vue-router";

const isUserLogin = ref(tools.isLogin())
const currentUser = ref(tools.getCurrentUser())

if (currentUser.value === null) {
  window.location.href = "/login"
}
if (currentUser.value && currentUser.value.type !== 'USER') {
  router.push({path: "/admin"})
}

function logout() {
  ElMessage({ message: '退出登录成功', type: 'success' });
  localStorage.clear()
  router.push({path: "/login"})
}

function editCurrentUser() { router.push({path: "/editCurrentUser"}) }
function editPassword() { router.push({path: "/editPassword"}) }
function balanceInfo() { router.push({path: "/balanceInfo"}) }
</script>

<style scoped>
.my-container { display: flex; flex-direction: column; min-height: 100vh; }

.my-header {
  height: 56px !important;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 56px;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  margin-right: 20px;
  white-space: nowrap;
}

.brand-icon { font-size: 24px; }
.brand-text { font-size: 16px; font-weight: 600; color: #2d6a4f; }

.nav-menu { flex: 1; border-bottom: none !important; }
.nav-menu .el-menu-item { font-size: 14px; }
.nav-menu .el-menu-item.is-active { color: #2d6a4f !important; border-bottom-color: #2d6a4f !important; }

.nav-user { margin-left: 16px; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { font-size: 14px; color: #333; }

.my-main { background-color: #f8faf8; flex: 1; padding: 0; }
.my-main::-webkit-scrollbar { display: none; }

.my-footer {
  font-size: 13px;
  padding: 16px;
  color: #999;
  background-color: #f0f5f0;
  text-align: center;
  height: auto !important;
}
</style>
