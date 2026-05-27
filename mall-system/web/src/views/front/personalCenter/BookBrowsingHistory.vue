<template>
  <div>
    <el-table :data="listData" style="width: 100%">
      <el-table-column label="书籍封面" width="80">
        <template #default="scope">
          <el-image :src="scope.row.bookMainImg" style="width: 50px; height: 65px;" fit="cover"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="bookName" label="书名"></el-table-column>
      <el-table-column prop="bookPrice" label="价格" width="100">
        <template #default="scope"><span style="color: #e74c3c;">¥{{ scope.row.bookPrice }}</span></template>
      </el-table-column>
      <el-table-column prop="createTime" label="浏览时间" width="180"></el-table-column>
    </el-table>
    <div style="margin-top: 16px; text-align: center;" v-if="pageInfo.total > 0">
      <el-pagination @current-change="currentChange" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
    </div>
    <el-empty v-if="listData.length === 0" description="暂无浏览记录"></el-empty>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";

const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });

getPageList()
function getPageList() {
  request.get("/browsingHistory/page", { params: toRaw(pageInfo.value) }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
</script>
