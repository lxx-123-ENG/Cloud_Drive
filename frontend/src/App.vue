<template>
  <div id="app">
    <!-- 路由出口：所有匹配的组件会在这里显示 -->
    <router-view/>
  </div>
</template>

<style>
/* 全局基础样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Helvetica Neue", Arial, sans-serif;
}

body {
  background-color: #f5f7fa;
  color: #333;
  min-height: 100vh;
}
</style>