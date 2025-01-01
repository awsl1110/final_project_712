<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getCartList, updateCartQuantity, removeFromCart, updateCartSelection } from '@/api/cart'
import { createOrder } from '@/api/order'
import type { CartItem, CartResponse, CartListRes } from '@/api/cart'
import type { Result } from '@/types/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus, Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { addressApi } from '@/api/address'
import type { Address } from '@/types/api'

const router = useRouter()
const cartItems = ref<CartItem[]>([])
const loading = ref(true)
const loadingItem = ref<number | null>(null)
const addressId = ref<number>(0)
const remark = ref('')
const showAddressDialog = ref(false)
const addresses = ref<Address[]>([])
const addressLoading = ref(false)

// 计算总价
const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.selected === 1)
    .reduce((total, item) => {
      return total + (item.product.productPrice || 0) * item.quantity
    }, 0)
})

// 格式化价格
const formatPrice = (price: number | undefined) => {
  return price ? price.toLocaleString() : '0'
}

// 获取购物车列表
const fetchCartList = async () => {
  try {
    loading.value = true
    const response = await getCartList()
    const res = response.data
    if (res.code === 200) {
      cartItems.value = res.data || []
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')
    } else {
      ElMessage.error(error.message || '获取购物车列表失败')
    }
  } finally {
    loading.value = false
  }
}

// 更新商品数量
const handleQuantityChange = async (item: CartItem, quantity: number) => {
  if (quantity < 1) {
    return
  }
  try {
    const response = await updateCartQuantity(item.id, quantity)
    const res = response.data as Result<any>
    if (res.code === 200) {
      item.quantity = quantity
      ElMessage.success('更新数量成功')
    } else {
      ElMessage.error(res.message || '更新数量失败')
    }
  } catch (error) {
    ElMessage.error('更新数量失败')
  }
}

// 删除商品
const handleDelete = async (item: CartItem) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    loadingItem.value = item.id
    const response = await removeFromCart(item.productId)
    if (response.data) {
      const res = response.data as CartResponse
      if (res.code === 200) {
        cartItems.value = cartItems.value.filter(i => i.id !== item.id)
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  } finally {
    loadingItem.value = null
  }
}

// 更新选中状态
const handleSelectionChange = async (item: CartItem) => {
  try {
    const newSelected = item.selected === 1 ? 0 : 1
    const response = await updateCartSelection(item.id, newSelected)
    const res = response.data as Result<any>
    if (res.code === 200) {
      item.selected = newSelected
    } else {
      ElMessage.error(res.message || '更新选中状态失败')
    }
  } catch (error) {
    ElMessage.error('更新选中状态失败')
  }
}

// 获取地址列表
const fetchAddresses = async () => {
  try {
    addressLoading.value = true
    const res = await addressApi.getAddressList()
    if (res.data.code === 200) {
      addresses.value = res.data.data
      // 如果有默认地址，自动选择
      const defaultAddress = addresses.value.find(addr => addr.isDefault)
      if (defaultAddress) {
        addressId.value = defaultAddress.id
      } else if (addresses.value.length > 0) {
        // 如果没有默认地址，选择第一个
        addressId.value = addresses.value[0].id
      }
    }
  } catch (error) {
    ElMessage.error('获取地址列表失败')
  } finally {
    addressLoading.value = false
  }
}

// 处理结算按钮点击
const handleCheckoutClick = () => {
  const selectedItems = cartItems.value.filter(item => item.selected === 1)
  if (selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  showAddressDialog.value = true
  // 打开对话框时获取地址列表
  fetchAddresses()
}

// 处理提交订单
const handleSubmitOrder = async () => {
  if (!addressId.value) {
    ElMessage.warning('请选择收货地址')
    return
  }

  try {
    const selectedItems = cartItems.value.filter(item => item.selected === 1)
    // 创建订单
    const createOrderRequest = {
      cartIds: selectedItems.map(item => item.id),
      addressId: addressId.value,
      remark: remark.value || ''  // 确保remark不为undefined
    }

    const token = localStorage.getItem('token')
    if (!token) {
      ElMessage.error('请先登录')
      router.push('/login')
      return
    }

    const response = await createOrder(createOrderRequest)

    if (response.data.code === 200) {
      // 清空已选择的商品
      cartItems.value = cartItems.value.filter(item => item.selected !== 1)
      ElMessage.success('订单创建成功')
      showAddressDialog.value = false
      // 跳转到订单详情页
      router.push(`/order/${response.data.data.id}`)
    } else {
      ElMessage.error(response.data.message || '创建订单失败')
    }
  } catch (error: any) {
    console.error('创建订单失败:', error)
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')
      router.push('/login')
    } else {
      ElMessage.error(error.response?.data?.message || '创建订单失败')
    }
  }
}

