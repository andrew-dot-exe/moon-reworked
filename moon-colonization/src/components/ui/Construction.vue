<script setup lang="ts">
const emit = defineEmits(['select-module']);
import ModuleComponent from '@/components/ui/ModuleComponent.vue';
import { typeModulesStore } from '@/stores/typeModulesStore';
import { TypeModule } from '@/components/typeModules/typeModules';
import { onMounted, ref } from 'vue';
import type { Module } from '@/components/modules/modules'

const typeStore = typeModulesStore();
const isLoaded = ref(false)

const visibleItems = ref([] as TypeModule[]);
const liveItems = ref([] as TypeModule[]);
const techItems = ref([] as TypeModule[]);

const currentPage = ref(1);
const totalPages = ref(1);
const activeHabitable = ref(true); // По умолчанию активна первая вкладка

const screenWidth = ref(window.innerWidth)

const handleResize = () => {
  screenWidth.value = window.innerWidth
  // Ваша логика при изменении размера
  couningVisible();
}

onMounted(async () => {
  window.addEventListener('resize', handleResize)
  await typeStore.getTypeModules();
  isLoaded.value = true;
  liveItems.value.length = 0;
  techItems.value.length = 0;
  for(let i = 0; i < typeStore.typeModules.length; i++){
    if(typeStore.typeModules[i].live){
        liveItems.value.push(typeStore.typeModules[i]);
    }
    else{
        techItems.value.push(typeStore.typeModules[i]);
    }
  }
  couningVisible();
});

const couningVisible = () => {
  
  const count = Math.floor((screenWidth.value - 110) / 400);

  const typeModules = activeHabitable.value ? liveItems : techItems;

  const l = typeModules.value.length;
  totalPages.value = Math.ceil(l / count);
  if(currentPage.value > totalPages.value) currentPage.value = totalPages.value;
  visibleItems.value.length = 0;
  for(let i = (currentPage.value - 1) * count; i < currentPage.value * count; i++){
    if(i < l){
        visibleItems.value.push(typeModules.value[i]);
    }
  }
}

const prevPage = () => {
  currentPage.value = currentPage.value > 1 ? currentPage.value - 1 : 1;
  couningVisible();
};

const nextPage = () => {
  currentPage.value = currentPage.value < totalPages.value ? currentPage.value + 1 : totalPages.value;
  couningVisible();
};

const setActiveTab = (flag : boolean) => {
    if(activeHabitable.value != flag){
        activeHabitable.value = flag;
        currentPage.value = 1;
        couningVisible();
    }
};

const direction = ref("left")

const open = ref(true)

const opening = () => {
    open.value = !open.value;
    if(open.value){
        direction.value = "left";
    }
    else{
        direction.value = "right";
    }
}
opening();

async function onModuleSelect(module: Module){
    emit('select-module', module)
}

</script>

