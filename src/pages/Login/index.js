import { Button, Card, Form, Input, Checkbox, message } from 'antd'
import logo from '../../assets/logo.png'
import './index.scss'
import { useStore } from '../../store/index'
import { useNavigate } from 'react-router-dom'


function Login () {

 const { loginStore } = useStore()
 const navigate = useNavigate()

 async function onFinish (values) {
  console.log('Success:', values)
  await loginStore.getToken(
   {
    username: values.username,
    password: values.password
   }
  )
  navigate('/')
  message.success('success')

 }

 return (
  <div className='login'>
   <Card className='login-container'>

    <div className='Log'>
     LOGIN
    </div>

    <Form
     initialValues={{
      remember: true,
     }}
     validateTrigger={['onBlur', 'onChange']}
     onFinish={onFinish}
    >
     <Form.Item
      name="username"
      rules={[
       {
        required: true,
        message: 'Please input your phone!',
       }, {
        pattern: /^[a-zA-Z]{6}$/,
        message: 'Invalide username(Six alphabet)',
        validateTrigger: 'onBlur'
       }
      ]}>
      <Input size="large" placeholder="Please input your username" />
     </Form.Item>
     <Form.Item
      name="password"
      rules={[
       {
        required: true,
        message: 'Please input your password!',
       },
       { len: 6, message: 'Atleaste 6 digits', validateTrigger: 'onBlur' },
      ]}>
      <Input size="large" placeholder="Please input your password" />
     </Form.Item>

     <Form.Item>
      <Button className='button' type='primary' htmlType="submit" size="large" block>
       LOGIN
      </Button>
     </Form.Item>
    </Form>
   </Card>
  </div>
 )
}
export default Login