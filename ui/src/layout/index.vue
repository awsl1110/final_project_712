<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <el-menu
        :default-active="route.path"
        class="el-menu-vertical"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Monitor /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <el-icon><Goods /></el-icon>
          <span>商品列表</span>
        </el-menu-item>
        <el-menu-item index="/cart">
          <el-icon><ShoppingCart /></el-icon>
          <span>购物车</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><List /></el-icon>
          <span>我的订单</span>
        </el-menu-item>
        <el-menu-item index="/favorites">
          <el-icon><Star /></el-icon>
          <span>我的收藏</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar 
                :size="32" 
                class="user-avatar"
                :src="userStore.avatarUrl"
                @error="() => true"
              >
                {{ userStore.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userStore.username }}</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="upload">上传头像</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>

    <input
      type="file"
      ref="fileInput"
      style="display: none"
      accept="image/*"
      @change="handleFileChange"
    />
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Monitor, ArrowDown, Goods, ShoppingCart, List, Star } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const fileInput = ref<HTMLInputElement>()

// 监听token变化
watch(() => userStore.token, (newToken) => {
  if (newToken) {
    // token存在时，等待路由跳转完成后再获取头像
    nextTick(() => {
      setTimeout(() => {
        userStore.fetchAvatar().catch(() => {
          // 忽略错误
        })
      }, 1000)
    })
  }
})

onMounted(() => {
  if (userStore.token) {
    // 组件挂载时，如果token存在，等待组件完全渲染后再获取头像
    nextTick(() => {
      setTimeout(() => {
        userStore.fetchAvatar().catch(() => {
          // 忽略错误
        })
      }, 1000)
    })
  }
})

const handleCommand = async (command: string) => {
  try {
    if (command === 'logout') {
      userStore.logout()
      router.push('/login')
    } else if (command === 'password') {
      router.push('/password')
    } else if (command === 'upload') {
      fileInput.value?.click()
    }
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const handleFileChange = async (event: Event) => {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  
  if (file) {
    const loadingMessage = ElMessage.info({
      message: '正在上传头像，请稍候...',
      duration: 0
    })
    
    try {
      const result = await userStore.uploadAvatar(file)
      loadingMessage.close()
      
      if (result === true) {
        ElMessage.success('头像上传成功')
      } else {
        ElMessage.warning('头像可能已上传，请刷新页面查看')
      }
    } catch (error: any) {
      console.error('上传错误:', error)
      loadingMessage.close()
      
      // 如果头像已经更新，说明实际上传成功了
      if (userStore.avatarUrl) {
        ElMessage.success('头像上传成功')
      } else {
        // 如果是业务错误（比如文件太大），显示具体错误信息
        if (error.message?.includes('请刷新页面')) {
          ElMessage.warning(error.message)
        } else if (error.message) {
          ElMessage.error(error.message)
        } else {
          ElMessage.error('头像上传失败，请重试')
        }
        
        // 如果上传失败，尝试重新获取头像
        setTimeout(() => {
          userStore.fetchAvatar().catch(() => {
            // 忽略错误
          })
        }, 1000)
      }
    }
  }
  
  // 清除input的值，这样相同文件可以重复上传
  input.value = ''
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100%;
  
  .el-menu-vertical {
    height: 100%;
  }
  
  .el-header {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    border-bottom: 1px solid #dcdfe6;
    
    .header-right {
      .el-dropdown-link {
        display: flex;
        align-items: center;
        cursor: pointer;
        
        .user-avatar {
          background-color: #409eff;
          color: #fff;
          margin-right: 8px;
        }
        
        .username {
          margin-right: 4px;
          font-size: 14px;
        }
      }
    }
  }
}
</style> 