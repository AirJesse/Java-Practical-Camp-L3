<template>
  <el-container class="home-container">
    <el-header class="home-header">
      <el-row>
        <el-col :span="12">
          <h1>系统介绍</h1>
        </el-col>
        
        <el-col :span="12" class="text-right">
          <span>当前用户：{{ username }}</span>
          <el-button type="primary" @click="logout">注销</el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="200px" class="home-aside">
        <el-menu :default-active="activeMenu" class="el-menu-vertical-demo">
          <el-menu-item index="home">
            <router-link to="/home">首页</router-link>
          </el-menu-item>
          <el-menu-item index="balance">
            <router-link to="/home/balance">查询余额</router-link>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="home-main">
        <div v-if="showWelcomeMessage" class="welcome-message">
          <h2>欢迎来到系统首页</h2>
          <p>在这里，您可以浏览系统的各项功能。</p>
        </div>
        <router-view></router-view>
      </el-main>
    </el-container>
    <el-footer class="home-footer">
      Designed By Lujiachao
    </el-footer>
  </el-container>
</template>

<script>
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import Cookies from 'js-cookie';

export default {
  name: 'Home',
  setup() {
    const username = ref(Cookies.get('username')); // 这里可以从实际数据中获取用户名
    const router = useRouter();
    const route = useRoute();
    const activeMenu = ref('home');

    const showWelcomeMessage = computed(() => {
      return route.path === '/home';
    });

    const logout = () => {
      Cookies.remove('satoken'); // 移除登录的Cookie
      Cookies.remove('username'); // 移除用户名
      router.push('/login'); // 跳转到登录页
    };

    return {
      username,
      activeMenu,
      showWelcomeMessage,
      logout
    };
  }
};
</script>

<style scoped>
.home-container {
  height: 100vh;
}

.home-header {
  background-color: #409EFF;
  color: white;
  padding: 0 20px;
}

.home-header h1 {
  margin: 0;
  line-height: 60px;
}

.text-right {
  text-align: right;
  line-height: 60px;
}

.home-aside {
  background-color: #f2f2f2;
}

.home-main {
  padding: 20px;
  background-color: #ffffff;
  flex: 1;
}

.home-footer {
  text-align: center;
  padding: 10px 0;
  background-color: #f2f2f2;
  color: #666;
}

.welcome-message {
  text-align: center;
  margin-top: 50px;
}

.welcome-message h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

.welcome-message p {
  font-size: 16px;
  color: #666;
}
</style>