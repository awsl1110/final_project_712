<template>
  <div class="checkout-container">
    <el-card class="address-card">
      <template #header>
        <div class="card-header">
          <span>收货地址</span>
        </div>
      </template>

      <!-- 已选择地址 -->
      <div v-if="selectedAddress" class="selected-address">
        <div class="address-info">
          <div class="address-header">
            <span class="receiver">{{ selectedAddress.receiverName }}</span>
            <span class="phone">{{ selectedAddress.receiverPhone }}</span>
            <el-tag v-if="selectedAddress.isDefault" size="small" type="success">默认</el-tag>
          </div>
          <div class="address-content">
            {{ `${selectedAddress.province}${selectedAddress.city}${selectedAddress.district}${selectedAddress.detailAddress}` }}
          </div>
        </div>
        <el-button type="primary" link @click="showAddressSelect = true">更换地址</el-button>
      </div>

      <!-- 未选择地址 -->
      <el-empty v-else description="请选择收货地址">
        <el-button type="primary" @click="showAddressSelect = true">选择地址</el-button>
      </el-empty>
    </el-card>

    <!-- 其他结算信息... -->

    <!-- 地址选择对话框 -->
    <address-select
      v-model="showAddressSelect"
      @select="handleAddressSelect"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import AddressSelect from '@/components/AddressSelect.vue'
import { addressApi } from '@/api/address'
import { ElMessage } from 'element-plus'
import type { Address } from '@/types/api'

const showAddressSelect = ref(false)
const selectedAddress = ref<Address>()

// 初始化获取地址列表
const initAddress = async () => {
  try {
    const res = await addressApi.getAddressList()
    const addresses = res.data.data
    
    // 如果有地址，选择默认地址或第一个地址
    if (addresses && addresses.length > 0) {
      const defaultAddress = addresses.find(addr => addr.isDefault)
      selectedAddress.value = defaultAddress || addresses[0]
    } else {
      // 如果没有地址，自动打开地址选择框
      showAddressSelect.value = true
    }
  } catch (error) {
    console.error('获取地址列表失败:', error)
    ElMessage.error('获取地址列表失败')
    // 获取失败也打开地址选择框
    showAddressSelect.value = true
  }
}

// 处理地址选择
const handleAddressSelect = (address: Address) => {
  selectedAddress.value = address
}

// 组件加载时初始化地址
onMounted(() => {
  initAddress()
})
</script>

<style scoped>
.checkout-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.address-card {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
}

.selected-address {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 12px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
}

.address-info {
  flex: 1;
}

.address-header {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.receiver {
  font-weight: bold;
}

.phone {
  color: #606266;
}

.address-content {
  color: #606266;
  line-height: 1.5;
}
</style> 