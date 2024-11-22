<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" />
    </div>
    <!-- 表单渲染 -->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="520px">
      <el-form ref="form" :inline="true" :model="form" size="small" label-width="80px">
        <el-form-item label="名称" prop="clientName">
          <el-input v-model="form.clientName" style="width: 380px;" />
        </el-form-item>

        <el-form-item label="密码" prop="clientSecret">
          <el-input v-model="form.clientSecret" :disabled="form.id!=null" style="width: 380px;" rows="5" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>

    <!--表格渲染-->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="clientId" label="客户端ID" />
      <el-table-column prop="clientName" label="客户端名称" />
      <!--   编辑与删除   -->
      <el-table-column
        v-if="checkPer(['sys:client'])"
        label="操作"
        width="130px"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
          />
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import crudClient from '@/api/resource/client'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import udOperation from '@crud/UD.operation'
const defaultForm = { id: null, clientId: null, clientName: null, clientSecret: null }
export default {
  name: 'Client',
  components: { crudOperation, pagination, udOperation },
  cruds() {
    return CRUD({
      title: '客户端资源',
      url: 'client/page',
      crudMethod: { ...crudClient },
      optShow: {
        add: true,
        edit: false,
        del: false,
        download: false,
        reset: true
      }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      permission: {
        add: ['sys:client', 'sys:client:add'],
        edit: ['sys:client', 'sys:client:edit'],
        del: ['sys:client', 'sys:client:del']
      }
    }
  },
  methods: {
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
</style>
