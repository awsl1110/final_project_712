<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAllCoupons, receiveCoupon } from '@/api/coupon'
import type { Coupon } from '@/types/api'
import { ElMessage } from 'element-plus'
import { Ticket } from '@element-plus/icons-vue'

const coupons = ref<Coupon[]>([])
const loading = ref(true)

// 格式化日期
const formatDate = (date: string | Date | null) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

// 获取优惠券列表
const fetchCoupons = async () => {
  try {
    loading.value = true
    const response = await getAllCoupons()
    if (response.data.code === 200) {
      coupons.value = response.data.data || []
    } else {
      ElMessage.error(response.data.message || '获取优惠券列表失败')
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')
    } else if (error.response?.data?.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('获取优惠券列表失败')
    }
  } finally {
    loading.value = false
  }
}

// 领取优惠券
const handleReceiveCoupon = async (coupon: Coupon) => {
  // 检查用户是否登录
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }

  if (coupon.remain <= 0) {
    ElMessage.warning('优惠券已被领完')
    return
  }

  try {
    const response = await receiveCoupon(coupon.id)
    if (response.data.code === 200) {
      ElMessage.success(response.data.message || '领取成功')
      // 重新获取优惠券列表
      fetchCoupons()
    } else {
      ElMessage.error(response.data.message || '领取失败')
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')
    } else if (error.response?.data?.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('领取失败，请稍后重试')
    }
  }
}

onMounted(() => {
  fetchCoupons()
})
</script>

<template>
  <div class="coupons-container">
    <div class="page-header">
      <el-card>
        <div class="header">
          <div class="title-wrapper">
            <span class="title">优惠券中心</span>
            <span class="subtitle">领取优惠券，享受优惠价格</span>
          </div>
        </div>
      </el-card>
    </div>

    <div class="coupons-content" v-loading="loading">
      <el-empty 
        v-if="!loading && coupons.length === 0" 
        description="暂无可领取的优惠券"
      >
        <template #image>
          <el-icon :size="60">
            <Ticket />
          </el-icon>
        </template>
      </el-empty>

      <div v-else class="coupon-list">
        <el-card 
          v-for="coupon in coupons" 
          :key="coupon.id"
          class="coupon-card"
          :class="{ 'coupon-disabled': coupon.remain <= 0 || coupon.status === 0 }"
        >
          <div class="coupon-content">
            <div class="coupon-value">
              <template v-if="coupon.type === 1">
                <span class="amount">¥{{ coupon.value }}</span>
              </template>
              <template v-else>
                <span class="discount">{{ coupon.value }}折</span>
              </template>
            </div>
            <div class="coupon-info">
              <h3 class="coupon-name">{{ coupon.name }}</h3>
              <p class="coupon-condition">满{{ coupon.minAmount }}元可用</p>
              <p class="coupon-time">
                {{ formatDate(coupon.startTime) }} 至 {{ formatDate(coupon.endTime) }}
              </p>
              <p class="coupon-remain">剩余 {{ coupon.remain }} 张</p>
            </div>
            <div class="coupon-action">
              <el-button 
                type="primary" 
                :disabled="coupon.remain <= 0 || coupon.status === 0"
                @click="handleReceiveCoupon(coupon)"
              >
                {{ coupon.status === 0 ? '已停用' : (coupon.remain > 0 ? '立即领取' : '已领完') }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.coupons-container {
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

.title-wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.subtitle {
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.coupon-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.coupon-card {
  position: relative;
  transition: all 0.3s;
}

.coupon-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.coupon-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
}

.coupon-value {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 100px;
}

.amount, .discount {
  font-size: 28px;
  font-weight: bold;
  color: var(--el-color-danger);
}

.coupon-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.coupon-name {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.coupon-condition,
.coupon-time,
.coupon-remain {
  margin: 0;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.coupon-action {
  display: flex;
  align-items: center;
}

.coupon-disabled {
  opacity: 0.7;
}

.coupon-disabled::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.6);
  pointer-events: none;
}
</style> 