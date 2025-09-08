<template>
  <div class="email-login-container">
    <el-card class="email-login-card" shadow="hover">
      <div slot="header" class="email-login-header">
        <h2><i class="el-icon-envelope"></i> 邮箱验证码登录</h2>
      </div>

      <el-form
          ref="emailLoginForm"
          :model="loginForm"
          :rules="loginRules"
          label-width="100px"
          class="email-login-form"
          size="medium"
      >
        <!-- 邮箱输入框 -->
        <el-form-item label="邮箱" prop="email">
          <el-input
              v-model="loginForm.email"
              placeholder="请输入您的注册邮箱"
              prefix-icon="el-icon-envelope"
              autocomplete="off"
              class="custom-input"
          ></el-input><!-- autocomplete 自动补全关闭 -->
        </el-form-item>

        <!-- 验证码输入框 + 发送按钮 -->
        <el-form-item label="验证码" prop="code">
          <el-row :gutter="8">
            <el-col :span="16">
              <el-input
                  v-model="loginForm.code"
                  placeholder="请输入6位验证码"
                  prefix-icon="el-icon-mobile-phone"
                  autocomplete="off"
                  class="custom-input"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-button
                  :class="['send-code-btn', { 'disabled-btn': isSendingCode || countDown > 0 }]"
                  :disabled="isSendingCode || countDown > 0"
                  @click="sendEmailCode"
              >
                {{ countDown > 0 ? `${countDown}秒后重发` : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item class="login-btn-item">
          <el-button
              type="primary"
              size="medium"
              :loading="isSubmitting"
              @click="handleEmailLogin"
              class="custom-login-btn"
          >
            登录账号
          </el-button>
        </el-form-item>

        <!-- 切换登录方式 -->
        <div class="switch-login-link">
          用密码登录？
          <el-button
              type="text"
              @click="$router.push('/login')"
              class="link-btn"
          >
            前往密码登录页
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
// 1. 导入封装好的接口（关键：与你的verification.js和user.js对应）
import { sendLoginEmail } from '@/api/verification' // 导入发送登录验证码接口
import { loginByEmail } from '@/api/user' // 导入邮箱登录接口
// 2. 导入Element UI提示组件
import { Message } from 'element-ui'

export default {
  name: 'EmailLoginPage',
  data() {
    return {
      // 登录表单数据（与接口参数格式对齐）
      loginForm: {
        email: '',  // 邮箱（传递给sendLoginEmail和loginByEmail）
        code: ''    // 验证码（传递给loginByEmail）
      },
      // 表单校验规则
      loginRules: {
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式（如xxx@xxx.com）', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 6, max: 6, message: '验证码为6位数字', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9]{6}$/, message: '验证码仅支持6位字母或数字', trigger: 'blur' }
        ]
      },
      isSendingCode: false, // 发送验证码按钮加载状态
      countDown: 0,         // 验证码倒计时（秒）
      isSubmitting: false   // 登录按钮加载状态
    }
  },
  methods: {
    /**
     * 1. 发送登录验证码（调用verification.js的sendLoginEmail接口）
     * 接口封装：接收单个email参数，用data传递
     */
    async sendEmailCode() {
      // 先校验邮箱格式
      const emailValid = await new Promise((resolve) => {
        this.$refs.emailLoginForm.validateField('email', (error) => {
          resolve(!error)
        })
      })

      if (!emailValid) {
        return
      }

      try {
        this.isSendingCode = true
        // 调用接口：传递单个email（与verification.js的data: email格式一致）
        await sendLoginEmail(this.loginForm.email)

        Message.success('验证码已发送至您的邮箱，请查收！')
        // 启动60秒倒计时
        this.countDown = 60
        const timer = setInterval(() => {
          this.countDown--
          if (this.countDown <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } catch (error) {
        // 捕获接口错误（如“邮箱未注册”“发送失败”）
        Message.error(error.response?.data?.message || '验证码发送失败，请重试！')
      } finally {
        this.isSendingCode = false
      }
    },

    /**
     * 2. 邮箱登录（调用user.js的loginByEmail接口）
     * 接口封装：接收{email, code}对象，用data传递
     */
    async handleEmailLogin() {
      this.$refs.emailLoginForm.validate(async (isValid) => {
        if (isValid) {
          this.isSubmitting = true
          try {
            // 调用接口：传递{email, code}对象（与user.js的data: params格式一致）
            const response = await loginByEmail(this.loginForm)

            // 登录成功：存储Token和用户信息（与后端返回格式对齐）
            const { token, userId, username, nickname, email } = response.data
            localStorage.setItem('saToken', token) // 存储Token（与Sa-Token配置一致）
            localStorage.setItem('currentUser', JSON.stringify({
              userId,
              username,
              nickname,
              email
            }))

            // 提示并跳转工作台
            Message.success('登录成功！正在跳转...')
            setTimeout(() => {
              this.$router.push('/dashboard')
            }, 1000)
          } catch (error) {
            // 捕获后端业务错误（如“验证码错误”“验证码过期”）
            const errorMsg = error.response?.data?.message || '登录失败，请重试！'
            Message.error(errorMsg)
          } finally {
            this.isSubmitting = false
          }
        } else {
          Message.warning('请完善信息并修正错误！')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
/* 保持原有样式，确保图标显示、布局对齐 */
.email-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
  box-sizing: border-box;
}

.email-login-card {
  width: 100%;
  max-width: 500px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: none;
  overflow: hidden;
}

.email-login-header {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;

  h2 {
    margin: 0;
    color: #333;
    font-weight: 600;
    font-size: 18px;
    display: flex;
    align-items: center;
    justify-content: center;

    i {
      margin-right: 8px;
      color: #42b983;
      font-size: 20px;
    }
  }
}

.email-login-form {
  padding: 30px 40px;

  .el-form-item {
    margin-bottom: 22px;
  }

  .custom-input {
    height: 40px;
    width: 100%;

    .el-input__prefix {
      left: 12px;
      top: 50%;
      transform: translateY(-50%);

      i {
        font-size: 16px;
        color: #999 !important;
        font-family: "element-icons" !important;
      }
    }

    .el-input__inner {
      height: 100%;
      line-height: 40px;
      padding-left: 38px;
      padding-right: 15px;
      border-radius: 6px;
      border-color: #dcdfe6;
      color: #333 !important;
      background-color: #fff !important;

      &:focus {
        border-color: #42b983;
        box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
        outline: none;
      }
    }
  }

  .el-row {
    display: flex;
    align-items: center;
    width: 100%;
  }

  .el-col {
    display: flex;
    align-items: center;
  }
}

.send-code-btn {
  width: 100%;
  height: 40px;
  line-height: 40px;
  padding: 0;
  border-radius: 6px;
  background-color: #42b983;
  color: #fff;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.send-code-btn:hover {
  background-color: #359e75;
  color: #fff;
}

.disabled-btn {
  background-color: #f5f7fa;
  color: #c0c4cc;
  cursor: not-allowed;
}

.disabled-btn:hover {
  background-color: #f5f7fa;
  color: #c0c4cc;
}

.login-btn-item {
  margin-bottom: 15px !important;
}

.custom-login-btn {
  width: 100%;
  height: 44px;
  line-height: 44px;
  padding: 0;
  font-size: 16px;
  border-radius: 6px;
  background-color: #42b983;
  border-color: #42b983;
}

.custom-login-btn:hover {
  background-color: #359e75;
  border-color: #359e75;
}

.switch-login-link {
  text-align: center;
  margin-top: 5px;
  color: #666;
  font-size: 14px;

  .link-btn {
    color: #42b983 !important;
    padding: 0 5px;
    font-size: 14px;
    height: auto;
    line-height: normal;
  }

  .link-btn:hover {
    color: #359e75 !important;
    text-decoration: underline;
  }
}
</style>