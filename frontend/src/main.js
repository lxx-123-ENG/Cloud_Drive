// src/main.js
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 1. 导入 Element UI 和样式（关键！缺一不可）
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 2. 导入 Font Awesome（图标，之前的代码可能已有）
import 'font-awesome/css/font-awesome.min.css'

// 3. 全局注册 Element UI（让所有组件都能使用 Element 组件）
Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')