<template>
  <el-container class="balance-container">
    <el-header class="balance-header">
      <h2>查询余额</h2>
    </el-header>
    <el-divider />
    <el-main class="balance-main">
      <div v-if="needAuth">
        <el-button class="authButton" type="primary" @click="authorize">登入银行系统以获取授权</el-button>
        <el-alert class="infoAlert" title="偏差提示" description="这里采用了监听新窗口回传事件的方式来获取Code,而非使用redirect_uri跳转。我觉得这更符合主流做法。" show-icon effect="light" type="info" :closable="false" />
      </div>
      <div v-else>
        <p>用户名: {{ username }}</p>
        <p>余额: {{ balance }}</p>
        <el-alert class="infoAlert" title="操作提示" description="采用会话Cookie来保存各类凭证，注销会清空本域Cookie。如遇各种问题，建议重启浏览器。" show-icon effect="light" type="info" :closable="false" />
      </div>

    </el-main>
  </el-container>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: 'Balance',
  data() {
    return {
      needAuth: true,
      username: '',
      balance: 0
    };
  },
  created() {
    this.fetchBalance(null);
  },
  methods: {
    authorize() {
      const clientId = import.meta.env.VITE_APP_CLIENT_ID;
      const authHost = import.meta.env.VITE_APP_BANK_API
      const clientSecret = import.meta.env.VITE_APP_CLIENT_SECRET;
      /**
       * 采用监听窗口事件的方式获取code,已经不需要redirect_uri了。 保留代码作为参考。
       */
      const redirectUri = import.meta.env.VITE_APP_REDIRECT_URI;
      const authUrl = `${authHost}/outh/authorize?response_type=code&client_id=${clientId}&client_secret=${clientSecret}&redirect_uri=${redirectUri}`;
      const authWindow = window.open(authUrl, '_blank', 'width=500,height=600');
      window.addEventListener('message', this.receiveMessage, false);
    },

    receiveMessage(event) {
      console.log('Received message:', event);
      const { code } = event.data;
      if (code) {
        this.fetchBalance(code);
      }
    },
    async fetchBalance(code) {
      try {
        const host = import.meta.env.VITE_APP_BASE_API;
        const params = new URLSearchParams();
        if(code){
          params.append('code', code);
        } 
        const response = await fetch(`${host}/third/myMoney?${params.toString()}`, {
          method: 'GET',
          headers: {
          'Content-Type': 'application/json'
          },
          credentials: 'include' // 确保 cookies 被发送
        });
        const data = await response.json();
        console.log('Balance data:', data);
        if(data.code == 200){
          this.needAuth = false;
          this.username = data.data.username;
          this.balance = data.data.balance;
        }
      } catch (error) {
        console.error('Error fetching balance:', error);
      }
    }
  }
};
</script>

<style scoped>
.balance-container {
  height: 100%;
}

.authButton { 
  height: 40px;
}
.infoAlert {
  margin-top: 30px;
  width: 60%;
}

</style>