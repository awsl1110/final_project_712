<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductDetail } from '@/api/product'
import { addToCart } from '@/api/cart'
import { addToFavorite } from '@/api/favorite'
import type { Product } from '@/api/product'
import type { Result } from '@/types/api'
import type { AddToCartParams, CartResponse } from '@/api/cart'
import type { AddToFavoriteParams, AddToFavoriteRes } from '@/api/favorite'
import { ElMessage, ElInputNumber } from 'element-plus'
import { ShoppingCart, Star } from '@element-plus/icons-vue'
import ProductReviews from '@/components/ProductReviews.vue'

const route = useRoute()
const loading = ref(true)
const product = ref<Product | null>(null)
const quantity = ref(1)

// 获取商品详情
const fetchProductDetail = async () => {
  try {
    loading.value = true
    const productId = Number(route.params.id)
    const response = await getProductDetail(productId)
    const res = response.data as Result<Product>
    if (res.code === 200) {
      product.value = res.data
    } else {
      ElMessage.error(res.message || '获取商品详情失败')
    }
  } catch (error) {
    ElMessage.error('获取商品详情失败')
  } finally {
    loading.value = false
  }
}

// 添加到购物车
const handleAddToCart = async () => {
  if (!product.value) return
  try {
    const params: AddToCartParams = {
      productId: product.value.id!,
      quantity: quantity.value
    }
    const response = await addToCart(params)
    if (response.data) {
      const res = response.data as CartResponse
      if (res.code === 200) {
        ElMessage.success('添加到购物车成功')
      } else {
        ElMessage.error(res.message || '添加到购物车失败')
      }
    } else {
      ElMessage.error('添加到购物车失败')
    }
  } catch (error) {
    ElMessage.error('添加到购物车失败')
  }
}

// 添加到收藏
const handleAddToFavorite = async () => {
  if (!product.value) return
  try {
    const params: AddToFavoriteParams = {
      productId: product.value.id!
    }
    const response = await addToFavorite(product.value.id!, params)
    if (response.data) {
      const res = response.data as AddToFavoriteRes
      if (res.code === 200) {
        ElMessage.success('添加到收藏成功')
      } else {
        ElMessage.error(res.message || '添加到收藏失败')
      }
    } else {
      ElMessage.error('添加到收藏失败')
    }
  } catch (error) {
    ElMessage.error('添加到收藏失败')
  }
}

// 格式化价格
const formatPrice = (price: number | undefined | null) => {
  if (price == null) return '¥0.00'
  return price.toLocaleString('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  })
}

// 解析规格信息
const parseSpecifications = (specs: string | null) => {
  if (!specs) return {}
  try {
    return JSON.parse(specs)
  } catch {
    return {}
  }
}

onMounted(() => {
  fetchProductDetail()
})
</script>

<template>
  <div class="product-detail-container" v-loading="loading">
    <el-card v-if="product" class="product-detail">
      <div class="product-content">
        <div class="product-image">
          <img :src="product.imageUrl || '/default-product.jpg'" :alt="product.name">
        </div>
        
        <div class="product-info">
          <h1 class="product-name">{{ product.name }}</h1>
          
          <div class="product-meta">
            <span class="brand">品牌：{{ product.brand }}</span>
            <span class="model">型号：{{ product.model }}</span>
          </div>
          
          <div class="product-price">
            <span class="price">{{ formatPrice(product.price) }}</span>
            <span class="stock">库存：{{ product.stock }}件</span>
          </div>
          
          <div class="product-description">
            <h3>商品描述</h3>
            <p>{{ product.description }}</p>
          </div>
          
          <div class="product-specs" v-if="product.specifications">
            <h3>商品规格</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item 
                v-for="(value, key) in parseSpecifications(product.specifications)" 
                :key="key" 
                :label="key"
              >
                {{ value }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="product-actions">
            <div class="quantity-selector">
              <span class="label">数量：</span>
              <el-input-number 
                v-model="quantity" 
                :min="1" 
                :max="product.stock"
                :disabled="product.stock <= 0"
              />
            </div>
            
            <div class="action-buttons">
              <el-button
                type="warning"
                :icon="Star"
                @click="handleAddToFavorite"
              >
                收藏商品
              </el-button>
              <el-button
                type="primary"
                :icon="ShoppingCart"
                @click="handleAddToCart"
                :disabled="product.stock <= 0"
              >
                加入购物车
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>
    
    <el-empty v-else description="商品不存在" />

    <!-- 商品评价组件 -->
    <ProductReviews
      v-if="product"
      :product-id="Number(route.params.id)"
    />
  </div>
</template>

<style scoped>
.product-detail-container {
  padding: 20px;
}

.product-detail {
  background-color: #fff;
}

.product-content {
  display: flex;
  gap: 40px;
}

.product-image {
  flex: 0 0 400px;
}

.product-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 4px;
}

.product-info {
  flex: 1;
}

.product-name {
  margin: 0 0 20px;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.product-meta {
  margin-bottom: 20px;
  font-size: 14px;
  color: #666;
}

.product-meta span {
  margin-right: 20px;
}

.product-price {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.price {
  font-size: 28px;
  font-weight: bold;
  color: #f56c6c;
}

.stock {
  font-size: 14px;
  color: #666;
}

.product-description {
  margin-bottom: 20px;
}

.product-description h3 {
  font-size: 16px;
  margin-bottom: 10px;
}

.product-description p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.product-specs {
  margin-bottom: 20px;
}

.product-specs h3 {
  font-size: 16px;
  margin-bottom: 10px;
}

.product-actions {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.quantity-selector {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-selector .label {
  font-size: 14px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.action-buttons .el-button {
  flex: 1;
}

/* 添加评价组件样式 */
:deep(.product-reviews) {
  margin-top: 20px;
}
</style> 