<script setup lang="ts">
const emit = defineEmits(['toggle']); 
import { statisticStore } from '@/stores/StatisticStore';
import { onMounted, ref } from 'vue';
import ResourceAll from './ResourceAll.vue';

const statistic = statisticStore();


// Lifecycle hooks
onMounted(async () => {
  await statistic.getStatistic();
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
                            :data="item"
                            />
                </div>
                <div v-else>
                    Данные загружаются...
                </div>
            </div>
            <div class="Area-all-container">
                <p>Всего потребление и восполнение ресурсов:</p>
                <div class="replinishment-consuption">
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <div class="heading-container">
                            <div class="icon-container">
                                <div class="icon">
                                    <div class="icon-s">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                            <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                        </svg>
                                    </div>
                                </div>
                            </div>
                            <div class="heading">
                                <p>Вода</p>
                            </div>
                        </div>
                        <div class="counter">
                            <div class="replinishment-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p class="value">100</p>
                                    <p class="unit">кг</p>
                                </div>
                            </div>
                            <div class="consuption-container">
                                <div class="icon-value-container">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                    </svg>
                                </div>
                                <div class="text-value-container">
                                    <p id="red-value">100</p>
                                    <p id="red-unit">кг</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="Area-specific-container">
                <div class="paragraph">
                    <p>Потребности по областям</p>
                </div>
                <div class="statistic-area-master">
                    <div class="heading-container-area">
                        <div class="name-area-container">
                            <p>Равнина 1</p>
                        </div>
                        <div class="arrows">
                            <div class="master-arrowsUp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="12" viewBox="0 0 8 12" fill="none">
                                    <path d="M4.48157 2.12522L8 5.73166V6.44007H7.3317L4.54054 3.45617H4.44226V12H3.55774V3.45617H3.4398L0.648649 6.44007H0V5.73166L3.51843 2.12522H4.48157ZM0 0H8V0.880143H0V0Z" fill="#464646"/>
                                </svg>
                            </div>
                        </div>
                    </div>
                    <div class="information-container-specific">
                        <p>Всего потребление и воспалнение ресурсов в области:</p>
                        <div class="replinishment-consuption" id="area-replinishment-consuption">
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="block">
                                <div class="heading-container">
                                    <div class="icon-container">
                                        <div class="icon">
                                            <div class="icon-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="8" height="11" viewBox="0 0 8 11" fill="none">
                                                    <path d="M7.23504 6.75C7.75488 8.75923 6.14427 10.5 4.00089 10.5C1.85751 10.5 0.240545 8.78375 0.766741 6.75C1.09016 5.5 2.70723 3.625 4.00089 0.5C5.29455 3.625 6.91163 5.5 7.23504 6.75Z" fill="white"/>
                                                </svg>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="heading">
                                        <p>Вода</p>
                                    </div>
                                </div>
                                <div class="counter">
                                    <div class="replinishment-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="6" viewBox="0 0 5 6" fill="none">
                                                <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p class="value">100</p>
                                            <p class="unit">кг</p>
                                        </div>
                                    </div>
                                    <div class="consuption-container">
                                        <div class="icon-value-container">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="5" height="2" viewBox="0 0 5 2" fill="none">
                                                <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                                            </svg>
                                        </div>
                                        <div class="text-value-container">
                                            <p id="red-value">100</p>
                                            <p id="red-unit">кг</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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
    display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 24px;
    align-self: stretch;
    flex-wrap: wrap;
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
.replinishment-consuption {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 24px;
    align-self: stretch;
    flex-wrap: wrap;
}
.block {
    display: flex;
    min-width: 135px;
    max-width: 174px;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 10px;
    flex: 1 0 0;
}
.heading-container {
    display: flex;
    justify-content: flex-end;
    align-items: flex-start;
    align-self: stretch;
}
.icon-container {
    display: flex;
    width: 20px;
    height: 20px;
    padding: 5px 0px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    background: #A3A3A3;
}
.icon {
    display: flex;
    height: 10px;
    justify-content: center;
    align-items: center;
}
.icon-s {
    display: flex;
    width: 7px;
    height: 10px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
}
.icon-s svg {
    width: 6.667px;
    height: 10px;
    flex-shrink: 0;
    aspect-ratio: 6.67/10.00;
}
.heading {
    display: flex;
    height: 20px;
    min-width: 58px;
    padding: 5px 10px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    border: 1px solid #A3A3A3;
}
.heading p {
    color: #A3A3A3;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 10px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.5px;
}
.counter {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
    align-self: stretch;
}
.replinishment-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
.icon-value-container {
    display: flex;
    width: 15px;
    height: 15px;
    padding: 5px;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;
    aspect-ratio: 1/1;
    border: 1px solid #BCFE37;
}
.icon-value-container svg {
    width: 5px;
    flex: 1 0 0;
    aspect-ratio: 1/1;
    fill: #BCFE37;
}
.text-value-container {
    display: flex;
    align-items: center;
    gap: 5px;
}
.value {
    color: #BCFE37;
    text-align: right;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.75px;
}
.unit {
    color: #BCFE37;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.75px;
}
.consuption-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
#red-value {
    color: #FF2C2C;
    text-align: right;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.75px;
}
#red-unit {
    color: #FF2C2C;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.75px;
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
.statistic-area-master {
    display: flex;
    height: 260px;
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
    align-self: stretch;
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
    padding: 8px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    align-self: stretch;
    aspect-ratio: 1/1;
    background: #A3A3A3;
}
.master-arrowsUp {
    display: flex;
    width: 14px;
    height: 14px;
    padding: 1px 3px;
    justify-content: center;
    align-items: center;
    flex-shrink: 0;
}
.master-arrowsUp svg {
    fill: #464646;
    width: 8px;
    height: 12px;
    flex-shrink: 0;
    aspect-ratio: 2/3;
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
#area-replinishment-consuption {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 24px;
    align-self: stretch;
    flex-wrap: wrap;
}
</style>