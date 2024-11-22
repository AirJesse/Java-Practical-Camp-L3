<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">

        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!--表单组件-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="500px"
    >
      <el-form
        ref="form"
        inline
        :model="form"
        :rules="rules"
        size="small"
        label-width="80px"
      >
        <el-form-item label="姓名" :label-width="formLabelWidth">
          <el-input v-model="form.userName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="余额" :label-width="formLabelWidth">
          <el-input v-model="form.money" autocomplete="off" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button
          type="primary"
          @click="add"
        >确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      lazy
      :data="data"
      row-key="id"
    >
      <el-table-column label="编号" prop="id" />
      <el-table-column label="姓名" prop="userName" />
      <el-table-column label="余额" prop="money" />
    </el-table>
  </div>
</template>

<script>
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import api from '@/api/system/bank'

const defaultForm = {

}
export default {
  name: 'Dept',
  components: {
    crudOperation
  },
  cruds() {
    return CRUD({
      url: 'bank/pageInfo',
      method: 'post',
      optShow: {
        add: true,
        edit: false,
        del: false,
        download: false,
        reset: false
      }
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      data: [],
      formLabelWidth: '80',
      rules: {
        userName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        money: [{ required: true, message: '请输入名称', trigger: 'blur' }]
      },
      permission: {
      },
      form: {
        userName: '',
        money: ''
      }
    }
  },
  mounted() {
    api.find().then(res => {
      this.data = res.data
    })
  },
  methods: {
    add() {
      api.add(this.form).then(res => {
        if (res.code === 0) {
          this.$message.success('新增成功')
          api.find().then(res => {
            this.data = res.data
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .vue-treeselect__control,
::v-deep .vue-treeselect__placeholder,
::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
