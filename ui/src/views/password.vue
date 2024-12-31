<template>
  <div class="password-container">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>
      
      <el-form ref="formRef" :model="passwordForm" :rules="rules">
        <el-form-item prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="原密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="新密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="确认新密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <div class="code-container">
            <el-input v-model="passwordForm.email" placeholder="请输入邮箱">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
            <el-button 
              :disabled="!!countdown || !passwordForm.email" 
              @click="sendEmailCode"
            >
              {{ countdown ? `${countdown}s后重试` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item prop="emailCode">
          <el-input v-model="passwordForm.emailCode" placeholder="邮箱验证码">
            <template #prefix>
              <el-icon><Key /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit" block>
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { UpdatePasswordParams } from '@/types/api'
import { ElMessage } from 'element-plus'
import { Lock, Message, Key } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import { userApi } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const countdown = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

const passwordForm = reactive<UpdatePasswordParams>({
  oldPassword: '',
  newPassword: '',
  emailCode: '',
  confirmPassword: '',
  email: ''
})

const validateNewPassword = (_rule: any, value: string, callback: Function) => {
  if (!value) {
    callback(new Error('请输入新密码'))
    return
  }
  
  if (value === passwordForm.oldPassword) {
    callback(new Error('新密码不能与原密码相同'))
    return
  }

  if (value.length < 6 || value.length > 20) {
    callback(new Error('密码长度在6-20个字符之间'))
    return
  }

  if (passwordForm.confirmPassword) {
    formRef.value?.validateField('confirmPassword')
  }
  callback()
}

const validateConfirmPassword = (_rule: any, value: string, callback: Function) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
    return
  }
  
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
    return
  }
  callback()
}

// 监听新密码变化
watch(() => passwordForm.newPassword, (newVal) => {
  if (newVal && passwordForm.confirmPassword) {
    formRef.value?.validateField('confirmPassword')
  }
}, { immediate: true })

// 监听确认密码变化
watch(() => passwordForm.confirmPassword, (newVal) => {
  if (newVal && passwordForm.newPassword) {
    formRef.value?.validateField('confirmPassword')
  }
}, { immediate: true })

const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
  ],
  newPassword: [
    { validator: validateNewPassword, trigger: ['blur', 'change'] }
  ],
  confirmPassword: [
    { validator: validateConfirmPassword, trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  emailCode: [
    { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度必须为6位', trigger: 'blur' }
  ]
}

const formRef = ref<FormInstance>()

const sendEmailCode = async () => {
  try {
    loading.value = true
    await userApi.sendEmailCode(passwordForm.email)
    ElMessage.success('验证码已发送到您的邮箱')
    countdown.value = 60
    timer = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--
      } else if (timer) {
        clearInterval(timer)
        timer = null
      }
    }, 1000)
  } catch (error: any) {
    ElMessage.error(error.message || '发送失败')
  } finally {
    loading.value = false
  }
}

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    await userStore.updatePassword(passwordForm)
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('修改失败')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.password-container {
  padding: 20px;
  
  .password-card {
    max-width: 500px;
    margin: 0 auto;
    
    .card-header {
      text-align: center;
      font-size: 18px;
    }

    .code-container {
      display: flex;
      gap: 12px;

      .el-input {
        flex: 1;
      }

      .el-button {
        width: 120px;
      }
    }
  }
}
</style> 