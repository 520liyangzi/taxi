import './index.scss'
import { Input, Table, Button, Menu, Layout, Space, Popconfirm, Pagination } from 'antd'
import axios from 'axios'
import React from 'react'
import { Link, Outlet } from 'react-router-dom'

const { Search } = Input
const { Footer } = Layout
// class Proposals extends React.Component
class WebsiteEditing extends React.Component {

   state = {
      list: [],
      columns: [
         {
            title: 'No.',
            dataIndex: 'id',
            key: 'id',
         },
         {
            title: 'Media',
            dataIndex: 'media',
            key: 'media',
         },
         {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
         },
         {
            title: 'Year',
            dataIndex: 'year',
            key: 'year',
         },
         {
            title: 'Views',
            dataIndex: 'views',
            key: 'views',
         },
         {
            title: '',
            dataIndex: 'edit',
            key: 'edit',
            render: (text, record) => (
               <Space size="middle">
                  <Popconfirm title="Are you sure to respond?"
                     onConfirm={() => this.handleEdit(record.id)}>
                     <Button>Edit!!</Button>
                  </Popconfirm>
               </Space>
            ),
         },
         {
            title: '',
            dataIndex: 'delete',
            key: 'delete',
            render: (text, record) => (
               <Space size="middle">
                  <Popconfirm title="Are you sure to respond?"
                     onConfirm={() => this.handleDelete(record.id)}>
                     {/* <a href="#">Respond</a> */}
                     <Button>Delete</Button>
                  </Popconfirm>
               </Space>
            ),
         },
      ]
   }

   render () {
      return (

         <div className='ban'>
            <div className='banner'>
               <div className='eight'>
                  <div className='t'>
                     Website Editing
                  </div>

                  <Menu className='mm'
                     triggerSubMenuAction="hover"
                     theme="dark" mode="horizontal" defaultSelectedKeys={['websiteEditing/events']} >
                     <Menu.Item key="/websiteEditing/events">
                        <Button className='b' block>
                           <Link to={"/websiteEditing/events"}>Events</Link>
                        </Button>
                     </Menu.Item>

                     <Menu.Item key="/websiteEditing/news">
                        <Button className='b' block>
                           <Link to={"/websiteEditing/news"}>News</Link>
                        </Button>
                     </Menu.Item>

                     <Menu.Item key="/websiteEditing/workWithUs">
                        <Button className='b' block>
                           <Link to={"/websiteEditing/workWithUs"}>Work with Us</Link>
                        </Button>
                     </Menu.Item>

                     <Menu.Item key="/websiteEditing/aboutUs">
                        <Button className='b' block>
                           <Link to={"/websiteEditing/aboutUs"}>About us</Link>
                        </Button>
                     </Menu.Item>

                  </Menu>
               </div>
            </div>
            <Outlet />
         </div>

      )
   }

}
export default WebsiteEditing