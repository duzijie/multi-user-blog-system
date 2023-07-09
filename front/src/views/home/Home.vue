<template>
  <div class="home">
    <yilan-nav></yilan-nav>
    <router-view></router-view>
    <yilan-foo></yilan-foo>
  </div>
</template>

<script>
import nav from "../../components/Nav";
import foo from "../../components/Foo";

export default {
  name: "Home",
  components: {
    "yilan-nav": nav,
    "yilan-foo": foo,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style scoped>
.home {
  width: 100%;
}
</style>