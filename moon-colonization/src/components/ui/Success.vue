<script setup lang="ts">
import { onMounted, onUnmounted, ref, computed, watch } from 'vue';
import { useDayStore } from '@/stores/dayStore'
import { storeToRefs } from 'pinia'
import { useSuccessStore } from '@/stores/SuccessStore'
import { userInfoStore } from '@/stores/userInfoStore';

const dayStore = useDayStore()
const { speed } = storeToRefs(dayStore)
const { setSpeed } = dayStore

const successStore = useSuccessStore()
const uInfo = userInfoStore();

const direction = ref("down")

const open = ref(false)

const opening = () => {
    open.value = !open.value;
    if(open.value){
        direction.value = "up";
    }
    else{
        direction.value = "down";
    }
}

const progressbar = ref(20)
const day = ref(0)

const loaded = ref(false)


// Функция для отслеживания изменений
const setupWatchers = () => {
  const unwatch = watch(
    () => uInfo.userInfo.currentDay,
    (neCurrentDay) => {
      day.value = neCurrentDay;
    }
  );
  
  onUnmounted(() => unwatch());
};

onMounted(async () => {
    try {
        await successStore.getSuccess();
    
        await uInfo.fetchUserInfo();
        if(successStore.success && uInfo.userInfo){
            loaded.value = true
        }
        day.value = uInfo.userInfo.currentDay;
        
        
        setupWatchers(); // Инициализируем отслеживание изменений
  } catch (error) {
    console.error('Initialization error:', error);
  }
    
})
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
const moodColor = ref(0)
const mood = computed(() => {
    if (!successStore.success || 
      successStore.success.mood === undefined) {
        return "Нет данных";
    }
    if(successStore.success?.mood > 60) moodColor.value = 2
    else if(successStore.success?.mood > 20) moodColor.value = 1
    else moodColor.value = 0
  return `${successStore.success?.mood}%`;
});
const countPeopleColor = ref(0)
const countPeople = computed(() => {
    // Проверяем наличие необходимых данных
  if (!successStore.success || 
      successStore.success.contPeople === undefined || 
      successStore.success.needContPeople === undefined ||
      successStore.success.needContPeople === 0) {
    return "Нет данных";
  }

  const { contPeople, needContPeople } = successStore.success;
  
  // Вычисляем процент свободных людей
  const free = ((contPeople - needContPeople) / needContPeople) * 100;
    if(free < 10){
        countPeopleColor.value = 0
        return "Мало"
    }
    if(free < 15) {
        countPeopleColor.value = 1
        return "Нормально"
    }
    countPeopleColor.value = 2
    return "Достаточно"
});

const resColor = ref(0)
const res = computed(() => {
    if (!successStore.success || 
      successStore.success.resources === undefined) {
        return "Нет данных";
    }
    const { resources } = successStore.success
    if(resources < 10) {
        resColor.value = 0
        return "Плохое"
    }
    if(resources < 40) {
        resColor.value = 1
        return "Нормальное"
    }
    if(resources < 70) {
        resColor.value = 1
        return "Хорошее"
    }
    resColor.value = 2
    return "Отличное"
  
});

const centrColor = ref(0)
const centr = computed(() => {
    if (!successStore.success || 
      successStore.success.central === undefined) {
        return "Нет данных";
    }
    const { central } = successStore.success
    if(central <= 2) {
        centrColor.value = 0
        return "Плохая"
    }
    if(central <= 4) {
        centrColor.value = 1
        return "Пормальная"
    }
    centrColor.value = 2
    return "Хорошая"
  
});

const serchColor = ref(0)
const serch = computed(() => {
    if (!successStore.success || 
      successStore.success.search === undefined) {
        return "Нет данных";
    }
    const { search } = successStore.success
    if(search < 1) {
        serchColor.value = 0
        return "Нет"
    }
    if(search < 3) {
        serchColor.value = 1
        return "Средний"
    }
    serchColor.value = 2
    return "Оптимальный"
});

const curDate = computed(() => {
    const date = new Date(2055, 3, 9 + day.value)
  const dayd = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return `${dayd}.${month}.${year}`;
})

</script>

