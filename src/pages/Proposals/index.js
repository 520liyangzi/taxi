import './index.scss'
import { Input, Table, Button, Layout, Space, Popconfirm, Pagination } from 'antd'
import axios from 'axios'
import React from 'react'
import { Link } from 'react-router-dom'
const { Search } = Input
const { Footer } = Layout
// class Proposals extends React.Component
class Proposals extends React.Component {

  state = {
    list: [],
    columns: [
      {
        title: 'No.',
        dataIndex: 'id',
        key: 'id',
      },
      {
        title: 'Status',
        dataIndex: 'status',
        key: 'status',
      },
      {
        title: 'Title',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: 'Date',
        dataIndex: 'date',
        key: 'date',
      },
      {
        title: 'Organization',
        dataIndex: 'organization',
        key: 'organization',
      },
      {
        title: '',
        dataIndex: 'do',
        key: 'do',
        render: (text, record) => (
          <Space size="middle">
            <Popconfirm title="Are you sure to respond?"
              onConfirm={() => this.handleDelete(record.id)}>
              <Button>Respond</Button>
            </Popconfirm>
          </Space>
        ),
      },
    ]
  }

  //load
  loadList = async () => {
    const res = await axios.get('http://localhost:8080/admin/proposal/data')
    console.log(res)
    this.setState({
      list: res.data
    })
  }

  // search
  onSearch = async (value) => {
    console.log(value)
    const url = 'http://localhost:8080/admin/search/?keyword=' + value
    const res = await axios.get(url)
    console.log(res)
    this.setState({
      list: res.data
    })
  }

  // 删除
  // handleDelete = async (id) => {
  //   await axios.delete(`http://localhost:8080/admin/delete/${id}`)
  //   this.loadList()
  // }

  componentDidMount () {
    // 发送接口请求
    this.loadList()
  }

  render () {
    return (

      <div className='ban'>
        <div className='eight'>
          <div className='prop'>
            Proposals
          </div>

          <div className='search_export'>
            <div className="search-box">
              <Search
                className='search'
                placeholder="search proposals"
                allowClear
                size="middle"
                onChange={this.inputChange}
                value={this.state.keyword}
                onSearch={this.onSearch}
              />
            </div>
            <Button className='export' type='primary'>
              <Link to="http://localhost:8080/admin/proposals/csv">Export to CSV</Link>
            </Button>
          </div>

          <div className='table'>
            <Table bordered dataSource={this.state.list} columns={this.state.columns}
              pagination={{ pageSize: 5, showTotal: total => `共 ${total} 条` }} />
          </div>

          <Footer
            style={{
              textAlign: 'center',
            }}
          >
            UCL ©2023 Created Group 10
          </Footer>
        </div>
      </div>

    )
  }

}
export default Proposals