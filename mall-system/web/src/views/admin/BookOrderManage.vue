<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="订单号" prop="orderNo">
            <el-input v-model="searchForm.orderNo" clearable placeholder="搜索订单号"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 130px">
              <el-option label="待支付" :value="0"></el-option>
              <el-option label="待发货" :value="1"></el-option>
              <el-option label="待收货" :value="2"></el-option>
              <el-option label="已完成" :value="3"></el-option>
              <el-option label="已取消" :value="4"></el-option>
              <el-option label="申请退款" :value="5"></el-option>
              <el-option label="退款完成" :value="6"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card>
        <el-table :data="listData" style="width: 100%" border>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="orderNo" label="订单号" width="160"></el-table-column>
          <el-table-column prop="bookName" label="书名" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="userName" label="买家" width="90"></el-table-column>
          <el-table-column prop="shopName" label="卖家" width="90"></el-table-column>
          <el-table-column prop="quantity" label="数量" width="60"></el-table-column>
          <el-table-column prop="totalMoney" label="金额" width="80">
            <template #default="scope"><span style="color:#e74c3c;font-weight:bold;">¥{{ scope.row.totalMoney }}</span></template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90">
            <template #default="scope">
              <el-tag :type="statusMap[scope.row.status].type" size="small">{{ statusMap[scope.row.status].label }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="trackingNumber" label="物流单号" width="120"></el-table-column>
          <el-table-column prop="consigneeName" label="收货人" width="80"></el-table-column>
          <el-table-column prop="createTime" label="下单时间" width="160"></el-table-column>
          <el-table-column fixed="right" label="操作" width="220">
            <template #default="scope">
              <el-button type="primary" size="small" @click="shipOrder(scope.row)" v-if="scope.row.status === 1">发货</el-button>
              <el-button type="success" size="small" @click="approveRefund(scope.row.id)" v-if="scope.row.status === 5">同意退款</el-button>
              <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
          <el-pagination @current-change="currentChange" @size-change="sizeChange" :page-size="pageInfo.pageSize"
            :current-page="pageInfo.pageNum" background layout="total,sizes, prev, pager, next" :total="pageInfo.total"></el-pagination>
        </div>
      </el-card>
    </el-space>

    <el-dialog v-model="shipDialogOpen" title="填写物流信息" width="400">
      <el-form label-width="80px">
        <el-form-item label="物流单号">
          <el-input v-model="trackingNumber" placeholder="请输入物流单号"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="confirmShip">确认发货</el-button>
        <el-button @click="shipDialogOpen = false">取消</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogOpen" title="订单详情" width="500">
      <el-descriptions v-if="currentOrder" :column="1" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="书名">{{ currentOrder.bookName }}</el-descriptions-item>
        <el-descriptions-item label="买家">{{ currentOrder.userName }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ currentOrder.totalMoney }}</el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrder.consigneeName }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ currentOrder.consigneeTel }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ currentOrder.consigneeAddress }}</el-descriptions-item>
        <el-descriptions-item label="物流">{{ currentOrder.trackingNumber || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentOrder.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Search, Refresh} from '@element-plus/icons-vue'
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

const searchFormComponents = ref();
const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });
const searchForm = ref({ orderNo: undefined, status: undefined });

const shipDialogOpen = ref(false);
const detailDialogOpen = ref(false);
const trackingNumber = ref('');
const currentOrder = ref(null);
const shipOrderId = ref(null);

getPageList()
function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/bookOrder/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; getPageList() }
function search() { pageInfo.value.pageNum = 1; getPageList() }
function resetSearch() { searchFormComponents.value.resetFields(); getPageList() }

function shipOrder(order) {
  shipOrderId.value = order.id;
  trackingNumber.value = '';
  shipDialogOpen.value = true;
}

function confirmShip() {
  if (!trackingNumber.value.trim()) { ElMessage.warning("请输入物流单号"); return; }
  request.put(`/bookOrder/ship/${shipOrderId.value}?trackingNumber=${trackingNumber.value}`).then(res => {
    if (!res) return;
    shipDialogOpen.value = false;
    ElMessage.success("发货成功");
    getPageList();
  })
}

function approveRefund(id) {
  ElMessageBox.confirm('确定同意退款？', '退款确认', { type: 'warning' }).then(() => {
    request.put(`/bookOrder/approveRefund/${id}`).then(res => {
      if (!res) return; ElMessage.success("退款成功"); getPageList()
    })
  }).catch(() => {})
}

function viewDetail(order) {
  currentOrder.value = order;
  detailDialogOpen.value = true;
}
</script>
