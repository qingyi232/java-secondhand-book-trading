<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-form ref="searchFormComponents" :model="searchForm" inline>
          <el-form-item label="书名" prop="bookName">
            <el-input v-model="searchForm.bookName" clearable placeholder="搜索书名"></el-input>
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input v-model="searchForm.author" clearable placeholder="搜索作者"></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="categoryId">
            <el-select v-model="searchForm.categoryId" placeholder="请选择" clearable filterable style="width: 150px">
              <el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in categoryList"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核状态" prop="reviewStatus">
            <el-select v-model="searchForm.reviewStatus" placeholder="请选择" clearable style="width: 120px">
              <el-option label="待审核" value="待审核"></el-option>
              <el-option label="已通过" value="已通过"></el-option>
              <el-option label="已驳回" value="已驳回"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="search">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
        <el-space>
          <el-button type="primary" @click="add" :icon="Plus">发布书籍</el-button>
          <el-button type="danger" :icon="Delete" @click="batchDelete(null)" :disabled="selectionRows.length<=0">批量删除</el-button>
        </el-space>
      </el-card>
      <el-card>
        <el-table ref="tableComponents" :data="listData" tooltip-effect="dark" style="width: 100%" @selection-change="selectionChange" border>
          <el-table-column type="selection" width="45"></el-table-column>
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="bookName" label="书名" min-width="180"></el-table-column>
          <el-table-column prop="mainImg" label="封面" width="80">
            <template #default="scope">
              <el-image v-if="scope.row.mainImg" style="width: 50px; height: 65px; object-fit: cover;" :src="scope.row.mainImg" :preview-src-list="[scope.row.mainImg]" :preview-teleported="true" fit="cover"></el-image>
            </template>
          </el-table-column>
          <el-table-column prop="author" label="作者" width="100"></el-table-column>
          <el-table-column prop="categoryName" label="分类" width="90"></el-table-column>
          <el-table-column prop="quality" label="品相" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.quality==='全新'?'success':scope.row.quality==='九成新'?'primary':scope.row.quality==='八成新'?'warning':'info'" size="small">{{ scope.row.quality }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="售价" width="70">
            <template #default="scope">
              <span style="color: #e74c3c; font-weight: bold;">¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="60"></el-table-column>
          <el-table-column prop="salesVolume" label="销量" width="60"></el-table-column>
          <el-table-column prop="shopName" label="卖家" width="90"></el-table-column>
          <el-table-column prop="reviewStatus" label="审核" width="80">
            <template #default="scope">
              <el-tag v-if="scope.row.reviewStatus === '已通过'" type="success" size="small">已通过</el-tag>
              <el-tag v-else-if="scope.row.reviewStatus === '已驳回'" type="danger" size="small">已驳回</el-tag>
              <el-tag v-else type="warning" size="small">待审核</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间" width="160"></el-table-column>
          <el-table-column fixed="right" label="操作" width="280">
            <template #default="scope">
              <el-button type="success" size="small" @click="reviewBook(scope.row.id, '已通过')" v-if="scope.row.reviewStatus === '待审核'">通过</el-button>
              <el-button type="warning" size="small" @click="reviewBook(scope.row.id, '已驳回')" v-if="scope.row.reviewStatus === '待审核'">驳回</el-button>
              <el-button :icon="Edit" size="small" @click="edit(scope.$index, scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" size="small" @click="deleteOne(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px">
          <el-pagination @current-change="currentChange" @size-change="sizeChange" :page-size="pageInfo.pageSize" :current-page="pageInfo.pageNum" background layout="total,sizes, prev, pager, next" :total="pageInfo.total"></el-pagination>
        </div>
      </el-card>
    </el-space>
    <el-dialog v-model="dialogOpen" v-if="dialogOpen" :title="formData.id?'编辑书籍':'发布书籍'" width="800">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="书名" prop="bookName" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
              <el-input v-model="formData.bookName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="formData.author"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出版社" prop="publisher">
              <el-input v-model="formData.publisher"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model="formData.isbn"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="分类" prop="categoryId" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
              <el-select v-model="formData.categoryId" placeholder="请选择" filterable>
                <el-option :label="item.name" :value="item.id" :key="item.id" v-for="item in categoryList"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="品相" prop="quality" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
              <el-select v-model="formData.quality" placeholder="请选择">
                <el-option label="全新" value="全新"></el-option>
                <el-option label="九成新" value="九成新"></el-option>
                <el-option label="八成新" value="八成新"></el-option>
                <el-option label="七成新" value="七成新"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="库存" prop="stock" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
              <el-input-number v-model="formData.stock" :min="0"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="原价" prop="originalPrice">
              <el-input v-model="formData.originalPrice" placeholder="¥"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="售价" prop="price" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
              <el-input v-model="formData.price" placeholder="¥"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图" prop="mainImg" :rules="[{required:true,message:'不能为空',trigger:['blur','change']}]">
          <MyUpLoad type="imageCar" :limit="1" :files="formData.mainImg" @setFiles="formData.mainImg=$event" v-if="dialogOpen"></MyUpLoad>
        </el-form-item>
        <el-form-item label="详细图片" prop="imgList">
          <MyUpLoad type="image" :limit="5" :files="formData.imgList" @setFiles="formData.imgList=$event" v-if="dialogOpen"></MyUpLoad>
        </el-form-item>
        <el-form-item label="书籍简介" prop="intro">
          <el-input v-model="formData.intro" type="textarea" :rows="4" placeholder="请描述书籍品相、使用情况等"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submit" :icon="Check">提交</el-button>
        <el-button @click="closeDialog" :icon="Close">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Check, Close, Delete, Edit, Refresh, Plus, Search} from '@element-plus/icons-vue'
