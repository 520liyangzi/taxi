
import React from 'react'
import './index.scss'
import { Button, Form, Input, } from 'antd'
import { http } from '@/utils'
const { TextArea } = Input
class AboutUs extends React.Component {


 onFinish = async (values) => {
  const { description } = values
  await http.post('/aboutUs', { description })
 }

 render () {
  return (
   <div>
    <div className='eeii'>
     <div className='about_text'>
      About Us
     </div>
     <div className="form">
      <Form onFinish={this.onFinish}>
       <Form.Item label="Description" name="description">
        <TextArea rows={10} />
       </Form.Item>
       <Form.Item>
        <div className='submit'>
         <Button htmlType='submit' type='primary'>SAVE</Button>
        </div>
       </Form.Item>
      </Form>
     </div>
    </div>

   </div>
  )
 }
}

export default AboutUs