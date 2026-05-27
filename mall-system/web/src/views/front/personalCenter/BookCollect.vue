<template>
  <div>
    <el-table :data="listData" style="width: 100%">
      <el-table-column type="selection" width="45"></el-table-column>
      <el-table-column label="书籍封面" width="80">
        <template #default="scope">
          <el-image :src="scope.row.bookMainImg" style="width: 50px; height: 65px;" fit="cover"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="bookName" label="书名"></el-table-column>
      <el-table-column prop="bookPrice" label="价格" width="100">
        <template #default="scope"><span style="color: #e74c3c;">¥{{ scope.row.bookPrice }}</span></template>
      </el-table-column>
      <el-table-column prop="createTime" label="收藏时间" width="180"></el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="small" @click="removeCollect(scope.row)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 16px; text-align: center;" v-if="pageInfo.total > 0">
      <el-pagination @current-change="currentChange" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
    </div>
    <el-empty v-if="listData.length === 0" description="暂无收藏"></el-empty>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw} from "vue";
import {ElMessage} from "element-plus";

const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });

getPageList()
function getPageList() {
  request.get("/bookCollect/page", { params: toRaw(pageInfo.value) }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }

function removeCollect(row) {
  request.delete(`/bookCollect/delete/${row.id}`).then(res => {
    if (!res) return; ElMessage.success("已取消收藏"); getPageList()
  })
}
</script>
