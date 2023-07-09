import Vue from 'vue'
import VueRouter from 'vue-router'

const Login = () => import('../views/Login')
const About = () => import('../views/About')
const Err404 = () => import('../views/Err404')
const Home = {
  Home: () => import('../views/home/Home'),
  User: () => import('../views/home/User'),
  Blog: () => import('../views/home/Blog'),
  Notice: () => import('../views/home/Notice'),
  Admin: () => import('../views/home/Admin'),
}
const Homes = {
  Homes: () => import('../views/homes/Homes'),
  User: () => import('../views/homes/User'),
  Blog: () => import('../views/homes/Blog'),
  Notice: () => import('../views/homes/Notice'),
}
Vue.use(VueRouter)

const routes = [{
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/home',
    component: Home.Home,
    children: [
      {
        path: '',
        name: 'HomeAdmin',
        component: Home.Admin
      },
      {
        path: 'user',
        name: 'HomeUser',
        component: Home.User
      },
      {
        path: 'blog',
        name: 'HomeBlog',
        component: Home.Blog
      },
      {
        path: 'notice',
        name: 'HomeNotice',
        component: Home.Notice
      },
      
    ]
  },
  {
    path: '/homes',
    component: Homes.Homes,
    children: [{
        path: '',
        name: 'HomesUser',
        component: Homes.User
      },
      {
        path: 'blog',
        name: 'HomesBlog',
        component: Homes.Blog
      },
      {
        path: 'notice',
        name: 'HomesNotice',
        component: Homes.Notice
      },
    ]
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/*',
    name: 'Err404',
    component: Err404
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path == '/') {
    next();
    return;
  }
  if (sessionStorage.getItem('login') == 'true') {
    next();
  } else {
    next('/');
  }
})

export default router