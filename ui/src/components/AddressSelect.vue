<template>
  <el-dialog
    v-model="visible"
    title="选择收货地址"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <!-- 空状态提示 -->
    <el-empty
      v-if="!addresses.length"
      description="暂无收货地址"
    >
      <el-button type="primary" @click="handleAddAddress">添加收货地址</el-button>
    </el-empty>

    <!-- 地址列表 -->
    <div v-else class="address-list">
      <el-radio-group v-model="selectedAddressId" class="address-radio-group">
        <el-radio
          v-for="address in addresses"
          :key="address.id"
          :label="address.id"
          class="address-radio"
        >
          <div class="address-item">
            <div class="address-header">
              <span class="receiver">{{ address.receiverName }}</span>
              <span class="phone">{{ address.receiverPhone }}</span>
              <el-tag v-if="address.isDefault" size="small" type="success">默认</el-tag>
            </div>
            <div class="address-content">
              {{ `${address.province}${address.city}${address.district}${address.detailAddress}` }}
            </div>
          </div>
        </el-radio>
      </el-radio-group>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleConfirm" :disabled="!selectedAddressId">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits, defineProps, watch } from 'vue'
import { addressApi } from '@/api/address'
import { ElMessage } from 'element-plus'
import type { Address } from '@/types/api'

const props = defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'select', address: Address): void
}>()

const visible = ref(false)
const addresses = ref<Address[]>([])
const selectedAddressId = ref<number>()

// 监听props.modelValue的变化
watch(() => props.modelValue, (newValue) => {
  visible.value = newValue
  if (newValue && !addresses.value.length) {
    fetchAddresses()
  }
})

// 监听visible的变化
watch(() => visible.value, (newValue) => {
  emit('update:modelValue', newValue)
})

// 获取地址列表
const fetchAddresses = async () => {
  try {
    const res = await addressApi.getAddressList()
    addresses.value = res.data.data
    // 如果有默认地址，自动选中
    const defaultAddress = addresses.value.find(addr => addr.isDefault)
    if (defaultAddress) {
      selectedAddressId.value = defaultAddress.id
    } else if (addresses.value.length > 0) {
      // 如果没有默认地址，选中第一个
      selectedAddressId.value = addresses.value[0].id
    }
  } catch (error) {
    console.error('获取地址列表失败:', error)
    ElMessage.error('获取地址列表失败')
  }
}

// 组件加载时获取地址列表
onMounted(() => {
  fetchAddresses()
})

// 处理添加地址
const handleAddAddress = () => {
  ElMessage.info('添加地址功能开发中...')
}

// 处理关闭
const handleClose = () => {
  visible.value = false
  selectedAddressId.value = undefined
}

// 处理确认选择
const handleConfirm = () => {
  const selectedAddress = addresses.value.find(addr => addr.id === selectedAddressId.value)
  if (selectedAddress) {
    emit('select', selectedAddress)
    handleClose()
  }
}
</script>

<style scoped>
.address-list {
  max-height: 400px;
  overflow-y: auto;
}

.address-radio-group {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-radio {
  width: 100%;
  margin-right: 0;
  padding: 12px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  transition: all 0.3s;
}

.address-radio:hover {
  border-color: #409EFF;
}

.address-radio :deep(.el-radio__label) {
  padding-left: 12px;
  width: 100%;
}

.address-item {
  width: 100%;
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 