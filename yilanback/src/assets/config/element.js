import Vue from 'vue'
import {
  Button,
  ButtonGroup,
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
  Container,
  Header,
  Aside,
  Main,
  Tooltip,
  Scrollbar,
  Table,
  TableColumn,
  Loading,
  Select,
  Option,
  Tag,
  Dialog,
  DatePicker,
  Message
} from 'element-ui'

Vue.use(Button)
Vue.use(ButtonGroup)
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
Vue.use(Container)
Vue.use(Header)
Vue.use(Aside)
Vue.use(Main)
Vue.use(Scrollbar)
Vue.use(Tooltip)
Vue.use(Table)
Vue.use(Select)
Vue.use(Option)
Vue.use(TableColumn)
Vue.use(Tag)
Vue.use(Dialog),
Vue.use(DatePicker),

Vue.use(Loading.directive);
Vue.prototype.$loading = Loading.service;
Vue.prototype.$message = Message;
