<template>
  <div class="foo">
    <a :href="src" target="_brank">
      <span>Copyright</span>
    </a>
    <span>walker</span>
  </div>
</template>

<script>
export default {
  data() {
    return {
      src: "",
    };
  },
};
</script>

<style scoped>
.foo {
  width: 100%;
  height: 70px;
  line-height: 70px;
  background: var(--background4);
  color: var(--white) !important;
  text-align: center;
}
.foo a {
  color: var(--white) !important;
  padding: 0 12px;
}
.foo span {
  color: var(--white) !important;
  padding: 0 10px;
}
</style>