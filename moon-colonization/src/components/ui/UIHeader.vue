<template>

  <div class="Header">
    <div class="master-header">
      <div class="Main-container">
        <div class="Logo-container">
          <div class="logo-container">
            <div class="margin"></div>
            <div class="logo-container-container">
              <svg xmlns="http://www.w3.org/2000/svg" width="152" height="22" viewBox="0 0 152 22" fill="none">
                <path
                  d="M85.8193 0L96.2999 12.4482H96.3612V0H102.797V22H96.3612L85.8806 9.55182H85.8193V22H79.3839V0H85.8193Z"
                  fill="white" />
                <path
                  d="M0 0H6.64446L13.8707 14.0196H13.9319L21.1582 0H27.8026V22H21.3725V12.0476H21.3113L15.861 22H11.9417L6.49137 12.0476H6.43013V22H0V0Z"
                  fill="white" />
                <path d="M111.653 22H110.113V20.4875L119.548 0H121.088V1.5125L111.653 22Z" fill="#BCFE37" />
                <path
                  d="M138.201 2.06667L131.537 11L139.196 20.1V22H137.306L129.283 12.3667H125.636V22H122.917V0H125.636V9.7H129.283L136.146 0H138.201V2.06667Z"
                  fill="#BCFE37" />
                <path d="M142.565 22H141.025V20.4875L150.46 0H152V1.5125L142.565 22Z" fill="#BCFE37" />
                <path fill-rule="evenodd" clip-rule="evenodd"
                  d="M28.7172 10.8302C28.7172 17.6881 33.3252 21.8689 41.1441 21.8689L66.0127 22C73.8315 22 78.4693 17.9502 78.4693 11.0923C78.4693 4.20483 73.846 0.106396 66.0276 0.0239934C60.9055 -0.0299914 53.5858 0.0239981 53.5858 0.0239981H41.1441C33.3252 0.0237858 28.7172 3.94272 28.7172 10.8302ZM41.1552 16.8667C37.4688 16.8667 35.2892 14.5547 35.2892 10.8302C35.2892 7.07607 37.4688 5.13334 41.1552 5.13334H57.2515L41.8869 16.8639L41.1552 16.8667ZM71.9288 11.0923C71.9288 7.33817 69.7475 5.13334 66.0313 5.13334H65.2996L49.935 16.8667H66.0313C69.7475 16.8667 71.9288 14.8168 71.9288 11.0923Z"
                  fill="white" />
              </svg>
            </div>
          </div>
        </div>
        <div class="Resource-container">
          <div class="Resource">
            <div class="resource-container">
              <!-- Вода -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="15" viewBox="0 0 10 15" fill="none">
                        <path
                          d="M9.85262 9.375C10.6324 12.3888 8.21647 15 5.0014 15C1.78633 15 -0.639121 12.4256 0.150173 9.375C0.635296 7.5 3.06091 4.6875 5.0014 0C6.94189 4.6875 9.3675 7.5 9.85262 9.375Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? 0 : resourceStore.resources[0].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Топливо -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="15" viewBox="0 0 10 15" fill="none">
                        <path
                          d="M10 1.49939V1.52177C10 2.34979 8.27097 3.02404 5 3.02404C1.56452 3.02404 0 2.34979 0 1.52177V1.48341C0 1.00386 0.53871 0.623415 1.60323 0.354867C2.5129 0.124683 3.71935 0 5 0C6.27742 0 7.48387 0.124683 8.39677 0.354867C9.46129 0.623415 10 1.00386 10 1.48341V1.49939ZM4.29032 1.48341C4.29032 1.34913 3.86452 1.24363 3.34194 1.24363C2.81613 1.24363 2.39032 1.34913 2.39032 1.48341C2.39032 1.61448 2.81613 1.72318 3.34194 1.72318C3.86452 1.72318 4.29032 1.61448 4.29032 1.48341ZM9.69677 4.61295V2.68836C9.41613 2.89936 8.99032 3.07839 8.41935 3.22226C7.5 3.45564 6.2871 3.58032 5 3.58032C3.7129 3.58032 2.5 3.45244 1.58065 3.22226C1.00968 3.07839 0.583871 2.90256 0.303226 2.68836V4.61295C0.1 4.78239 0 4.97101 0 5.17882V5.21399C0 6.04201 1.56452 6.71338 5 6.71338C8.27097 6.71338 10 6.04201 10 5.21399V5.17882C10 4.97101 9.9 4.78239 9.69677 4.61295ZM9.69677 8.73388V6.38409C9.41613 6.59509 8.99032 6.77412 8.41935 6.91799C7.5 7.14817 6.2871 7.27605 5 7.27605C3.7129 7.27605 2.5 7.14817 1.58065 6.91799C1.00968 6.77412 0.583871 6.59509 0.303226 6.38409V8.73388C0.1 8.90332 0 9.09194 0 9.29975V9.32852C0 10.1565 1.56452 10.8279 5 10.8279C8.27097 10.8279 10 10.1565 10 9.32852V9.29975C10 9.08875 9.9 8.90013 9.69677 8.73388ZM9.98387 13.3823C9.94839 13.2257 9.85484 13.0818 9.69677 12.9507V10.505C9.41613 10.716 8.99032 10.8951 8.41935 11.0389C7.5 11.2691 6.2871 11.397 5 11.397C3.7129 11.397 2.5 11.2691 1.58065 11.0389C1.00968 10.8951 0.583871 10.716 0.303226 10.505V12.9507C0.145161 13.0818 0.0516129 13.2257 0.016129 13.3823C0.00645161 13.4207 0 13.4622 0 13.5006V13.5166C0 13.6988 0.0741936 13.8651 0.225806 14.0153C0.774194 14.5908 2.31613 15 5 15C6.94194 15 8.34194 14.7634 9.15806 14.3958C9.71613 14.156 10 13.8651 10 13.5166V13.5006C10 13.4622 9.99355 13.4207 9.98387 13.3823Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[1].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Энергия -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="7" height="15" viewBox="0 0 7 15" fill="none">
                        <path d="M7 6.09375H3.5V0L0 8.90625H3.5V15L7 6.09375Z" fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[2].count }}</p>
                      <p class="unit">кВт*ч</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Провизия -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="18" height="15" viewBox="0 0 18 15" fill="none">
                        <path
                          d="M16.211 0L16.21 14.4132C16.4716 14.8378 16.7422 15.0011 16.9692 15C17.1944 14.9989 17.4622 14.8337 17.7187 14.4218L17.0182 7.06012L17.01 6.97046L17.0661 6.89899C17.8711 5.88015 18.1524 4.11994 17.9227 2.63111C17.8079 1.88666 17.5676 1.21286 17.231 0.736537C16.9488 0.337087 16.6121 0.082275 16.2111 0H16.211ZM2.054 0.000150061L2.05339 2.51741L1.57589 2.51681V0.000299978H1.03204L1.03211 2.51692H0.553923V0.000299978H4.5754e-09V3.87135C-3.80135e-05 4.29949 0.236853 4.5369 0.60981 4.68367L0.764163 4.7445L0.760741 4.90837C0.684705 8.07975 0.609544 11.2511 0.534078 14.4224C0.794121 14.8421 1.06124 15.001 1.28364 15C1.50605 14.9989 1.77293 14.8359 2.02967 14.4213C1.94166 11.2508 1.85365 8.0802 1.76579 4.90965L1.76122 4.74465L1.91607 4.68442C2.30857 4.53045 2.56386 4.26915 2.56386 3.87157V0.000150061H2.054ZM9.00321 1.4625C7.37984 1.4625 5.82297 2.09859 4.67507 3.23084C3.52718 4.36309 2.8823 5.89875 2.8823 7.5C2.8823 9.10124 3.52718 10.6369 4.67507 11.7692C5.82297 12.9014 7.37984 13.5375 9.00321 13.5375C10.6266 13.5375 12.1835 12.9014 13.3313 11.7692C14.4792 10.6369 15.1241 9.10124 15.1241 7.5C15.1241 5.89875 14.4792 4.36309 13.3313 3.23084C12.1835 2.09859 10.6266 1.4625 9.00321 1.4625Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[3].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Кислород -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 15 15" fill="none">
                        <path
                          d="M3.66805 10.6019L2 8.58175V4.02014L3.66805 2H6.35548L8.02353 4.02014V8.58175L6.35548 10.6019H3.66805ZM3.47113 4.65877V7.94313L4.30516 8.94668H5.71837L6.5524 7.94313V4.65877L5.71837 3.65521H4.30516L3.47113 4.65877Z"
                          fill="white" />
                        <path
                          d="M13 13H9.64072V10.9929L10.2431 10.3412H11.8764L12.0154 10.2239V9.70261L11.8764 9.54621H10.7643L10.6253 9.70261V9.96327H9.64072V9.22038L10.3705 8.43839H12.2702L13 9.22038V10.7322L12.3861 11.3187H10.7528L10.6253 11.4751V11.8922H13V13Z"
                          fill="white" />
                        <path d="M15 0V15H0V0H15ZM1 14H14V1H1V14Z" fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[4].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Углекислый газ -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="19" height="15" viewBox="0 0 19 15" fill="none">
                        <path d="M19 0V15H0V0H19ZM1 14H18V1H1V14Z" fill="white" />
                        <path
                          d="M5.8826 10.6019H3.39425L2 8.58175V4.02014L3.39425 2H5.8826L7.13162 3.79858L6.08593 4.7109L5.35008 3.65521H3.92678L3.22965 4.65877V7.95616L3.92678 8.94668H5.35008L6.0472 7.94313L7.10257 8.82938L5.8826 10.6019Z"
                          fill="white" />
                        <path
                          d="M9.19985 10.6019L7.8056 8.58175V4.02014L9.19985 2H11.4461L12.8404 4.02014V8.58175L11.4461 10.6019H9.19985ZM9.03525 4.65877V7.94313L9.73238 8.94668H10.9136L11.6107 7.94313V4.65877L10.9136 3.65521H9.73238L9.03525 4.65877Z"
                          fill="white" />
                        <path
                          d="M17 13H14.1921V10.9929L14.6956 10.3412H16.0608L16.177 10.2239V9.70261L16.0608 9.54621H15.1313L15.0151 9.70261V9.96327H14.1921V9.22038L14.8021 8.43839H16.39L17 9.22038V10.7322L16.4868 11.3187H15.1216L15.0151 11.4751V11.8922H17V13Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[5].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Мусор -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="15" viewBox="0 0 10 15" fill="none">
                        <path
                          d="M2.88889 0V1.95312H0V3.125H10V1.95312H7.11111V0H2.88889ZM3.55556 0.703125H6.44444V1.95312H3.55556V0.703125ZM0.629556 3.82812L1.74459 15H8.25548L9.37045 3.82812H0.629556ZM2.93837 4.35078L2.96207 4.70156L3.55467 13.4516L3.57848 13.8022L2.91344 13.8523L2.88974 13.5015L2.29715 4.75152L2.27333 4.4009L2.93837 4.35078ZM7.06163 4.35078L7.72663 4.40094L7.70282 4.7516L7.11022 13.5016L7.08652 13.8524L6.42148 13.8022L6.4453 13.4516L7.03789 4.70156L7.06159 4.35078H7.06163ZM4.66667 4.375H5.33333V13.8281H4.66667V4.375Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[6].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>

              <!-- Стройматериалы -->
              <div class="block-resource">
                <div class="master-resource">
                  <div class="information">
                    <div class="icon">
                      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="15" viewBox="0 0 22 15" fill="none">
                        <path
                          d="M14.497 0L2.10545 6.5862L7.76632 9.5746L20.1207 3.04988L14.497 0H14.497ZM16.721 2.3186L18.0526 2.99468L7.78744 8.38775L4.30828 6.53603L5.6268 5.84039L7.78749 6.99175L16.7212 2.31865L16.721 2.3186ZM20.6961 3.84276L8.14683 10.4712L7.84602 14.7973L22 7.2835L20.6961 3.84276ZM1.49894 7.3646L0 11.3599L6.85916 15L7.17937 10.3627L1.49889 7.36465L1.49894 7.3646Z"
                          fill="white" />
                      </svg>
                    </div>
                    <div class="value">
                      <p class="text-value">{{ isLoading ? -0 : resourceStore.resources[7].count }}</p>
                      <p class="unit">кг</p>
                    </div>
                  </div>

                </div>
              </div>
            </div>
            <div class="new-resource">
              <div class="time">
                <p>Дней до прибытия ресурсов</p>
                <div class="time-value">
                  <p>{{ dayBefor }}</p>
                </div>
              </div>
              <div class="visual-path">
                <div class="the-suttle" :style="{ left: position.left + 'px' }"></div>
                <div class="way">
                  <svg class="point" xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 10 10"
                    fill="none">
                    <circle cx="5" cy="5" r="5" fill="#464646" />
                  </svg>
                  <svg class="trajectory" xmlns="http://www.w3.org/2000/svg" width="548" height="2" viewBox="0 0 548 2"
                    fill="none">
                    <path d="M1 1H547" stroke="#464646" stroke-miterlimit="3.8637" stroke-linecap="square"
                      stroke-dasharray="10 10" />
                  </svg>
                  <svg class="point" xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 10 10"
                    fill="none">
                    <circle cx="5" cy="5" r="5" fill="#464646" />
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="Add-functions">
          <div class="Button-statistic" @click="toggleStatistics">
            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
              <g clip-path="url(#clip0_115_667)">
                <path
                  d="M1.34766 1.34766V28.6523H28.6523V27.5977H26.2563V15H23.4438V27.5977H21.5878V9.33574H18.7753V27.5977H16.9456V18.7642H14.1331V27.5977H12.1406V4.71199H9.32812V27.5977H7.5V11.25H4.6875V27.5977H2.40234V1.34766H1.34766Z"
                  fill="black" />
              </g>
              <defs>
                <clipPath id="clip0_115_667">
                  <rect width="30" height="30" fill="white" />
                </clipPath>
              </defs>
            </svg>
          </div>
          <div class="Button-menu" @click="toggleMenu">
            <div class="burger">
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="6" viewBox="0 0 12 6" fill="none">
                <path d="M1 1L11 1" stroke="black" stroke-linecap="square" />
                <path d="M1 5H11" stroke="black" stroke-linecap="square" />
              </svg>
            </div>
          </div>
        </div>
      </div>
      <div class="Mode-container">
        <p>{{ name }}</p>
      </div>
    </div>
    <div v-if="isStatisticsVisible" class="statistics-overlay">
    <StatisticWindow @toggle="toggleStatistics"/>
  </div>
  <div v-if="isMenuVisible" class="menu-overlay">
    <MenuWindow @toggle="toggleMenu" @end_col="endColonisation"/>
  </div>
  </div>
  
  

