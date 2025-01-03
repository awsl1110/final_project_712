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
      <el-empty v-if="cartItems.length === 0" description="购物车是空的">
        <el-button type="primary" @click="router.push('/products')">去购物</el-button>
      </el-empty>
      
      <template v-else>
        <transition-group name="cart-item">
          <el-card v-for="item in cartItems" :key="item.id" class="cart-item">
            <div class="item-content">
              <el-checkbox 
                v-model="item.selected" 
                :true-label="1"
                :false-label="0"
                @change="() => handleSelectionChange(item)"
              />
              <el-image 
                :src="item.product.productImage" 
                fit="cover"
                class="product-image"
                :preview-src-list="[item.product.productImage]"
              >
                <template #error>
                  <div class="image-placeholder">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="item-info">
                <h3 class="item-name" @click="router.push(`/product/${item.product.id}`)">
                  {{ item.product.productName }}
                </h3>
                <div class="item-price">¥{{ formatPrice(item.product.productPrice) }}</div>
              </div>
              <div class="item-actions">
                <el-input-number 
                  v-model="item.quantity"
                  :min="1"
                  :max="99"
                  size="small"
                  @change="(val: number) => handleQuantityChange(item, val)"
                  :loading="loadingItem === item.id"
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
        </transition-group>

        <el-card class="cart-footer" v-if="cartItems.length > 0">
          <div class="footer-content">
            <div class="footer-left">
              <el-checkbox 
                v-model="selectAll" 
                @change="handleSelectAll"
                :indeterminate="isIndeterminate"
              >
                全选
              </el-checkbox>
              <div class="selected-count">
                已选 {{ selectedCount }} 件商品
              </div>
            </div>
            <div class="footer-right">
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
          <el-form-item label="优惠券">
            <div v-loading="couponLoading">
              <el-popover
                placement="bottom-start"
                :width="400"
                trigger="click"
                popper-class="coupon-popover"
              >
                <template #reference>
                  <el-input
                    readonly
                    :placeholder="userCouponId === 0 ? '不使用优惠券' : userCoupons.find(c => c.id === userCouponId)?.coupon.name || '请选择优惠券'"
                    :suffix-icon="ArrowDown"
                    style="width: 100%"
                  />
                </template>

                <div style="max-height: 300px; overflow-y: auto">
                  <el-radio-group v-model="userCouponId" style="width: 100%">
                    <div style="padding: 8px 0">
                      <el-radio :label="0" style="width: 100%; margin: 0; padding: 8px 16px">
                        <div style="display: flex; justify-content: space-between; align-items: center; width: 100%">
                          <span>不使用优惠券</span>
                          <span style="color: var(--el-text-color-secondary)">按原价支付</span>
                        </div>
                      </el-radio>
                    </div>

                    <div v-for="userCoupon in userCoupons" :key="userCoupon.id" style="padding: 4px 0">
                      <el-radio
                        :label="userCoupon.id"
                        :disabled="userCoupon.coupon.minAmount > totalPrice"
                        style="width: 100%; margin: 0; padding: 8px 16px"
                      >
                        <div style="display: flex; justify-content: space-between; align-items: center; width: 100%">
                          <div style="display: flex; align-items: center; gap: 12px">
                            <span style="color: var(--el-color-danger); font-size: 16px; font-weight: bold; min-width: 80px">
                              <template v-if="userCoupon.coupon.type === 1">
                                ¥{{ userCoupon.coupon.value }}
                              </template>
                              <template v-else>
                                {{ userCoupon.coupon.value }}折
                              </template>
                            </span>
                            <div style="display: flex; flex-direction: column; gap: 4px">
                              <span>{{ userCoupon.coupon.name }}</span>
                              <span style="color: var(--el-text-color-secondary); font-size: 12px">
                                满{{ userCoupon.coupon.minAmount }}元可用
                              </span>
                            </div>
                          </div>
                          <span style="color: var(--el-text-color-secondary); font-size: 12px">
                            有效期至：{{ formatDate(userCoupon.coupon.endTime) }}
                          </span>
                        </div>
                      </el-radio>
                    </div>
                  </el-radio-group>

                  <el-empty 
                    v-if="userCoupons.length === 0" 
                    description="暂无可用优惠券"
                  >
                    <template #image>
                      <el-icon :size="60">
                        <Ticket />
                      </el-icon>
                    </template>
                  </el-empty>
                </div>
              </el-popover>
            </div>
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
<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { getCartList, updateCartQuantity, removeFromCart, updateCartSelection } from '@/api/cart'
import { createOrder } from '@/api/order'
import type { CartItem, CartResponse } from '@/api/cart'
import type { Result, Address, UserCoupon } from '@/types/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus, Delete, Ticket, ArrowDown, Picture } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { addressApi } from '@/api/address'
import { getUserCoupons } from '@/api/coupon'
import { formatDate } from '@/utils/date'

