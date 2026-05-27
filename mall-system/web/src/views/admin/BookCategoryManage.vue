<template>
  <div>
    <el-space direction="vertical" alignment="left" style="width: 100%">
      <el-card>
        <el-button type="primary" @click="add" :icon="Plus">新增分类</el-button>
      </el-card>
      <el-card>
        <el-table :data="listData" style="width: 100%" border>
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="name" label="分类名称"></el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button :icon="Edit" @click="edit(scope.row)">编辑</el-button>
              <el-button :icon="Delete" type="danger" @click="deleteItem(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-space>
    <el-dialog v-model="dialogOpen" :title="formData.id?'编辑分类':'新增分类'" width="500">
      <el-form ref="formRef" :model="formData" label-width="100px">
        <el-form-item label="分类名称" prop="name" :rules="[{required:true,message:'不能为空',trigger:['blur']}]">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button @click="dialogOpen = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/http.js";
import {Delete, Edit, Plus} from '@element-plus/icons-vue'
import {ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const listData = ref([]);
const dialogOpen = ref(false);
const formData = ref({});
const formRef = ref();

getList()
function getList() {
  request.get("/bookCategory/list").then(res => { listData.value = res.data })
}

function add() { formData.value = {}; dialogOpen.value = true }
function edit(row) { formData.value = Object.assign({}, row); dialogOpen.value = true }

function submit() {
  formRef.value.validate((valid) => {
    if (!valid) return
    if (!formData.value.id) {
      request.post("/bookCategory/add", formData.value).then(res => {
        if (!res) return; dialogOpen.value = false; ElMessage.success("操作成功"); getList()
      })
    } else {
      request.put("/bookCategory/update", formData.value).then(res => {
        if (!res) return; dialogOpen.value = false; ElMessage.success("操作成功"); getList()
      })
    }
  })
}

function deleteItem(row) {
  ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' }).then(() => {
    request.delete(`/bookCategory/delete/${row.id}`).then(res => {
      if (!res) return; ElMessage.success("删除成功"); getList()
    })
  }).catch(() => {})
}
</script>
