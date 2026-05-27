<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-table :data="listData" style="width: 100%" border>
          <el-table-column prop="id" label="ID" width="60"></el-table-column>
          <el-table-column prop="bookName" label="书名" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="userName" label="评价人" width="100"></el-table-column>
          <el-table-column prop="content" label="评价内容" min-width="200" show-overflow-tooltip></el-table-column>
          <el-table-column prop="rate" label="评分" width="160">
            <template #default="scope">
              <el-rate v-model="scope.row.rate" disabled></el-rate>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="评价时间" width="170"></el-table-column>
        </el-table>
        <div style="margin-top: 20px" v-if="pageInfo.total > 0">
          <el-pagination @current-change="currentChange" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" background layout="total, prev, pager, next" :total="pageInfo.total"></el-pagination>
        </div>
        <el-empty v-if="listData.length === 0" description="暂无评价"></el-empty>
      </el-card>
    </el-space>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, toRaw, onMounted} from "vue";

const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });

onMounted(() => { getPageList() })

function getPageList() {
  request.get("/orderEvaluate/page", { params: toRaw(pageInfo.value) }).then(res => {
    listData.value = res.data.list
    pageInfo.value.total = res.data.total
  }).catch(() => {
    listData.value = []
  })
}

function currentChange(e) { pageInfo.value.pageNum = e; getPageList() }
</script>
