/**
 * 格式化日期
 * @param dateStr 日期字符串或数组
 * @returns 格式化后的日期字符串 YYYY-MM-DD HH:mm
 */
export const formatDate = (dateStr: string | number[] | null | undefined) => {
  if (!dateStr) return ''
  
  try {
    // 如果输入是数组
    if (Array.isArray(dateStr)) {
      const [year, month, day, hour = 0, minute = 0] = dateStr
      return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
    }
    
    // 如果是字符串但是数组格式
    if (typeof dateStr === 'string' && dateStr.startsWith('[') && dateStr.endsWith(']')) {
      const numbers = dateStr.slice(1, -1).split(',').map(num => parseInt(num.trim()))
      if (numbers.length >= 3) {
        const [year, month, day, hour = 0, minute = 0] = numbers
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
      }
    }
    
    // 如果是普通日期字符串
    const date = new Date(dateStr)
    if (!isNaN(date.getTime())) {
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    }
    
    return String(dateStr)
  } catch (error) {
    console.error('日期格式化错误:', error)
    return String(dateStr)
  }
} 