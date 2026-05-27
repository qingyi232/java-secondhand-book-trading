<template>
  <div class="book-list-page">
    <div class="search-bar">
      <el-card shadow="never" class="filter-card">
        <el-form inline>
          <el-form-item label="书名">
            <el-input v-model="searchForm.bookName" placeholder="搜索书名" clearable style="width: 180px" @keyup.enter="search"></el-input>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="searchForm.author" placeholder="搜索作者" clearable style="width: 140px"></el-input>
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.categoryId" placeholder="全部分类" clearable style="width: 140px">
              <el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in categoryList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="品相">
            <el-select v-model="searchForm.quality" placeholder="全部品相" clearable style="width: 120px">
              <el-option label="全新" value="全新"></el-option>
              <el-option label="九成新" value="九成新"></el-option>
              <el-option label="八成新" value="八成新"></el-option>
              <el-option label="七成新" value="七成新"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search" style="background: #2d6a4f; border-color: #2d6a4f;">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <div class="book-grid" v-if="listData.length > 0">
      <div class="book-card" v-for="item in listData" :key="item.id" @click="goDetail(item.id)">
        <div class="book-cover">
          <el-image :src="item.mainImg" fit="cover" style="width: 100%; height: 200px; border-radius: 8px 8px 0 0;">
            <template #error><div class="image-placeholder">暂无图片</div></template>
          </el-image>
          <el-tag :type="item.quality==='全新'?'success':item.quality==='九成新'?'':item.quality==='八成新'?'warning':'info'" size="small" class="quality-tag">{{ item.quality }}</el-tag>
        </div>
        <div class="book-info">
          <h4 class="book-title">{{ item.bookName }}</h4>
          <p class="book-author" v-if="item.author">{{ item.author }}</p>
          <div class="book-price-row">
            <span class="price">¥{{ item.price }}</span>
            <span class="original-price" v-if="item.originalPrice">¥{{ item.originalPrice }}</span>
          </div>
          <p class="book-shop">{{ item.shopName }}</p>
        </div>
      </div>
    </div>
    <el-empty v-else description="暂无书籍" style="margin-top: 60px;"></el-empty>

    <div class="pagination-wrap" v-if="pageInfo.total > 0">
      <el-pagination @current-change="currentChange" @size-change="sizeChange" :page-size="pageInfo.pageSize"
        :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw, onMounted} from "vue";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
const listData = ref([]);
const categoryList = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 12, total: 0 });
const searchForm = ref({
  bookName: route.query.bookName || undefined,
  author: undefined,
  categoryId: route.query.categoryId ? parseInt(route.query.categoryId) : undefined,
  quality: undefined
});

onMounted(() => {
  getCategoryList();
  getPageList();
})

function getCategoryList() {
  request.get("/bookCategory/list").then(res => { categoryList.value = res.data })
}

function getPageList() {
  let data = Object.assign(toRaw(searchForm.value), toRaw(pageInfo.value))
  request.get("/book/page", { params: data }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
function sizeChange(e) { pageInfo.value.pageSize = e; getPageList() }
function search() { pageInfo.value.pageNum = 1; getPageList() }
function resetSearch() {
  searchForm.value = { bookName: undefined, author: undefined, categoryId: undefined, quality: undefined }
  getPageList()
}
function goDetail(id) { router.push(`/bookDetails/${id}`) }
</script>

<style scoped>
.book-list-page { max-width: 1200px; margin: 0 auto; padding: 20px; }
.filter-card { margin-bottom: 20px; border-radius: 12px; }
.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}
.book-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}
.book-cover { position: relative; }
.quality-tag { position: absolute; top: 8px; right: 8px; }
.book-info { padding: 12px 14px; }
.book-title {
  font-size: 14px;
  color: #333;
  margin: 0 0 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.book-author { font-size: 12px; color: #888; margin: 0 0 8px; }
.book-price-row { display: flex; align-items: baseline; gap: 8px; margin-bottom: 4px; }
.price { font-size: 18px; font-weight: 700; color: #e74c3c; }
.original-price { font-size: 12px; color: #bbb; text-decoration: line-through; }
.book-shop { font-size: 12px; color: #52c41a; margin: 4px 0 0; }
.image-placeholder {
  width: 100%; height: 200px; display: flex; align-items: center; justify-content: center;
  background: #f5f5f5; color: #ccc; font-size: 14px;
}
.pagination-wrap { text-align: center; margin-top: 30px; }
</style>
