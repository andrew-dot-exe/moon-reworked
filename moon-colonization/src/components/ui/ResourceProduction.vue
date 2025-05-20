<script setup lang="ts">
defineProps({
  key: {type: Number, required: true },
  prod: { type: Number, required: true },
  cons: { type: Number, required: true }
});
const name = ["Вода", "Топливо", "Электроэнергия", "Провизия", 
"Кислород", "Углекислый газ", "Мусор", "Материалы"];
</script>

<template>
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
                <p>{{ name[key] }}</p>
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
                    <p class="value">{{ prod }}</p>
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
</style>