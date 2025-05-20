<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  prod: { type: Number, required: true },
  cons: { type: Number, required: true }
});

const name = [
  "Вода", "Топливо", "Электроэнергия", "Провизия",
  "Кислород", "Углекислый газ", "Мусор", "Материалы"
] as const; // Фиксируем типы

const iconPath = computed(() => {
  return `textures/icons/sprite.svg#icon-${name[props.id]}`;
});
</script>

<template>
    <div class="block">
        <div class="heading-container">
            <div class="icon-container">
                <div class="icon">
                    <div class="icon-s">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                width="15"
                                height="15"
                                fill="none">
                            <use :xlink:href="iconPath" width="148%" height="148%"/>
                        </svg>
                    </div>
                </div>
            </div>
            <div class="heading">
                <p>{{ name[id] }}</p>
            </div>
        </div>
        <div class="counter">
            <div class="replinishment-container">
                <div class="icon-value-container">
                    <svg xmlns="http://www.w3.org/2000/svg" width="8" height="9" viewBox="0 0 5 6" fill="none">
                        <path d="M2.91667 5.5H2.08333V3.41667H0V2.58333H2.08333V0.5H2.91667V2.58333H5V3.41667H2.91667V5.5Z" fill="#BCFE37"/>
                    </svg>
                </div>
                <div class="text-value-container">
                    <p class="value">{{ prod }}</p>
                    <p class="unit">кг</p>
                </div>
            </div>
            <div class="consuption-container">
                <div class="icon-value-container">
                    <svg xmlns="http://www.w3.org/2000/svg" width="8" height="4" viewBox="0 0 5 2" fill="none">
                        <path d="M5 1.59375H0V0.40625H5V1.59375Z" fill="#FF2C2C"/>
                    </svg>
                </div>
                <div class="text-value-container">
                    <p id="red-value">{{ cons }}</p>
                    <p id="red-unit">кг</p>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

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
    justify-content: center;
    align-items: center;
    background: #A3A3A3;
}
.icon {
    display: flex;
    justify-content: center;
    align-items: center;
}
.icon-s {
  display: flex;
  width: 22px;
  height: 20px;
  justify-content: center;
  align-items: center;
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
    width: 15px;
    height: 15px;
    display: flex;
    justify-content: center;
    align-content: center;
    border: 1px solid #BCFE37;
}
.icon-value-container svg {
    margin: auto;
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

.consuption-container .icon-value-container{
    border: 1px solid #FF2C2C;
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
</style>