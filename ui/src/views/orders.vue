<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrders, cancelOrder, deleteOrder } from '@/api/order'
import type { OrderInfo } from '@/api/order'
import type { Result } from '@/types/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Document } from '@element-plus/icons-vue'

const router = useRouter()
const orders = ref<OrderInfo[]>([])
const loading = ref(true)

// 获取订单列表
const fetchOrders = async () => {
  try {
    loading.value = true
    const response = await getOrders()
    if (response.data.code === 200) {
      orders.value = response.data.data
    } else if (response.data.code === 500) {
      ElMessage.error('服务器内部错误')
    } else {
      ElMessage.error(response.data.message || '获取订单列表失败')
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 取消订单
const handleCancel = async (order: OrderInfo) => {
  try {
    await ElMessageBox.confirm('确定要取消这个订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await cancelOrder(order.id)
    const res = response.data as Result<any>
    if (res.code === 200) {
      order.status = 4 // 已取消状态
      ElMessage.success('订单已取消')
    } else {
      ElMessage.error(res.message || '取消订单失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消订单失败')
    }
  }
}

// 删除订单
const handleDelete = async (order: OrderInfo) => {
  try {
    await ElMessageBox.confirm('确定要删除这个订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteOrder(order.id)
    if (response.code === 200) {
      orders.value = orders.value.filter(o => o.id !== order.id)
      ElMessage.success('删除成功')
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 获取订单状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待付款',
    1: '待发货',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知状态'
}

// 格式化价格
const formatPrice = (price: number | null | undefined) => {
  if (price == null) return '¥0.00'
  return price.toLocaleString('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  })
}

// 查看订单详情
const handleViewDetail = (orderId: number) => {
  router.push(`/order/${orderId}`)
}

onMounted(() => {
  fetchOrders()
})
</script>

<template>
  <div class="orders-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">我的订单</span>
        </div>
      </template>
    </el-card>

    <div class="orders-content" v-loading="loading">
      <el-empty v-if="orders.length === 0" description="暂无订单" />
      
      <template v-else>
        <el-card v-for="order in orders" :key="order.id" class="order-card">
          <template #header>
            <div class="order-header">
              <div class="order-info">
                <span class="order-number">订单号：{{ order.id }}</span>
                <span class="order-time">下单时间：{{ order.createTime }}</span>
              </div>
              <div class="order-status">
                <el-tag :type="order.status === 4 ? 'info' : order.status === 0 ? 'warning' : 'success'">
                  {{ getStatusText(order.status) }}
                </el-tag>
              </div>
            </div>
          </template>
          
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <div class="item-info">
                <el-image 
                  :src="item.productImage || '/default-product.jpg'" 
                  fit="cover"
                  class="item-image"
                />
                <div class="item-details">
                  <h3 class="item-name">{{ item.productName }}</h3>
                  <div class="item-price">
                    <span>{{ formatPrice(item.productPrice) }} × {{ item.quantity }}</span>
                    <span class="subtotal">小计：{{ formatPrice(item.subtotal) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-total">
              <div class="amount-detail">
                <p>商品总额：{{ formatPrice(order.totalAmount) }}</p>
                <p v-if="order.discountAmount > 0">优惠金额：-{{ formatPrice(order.discountAmount) }}</p>
                <p class="final-amount">实付金额：{{ formatPrice(order.payAmount) }}</p>
              </div>
            </div>
            <div class="order-actions">
              <el-button 
                v-if="order.status === 0" 
                type="primary"
                size="small"
              >
                去付款
              </el-button>
              <el-button 
                v-if="order.status !== 4" 
                type="warning"
                size="small"
                @click="handleCancel(order)"
              >
                取消订单
              </el-button>
              <el-button
                type="info"
                size="small"
                :icon="Document"
                @click="handleViewDetail(order.id)"
              >
                订单详情
              </el-button>
              <el-button 
                type="danger"
                size="small"
                :icon="Delete"
                @click="handleDelete(order)"
              >
                删除订单
              </el-button>
            </div>
          </div>
        </el-card>
      </template>
    </div>
  </div>
</template>

<style scoped>
.orders-container {
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

.orders-content {
  min-height: 200px;
}

.order-card {
  margin-bottom: 20px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.order-items {
  margin: 20px 0;
}

.order-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  gap: 20px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
}

.item-details {
  flex: 1;
}

.item-name {
  margin: 0 0 10px;
  font-size: 16px;
  color: #333;
}

.item-price {
  display: flex;
  justify-content: space-between;
  color: #666;
}

.subtotal {
  color: #f56c6c;
  font-weight: bold;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.amount-detail {
  text-align: right;
}

.amount-detail p {
  margin: 5px 0;
  color: #666;
}

.final-amount {
  color: #f56c6c;
  font-size: 16px;
  font-weight: bold;
}

.order-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
</style> 