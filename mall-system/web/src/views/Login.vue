<template>
  <div class="login-page">
    <el-card class="login-card" shadow="hover">
      <div class="login-header">
        <span class="login-icon">📚</span>
        <h2 class="login-title">二手书交易平台</h2>
        <p class="login-subtitle">让每一本好书都找到新主人</p>
      </div>
      <el-form :model="formData" label-width="0px" ref="formRef">
        <el-form-item prop="username" :rules="[{required:true,message:'请输入用户名',trigger:['blur','change']}]">
          <el-input :prefix-icon="User" placeholder="请输入账号" v-model.trim="formData.username" clearable size="large"></el-input>
        </el-form-item>
        <el-form-item prop="password" :rules="[{required:true,message:'请输入密码',trigger:['blur','change']}]">
          <el-input :prefix-icon="Lock" placeholder="请输入密码" show-password v-model.trim="formData.password" clearable size="large"></el-input>
        </el-form-item>
        <el-form-item prop="type">
          <el-select v-model="formData.type" placeholder="请选择身份" style="width: 100%;" size="large">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="买家（普通用户）" value="USER"></el-option>
            <el-option label="卖家（书籍卖家）" value="SHOP"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="submitForm()" type="primary" style="width: 100%; background: #2d6a4f; border-color: #2d6a4f;" size="large">登 录</el-button>
        </el-form-item>
        <div style="text-align: center;">
          <router-link to="/register" style="color: #2d6a4f; font-size: 14px;">没有账号？去注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";
import {User, Lock} from "@element-plus/icons-vue";

const formData = ref({ username: '', password: '', type: 'ADMIN' });
const formRef = ref(null);

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) return;
    http.post("/common/login", formData.value).then(res => {
      if (!res) return;
      ElMessage({ message: "登录成功", type: "success" });
      localStorage.setItem("token", res.data);
      http.get("/common/currentUser").then(res1 => {
        let currentUser = res1.data;
        localStorage.setItem("currentUser", JSON.stringify(currentUser));
        if (currentUser.type === "ADMIN" || currentUser.type === "SHOP") {
          window.location.href = "/admin";
        } else {
          window.location.href = "/";
        }
      })
    })
  });
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 30%, #f1f8e9 60%, #dcedc8 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  width: 380px;
  border-radius: 16px;
  padding: 20px;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-icon { font-size: 48px; }

.login-title {
  font-size: 22px;
  color: #2d6a4f;
  margin: 8px 0 4px;
  font-weight: 700;
}

.login-subtitle {
  font-size: 13px;
  color: #888;
  margin: 0;
}
</style>
