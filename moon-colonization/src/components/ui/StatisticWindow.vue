<script setup lang="ts">
const emit = defineEmits(['toggle']); 
import { statisticStore } from '@/stores/StatisticStore';
import { onMounted, computed } from 'vue';
import ResourceAll from './ResourceAll.vue';
import ResourceProduction from './ResourceProduction.vue';
import AreaProduction from './AreaProduction.vue';

const statistic = statisticStore();


// Lifecycle hooks
onMounted(async () => {
  await statistic.getStatistic();
});

// Вычисляемые свойства для безопасного доступа
const productionData = computed(() => {
  return statistic.statistic?.sumProduction || [];
});

const consumptionData = computed(() => {
  return statistic.statistic?.sumConsumption || [];
});

const hasProductionData = computed(() => {
  return productionData.value.length > 0;
});
</script>

<template>
    <div class="statistic">
        <div class="header-block-container">
            <div class="Close-container" @click="$emit('toggle')">
                <div class="close">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M1.43869 16H0V14.5574L6.7139 8.13115L0 1.44262V0H1.43869L7.93461 6.73224H8.10899L14.6049 0H16V1.44262L9.3297 8.13115L16 14.5574V16H14.6049L8.10899 9.61749H7.93461L1.43869 16Z" fill="black"/>
                    </svg>
                </div>
            </div>
            <p>Статистика</p>
        </div>
        <div class="Line-container">
            <div class="line"></div>
        </div>
        <div class="information-container">
            <div class="Resource-container">
                <p>Всего у вас ресурсов:</p>
                <div v-if="statistic" class="resource-container-container">
                    <ResourceAll v-for="(item, index) in statistic?.statistic?.countResources || []"
                            :key="index"
                            :id="index"
                            :data="item"
                            />
                </div>
                <div v-else>
                    Данные загружаются...
                </div>
            </div>
            <div class="Area-all-container">
                <p>Всего потребление и восполнение ресурсов:</p>
                <div v-if="statistic" class="replinishment-consuption">
                    <ResourceProduction
                        v-for="(item, index) in productionData"
                        :key="index"
                        :id="index"
                        :prod="item"
                        :cons="consumptionData[index]"
                    />
                </div>
            </div>
            <div class="Area-specific-container">
                <div class="paragraph">
                    <p>Потребности по областям</p>
                </div>
                <div v-if="statistic">
                    <AreaProduction 
                        v-for="(item, index) in statistic.statistic?.zoneProductions"
                        :key="index"
                        :data="item"/>
                </div>
            </div>
        </div>
        <div class="Line-container">
            <div class="line"></div>
        </div>
    </div>
</template>

<style scoped>
p {
    margin: 0px
}
.statistic {
    display: flex;
    width: 868px;
    height: 98vh;
    padding-bottom: 24px;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    flex-shrink: 0;
    background: #464646;
}
.header-block-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: -12px;
    align-self: stretch;
}
.header-block-container p {
    color: #BCFE37;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 40px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 2px;
}
.Close-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    cursor: pointer;
}
.close {
    display: flex;
    width: 30px;
    height: 30px;
    padding: 7px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
    background: #BCFE37;
}
.close svg {
    width: 16px;
    flex: 1 0 0;
    fill: #000;
}
.Line-container {
    display: flex;
    padding: 0px 25px;
    flex-direction: column;
    align-items: flex-start;
    align-self: stretch;
}
.line {
    width: 818px;
    height: 1px;
    background: #A3A3A3;
}
.information-container {
    height: 660px;
    display: flex; 
    padding: 10px 50px 0px 50px;
    flex-direction: column;
    align-items: flex-start;
    gap: 51px;
    flex: 1 0 0;
    align-self: stretch;
    overflow: auto;   
    overflow-y: auto;  /* Включаем прокрутку */
    scrollbar-width: none; /* Firefox */
}
.information-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Edge */   
}
.Resource-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 24px;
    align-self: stretch;
}
.Resource-container p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1px;
}
.resource-container-container {
    /* display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 24px;
    align-self: stretch;
    flex-wrap: wrap; */
    margin: auto;
    display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 колонки */
  gap: 16px; /* Отступ между элементами */
}
.replinishment-consuption{
    margin: auto;
    display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 колонки */
  gap: 16px; /* Отступ между элементами */
}
.Area-all-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 22px;
    align-self: stretch;
}
.Area-all-container p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1px;
}
.Area-specific-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 25px;
    align-self: stretch;
}
.paragraph {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
}
.paragraph p {
    align-self: stretch;
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