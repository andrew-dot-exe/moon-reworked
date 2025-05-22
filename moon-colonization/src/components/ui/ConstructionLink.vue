<script setup lang="ts">
import { ref } from 'vue';
const emit = defineEmits(['select-link1', 'select-link2', 'open-fun', 'build-b']);

const direction = ref("left")

const open = ref(true)

const opening = () => {
    open.value = !open.value;
    if(open.value){
        direction.value = "left";
    }
    else{
        direction.value = "right";
        selectL.value = 0
    }
    emit("open-fun")
}

const selectL = ref(0)
const pressL1 = () => {
    if(selectL.value == 1) selectL.value = 0
    else selectL.value = 1
    emit('select-link1')
}
const pressL2 = () => {
    if(selectL.value == 2) selectL.value = 0
    else selectL.value = 2
    emit('select-link2')
}
opening();

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
                            <div class="select">
                                <p class="ss-p">Связи</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="object-container" >
                    <div class="object-container-container">
                        <div class="object-object" id="cont">

                            <div class="object" @click="pressL2" :class="{select: selectL == 2}">
                                <div class="master-object-block">
                                <div class="icon">
                                    <div class="icon-object">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="90" height="53" viewBox="0 0 90 53" fill="none">
                                            <path d="M11.4042 42.0172L71.8767 7.10346L78.5958 10.9828L18.1233 45.8966L11.4042 42.0172Z" fill="#ff4f00" fill-opacity="0.7"/>
                                        </svg>
                                    </div>
                                </div>
                                <div class="information-container">
                                    <div class="Heading">
                                    <h1>Маршрут</h1>
                                    <div class="line"></div>
                                    </div>
                                    <div class="Paragraph">
                                    <div class="Built-all">
                                        <p class="Built">0</p>
                                        <!--<p class="All">/</p>
                                                    <p class="All">0</p>-->
                                    </div>
                                    <div class="Cost-container">
                                        <div class="Text">
                                        <div class="Number">
                                            <p class="number">0.0012</p>
                                            <div class="Unit">
                                            <p class="unit">кВт*ч/м</p>
                                            </div>
                                        </div>
                                        <div class="Icon">
                                            <div class="icon-material">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="15" viewBox="0 0 22 15" fill="none">
                                                <path d="M4.83333 4.0625H2.5V0L0.166664 5.9375H2.5V10L4.83333 4.0625Z" fill="white"/>
                                            </svg>
                                            </div>
                                        </div>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                                <div class="Docs">
                                    <div class="question">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="16" viewBox="0 0 10 16" fill="none">
                                        <path
                                        d="M5.13171 11.4853H4.38537L4.1 10.4485V7.44853H6.38293L8.18293 5.59559V3.67647L6.38293 1.82353H3.61707L1.81707 3.67647V5.44118H0.5V3.14706L3.06829 0.5H6.93171L9.5 3.14706V6.125L6.93171 8.77206H5.41707V10.4485L5.13171 11.4853ZM5.72439 15.5H3.83659V13.4706H5.72439V15.5Z"
                                        fill="#464646" />
                                    </svg>
                                    </div>
                                </div>
                                </div>
                            </div>

                            <div class="object"  @click="pressL1" :class="{select: selectL == 1}">
                                <div class="master-object-block">
                                <div class="icon">
                                    <div class="icon-object">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="90" height="53" viewBox="0 0 90 53" fill="none">
                                            <path d="M11.4042 42.0172L71.8767 7.10346L78.5958 10.9828L18.1233 45.8966L11.4042 42.0172Z" fill="#F3FF73" fill-opacity="0.7"/>
                                        </svg>
                                    </div>
                                </div>
                                <div class="information-container">
                                    <div class="Heading">
                                    <h1>Электрический провод</h1>
                                    <div class="line"></div>
                                    </div>
                                    <div class="Paragraph">
                                    <div class="Built-all">
                                        <p class="Built">0</p>
                                        <!--<p class="All">/</p>
                                                    <p class="All">0</p>-->
                                    </div>
                                    <div class="Cost-container">
                                        <div class="Text">
                                        <div class="Number">
                                            <p class="number">1</p>
                                            <div class="Unit">
                                            <p class="unit">кг/м</p>
                                            </div>
                                        </div>
                                        <div class="Icon">
                                            <div class="icon-material">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="15" viewBox="0 0 22 15" fill="none">
                                                <path d="M9.83132 0L1.5703 4.3908L5.34421 6.38307L13.5804 2.03326L9.83135 0H9.83132ZM11.314 1.54573L12.2017 1.99645L5.35829 5.59183L3.03885 4.35735L3.91786 3.89359L5.35833 4.66116L11.3141 1.54577L11.314 1.54573ZM13.9641 2.56184L5.59789 6.9808L5.39735 9.86485L14.8333 4.85567L13.9641 2.56184ZM1.16596 4.90973L0.166666 7.57324L4.73944 10L4.95292 6.90843L1.16592 4.90977L1.16596 4.90973Z" fill="white"/>
                                            </svg>
                                            </div>
                                        </div>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                                <div class="Docs">
                                    <div class="question">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="16" viewBox="0 0 10 16" fill="none">
                                        <path
                                        d="M5.13171 11.4853H4.38537L4.1 10.4485V7.44853H6.38293L8.18293 5.59559V3.67647L6.38293 1.82353H3.61707L1.81707 3.67647V5.44118H0.5V3.14706L3.06829 0.5H6.93171L9.5 3.14706V6.125L6.93171 8.77206H5.41707V10.4485L5.13171 11.4853ZM5.72439 15.5H3.83659V13.4706H5.72439V15.5Z"
                                        fill="#464646" />
                                    </svg>
                                    </div>
                                </div>
                                </div>
                            </div>
                            
                            <div class="build-button" @click="emit('build-b')"><div class="text-build"><p>Построить</p></div></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.build-button p {
    flex-grow: 1;
    
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 750;
    line-height: normal;
    letter-spacing: 0.75px;
}
.build-button{
    display: flex;
    flex-direction: column;
  height: 40px;
    justify-content: center;
    align-items: center;
    background-color: #BCFE37;
    cursor: pointer;
    border-radius: 5px;
}
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
    pointer-events: none;
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
    pointer-events: auto;
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
    cursor: pointer;
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
    font-weight: 350;
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
    pointer-events: auto;
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
    pointer-events: auto;
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


