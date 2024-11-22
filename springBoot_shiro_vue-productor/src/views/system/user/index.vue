<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--侧边部门数据-->
      <el-col :xs="9" :sm="6" :md="5" :lg="4" :xl="4">
        <div class="head-container">
          <el-input
            v-model="deptName"
            clearable
            size="small"
            placeholder="输入部门名称搜索"
            prefix-icon="el-icon-search"
            class="filter-item"
            @input="getDeptDatas"
          />
        </div>
        <el-tree
          :data="deptDatas"
          :load="getDeptDatas"
          :props="defaultProps"
          :expand-on-click-node="false"
          lazy
          @node-click="handleNodeClick"
        />
      </el-col>
      <!--用户数据-->
      <el-col :xs="15" :sm="18" :md="19" :lg="20" :xl="20">
        <!--工具栏-->
        <div class="head-container">
          <div v-if="crud.props.searchToggle">
            <!-- 搜索 -->
            <el-input
              v-model="query.username"
              clearable
              size="small"
              placeholder="输入用户名"
              style="width: 200px;"
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
          <crudOperation show="" :permission="permission" />
        </div>
        <!--表单渲染-->
        <el-dialog
          append-to-body
          :close-on-click-modal="false"
          :before-close="crud.cancelCU"
          :visible.sync="crud.status.cu > 0"
          :title="crud.status.title"
          width="570px"
        >
          <el-form ref="form" :inline="true" :model="form" :rules="rules" size="small" label-width="66px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" @keydown.native="keydown($event)" />
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input v-model.number="form.phone" />
            </el-form-item>
            <el-form-item label="真实名称" prop="realName">
              <el-input v-model="form.realName" @keydown.native="keydown($event)" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>

            <el-form-item label="性别">
              <el-radio-group v-model="form.sex" style="width: 178px">
                <el-radio key="1" label="1">男</el-radio>
                <el-radio key="2" label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="状态">
              <el-radio-group v-model="form.status" :disabled="form.id === user.id">
                <el-radio v-for="item in statusTypeOptions" :key="item.key" :label="item.key">{{ item.display_name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="部门" prop="deptId">
              <treeselect
                v-model="form.deptId"
                :options="depts"
                :load-options="loadDepts"
                style="width: 437px"
                placeholder="选择部门"
              />
            </el-form-item>
            <el-form-item style="margin-bottom: 0;" label="角色" prop="roleIds">
              <el-select
                v-model="roleDatas"
                style="width: 437px"
                multiple
                placeholder="请选择"
                @remove-tag="deleteTag"
                @change="changeRole"
              >
                <el-option
                  v-for="item in roles"
                  :key="item.name"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
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
          :data="crud.data"
          style="width: 100%;"
          @selection-change="crud.selectionChangeHandler"
        >
          <el-table-column :selectable="checkboxT" type="selection" width="55" />
          <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" />
          <el-table-column :show-overflow-tooltip="true" prop="realName" label="真实名称" />
          <el-table-column prop="sex" label="性别" :formatter="formatterSex" />
          <el-table-column :show-overflow-tooltip="true" prop="phone" width="100" label="电话" />
          <el-table-column :show-overflow-tooltip="true" width="135" prop="email" label="邮箱" />
          <el-table-column :show-overflow-tooltip="true" prop="deptName" label="部门" />
          <el-table-column label="状态" align="center" prop="status" :formatter="formatterStatus" />
          <el-table-column :show-overflow-tooltip="true" prop="createTime" width="135" label="创建日期" />
          <el-table-column
            v-if="checkPer(['sys:user:update', 'sys:user:deleted'])"
            label="操作"
            width="115"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <udOperation :data="scope.row" :permission="permission" :disabled-dle="scope.row.id === user.id" />
            </template>
          </el-table-column>
        </el-table>
        <!--分页组件-->
        <pagination />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import crudUser from '@/api/system/user'
import { getDepts, getAllDeptSuperior, getChildrenDept } from '@/api/system/dept'
import { getAll } from '@/api/system/role'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import Treeselect from '@riophae/vue-treeselect'
import { mapGetters } from 'vuex'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { LOAD_CHILDREN_OPTIONS } from '@riophae/vue-treeselect'
let userRoles = []
const defaultForm = { id: null, username: null, realName: null, sex: '1', email: null, status: '1', roles: [], dept: { id: null }, phone: null }
export default {
  name: 'User',
  components: { Treeselect, crudOperation, rrOperation, udOperation, pagination },
  cruds() {
    return CRUD({ title: '用户', url: 'sys/users', crudMethod: { ...crudUser }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],

  data() {
    return {
      height: document.documentElement.clientHeight - 180 + 'px;',
      deptName: '', depts: [], deptDatas: [], roles: [],
      roleDatas: [], // 多选时使用
      defaultProps: { children: 'children', label: 'name', isLeaf: 'leaf' },
      permission: {
        add: ['sys:user:add'],
        edit: ['sys:user:update'],
        del: ['sys:user:deleted']
      },
      statusTypeOptions: [
        { key: '1', display_name: '正常' },
        { key: '2', display_name: '禁用' }
      ],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {
    this.crud.msg.add = '新增成功，默认密码：123456'
  },
  mounted: function() {
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 180 + 'px;'
    }
  },
  methods: {
    formatterStatus(row) {
      return row.status === 1 ? '正常' : '禁用'
    },
    formatterSex(row) {
      return row.sex === 1 ? '男' : '女'
    },
    // 禁止输入空格
    keydown(e) {
      if (e.keyCode === 32) {
        e.returnValue = false
      }
    },
    changeRole(value) {
      userRoles = []
      value.forEach(function(data, index) {
        userRoles.push(data)
      })
    },

    deleteTag(value) {
      userRoles.forEach(function(data, index) {
        if (data === value) {
          userRoles.splice(index, value)
        }
      })
    },
    // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      this.getRoles()
      if (form.id == null) {
        this.getDepts()
      } else {
        this.getSupDepts(form.dept.id)
      }
      form.sex = form.sex.toString()
      form.status = form.status.toString()
    },
    // 新增前将多选的值设置为空
    [CRUD.HOOK.beforeToAdd]() {
      this.roleDatas = []
    },

    // 初始化编辑时候的角色与岗位
    [CRUD.HOOK.beforeToEdit](crud, form) {
      this.roleDatas = []
      userRoles = []
      const _this = this
      form.roles.forEach(function(role, index) {
        _this.roleDatas.push(role.id)
        userRoles.push(role.id)
      })
    },
    // 提交前做的操作
    [CRUD.HOOK.afterValidateCU](crud) {
      if (!crud.form.deptId || crud.form.deptId === '0') {
        this.$message({
          message: '部门不能为空',
          type: 'warning'
        })
        return false
      } else if (this.roleDatas.length === 0) {
        this.$message({
          message: '角色不能为空',
          type: 'warning'
        })
        return false
      }
      if (crud.form.id !== undefined && crud.form.id !== '' && crud.form.id !== null &&
          (crud.form.password !== undefined || crud.form.password !== null || crud.form.password !== '')) {
        delete crud.form.password
      }
      if ((crud.form.id === undefined || crud.form.id === '' || crud.form.id === null) &&
      (crud.form.password === undefined || crud.form.password === null || crud.form.password === '')) {
        crud.form.password = '123456'
      }
      crud.form.roleIds = this.roleDatas
      return true
    },
    // 获取左侧部门数据
    getDeptDatas(node, resolve) {
      const params = {}
      params['pid'] = node.data.id
      setTimeout(() => {
        getChildrenDept(params).then((res) => {
          if (res.code === 0) {
            const list = res.data.list
            if (list !== undefined && list.length > 0) {
              list.forEach(data => {
                if (!data.hasChildren) {
                  data['leaf'] = true
                }
              })
            }
            if (resolve) {
              resolve(res.data.list)
            } else {
              this.deptDatas = res.data.list
            }
          }
        })
      }, 100)
    },
    getDepts() {
      getDepts().then((res) => {
        this.depts = res.data.map(function(obj) {
          if (obj.hasChildren) {
            obj.children = null
          }
          return obj
        })
      })
    },

    getSupDepts(id) {
      getAllDeptSuperior({ 'filterDeptId': id }).then((res) => {
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
    // 获取弹窗内部门数据
    loadDepts({ action, parentNode, callback }) {
      if (action === LOAD_CHILDREN_OPTIONS) {
        getChildrenDept({ pid: parentNode.id }).then((res) => {
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
    // 切换部门
    handleNodeClick(data) {
      if (data.pid === null) {
        this.query.deptId = null
      } else {
        this.query.deptId = data.id
      }
      this.crud.toQuery()
    },
    // 获取弹窗内角色数据
    getRoles() {
      getAll().then(res => {
        this.roles = res.data
      }).catch(() => { })
    },
    checkboxT(row, rowIndex) {
      return row.id !== this.user.id
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