onMounted(() => {
  fetchCartList()
})
</script>

<template>
  <div class="cart-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">购物车</span>
        </div>
      </template>
    </el-card>

    <div class="cart-content" v-loading="loading">
      <el-empty v-if="cartItems.length === 0" description="购物车是空的" />
      
      <template v-else>
        <el-card v-for="item in cartItems" :key="item.id" class="cart-item">
          <div class="item-content">
            <el-checkbox 
              v-model="item.selected" 
              :true-label="1"
              :false-label="0"
              @change="() => handleSelectionChange(item)"
            />
            <div class="item-info">
              <h3 class="item-name">{{ item.product.productName }}</h3>
              <div class="item-price">¥{{ formatPrice(item.product.productPrice) }}</div>
            </div>
            <div class="item-actions">
              <el-input-number 
                v-model="item.quantity"
                :min="1"
                :max="99"
                size="small"
                @change="(val: number) => handleQuantityChange(item, val)"
              >
                <template #decrease>
                  <el-icon><Minus /></el-icon>
                </template>
                <template #increase>
                  <el-icon><Plus /></el-icon>
                </template>
              </el-input-number>
              <el-button 
                type="danger" 
                :icon="Delete"
                circle
                @click="handleDelete(item)"
              />
            </div>
          </div>
        </el-card>

        <el-card class="cart-footer" v-if="cartItems.length > 0">
          <div class="footer-content">
            <div class="total-price">
              总计: <span class="price">¥{{ formatPrice(totalPrice) }}</span>
            </div>
            <el-button 
              type="primary" 
              size="large"
              @click="handleCheckoutClick"
              :disabled="!cartItems.some(item => item.selected === 1)"
            >
              结算
            </el-button>
          </div>
        </el-card>
      </template>
    </div>

    <!-- 收货地址选择对话框 -->
    <el-dialog
      v-model="showAddressDialog"
      title="选择收货地址"
      width="50%"
      :close-on-click-modal="false"
    >
      <div class="address-form" v-loading="addressLoading">
        <el-form :model="{ addressId, remark }" label-width="100px">
          <el-form-item label="收货地址" required>
            <el-empty v-if="addresses.length === 0" description="暂无收货地址">
              <el-button type="primary" @click="router.push('/address')">
                添加收货地址
              </el-button>
            </el-empty>
            <template v-else>
              <el-radio-group v-model="addressId" class="address-list">
                <el-radio
                  v-for="address in addresses"
                  :key="address.id"
                  :label="address.id"
                  class="address-item"
                >
                  <div class="address-info">
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
            </template>
            <div class="address-actions">
              <el-link type="primary" @click="router.push('/address')">
                管理收货地址
              </el-link>
            </div>
          </el-form-item>
          <el-form-item label="订单备注">
            <el-input
              v-model="remark"
              type="textarea"
              :rows="3"
              placeholder="请输入订单备注（选填）"
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddressDialog = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="handleSubmitOrder"
            :disabled="!addressId"
          >
            提交订单
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.cart-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.cart-content {
  min-height: 200px;
}

.cart-item {
  margin-bottom: 16px;
}

.item-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.item-info {
  flex: 1;
}

.item-name {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

.item-price {
  margin-top: 8px;
  color: #f56c6c;
  font-size: 16px;
  font-weight: bold;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.cart-footer {
  margin-top: 20px;
}

.footer-content {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
}

.total-price {
  font-size: 16px;
}

.total-price .price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

.address-form {
  padding: 20px;
}

.address-actions {
  margin-top: 8px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.address-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  width: 100%;
  padding: 12px;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  margin: 0;
  height: auto;
}

.address-item.is-checked {
  border-color: var(--el-color-primary);
}

.address-info {
  margin-left: 8px;
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

.address-actions {
  margin-top: 12px;
  text-align: right;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 