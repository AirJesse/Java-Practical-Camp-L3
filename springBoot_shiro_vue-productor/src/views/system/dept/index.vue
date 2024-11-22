<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input
          v-model="query.name"
          clearable
          size="small"
          placeholder="输入部门名称搜索"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="crud.toQuery"
        />
        <el-select
          v-model="query.status"
          clearable
          size="small"
          placeholder="状态"
          class="filter-item"
          style="width: 90px"
          @change="crud.toQuery"
        >
          <el-option
            v-for="item in statusTypeOptions"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>
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
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" style="width: 370px" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio
            v-for="item in statusTypeOptions"
            :key="item.key"
            v-model="form.status"
            :label="item.key"
          >{{ item.display_name }}</el-radio>
        </el-form-item>
        <el-form-item
          style="margin-bottom: 0"
          label="上级部门"
          prop="pid"
        >
          <treeselect
            v-model="form.pid"
            :load-options="loadDepts"
            :options="depts"
            style="width: 370px"
            placeholder="选择上级类目"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button
          :loading="crud.status.cu === 2"
          type="primary"
          @click="crud.submitCU"
        >确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      lazy
      :load="getDeptDatas"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :data="crud.data"
      row-key="id"
      @select="crud.selectChange"
      @select-all="crud.selectAllChange"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column :selectable="checkboxT" type="selection" width="55" />
      <el-table-column label="名称" prop="name" />
      <el-table-column label="编号" prop="deptNo" />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        :formatter="formatterStatus"
      />
      <el-table-column prop="createTime" label="创建日期" />
      <el-table-column
        v-if="checkPer(['sys:dept:update','sys:dept:deleted'])"
        label="操作"
        width="130px"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <udOperation
            :data="scope.row"
            :permission="permission"
            :disabled-dle="scope.row.id === 1"
            msg="确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！"
          />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import crudDept from '@/api/system/dept'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'

const defaultForm = {
  id: null,
  name: null,
  pid: null,
  status: '1'
}
export default {
  name: 'Dept',
  components: {
    Treeselect,
    crudOperation,
    rrOperation,
    udOperation
  },
  cruds() {
    return CRUD({
      title: '部门',
      url: 'sys/deptForVue',
      crudMethod: { ...crudDept },
      method: 'post',
      optShow: {
        add: true,
        edit: true,
        del: true,
        download: false,
        reset: true
      }
    })
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      depts: [],
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        deptSort: [
          {
            required: true,
            message: '请输入序号',
            trigger: 'blur',
            type: 'number'
          }
        ]
      },
      permission: {
        add: ['sys:dept:add'],
        edit: ['sys:dept:update'],
        del: ['sys:dept:deleted']
      },
      statusTypeOptions: [
        { key: '1', display_name: '正常' },
        { key: '0', display_name: '禁用' }
      ]
    }
  },
  methods: {
    formatterStatus(row) {
      return row.status === 1 ? '正常' : '禁用'
    },
    getDeptDatas(tree, treeNode, resolve) {
      const params = { pid: tree.id }
      setTimeout(() => {
        crudDept.getChildrenDept(params).then((res) => {
          resolve(res.data.list)
        })
      }, 100)
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      form.status = `${form.status}`
      if (form.id != null) {
        this.getSupDepts(form.id)
      } else {
        this.getDepts()
      }
    },
    getSupDepts(id) {
      crudDept.getAllDeptSuperior({ 'filterDeptId': id }).then((res) => {
        const data = res.data
        this.buildDepts(data)
        this.depts = data
      })
    },
    buildDepts(depts) {
      depts.forEach((data) => {
        if (data.children) {
          this.buildDepts(data.children)
        }
        if (!data.hasChildren) {
          delete data.children
        }
      })
    },
    getDepts() {
      crudDept.getDepts().then((res) => {
        this.depts = res.data.map(function(obj) {
          if (obj.hasChildren) {
            obj.children = null
          }
          return obj
        })
      })
    },
    // 获取弹窗内部门数据
    loadDepts({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        crudDept.getChildrenDept({ pid: parentNode.id }).then((res) => {
          parentNode.children = res.data.list.map(function(obj) {
            if (!obj.hasChildren) {
              delete obj.children
            }
            return obj
          })
          setTimeout(() => {
            callback()
          }, 100)
        })
      }
    },
    // 提交前的验证
    [CRUD.HOOK.afterValidateCU]() {
      if (this.form.pid === undefined || this.form.pid === null || this.form.pid === '') {
        this.$message({
          message: '上级部门不能为空',
          type: 'warning'
        })
        return false
      }
      if (this.form.pid !== '0' && this.form.pid === this.form.id) {
        this.$message({
          message: '上级部门不能为空',
          type: 'warning'
        })
        return false
      }
      return true
    },
    checkboxT(row, rowIndex) {
      return row.id !== 1
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
