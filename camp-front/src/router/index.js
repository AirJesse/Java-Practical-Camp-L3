import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Balance from '../views/Balance.vue'; // 导入新的页面
import Cookies from 'js-cookie';

const routes = [
  {
    path: '/',
    redirect: '/home' // 将默认页面改为home
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    children: [
      {
        path: 'balance',
        name: 'Balance',
        component: Balance // 添加新的路由
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// 添加全局前置守卫
router.beforeEach((to, from, next) => {
  const satoken = Cookies.get('satoken');
  if (to.name !== 'Login' && !satoken) {
    next({ name: 'Login' }); // 如果未登录且访问的不是登录页，则强制跳转到登录页
  } else {
    next(); // 否则允许访问
  }
});

export default router;
