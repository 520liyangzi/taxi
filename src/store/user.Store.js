import { makeAutoObservable } from "mobx"
import { http } from '@/utils'


class UserStore {
 userInfo = {}
 constructor() {
  makeAutoObservable(this)
 }
 getUserInfo = async () => {
  //接口获得数据
  const res = await http.get('/user/profile')
  this.userInfo = res.data
  console.log(res.data)
 }

}
export default UserStore