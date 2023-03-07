import './index.scss'
import React from 'react'
import { Upload, message } from 'antd'
import { PlusOutlined } from '@ant-design/icons'
import {
 Button,
 Cascader,
 Checkbox,
 DatePicker,
 Form,
 Input,
 InputNumber,
 Radio,
 Select,
 Switch,
 TreeSelect,
} from 'antd'
import { useState } from 'react'
import axios from 'axios'
import { http } from '@/utils'
import { useNavigate } from 'react-router-dom'
const { RangePicker } = DatePicker
const { TextArea } = Input
const EditNews = () => {

 const path = window.location.pathname
 const pathParts = path.split('/')
 const newsId = pathParts.pop()
 const navigate = useNavigate()
 const onFinish = async (values) => {
  const { title, description } = values
  await http.post('/news/edit', {
   id: newsId, description, title, media: fileList[0].response + ""
  })
  navigate('/websiteEditing/news')
 }

 const [fileList, setFileList] = useState()

 const handleChange = ({ fileList }) => {
  setFileList(fileList)
 }

 return (
  <div className='nine'>
   <div className='addPro'>
    Edit News
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

export default EditNews