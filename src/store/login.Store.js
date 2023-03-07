import { makeAutoObservable } from "mobx"
import { http, getToken, setToken, removeToken } from '@/utils'

class LoginStroe {
 token = getToken() || ''   //能取到就拿 不行就生成去把
 constructor() {
  makeAutoObservable(this)
 }

 getToken = async ({ username, password }) => {
  const res = await http.post('login', {
   username,
   password
  })
  this.token = res.data
  setToken(this.token)
 }

 loginOut = () => {
  this.token = ''
  removeToken()
 }

}
export default LoginStroe