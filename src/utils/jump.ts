// utils/jump.ts
export const jumpByRole = (router: any, role: string) => {
  switch (role) {
    case 'admin':
      router.push('/')
      break
    case 'teacher':
      router.push('/')
      break
    case 'student':
      router.push('/')
      break
    case 'collegeAdmin':
      router.push('/collge-management')
      break
    default:
      router.push('/404')
  }
}
