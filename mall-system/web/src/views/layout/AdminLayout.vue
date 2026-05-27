<template>
  <el-container style="height: 100%;">
    <el-aside width="220px" class="my-aside">
      <h3 class="title">📚 二手书交易平台</h3>
      <el-menu
          style="width: 100%"
          active-text-color="#52c41a"
          background-color="#1a1a2e"
          text-color="rgba(255,255,255,0.75)"
          :default-active="useRoute().path"
          @select="handleMenuSelect"
          router>
        <el-menu-item index="/admin">
          <el-icon><HomeFilled/></el-icon>
          <span>首页概览</span>
        </el-menu-item>

        <el-menu-item index="/admin/admin" v-if="currentUser.type==='ADMIN'">
          <el-icon><Lock/></el-icon>
          <span>管理员管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/user" v-if="currentUser.type==='ADMIN'">
          <el-icon><User/></el-icon>
          <span>买家管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/shop" v-if="currentUser.type==='ADMIN'">
          <el-icon><Shop/></el-icon>
          <span>卖家管理</span>
        </el-menu-item>

        <el-sub-menu index="1">
          <template #title>
            <el-icon><Reading/></el-icon>
            <span>书籍管理</span>
          </template>
          <el-menu-item index="/admin/bookCategory" v-if="currentUser.type==='ADMIN'">
            <el-icon><Folder/></el-icon>
            <span>书籍分类</span>
          </el-menu-item>
          <el-menu-item index="/admin/book">
            <el-icon><Document/></el-icon>
            <span>书籍信息</span>
          </el-menu-item>
          <el-menu-item index="/admin/bookCollect" v-if="currentUser.type==='ADMIN'">
            <el-icon><Star/></el-icon>
            <span>书籍收藏</span>
          </el-menu-item>
          <el-menu-item index="/admin/browsingHistory" v-if="currentUser.type==='ADMIN'">
            <el-icon><View/></el-icon>
            <span>浏览记录</span>
          </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <el-icon><Tickets/></el-icon>
            <span>订单管理</span>
          </template>
          <el-menu-item index="/admin/bookOrder">
            <el-icon><Tickets/></el-icon>
            <span>书籍订单</span>
          </el-menu-item>
          <el-menu-item index="/admin/orderEvaluate">
            <el-icon><ChatDotRound/></el-icon>
            <span>订单评价</span>
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/admin/message" v-if="currentUser.type==='ADMIN'">
          <el-icon><ChatLineSquare/></el-icon>
          <span>消息管理</span>
        </el-menu-item>

        <el-menu-item index="/admin/shippingAddress" v-if="currentUser.type==='ADMIN'">
          <el-icon><AddLocation/></el-icon>
          <span>收货地址</span>
        </el-menu-item>

        <el-menu-item index="/admin/shopCollect" v-if="currentUser.type==='ADMIN'">
          <el-icon><Star/></el-icon>
          <span>店铺收藏</span>
        </el-menu-item>

        <el-sub-menu index="3" v-if="currentUser.type==='ADMIN'">
          <template #title>
            <el-icon><Setting/></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/admin/slideshow">
            <el-icon><Picture/></el-icon>
            <span>轮播图</span>
          </el-menu-item>
          <el-menu-item index="/admin/announcement">
            <el-icon><Bell/></el-icon>
            <span>公告管理</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header height="60px" class="my-header">
        <el-row :gutter="5">
          <el-col :span="6" style="margin-top: 20px;">
            <el-space>
              <router-link to="/admin" style="color: #333; text-decoration: none;">
                <HomeFilled style="font-size: 18px;"/>
              </router-link>
            </el-space>
          </el-col>
          <el-col :span="9"></el-col>
          <el-col :span="9">
            <div style="text-align: right;">
              <el-space style="margin-top: 15px;">
                <el-tag v-if="currentUser.type==='ADMIN'" type="danger" size="small">管理员</el-tag>
                <el-tag v-if="currentUser.type==='SHOP'" type="success" size="small">卖家</el-tag>
                <el-dropdown v-if="isUserLogin">
                  <div style="cursor: pointer;">
                    <el-space>
                      <el-avatar style="width: 32px;height: 32px;" :size="32" :src="currentUser.avatarUrl"></el-avatar>
                      <span style="font-size: 14px; color: #333;">{{ currentUser.nickname || currentUser.username }}</span>
                    </el-space>
                  </div>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item><span @click="editCurrentUser">个人信息</span></el-dropdown-item>
                      <el-dropdown-item><span @click="editPassword">修改密码</span></el-dropdown-item>
                      <el-dropdown-item divided><span @click="logout">退出登录</span></el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </el-space>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-main style="background-color: #f5f7fa;" class="my-main">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import tools from "@/utils/tools.js";
import {ref} from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {useRoute} from 'vue-router';
import {HomeFilled} from "@element-plus/icons-vue";

const isUserLogin = ref(tools.isLogin())
const currentUser = ref(tools.getCurrentUser())
const activeIndex = ref(useRoute().path)

if (currentUser.value === null) {
  window.location.href = "/login"
}
if (currentUser.value && currentUser.value.type === 'USER') {
  router.push({path: "/"})
}

function handleMenuSelect(key, keyPath) {
  activeIndex.value = key
}

function logout() {
  ElMessage({ message: '退出登录成功', type: 'success' });
  localStorage.clear()
  router.push({path: "/login"})
}

function editCurrentUser() {
  router.push({path: "/admin/editCurrentUser"})
}

function editPassword() {
  router.push({path: "/admin/editPassword"})
}
</script>

<style scoped>
.title {
  color: #52c41a;
  width: 100%;
  text-align: center;
  margin: 18px 5px;
  font-size: 16px;
  letter-spacing: 1px;
}

.my-main::-webkit-scrollbar {
  display: none;
}

.my-aside {
  background-color: #1a1a2e;
  overflow-x: hidden;
}

.el-menu {
  border-right: 0;
}

.my-header {
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}
</style>
