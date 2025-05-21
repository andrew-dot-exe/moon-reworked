<script setup lang="ts">
const emit = defineEmits(['toggle']); 
import { statisticStore } from '@/stores/StatisticStore';
import ResourceAll from './ResourceAll.vue';
import ResourceProduction from './ResourceProduction.vue';
import { onMounted, ref, computed } from 'vue';
import { useSuccessStore } from '@/stores/SuccessStore'
import { userInfoStore } from '@/stores/userInfoStore';

const statistic = statisticStore();
const successStore = useSuccessStore()
const uInfo = userInfoStore();


// Lifecycle hooks
onMounted(async () => {
  await statistic.getStatistic();
});
onMounted(async () => {
    try {
        await statistic.getStatistic();
        await successStore.getSuccess();
    
        await uInfo.fetchUserInfo();
  } catch (error) {
    console.error('Initialization error:', error);
  }
    
})

// Вычисляемые свойства для безопасного доступа
const productionData = computed(() => {
  return statistic.statistic?.sumProduction || [];
});

const consumptionData = computed(() => {
  return statistic.statistic?.sumConsumption || [];
});

const progressbar = ref(20)

const successColor = ref(0)
const success = computed(() => {
    if (!successStore.success || 
      successStore.success.successful === undefined) {
        return "Нет данных";
    }
    const { successful } = successStore.success
    if(successful > 60) successColor.value = 2
    else if(successful > 20) successColor.value = 1
    else successColor.value = 0
    progressbar.value = 274 - 274 * successful / 100
  return `${successful}%`;
});
</script>

<template>
    <div class="statistic">
        <div class="header-block-container">
            <div class="Close-container">
                <div class="close" @click="$emit('toggle')">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M1.43869 16H0V14.5574L6.7139 8.13115L0 1.44262V0H1.43869L7.93461 6.73224H8.10899L14.6049 0H16V1.44262L9.3297 8.13115L16 14.5574V16H14.6049L8.10899 9.61749H7.93461L1.43869 16Z" fill="black"/>
                    </svg>
                </div>
            </div>
            <p>Вы завершили колонизацию</p>
        </div>
        <div class="Line-container">
            <div class="line"></div>
        </div>

        <!--Здесь полоска успешности-->
        <div class="info-container">
            <div class="text-value">
                <p class="heading">Успешность вашей колонизации</p>
                <p class="proccent" :class="{good: successColor == 2, normal: successColor == 1, bad: successColor == 0 }">{{ success }}</p>
            </div>
            <div class="way" :style="{ 'padding-right': progressbar + 'px' }">
                <div class="passed-way" :class="{good: successColor == 2, normal: successColor == 1, bad: successColor == 0 }"></div>
            </div>
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
            <!--пдф + начать заново-->
        </div>
        <div class="Line-container">
            <div class="line"></div>
        </div>
    </div>
</template>

<style scoped>

.good{
    color: #BCFE37;
}
.normal{
    color: #FFD335;
}
.bad{
    color: #FF2C2C;
}

.info-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 6px;
    align-self: stretch;
}
.text-value {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
.heading {
    width: 90px;
    flex-shrink: 0;
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
.proccent {
    width: 27px;
    flex-shrink: 0;
    color: #BCFE37;
    text-align: right;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
.way {
    display: flex;
    height: 5px;
    
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;
    background: #FFF;
    box-shadow: 0px 0px 3px 2px rgba(255, 255, 255, 0.20);
}
.passed-way {
    height: 5px;
    flex-shrink: 0;
    align-self: stretch;
    box-shadow: 0px 0px 3px 2px rgba(188, 254, 55, 0.20);
}
.passed-way.good{
    background: #BCFE37;
}
.passed-way.normal{
    background: #FFD335;
}
.passed-way.bad{
    background: #FF2C2C;
}
p {
    margin: 0px
}
.statistic {
    display: flex;
    max-width: 868px; 
    width: 98vw;
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
    cursor: pointer;
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
    width: 100%;
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

</style>