import './index.scss'
import { Input, Table, Button, Layout, Space, Popconfirm, Pagination } from 'antd'
import axios from 'axios'
import React from 'react'
import { Link } from 'react-router-dom'
import img404 from '../../assets/error.png'
const { Search } = Input
const { Footer } = Layout
class Projects extends React.Component {



  state = {
    list: [],
    columns: [
      {
        title: 'No',
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
        dataIndex: 'id',
        key: 'id',
        render: (text, record) => (
          <Space size="middle">
            <Button>
              <Link to={"/editProject/" + record.id}>Edit</Link>
            </Button>
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

  //load
  loadList = async () => {
    const res = await axios.get('http://localhost:8080/admin/project/data')
    console.log(res)
    this.setState({
      list: res.data
    })
  }

  // search
  onSearch = async (value) => {
    console.log(value)
    const url = 'http://localhost:8080/admin/search/project/?keyword=' + value
    const res = await axios.get(url)
    console.log(res)
    this.setState({
      list: res.data
    })
  }

  // 删除
  handleDelete = async (id) => {
    await axios.delete(`http://localhost:8080/admin/project/delete/${id}`)
    this.loadList()
  }

  goToEdit = (id) => {
    console.log(id)
  }

  componentDidMount () {
    // 发送接口请求
    this.loadList()
  }

  render () {
    return (

      <div className='ban'>
        <div className='eight'>
          <div className='prop'>
            Projects
          </div>

          <div className='search_export'>
            <div className="search-box">
              <Search
                className='search'
                placeholder="search projects"
                allowClear
                size="middle"
                onChange={this.inputChange}
                value={this.state.keyword}
                onSearch={this.onSearch}
              />
            </div>
            <Button className='add' type='primary'>
              <Link to={"/addProject"}> Add Project </Link>
            </Button>
            <Button className='export' type='primary'>
              <Link to="http://localhost:8080/admin/csv">Export Projects</Link>
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
export default Projects