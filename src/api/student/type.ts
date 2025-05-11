//登录接口需要携带参数ts类型
export interface loginForm {
  username: string
  password: string
}

interface dataType {
  token: string
}
//登录接口返回数据类型
export interface loginResponseData {
  code: number
  data: dataType
  message: string
}

export interface student {
  userId: number
  username: string
  majorId: number
  collegeId: number
  studentName: string
}

interface checkStudnet {
  checkUser: student
}
//定义服务器返回用户信息相关的数据类型
export interface studentResponseData {
  code: number
  data: checkStudnet
}