.object {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  flex: 1 0 0;
}
.object.select {
  background-color: #BCFE37;
}

.master-object-block {
  background: #464646;
  display: flex;
  min-width: 400px;
  padding: 10px;
  align-items: center;
  gap: 10px;
  flex: 1 0 0;
  align-self: stretch;
}

.icon {
  border: 1px solid #BCFE37;
  background: #464646;
  display: flex;
  width: 100px;
  height: 100px;
  padding: 5px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  aspect-ratio: 1/1;
}

.icon-object {
  display: flex;
  height: 51.724px;
  justify-content: center;
  align-items: center;
  flex: 1 0 0;
}

.icon-object img {
  width: 90px;
  height: 51.724px;
}

.information-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  flex: 1 0 0;
}

.Heading {
  display: flex;
  height: 45px;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  align-self: stretch;
}

.Heading h1 {
  color: #FFF;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 16px;
  font-style: normal;
  font-weight: 200;
  line-height: normal;
  letter-spacing: 0.8px;
}

.line {
  width: 260px;
  height: 1px;
  background: #FFF;
}

.Paragraph {
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
}

.Built-all {
  display: flex;
  align-items: center;
  gap: 4px;
}

.Built {
  color: #BCFE37;
  /* leading-trim: both;
    text-edge: cap; */
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 30px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.5px;
}

/*
.All {
    color: #9C9C9C;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 30px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 1.5px;
}*/
.Cost-container {
  border: 1px solid #FFF;
  display: flex;
  padding: 5px;
  flex-direction: column;
  align-items: flex-start;
  gap: 5px;
}

.Text {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.Number {
  display: flex;
  align-items: flex-end;
  gap: 5px;
}

.number {
  color: #FFF;
  /* leading-trim: both;
    text-edge: cap; */
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.2px;
}

.Unit {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.unit {
  color: #FFF;
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

.Icon {
  position: relative;
  top: -10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}

.icon-material {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.icon-material svg {
  width: 26px;
  height: 18px;
  fill: #FFF;
}

.Docs {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 10px;
  align-self: stretch;
}

.question {
  display: flex;
  width: 20px;
  height: 20px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex: 1 0 0;
  background: #BCFE37;
}

.question svg {
  width: 9px;
  height: 15px;
  flex-shrink: 0;
  aspect-ratio: 3/5;
  fill: #464646;
}
</style>