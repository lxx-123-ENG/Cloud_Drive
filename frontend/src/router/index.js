import Vue from 'vue'
import Router from 'vue-router'

// 导入页面组件（后面会创建这些组件）
import LoginPage from '@/views/LoginPage.vue'
import NotFound from "@/views/NotFound.vue";
import RegisterPage from "@/views/RegisterPage.vue";
import EmailLoginPage from "@/views/EmailLoginPage.vue";

Vue.use(Router)

const router = new Router({
  mode: 'history', // 去掉URL中的#号
  routes: [
    {
      path: '/',
      redirect: '/login' // 访问根路径时自动跳转到登录页
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage
    },
    {
      path: '/login/email', // 邮箱登录页路由
      name: 'EmailLoginPage',
      component: EmailLoginPage,
      meta: { noAuth: true }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage
    },
    {
      path: '/404',
      name: 'NotFound',
      component: NotFound
    },
    {
      path: '*', // 匹配所有未定义路由
      redirect: '/404' // 重定向到 404 页面
    }
  ]
})

/*router.beforeEach((to, from, next) => {
  // 如果目标页面需要登录，且本地没有保存用户信息
  if (to.meta.requiresAuth && !localStorage.getItem('userInfo')) {//当前要进入的那条路由有meta: { requiresAuth: true }说明这一页需要登录才能访问。
    //;!localStorage.getItem('userInfo') 本地浏览器缓存里找不到 userInfo 这一项（登录成功后通常会把用户信息或 token 存进去）。
    next('/login') // 强制跳转到登录页
  } else {
    next() // 允许访问
  }
})*/

export default router
