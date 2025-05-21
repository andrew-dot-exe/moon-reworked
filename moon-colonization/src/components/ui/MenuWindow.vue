
<script setup lang="ts">
import { onMounted, onUnmounted, ref, computed, watch } from 'vue';
import { userApi } from "@/components/user/userApi"
import { useSuccessStore } from '@/stores/SuccessStore'
import { userInfoStore } from '@/stores/userInfoStore';
import { statisticStore } from '@/stores/StatisticStore';
import { User } from '../user/userInfo';

import {generatePDF} from "@/components/pdf/generate"
const emit = defineEmits(['toggle']); 
const props = defineProps({
  name: { type: String, default: "Anonim" }
});

const successStore = useSuccessStore()
const uInfo = userInfoStore();
const statistic = statisticStore();

onMounted(async () => {
    try {
       
  } catch (error) {
    console.error('Initialization error:', error);
  }
    
})

const generating = ref(false);

const pdf = async () => {
  if(generating.value) return;
  generating.value = true
  await successStore.getSuccess();
  await statistic.getStatistic();
  await uInfo.fetchUserInfo();
  // Проверяем, что все данные загружены и определены
  if (successStore?.success && uInfo?.userInfo && statistic?.statistic) {
    const userData = new User(
      uInfo.userInfo.name,
       uInfo.userInfo.currentDay,
       uInfo.userInfo.dayBeforeDelivery,
      uInfo.userInfo.live,
       uInfo.userInfo.links,
       uInfo.userInfo.modules
  );
  
      generatePDF(successStore.success, userData, statistic.statistic);
      generating.value = false
  }
  
}

const endColonisation = () => {
// возможно стоит колбэк сделать
}

const exit = async () => {
  await userApi.logout()
}
</script>

<template>
  <div class="menu-container">
      <div class="main-container">
          <div class="account-container">
              <div class="icon">
                  <p>{{ name[0] }}</p>
              </div>
              <div class="user-container">
                  <p class="user-name">{{ name }}</p>
                  <p class="exit" @click="exit">Выйти</p>
              </div>
              </div>
              <div class="points-container">
              <div class="points">
                  <div class="end" @click="endColonisation">
                      <p>Завершить колонизацию</p>
                  </div>
                  <a class="text-points">Обучение</a>
                  <p class="text-points" @click="pdf">Экспорт в PDF</p>
                  <a class="link-to">
                      <p>Документация</p>
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                          <path d="M0 17.3333L14.0596 3.72223L13.7609 3.42356L0 2.66667V1.33333V0L17.8567 0.351562L19.8193 2.31423L20 19.9996H18.6667H17.3335L16.7474 6.41023L16.4488 6.11156L2.66654 20L0 17.3333Z" fill="white"/>
                      </svg>
                  </a>
                  <a class="link-to">
                      <p>Вернуться на сайт-визитку</p>
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                          <path d="M0 17.3333L14.0596 3.72223L13.7609 3.42356L0 2.66667V1.33333V0L17.8567 0.351562L19.8193 2.31423L20 19.9996H18.6667H17.3335L16.7474 6.41023L16.4488 6.11156L2.66654 20L0 17.3333Z" fill="white"/>
                      </svg>
                  </a>
              </div>
              <div class="footer">
                  <div class="logo-container">
                      <div class="line"></div>
                      <div class="logo">
                          <svg xmlns="http://www.w3.org/2000/svg" width="470" height="100" viewBox="0 0 470 100" fill="none">
                              <path d="M392.377 0L440.296 56.5826H440.576V0H470V100H440.576L392.658 43.4174H392.377V100H362.954V0H392.377Z" fill="#A3A3A3"/>
                              <path d="M0 0H30.3794L63.4187 63.7255H63.6987L96.738 0H127.117V100H97.718V54.7619H97.438L72.5185 100H54.5989L29.6794 54.7619H29.3994V100H0V0Z" fill="#A3A3A3"/>
                              <path fill-rule="evenodd" clip-rule="evenodd" d="M131.299 49.2281C131.299 80.4004 152.368 99.4043 188.116 99.4043L301.819 100C337.568 100 358.772 81.5918 358.772 50.4195C358.772 19.1128 337.634 0.48362 301.887 0.109061C278.468 -0.136324 245.002 0.109082 245.002 0.109082H188.116C152.368 0.108117 131.299 17.9215 131.299 49.2281ZM188.167 76.6667C171.312 76.6667 161.347 66.1579 161.347 49.2281C161.347 32.1639 171.312 23.3334 188.167 23.3334H301.904C318.895 23.3334 328.868 33.3553 328.868 50.4195C328.868 67.3493 318.895 76.6667 301.904 76.6667H188.167Z" fill="#A3A3A3"/>
                              <path d="M261.762 23.3334H298.559L228.31 76.6667L191.512 76.654L261.762 23.3334Z" fill="#A3A3A3"/>
                          </svg>
                      </div>
                  </div>
                  <div class="link">
                      <p>Разработано командой BFG 10.000 в рамках олимпиады</p>
                      <a class="link-to-hackathon" href="https://challenge.braim.org/landing/moon_contest">
                          <p>Космический кубок: Миссия «ЛУНА»</p>
                          <svg xmlns="http://www.w3.org/2000/svg" width="15" height="16" viewBox="0 0 15 16" fill="none">
                              <path d="M0 13.5L10.5447 3.29167L10.3207 3.06767L0 2.5V1.5V0.5L13.3926 0.763672L14.8645 2.23567L15 15.4997H14H13.0001L12.5606 5.30767L12.3366 5.08367L1.99991 15.5L0 13.5Z" fill="#A3A3A3"/>
                          </svg>
                      </a>
                  </div>
              </div>
          </div>
      </div>
      <div class="close" @click="$emit('toggle')">
          <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 10 10" fill="none">
              <path d="M1.5 8.5L8.5 1.5M1.5 1.5L8.5 8.5" stroke="black" stroke-linecap="square"/>
          </svg>
      </div>
  </div>
