<template>
  <div class="register-container">
    <!-- 注册卡片 -->
    <el-card class="register-card" shadow="hover">
      <!-- 卡片头部：标题 -->
      <div slot="header" class="register-header">
        <h2><i class="el-icon-user-plus"></i> 账号注册</h2>
      </div>

      <!-- 注册表单 -->
      <el-form
          ref="registerForm"
          :model="registerForm"
          :rules="registerRules"
          label-width="100px"
          class="register-form"
          size="medium"
      >
        <!-- 用户名输入框 -->
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="请输入3-20位用户名"
              prefix-icon="el-icon-user"
              autocomplete="off"
          ></el-input>
        </el-form-item>

        <!-- 密码输入框 -->
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入6-20位密码（含字母+数字）"
              prefix-icon="el-icon-lock"
              show-password
              autocomplete="new-password"
          ></el-input>
        </el-form-item>

        <!-- 确认密码输入框 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              prefix-icon="el-icon-lock"
              show-password
              autocomplete="new-password"
          ></el-input>
        </el-form-item>

        <!-- 邮箱输入框 -->
        <el-form-item label="邮箱" prop="email">
          <el-input
              v-model="registerForm.email"
              placeholder="请输入用于接收验证码的邮箱"
              prefix-icon="el-icon-message"
              autocomplete="off"
          ></el-input>
        </el-form-item>

        <!-- 验证码输入框 + 发送按钮 -->
        <el-form-item label="验证码" prop="code">
          <el-row :gutter="8">
            <el-col :span="16">
              <el-input
                  v-model="registerForm.code"
                  placeholder="请输入邮箱收到的验证码"
                  prefix-icon="el-icon-mobile-phone"
                  autocomplete="off"
              ></el-input>
            </el-col>
            <el-col :span="8">
              <el-button
                  :class="['send-code-btn', { 'disabled-btn': isSendingCode || countDown > 0 }]"
                  :disabled="isSendingCode || countDown > 0"
                  @click="sendEmail"
              >
                {{ countDown > 0 ? `${countDown}秒后重发` : '发送验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <!-- 注册按钮 -->
        <el-form-item class="register-btn-item">
          <el-button
              type="primary"
              size="medium"
              :loading="isSubmitting"
              @click="handleRegister"
              class="register-btn"
          >
            注册账号
          </el-button>
        </el-form-item>

        <!-- 跳转登录页链接 -->
        <div class="login-link">
          已有账号？
          <el-button
              type="text"
              @click="$router.push('/login')"
              class="link-btn"
          >
            立即登录
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
// 1. 导入接口
import { userRegister } from '@/api/user'
import { sendEmail } from '@/api/verification'
// 2. 导入Element UI组件（提示用）
import { Message } from 'element-ui'

export default {
  name: 'RegisterPage',
  data() {
    // 自定义表单校验规则：密码一致性校验
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致，请重新输入！'))
      } else {
        callback()
      }
    }

    return {
      // 注册表单数据（与输入框v-model绑定）
      registerForm: {
        username: '',    // 用户名
        password: '',    // 密码
        confirmPassword: '', // 确认密码
        email: '',       // 邮箱
        code: ''         // 验证码
      },
      // 表单校验规则（Element UI自带校验）
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度需在3-20位之间', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_-]{3,20}$/, message: '用户名仅支持字母、数字、下划线和短横线', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度需在6-20位之间', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' } // 自定义校验：密码一致性
        ],
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
      isSubmitting: false   // 注册按钮加载状态
    }
  },
  methods: {
    /**
     * 1. 发送邮箱验证码
     */
    async sendEmail() {
      // 先校验邮箱格式（避免无效请求）
      const emailValid = await new Promise((resolve) => {
        this.$refs.registerForm.validateField('email', (error) => {
          resolve(!error) // 无错误则返回true
        })
      })

      if (!emailValid) {
        return // 邮箱格式错误，不发送验证码
      }

      try {
        this.isSendingCode = true // 按钮加载中
        // 调用后端发送验证码接口（传递邮箱参数）
        await sendEmail(this.registerForm.email)
        Message.success('验证码已发送至您的邮箱，请查收！')

        // 启动倒计时（60秒）
        this.countDown = 60
        const timer = setInterval(() => {
          this.countDown--
          if (this.countDown <= 0) {
            clearInterval(timer) // 倒计时结束，清除定时器
          }
        }, 1000)
      } catch (error) {
        // 后端返回错误（如邮箱已被注册）
        Message.error(error.message || '验证码发送失败，请重试！')
      } finally {
        this.isSendingCode = false // 取消按钮加载
      }
    },

    /**
     * 2. 提交注册表单
     */
    async handleRegister() {
      // 先做表单整体校验（所有字段需通过校验）
      this.$refs.registerForm.validate(async (isValid) => {
        if (isValid) {
          this.isSubmitting = true // 注册按钮加载中
          try {
            // 组装注册参数（过滤confirmPassword，后端不需要）
            const registerData = {
              username: this.registerForm.username,
              password: this.registerForm.password,
              email: this.registerForm.email,
              code: this.registerForm.code
            }

            // 调用后端注册接口
            await userRegister(registerData)

            // 注册成功：提示+跳转登录页
            Message.success('注册成功！即将跳转到登录页')
            setTimeout(() => {
              this.$router.push('/login') // 1秒后跳转
            }, 1000)
          } catch (error) {
            // 注册失败（如用户名已存在、验证码无效）
            Message.error(error.message || '注册失败，请重试！')
          } finally {
            this.isSubmitting = false // 取消按钮加载
          }
        } else {
          // 表单校验失败（如字段为空、格式错误）
          Message.warning('请完善注册信息并修正错误！')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
/* 页面容器：居中布局 */
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
  box-sizing: border-box;
}

/* 注册卡片：限制宽度+圆角 */
.register-card {
  width: 100%;
  max-width: 500px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  border: none;
}

/* 卡片头部：标题样式 */
.register-header {
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

/* 表单容器：内边距和间距 */
.register-form {
  padding: 30px 40px;

  /* 统一表单项间距 */
  .el-form-item {
    margin-bottom: 20px;
  }

  /* 输入框样式优化 */
  .el-input {
    height: 40px;
    width: 100%;

    /* 修复图标显示问题 */
    .el-input__prefix {
      left: 12px;
      top: 50%;
      transform: translateY(-50%);
      i {
        font-size: 16px;
        color: #999;
      }
    }

    /* 输入框内部样式 */
    .el-input__inner {
      height: 100%;
      line-height: 40px;
      padding-left: 38px; /* 为图标留出空间 */
      padding-right: 15px;
      border-radius: 6px;
      border-color: #dcdfe6;
      transition: border-color 0.3s, box-shadow 0.3s;

      &:focus {
        border-color: #42b983;
        box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
      }
    }
  }

  /* 验证码行样式 */
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

/* 发送验证码按钮样式优化 */
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

/* 禁用状态的验证码按钮 */
.disabled-btn {
  background-color: #f5f7fa;
  color: #c0c4cc;
  cursor: not-allowed;
}

.disabled-btn:hover {
  background-color: #f5f7fa;
  color: #c0c4cc;
}

/* 注册按钮样式 */
.register-btn-item {
  margin-top: 10px !important;
  margin-bottom: 10px !important;
}

.register-btn {
  width: 100%;
  height: 44px;
  line-height: 44px;
  padding: 0;
  font-size: 16px;
  border-radius: 6px;
  background-color: #42b983;
  border-color: #42b983;

  &:hover {
    background-color: #359e75;
    border-color: #359e75;
  }
}

/* 登录链接：居中+颜色 */
.login-link {
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