<template>
    <div class="Success">
        <div class="master-success">
            <div class="Rewind-container">
                <div class="speed-block" @click="setSpeed(1)">
                    <div class="number">
                        <p :class="{selected: speed === 1, unselected: speed !== 1}">1x</p>
                        <div class="forward">
                            <svg :class="{selected: speed === 1, unselected: speed !== 1}" xmlns="http://www.w3.org/2000/svg" width="5" height="7" viewBox="0 0 5 7" fill="none">
                                <path d="M1 7H0V0H1L5 3.19685V3.81693L1 7Z"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <div class="speed-block" @click="setSpeed(2)">
                    <div class="number">
                        <p :class="{selected: speed === 2, unselected: speed !== 2}">2x</p>
                        <div class="forward">
                            <svg :class="{selected: speed === 2, unselected: speed !== 2}" xmlns="http://www.w3.org/2000/svg" width="9" height="7" viewBox="0 0 9 7" fill="none">
                                <path d="M1.5 7H0.5V0H1.5L5.5 3.19685V3.81693L1.5 7Z"/>
                                <path d="M4.5 7H3.5V0H4.5L8.5 3.19685V3.81693L4.5 7Z"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <div class="speed-block" @click="setSpeed(3)">
                    <div class="number">
                        <p :class="{selected: speed === 3, unselected: speed !== 3}">3x</p>
                        <div class="forward">
                            <svg :class="{selected: speed === 3, unselected: speed !== 3}" xmlns="http://www.w3.org/2000/svg" width="11" height="7" viewBox="0 0 11 7" fill="none">
                                <path d="M1 7H0V0H1L5 3.19685V3.81693L1 7Z"/>
                                <path d="M4 7H3V0H4L8 3.19685V3.81693L4 7Z"/>
                                <path d="M7 7H6V0H7L11 3.19685V3.81693L7 7Z"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <div class="speed-block" @click="setSpeed(0)">
                    <div class="icon">
                        <svg :class="{selected: speed === 0, unselected: speed !== 0}" xmlns="http://www.w3.org/2000/svg" width="5" height="7" viewBox="0 0 5 7" fill="none">
                            <path d="M5 2.03986e-07V7H3.57143L3.57143 1.45705e-07L5 2.03986e-07ZM1.42857 5.82819e-08L1.42857 7H0L3.27835e-07 0L1.42857 5.82819e-08Z"/>
                        </svg>
                    </div>
                </div>
            </div>
            <div class="Time-container">
                <p>Дней {{ day }}</p>
                <p>{{ curDate }}</p>
            </div>
            <div class="Statistic-container">
                <div class="button">
                    <div class="heading-button">
                        <p>Успешность</p>
                    </div>
                    <div class="Arrows" @click="opening">
                        <div class="master-arriwsUp">
                            <svg xmlns="http://www.w3.org/2000/svg" class="arrow-icon" :class="direction" width="8" height="13" viewBox="0 0 8 13" fill="none">
                                <path d="M4.48157 2.62522L8 6.23166V6.94007H7.3317L4.54054 3.95617H4.44226V12.5H3.55774V3.95617H3.4398L0.648649 6.94007H0V6.23166L3.51843 2.62522H4.48157ZM0 0.5H8V1.38014H0V0.5Z" fill="#A3A3A3"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <div v-if="open && loaded" class="statistics">
                    <div class="success-container">
                        <div class="info-container">
                            <div class="text-value">
                                <p class="heading">Успешность</p>
                                <p class="proccent" :class="{good: successColor == 2, normal: successColor == 1, bad: successColor == 0 }">{{ success }}</p>
                            </div>
                            <div class="way" :style="{ 'padding-right': progressbar + 'px' }">
                                <div class="passed-way" :class="{good: successColor == 2, normal: successColor == 1, bad: successColor == 0 }"></div>
                            </div>
                        </div>
                    </div>
                    <div class="criteria-container-parent">
                        <!-- Настроение -->
                        <div class="_criteria">
                            <div class="master-criteria">
                                <div class="criteria-container">
                                    <p>Настроение</p>
                                </div>
                                <div class="value-container">
                                    <p :class="{good: moodColor == 2, normal: moodColor == 1, bad: moodColor == 0 }">{{ mood }}</p>
                                </div>
                            </div>
                        </div>

                        <!-- Кол-во населения -->
                        <div class="_criteria">
                            <div class="master-criteria">
                                <div class="criteria-container">
                                    <p>Кол-во населения</p>
                                </div>
                                <div class="value-container">
                                    <p :class="{good: countPeopleColor == 2, normal: countPeopleColor == 1, bad: countPeopleColor == 0 }">{{ countPeople }}</p>
                                </div>
                            </div>
                        </div>

                        <!-- Состояние ресурсов -->
                        <div class="_criteria">
                            <div class="master-criteria">
                                <div class="criteria-container">
                                    <p>Состояние ресурсов</p>
                                </div>
                                <div class="value-container">
                                    <p :class="{good: resColor == 2, normal: resColor == 1, bad: resColor == 0 }">{{ res }}</p>
                                </div>
                            </div>
                        </div>

                        <!-- Централизация -->
                        <div class="_criteria">
                            <div class="master-criteria">
                                <div class="criteria-container">
                                    <p>Централизация</p>
                                </div>
                                <div class="value-container">
                                    <p :class="{good: centrColor == 2, normal: centrColor == 1, bad: centrColor == 0 }">{{ centr }}</p>
                                </div>
                            </div>
                        </div>

                         <!-- Темп исследования -->
                        <div class="_criteria">
                            <div class="master-criteria">
                                <div class="criteria-container">
                                    <p>Темп исследования</p>
                                </div>
                                <div class="value-container">
                                    <p :class="{good: serchColor == 2, normal: serchColor == 1, bad: serchColor == 0 }">{{ serch }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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
.selected {
    color: #A3A3A3;
    fill: #A3A3A3;
}
.unselected {
    color: #464646;
    fill: #464646;
}
.good{
    color: #BCFE37;
}
.normal{
    color: #FFD335;
}
.bad{
    color: #FF2C2C;
}
p {
    margin: 0px;
}
.Success {
    display: flex;
    width: 308px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
}
.master-success {
    display: flex;
    min-width: 308px;
    flex-direction: column;
    align-items: flex-start;
    align-self: stretch;
}
.Rewind-container {
    display: flex;
    padding: 1px;
    align-items: center;
    gap: 2px;
    align-self: stretch;
}
.speed-block {
    display: flex;
    height: 14px;
    padding: 2px 8px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    background: rgba(0, 0, 0, 0.80);
    cursor: pointer;
}
.number {
    display: flex;
    width: 27px;
    align-items: center;
    gap: 1px;
}
.number p {
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 13px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.65px;
}
.forward {
    display: flex;
    padding: 0px 2px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    align-self: stretch;
}
.forward svg {
    width: 9px;
    height: 5px;
    aspect-ratio: 5/7;
}
.icon {
    display: flex;
    align-items: center;
    gap: 1px;
}
.icon svg {
    width: 5px;
    height: 7px;
    aspect-ratio: 5/7;
}
/* .Time-container {
    display: flex;
    height: 30px;
    padding: 5px;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
    border: 1px solid #FFF;
    background: rgba(0, 0, 0, 0.80);
} */
 .Time-container {
    position: relative; /* для псевдоэлемента */
    display: flex;
    height: 30px;
    padding: 5px;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
    background: rgba(0, 0, 0, 0.80);
    /* убираем стандартную рамку */
    /* border: 1px solid #FFF; */
}

.Time-container::before {
    content: "";
    position: absolute;
    inset: 0;
    border: 1px solid #FFF;
    pointer-events: none;
    box-sizing: border-box;
    z-index: 1;
    border-radius: inherit;
}
.Time-container p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
.Statistic-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    align-self: stretch;
}
.button {
    display: flex;
    height: 30px;
    align-items: center;
    align-self: stretch;
}
/* .heading-button {
    display: flex;
    height: 30px;
    justify-content: space-between;
    align-items: center;
    flex: 1 0 0;
    border: 1px solid #464646;
    background: rgba(0, 0, 0, 0.80);
} */
 .heading-button {
    position: relative; /* для псевдоэлемента */
    display: flex;
    height: 30px;
    justify-content: space-between;
    align-items: center;
    flex: 1 0 0;
    background: rgba(0, 0, 0, 0.80);
    padding-left: 5px;
    /* border удаляем */
    /* border: 1px solid #464646; */
}

