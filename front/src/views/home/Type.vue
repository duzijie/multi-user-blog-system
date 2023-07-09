<template>
  <el-row :gutter="20" class="type">
    <el-col :span="6" class="left">
      <el-menu unique-opened :default-openeds="defaultOpen">
        <el-submenu
          v-for="(val1, index1) in types"
          :key="val1.id"
          :index="index1 + ''"
          @click.native.stop="setBreadcrumb(0, val1.name)"
        >
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>{{ val1.name }}</span>
          </template>
          <el-submenu
            v-for="(val2, index2) in val1.children"
            :key="val2.id"
            :index="index1 + '-' + index2"
            @click.native.stop="setBreadcrumb(1, val2.name)"
          >
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>{{ val2.name }}</span>
            </template>
            <el-menu-item
              v-for="(val3, index3) in val2.children"
              :key="val3.id"
              :index="index1 + '-' + index2 + '-' + index3"
              @click.native.stop="setCurrentType(val3.id, val3.name)"
            >
              <i class="el-icon-star-on"></i>
              <span>{{ val3.name }}</span>
            </el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-col>
    <el-col :span="18" class="right">
      <div class="item" style="padding-bottom: 20px">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>{{ breadcrumb[0] }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="breadcrumb[1]">
            {{ breadcrumb[1] }}
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="breadcrumb[2]">
            {{ breadcrumb[2] }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div v-for="item in blogs" :key="item.id" class="item">
        <el-row :gutter="20">
          <el-col :span="17" class="title">
            <router-link target="_blank" :to="'/show/' + item.id">
              {{ item.title }}
            </router-link>
          </el-col>
          <el-col :span="7" class="create-time">
            {{ item.createTime | timeFormat }}
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin: 5px -10px">
          <el-col class="summary">{{ item.summary | lengthFormat }}</el-col>
        </el-row>
        <el-row :gutter="20" class="bottom">
          <el-col :span="3">
            <span class="el-icon-view"></span>
            {{ item.readSize | numberFormat }}
          </el-col>
          <el-col :span="3">
            <span class="el-icon-chat-line-round"></span>
            {{ item.commentSize | numberFormat }}
          </el-col>
          <el-col :span="3">
            <span class="el-icon-sugar"></span>
            {{ item.voteSize | numberFormat }}
          </el-col>
          <el-col :span="15" class="author">
            <span>
              <i class="el-icon-user"></i>
              <router-link
                :to="'/blog/' + item.userId + '/intro'"
                target="_blank"
              >
                {{ item.username }}
              </router-link>
            </span>
          </el-col>
        </el-row>
      </div>
      <div class="item nomore">
        没有更多了
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "",
  data() {
    return {
      types: [{}, {}, {}, {}, {}, {}, {}],
      breadcrumb: ["", "", ""],
      defaultOpen: ["0", "0-0"],
      currentType: "",
      blogs: [],
    };
  },
  created() {
    this.getType();
  },
  watch: {
    currentType() {
      this.getBlogListByType();
    },
    types() {
      this.breadcrumb[0] = this.types[0].name;
      this.breadcrumb[1] = this.types[0].children[0].name;
      this.breadcrumb[2] = this.types[0].children[0].children[0].name;
      this.currentType = this.types[0].children[0].children[0].id;
      this.getBlogListByType();
    },
  },
  methods: {
    getType() {
      this.axios.get("/api/blogsystem/catalog/list/tree").then((res) => {
        if (res.data.code == 0) {
          this.types = res.data.catalogEntityList;
          // console.info(this.types);
        } else this.$message.error(res.data.msg);
      });
    },
    setCurrentType(val, name) {
      this.currentType = val;
      this.setBreadcrumb(2, name);
    },
    setBreadcrumb(index, val) {
      this.$set(this.breadcrumb, index, val);
      if (index == 0) {
        this.$set(this.breadcrumb, 1, "");
        this.$set(this.breadcrumb, 2, "");
      }
      if (index == 1) {
        this.$set(this.breadcrumb, 2, "");
      }
      // console.info(this.breadcrumb);
    },
    getBlogListByType() {
      this.axios
        .get("/api2/blogsystem/blog/cataloglist/" + this.currentType)
        .then((res) => {
          if (res.data.code == 0) {
            this.blogs = res.data.entities;
            for (let key in this.blogs) {
              this.getBlogUserName(this.blogs[key], key);
            }
          } else this.$message.error(res.data.msg);
        });
    },
    getBlogUserName(item, key) {
      this.axios
        .get("/api/blogsystem/user/profilePage?id=" + item.userId)
        .then((res) => {
          // console.info(res.data);
          if (res.data.code == 0) {
            item.username = res.data.username + "";
            // console.info(key, item)
            this.$set(this.blogs, key, JSON.parse(JSON.stringify(item)));
          }
        });
    },
  },
  filters: {
    timeFormat(val) {
      val = (val + "").replace("T", " ");
      val = val.slice(0, 16);
      return val;
    },
    numberFormat(val) {
      if (!val) return 0;
      return parseInt(val);
    },
    lengthFormat(val) {
      let length = val.length;
      let maxLen = 80;
      val = length > maxLen ? val.slice(0, maxLen) + "..." : val;
      return val;
    },
  },
};
</script>

<style scoped>
.type {
  width: 100%;
  min-height: 1000px;
  padding: 20px 100px;
  box-sizing: border-box;
  text-align: left;
}
.type .left .el-menu {
  border-radius: 4px;
  border: none;
  user-select: none;
}

.type .right {
  min-height: 100px;
  border-radius: 4px;
}
.type .right .item {
  width: 100%;
  background: var(--background3);
  text-align: left;
  padding: 20px 25px 12px;
  box-sizing: border-box;
  margin-bottom: 20px;
  border-radius: 4px;
}
.type .right .item .title {
  font-weight: 600;
  line-height: 32px;
  cursor: pointer;
  color: var(--background);
}
.type .right .item .title:hover {
  text-decoration: underline;
}
.type .right .item .create-time {
  line-height: 32px;
  text-align: right;
  opacity: 0.64;
}
.type .right .item .summary {
  font-size: 15px;
  color: var(--black);
  line-height: 26px;
  text-align: justify;
  opacity: 0.64;
}
.type .right .item .bottom {
  line-height: 32px;
  opacity: 0.64;
  user-select: none;
}
.type .right .item .author {
  text-align: right;
}
.type .right .item .author span {
  cursor: pointer;
}
.type .right .item .author span:hover {
  text-decoration: underline;
}
.type .right .item .author i {
  vertical-align: middle;
  margin: -2px 5px 0 0;
}
.type .right .item.nomore {
  text-align: center; 
  padding-bottom: 20px;
  cursor: default;
}
</style>
<style>
.type .left .el-menu .el-menu-item.is-active {
  background: rgb(245, 249, 252);
}
</style>