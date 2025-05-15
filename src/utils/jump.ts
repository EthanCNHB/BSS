// utils/jump.ts
export const jumpByRole = (router: any, role: string) => {
  switch (role) {
    case 'admin':
      router.push('/admin')
      break
    case 'teacher':
      router.push('/teacher')
      break
    case 'student':
      router.push('/student')
      break
    default:
      router.push('/404')
  }
}
