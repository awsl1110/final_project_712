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
import { ShoppingCart, Star, PictureFilled, Loading } from '@element-plus/icons-vue'

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
  // 检查用户是否登录
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    return
  }

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
    router.push('/login')
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
  <div class="p-4">
    <el-card class="mb-4">
      <template #header>
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-4">
          <el-text class="text-xl font-bold">商品列表</el-text>
          <div class="d-flex align-items-center gap-2">
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

    <div v-loading="loading" class="mt-5">
      <el-empty v-if="filteredProducts.length === 0" description="暂无商品" />
      
      <el-row v-else :gutter="20">
        <el-col 
          v-for="product in filteredProducts" 
          :key="product.id" 
          :xs="24" 
          :sm="12" 
          :md="8" 
          :lg="6" 
          class="mb-5"
        >
          <el-card 
            shadow="hover" 
            :body-style="{ padding: 0 }"
            class="product-card"
          >
            <el-image 
              :src="product.imageUrl || '/default-product.jpg'" 
              :alt="product.name"
              class="w-100 product-image"
              fit="cover"
              :preview-src-list="product.imageUrl ? [product.imageUrl] : []"
              :initial-index="0"
              preview-teleported
              loading="lazy"
              @click="handleViewDetail(product)"
            >
              <template #placeholder>
                <div class="image-placeholder d-flex justify-content-center align-items-center">
                  <el-icon :size="24" class="is-loading"><Loading /></el-icon>
                </div>
              </template>
              <template #error>
                <div class="image-placeholder d-flex justify-content-center align-items-center flex-column gap-2">
                  <el-icon :size="30"><picture-filled /></el-icon>
                  <el-text class="text-secondary" size="small">图片加载失败</el-text>
                </div>
              </template>
            </el-image>

            <div class="p-4">
              <el-link 
                :underline="false"
                @click="handleViewDetail(product)"
              >
                <el-text class="mb-3 text-truncate d-block" style="font-size: 16px">
                  {{ product.name }}
                </el-text>
              </el-link>

              <el-text type="info" class="mb-3 text-truncate d-block">
                {{ product.description }}
              </el-text>

              <div class="mb-3">
                <el-tag 
                  v-if="product.brand" 
                  size="small" 
                  effect="plain"
                  class="me-2"
                >
                  {{ product.brand }}
                </el-tag>
                <el-tag 
                  v-if="product.model" 
                  size="small" 
                  effect="plain"
                >
                  {{ product.model }}
                </el-tag>
              </div>

              <div class="d-flex justify-content-between align-items-center mb-3">
                <el-text type="danger" class="fs-4">
                  ¥{{ product.price.toLocaleString() }}
                </el-text>
                <div class="d-flex gap-2">
                  <el-tooltip 
                    content="收藏" 
                    placement="top" 
                    :show-after="500"
                  >
                    <el-button 
                      type="warning" 
                      :icon="Star" 
                      circle 
                      plain
                      @click="handleAddToFavorite(product)"
                    />
                  </el-tooltip>
                  <el-tooltip 
                    :content="product.stock <= 0 ? '暂无库存' : '加入购物车'" 
                    placement="top" 
                    :show-after="500"
                  >
                    <el-button 
                      type="primary" 
                      :icon="ShoppingCart" 
                      circle
                      plain
                      :disabled="product.stock <= 0"
                      @click="handleAddToCart(product)"
                    />
                  </el-tooltip>
                </div>
              </div>

              <el-tag 
                :type="product.stock > 0 ? 'success' : 'danger'"
                size="small"
                effect="light"
              >
                {{ product.stock > 0 ? `库存: ${product.stock}` : '无货' }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.product-card :deep(.el-card__body) {
  height: 100%;
}

.product-image {
  height: 240px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-image:hover {
  opacity: 0.9;
}

.image-placeholder {
  width: 100%;
  height: 240px;
  background-color: var(--el-fill-color-light);
}

.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .category-filter {
    width: 100%;
    overflow-x: auto;
  }
  
  .product-image {
    height: 200px;
  }
  
  .image-placeholder {
    height: 200px;
  }
}
</style> 