<template>
  <div class="user">
    <el-row>
      <div class="filter-container">
        <el-input
          v-model="listQuery.id"
          placeholder="用户ID"
          style="width: 80px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-input
          v-model="listQuery.username"
          placeholder="用户名"
          style="width: 100px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />

        <el-input
          v-model="listQuery.address"
          placeholder="住址"
          style="width: 200px"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />

        <el-select
          v-model="listQuery.sex"
          placeholder="性别"
          clearable
          class="filter-item"
          style="width: 80px"
        >
          <el-option
            v-for="item in sexTypeOptions"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>

        <el-select
          v-model="listQuery.islock"
          placeholder="状态"
          clearable
          class="filter-item"
          style="width: 100px"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>

        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleFilter"
          >查询</el-button
        >
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-circle-close"
          @click="cleanFilter"
          >清空</el-button
        >

        <el-button
          class="filter-item"
          style="margin-left: 10px"
          type="primary"
          icon="el-icon-refresh"
          @click="handleRestFilter"
          >重置</el-button
        >
      </div>
    </el-row>

    <el-row>
      <el-col>
        <el-table
          v-loading="listLoading"
          height="460"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%"
          @sort-change="sortChange"
        >
          <el-table-column
            label="ID"
            prop="id"
            sortable="custom"
            align="center"
            width="70"
            :class-name="getSortClass('id')"
          >
            <template slot-scope="{ row }">
              <span>{{ row.id }}</span>
            </template>
          </el-table-column>

          <el-table-column
            label="用户名"
            prop="username"
            align="center"
            width="100"
          >
            <template slot-scope="{ row }">
              <span>{{ row.username }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="sex" label="性别" align="center" width="80">
            <template slot-scope="{ row }">
              <el-tag :type="row.sex != '1' ? 'danger' : 'success'">{{
                row.sex | sexFilter
              }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column
            label="电话号码"
            prop="phone"
            align="center"
            width="120"
          >
            <template slot-scope="{ row }">
              <span>{{ row.phone }}</span>
            </template>
          </el-table-column>

          <el-table-column label="家庭住址" prop="address" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.address }}</span>
            </template>
          </el-table-column>

          <!-- 日期处理 使用插槽slot -->
          <el-table-column
            label="创建时间"
            prop="createTime"
            width="180px"
            align="center"
          >
            <template slot-scope="{ row }">
              <span>{{ dateFormat(row.createTime) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="状态" prop="islock" align="center" width="60">
            <template slot-scope="{ row }">
              <el-tag
                :type="row.islock != '1' ? 'success' : 'info'"
                disable-transitions
                >{{ row.islock | statusFilter }}</el-tag
              >
            </template>
          </el-table-column>

          <!-- 操作 绑定事件 -->
          <el-table-column
            label="操作"
            align="center"
            fixed="right"
            width="170"
          >
            <template slot-scope="{ row }">
              <el-button
                v-if="row.islock != '0'"
                size="mini"
                type="success"
                @click="handleModifyStatus(row, '0')"
                >启用</el-button
              >
              <el-button
                v-if="row.islock != '1'"
                size="mini"
                type="danger"
                @click="handleModifyStatus(row, '1')"
                >禁用</el-button
              >
              <el-button
                size="mini"
                type="warning"
                @click="upgradePermissions(row)"
                >升级</el-button
              >
            </template>
          </el-table-column>

          <el-table-column label="查看主页" width="100px" align="center">
            <template slot-scope="{ row }">
              <el-button
                size="mini"
                type="primary"
                class="el-icon-view el-icon--right"
                @click="viewHomepage(row)"
                >查看</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <!-- 使用分页组件 -->
        <div class="block">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            :page-sizes="limit"
            :page-size="limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </div>

      </el-col>
    </el-row>

    <el-dialog :title="textMap" :visible.sync="dialogUser">
      <el-form
        ref="dataForm"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 80%; margin-left: 50px"
      >
        <el-form-item label="用户id" prop="id">
          <el-input readonly v-model="temp.id" />
        </el-form-item>

        <el-form-item label="用户昵称" prop="name">
          <el-input readonly v-model="temp.name" />
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input readonly v-model="temp.username" />
        </el-form-item>

        <el-form-item label="性别" prop="sex">
          <el-input readonly v-model="temp.sex" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input readonly v-model="temp.email" />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input readonly v-model="temp.phone" />
        </el-form-item>

        <el-form-item label="生日" prop="birthday">
          <el-input readonly v-model="temp.birthday" />
        </el-form-item>

        <el-form-item label="住址" prop="address">
          <el-input readonly v-model="temp.address" />
        </el-form-item>

        <el-form-item label="创建时间" prop="createTime">
          <el-input readonly v-model="temp.createTime" />
        </el-form-item>

        <el-form-item label="个性签名" prop="motto">
          <el-input readonly type="textarea" v-model="temp.motto" />
        </el-form-item>

        <el-form-item label="个人简介" prop="introduction">
          <el-input
            readonly
            type="textarea"
            :rows="10"
            v-model="temp.introduction"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogUser = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      listall: [],
      list: [5],
      total: 0,
      limit: 5,
      listLoading: false,
      listQuery: {
        id: undefined,
        name: undefined,
        email: undefined,
        username: undefined,
        phone: undefined,
        sex: undefined,
        birthday: undefined,
        address: undefined,
        islock: undefined, //状态
        createTime: undefined,
        introduction: undefined, //个人简介
        motto: undefined, //个性签名
        roles: undefined, //角色身份
        page: 1,
        limit: 20,
      },
      man: "男",
      woman: "女",
      textMap: "查看用户",
      //查看用户结构体
      temp: {
        id: undefined,
        name: undefined,
        email: undefined,
        username: undefined,
        phone: undefined,
        sex: undefined,
        birthday: undefined,
        address: undefined,
        islock: undefined, //状态
        createTime: undefined,
        introduction: undefined, //个人简介
        motto: undefined, //个性签名
        roles: undefined, //角色身份
      },
      dialogUser: false,

      sexTypeOptions: [
        { key: "1", display_name: "男" },
        { key: "0", display_name: "女" },
      ],
      statusOptions: [
        { key: "1", display_name: "封禁" },
        { key: "0", display_name: "启用" },
      ],
    };
  },

  created() {
    this.getList();
  },
  methods: {
    sortChange(data) {
      const { prop, order } = data;
      this.sortFilter(prop, order);
    },
    getSortClass: function (key) {
      const sort = this.listQuery.sort;
      if (key === "id") {
        return sort === `+${key}` ? "ascending" : "descending";
      }
    },
    //排序
    sortFilter: function (prop, order) {
      if (prop === "id") {
        this.proptype = prop;
        if (order === "descending") {
          this.listall.sort(this.my_desc_sort);
        } else if (order === "ascending") {
          this.listall.sort(this.my_asc_sort);
        }
      }
      this.currentPage=1;
      this.list = this.listall.filter(
        (item, index) => index < this.limit && index >= 0
      );
    },
    //逆序
    my_desc_sort(a, b) {
      return b[this.proptype] - a[this.proptype];
    },
    //正序
    my_asc_sort(a, b) {
      return a[this.proptype] - b[this.proptype];
    },
    // handleSizeChange(val) {
    //   console.log(`每页 ${val} 条`);
    // },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.list = this.listall.filter(
        (item, index) =>
          index < this.limit * val && index >= this.limit * (val - 1)
      );
    },
    //查询
    handleFilter: function () {
      (this.listQuery.page = 1), this.getList();
    },
    //清空
    cleanFilter: function () {
      this.listQuery = {
        id: undefined,
        name: undefined,
        email: undefined,
        username: undefined,
        phone: undefined,
        sex: undefined,
        birthday: undefined,
        address: undefined,
        islock: undefined, //状态
        create_time: undefined,
        introduction: undefined, //个人简介
        motto: undefined, //个性签名
        roles: undefined, //角色身份
        page: 1,
        limit: 20,
      };
    },
    //重置
    handleRestFilter: function () {
      this.listQuery = {
        id: undefined,
        name: undefined,
        email: undefined,
        username: undefined,
        phone: undefined,
        sex: undefined,
        birthday: undefined,
        address: undefined,
        islock: undefined, //状态
        create_time: undefined,
        introduction: undefined, //个人简介
        motto: undefined, //个性签名
        roles: undefined, //角色身份
        page: 1,
        limit: 20,
      };
      this.getList();
    },
    getList() {
      this.listLoading = true;
      this.axios
        .post("/api/blogsystem/user/admin/searchUsersByParams", this.listQuery)
        .then((res) => {
          console.info(res.data);
          if (res.data.code == 0) {
            this.listLoading = false;
          }
          
          this.listall = res.data.userEntityList;
          this.total = this.listall.length;
          this.list = this.listall.filter(
            (item, index) => index < this.limit && index >= 0
          );
          
        })
        .catch(() => {});
    },
    //时间格式化
    dateFormat: function (time) {
      var date = new Date(time);
      var year = date.getFullYear();
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hours =
        date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
      var minutes =
        date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      var seconds =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      // 拼接
      return (
        year +
        "-" +
        month +
        "-" +
        day +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds
      );
    },
    //查看用户详细信息
    viewHomepage: function (row) {
      this.temp = Object.assign({}, row);
      this.temp.birthday = this.dateFormat(this.temp.birthday);
      this.temp.createTime = this.dateFormat(this.temp.createTime);
      if (this.temp.sex == "1") {
        this.temp.sex = "男";
      }
      if (this.temp.sex == "0") {
        this.temp.sex = "女";
      }
      this.dialogUser = true;
      if (this.temp.sex == "男") {
        this.temp.sex = "1";
      }
      if (this.temp.sex == "女") {
        this.temp.sex = "0";
      }
    },
    //升级为管理员
    upgradePermissions: function (row) {
      row.roles = 'ROLE_ADMIN';
      console.info(row);
      this.axios
        .post("/api/blogsystem/user/super/changeRoles", row)
        .then((res) => {
          if (res.data.msg == "success") {
            this.$message.success("升级成功");
             this.getList();
          }
        })
        .catch(() => {});
    },
    //修改用户状态
    handleModifyStatus: function (row, status) {
      row.islock = status;
      this.axios
        .post(
          "/api/blogsystem/user/admin/updateUserStatus?id=" +
            row.id +
            "&islock=" +
            row.islock
        )
        .then((res) => {
          if (res.data.code == 0) {
            this.$message.success("修改成功");
            this.getList();
          }
        })
        .catch(() => {});
    },
  },

  filters: {
    statusFilter(islock) {
      const statusMap = {
        1: "封禁",
        0: "启用",
      };
      return statusMap[islock];
    },
    sexFilter(sex) {
      const sexMap = {
        1: "男",
        0: "女",
      };
      return sexMap[sex];
    },
  },
};
</script>

<style scoped>
.user {
  width: 100%;
  min-height: 100%;
  background: var(--background3);
  padding: 20px 25px;
  box-sizing: border-box;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}
</style>