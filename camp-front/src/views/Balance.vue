<template>
  <el-container class="balance-container">
    <el-header class="balance-header">
      <h2>查询余额</h2>
    </el-header>
    <el-main class="balance-main">
      <div v-if="!accessToken">
        <el-button type="primary" @click="authorize">登入银行系统以获取授权</el-button>
      </div>
      <div v-else>
        <p>用户名: {{ username }}</p>
        <p>余额: {{ balance }}</p>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'Balance',
  data() {
    return {
      accessToken: null,
      username: '',
      balance: 0
    };
  },
  created() {
    this.checkAccessToken();
    if (this.accessToken) {
      this.fetchBalance();
    }
  },
  methods: {
    authorize() {
      const clientId = import.meta.env.VITE_APP_CLIENT_ID;
      const redirectUri = import.meta.env.VITE_APP_REDIRECT_URI;
      const authHost = import.meta.env.VITE_APP_BANK_API
      const clientSecret = import.meta.env.VITE_APP_CLIENT_SECRET;
      const authUrl = `${authHost}/outh/authorize?response_type=code&client_id=${clientId}&client_secret=${clientSecret}&redirect_uri=${redirectUri}`;
      const authWindow = window.open(authUrl, '_blank', 'width=500,height=600');
      // window.location.href = authUrl;

      // const checkAuthWindow = setInterval(() => {
      //   if (authWindow.closed) {
      //     clearInterval(checkAuthWindow);
      //     this.checkAccessToken();
      //   }
      // }, 1000);
      window.addEventListener('message', this.receiveMessage, false);
    },
    receiveMessage(event) {
      console.log('Received message:', event);
      // if (event.origin !== window.location.origin) {
      //   return;
      // }
      const { code } = event.data;
      if (code) {
        this.fetchAccessToken(code);
      }
    },
    async fetchAccessToken(code) {
      try {
        const host = import.meta.env.VITE_APP_BANK_API;
        const access_token_url = `${host}/outh/accessTokenByCode?code=${code}`;
        const response = await fetch(access_token_url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
        });
        const data = await response.json();
        this.accessToken = data.access_token;
        localStorage.setItem('accessToken', this.accessToken);
        window.history.replaceState({}, document.title, window.location.pathname);
        this.fetchBalance();
      } catch (error) {
        console.error('Error fetching access token:', error);
      }
    },


    async checkAccessToken() {
      // const urlParams = new URLSearchParams(window.location.search);
      // const code = urlParams.get('code');
      // if (code) {
      //   try {
      //     const host = import.meta.env.VITE_APP_BANK_API;
      //     const response = await fetch(host+'/outh/accessTokenByCode', {
      //       method: 'GET',
      //       headers: {
      //         'Content-Type': 'application/x-www-form-urlencoded'
      //       },
      //       body: new URLSearchParams({
      //         code: code,
      //       })
      //     });
      //     const data = await response.json();
      //     this.accessToken = data.access_token;
      //     localStorage.setItem('accessToken', this.accessToken);
      //     window.history.replaceState({}, document.title, window.location.pathname);
      //   } catch (error) {
      //     console.error('Error fetching access token:', error);
      //   }
      // } else {
      //   this.accessToken = localStorage.getItem('accessToken');
      // }

      this.accessToken = localStorage.getItem('accessToken');
    },
    async fetchBalance() {
      try {
        const host = import.meta.env.VITE_APP_BANK_API;
        const response = await fetch(`${host}/bank/myMoney`, {
          headers: {
            'access_token': `${this.accessToken}`
          }
        });
        const data = await response.json();
        console.log('Balance data:', data);
        this.username = data.data.userName;
        this.balance = data.data.money;
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
</style>