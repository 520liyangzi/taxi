import { BrowserRouter, Route, Routes, unstable_HistoryRouter as HistoryRounter } from 'react-router-dom'

// 导入页面组件
import Layout from './pages/Layout'
import Login from '@/pages/Login'
import { AuthRoute } from '../src/components/AuthComponent'
import './App.css'
import WebsiteEditing from './pages/WebsiteEditing'
import Proposals from './pages/Proposals'
import Projects from './pages/Projects'
import { history } from './utils/history'
import Events from './pages/WebsiteEditing/Events'
import News from './pages/WebsiteEditing/News'
import WorkWithUs from './pages/WebsiteEditing/WorkWithUs'
import AboutUs from './pages/WebsiteEditing/AboutUs'
import AddProject from './pages/Projects/AddProject'
import EditProject from './pages/Projects/EditProject'
import AddEvent from './pages/WebsiteEditing/Events/AddEvents'
import EditEvent from './pages/WebsiteEditing/Events/EditEvents'
import AddNews from './pages/WebsiteEditing/News/AddNews'
import EditNews from './pages/WebsiteEditing/News/EditNews'
function App () {
  return (

    <HistoryRounter history={history}>
      <div className="App">
        <Routes>
          <Route path="/*" element={
            <AuthRoute>
              <Layout />
            </AuthRoute>
          }>
            <Route path='editProject/*' element={<EditProject />} />
            <Route path='addProject' element={<AddProject />} />

            <Route path='editEvent/*' element={<EditEvent />} />
            <Route path='addEvent' element={<AddEvent />} />

            <Route path='editNews/*' element={<EditNews />} />
            <Route path='addNews' element={<AddNews />} />

            <Route index element={<Proposals />}></Route>
            <Route path='projects' element={<Projects />}></Route>
            <Route path='websiteEditing' element={<WebsiteEditing />}>
              <Route path='events' element={<Events />} />
              <Route path='news' element={<News />} />
              <Route path='workWithUs' element={<WorkWithUs />} />
              <Route path='aboutUs' element={<AboutUs />} />
            </Route>

          </Route>



          {/* 不需要鉴权的路由 */}
          <Route path='/login' element={<Login />} />
        </Routes>
      </div>
    </HistoryRounter>
  )
}

export default App
