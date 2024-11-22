<template>
    <div class="login-page">
        <div class="loginBody">
            <el-form :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
                <h3 class="loginTitle">
                    L3Camp-前端
                    <P style="font-size:medium;">系统登入</P>
                </h3>

                <el-form-item prop="username">
                    <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名">
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" v-model="loginForm.password" placeholder="请输入密码">
                    </el-input>
                </el-form-item>
                <!-- <el-form-item prop="code">
                <el-input type="text" auto-complete="false" v-model="loginForm.code" placeholder="点击图片更换验证码"
                    style="width: 250px;margin-right: 5px">
                </el-input>
                <img :src="captchaUrl">

            </el-form-item> -->
                <!-- <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox> -->
                <el-button type="primary" style="width:100%" @click="submitLogin">登录</el-button>
            </el-form>
        </div>
    </div>
</template>

<script>
import { get, post } from "@/api/request";
import { ElMessage, ElMessageBox } from "element-plus";
import Cookies from "js-cookie";
export default {
    name: "Login",
    data() {
        return {
            captchaUrl: "",
            loginForm: {
                username: "admin",
                password: "admin"
            },
            checked: true,
            rules: {
                username: [{ required: true, message: "请输入用户名", trigger: "blur" }, { min: 5, max: 14, message: '长度在 5 到 14 个字符', trigger: 'blur' }
                ],
                password: [{ required: true, message: "请输入密码", trigger: "blur" }, , { min: 6, message: '密码长度要大于6', trigger: 'blur' }],
                code: [{ required: true, message: "请输入验证码", trigger: "blur" }],
            }

        }
    },

    methods: {
        submitLogin() {
            console.log(Cookies.get('satoken'));
            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    post("/user/doLogin", this.loginForm).then(res => {
                        const code = res.data.code;
                        console.log(res.data);
                        if (code !== 200) {
                            ElMessageBox.alert('请重新输入！', '用户名或密码错误');
                            return;
                        }
                        ElMessage({
                            message: '登入成功',
                            type: 'success',
                        });
                        Cookies.set('username', this.loginForm.username);
                        this.$router.push('/home');
                    })


                } else {
                    this.$message.error('请重新输入');
                    return false;
                }
            });
        }
    }
};
</script>

<style scoped lang="less">
.loginContainer {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 15px 35px 15px 35px;
    background: aliceblue;
    border: 1px solid blueviolet;
    box-shadow: 0 0 25px #f885ff;
}

.loginTitle {
    margin: 0px auto 48px auto;
    text-align: center;
    font-size: 40px;
}

.loginRemember {
    text-align: left;
    margin: 0px 0px 15px 0px;
}

// body {
//     background-image: url("../assets/background.jpg");
//     background-size: cover;
//     height: 100%;
//     width: 100%;
//     background-position: center;
//     // position: fixed;
//     // left: 2px;
// }
.login-page {
  height: 100%;
  width: 100%;
  background-image: url('../assets/background.jpg');
  background-size: cover;
    left: -0.5px;
  background-repeat: no-repeat;
  background-attachment: fixed;
  position: fixed;
}
</style>
