<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.roleName" size="small" clearable placeholder="输入名称" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission" />
    </div>
    <!-- 表单渲染 -->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="520px">
      <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" style="width: 380px;" />
        </el-form-item>
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="form.description" style="width: 380px;" rows="5" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <el-row :gutter="15">
      <!--角色管理-->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="17" style="margin-bottom: 10px">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span class="role-span">角色列表</span>
          </div>
          <el-table ref="table" v-loading="crud.loading" highlight-current-row style="width: 100%;" :data="crud.data" @selection-change="crud.selectionChangeHandler" @current-change="handleCurrentChange">
            <el-table-column :selectable="checkboxT" type="selection" width="55" />
            <el-table-column prop="name" label="名称" />
            <el-table-column :show-overflow-tooltip="true" prop="description" label="描述" />
            <el-table-column :show-overflow-tooltip="true" width="135px" prop="createTime" label="创建日期" />
            <el-table-column v-if="checkPer(['sys:dept:update','sys:dept:deleted'])" label="操作" width="130px" align="center" fixed="right">
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
        </el-card>
      </el-col>
      <!-- 菜单授权 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="7">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <el-tooltip class="item" effect="dark" content="选择指定角色分配菜单" placement="top">
              <span class="role-span">菜单分配</span>
            </el-tooltip>
            <el-button
              v-permission="['sys:role:update']"
              :disabled="!showButton"
              :loading="menuLoading"
              icon="el-icon-check"
              size="mini"
              style="float: right; padding: 6px 9px"
              type="primary"
              @click="saveMenu"
            >保存</el-button>
          </div>
          <el-tree
            ref="menu"
            lazy
            :data="menus"
            :default-checked-keys="menuIds"
            :load="getMenuDatas"
            :props="defaultProps"
            check-strictly
            accordion
            show-checkbox
            node-key="id"
            @check="menuChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudRoles from '@/api/system/role'
import { getChild, getChildrenMenus } from '@/api/system/menu'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

const defaultForm = { id: null, name: null, depts: [], description: null }
export default {
  name: 'Role',
  components: { pagination, crudOperation, rrOperation, udOperation },
  cruds() {
    return CRUD({ title: '角色', url: 'sys/roles', crudMethod: { ...crudRoles },
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
      defaultProps: { children: 'children', label: 'label', isLeaf: 'leaf' },
      dateScopes: ['全部', '本级', '自定义'], level: 3,
      currentId: 0, menuLoading: false, showButton: false,
      menus: [], menuIds: [], depts: [], deptDatas: [], // 多选时使用
      permission: {
        add: ['sys:role:add'],
        edit: ['sys:role:update'],
        del: ['sys:role:deleted']
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        permission: [
          { required: true, message: '请输入权限', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    getMenuDatas(node, resolve) {
      setTimeout(() => {
        getChildrenMenus({ pid: node.data.id ? node.data.id : 0 }).then(res => {
          const list = res.data.list
          list.forEach((menu) => {
            if (!menu.hasChildren) {
              menu['leaf'] = true
            }
          })
          resolve(res.data.list)
        })
      }, 100)
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
    [CRUD.HOOK.afterRefresh]() {
      this.$refs.menu.setCheckedKeys([])
    },
    // 新增前初始化部门信息
    [CRUD.HOOK.beforeToAdd](crud, form) {
      this.deptDatas = []
      form.menus = null
    },
    // 编辑前初始化自定义数据权限的部门信息
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.deptDatas = []
      if (form.dataScope === '自定义') {
        this.getSupDepts(form.depts)
      }
      const _this = this
      form.depts.forEach(function(dept) {
        _this.deptDatas.push(dept.id)
      })
      // 将角色的菜单清空，避免日志入库数据过长
      form.menus = null
    },
    // 触发单选
    handleCurrentChange(val) {
      if (val) {
        const _this = this
        // 清空菜单的选中
        this.$refs.menu.setCheckedKeys([])
        // 保存当前的角色id
        this.currentId = val.id
        // 初始化默认选中的key
        this.menuIds = []
        val.permIds.forEach(function(data) {
          _this.menuIds.push(data)
        })
        this.showButton = true
      }
    },
    menuChange(menu) {
      // 获取该节点的所有子节点，id 包含自身
      getChild(menu.id).then(res => {
        // 判断是否在 menuIds 中，如果存在则删除，否则添加
        if (res.code === 0) {
          const childIds = res.data
          if (this.menuIds.indexOf(menu.id) !== -1) {
            for (let i = 0; i < childIds.length; i++) {
              const index = this.menuIds.indexOf(childIds[i])
              if (index !== -1) {
                this.menuIds.splice(index, 1)
              }
            }
          } else {
            for (let i = 0; i < childIds.length; i++) {
              const index = this.menuIds.indexOf(childIds[i])
              if (index === -1) {
                this.menuIds.push(childIds[i])
              }
            }
            this.$refs.menu.setCheckedKeys(this.menuIds)
          }
        }
      })
    },
    // 保存菜单
    saveMenu() {
      this.menuLoading = true
      const role = { id: this.currentId, menus: [] }
      // 得到已选中的 key 值
      this.menuIds.forEach(function(id) {
        const menu = id
        role.menus.push(menu)
      })
      crudRoles.editMenu({ roleId: role.id, permissionIds: role.menus }).then((res) => {
        if (res.code === 0) {
          this.crud.notify('保存成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
          this.menuLoading = false
          this.update()
        }
      }).catch(err => {
        this.menuLoading = false
        console.log(err.response.data.message)
      })
    },
    // 改变数据
    update() {
      // 无刷新更新 表格数据
      crudRoles.get(this.currentId).then(res => {
        for (let i = 0; i < this.crud.data.length; i++) {
          if (res.data.id === this.crud.data[i].id) {
            this.crud.data[i] = res.data
            break
          }
        }
      })
    },
    checkboxT(row) {
      return row.level >= this.level
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .role-span {
    font-weight: bold;color: #303133;
    font-size: 15px;
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .el-input-number .el-input__inner {
    text-align: left;
  }
 ::v-deep .vue-treeselect__multi-value{
    margin-bottom: 0;
  }
 ::v-deep .vue-treeselect__multi-value-item{
    border: 0;
    padding: 0;
  }
</style>