</template>

<script setup lang="ts">
const emit = defineEmits(['end_col']);
import { userInfoStore } from '@/stores/userInfoStore';
import { useResourceStore } from '@/stores/resourceStore';
import { onMounted, ref, watch, onUnmounted } from 'vue';
import StatisticWindow from '@/components/ui/StatisticWindow.vue';
import MenuWindow from '@/components/ui/MenuWindow.vue';

const endColonisation = () => {
   emit('end_col')
}

defineProps({
  name: { type: String, required: true }
});

const uInfo = userInfoStore();
const resourceStore = useResourceStore();
const isLoading = ref(true);
const dayBefor = ref(0);

// Функция для отслеживания изменений
const setupWatchers = () => {
  const unwatchDayBefore = watch(
    () => uInfo.userInfo.dayBeforeDelivery,
    (newDayBefore) => {
      dayBefor.value = newDayBefore;
      moveSuttle();
    }
  );
  
  // Добавляем вотчер для отслеживания изменений в resourceStore
  const unwatchResources = watch(
    () => resourceStore.resources, // отслеживаем массив ресурсов
    (newResources, oldResources) => {
      // Здесь можно добавить любую логику, которая должна выполняться при изменении ресурсов
    },
    { deep: true } // используем deep: true для отслеживания изменений внутри объектов массива
  );
  
  onUnmounted(() => {
    unwatchDayBefore();
    //unwatchResources();
  });
};

