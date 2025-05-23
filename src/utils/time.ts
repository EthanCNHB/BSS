export const getTime = () => {
  let message = ''
  const hours = new Date().getHours()
  if (hours < 12 && hours >= 5) {
    message = '早上'
  } else if (hours < 18) {
    message = '下午'
  } else {
    message = '晚上'
  }
  return message
}
// utils/time.ts
import dayjs from 'dayjs'

export function formatTime(value: string | Date, pattern = 'YYYY-MM-DD HH:mm:ss') {
  return value ? dayjs(value).format(pattern) : ''
}
