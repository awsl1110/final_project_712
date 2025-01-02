<template>
  <el-dialog
    v-model="dialogVisible"
    title="发表评价"
    width="600px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="review-form"
    >
      <el-form-item label="评分" prop="rating">
        <el-rate
          v-model="form.rating"
          :texts="['很差', '较差', '一般', '不错', '很好']"
          show-text
        />
      </el-form-item>

      <el-form-item label="评价内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="4"
          placeholder="请输入您的评价内容"
        />
      </el-form-item>

      <el-form-item label="上传图片">
        <el-upload
          v-model:file-list="fileList"
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
        >
          <el-icon><Plus /></el-icon>
          <template #tip>
            <div class="upload-tip">
              支持jpg/png文件，最多可上传6张图片
            </div>
          </template>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          提交评价
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import type { FormInstance, UploadUserFile } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { addProductReview } from '@/api/review'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  modelValue: boolean
  orderId: number
  productId: number
}>()

const emit = defineEmits<{
  (e: 'success'): void
  (e: 'update:modelValue', value: boolean): void
}>()

const dialogVisible = ref(props.modelValue)
const submitting = ref(false)
const formRef = ref<FormInstance>()
const fileList = ref<UploadUserFile[]>([])

const form = reactive({
  rating: 5,
  content: ''
})

const rules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 5, max: 500, message: '评价内容长度应在5-500个字符之间', trigger: 'blur' }
  ]
}

const handleFileChange = (uploadFile: UploadUserFile) => {
  if (fileList.value.length > 6) {
    ElMessage.warning('最多只能上传6张图片')
    fileList.value = fileList.value.slice(0, 6)
    return
  }
  
  const isImage = uploadFile.raw?.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    const index = fileList.value.indexOf(uploadFile)
    if (index !== -1) {
      fileList.value.splice(index, 1)
    }
    return
  }

  const isLt5M = uploadFile.size! / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB')
    const index = fileList.value.indexOf(uploadFile)
    if (index !== -1) {
      fileList.value.splice(index, 1)
    }
  }
}

const handleFileRemove = (uploadFile: UploadUserFile) => {
  const index = fileList.value.indexOf(uploadFile)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const response = await addProductReview(props.orderId, props.productId, {
          rating: form.rating,
          content: form.content,
          files: fileList.value.map(file => file.raw!),
        })
        
        if (response.data.code === 200) {
          ElMessage.success('评价提交成功')
          dialogVisible.value = false
          emit('success')
          // 重置表单
          form.rating = 5
          form.content = ''
          fileList.value = []
        } else {
          ElMessage.error(response.data.message || '评价提交失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '评价提交失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 监听对话框可见性
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})
</script>

<style scoped>
.review-form {
  padding: 20px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

:deep(.el-rate__text) {
  font-size: 14px;
}

:deep(.el-upload--picture-card) {
  --el-upload-picture-card-size: 100px;
}
</style> 