onMounted(async () => {
  try {
    await resourceStore.getResources();
    isLoading.value = resourceStore.resources.length === 0;
    
    await uInfo.fetchUserInfo();
    dayBefor.value = uInfo.userInfo.dayBeforeDelivery;
    moveSuttle();
    
    setupWatchers(); // Инициализируем отслеживание изменений
  } catch (error) {
    console.error('Initialization error:', error);
  }
});

const isStatisticsVisible = ref(false);

const toggleStatistics = () => {
  isStatisticsVisible.value = !isStatisticsVisible.value;
};

const isMenuVisible = ref(false);

const toggleMenu = () => {
  isMenuVisible.value = !isMenuVisible.value;
  console.log('isMenuVisible:', isMenuVisible.value);
};

const position = ref({
  left: 10 // Начальное значение
});

// Функция для изменения положения
const moveSuttle = () => {
  position.value.left = (1 - 31 / (dayBefor.value + 1)) * 540 + 10;
};



</script>

<style scoped>
.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  z-index: 100;
  display: flex;
  justify-content: flex-end; /* Меню выезжает справа, поэтому выравниваем по правому краю */
}
.statistics-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  z-index: 100;
  display: flex;
  justify-content: center;
  align-items: center;
}



.Header {
  display: flex;
  min-width: 1220px;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.master-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  flex: 1 0 0;
}

