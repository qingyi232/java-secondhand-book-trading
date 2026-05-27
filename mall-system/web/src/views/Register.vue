<template>
  <div class="main-context">
    <el-card class="box-card">
      <el-space direction="vertical" style="width: 100%" size="large">
        <el-space>
          <el-space direction="vertical" style="width: 100%" size="small">
            <span style="font-size: 40px;">📚</span>
            <h2 style="color: #2d6a4f; margin: 0;">二手书交易平台</h2>
            <span style="font-size: 13px; color: #888;">注册新账号</span>
          </el-space>
        </el-space>
        <el-form :model="formData" label-width="80px" :rules="rules" ref="formRef" style="width: 100%" inline>
          <el-form-item label="头像" prop="avatarUrl"
                        style="width: 100%"
                        :rules="[{required:true,message:'请选择头像',trigger:[ 'blur','change']}]">
            <MyUpLoad type="imageCard" :limit="1" :files="formData.avatarUrl"

                      @setFiles="formData.avatarUrl =$event"></MyUpLoad>
          </el-form-item>
          <el-form-item label="用户类型" prop="type"
                        :rules="[{required:true,message:'请选择类型',trigger:[ 'blur','change']}]">
            <el-select v-model="formData.type" placeholder="请选择用户类型" style="width: 180px">
              <el-option label="用户" value="USER"></el-option>
              <el-option label="店铺" value="SHOP"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="用户名" prop="username"
                        :rules="[{required:true,message:'请输入用户名',trigger:[ 'blur','change']}]">
            <el-input
                style="width: 180px"
                placeholder="请输入用户名"
                v-model.trim="formData.username"
                clearable
            >
            </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password"
                        :rules="[{required:true,message:'请输入密码',trigger:[ 'blur','change']}]">
            <el-input
                style="width: 180px"
                placeholder="请输入密码"
                show-password
                v-model.trim="formData.password"
                clearable
            >
            </el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname"
                        :rules="[{required:true,message:'请输入昵称',trigger:[ 'blur','change']}]">
            <el-input
                style="width: 180px"
                placeholder="请输入昵称"
                v-model.trim="formData.nickname"
                clearable
            >
            </el-input>
          </el-form-item>
          <el-form-item label="店铺名称" prop="name"
                        style="width: 100%"
                        v-if="formData.type === 'SHOP'"
                        :rules="[{required:true,message:'请输入店铺名称',trigger:[ 'blur','change']}]">
            <el-input
                style="width: 180px"
                placeholder="请输入店铺名称"
                v-model.trim="formData.name"
                clearable
            >
            </el-input>
          </el-form-item>
          <el-form-item label="店铺资质" prop="aptitudeImgs"
                        style="width: 100%"
                        v-if="formData.type === 'SHOP'"
                        :rules="[{required:true,message:'请上传店铺资质',trigger:[ 'blur','change']}]">
            <MyUpLoad type="imageCard" :limit="5" :files="formData.aptitudeImgs"
                      @setFiles="formData.aptitudeImgs =$event"></MyUpLoad>
          </el-form-item>
          <el-form-item label="" style="width: 100%">
            <el-space direction="vertical" alignment="left" style="width: 100%">
              <el-button @click="submitForm()" type="success" style="width: 100%">注 册</el-button>
              <router-link tag="span" :to="{path:'login'}">
                <el-button type="text" class="button" style="float: right">已有账号？去登录</el-button>
              </router-link>
            </el-space>

          </el-form-item>
        </el-form>
      </el-space>
    </el-card>
  </div>
</template>
<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import http from "@/utils/http.js";
import MyUpLoad from "@/components/MyUpload.vue";

const formRef = ref(null);
const formData = ref({
  type: 'USER',
  username: '',
  nickname: '',
  avatarUrl: '',
  password: '',
  name: '',
});

const rules = ref({
  username: [
    {required: true, message: '请输入用户名称', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
});

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (!valid) {
      return
    }
    http.put("/common/register", formData.value).then(res => {
      if (!res) {
        return
      }
      ElMessage({
        message: "注册成功，正在跳转",
        type: "success"
      });
      window.location.href = "/login";
    });
  });
}

</script>

<style scoped>
.main-context {
  height: 100vh; /* 使 .app 高度为视口高度 */
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 30%, #f1f8e9 60%, #dcedc8 100%);
  background-size: cover; /* 使用 cover 保持图片比例 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: white; /* 根据背景图片调整文字颜色 */
}

.box-card {
  width: 650px;
  margin: 0 auto;
  text-align: center;
}
</style>
