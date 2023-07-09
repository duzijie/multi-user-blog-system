import Vue from 'vue'
import VueRouter from 'vue-router'

const Login = {
  Default: () => import('../views/login/Default'),
  Login: () => import('../views/login/Login'),
  Regist: () => import('../views/login/Regist'),
  Reset: () => import('../views/login/Reset'),
}
const Other = {
  Other: () => import('../views/other/Other'),
  Help: () => import('../views/other/Help'),
  About: () => import('../views/other/About'),
  Err404: () => import('../views/other/Err404'),
}
const Editor = {
  Default: () => import('../views/editor/Default'),
  Editor: () => import('../views/editor/Editor2'),
  Show: () => import('../views/editor/Show'),
}
const Home = {
  Home: () => import('../views/home/Home'),
  Default: () => import('../views/home/Default'),
  Type: () => import('../views/home/Type'),
  Search: () => import('../views/home/Search'),
}
const Blog = {
  Blog: () => import('../views/blog/Blog'),
  Intro: () => import('../views/blog/Intro'),
  Article: () => import('../views/blog/Article'),
  Follow: () => import('../views/blog/Follow'),
  Letter: () => import('../views/blog/Letter'),
}
const Mine = {
  Mine: () => import('../views/mine/Mine'),
  Notice: () => import('../views/mine/Notice'),
  Letter: () => import('../views/mine/Letter'),
  Info: () => import('../views/mine/Info'),
  Account: () => import('../views/mine/Account'),
  Blog: () => import('../views/mine/Blog')
}

Vue.use(VueRouter)

const routes = [
  // 根路径
  {
    path: '/',
    redirect: '/home'
  },
  // 主页
  {
    path: '/',
    component: Home.Home,
    children: [
      // 默认
      {
        path: 'home',
        name: 'HomeDefault',
        component: Home.Default
      },
      // 分类
      {
        path: 'type',
        name: 'HomeType',
        component: Home.Type
      },
      // 搜索
      {
        path: 'search',
        name: 'HomeSearch',
        component: Home.Search
      }
    ]
  },
  // 博客
  {
    path: '/blog/:userId',
    component: Blog.Blog,
    props: true,
    beforeEnter: (to, from, next) => {
      const bool1 = to.params.userId == "me";
      const bool2 = !localStorage.getItem('login');
      if (bool1 && bool2) next('/login');
      else next();
    },
    children: [
      // 默认
      {
        path: 'intro',
        name: 'BlogDefault',
        props: true,
        component: Blog.Intro
      },
      // 文章
      {
        path: 'article',
        name: 'BlogArticle',
        props: true,
        component: Blog.Article
      },
      // 文章
      {
        path: 'follow',
        name: 'BlogFollow',
        props: true,
        component: Blog.Follow
      },
      // 私信
      {
        path: 'letter',
        name: 'BlogLetter',
        props: true,
        component: Blog.Letter
      },
    ]
  },
  // 我的
  {
    path: '/mine',
    component: Mine.Mine,
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem('login')) next();
      else next('/login');
    },
    children: [
      // 通知管理
      {
        path: 'notice',
        name: 'MineNotice',
        component: Mine.Notice
      },
      // 通知管理
      {
        path: 'letter',
        name: 'MineLetter',
        component: Mine.Letter
      },
      // 个人资料
      {
        path: 'info',
        name: 'MineInfo',
        component: Mine.Info
      },
      // 账号管理
      {
        path: 'account',
        name: 'MineAccount',
        component: Mine.Account
      },
      // 文章管理
      {
        path: 'blog/:type?',
        name: 'MineBlog',
        props: true,
        component: Mine.Blog
      },
    ]
  },
  // 文章相关
  {
    path: '/',
    component: Editor.Default,
    children: [
      // 编辑器
      {
        path: '/editor/:blogId?',
        name: 'Editor',
        props: true,
        beforeEnter: (to, from, next) => {
          if (localStorage.getItem('login')) next();
          else next('/login');
        },
        component: Editor.Editor
      },
      // 查看文章
      {
        path: '/show/:blogId?',
        name: 'Show',
        props: true,
        component: Editor.Show
      },
    ]
  },
  // 登录相关
  {
    path: '/',
    component: Login.Default,
    children: [
      // 登录
      {
        path: '/login',
        name: 'Login',
        component: Login.Login
      },
      // 注册
      {
        path: '/regist',
        name: 'Regist',
        props: true,
        component: Login.Regist
      },
      // 重置密码
      {
        path: '/reset',
        name: 'Reset',
        props: true,
        component: Login.Reset
      },
    ]
  },
  // 其他
  {
    path: '/',
    component: Other.Other,
    children: [
      // 关于
      {
        path: '/about',
        name: 'About',
        component: Other.About
      },
      // 帮助
      {
        path: '/help',
        name: 'Help',
        component: Other.Help
      },
      // 404
      {
        path: '*',
        name: 'Err404',
        component: Other.Err404
      },
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  let app = document.getElementById('app');
  if (app) app.scrollTo(0, 0);
  next();
})

export default router