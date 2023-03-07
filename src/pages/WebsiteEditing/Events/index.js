import React from 'react'
import { Input, Table, Button, Menu, Layout, Space, Popconfirm, Pagination } from 'antd'
import './index.scss'
import axios from 'axios'
import { Link } from 'react-router-dom'
class Events extends React.Component {

  //load
  loadList = async () => {
    const res = await axios.get('http://localhost:8080/admin/events')
    console.log(res)
    this.setState({
      list: res.data
    })
  }

  handleDelete = async (id) => {
    const res = await axios.delete(`http://localhost:8080/admin/events/${id}`)
    this.setState({
      list: res.data
    })
    this.loadList()
  }

  componentDidMount () {
    // 发送接口请求
    this.loadList()
  }

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
        with: 120,
        render: media => {
          const ur = (media === "" ? '../../assets/error.png' :
            ('../../../public/img/' + media))
          return <img src={ur} width={80} height={60} alt="" />
        }
      },
      {
        title: 'Title',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: 'Views',
        dataIndex: 'views',
        key: 'views',
      },
      {
        title: 'edit',
        dataIndex: 'edit',
        key: 'edit',
        render: (text, record) => (
          <Space size="middle">
            <Button>
              <Link to={"/editEvent/" + record.id}>Edit</Link>
            </Button>
          </Space>
        ),
      },
      {
        title: 'publish',
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
      <div>
        <div className='eight'>
          <div className='events_box'>
            Event
            <Button className='add_new' type='primary'>
              <Link to={"/addEvent"}> ADD NEW EVENT </Link>
            </Button>
          </div>
          <div className='table'>
            <Table bordered dataSource={this.state.list} columns={this.state.columns}
              pagination={{ pageSize: 5, showTotal: total => `共 ${total} 条` }} />
          </div>
        </div>
      </div>
    )
  }
}

export default Events