const router = useRouter()
const cartItems = ref<CartItem[]>([])
const loading = ref(true)
const loadingItem = ref<number | null>(null)
const addressId = ref<number>(0)
const remark = ref('')
const showAddressDialog = ref(false)
const addresses = ref<Address[]>([])
const addressLoading = ref(false)
const userCouponId = ref<number>(0)
const userCoupons = ref<UserCoupon[]>([])
const couponLoading = ref(false)

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
      // 确保选中状态是数字类型
      cartItems.value.forEach(item => {
        item.selected = typeof item.selected === 'boolean' ? (item.selected ? 1 : 0) : item.selected
      })
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
    loadingItem.value = item.id
    const newSelected = item.selected === 1 ? 0 : 1
    const originalSelected = item.selected
    
    // 先更新本地状态，提供即时反馈
    item.selected = newSelected
    
    const response = await updateCartSelection(item.id, newSelected)
    if (response.data.code !== 200) {
      // 如果更新失败，恢复原来的状态
      item.selected = originalSelected
      ElMessage.error(response.data.message || '更新选中状态失败')
    }
  } catch (error) {
    // 如果发生错误，恢复原来的状态
    item.selected = item.selected === 1 ? 0 : 1
    ElMessage.error('更新选中状态失败')
  } finally {
    loadingItem.value = null
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

// 获取用户优惠券列表
const fetchUserCoupons = async () => {
  try {
    couponLoading.value = true
    const response = await getUserCoupons()
    if (response.data.code === 200) {
      // 只获取未使用的优惠券
      userCoupons.value = response.data.data.filter(uc => uc.status === 0)
    }
  } catch (error) {
    ElMessage.error('获取优惠券列表失败')
  } finally {
    couponLoading.value = false
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
  // 打开对话框时获取地址列表和优惠券列表
  fetchAddresses()
  fetchUserCoupons()
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
      remark: remark.value || '',  // 确保remark不为undefined
      userCouponId: userCouponId.value || undefined  // 添加优惠券ID
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

// 添加全选相关的计算属性和方法
const selectAll = computed({
  get: () => cartItems.value.length > 0 && cartItems.value.every(item => item.selected === 1),
  set: (val) => handleSelectAll(val)
})

const isIndeterminate = computed(() => {
  const selected = cartItems.value.filter(item => item.selected === 1)
  return selected.length > 0 && selected.length < cartItems.value.length
})

const selectedCount = computed(() => cartItems.value.filter(item => item.selected === 1).length)

const handleSelectAll = async (val: boolean) => {
  const newSelection = val ? 1 : 0
  try {
    loading.value = true
    // 先更新本地状态
    const originalStates = cartItems.value.map(item => item.selected)
    cartItems.value.forEach(item => item.selected = newSelection)

    // 发送请求到服务器
    const results = await Promise.allSettled(
      cartItems.value.map(item => 
        updateCartSelection(item.id, newSelection)
      )
    )

    // 检查是否有失败的请求
    const hasError = results.some(result => 
      result.status === 'rejected' || 
      (result.status === 'fulfilled' && result.value.data.code !== 200)
    )
    
    if (hasError) {
      // 如果有失败的请求，恢复原来的状态
      cartItems.value.forEach((item, index) => {
        item.selected = originalStates[index]
      })
      ElMessage.error('部分商品更新失败')
    }
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCartList()
})
</script>
<style scoped lang="scss">
.cart-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
  background: #f5f7fa;

  .page-header {
    margin-bottom: 20px;
    .header {
      .title {
        font-size: 20px;
        font-weight: bold;
      }
    }
  }

  .cart-content {
    .cart-item {
      margin-bottom: 16px;
      transition: all 0.3s ease;
      border: none;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
      }

      .item-content {
        display: flex;
        align-items: center;
        gap: 20px;
        padding: 10px;

        .product-image {
          width: 100px;
          height: 100px;
          border-radius: 8px;
          overflow: hidden;
          flex-shrink: 0;

          .image-placeholder {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f5f7fa;
            color: #909399;
            font-size: 24px;
          }
        }

        .item-info {
          flex: 1;
          min-width: 0;

          .item-name {
            margin: 0 0 8px;
            font-size: 16px;
            color: #303133;
            cursor: pointer;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;

            &:hover {
              color: var(--el-color-primary);
            }
          }

          .item-price {
            color: #f56c6c;
            font-size: 18px;
            font-weight: bold;
          }
        }

        .item-actions {
          display: flex;
          align-items: center;
          gap: 12px;
        }
      }
    }

    .cart-footer {
      position: sticky;
      bottom: 20px;
      margin-top: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      background: rgba(255, 255, 255, 0.9);
      backdrop-filter: blur(10px);

      .footer-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px;

        .footer-left {
          display: flex;
          align-items: center;
          gap: 20px;

          .selected-count {
            color: #606266;
          }
        }

        .footer-right {
          display: flex;
          align-items: center;
          gap: 24px;

          .total-price {
            font-size: 16px;
            
            .price {
              color: #f56c6c;
              font-size: 24px;
              font-weight: bold;
            }
          }
        }
      }
    }
  }
}

// 动画效果
.cart-item-enter-active,
.cart-item-leave-active {
  transition: all 0.5s ease;
}

.cart-item-enter-from,
.cart-item-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.cart-item-move {
  transition: transform 0.5s ease;
}
</style>