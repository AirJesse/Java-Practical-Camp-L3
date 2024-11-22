<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <crudOperation :permission="permission" />
    </div>
    <!--表单渲染-->
    <el-dialog
      append-to-body
      :close-on-click-modal="false"
      :before-close="crud.cancelCU"
      :visible.sync="crud.status.cu > 0"
      :title="crud.status.title"
      width="580px"
    >
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type" size="mini" style="width: 178px">
            <el-radio-button label="1">目录</el-radio-button>
            <el-radio-button label="2">菜单</el-radio-button>
            <el-radio-button label="3">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="form.type.toString() !== '3'" label="菜单图标" prop="icon">
          <el-popover placement="bottom-start" width="450" trigger="click" @show="$refs['iconSelect'].reset()">
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input slot="reference" v-model="form.icon" style="width: 450px;" placeholder="点击选择图标" readonly>
              <svg-icon
                v-if="form.icon"
                slot="prefix"
                :icon-class="form.icon"
                class="el-input__icon"
                style="height: 32px;width: 16px;"
              />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item v-if="form.type.toString() !== '3'" label="菜单标题" prop="name">
          <el-input
            v-model="form.name"
            :style="form.type.toString() === '1' ? 'width: 450px' : 'width: 178px'"
            placeholder="菜单标题"
          />
        </el-form-item>
        <el-form-item v-if="form.type.toString() === '3'" label="按钮名称" prop="name">
          <el-input v-model="form.name" placeholder="按钮名称" style="width: 178px;" />
        </el-form-item>
        <el-form-item v-show="form.type.toString() !== '1'" label="权限标识" prop="perms">
          <el-input
            v-model="form.perms"
            placeholder="权限标识"
            style="width: 178px;"
          />
        </el-form-item>
        <el-form-item v-if="form.type.toString() !== '3'" label="路由地址" prop="path">
          <el-input v-model="form.path" placeholder="路由地址" style="width: 178px;" />
        </el-form-item>
        <el-form-item label="菜单排序" prop="orderNum">
          <el-input-number
            v-model.number="form.orderNum"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 178px;"
          />
        </el-form-item>
        <el-form-item
          v-show="form.type.toString() === '2'"
          label="组件路径"
          prop="component"
        >
          <el-input v-model="form.component" style="width: 178px;" placeholder="组件路径" />
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <treeselect
            v-model="form.pid"
            :options="menus"
            :load-options="loadMenus"
            style="width: 450px;"
            placeholder="选择上级类目"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table
      ref="table"
      v-loading="crud.loading"
      lazy
      :load="getMenus"
      :data="crud.data"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="id"
      @select="crud.selectChange"
      @select-all="crud.selectAllChange"
      @selection-change="crud.selectionChangeHandler"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column :show-overflow-tooltip="true" label="菜单标题" width="125px" prop="title" />
      <el-table-column prop="icon" label="图标" align="center" width="60px">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" align="center" label="排序">
        <template slot-scope="scope">
          {{ scope.row.orderNum }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="perms" label="权限标识" />
      <el-table-column :show-overflow-tooltip="true" prop="component" label="组件路径" />
      <el-table-column prop="createTime" label="创建日期" width="135px" />
      <el-table-column
        v-if="checkPer(['sys:permission:update','sys:permission:deleted'])"
        label="操作"
        width="130px"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <udOperation :data="scope.row" :permission="permission" msg="确定删除吗,如果存在下级节点则需要先删除下一节点！" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import crudMenu from '@/api/system/menu'
import IconSelect from '@/components/IconSelect'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'

// crud交由presenter持有
const defaultForm = { id: null, title: null, menuSort: 999, path: null, component: null, componentName: null, iFrame: false, roles: [], pid: 0, icon: null, cache: false, hidden: false, type: 0, permission: null }
export default {
  name: 'Menu',
  components: { Treeselect, IconSelect, crudOperation, udOperation },
  curd: null,
  cruds() {
    return CRUD(
      {
        title: '菜单',
        url: 'sys/permissionsForVue',
        crudMethod: { ...crudMenu },
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
      menus: [],
      permission: {
        add: ['sys:permission:add'],
        edit: ['sys:permission:update'],
        del: ['sys:permission:deleted']
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.menus = []
      if (form.id != null) {
        if (form.pid === null) {
          form.pid = 0
        }
        this.getSupMenus(form.id)
      } else {
        this.menus.push({ id: 0, label: '默认顶级菜单', children: null })
      }
    },
    [CRUD.HOOK.beforeSubmit](crud, form) {
      const data = crud.form
      if (data.type !== 1 && (data.url === undefined || data.url === null || data.url === '')) {
        data['method'] = 'post'
        data['url'] = 'VUE无需用到，如需使用请自行配置'
      }
    },
    getMenus(tree, treeNode, resolve) {
      const params = { pid: tree.id }
      setTimeout(() => {
        crudMenu.getChildrenMenus(params).then(res => {
          resolve(res.data.list)
        })
      }, 100)
    },
    getSupMenus(id) {
      crudMenu.getAllMenuSuperior({ 'filterMenuId': id }).then((res) => {
        const data = res.data
        this.buildMenus(data)
        this.menus = data
      })
    },
    buildMenus(menus) {
      menus.forEach((data) => {
        if (data.children) {
          this.buildMenus(data.children)
        }
        if (!data.hasChildren) {
          delete data.children
        }
      })
    },
    loadMenus({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        crudMenu.getChildrenMenus({ pid: parentNode.id, notShowButton: true }).then(res => {
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
    // 选中图标
    selected(name) {
      this.form.icon = name
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}

::v-deep .vue-treeselect__control,
::v-deep .vue-treeselect__placeholder,
::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
</style>
