import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import('@/views/password.vue'),
        meta: {
          title: '修改密码',
          requiresAuth: true
        }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('@/views/products.vue'),
        meta: {
          title: '商品列表',
          requiresAuth: true
        }
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('@/views/cart.vue'),
        meta: {
          title: '购物车',
          requiresAuth: true
        }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('@/views/orders.vue'),
        meta: {
          title: '我的订单',
          requiresAuth: true
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router 