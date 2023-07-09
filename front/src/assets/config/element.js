import Vue from 'vue'
import {
  Button,
  ButtonGroup,
  Radio,
  Input,
  Form,
  FormItem,
  Row,
  Col,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Avatar,
  Pagination,
  Menu,
  MenuItem,
  MenuItemGroup,
  Submenu,
  Breadcrumb,
  BreadcrumbItem,
  Dialog,
  Upload,
  DatePicker,
  Cascader,
  Table,
  TableColumn,
  Loading,
  Message,
  MessageBox,
} from 'element-ui'

Vue.use(Button)
Vue.use(ButtonGroup)
Vue.use(Radio)
Vue.use(Input)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Row)
Vue.use(Col)
Vue.use(Dropdown)
Vue.use(DropdownMenu)
Vue.use(DropdownItem)
Vue.use(Avatar)
Vue.use(Pagination)
Vue.use(Menu)
Vue.use(MenuItem)
Vue.use(MenuItemGroup)
Vue.use(Submenu)
Vue.use(Breadcrumb)
Vue.use(BreadcrumbItem)
Vue.use(Dialog)
Vue.use(Upload)
Vue.use(DatePicker)
Vue.use(Cascader)
Vue.use(Table)
Vue.use(TableColumn)
Vue.prototype.$loading = Loading.service;
Vue.prototype.$message = Message;
Vue.prototype.$confirm = MessageBox.confirm;
