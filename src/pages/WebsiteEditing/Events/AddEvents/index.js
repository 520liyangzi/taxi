import './index.scss'
import React from 'react'
import { Upload, message } from 'antd'
import { PlusOutlined } from '@ant-design/icons'
import {
 Button,
 Form,
 Input,
} from 'antd'
import { useState } from 'react'
import { http } from '@/utils'
import { useNavigate } from 'react-router-dom'
const { TextArea } = Input
const AddEvent = () => {

 const navigate = useNavigate()
 const onFinish = async (values) => {
  console.log(values)
  const { title, description } = values
  await http.post('/events/add', {
   title, description, media: fileList[0].response + ""
  })
  navigate('/websiteEditing/events')
 }

 const [fileList, setFileList] = useState()

 const handleChange = ({ fileList }) => {
  setFileList(fileList)
 }


 return (
  <div className='nine'>
   <div className='addPro'>
    Add Event
   </div>

   <div className='form'>
    <Form
     onFinish={onFinish}
     labelCol={{
      span: 5,
     }}
     wrapperCol={{
      span: 40,
     }}
     layout="horizontal"
     style={{
      maxWidth: 800
     }}
    >

     <Form.Item label="Title" name="title">
      <Input />
     </Form.Item>

     <Form.Item label="Description" name="description">
      <TextArea rows={5} />
     </Form.Item>

     <Form.Item label="Upload Cover" valuePropName="fileList">
      <Upload action="http://localhost:8080/admin/upload"
       name='image'
       listType="picture-card"
       fileList={fileList}
       onChange={handleChange}>
       <div>
        <PlusOutlined />
        <div
         style={{
          marginTop: 8,
         }}
        >
         Upload
        </div>
       </div>
      </Upload>
     </Form.Item>

     <Form.Item>
      <Button htmlType='submit' type='primary'>SAVE</Button>
     </Form.Item>
    </Form>
   </div>



  </div>
 )

}

export default AddEvent