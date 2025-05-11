import { GET_TOKEN } from './../../utils/token'
//统一管理咱们的项目学生相关的接口
import request from '@/utils/request'
import { student } from './type'
//项目学生相关的请求地址
enum API {
  LOGIN_URL = '/student/login',
  STDINFO_URL = '/student/studentInfo',
  LOGOUT_URL = '',
}

//登录接口
export const reqLogin = (data: any) => request.post<any, any>(API.LOGIN_URL, data)

//获取用户信息
export const reqStudentInfo = () => request.get<any, any>(API.STDINFO_URL)

//退出登录
export const reqLogout = () => request.post<any, any>(API.LOGOUT_URL)
