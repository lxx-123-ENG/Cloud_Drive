import axios from 'axios'
import { Message } from 'element-ui'

// 1. 创建 Axios 实例（基础配置不变）
const request = axios.create({
    baseURL: 'http://127.0.0.1:8085', // 后端基础地址（必须和后端一致）
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json' // 确保请求体是 JSON 格式（后端 @RequestBody 需要）
    }
})

// 统一处理 Token
request.interceptors.request.use(
    (config) => {
        // 关键：Token 存储的 Key 是 "saToken"（和登录成功后存储的 Key 一致！）
        const token = localStorage.getItem('saToken')
        if (token) {
            // 格式：Bearer + 空格 + Token（和后端 Sa-Token 配置的 token-prefix 一致）
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        // 请求发送前的错误（如参数错误），这里很少触发
        Message.error('请求参数错误，请检查！')
        return Promise.reject(error)
    }
)

// 3. 响应拦截器
request.interceptors.response.use(
    (response) => {
        const res = response.data
        // 后端统一返回格式：{ code: 200, message: "", data: {} }
        if (res.code === 200) {
            return res.data // 成功时只返回 data，简化前端调用
        } else {
            // 业务错误（如用户名不存在、密码错误），后端会返回具体 message
            Message.error(res.message || '操作失败')
            return Promise.reject(res.message)
        }
    },
    (error) => {
        // 网络错误/后端 500/404 等错误，先打印详细信息（方便排查）
        console.error('服务器请求错误详情：', error)

        // 分场景提示错误（比“服务器异常”更精准）
        if (error.message.includes('Network Error')) {
            // 网络错误（如后端没启动、地址写错）
            Message.error('网络异常，请检查后端是否启动！')
        } else if (error.response?.status === 404) {
            // 接口不存在（地址写错）
            Message.error(`接口不存在：${error.response.config.url}`)
        } else if (error.response?.status === 500) {
            // 后端代码报错（如空指针、SQL错误）
            Message.error('后端服务报错，请查看后端控制台！')
        } else if (error.response?.status === 401) {
            // Token 无效（登录过期）
            localStorage.removeItem('saToken')
            localStorage.removeItem('currentUser')
            Message.error('登录已过期，请重新登录！')
            window.location.href = '/login'
        } else {
            // 其他未知错误
            Message.error('服务器异常，请稍后重试！')
        }

        return Promise.reject(error)
    }
)

export default request