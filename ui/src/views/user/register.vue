<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <span>用户注册</span>
        </div>
      </template>
      
      <el-form ref="formRef" :model="registerForm" :rules="rules">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" block>
            注册
          </el-button>
          <el-link type="primary" @click="router.push('/login')" class="login-link">
            已有账号？去登录
          </el-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UserRegisterParams } from '@/types/api'
import { ElMessage } from 'element-plus'
import { User, Lock, Message } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const registerForm = reactive<UserRegisterParams>({
  username: '',
  password: '',
  email: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const formRef = ref<FormInstance>()

const handleRegister = async () => {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    await userStore.register(registerForm)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error: any) {
    console.error('注册错误:', error)
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('注册失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
  
  .register-card {
    width: 400px;
    
    .card-header {
      text-align: center;
      font-size: 20px;
    }

    .login-link {
      display: block;
      text-align: center;
      margin-top: 12px;
    }
  }
}
</style> 