<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '@/api/order'
import type { OrderInfo } from '@/api/order'
import { ElMessage } from 'element-plus'
import ReviewForm from '@/components/ReviewForm.vue'

const route = useRoute()
const order = ref<OrderInfo | null>(null)
const loading = ref(true)
const showReviewForm = ref(false)
const currentReviewItem = ref<{
  orderId: number
  productId: number
} | null>(null)

// 获取订单详情
const fetchOrderDetail = async () => {
  const orderId = Number(route.params.id)
  if (!orderId) {
    ElMessage.error('订单ID无效')
    return
  }

  try {
    loading.value = true
    const response = await getOrderDetail(orderId)
    if (response.data.code === 200) {
      order.value = response.data.data
    } else if (response.data.code === 500) {
      ElMessage.error('服务器内部错误')
    } else {
      ElMessage.error(response.data.message || '获取订单详情失败')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 格式化价格
const formatPrice = (price: number | null | undefined) => {
  if (price == null) return '¥0.00'
  return `¥${price.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')}`
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

// 打开评价表单
const handleReview = (productId: number) => {
  if (!order.value) return
  currentReviewItem.value = {
    orderId: order.value.id,
    productId
  }
  showReviewForm.value = true
}

// 评价成功回调
const handleReviewSuccess = () => {
  ElMessage.success('评价成功')
  // 重新加载订单详情
  fetchOrderDetail()
}

// 判断商品是否可以评价
const canReview = (item: OrderInfo['items'][0]) => {
  if (!order.value) return false
  console.log('订单状态:', order.value.status)
  console.log('商品评价状态:', item.hasReviewed)
  // 只有已完成的订单可以评价
  return order.value.status === 3 && !item.hasReviewed
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

    <div v-loading="loading">
      <el-empty v-if="!order" description="订单不存在" />
      
      <template v-else>
        <el-card class="order-info">
          <template #header>
            <div class="section-header">
              <span class="section-title">订单信息</span>
              <el-tag :type="order.status === 4 ? 'info' : order.status === 0 ? 'warning' : 'success'">
                {{ getStatusText(order.status) }}
              </el-tag>
            </div>
          </template>
          
          <div class="info-grid">
            <div class="info-item">
              <span class="label">订单编号：</span>
              <span class="value">{{ order.id }}</span>
            </div>
            <div class="info-item">
              <span class="label">创建时间：</span>
              <span class="value">{{ order.createTime }}</span>
            </div>
            <div class="info-item">
              <span class="label">备注：</span>
              <span class="value">{{ order.remark || '无' }}</span>
            </div>
          </div>
        </el-card>

        <el-card class="delivery-info">
          <template #header>
            <div class="section-header">
              <span class="section-title">收货信息</span>
            </div>
          </template>
          
          <div class="info-grid">
            <div class="info-item">
              <span class="label">收货人：</span>
              <span class="value">{{ order.receiverName }}</span>
            </div>
            <div class="info-item">
              <span class="label">联系电话：</span>
              <span class="value">{{ order.receiverPhone }}</span>
            </div>
            <div class="info-item">
              <span class="label">收货地址：</span>
              <span class="value">{{ order.address }}</span>
            </div>
          </div>
        </el-card>

        <el-card class="products-info">
          <template #header>
            <div class="section-header">
              <span class="section-title">商品信息</span>
            </div>
          </template>
          
          <div class="products-list">
            <div v-for="item in order.items" :key="item.id" class="product-item">
              <el-image 
                :src="item.productImage || '/default-product.jpg'" 
                fit="cover"
                class="product-image"
              />
              <div class="product-details">
                <h3 class="product-name">{{ item.productName }}</h3>
                <div class="product-price">
                  <span>{{ formatPrice(item.productPrice) }} × {{ item.quantity }}</span>
                  <span class="subtotal">小计：{{ formatPrice(item.subtotal) }}</span>
                </div>
                <div class="product-actions">
                  <el-button
                    v-if="canReview(item)"
                    type="primary"
                    size="small"
                    @click="handleReview(item.productId)"
                  >
                    评价商品
                  </el-button>
                  <el-tag v-else-if="item.hasReviewed" type="info" size="small">
                    已评价
                  </el-tag>
                  <el-tag v-else type="warning" size="small">
                    {{ order.status === 3 ? '未评价' : '待完成' }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>

          <div class="order-amount">
            <div class="amount-item">
              <span>商品总额：</span>
              <span>{{ formatPrice(order.totalAmount) }}</span>
            </div>
            <div v-if="order.discountAmount > 0" class="amount-item discount">
              <span>优惠金额：</span>
              <span>-{{ formatPrice(order.discountAmount) }}</span>
            </div>
            <div class="amount-item final">
              <span>实付金额：</span>
              <span>{{ formatPrice(order.payAmount) }}</span>
            </div>
          </div>
        </el-card>
      </template>
    </div>

    <!-- 评价表单 -->
    <ReviewForm
      v-if="currentReviewItem"
      v-model="showReviewForm"
      :order-id="currentReviewItem.orderId"
      :product-id="currentReviewItem.productId"
      @success="handleReviewSuccess"
    />
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

.order-info,
.delivery-info,
.products-info {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
}

.label {
  color: #606266;
  margin-right: 10px;
  min-width: 80px;
}

.products-list {
  margin-bottom: 20px;
}

.product-item {
  display: flex;
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-image {
  width: 100px;
  height: 100px;
  margin-right: 20px;
  border-radius: 4px;
}

.product-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.product-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #606266;
}

.subtotal {
  color: #303133;
  font-weight: bold;
}

.product-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

.order-amount {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
  margin-top: 20px;
}

.amount-item {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
  color: #606266;
}

.amount-item.discount {
  color: #f56c6c;
}

.amount-item.final {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.amount-item span:last-child {
  margin-left: 20px;
  min-width: 100px;
  text-align: right;
}
</style> 