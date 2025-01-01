<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { OrderInfo } from '@/api/order'
import { getOrderDetail } from '@/api/order'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const order = ref<OrderInfo | null>(null)

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

// 获取订单详情
const fetchOrderDetail = async () => {
  try {
    loading.value = true
    const orderId = Number(route.params.id)
    if (isNaN(orderId)) {
      ElMessage.error('无效的订单ID')
      router.push('/orders')
      return
    }

    const response = await getOrderDetail(orderId)
    if (response.data.code === 200) {
      order.value = response.data.data
    } else {
      ElMessage.error(response.data.message || '获取订单详情失败')
      router.push('/orders')
    }
  } catch (error: any) {
    console.error('获取订单详情失败:', error)
    ElMessage.error(error.response?.data?.message || '获取订单详情失败')
    router.push('/orders')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchOrderDetail()
})
</script>

<template>
  <div class="order-detail-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">订单详情</span>
        </div>
      </template>
    </el-card>

    <div class="order-content" v-loading="loading">
      <el-empty v-if="!order" description="订单不存在" />
      
      <template v-else>
        <el-card class="order-card">
          <template #header>
            <div class="order-header">
              <div class="order-info">
                <span class="order-number">订单号：{{ order.id }}</span>
                <span class="order-time">下单时间：{{ order.createTime }}</span>
              </div>
              <div class="order-status">
                <el-tag :type="order.status === 4 ? 'info' : 'success'">
                  {{ getStatusText(order.status) }}
                </el-tag>
              </div>
            </div>
          </template>
          
          <div class="order-section">
            <h3>收货信息</h3>
            <div class="delivery-info">
              <p><span class="label">收货人：</span>{{ order.receiverName }}</p>
              <p><span class="label">联系电话：</span>{{ order.receiverPhone }}</p>
              <p><span class="label">收货地址：</span>{{ order.address }}</p>
            </div>
          </div>

          <div class="order-section">
            <h3>商品信息</h3>
            <div class="order-items">
              <div v-for="item in order.items" :key="item.id" class="order-item">
                <div class="item-info">
                  <el-image 
                    :src="item.productImage || '/default-product.jpg'" 
                    fit="cover"
                    class="item-image"
                  />
                  <div class="item-details">
                    <h4 class="item-name">{{ item.productName }}</h4>
                    <div class="item-price">
                      <span>{{ formatPrice(item.productPrice) }} × {{ item.quantity }}</span>
                      <span class="subtotal">小计：{{ formatPrice(item.subtotal) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="order-section">
            <h3>订单信息</h3>
            <div class="order-summary">
              <p><span class="label">订单备注：</span>{{ order.remark || '无' }}</p>
              <p><span class="label">创建时间：</span>{{ order.createTime }}</p>
              <p><span class="label">更新时间：</span>{{ order.updateTime }}</p>
              <div class="total-amount">
                总计：<span class="price">{{ formatPrice(order.totalAmount) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </template>
    </div>
  </div>
</template>

<style scoped>
.order-detail-container {
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

.order-content {
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

.order-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #EBEEF5;
}

.order-section:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.order-section h3 {
  margin: 0 0 16px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.delivery-info p {
  margin: 8px 0;
  color: #666;
}

.label {
  color: #999;
  margin-right: 8px;
}

.order-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.item-info {
  display: flex;
  gap: 16px;
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
  margin: 0 0 8px;
  font-size: 14px;
  font-weight: bold;
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

.order-summary {
  color: #666;
}

.order-summary p {
  margin: 8px 0;
}

.total-amount {
  margin-top: 16px;
  text-align: right;
  font-size: 16px;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}
</style> 