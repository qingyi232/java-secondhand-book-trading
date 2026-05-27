<template>
  <div>
    <el-card>
      <el-table :data="listData" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="bookName" label="书名" min-width="200"></el-table-column>
        <el-table-column prop="bookPrice" label="价格" width="100">
          <template #default="scope"><span style="color:#e74c3c;">¥{{ scope.row.bookPrice }}</span></template>
        </el-table-column>
        <el-table-column prop="userId" label="用户ID" width="80"></el-table-column>
        <el-table-column prop="createTime" label="浏览时间" width="170"></el-table-column>
      </el-table>
      <div style="margin-top: 20px" v-if="pageInfo.total > 0">
        <el-pagination @current-change="currentChange" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
      </div>
      <el-empty v-if="listData.length === 0" description="暂无浏览记录"></el-empty>
    </el-card>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw, onMounted} from "vue";

const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });

onMounted(() => { getPageList() })

function getPageList() {
  request.get("/browsingHistory/page", { params: toRaw(pageInfo.value) }).then(res => {
    listData.value = res.data.list; pageInfo.value.total = res.data.total
  }).catch(() => { listData.value = [] })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
</script>
