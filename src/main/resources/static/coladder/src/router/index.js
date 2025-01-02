import {createRouter,createWebHistory} from 'vue-router'

//导入组件
import Login from '@/views/Login.vue'
import Layout from '@/views/Layout.vue'

import ArticleChart from '@/views/article/ArticleChart.vue'
import ArticleManage from '@/views/article/ArticleManage.vue'
import ArticleBlock from '@/views/article/ArticleBlock.vue'
import ArticleMain from '@/views/article/ArticleMain.vue'
import UserInfo from '@/views/user/UserInfo.vue'


const routes = [
    {path:'/login',component:Login},
    {path:'/',component:Layout,redirect: '/article/main',children:[
        {path : '/article/chart',component: ArticleChart},
        {path : '/article/manage',component:ArticleManage},
        {path:'/article/block',component:ArticleBlock},
        {path:'/article/main',component:ArticleMain},
    ]}
]

//创建路由器
const router = createRouter({
    history:createWebHistory(),
    routes:routes
})

export default router

