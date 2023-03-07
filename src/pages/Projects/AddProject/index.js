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
const AddProject = () => {

 const navigate = useNavigate()
 const onFinish = async (values) => {
  console.log(values)
  const { category, description, link, partner, title, users, year } = values
  await http.post('/project/add', {
   category, description, link, partner, title, users, year, media: fileList[0].response + ""
  })
  navigate('/projects')
 }

 const [fileList, setFileList] = useState()

 const handleChange = ({ fileList }) => {
  setFileList(fileList)
 }


 return (
  <div className='nine'>
   <div className='addPro'>
    Add Project
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

     <Form.Item label="Category" name="category">
      <Select>
       <Select.Option value="demo">category</Select.Option>
      </Select>
     </Form.Item>

     <Form.Item label="Description" name="description">
      <TextArea rows={5} />
     </Form.Item>

     <Form.Item label="Users of the Project" name="users">
      <Select>
       <Select.Option value="demo">userOfTheProject</Select.Option>
      </Select>
     </Form.Item>

     <Form.Item label="YouTube Link" name="link">
      <Input />
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

     <Form.Item label="Year" name="year">
      <Select>
       <Select.Option value="2018">2018</Select.Option>
       <Select.Option value="2019">2019</Select.Option>
       <Select.Option value="2020">2020</Select.Option>
       <Select.Option value="2021">2021</Select.Option>
       <Select.Option value="2022">2022</Select.Option>
       <Select.Option value="2023">2023</Select.Option>
      </Select>
     </Form.Item>

     <Form.Item label="Partner" name="partner">
      <Select>
       <Select.Option value="partner1">partner1</Select.Option>
       <Select.Option value="partner2">partner2</Select.Option>
       <Select.Option value="partner3">partner3</Select.Option>
      </Select>
     </Form.Item>


     <Form.Item>
      <Button htmlType='submit' type='primary'>SAVE</Button>
     </Form.Item>
    </Form>
   </div>



  </div>
 )

}

export default AddProject