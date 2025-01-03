<template>
  <div class="login-container">
    <el-card class="login-box">
      <template #header>
        <div class="header">
          <span class="title">用户登录</span>
        </div>
      </template>
      
      <el-form ref="formRef" :model="loginForm" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="captcha">
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="验证码">
              <template #prefix>
                <el-icon><Key /></el-icon>
              </template>
            </el-input>
            <img 
              :src="captchaUrl" 
              alt="验证码" 
              class="captcha-img"
              @click="refreshCaptcha"
            />
          </div>
        </el-form-item>

        <el-button type="primary" class="login-button" @click="handleLogin" :loading="loading">
          登录
        </el-button>
      </el-form>

      <div class="register-link">
        <el-link type="primary" @click="router.push('/register')">还没有账号？立即注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UserLoginParams } from '@/types/api'
import { ElMessage } from 'element-plus'
import { User, Lock, Key } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const captchaUrl = ref('/api/kaptcha/code')

const loginForm = reactive<UserLoginParams>({
  username: '',
  password: '',
  captcha: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 4, max: 4, message: '验证码长度必须为4位', trigger: 'blur' }
  ]
}

const refreshCaptcha = () => {
  captchaUrl.value = `/api/kaptcha/code?t=${Date.now()}`
}

const formRef = ref<FormInstance>()

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    console.log('登录参数:', loginForm)
    await userStore.login(loginForm)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error: any) {
    console.error('登录错误:', error)
    if (error.captcha) {
      ElMessage.error('请输入正确的验证码')
    } else if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('登录失败')
    }
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--el-fill-color-light);
}

.login-box {
  width: 400px;
  
  .header {
    text-align: center;
    
    .title {
      font-size: 20px;
      font-weight: bold;
    }
  }
}

.captcha-container {
  display: flex;
  gap: 12px;
  
  .el-input {
    flex: 1;
  }
  
  .captcha-img {
    height: 40px;
    cursor: pointer;
    border-radius: 4px;
  }
}

.login-button {
  width: 100%;
  margin-top: 12px;
}

.register-link {
  margin-top: 16px;
  text-align: center;
}
</style> 