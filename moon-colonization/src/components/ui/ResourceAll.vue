
<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  data: { type: Number, required: true }
});
const name = ["Вода", "Топливо", "Электроэнергия", "Провизия", 
"Кислород", "Углекислый газ", "Мусор", "Материалы"];

const iconPath = computed(() => {
  return `textures/icons/sprite.svg#icon-${name[props.id]}`;
});
</script>

<template>
    <div class="resource">
        <svg xmlns="http://www.w3.org/2000/svg">
            <use :xlink:href="iconPath"  width="148%" height="148%"/>
        </svg>
        <div class="information">
            <p>{{ name[id] }}</p>
            <div class="value-container">
                <p>{{ data }}</p>
                <p>кг</p>
            </div>
        </div>
    </div>
</template>

<style scoped>


.resource {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 14px;
    align-self: stretch;
    flex-wrap: nowrap;
}
.resource svg {
    width: 45px;
    height: 40px;
    flex-shrink: 0;
    aspect-ratio: 1/1;
    filter: #A3A3A3;
}
.information {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
}
.information p {
    color: #A3A3A3;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 10px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.5px;
} 
.value-container {
    display: flex;
    align-items: flex-start;
    gap: 5px;
}
.value-container p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1px;
}
</style>