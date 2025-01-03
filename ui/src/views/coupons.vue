<template>
  <div class="coupons-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <div class="title-section">
            <span class="title">优惠券中心</span>
            <span class="subtitle">领取优惠券，享受优惠价格</span>
          </div>
        </div>
      </template>
    </el-card>

    <div v-loading="loading">
      <el-empty v-if="coupons.length === 0" description="暂无可用优惠券" />
      
      <el-row :gutter="20" v-else>
        <el-col 
          v-for="coupon in coupons" 
          :key="coupon.id"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="8"
          class="mb-4"
        >
          <div class="coupon-card">
            <div class="coupon-content">
              <!-- 左侧金额部分 -->
              <div class="coupon-left">
                <div class="amount">
                  <span class="currency" v-if="coupon.type === 1">¥</span>
                  <span class="value">{{ coupon.value }}</span>
                  <span class="unit">{{ coupon.type === 1 ? '元' : '折' }}</span>
                </div>
                <div class="condition">
                  满{{ coupon.minAmount }}元可用
                </div>
              </div>
              
              <!-- 右侧信息部分 -->
              <div class="coupon-right">
                <h3 class="coupon-name">{{ coupon.name }}</h3>
                <div class="coupon-info">
                  <div class="info-item">
                    <span class="label">有效期</span>
                    <span class="text">{{ formatDate(coupon.startTime) }} 至 {{ formatDate(coupon.endTime) }}</span>
                  </div>
                  <div class="info-item">
                    <span class="label">剩余数量</span>
                    <span class="text">{{ coupon.remain }}张</span>
                  </div>
                </div>
                
                <el-button
                  type="danger"
                  :disabled="coupon.remain <= 0"
                  @click="handleReceiveCoupon(coupon)"
                  class="receive-btn"
                >
                  {{ coupon.remain <= 0 ? '已领完' : '立即领取' }}
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.coupons-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    .header {
      .title-section {
        display: flex;
        flex-direction: column;
        gap: 4px;
        
        .title {
          font-size: 20px;
          font-weight: bold;
          color: var(--el-text-color-primary);
        }
        
        .subtitle {
          font-size: 14px;
          color: var(--el-text-color-secondary);
        }
      }
    }
  }
}

.coupon-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 160px;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  }
  
  .coupon-content {
    display: flex;
    position: relative;
    height: 100%;
    
    &::after {
      content: '';
      position: absolute;
      left: 35%;
      top: 0;
      bottom: 0;
      border-left: 1px dashed #ddd;
    }
  }
  
  .coupon-left {
    width: 35%;
    background: var(--el-color-danger-light-9);
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
    
    .amount {
      color: var(--el-color-danger);
      text-align: center;
      
      .currency {
        font-size: 18px;
        font-weight: bold;
      }
      
      .value {
        font-size: 32px;
        font-weight: bold;
      }
      
      .unit {
        font-size: 14px;
        margin-left: 2px;
      }
    }
    
    .condition {
      font-size: 13px;
      color: var(--el-text-color-secondary);
      margin-top: 8px;
      text-align: center;
    }
  }
  
  .coupon-right {
    flex: 1;
    padding: 20px;
    display: flex;
    flex-direction: column;
    
    .coupon-name {
      margin: 0 0 12px;
      font-size: 16px;
      font-weight: bold;
      color: var(--el-text-color-primary);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    
    .coupon-info {
      flex: 1;
      
      .info-item {
        display: flex;
        flex-direction: column;
        margin-bottom: 8px;
        
        .label {
          font-size: 13px;
          color: var(--el-text-color-secondary);
          margin-bottom: 4px;
        }
        
        .text {
          font-size: 13px;
          color: var(--el-text-color-primary);
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
    
    .receive-btn {
      width: 100%;
      margin-top: 8px;
      height: 32px;
      font-size: 14px;
    }
  }
}
</style>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAllCoupons, receiveCoupon } from '@/api/coupon'
import type { Coupon } from '@/types/api'
import { ElMessage } from 'element-plus'
import { Ticket } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/date'

const coupons = ref<Coupon[]>([])
const loading = ref(true)

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