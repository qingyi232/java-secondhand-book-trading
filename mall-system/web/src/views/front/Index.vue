<template>
  <div class="home-page">
    <div class="home-container">
      <div class="search-section">
        <h1 class="brand-title">📚 二手书交易平台</h1>
        <p class="brand-desc">让每一本好书都找到新主人</p>
        <div class="search-box">
          <el-input v-model="searchName" placeholder="搜索你想要的书籍..." size="large" class="search-input"
                    @keyup.enter="search">
            <template #append>
              <el-button type="primary" @click="search" style="background: #2d6a4f; border-color: #2d6a4f;">搜索</el-button>
            </template>
          </el-input>
        </div>
        <div class="category-tags">
          <el-tag v-for="cat in categoryList" :key="cat.id" @click="goCategory(cat.id)"
                  class="cat-tag" effect="plain" round>{{ cat.name }}</el-tag>
        </div>
      </div>

      <div class="banner-section" v-if="slideshow.length > 0">
        <el-carousel height="320px" indicator-position="outside" style="border-radius: 12px; overflow: hidden;">
          <el-carousel-item v-for="item in slideshow" :key="item.id" @click="openLink(item.link)">
            <el-image style="width: 100%; height: 320px;" fit="cover" :src="item.mainImg">
              <template #error><div class="banner-placeholder">{{ item.title }}</div></template>
            </el-image>
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="section">
        <div class="section-header">
          <h2>热门书籍</h2>
          <el-button text type="primary" @click="goBookList" style="color: #2d6a4f;">查看更多 →</el-button>
        </div>
        <div class="book-grid">
          <div class="book-card" v-for="item in hotBooks" :key="item.id" @click="goDetail(item.id)">
            <div class="book-cover">
              <el-image :src="item.mainImg" fit="cover" style="width: 100%; height: 180px; border-radius: 8px 8px 0 0;">
                <template #error><div class="img-placeholder">📖</div></template>
              </el-image>
              <el-tag :type="qualityType(item.quality)" size="small" class="quality-badge">{{ item.quality }}</el-tag>
            </div>
            <div class="book-info">
              <h4 class="book-title">{{ item.bookName }}</h4>
              <p class="book-author" v-if="item.author">{{ item.author }}</p>
              <div class="price-row">
                <span class="price">¥{{ item.price }}</span>
                <span class="original" v-if="item.originalPrice">¥{{ item.originalPrice }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="section" v-if="recommendBooks.length > 0">
        <div class="section-header">
          <h2>为你推荐</h2>
        </div>
        <div class="book-grid">
          <div class="book-card" v-for="item in recommendBooks" :key="item.id" @click="goDetail(item.id)">
            <div class="book-cover">
              <el-image :src="item.mainImg" fit="cover" style="width: 100%; height: 180px; border-radius: 8px 8px 0 0;">
                <template #error><div class="img-placeholder">📖</div></template>
              </el-image>
            </div>
            <div class="book-info">
              <h4 class="book-title">{{ item.bookName }}</h4>
              <p class="book-author" v-if="item.author">{{ item.author }}</p>
              <div class="price-row">
                <span class="price">¥{{ item.price }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="announcement-section" v-if="announcements.length > 0">
        <el-card shadow="never" class="announcement-card">
          <template #header><span style="font-weight: 600;">📢 平台公告</span></template>
          <div v-for="item in announcements" :key="item.id" class="announcement-item">
            <strong>{{ item.title }}</strong>
            <p>{{ item.content }}</p>
            <span class="time">{{ item.createTime }}</span>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from "vue";
import request from "@/utils/http.js";
import {useRouter} from "vue-router";

const router = useRouter();
const searchName = ref('');
const categoryList = ref([]);
const slideshow = ref([]);
const hotBooks = ref([]);
const recommendBooks = ref([]);
const announcements = ref([]);

onMounted(() => {
  getCategoryList();
  getSlideshowList();
  getHotBooks();
  getRecommendBooks();
  getAnnouncements();
})

function getCategoryList() {
  request.get("/bookCategory/list").then(res => { categoryList.value = res.data })
}

function getSlideshowList() {
  request.get("/slideshow/page", { params: { pageNum: 1, pageSize: 10 } }).then(res => {
    slideshow.value = res.data.list
  })
}

function getHotBooks() {
  request.get("/book/salesVolumeTop/8").then(res => { hotBooks.value = res.data })
}

function getRecommendBooks() {
  request.get("/book/recommend/4").then(res => { recommendBooks.value = res.data }).catch(() => {})
}

function getAnnouncements() {
  request.get("/announcement/list").then(res => { announcements.value = res.data }).catch(() => {})
}

function search() {
  router.push('/bookList?bookName=' + searchName.value)
}

function goCategory(id) {
  router.push('/bookList?categoryId=' + id)
}

function goBookList() {
  router.push('/bookList')
}

function goDetail(id) {
  router.push('/bookDetails/' + id)
}

function openLink(link) {
  if (link) router.push(link)
}

function qualityType(q) {
  if (q === '全新') return 'success'
  if (q === '九成新') return 'primary'
  if (q === '八成新') return 'warning'
  return 'info'
}
</script>

<style scoped>
.home-page { background: #f8faf8; min-height: 100vh; }
.home-container { max-width: 1100px; margin: 0 auto; padding: 20px; }

.search-section { text-align: center; padding: 40px 0 30px; }
.brand-title { font-size: 32px; color: #1a1a2e; margin: 0 0 8px; font-weight: 700; }
.brand-desc { font-size: 16px; color: #888; margin: 0 0 24px; }
.search-box { max-width: 600px; margin: 0 auto 20px; }
.search-input :deep(.el-input__wrapper) { border-radius: 24px; padding: 4px 4px 4px 20px; }
.category-tags { display: flex; justify-content: center; flex-wrap: wrap; gap: 10px; }
.cat-tag { cursor: pointer; border-color: #2d6a4f; color: #2d6a4f; transition: all 0.2s; }
.cat-tag:hover { background: #2d6a4f; color: #fff; }

.banner-section { margin-bottom: 30px; }
.banner-placeholder { width: 100%; height: 320px; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #d4edda, #c3e6cb); font-size: 24px; color: #2d6a4f; font-weight: 600; }

.section { margin-bottom: 30px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.section-header h2 { font-size: 20px; color: #1a1a2e; margin: 0; }

.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
.book-card { background: #fff; border-radius: 12px; overflow: hidden; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.book-card:hover { transform: translateY(-4px); box-shadow: 0 8px 20px rgba(0,0,0,0.1); }
.book-cover { position: relative; }
.quality-badge { position: absolute; top: 8px; right: 8px; }
.book-info { padding: 10px 12px; }
.book-title { font-size: 14px; color: #333; margin: 0 0 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.book-author { font-size: 12px; color: #999; margin: 0 0 6px; }
.price-row { display: flex; align-items: baseline; gap: 6px; }
.price { font-size: 18px; font-weight: 700; color: #e74c3c; }
.original { font-size: 12px; color: #ccc; text-decoration: line-through; }
.img-placeholder { width: 100%; height: 180px; display: flex; align-items: center; justify-content: center; background: #f0f5f0; font-size: 48px; }

.announcement-card { border-radius: 12px; }
.announcement-item { padding: 12px 0; border-bottom: 1px solid #f5f5f5; }
.announcement-item:last-child { border-bottom: none; }
.announcement-item strong { color: #333; font-size: 15px; }
.announcement-item p { margin: 6px 0; color: #666; font-size: 14px; line-height: 1.6; }
.announcement-item .time { font-size: 12px; color: #bbb; }
</style>