<template>
    <div class="Construction">
        <div class="master-construction">
            <div class="button">
                <div class="Arrows" @click="opening"> 
                    <div class="arrow-left">
                        <svg class="arrow-icon" :class="direction" xmlns="http://www.w3.org/2000/svg" width="12" height="8" viewBox="0 0 12 8" fill="none">
                            <path d="M5.73166 8L2.12522 4.48157V3.51843L5.73166 0H6.44007V0.648649L3.45617 3.4398V3.55774H12V4.44226H3.45617V4.54054L6.44007 7.3317V8H5.73166ZM0 8V0H0.880143V8H0Z" fill="#A3A3A3"/>
                        </svg>
                    </div>
                </div>
                <div class="build">
                    <p>Построить</p>
                </div>
            </div>
            <div v-if="open" class="main-container">
                <div class="section">
                    <div class="section-section">
                        <div class="section-section">
                            <div 
                            :class="{ 'select': activeHabitable == true, 'default': activeHabitable == false }"
                            @click="setActiveTab(true)"
                            >
                                <p class="ss-p">Обитаемые модули</p>
                            </div>
                            <svg xmlns="http://www.w3.org/2000/svg" width="2" height="30" viewBox="0 0 2 30" fill="none">
                                <path d="M0 30V0H2V30H0Z" fill="#464646"/>
                            </svg>
                            <div 
                            :class="{ 'select': activeHabitable == false, 'default': activeHabitable == true }"
                            @click="setActiveTab(false)"
                            >
                                <p class="ss-p">Технологические модули</p>
                            </div>
                            <svg xmlns="http://www.w3.org/2000/svg" width="2" height="30" viewBox="0 0 2 30" fill="none">
                                <path d="M0 30V0H2V30H0Z" fill="#464646"/>
                            </svg>
                        </div>
                    </div>
                </div>
                <div class="object-container" v-if="{ isLoaded }" >
                    <div class="object-container-container">
                        <div class="object-object" id="cont">
                            <ModuleComponent 
                            v-for="(item, index) in visibleItems" 
                            :key="index"
                            :data="item"
                            @select-module="onModuleSelect"
                            />
                        </div>
                    </div>
                    <div class="pages" v-if="totalPages > 1">
                        <div class="pages-pages">
                            <p class="pp-left" @click="prevPage" :class="{ disabled: currentPage === 1 }" ><</p>
                            <div class="pages-container">
                                <p>{{ currentPage }}</p>
                                <p>/</p>
                                <p>{{ totalPages }}</p>
                            </div>
                            <p class="pp-rigth" @click="nextPage" :class="{ disabled: currentPage === totalPages }">></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.arrow-icon.left {
  transform: rotate(0deg);
}
.arrow-icon.right {
  transform: rotate(180deg);
}
p {
    margin: 0px;
}
.Construction {
    display: flex;
    flex-shrink: 1;
    align-items: flex-start;
    gap: 10px;
}
.master-construction {
    display: flex;
    align-items: center;
    flex: 1 0 0;
}
.button {
    display: flex;
    width: 30px;
    height: 196px;
    flex-direction: column;
    align-items: flex-start;
}
.Arrows {
    display: flex;
    padding: 8px;
    align-items: center;
    gap: 10px;
    background: #464646;
}
.arrow-left {
    display: flex;
    padding: 3px 1px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.arrow-left svg {
    width: 12px;
    height: 8px;
    flex-shrink: 0;
    aspect-ratio: 3/2;
    fill: #A3A3A3;
}
.build {
    background: rgba(0, 0, 0, 0.80);
    display: flex;
    padding: 9px 13px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    align-self: stretch;
}
.build p {
    transform: rotate(-90deg);
    color: #A3A3A3;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.75px;
}
.main-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    flex: 1 0 0;
}
.section {
    display: flex;
    align-items: center;
    gap: 150px;
    align-self: stretch;
}
.section-section {
    display: flex;
    align-items: center;
}
.section-section svg {
        width: 2px;
align-self: stretch;
fill: #464646;
}

.select {
    background: #BCFE37;
    display: flex;
    padding: 7px 22px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    color: #000;
}
.default {
    background: rgba(0, 0, 0, 0.80);
    display: flex;
    padding: 7px 22px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    color: #464646;
}

.ss-p {
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 750;
    line-height: normal;
    letter-spacing: 0.75px;
}

.object-container {
    position: relative; /* нужно для псевдоэлемента */
    display: flex;
    padding: 10px 20px;
    flex-direction: column;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    background: rgba(0, 0, 0, 0.80);
    /* убираем outline или border */
}

.object-container::before {
    content: "";
    position: absolute;
    inset: 0; /* растягивается на весь элемент */
    border: 1px solid #BCFE37; /* внутренняя обводка */
    pointer-events: none; /* чтобы не мешала взаимодействию */
    box-sizing: border-box;
    z-index: 1; /* поверх фона, но ниже контента */
    border-radius: inherit; /* если вдруг контейнер скругляется */
}
.object-container-container {
    display: flex;
align-items: center;
gap: 35px;
align-self: stretch;
}
.object-object {
    display: flex;
height: 120px;
align-items: center;
gap: 35px;
flex: 1 0 0;
    width: 90vw;
}

.pages {
    position: relative;
    top: -10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-end;
    gap: 10px;
    align-self: stretch;
}
.pages-pages {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 15px;
}
.pages-pages p {
    color: #BCFE37;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 24px;
    font-style: normal;
    font-weight: 127;
    line-height: normal;
    letter-spacing: 1.2px;
    height: 16px;
}
.pp-left {
    color: #BCFE37;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 24px;
    font-style: normal;
    font-weight: 127;
    line-height: normal;
    letter-spacing: 1.2px;
}
.pp-rigth {
    color: #BCFE37;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 24px;
    font-style: normal;
    font-weight: 127;
    line-height: normal;
    letter-spacing: 1.2px;
}
.pages-container {
    display: flex;
    align-items: center;
    gap: 5px;
}
.pages-container p {
    color: #BCFE37;
    /* leading-trim: both;
    text-edge: cap; */
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 24px;
    font-style: normal;
    font-weight: 95;
    line-height: normal;
    letter-spacing: 1.2px;
}


.disabled {
  color: #464646;;
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

.object-object {
  display: flex;
  height: 120px;
  align-items: center;
  gap: 35px;
  flex: 1 0 0;
  overflow: hidden; /* Скрываем элементы, которые не помещаются */
}

.pp-left, .pp-rigth {
  cursor: pointer;
  user-select: none;
}

.pp-left:hover:not(.disabled), 
.pp-rigth:hover:not(.disabled) {
  opacity: 0.8;
}
</style>