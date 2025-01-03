<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getProducts, getProductCategories, getProductsByCategory } from '@/api/product'
import { addToCart } from '@/api/cart'
import { addToFavorite } from '@/api/favorite'
import type { Product } from '@/api/product'
import type { AddToCartParams, CartResponse } from '@/api/cart'
import type { Result, ProductCategory } from '@/types/api'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Star, PictureFilled } from '@element-plus/icons-vue'

const router = useRouter()
const products = ref<Product[]>([])
const categories = ref<ProductCategory[]>([])
const selectedCategory = ref<number | null>(null)
const loading = ref(true)

const fetchCategories = async () => {
  try {
    const response = await getProductCategories()
    const res = response.data as Result<ProductCategory[]>
    if (res.code === 200) {
      categories.value = res.data
    } else {
      ElMessage.error(res.message || '获取商品分类失败')
    }
  } catch (error) {
    ElMessage.error('获取商品分类失败')
  }
}

const filteredProducts = computed(() => {
  if (!selectedCategory.value) {
    return products.value
  }
  return products.value.filter(product => product.categoryId === selectedCategory.value)
})

const fetchProducts = async () => {
  try {
    const response = await getProducts()
    const res = response.data as Result<Product[]>
    if (res.code === 200) {
      products.value = res.data
    } else {
      ElMessage.error(res.message || '获取商品列表失败')
    }
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

const handleCategoryChange = async (categoryId: number | null) => {
  loading.value = true
  selectedCategory.value = categoryId
  try {
    if (categoryId === null) {
      await fetchProducts()
    } else {
      const response = await getProductsByCategory(categoryId)
      const res = response.data as Result<Product[]>
      if (res.code === 200) {
        products.value = res.data
      } else {
        ElMessage.error(res.message || '获取商品列表失败')
      }
    }
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 添加到购物车
const handleAddToCart = async (product: Product) => {
  try {
    const params: AddToCartParams = {
      productId: product.id,
      quantity: 1
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
const handleAddToFavorite = async (product: Product) => {
  // 检查用户是否登录
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    const response = await addToFavorite(product.id)
    const res = response.data
    if (res.code === 200) {
      ElMessage.success('收藏成功')
    }
  } catch (error: any) {
    // 错误已在请求拦截器中处理
  }
}

// 查看商品详情
const handleViewDetail = (product: Product) => {
  router.push(`/product/${product.id}`)
}

onMounted(() => {
  fetchProducts()
  fetchCategories()
})
</script>

<template>
  <div class="product-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">商品列表</span>
          <div class="category-filter">
            <el-radio-group v-model="selectedCategory" @change="handleCategoryChange">
              <el-radio-button :label="null">全部</el-radio-button>
              <el-radio-button 
                v-for="category in categories" 
                :key="category.id" 
                :label="category.id"
              >
                {{ category.name }}
              </el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>
    </el-card>

    <div class="product-list" v-loading="loading">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in filteredProducts" :key="product.id">
          <el-card class="product-card" :body-style="{ padding: '0px' }">
            <div class="product-content">
              <div class="product-main" @click="handleViewDetail(product)">
                <el-image 
                  :src="product.imageUrl || '/default-product.jpg'" 
                  :alt="product.name"
                  class="product-image"
                  fit="cover"
                >
                  <template #error>
                    <div class="image-slot">
                      <el-icon><picture-filled /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="product-info">
                  <el-text class="product-name" truncated>{{ product.name }}</el-text>
                  <el-text class="product-description" type="info">{{ product.description }}</el-text>
                  <div class="product-meta">
                    <el-tag size="small" type="info">{{ product.brand }}</el-tag>
                    <el-tag size="small" type="info">{{ product.model }}</el-tag>
                  </div>
                  <div class="product-footer">
                    <el-text class="price" type="danger" size="large">¥{{ product.price.toLocaleString() }}</el-text>
                    <el-text class="stock" type="info">库存: {{ product.stock }}</el-text>
                  </div>
                </div>
              </div>
              <div class="product-actions">
                <el-button 
                  type="warning"
                  :icon="Star"
                  circle
                  @click="handleAddToFavorite(product)"
                />
                <el-button 
                  type="primary" 
                  :icon="ShoppingCart"
                  circle
                  @click="handleAddToCart(product)"
                  :disabled="product.stock <= 0"
                />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.product-container {
  padding: var(--el-main-padding);
}

.page-header {
  margin-bottom: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.title {
  font-size: var(--el-font-size-extra-large);
  font-weight: bold;
}

.product-list {
  margin-top: 20px;
}

.product-card {
  margin-bottom: 20px;
  height: 400px;
  position: relative;
}

.product-image {
  width: 100%;
  height: 200px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}

.product-info {
  padding: var(--el-card-padding);
  height: 200px;
  display: flex;
  flex-direction: column;
}

.product-name {
  display: block;
  font-size: var(--el-font-size-large);
  font-weight: bold;
  margin-bottom: 8px;
}

.product-description {
  display: block;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}

.product-meta {
  margin-bottom: 8px;
  display: flex;
  gap: 8px;
}

.product-content {
  height: 100%;
}

.product-main {
  cursor: pointer;
  height: 100%;
}

.product-actions {
  position: absolute;
  right: 14px;
  bottom: 14px;
  display: flex;
  gap: 8px;
  z-index: 1;
}

.product-actions .el-button {
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: var(--el-font-size-extra-large);
  font-weight: bold;
}

.stock {
  font-size: var(--el-font-size-small);
}

.category-filter {
  display: flex;
  align-items: center;
  gap: 8px;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .category-filter {
    width: 100%;
    overflow-x: auto;
    padding-bottom: 8px;
  }
}
</style> 