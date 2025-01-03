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
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">我的收藏</span>
          <span class="count">共 {{ favorites.length }} 件商品</span>
        </div>
      </template>
    </el-card>

    <div class="favorites-content">
      <el-empty 
        v-if="!loading && favorites.length === 0" 
        description="暂无收藏商品" 
      >
        <el-button type="primary" @click="router.push('/products')">
          去逛逛
        </el-button>
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
          <el-card class="favorite-card" :body-style="{ padding: '0px' }">
            <div class="product-content">
              <div class="product-main" @click="handleViewDetail(item)">
                <el-image 
                  :src="item.product.imageUrl || '/default-product.jpg'" 
                  :alt="item.product.name"
                  class="product-image"
                  fit="cover"
                  :preview-src-list="[item.product.imageUrl]"
                >
                  <template #error>
                    <div class="image-slot">
                      <el-icon :size="24"><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="product-info">
                  <h3 class="product-name">{{ item.product.name }}</h3>
                  <p class="product-description">{{ item.product.description }}</p>
                </div>
              </div>
              <div class="product-footer">
                <div class="price-stock">
                  <span class="price">{{ formatPrice(item.product.price) }}</span>
                  <el-tag 
                    :type="item.product.stock > 0 ? 'success' : 'error'" 
                    size="small"
                    effect="light"
                  >
                    {{ item.product.stock > 0 ? '有货' : '无货' }}
                  </el-tag>
                </div>
                <div class="product-actions">
                  <el-button 
                    circle
                    type="danger"
                    @click.stop="handleRemove(item)"
                    :loading="loadingItem === item.id"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                  <el-button 
                    circle
                    type="primary" 
                    @click.stop="handleAddToCart(item)"
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

<style scoped lang="scss">
.favorites-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
  background: #f5f7fa;

  .page-header {
    margin-bottom: 20px;

    .header {
      display: flex;
      align-items: center;
      gap: 12px;

      .title {
        font-size: 20px;
        font-weight: bold;
      }

      .count {
        color: var(--el-text-color-secondary);
      }
    }
  }

  .favorites-content {
    .favorite-card {
      height: 100%;
      transition: all 0.3s ease;
      border: none;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
      }

      .product-content {
        .product-main {
          cursor: pointer;

          .product-image {
            width: 100%;
            height: 200px;
            display: block;
            border-radius: 8px 8px 0 0;
            overflow: hidden;

            .image-slot {
              height: 100%;
              display: flex;
              justify-content: center;
              align-items: center;
              background: var(--el-fill-color-light);
              color: var(--el-text-color-secondary);
            }
          }

          .product-info {
            padding: 14px;

            .product-name {
              margin: 0 0 8px;
              font-size: 16px;
              font-weight: bold;
              color: var(--el-text-color-primary);
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .product-description {
              margin: 0;
              font-size: 14px;
              color: var(--el-text-color-secondary);
              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              line-height: 1.5;
              height: 42px;
            }
          }
        }

        .product-footer {
          padding: 14px;
          border-top: 1px solid var(--el-border-color-lighter);
          display: flex;
          justify-content: space-between;
          align-items: center;

          .price-stock {
            display: flex;
            align-items: center;
            gap: 8px;

            .price {
              color: var(--el-color-danger);
              font-size: 18px;
              font-weight: bold;
            }
          }

          .product-actions {
            display: flex;
            gap: 8px;
          }
        }
      }
    }
  }
}

.el-row {
  margin: 0 -10px;
}

.el-col {
  padding: 0 10px;
  margin-bottom: 20px;
}
</style> 