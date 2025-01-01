<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavorites, removeFavorite } from '@/api/favorite'
import type { FavoriteItem, GetFavoritesRes, AddToFavoriteRes } from '@/api/favorite'
import type { Result } from '@/types/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, ShoppingCart } from '@element-plus/icons-vue'
import { addToCart } from '@/api/cart'
import type { AddToCartParams, AddToCartRes } from '@/api/cart'

const router = useRouter()
const favorites = ref<FavoriteItem[]>([])
const loading = ref(true)

// 获取收藏列表
const fetchFavorites = async () => {
  try {
    loading.value = true
    const response = await getFavorites()
    if (response.data) {
      const res = response.data as unknown as GetFavoritesRes
      if (res.code === 200) {
        favorites.value = res.data
      } else {
        ElMessage.error(res.message || '获取收藏列表失败')
      }
    }
  } catch (error) {
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 取消收藏
const handleRemove = async (item: FavoriteItem) => {
  try {
    await ElMessageBox.confirm('确定要取消收藏这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await removeFavorite(item.id)
    if (response.data) {
      const res = response.data as AddToFavoriteRes
      if (res.code === 200) {
        favorites.value = favorites.value.filter(i => i.id !== item.id)
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error(res.message || '取消收藏失败')
      }
    } else {
      ElMessage.error('取消收藏失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消收藏失败')
    }
  }
}

// 添加到购物车
const handleAddToCart = async (item: FavoriteItem) => {
  try {
    const params: AddToCartParams = {}
    const response = await addToCart(item.productId, 1, params)
    if (response.data) {
      const res = response.data as AddToCartRes
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

// 格式化价格
const formatPrice = (price: number | undefined | null) => {
  if (price == null) return '¥0.00'
  return price.toLocaleString('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  })
}

// 查看商品详情
const handleViewDetail = (item: FavoriteItem) => {
  router.push(`/product/${item.productId}`)
}

onMounted(() => {
  fetchFavorites()
})
</script>

<template>
  <div class="favorites-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">我的收藏</span>
        </div>
      </template>
    </el-card>

    <div class="favorites-content" v-loading="loading">
      <el-empty v-if="favorites.length === 0" description="暂无收藏" />
      
      <template v-else>
        <el-row :gutter="20">
          <el-col 
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="6" 
            v-for="item in favorites" 
            :key="item.id"
          >
            <el-card class="favorite-card" :body-style="{ padding: '0px' }">
              <div class="product-content" @click="handleViewDetail(item)">
                <img 
                  :src="item.product.imageUrl || '/default-product.jpg'" 
                  :alt="item.product.name"
                  class="product-image"
                >
                <div class="product-info">
                  <h3 class="product-name">{{ item.product.name }}</h3>
                  <p class="product-description">{{ item.product.description }}</p>
                  <div class="product-meta">
                    <span class="product-brand">{{ item.product.brand }}</span>
                    <span class="product-model">{{ item.product.model }}</span>
                  </div>
                  <div class="product-footer">
                    <span class="price">{{ formatPrice(item.product.price) }}</span>
                  </div>
                </div>
              </div>
              <div class="product-actions">
                <el-button
                  type="primary"
                  :icon="ShoppingCart"
                  circle
                  @click.stop="handleAddToCart(item)"
                  :disabled="item.product.stock <= 0"
                />
                <el-button
                  type="danger"
                  :icon="Delete"
                  circle
                  @click.stop="handleRemove(item)"
                />
              </div>
            </el-card>
          </el-col>
        </el-row>
      </template>
    </div>
  </div>
</template>

<style scoped>
.favorites-container {
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

.favorites-content {
  min-height: 200px;
}

.favorite-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.favorite-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 14px;
}

.product-name {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-description {
  margin: 0 0 8px;
  font-size: 14px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 40px;
}

.product-meta {
  margin-bottom: 8px;
  font-size: 14px;
}

.product-brand, .product-model {
  color: #666;
  margin-right: 10px;
}

.product-content {
  cursor: pointer;
  position: relative;
  padding-bottom: 50px;
}

.product-actions {
  position: absolute;
  right: 14px;
  bottom: 14px;
  display: flex;
  gap: 8px;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 4px;
  border-radius: 4px;
  z-index: 1;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}
</style> 