<template>
  <div class="order-page">
    <h2 class="page-title">我的订单</h2>
    <el-tabs v-model="activeTab" @tab-change="tabChange" type="card">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="待支付" name="0"></el-tab-pane>
      <el-tab-pane label="待发货" name="1"></el-tab-pane>
      <el-tab-pane label="待收货" name="2"></el-tab-pane>
      <el-tab-pane label="已完成" name="3"></el-tab-pane>
      <el-tab-pane label="已取消" name="4"></el-tab-pane>
    </el-tabs>

    <div v-if="listData.length > 0">
      <el-card v-for="order in listData" :key="order.id" class="order-card" shadow="hover">
        <div class="order-header">
          <span class="order-no">订单号：{{ order.orderNo }}</span>
          <el-tag :type="statusMap[order.status].type" size="small">{{ statusMap[order.status].label }}</el-tag>
        </div>
        <div class="order-body">
          <el-image :src="order.bookMainImg" style="width: 80px; height: 100px; border-radius: 6px;" fit="cover">
            <template #error><div style="width:80px;height:100px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;color:#ccc;font-size:12px;">暂无</div></template>
          </el-image>
          <div class="order-info">
            <h4>{{ order.bookName }}</h4>
            <p>卖家：{{ order.shopName }}</p>
            <p>数量：{{ order.quantity }}</p>
          </div>
          <div class="order-price">
            <span class="total">¥{{ order.totalMoney }}</span>
          </div>
        </div>
        <div class="order-footer">
          <span class="order-time">{{ order.createTime }}</span>
          <div class="order-actions">
            <el-button type="primary" size="small" @click="payOrder(order.id)" v-if="order.status === 0" style="background:#2d6a4f;border-color:#2d6a4f;">去支付</el-button>
            <el-button size="small" @click="cancelOrder(order.id)" v-if="order.status === 0">取消订单</el-button>
            <el-button type="success" size="small" @click="receiveOrder(order.id)" v-if="order.status === 2">确认收货</el-button>
            <el-button type="warning" size="small" @click="refundOrder(order.id)" v-if="order.status === 1 || order.status === 2">申请退款</el-button>
            <el-button type="primary" size="small" @click="openEvaluate(order)" v-if="order.status === 3 && !order.orderEvaluateId" style="background:#2d6a4f;border-color:#2d6a4f;">评价</el-button>
            <el-tag v-if="order.orderEvaluateId" type="success" size="small">已评价</el-tag>
            <el-tag v-if="order.trackingNumber && order.status === 2" type="info" size="small">物流：{{ order.trackingNumber }}</el-tag>
          </div>
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无订单" style="margin-top: 60px;"></el-empty>

    <div class="pagination-wrap" v-if="pageInfo.total > 0">
      <el-pagination @current-change="currentChange" :page-size="pageInfo.pageSize"
        :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
    </div>

    <el-dialog v-model="evaluateDialogOpen" title="发表评价" width="500">
      <el-form label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="evaluateForm.rate"></el-rate>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluateForm.content" type="textarea" :rows="4" placeholder="请分享您的购书体验..."></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitEvaluate" style="background:#2d6a4f;border-color:#2d6a4f;">提交评价</el-button>
        <el-button @click="evaluateDialogOpen = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const statusMap = {
  0: { label: '待支付', type: 'warning' },
  1: { label: '待发货', type: 'primary' },
  2: { label: '待收货', type: '' },
  3: { label: '已完成', type: 'success' },
  4: { label: '已取消', type: 'info' },
  5: { label: '申请退款', type: 'danger' },
  6: { label: '退款完成', type: 'info' },
}

const activeTab = ref('all');
const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });
const searchForm = ref({ status: undefined });

getPageList()
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/bookOrder/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function tabChange(tab) {
  searchForm.value.status = tab === 'all' ? undefined : parseInt(tab)
  pageInfo.value.pageNum = 1
  getPageList()
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }

function payOrder(id) {
  ElMessageBox.confirm('确认支付该订单？', '支付确认', { type: 'warning' }).then(() => {
    request.put(`/bookOrder/pay/${id}`).then(res => {
      if (!res) return; ElMessage.success("支付成功"); getPageList()
    })
  }).catch(() => {})
}

function cancelOrder(id) {
  ElMessageBox.confirm('确定取消该订单？', '提示', { type: 'warning' }).then(() => {
    request.put(`/bookOrder/cancel/${id}`).then(res => {
      if (!res) return; ElMessage.success("已取消"); getPageList()
    })
  }).catch(() => {})
}

function receiveOrder(id) {
  ElMessageBox.confirm('确认已收到书籍？', '确认收货', { type: 'success' }).then(() => {
    request.put(`/bookOrder/receive/${id}`).then(res => {
      if (!res) return; ElMessage.success("已确认收货"); getPageList()
    })
  }).catch(() => {})
}

const evaluateDialogOpen = ref(false);
const evaluateForm = ref({ rate: 5, content: '', bookId: null, orderId: null });

function openEvaluate(order) {
  evaluateForm.value = { rate: 5, content: '', bookId: order.bookId, orderId: order.id };
  evaluateDialogOpen.value = true;
}

function submitEvaluate() {
  if (!evaluateForm.value.content.trim()) { ElMessage.warning("请填写评价内容"); return; }
  request.post("/orderEvaluate/add", evaluateForm.value).then(res => {
    if (!res) return;
    evaluateDialogOpen.value = false;
    ElMessage.success("评价成功");
    getPageList();
  })
}

function refundOrder(id) {
  ElMessageBox.confirm('确定申请退款？', '退款申请', { type: 'warning' }).then(() => {
    request.put(`/bookOrder/applyRefund/${id}`).then(res => {
      if (!res) return; ElMessage.success("已提交退款申请"); getPageList()
    })
  }).catch(() => {})
}
</script>

<style scoped>
.order-page { max-width: 900px; margin: 0 auto; padding: 20px; }
.page-title { font-size: 22px; color: #1a1a1a; margin: 0 0 20px; }
.order-card { margin-top: 16px; border-radius: 12px; }
.order-header { display: flex; justify-content: space-between; align-items: center; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
.order-no { font-size: 13px; color: #888; }
.order-body { display: flex; gap: 16px; padding: 16px 0; align-items: center; }
.order-info { flex: 1; }
.order-info h4 { margin: 0 0 6px; font-size: 15px; color: #333; }
.order-info p { margin: 2px 0; font-size: 13px; color: #888; }
.order-price .total { font-size: 20px; font-weight: 700; color: #e74c3c; }
.order-footer { display: flex; justify-content: space-between; align-items: center; padding-top: 12px; border-top: 1px solid #f0f0f0; }
.order-time { font-size: 12px; color: #bbb; }
.order-actions { display: flex; gap: 8px; }
.pagination-wrap { text-align: center; margin-top: 30px; }
</style>
