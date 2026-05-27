<template>
  <div style="width: 700px; margin: 0 auto; padding: 20px">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span style="font-size: 18px; font-weight: bold">余额/充值</span>
        </div>
      </template>
      <div class="balance-section">
        <div class="balance-label">当前余额（元）</div>
        <div class="balance-value">{{ formatMoney(userInfo.balance) }}</div>
        <el-button type="primary" size="large" @click="openRechargeDialog" style="margin-top: 20px; width: 160px">
          充值
        </el-button>
      </div>
    </el-card>

    <!-- 充值弹窗 -->
    <el-dialog
        v-model="dialogVisible"
        title="账户充值"
        width="520px"
        :close-on-click-modal="false"
        @closed="resetDialog"
    >
      <!-- 步骤1: 选择金额和支付方式 -->
      <div v-if="step === 1">
        <div class="section-title">选择充值金额</div>
        <div class="amount-grid">
          <div
              v-for="item in presetAmounts"
              :key="item"
              class="amount-item"
              :class="{ active: selectedAmount === item }"
              @click="selectAmount(item)"
          >
            ¥{{ item }}
          </div>
        </div>

        <div class="custom-amount">
          <span class="section-title">自定义金额</span>
          <el-input
              v-model="customAmount"
              placeholder="请输入充值金额"
              style="width: 200px; margin-left: 12px"
              @focus="selectedAmount = null"
              @input="onCustomInput"
          >
            <template #prefix>¥</template>
          </el-input>
        </div>

        <div class="section-title" style="margin-top: 24px">选择支付方式</div>
        <el-radio-group v-model="payMethod" class="pay-methods">
          <el-radio value="wechat" size="large">
            <span class="pay-label">
              <svg class="pay-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <path d="M690.1 377.4c5.9 0 11.8 0.2 17.6 0.5-24.4-128.7-158.3-227.1-313.4-227.1C209 150.8 57.7 280.6 57.7 443.8c0 93.7 51.2 175.3 131.8 228.6l-33.6 99.2 115.6-58.1c40.5 11.2 75.2 22.4 117.4 22.4 5.6 0 11.2-0.2 16.7-0.5-3.5-12.2-5.5-24.9-5.5-38 0-169.5 143.4-320 289.9-320z" fill="#07C160"/>
                <path d="M966.3 695.6c0-131.2-131.8-237.8-279.2-237.8-156.1 0-281.4 106.6-281.4 237.8s125.3 237.8 281.4 237.8c32.6 0 65.8-10.6 99.2-21.8l89.6 53.4-24.6-79.4c65.8-42.4 114.9-114.8 114.9-190z" fill="#07C160"/>
                <circle cx="300" cy="400" r="28" fill="#fff"/>
                <circle cx="480" cy="400" r="28" fill="#fff"/>
                <circle cx="610" cy="680" r="22" fill="#fff"/>
                <circle cx="770" cy="680" r="22" fill="#fff"/>
              </svg>
              <span style="color: #07c160">微信支付</span>
            </span>
          </el-radio>
          <el-radio value="alipay" size="large">
            <span class="pay-label">
              <svg class="pay-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <path d="M789.1 417.8H571.5c-11 0-20-9-20-20s9-20 20-20h217.6c11 0 20 9 20 20s-9 20-20 20z" fill="#1677FF"/>
                <path d="M680.3 540.2c-48.4 0-93.4-15.4-126.8-43.2-36.6-30.4-56.8-71.8-56.8-116.4 0-44.6 20.2-86 56.8-116.4 33.4-27.8 78.4-43.2 126.8-43.2s93.4 15.4 126.8 43.2c36.6 30.4 56.8 71.8 56.8 116.4 0 44.6-20.2 86-56.8 116.4-33.4 27.8-78.4 43.2-126.8 43.2z m0-279.2c-77 0-143.6 50.2-143.6 119.6s66.6 119.6 143.6 119.6 143.6-50.2 143.6-119.6-66.6-119.6-143.6-119.6z" fill="#1677FF"/>
                <path d="M512 946c-239.4 0-434-194.6-434-434S272.6 78 512 78s434 194.6 434 434c0 105.6-37.6 207.4-106 287l-3 3.4c-1 1-1.6 2-2.6 3C760.8 893.2 640.4 946 512 946z m0-828C292.8 118 114 296.8 114 516s182.6 394 398 394c116.8 0 226.4-48.4 297-132.8l2-2.4 3-3.4C874.2 706 908 613.4 908 516c0-219.2-178.8-398-396-398z" fill="#1677FF"/>
                <path d="M853.4 625.4c-20.6-9-169.4-68-207.2-81.2 29-40.4 51.8-85.8 67-134.8H581.4v-41.8h97.4v-24.2h-97.4V296h-46.8c-8.2 0-8.2 8.2-8.2 8.2v39.2h-97.6v24.2h97.6v41.8H393v24.2h232.2c-12.8 35.2-30.6 68-52.4 97.6-45.2-30.2-95.8-52.4-116.6-52.4-51.6 0-80.6 32-80.6 66 0 76.8 109.4 80 199.6 19.6 5.2-3.4 10.2-7.2 15.2-11.2 46 18.8 213.8 88.4 213.8 88.4l49.2-20.2z" fill="#1677FF"/>
              </svg>
              <span style="color: #1677ff">支付宝</span>
            </span>
          </el-radio>
          <el-radio value="bank" size="large">
            <span class="pay-label">
              <svg class="pay-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
                <rect x="120" y="280" width="784" height="464" rx="40" fill="none" stroke="#ff6a00" stroke-width="48"/>
                <rect x="120" y="360" width="784" height="80" fill="#ff6a00"/>
                <rect x="200" y="520" width="200" height="40" rx="8" fill="#ff6a00" opacity="0.6"/>
                <rect x="200" y="600" width="120" height="40" rx="8" fill="#ff6a00" opacity="0.4"/>
              </svg>
              <span style="color: #ff6a00">银行卡</span>
            </span>
          </el-radio>
        </el-radio-group>
      </div>

      <!-- 步骤2: 确认支付 -->
      <div v-else-if="step === 2" class="confirm-section">
        <div class="confirm-title">确认充值信息</div>
        <div class="confirm-row">
          <span class="confirm-label">充值金额</span>
          <span class="confirm-value highlight">¥{{ finalAmount }}</span>
        </div>
        <div class="confirm-row">
          <span class="confirm-label">支付方式</span>
          <span class="confirm-value">{{ payMethodLabel }}</span>
        </div>
        <div class="confirm-row">
          <span class="confirm-label">充值账户</span>
          <span class="confirm-value">{{ userInfo.nickname || userInfo.username }}</span>
        </div>
      </div>

      <!-- 步骤3: 支付中 -->
      <div v-else-if="step === 3" class="loading-section">
        <el-icon class="is-loading" :size="48" color="#409eff"><Loading/></el-icon>
        <div class="loading-text">正在处理支付，请稍候...</div>
      </div>

      <!-- 步骤4: 支付成功 -->
      <div v-else-if="step === 4" class="success-section">
        <el-icon :size="64" color="#67c23a"><CircleCheckFilled/></el-icon>
        <div class="success-text">充值成功！</div>
        <div class="success-amount">¥{{ finalAmount }}</div>
        <div class="success-balance">当前余额：¥{{ formatMoney(userInfo.balance) }}</div>
      </div>

      <template #footer>
        <div v-if="step === 1">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="goConfirm" :disabled="!finalAmount">下一步</el-button>
        </div>
        <div v-else-if="step === 2">
          <el-button @click="step = 1">上一步</el-button>
          <el-button type="primary" @click="doRecharge">确认支付</el-button>
        </div>
        <div v-else-if="step === 4">
          <el-button type="primary" @click="dialogVisible = false">完成</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed} from 'vue';
