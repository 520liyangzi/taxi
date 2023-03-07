import axios from 'axios'
import getToken from 'axios'
import { history } from './history'
const http = axios.create({
 baseURL: 'http://localhost:8080/admin',
 timeout: 5000
})
// 添加请求拦截器
http.interceptors.request.use((config) => {
 if (getToken()) {
  // config.headers.Authorization = `Bearer ${getToken()}`  //b6926a14-ddb1-4973-addd-b7e3adabbd23
  config.headers.Authorization = `Bearer b6926a14-ddb1-4973-addd-b7e3adabbd23`
 }
 return config
}, (error) => {
 return Promise.reject(error)
})

// 添加响应拦截器
http.interceptors.response.use((response) => {
 // 2xx 范围内的状态码都会触发该函数。
 // 对响应数据做点什么
 return response.data
}, (error) => {
 // 超出 2xx 范围的状态码都会触发该函数。
 // 对响应错误做点什么
 console.dir(error)
 if (error.response.status === 401) {
  console.dir(' token 失效')
  //回到登陆  reactRouter默认状态下不支持在组建之外完成路由跳转
  history.push("/login")
 }
 return Promise.reject(error)
})

export { http }