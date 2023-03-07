import { Layout, Menu, Button, Popconfirm } from 'antd'
import { LogoutOutlined } from '@ant-design/icons'
import './index.scss'
import { Outlet, Link } from 'react-router-dom'
import UCL from '../../assets/UCL.png'
import { useNavigate, useLocation } from 'react-router-dom'
import { useStore } from '../../store/index'
const { Header } = Layout
const GeekLayout = () => {
 const { pathname } = useLocation()
 const { loginStore } = useStore()
 const navigate = useNavigate()
 const items1 = ['1', '2', '3'].map((key) => ({
  key,
  label: `nav ${key}`,
 }))

 const onConfirm = () => {
  console.log('confirmmmmmmm')
  loginStore.loginOut()
  navigate('/login')
 }



 return (
  <Layout>
   <Header className="header" style={{ display: 'flex' }}>
    <img className='logo' src={UCL} alt="" />

    <Menu className='menu'
     triggerSubMenuAction="hover"
     theme="dark" mode="horizontal" defaultSelectedKeys={[pathname]} >

     <Menu.Item key="/">
      <Link to={"/"}>PROPOSALS</Link>
     </Menu.Item>

     <Menu.Item key="/projects">
      <Link to={"/projects"}>PROJECTS</Link>
     </Menu.Item>

     <Menu.Item key="/websiteEditing">
      <Link to={"/websiteEditing/events"}>WEBSITE EDITING</Link>
     </Menu.Item>

    </Menu>
    <Button className="login-button">
     <Popconfirm title="Are you sure to logout？" okText="logout" cancelText="cancle" onConfirm={onConfirm}>
      <LogoutOutlined /> LOGOUT
     </Popconfirm>
    </Button>
   </Header>
   <Outlet />
  </Layout>


 )
}

export default GeekLayout