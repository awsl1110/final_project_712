# 后台管理系统API文档

## 接口说明

本文档提供了后台管理系统的所有API接口说明。

### 接口分组

1. 用户相关接口
   - 用户认证（登录、注册）
   - 个人信息管理
   - 地址管理
   - 头像管理
   - 验证码
   - 优惠券管理

2. 商品相关接口
   - 商品管理
   - 收藏管理
   - 订单管理
   - 评价管理
   - 购物车管理

### 认证说明

除了以下接口外，所有接口都需要在请求头中携带JWT token进行认证：
- 验证码接口 `/kaptcha/**`
- 用户登录 `/user/login`
- 用户注册 `/user/register`
- 邮箱验证码 `/email/captcha/send`
- 商品查询 `/product/**`
- 评论列表 `/review/list`

认证方式：在请求头中添加 `Authorization: Bearer {token}` 