<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button type="primary" @click="refreshProfile">刷新</el-button>
        </div>
      </template>
      
      <div class="avatar-section">
        <el-avatar 
          :size="100" 
          :src="userStore.avatarUrl"
          @error="() => true"
        >
          {{ userStore.userProfile?.name?.charAt(0)?.toUpperCase() }}
        </el-avatar>
        <el-button type="primary" @click="uploadAvatar" class="upload-btn">
          更换头像
        </el-button>
      </div>

      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户ID">
          {{ userStore.userProfile?.id }}
        </el-descriptions-item>
        <el-descriptions-item label="用户名">
          {{ userStore.userProfile?.name }}
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          {{ userStore.userProfile?.email }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 收货地址卡片 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收货地址</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleAddAddress">添加地址</el-button>
            <el-button type="primary" @click="refreshAddresses">刷新</el-button>
          </div>
        </div>
      </template>

      <!-- 空状态提示 -->
      <el-empty
        v-if="!addresses.length"
        description="暂无收货地址"
      >
        <el-button type="primary" @click="handleAddAddress">添加收货地址</el-button>
      </el-empty>

      <!-- 地址列表 -->
      <el-collapse v-else>
        <el-collapse-item 
          v-for="address in addresses" 
          :key="address.id"
          :title="getAddressTitle(address)"
        >
          <template #extra>
            <div class="address-actions">
              <el-button 
                v-if="!address.isDefault" 
                type="primary" 
                link 
                @click.stop="handleSetDefault(address)"
              >
                设为默认
              </el-button>
              <el-button type="primary" link @click.stop="handleEditAddress(address)">
                编辑
              </el-button>
              <el-button type="danger" link @click.stop="handleDeleteAddress(address)">
                删除
              </el-button>
            </div>
          </template>

          <el-descriptions :column="1" border>
            <el-descriptions-item label="收货人">
              {{ address.receiverName }}
            </el-descriptions-item>
            <el-descriptions-item label="联系电话">
              {{ address.receiverPhone }}
            </el-descriptions-item>
            <el-descriptions-item label="所在地区">
              {{ `${address.province}${address.city}${address.district}` }}
            </el-descriptions-item>
            <el-descriptions-item label="详细地址">
              {{ address.detailAddress }}
            </el-descriptions-item>
            <el-descriptions-item label="是否默认">
              {{ address.isDefault ? '是' : '否' }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDate(address.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间">
              {{ formatDate(address.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <input
      type="file"
      ref="fileInput"
      style="display: none"
      accept="image/*"
      @change="handleFileChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { addressApi } from '@/api/address'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Address } from '@/types/api'
import { formatDate } from '@/utils/date'

const userStore = useUserStore()
const fileInput = ref<HTMLInputElement>()
const addresses = ref<Address[]>([])

onMounted(async () => {
  try {
    await Promise.all([
      userStore.fetchUserProfile(),
      fetchAddresses()
    ])
  } catch (error) {
    ElMessage.error('获取信息失败')
  }
})

const fetchAddresses = async () => {
  try {
    const res = await addressApi.getAddressList()
    addresses.value = res.data.data
  } catch (error) {
    console.error('获取地址列表失败:', error)
    ElMessage.error('获取地址列表失败')
  }
}

const refreshAddresses = async () => {
  try {
    await fetchAddresses()
    ElMessage.success('刷新地址列表成功')
  } catch (error) {
    ElMessage.error('刷新地址列表失败')
  }
}

const refreshProfile = async () => {
  try {
    await userStore.fetchUserProfile()
    ElMessage.success('刷新成功')
  } catch (error) {
    ElMessage.error('刷新失败')
  }
}

const uploadAvatar = () => {
  fileInput.value?.click()
}

const handleFileChange = async (event: Event) => {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return

  try {
    await userStore.uploadAvatar(file)
    ElMessage.success('头像上传成功')
  } catch (error: any) {
    ElMessage.error(error.message || '头像上传失败')
  } finally {
    // 清空input，以便可以重复上传同一个文件
    input.value = ''
  }
}

const getAddressTitle = (address: Address) => {
  return `${address.province}${address.city}${address.district} ${address.detailAddress} ${address.isDefault ? '(默认)' : ''}`
}

// 处理添加地址
const handleAddAddress = () => {
  ElMessage.info('添加地址功能开发中...')
}

// 处理编辑地址
const handleEditAddress = (address: Address) => {
  ElMessage.info('编辑地址功能开发中...')
}

// 处理删除地址
const handleDeleteAddress = (address: Address) => {
  ElMessageBox.confirm(
    '确定要删除这个地址吗？',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.info('删除地址功能开发中...')
  }).catch(() => {
    // 用户取消删除，不做任何操作
  })
}

// 设置默认地址
const handleSetDefault = (address: Address) => {
  ElMessage.info('设置默认地址功能开发中...')
}
</script>

<style scoped>
.profile-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.profile-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.upload-btn {
  margin-top: 10px;
}

:deep(.el-collapse-item__header) {
  font-size: 14px;
}

:deep(.el-collapse) {
  border: none;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.address-actions {
  display: flex;
  gap: 8px;
  margin-right: 16px;
}

:deep(.el-collapse-item__header) {
  font-size: 14px;
  display: flex;
  justify-content: space-between;
}

:deep(.el-collapse-item__content) {
  padding: 16px;
}

:deep(.el-empty) {
  padding: 40px 0;
}
</style> 