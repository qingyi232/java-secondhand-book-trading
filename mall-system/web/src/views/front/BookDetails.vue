<template>
  <div class="book-detail-page" v-if="book">
    <el-card shadow="never" class="detail-card">
      <el-row :gutter="40">
        <el-col :span="8">
          <el-image :src="book.mainImg" fit="contain" style="width: 100%; max-height: 400px; border-radius: 8px; background: #f9f9f9;">
            <template #error><div class="no-img">暂无图片</div></template>
          </el-image>
        </el-col>
        <el-col :span="16">
          <h1 class="detail-title">{{ book.bookName }}</h1>
          <div class="detail-meta">
            <span v-if="book.author"><strong>作者：</strong>{{ book.author }}</span>
            <span v-if="book.publisher"><strong>出版社：</strong>{{ book.publisher }}</span>
            <span v-if="book.isbn"><strong>ISBN：</strong>{{ book.isbn }}</span>
          </div>
          <div class="price-section">
            <span class="detail-price">¥{{ book.price }}</span>
            <span class="detail-original" v-if="book.originalPrice">原价 ¥{{ book.originalPrice }}</span>
            <el-tag :type="book.quality==='全新'?'success':book.quality==='九成新'?'primary':'warning'" style="margin-left: 12px;">{{ book.quality }}</el-tag>
          </div>
          <div class="detail-stats">
            <span>库存：{{ book.stock }}</span>
            <span>销量：{{ book.salesVolume }}</span>
            <span>卖家：<router-link to="#" style="color: #2d6a4f;">{{ book.shopName }}</router-link></span>
          </div>
          <div class="detail-intro" v-if="book.intro">
            <h4>书籍描述</h4>
            <p>{{ book.intro }}</p>
          </div>
          <div class="detail-actions">
            <el-button type="primary" size="large" @click="addToCart" style="background: #2d6a4f; border-color: #2d6a4f;" :disabled="book.stock <= 0">
              {{ book.stock > 0 ? '加入购物车' : '已售罄' }}
            </el-button>
            <el-button size="large" @click="buyNow" :disabled="book.stock <= 0">立即购买</el-button>
            <el-button :icon="book.bookCollectId ? StarFilled : Star" @click="toggleCollect" :type="book.bookCollectId ? 'warning' : 'default'" circle size="large"></el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px; border-radius: 12px;">
      <h3 style="margin: 0 0 16px;">买家评价（{{ evaluateList.length }}）</h3>
      <div v-if="evaluateList.length === 0" style="text-align: center; padding: 30px 0; color: #999;">暂无评价</div>
      <div v-for="item in evaluateList" :key="item.id" class="evaluate-item">
        <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 4px;">
          <span style="font-weight: 600; color: #333;">{{ item.userName || '匿名用户' }}</span>
          <el-rate v-model="item.rate" disabled></el-rate>
        </div>
        <p>{{ item.content }}</p>
        <span class="evaluate-time">{{ item.createTime }}</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, onMounted} from "vue";
import {useRoute, useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {Star, StarFilled} from "@element-plus/icons-vue";
import tools from "@/utils/tools.js";

const route = useRoute();
const router = useRouter();
const book = ref(null);
const evaluateList = ref([]);
const bookId = route.params.id;

onMounted(() => {
  getBookDetail();
  getEvaluateList();
})

function getBookDetail() {
  request.get(`/book/selectById/${bookId}`).then(res => {
    book.value = res.data;
  })
}

function getEvaluateList() {
  request.get(`/orderEvaluate/listByBookId/${bookId}`).then(res => {
    if (res && res.data) {
      evaluateList.value = res.data;
    }
  })
}

function addToCart() {
  if (!tools.isLogin()) { router.push('/login'); return; }
  request.post("/shoppingCart/add", { bookId: bookId, quantity: 1 }).then(res => {
    if (!res) return;
    ElMessage.success("已加入购物车");
  })
}

function buyNow() {
  if (!tools.isLogin()) { router.push('/login'); return; }
  addToCart();
  router.push('/shoppingCart');
}

function toggleCollect() {
  if (!tools.isLogin()) { router.push('/login'); return; }
  if (book.value.bookCollectId) {
    request.delete(`/bookCollect/delete/${book.value.bookCollectId}`).then(res => {
      if (!res) return;
      book.value.bookCollectId = null;
      ElMessage.success("已取消收藏");
    })
  } else {
    request.post("/bookCollect/add", { bookId: bookId }).then(res => {
      if (!res) return;
      getBookDetail();
      ElMessage.success("收藏成功");
    })
  }
}
</script>

<style scoped>
.book-detail-page { max-width: 1100px; margin: 0 auto; padding: 20px; }
.detail-card { border-radius: 12px; }
.detail-title { font-size: 24px; color: #1a1a1a; margin: 0 0 16px; line-height: 1.4; }
.detail-meta { display: flex; flex-direction: column; gap: 6px; color: #666; font-size: 14px; margin-bottom: 20px; }
.price-section { background: #fef9ef; padding: 16px 20px; border-radius: 8px; margin-bottom: 16px; display: flex; align-items: baseline; }
.detail-price { font-size: 32px; font-weight: 700; color: #e74c3c; margin-right: 12px; }
.detail-original { font-size: 16px; color: #bbb; text-decoration: line-through; }
.detail-stats { display: flex; gap: 24px; color: #888; font-size: 14px; margin-bottom: 16px; }
.detail-intro { margin-bottom: 24px; }
.detail-intro h4 { color: #333; margin: 0 0 8px; }
.detail-intro p { color: #666; line-height: 1.8; margin: 0; }
.detail-actions { display: flex; gap: 12px; }
.no-img { width: 100%; height: 300px; display: flex; align-items: center; justify-content: center; background: #f5f5f5; color: #ccc; }
.evaluate-item { padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.evaluate-item p { margin: 8px 0 4px; color: #333; }
.evaluate-time { font-size: 12px; color: #bbb; }
</style>
