
<template>
    <img
      :src="srcsvg || '/img/resources/garbage.svg'"
      :alt="alts || 'иконка'"
      class="inline-icon"
    />
  </template>
  
  <script setup>
  
  defineProps({
    srcsvg: String,
    alts: String
  })
  </script>
  
  <style scoped>
      .inline-icon {
          filter: invert(0);
          display: inline;
          vertical-align: middle;
          height: 1.2em;
      }
  
      .dark .inline-icon {
          filter: invert(1);
      }
  </style>
  