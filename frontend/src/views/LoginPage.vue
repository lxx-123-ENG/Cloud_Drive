<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <div slot="header" class="login-header">
        <h2>
          <i class="fa fa-user-circle-o"></i> 账号登录
        </h2>
      </div>

      <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          label-width="80px"
          class="login-form"

      >
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="el-icon-user"
              name="username"
              autocomplete="off"
              style="width: 80%; margin: 0 auto;"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              name="password"
              autocomplete="off"
              style="width: 80%; margin: 0 auto;"
              show-password
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="isLoading"
              style="width: 80%; margin: 0 auto;"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-links">
        <el-button
            type="text"
            @click="$router.push('/login/email')"
            class="link-btn"
        >
          <i class="fa fa-envelope-o"></i> 邮箱登录
        </el-button>
        <span class="divider">|</span>
        <el-button
            type="text"
            @click="$router.push('/register')"
            class="link-btn"
        >
          <i class="fa fa-user-plus"></i> 注册账号
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'LoginPage',
  data() {
    return {
      isLoading: false,
      loginForm: {
        username: '',
        password: ''
      },
      // 表单验证规则
      loginRules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    async handleLogin() {
      // 表单验证
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            // 调用登录接口
            const result = await login(this.loginForm)
            //  存储Token 和用户信息到 localStorage（持久化，关闭浏览器不丢失）
            // 存储 Token（后端返回的 result.token）
            localStorage.setItem('saToken', result.token)

            // 存储用户信息
            localStorage.setItem('currentUser', JSON.stringify({
              userId: result.userId,    // 用户ID（后续可能用到）
              username: result.username,// 用户名
              nickname: result.nickname,// 昵称
              avatar: result.avatar // 头像
            }))

            // 显示成功消息并跳转
            this.$message.success('登录成功')
            this.$router.push('/dashboard')
          } catch (error) {
            console.error('登录失败:', error)
            // 错误信息由request拦截器统一处理
          } finally {
            this.isLoading = false
          }
        } else {
          this.$message.warning('请完善登录信息')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 450px;
  border-radius: 8px;
}

.login-header {
  text-align: center;
  padding: 15px 0;

  h2 {
    margin: 0;
    color: #333;
    font-weight: 500;

    i {
      margin-right: 10px;
      color: #42b983;
    }
  }
}

.login-form {
  padding-top: 20px;
}

.login-btn {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
}

.login-links {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  color: #666;
}

.link-btn {
  padding: 0 10px;
  color: #42b983 !important;

  &:hover {
    color: #359e75 !important;
  }
}

.divider {
  margin: 0 10px;
  color: #ddd;
}
</style>
