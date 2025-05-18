<template>
  <div class="zone-chooser">
    <h1>Zone Chooser</h1>
    <div class="zones">
      <div class="zone" v-for="zone in zoneStore.zones" :key="zone.id">
        <a>{{ zone.name }}</a>
      </div>
      <div class="zone" v-for="resource in resourceStore.resources" :key="resource.type">
        <a>{{ resource.type }}/{{ resource.count }}</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useZoneStore } from '@/stores/zoneStore';
import { useResourceStore } from '@/stores/resourceStore';

// Create reactive references
const zoneStore = useZoneStore();
const resourceStore = useResourceStore();


// Lifecycle hooks
onMounted(async () => {
  await zoneStore.fetchAllZones();
  await resourceStore.getResources();
});

</script>

<style scoped>
.zone-chooser {
  text-align: center;
}
</style>