import http from "@/utils/http.js";
import {ElMessage} from "element-plus";
import {Loading, CircleCheckFilled} from '@element-plus/icons-vue';

const userInfo = ref({});
const dialogVisible = ref(false);
const step = ref(1);
const selectedAmount = ref(null);
const customAmount = ref('');
const payMethod = ref('wechat');

const presetAmounts = [50, 100, 200, 500, 1000, 2000];

const payMethodMap = {
  wechat: '微信支付',
  alipay: '支付宝',
  bank: '银行卡',
};

const payMethodLabel = computed(() => payMethodMap[payMethod.value]);

const finalAmount = computed(() => {
  if (selectedAmount.value) return selectedAmount.value;
  const v = parseInt(customAmount.value);
  return v > 0 ? v : null;
});

load();

function load() {
  http.get('/common/currentUser').then(res => {
    userInfo.value = res.data;
  });
}

function formatMoney(val) {
  if (val === undefined || val === null) return '0.00';
  return Number(val).toFixed(2);
}

function selectAmount(amount) {
  selectedAmount.value = amount;
  customAmount.value = '';
}

function onCustomInput(val) {
  customAmount.value = val.replace(/[^\d]/g, '');
  selectedAmount.value = null;
}

function openRechargeDialog() {
  dialogVisible.value = true;
}

function resetDialog() {
  step.value = 1;
  selectedAmount.value = null;
  customAmount.value = '';
  payMethod.value = 'wechat';
}

function goConfirm() {
  if (!finalAmount.value) {
    ElMessage.warning('请选择或输入充值金额');
    return;
  }
  step.value = 2;
}

function doRecharge() {
  step.value = 3;
  http.post('/user/topUp/' + finalAmount.value).then(() => {
    setTimeout(() => {
      load();
      step.value = 4;
      ElMessage.success('充值成功');
    }, 1500);
  }).catch(() => {
    step.value = 2;
    ElMessage.error('充值失败，请重试');
  });
}
</script>

<style scoped>
.balance-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 0;
}

.balance-label {
  font-size: 14px;
  color: #909399;
}

.balance-value {
  font-size: 42px;
  font-weight: bold;
  color: #303133;
  margin-top: 8px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
}

.amount-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}

.amount-item {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  cursor: pointer;
  transition: all 0.2s;
}

.amount-item:hover {
  border-color: #409eff;
  color: #409eff;
}

.amount-item.active {
  border-color: #409eff;
  background: #ecf5ff;
  color: #409eff;
}

.custom-amount {
  display: flex;
  align-items: center;
}

.pay-methods {
  display: flex;
  flex-direction: column;
  gap: 0;
  align-items: flex-start;
}

.pay-methods :deep(.el-radio) {
  height: 40px;
  margin-right: 0;
}

.pay-label {
  font-size: 14px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.pay-icon {
  width: 22px;
  height: 22px;
  flex-shrink: 0;
  vertical-align: middle;
}

.confirm-section {
  padding: 12px 0;
}

.confirm-title {
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 24px;
  color: #303133;
}

.confirm-row {
  display: flex;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.confirm-label {
  color: #909399;
}

.confirm-value {
  color: #303133;
  font-weight: 500;
}

.confirm-value.highlight {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

.loading-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 48px 0;
}

.loading-text {
  margin-top: 16px;
  font-size: 14px;
  color: #909399;
}

.success-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px 0;
}

.success-text {
  margin-top: 12px;
  font-size: 20px;
  font-weight: bold;
  color: #67c23a;
}

.success-amount {
  margin-top: 8px;
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.success-balance {
  margin-top: 12px;
  font-size: 14px;
  color: #909399;
}
</style>
