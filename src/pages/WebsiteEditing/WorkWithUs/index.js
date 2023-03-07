
import React from 'react'
import './index.scss'
import { Button, Form, Input, } from 'antd'
import { http } from '@/utils'
const { TextArea } = Input
class WorkWithUS extends React.Component {

 onFinish = async (values) => {
  const { description1, description2 } = values
  console.log(values)
  await http.post('/editWorkWithUs', { description1, description2 })
 }

 render () {
  return (
   <div>
    <div className='eeii'>
     <div className='about_text'>
      Work with Us
     </div>
     <div className="form">
      <Form onFinish={this.onFinish}  >
       <Form.Item label="Description2" name="description1">
        <TextArea rows={5} />
       </Form.Item>
       <Form.Item label="Description1" name="description2">
        <TextArea rows={5} />
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

export default WorkWithUS