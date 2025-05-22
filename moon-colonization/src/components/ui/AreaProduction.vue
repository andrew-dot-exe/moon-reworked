<script setup lang="ts">
import ResourceProduction from './ResourceProduction.vue';
import {ZoneProduction} from '@/components/statistic/ZoneProduction.ts';
import { ref } from 'vue';
defineProps({
  data: {type: ZoneProduction, required: true }
});


const direction = ref("up")

const open = ref(true)

const opening = () => {
    open.value = !open.value;
    if(open.value){
        direction.value = "up";
    }
    else{
        direction.value = "down";
    }
}
opening();
const name = ["Равнина 1", "Равнина 2", "Высота 1", "Высота 2", "Низина 1", "Низина 2"]
</script>

<template>
    <div class="statistic-area-master">
        <div class="heading-container-area">
            <div class="name-area-container">
                <p>{{ name[data.id] }}</p>
            </div>
            <div class="arrows" @click="opening">
                <div class="master-arrowsUp">
                    <svg class="arrow-icon" :class="direction" width="30" height="30" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M7.48157 3.12522L11 6.73166V7.44007H10.3317L7.54054 4.45617H7.44226V13H6.55774V4.45617H6.4398L3.64865 7.44007H3V6.73166L6.51843 3.12522H7.48157ZM3 1H11V1.88014H3V1Z" fill="#464646"/>
                    </svg>
                </div>
            </div>
        </div>
        <div v-if="open" class="information-container-specific">
            <p>Всего потребление и воспалнение ресурсов в области:</p>
            <div class="replinishment-consuption">
                <ResourceProduction
                    v-for="(item, index) in data.production"
                    :key="index"
                    :id="index"
                    :prod="item"
                    :cons="data.consumption[index]"
                />
            </div>
        </div>
    </div>
</template>

<style scoped>
.arrow-icon.up {
  transform: rotate(0deg);
}
.arrow-icon.down {
  transform: rotate(180deg);
}
.statistic-area-master {
    display: flex;
    /* height: 260px; */
    width: 743px;
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    align-self: stretch;
    margin: 5px;
}

.heading-container-area {
    display: flex;
    align-items: center;
    align-self: stretch;
}
.name-area-container {
    display: flex;
    height: 40px;
    padding: 13px 16px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    border: 1px solid #A3A3A3;
}
.name-area-container p {
    color: #A3A3A3;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1px;
}
.arrows {
    display: flex;
    width: 40px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    aspect-ratio: 1/1;
    background: #A3A3A3;
    cursor: pointer;
}
.master-arrowsUp {
    display: flex;
    padding: 1px 3px;
    justify-content: center;
    align-items: center;
    flex-shrink: 0;
}

.information-container-specific {
    display: flex;
    padding: 0px 40px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;
}
.information-container-specific p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1px;
}
.replinishment-consuption {
    margin: auto;
    display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 колонки */
  gap: 16px; /* Отступ между элементами */
}
</style>