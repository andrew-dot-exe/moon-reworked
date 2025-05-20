<script setup lang="ts">
import { computed } from 'vue'
const props = defineProps({
  opt: { type: Number, default: 0 },
  rel: { type: Number, default: 0 },
  rat: { type: Number, default: 0 }
});

const optimal = computed(() => {
  return `${props.opt}%`;
});
const relief = computed(() => {
  return `${props.rel}%`;
});
const rationality = computed(() => {
  return `${props.rat}%`;
});

</script>

<template> 
    <div class="Optimality">
        <div class="Optimality-master">
            <div class="line"></div>
            <div class="main-container">
                <div class="value-container">
                    <div class="criteria">
                        <p>Оптимальность расположения</p>
                    </div>
                    <div class="value">
                        <p :class="{good: opt > 50, normal: opt > 10 && opt < 51, bad: opt <= 10 }">{{ optimal }}</p>
                    </div>
                </div>
            </div>
            <div class="additionally-container">
                <div class="add-value-container">
                    <div class="value-container1">
                        <div class="add-criteria">
                            <p>Рельеф</p>
                        </div>
                        <div class="add-value">
                            <p :class="{good: rel > 50, normal: rel > 10 && rel < 51, bad: rel <= 10 }">{{ relief }}</p>
                        </div>
                    </div>
                    <div class="value-container2">
                        <div class="add-criteria">
                            <p>Рациональность</p>
                        </div>
                        <div class="add-value">
                            <p :class="{good: rat > 50, normal: rat > 10 && rat < 51, bad: rat <= 10 }">{{ rationality }}</p>
                        </div>
                    </div>
                </div>
            </div>
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
p {
    margin: 0;
}
.Optimality {
    display: flex;
    width: 440px;
    min-width: 440px;
    align-items: flex-start;
    gap: 10px;
}
.Optimality-master {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    flex: 1 0 0;
}
.line {
    height: 10px;
    align-self: stretch;
    background: #464646;
}
.main-container {
    display: flex;
    height: 100px;
    padding: 10px;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 10px;
    align-self: stretch;
    border: 1px solid #464646;
    background: rgba(0, 0, 0, 0.80);
}
.value-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
.criteria {
    display: flex;
    min-width: 210px;
    height: 80px;
    padding: 10px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    background: #464646;
}
.criteria p {
    width: 190px;
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 20px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
/* .value {
    display: flex;
    height: 60px;
    padding: 12px 72px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    border: 2px solid #464646;
} */
 .value {
    position: relative; /* нужно для псевдоэлемента */
    display: flex;
    padding: 10px;
    height: 80px;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    /* border удаляем */
    /* border: 2px solid #464646; */
}

.value::before {
    content: "";
    position: absolute;
    inset: 0;
    border: 2px solid #464646;
    pointer-events: none;
    box-sizing: border-box;
    z-index: 1;
    border-radius: inherit;
}

.value p {
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 36px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
/* .additionally-container {
    display: flex;
    padding: 10px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;         
    align-self: stretch;
    border: 1px solid #464646;
    background: rgba(0, 0, 0, 0.80);
} */
 .additionally-container {
    position: relative; /* нужно для псевдоэлемента */
    display: flex;
    padding: 10px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;         
    align-self: stretch;
    background: rgba(0, 0, 0, 0.80);
    /* border удаляем */
    /* border: 1px solid #464646; */
}

.additionally-container::before {
    content: "";
    position: absolute;
    inset: 0;
    border: 1px solid #464646;
    pointer-events: none;
    box-sizing: border-box;
    z-index: 1;
    border-radius: inherit;
}

.add-value-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    gap: 4px;
    align-self: stretch;
}
.value-container1 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
.add-criteria {
    background: #464646;
    display: flex;
    min-width: 210px;
    padding: 4px 10px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
}
.add-criteria p {
    color: #FFF;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
/* .add-value {
    display: flex;
    min-width: 210px;
    padding: 4px 10px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    border: 2px solid #464646;
} */
.add-value {
    position: relative; /* для псевдоэлемента */
    display: flex;
    min-width: 210px;
    padding: 4px 10px;
    align-items: center;
    gap: 10px;
    flex: 1 0 0;
    /* Убираем внешнюю рамку */
    /* border: 2px solid #464646; */
}

.add-value::before {
    content: "";
    position: absolute;
    inset: 0;
    border: 2px solid #464646;
    pointer-events: none;
    box-sizing: border-box;
    z-index: 1;
    border-radius: inherit;
}

.add-value p {
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 15px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
}
.value-container2 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    align-self: stretch;
}
</style>