.heading-button::before {
    content: "";
    position: absolute;
    inset: 0;
    border: 1px solid #464646;
    pointer-events: none;
    box-sizing: border-box;
    z-index: 1;
    border-radius: inherit;
}

.heading-button p {
    color: #A3A3A3;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 600;
    line-height: normal;
    letter-spacing: 0.75px;
}
.Arrows {
    background: #464646;
    display: flex;
    padding: 8px;
    align-items: center;
    gap: 10px;
    cursor: pointer;
}
.master-arriwsUp {
    display: flex;
    width: 14px;
    height: 14px;
    justify-content: center;
    align-items: center;
}
.master-arriwsUp svg {
    width: 8px;
    height: 12px;
    flex-shrink: 0;
    aspect-ratio: 2/3;
    fill: #A3A3A3;
}
.statistics {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    align-self: stretch;
}
.success-container {
    display: flex;
    padding: 10px 16px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    border: 1px solid #464646;
    background: rgba(0, 0, 0, 0.80);
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
.criteria-container-parent {
    display: flex;
    padding: 8px 4px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 5px;
    align-self: stretch;
    border: 1px solid #464646;
    background: rgba(0, 0, 0, 0.80);
}
._criteria {
    display: flex;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;
}
.master-criteria {
    display: flex;
    height: 22px;
    justify-content: space-between;
    align-items: center;
    flex: 1 0 0;
}
.criteria-container {
    display: flex;
    min-width: 180px;
    padding: 2px 5px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    align-self: stretch;
    background: #464646;
}
.criteria-container p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
.value-container {
    display: flex;
    padding: 2px 5px;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    align-self: stretch;
    border: 1px solid #464646;
}
.value-container p {
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
} 
</style>