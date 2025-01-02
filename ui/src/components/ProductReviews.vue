<template>
  <div class="product-reviews">
    <el-card class="review-card">
      <template #header>
        <div class="review-header">
          <span class="title">商品评价</span>
          <div class="rating-summary" v-if="reviews.length > 0">
            <el-rate
              v-model="averageRating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
            <span class="review-count">( {{ reviews.length }} 条评价 )</span>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>

      <div v-else-if="reviews.length === 0" class="empty-reviews">
        暂无评价
      </div>

      <div v-else class="review-list">
        <div v-for="review in reviews" :key="review.id" class="review-item">
          <div class="review-item-header">
            <el-rate v-model="review.rating" disabled />
            <span class="review-time">{{ formatDate(review.createTime) }}</span>
          </div>
          <div class="review-content">{{ review.content }}</div>
          <div v-if="review.images" class="review-images">
            <el-image
              v-for="(image, index) in review.images.split(',')"
              :key="index"
              :src="image"
              :preview-src-list="review.images.split(',')"
              fit="cover"
              class="review-image"
            />
          </div>
        </div>
      </div>

      <div class="pagination" v-if="totalPage > 1">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="totalRow"
          @current-change="handlePageChange"
          layout="prev, pager, next"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getProductReviews } from '@/api/review'
import type { ProductReview } from '@/types/api'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  productId: number
}>()

const loading = ref(true)
const reviews = ref<ProductReview[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const totalRow = ref(0)

const averageRating = computed(() => {
  if (reviews.value.length === 0) return 0
  const sum = reviews.value.reduce((acc, review) => acc + review.rating, 0)
  return Number((sum / reviews.value.length).toFixed(1))
})

const formatDate = (dateStr: string) => {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const fetchReviews = async () => {
  loading.value = true
  try {
    const response = await getProductReviews(props.productId)
    if (response.data.code === 200) {
      const { records, totalPage: total, totalRow: row } = response.data.data
      reviews.value = records
      totalPage.value = total
      totalRow.value = row
    }
  } catch (error: any) {
    if (error.message === '此商品无评价') {
      reviews.value = []
      totalPage.value = 0
      totalRow.value = 0
    } else {
      ElMessage.error('获取商品评价失败')
      console.error('获取商品评价失败:', error)
    }
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchReviews()
}

onMounted(() => {
  fetchReviews()
})
</script>

<style scoped>
.product-reviews {
  margin: 20px 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.rating-summary {
  display: flex;
  align-items: center;
  gap: 10px;
}

.review-count {
  color: #909399;
}

.loading-container {
  padding: 20px;
}

.empty-reviews {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 20px;
}

.review-item:last-child {
  border-bottom: none;
}

.review-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.review-time {
  color: #909399;
  font-size: 14px;
}

.review-content {
  margin: 10px 0;
  line-height: 1.5;
}

.review-images {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 10px;
}

.review-image {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  cursor: pointer;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 