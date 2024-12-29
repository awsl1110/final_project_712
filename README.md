# final_project_712

#### 介绍
javaEE的期末项目
#### 软件架构
spring boot
### 文件目录说明
_712.final_project_712:
```
├── advice/      # 全局异常处理和响应处理的通知类
├── config/      # 配置类（Redis、MyBatis、Security等）
├── controller/  # REST API 控制器
├── Exception/   # 自定义异常类
├── interceptor/ # 拦截器
├── mapper/      # MyBatis 数据库访问接口
├── model/       # 实体类/数据模型
├── service/     # 业务逻辑层
└── util/        # 工具类
```

#### 参与贡献
1. fork本项目
2. 将项目克隆到本地
3. 创建新的分支，并切换到新分支
4. 在新分支上进行开发，并提交代码
5. 提交Pull Request到原项目的 test 分支并解决冲突
6. 若自动测试结果无异常则由维护者审核及合并
#### 部署ui
1. 下载node.js
2. 安装pnpm
```sh
npm install -g pnpm
```
开始开发
1. 开启后端服务热部署  
很简单，只需要用IDEA打开本项目。
2. 启动前端开发环境  
   进入ui目录，执行以下命令：
```sh
# 安装依赖
pnpm install
```

```sh
# 启动开发服务器
pnpm dev
```
3. 编写代码  
开始你的表演。
### 贡献者

<a href="https://github.com/awsl1110/final_project_712/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=awsl1110/final_project_712" />
</a>
