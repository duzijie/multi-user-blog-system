<template>
  <div class="notice">
    <el-table :data="notices" style="width: 100%">
      <el-table-column type="index" label="序"></el-table-column>
      <el-table-column label="时间" width="120">
        <template slot-scope="scope">
          {{ scope.row.createTime | timeFormat }}
        </template>
      </el-table-column>
      <el-table-column
        label="标题"
        prop="content"
      ></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Notice",
  data() {
    return {
      notices: [],
    };
  },
  created() {
    this.getMessageNotice();
  },
  methods: {
    getMessageNotice() {
      this.axios.get("/api2/blogsystem/notice/noticeList").then((res) => {
        // console.info(res.data);
        if (res.data.code == 0) {
          this.notices = res.data.entities.reverse();
          console.info(this.notices);
        }
      });
    },
  },
  filters: {
    timeFormat(val) {
      val = (val + "").replace("T", " ");
      val = val.slice(0, 10);
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
.notice {
  padding: 20px 30px;
  box-sizing: border-box;
  text-align: left;
  background: var(--background3);
  border-radius: 4px;
}
</style>