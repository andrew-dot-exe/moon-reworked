<template>
  <div class="zone-chooser">
    <h1>Zone Chooser</h1>
    <div class="zones">
      <div class="zone" v-for="zone in zoneStore.zones" :key="zone.id">
        <a>{{ zone.name }}</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useZoneStore } from '@/stores/zoneStore';

// Create reactive references
const zoneStore = useZoneStore();


// Lifecycle hooks
onMounted(async () => {
  await zoneStore.fetchAllZones();
});

</script>

<style scoped>
.zone-chooser {
  text-align: center;
}
</style>