.Main-container {
  display: flex;
  height: 62px;
  padding: 0px 20px;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
  background: rgba(0, 0, 0, 0.80);
}

.Logo-container {
  display: flex;
  width: 188px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  align-self: stretch;
}

.logo-container {
  display: flex;
  width: 174px;
  height: 38px;
  align-items: center;
}

.margin {
  width: 10px;
  flex-shrink: 0;
  align-self: stretch;
  border: 1px solid #BCFE37;
  background: #BCFE37;
}

.logo-container-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex: 1 0 0;
  align-self: stretch;
  border: 1px solid #BCFE37;
  background: rgba(0, 0, 0, 0.80);
}

.logo-container-container svg {
  width: 152px;
  height: 22px;
  aspect-ratio: 76/11;
}

.Resource-container {
  display: flex;
  padding-top: 9px;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  align-self: stretch;
}

.Resource {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.resource-container {
  display: flex;
  align-items: center;
  align-content: center;
  gap: 2px;
  align-self: stretch;
  flex-wrap: wrap;
}

.block-resource {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}

.master-resource {
  display: flex;
  padding-right: 5px;
  padding-left: 5px;
  align-items: flex-start;
  align-self: stretch;
  background: #464646;
  max-height: 25px;
}

.information {
  display: flex;
  align-items: center;
  gap: 12px;
}

.icon {
  display: flex;
  height: 15px;
  justify-content: center;
  align-items: center;
}

.icon svg {
  align-self: stretch;
  aspect-ratio: 2/3;
}

.value {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
}

.value p {
  color: #FFF;
  /* leading-trim: both;
    text-edge: cap; */
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 15px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
}

.new-resource {
  display: flex;
  padding: 5px;
  align-items: center;
  gap: 30px;
  align-self: stretch;
}

.time {
  display: flex;
  align-items: center;
  gap: 10px;
}

.time p {
  color: #A3A3A3;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 10px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
}

.time-value {
  display: flex;
  align-items: center;
}

/* .visual-path {
    display: flex;
    height: 10px;
    padding: 2px 0px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
}
.the-suttle {
    width: 6px;
    height: 6px;
    aspect-ratio: 1/1;
    position: absolute;
    left: 149px;
    top: 2px;
    background: #464646;
} */
.visual-path {
  position: relative;
  /* добавлено */
  display: flex;
  height: 10px;
  align-items: center;
  gap: 10px;
  flex: 1 0 0;
}

.the-suttle {
  width: 8px;
  height: 8px;
  aspect-ratio: 1/1;
  position: absolute;
  top: 1px;
  background: #505050;
}

.way {
  display: flex;
  align-items: center;
  flex: 1 0 0;
}

.point {
  fill: #464646;
  width: 10px;
  height: 10px;
  aspect-ratio: 1/1;
}

.trajectory {
  stroke-width: 1px;
  stroke: #464646;
  flex: 1 0 0;
}

.Add-functions {
  display: flex;
  width: 188px;
  justify-content: flex-end;
  align-items: center;
  gap: 30px;
  align-self: stretch;
}

.Button-statistic {
  display: flex;
  width: 40px;
  height: 40px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
  background: #BCFE37;

  cursor: pointer;
  transition: transform 0.2s;
}

.Button-statistic svg {
  width: 30px;
  height: 30px;
  flex-shrink: 0;
  aspect-ratio: 1/1;
}

.Button-menu {
  display: flex;
  width: 20px;
  height: 40px;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
  cursor: pointer;
}

.burger {
  display: flex;
  padding: 17px 6px;
  justify-content: space-between;
  align-items: center;
  flex: 1 0 0;
  background: #BCFE37;
}

.burger svg {
  width: 10px;
  height: 4px;
}

.Mode-container {
  background: #BCFE37;
  display: flex;
  height: 20px;
  justify-content: center;
  align-items: center;
  gap: 5px;
  align-self: stretch;
}

.Mode-container p {
  color: #000;
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
</style>
