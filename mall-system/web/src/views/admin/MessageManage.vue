<template>
  <div>
    <el-card>
      <el-table :data="listData" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="fromUserType" label="发送方类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.fromUserType === 'USER' ? 'primary' : 'success'" size="small">{{ scope.row.fromUserType === 'USER' ? '买家' : '卖家' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fromUserId" label="发送方ID" width="80"></el-table-column>
        <el-table-column prop="toUserType" label="接收方类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.toUserType === 'USER' ? 'primary' : 'success'" size="small">{{ scope.row.toUserType === 'USER' ? '买家' : '卖家' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="toUserId" label="接收方ID" width="80"></el-table-column>
        <el-table-column prop="content" label="消息内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="bookName" label="关联书籍" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="isRead" label="已读" width="70">
          <template #default="scope">
            <el-tag :type="scope.row.isRead === 1 ? 'success' : 'warning'" size="small">{{ scope.row.isRead === 1 ? '已读' : '未读' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发送时间" width="170"></el-table-column>
      </el-table>
      <el-empty v-if="listData.length === 0" description="暂无消息"></el-empty>
    </el-card>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {ref, onMounted} from "vue";

const listData = ref([]);

onMounted(() => {
  request.get("/message/list").then(res => {
    listData.value = res.data || []
  }).catch(() => { listData.value = [] })
})
</script>
