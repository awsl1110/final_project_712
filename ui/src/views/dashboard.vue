<template>
  <div class="dashboard-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>欢迎使用</span>
        </div>
      </template>
      <div class="card-content">
        这里是系统首页
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  
  .card-header {
    font-size: 18px;
  }
  
  .card-content {
    font-size: 14px;
    color: #666;
  }
}
</style> 