</template>

<style scoped>
p {
  margin: 0;
}
a {
  text-decoration: none;
}
.menu-container {
  background: #464646;
  display: flex;
  width: 568px;
  height: 100vh;
  min-height: 730px;
  padding: 12px 20px 24px 50px;
  justify-content: flex-end;
  align-items: flex-start;
  gap: 8px;
}
.main-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 64px;
  flex: 1 0 0;
  align-self: stretch;
}
.account-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}
.icon {
  display: flex;
  width: 50px;
  height: 50px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 25px;
  background: #BCFE37;
}
.icon p {
  color: #000;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 32px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.6px;
}
.user-container {
  display: flex;
  width: 250px;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}
.user-name {
  color: #FFF;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1px;
  align-self: stretch;
}
.exit {
  color:#A3A3A3;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 15px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 0.75px;
  cursor: pointer;
}
.points-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  flex: 1 0 0;
  align-self: stretch;
}
.points {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  align-self: stretch;
}
.end {
  display: flex;
  padding: 10px;
  align-items: center;
  align-self: stretch;
  background: #BCFE37;
  cursor: pointer;
}
.end p {
  color: #000;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.2px;
}
.text-points {
  color: #FFF;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.2px;
  align-self: stretch;
  cursor: pointer;
}
.link-to {
  display: flex;
  height: 24px;
  align-items: center;
  gap: 20px;
  align-self: stretch;
  cursor: pointer;
}
.link-to p {
  color: #FFF;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1.2px;
  text-decoration-line: underline;
  text-decoration-style: solid;
  text-decoration-skip-ink: auto;
  text-decoration-thickness: 10%; /* 2.4px */
  text-underline-offset: 25%; /* 6px */
  text-underline-position: from-font;
}
.link-to svg {
  width: 20px;
  height: 20px;
  aspect-ratio: 1/1;
  fill: #FFF;
}
.footer {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 70px;
  align-self: stretch;
}
.logo-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 30px;
}
.line {
  width: 470px;
  height: 1px;
  background: #A3A3A3;
}
.logo {
  display: flex;
  width: 423px;
  height: 90px;
  justify-content: center;
  align-items: center;
  aspect-ratio: 47/10;
}
.logo svg {
  width: 423px;
  height: 90px;
  flex-shrink: 0;
  fill: #A3A3A3;
}
.link {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 6px;
}
.link p {
  color: #A3A3A3;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: 120%; /* 24px */
  letter-spacing: 1px;
}
.link-to-hackathon {
  display: flex;
  height: 24px;
  align-items: center;
  gap: 20px;
  align-self: stretch;
}
.link-to-hackathon p {
  color: #A3A3A3;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  letter-spacing: 1px;
  text-decoration-line: underline;
  text-decoration-style: solid;
  text-decoration-skip-ink: auto;
  text-decoration-thickness: 5%; /* 1px */
  text-underline-offset: 25%; /* 5px */
  text-underline-position: from-font;
}
.link-to-hackathon svg {
  fill: #A3A3A3;
  width: 15px;
  height: 15px;
  aspect-ratio: 1/1;
}
.close {
  display: flex;
  padding: 17px 6px;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
  background: #BCFE37;
}
.close svg {
  width: 7px;
  height: 7px;
  flex-shrink: 0;
  aspect-ratio: 1/1;
  stroke-width: 1px;
  stroke: #000;
}
</style>