<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getCartList, updateCartQuantity, removeFromCart, updateCartSelection } from '@/api/cart'
import type { CartItem, RemoveFromCartRes, RemoveFromCartParams } from '@/api/cart'
import type { Result } from '@/types/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus, Delete } from '@element-plus/icons-vue'

const cartItems = ref<CartItem[]>([])
const loading = ref(true)

// 计算总价
const totalPrice = computed(() => {
  return cartItems.value
    .filter(item => item.selected === 1)
    .reduce((total, item) => {
      return total + (item.price || 0) * item.quantity
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
    const res = response.data as Result<CartItem[]>
    if (res.code === 200) {
      cartItems.value = res.data
    } else {
      ElMessage.error(res.message || '获取购物车列表失败')
    }
  } catch (error) {
    ElMessage.error('获取购物车列表失败')
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
    
    const params: RemoveFromCartParams = {}
    const response = await removeFromCart(item.productId, params)
    if (response.data) {
      const res = response.data as RemoveFromCartRes
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
              <h3 class="item-name">{{ item.productName }}</h3>
              <div class="item-price">¥{{ formatPrice(item.price) }}</div>
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
            <el-button type="primary" size="large">结算</el-button>
          </div>
        </el-card>
      </template>
    </div>
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
</style> 