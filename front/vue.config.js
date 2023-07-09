module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
  devServer: {
    open: true,//自动打开
    port: 8100,//端口号
    proxy: {
      //配置跨域
      '^/api2': {
        // target: 'http://localhost:8083', //跨域的地址
        target: 'http://localhost:8083', //跨域的地址
        changOrigin: true, //是否跨域
        pathRewrite: { //当前的名字
          '^/api2/': '/'
        }
      },
      '^/api': {
        // target: 'http://localhost:8083', //跨域的地址
        target: 'http://localhost:8083', //跨域的地址
        changOrigin: true,//是否跨域
        pathRewrite: {//当前的名字
          '^/api/': '/'
        }
      },
    }
  },
};