import {ref, toRaw} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import MyUpLoad from "@/components/MyUpload.vue";

const searchFormComponents = ref();
const tableComponents = ref();
const listData = ref([]);
const pageInfo = ref({ pageNum: 1, pageSize: 10, total: 0 });
const searchForm = ref({ bookName: undefined, author: undefined, categoryId: undefined, reviewStatus: undefined });

const categoryList = ref([])
getCategoryList()
function getCategoryList() {
  request.get("/bookCategory/list").then(res => { categoryList.value = res.data; })
}

getPageList()
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
function resetSearch() { searchFormComponents.value.resetFields(); getPageList() }

const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();

function add() { formData.value = { stock: 1, quality: '九成新' }; dialogOpen.value = true }
function edit(index, row) { formData.value = Object.assign({}, row); dialogOpen.value = true }
function closeDialog() { dialogOpen.value = false }

function submit() {
  formRef.value.validate((valid) => {
    if (!valid) { ElMessage({ message: "请检查表单!", type: 'warning' }); return }
    if (!formData.value.id) {
      request.post("/book/add", formData.value).then(res => {
        if (!res) return; dialogOpen.value = false;
        ElMessage({ message: "发布成功，等待审核", type: 'success' }); getPageList()
      })
    } else {
      request.put("/book/update", formData.value).then(res => {
        if (!res) return; dialogOpen.value = false;
        ElMessage({ message: "更新成功", type: 'success' }); getPageList()
      })
    }
  })
}

function reviewBook(id, reviewStatus) {
  let msg = reviewStatus === '已通过' ? '确定通过该书籍审核？' : '确定驳回该书籍？'
  ElMessageBox.confirm(msg, '审核确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => {
      request.put(`/book/review/${id}?reviewStatus=${reviewStatus}`).then(res => {
        if (!res) return; ElMessage({ message: '审核操作成功', type: 'success' }); getPageList()
      })
    }).catch(() => {})
}

const selectionRows = ref([]);
function selectionChange(rows) { selectionRows.value = rows }
function deleteOne(index, row) { batchDelete([row]) }
function batchDelete(rows) {
  if (!rows) rows = selectionRows.value;
  let ids = rows.map(item => item.id);
  ElMessageBox.confirm(`确定删除这${ids.length}本书籍吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => {
      request.delete("/book/delBatch", {data: ids}).then(res => {
        if (!res) return; ElMessage({ message: "删除成功", type: 'success' }); getPageList()
      })
    }).catch(() => { tableComponents.value.clearSelection() })
}
</script>
