<template>
  <div style="width: 75%; margin: 0 auto; padding: 20px;" :key="activeName">
    <el-card shadow="never" style="border-radius: 12px;">
      <el-tabs v-model="activeName" tab-position="left">
        <el-tab-pane label="收货地址" name="shippingAddress">
          <ShippingAddress v-if="activeName==='shippingAddress'"></ShippingAddress>
        </el-tab-pane>
        <el-tab-pane label="书籍收藏" name="bookCollect">
          <BookCollect v-if="activeName==='bookCollect'"></BookCollect>
        </el-tab-pane>
        <el-tab-pane label="浏览记录" name="browsingHistory">
          <BookBrowsingHistory v-if="activeName==='browsingHistory'"></BookBrowsingHistory>
        </el-tab-pane>
        <el-tab-pane label="关注卖家" name="shopCollect">
          <ShopCollect v-if="activeName==='shopCollect'"></ShopCollect>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useRoute} from "vue-router";
import ShippingAddress from "@/views/front/personalCenter/ShippingAddress.vue";
import BookCollect from "@/views/front/personalCenter/BookCollect.vue";
import BookBrowsingHistory from "@/views/front/personalCenter/BookBrowsingHistory.vue";
import ShopCollect from "@/views/front/personalCenter/ShopCollect.vue";

const activeName = ref('shippingAddress')
const route = useRoute()
if (route.query.type) {
  activeName.value = route.query.type
}
</script>
