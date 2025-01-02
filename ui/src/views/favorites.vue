<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavorites, removeFavorite } from '@/api/favorite'
import type { FavoriteItem, GetFavoritesRes, AddToFavoriteRes } from '@/api/favorite'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, ShoppingCart, Picture } from '@element-plus/icons-vue'
import { addToCart } from '@/api/cart'
import type { AddToCartParams, CartResponse } from '@/api/cart'

const router = useRouter()
const favorites = ref<FavoriteItem[]>([])
const loading = ref(true)
const loadingItem = ref<number | null>(null)

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
    
    loadingItem.value = item.id
    const response = await removeFavorite(item.id)
    if (response.data) {
      const res = response.data as AddToFavoriteRes
      if (res.code === 200) {
        favorites.value = favorites.value.filter(i => i.id !== item.id)
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error(res.message || '取消收藏失败')
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消收藏失败')
    }
  } finally {
    loadingItem.value = null
  }
}

// 添加到购物车
const handleAddToCart = async (item: FavoriteItem) => {
  if (item.product.stock <= 0) {
    ElMessage.warning('商品库存不足')
    return
  }

  try {
    loadingItem.value = item.id
    const params: AddToCartParams = {
      productId: item.productId,
      quantity: 1,
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
  } catch (error: any) {
    ElMessage.error(error?.message || '添加到购物车失败')
  } finally {
    loadingItem.value = null
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
    <div class="page-header">
      <div class="header">
        <div class="title-wrapper">
          <span class="title">我的收藏</span>
          <span class="count">共 {{ favorites.length }} 件商品</span>
        </div>
      </div>
    </div>

    <div class="favorites-content">
      <el-empty 
        v-if="!loading && favorites.length === 0" 
        description="暂无收藏商品" 
      >
        <template #extra>
          <el-button type="primary" @click.passive="router.push('/products')">
            去逛逛
          </el-button>
        </template>
      </el-empty>
      
      <el-row v-else :gutter="20" v-loading="loading">
        <el-col 
          :xs="24" 
          :sm="12" 
          :md="8" 
          :lg="6" 
          v-for="item in favorites" 
          :key="item.id"
        >
          <el-card class="favorite-card" shadow="hover">
            <div class="product-content">
              <div class="product-main" @click.passive="handleViewDetail(item)">
                <el-image 
                  :src="item.product.imageUrl || '/default-product.jpg'" 
                  :alt="item.product.name"
                  class="product-image"
                  fit="cover"
                >
                  <template #error>
                    <div class="image-slot">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="product-info">
                  <div class="product-name">{{ item.product.name }}</div>
                  <div class="product-description">{{ item.product.description }}</div>
                </div>
              </div>
              <div class="product-footer">
                <div class="price-stock">
                  <span class="price">{{ formatPrice(item.product.price) }}</span>
                  <el-tag 
                    :type="item.product.stock > 0 ? 'success' : 'error'" 
                    size="small"
                    effect="plain"
                  >
                    {{ item.product.stock > 0 ? '有货' : '无货' }}
                  </el-tag>
                </div>
                <div class="product-actions">
                  <el-button 
                    circle
                    type="danger"
                    @click.stop.passive="handleRemove(item)"
                    :loading="loadingItem === item.id"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                  <el-button 
                    circle
                    type="primary" 
                    @click.stop.passive="handleAddToCart(item)"
                    :loading="loadingItem === item.id"
                    :disabled="item.product.stock <= 0"
                  >
                    <el-icon><ShoppingCart /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.favorites-container {
  padding: var(--el-main-padding);
  background-color: var(--el-bg-color-page);
  min-height: calc(100vh - var(--el-header-height));
}

.page-header {
  margin-bottom: var(--el-main-padding);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title {
  font-size: var(--el-font-size-large);
  font-weight: var(--el-font-weight-bold);
  color: var(--el-text-color-primary);
}

.count {
  font-size: var(--el-font-size-base);
  color: var(--el-text-color-secondary);
}

.favorites-content {
  min-height: 300px;
}

.el-row {
  margin-bottom: -16px;
  margin-right: -16px;
}

.el-col {
  padding-bottom: 16px;
  padding-right: 16px;
}

.favorite-card {
  border: 1px solid var(--el-border-color-lighter);
  border-radius: var(--el-border-radius-base);
  height: 100%;
}

.favorite-card :deep(.el-card__body) {
  padding: 0;
  height: 100%;
}

.product-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.product-main {
  flex: 1;
  cursor: pointer;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: var(--el-font-size-extra-large);
}

.product-info {
  padding: var(--el-card-padding);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.product-name {
  font-size: var(--el-font-size-base);
  font-weight: var(--el-font-weight-bold);
  color: var(--el-text-color-primary);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5;
}

.product-description {
  font-size: var(--el-font-size-small);
  color: var(--el-text-color-secondary);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5;
}

.product-footer {
  padding: var(--el-card-padding);
  border-top: 1px solid var(--el-border-color-lighter);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--el-fill-color-light);
}

.price-stock {
  display: flex;
  align-items: center;
  gap: 8px;
}

.price {
  font-size: var(--el-font-size-large);
  font-weight: var(--el-font-weight-bold);
  color: var(--el-color-danger);
}

.product-actions {
  display: flex;
  gap: 8px;
}

.product-actions :deep(.el-button) {
  --el-button-size: 32px;
  --el-button-padding-horizontal: 0;
}

.product-actions :deep(.el-button .el-icon) {
  font-size: var(--el-font-size-base);
}
</style> 