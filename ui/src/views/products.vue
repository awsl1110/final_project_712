<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getProducts } from '@/api/product'
import type { Product } from '@/api/product'
import type { Result } from '@/types/api'
import { ElMessage } from 'element-plus'

const products = ref<Product[]>([])
const loading = ref(true)

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

onMounted(() => {
  fetchProducts()
})
</script>

<template>
  <div class="product-container">
    <el-card class="page-header">
      <template #header>
        <div class="header">
          <span class="title">商品列表</span>
        </div>
      </template>
    </el-card>

    <div class="product-list" v-loading="loading">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="product in products" :key="product.id">
          <el-card class="product-card" :body-style="{ padding: '0px' }">
            <img :src="product.imageUrl || '/default-product.jpg'" class="product-image">
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-description">{{ product.description }}</p>
              <div class="product-meta">
                <span class="product-brand">{{ product.brand }}</span>
                <span class="product-model">{{ product.model }}</span>
              </div>
              <div class="product-price">
                <span class="price">¥{{ product.price.toLocaleString() }}</span>
                <span class="stock">库存: {{ product.stock }}</span>
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

.product-list {
  margin-top: 20px;
}

.product-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.product-card:hover {
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
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-description {
  margin: 8px 0;
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
  margin: 8px 0;
  font-size: 14px;
}

.product-brand, .product-model {
  color: #666;
  margin-right: 10px;
}

.product-price {
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

.stock {
  font-size: 14px;
  color: #666;
}
</style> 