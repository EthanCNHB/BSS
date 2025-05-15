// 设置角色
export const SET_ROLE = (role: string) => {
  localStorage.setItem('ROLE', role)
}

// 获取角色
export const GET_ROLE = (): string | null => {
  return localStorage.getItem('ROLE')
}

// 删除角色
export const REMOVE_ROLE = () => {
  localStorage.removeItem('ROLE')
}
