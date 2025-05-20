
<script setup lang="ts">
defineProps({
  id: {type: Number, required: true },
  data: { type: Number, required: true }
});
const name = ["Вода", "Топливо", "Электроэнергия", "Провизия", 
"Кислород", "Углекислый газ", "Мусор", "Материалы"];
</script>

<template>
    <div class="resource">
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="41" viewBox="0 0 40 41" fill="none">
            <path d="M31.607 25.5C33.6864 33.5369 27.2439 40.5 18.6704 40.5C10.0969 40.5 3.62905 33.635 5.73384 25.5C7.0275 20.5 13.4958 13 18.6704 0.5C23.8451 13 30.3134 20.5 31.607 25.5Z" fill="#A3A3A3"/>
        </svg>
        <div class="information">
            <p>{{ name[id] }}</p>
            <div class="value-container">
                <p>{{ data }}</p>
                <p>кг</p>
            </div>
        </div>
    </div>
</template>

<style scoped>


.resource {
    display: flex;
    align-items: center;
    align-content: center;
    gap: 15px 14px;
    align-self: stretch;
    flex-wrap: wrap;
}
.resource svg {
    width: 40px;
    height: 40px;
    flex-shrink: 0;
    aspect-ratio: 1/1;
    filter: #A3A3A3;
}
.information {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
}
.information p {
    color: #A3A3A3;
    font-feature-settings: 'dlig' on;
    font-family: "Feature Mono";
    font-size: 10px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    letter-spacing: 0.5px;
} 
.value-container {
    display: flex;
    align-items: flex-start;
    gap: 5px;
}
.value-container p {
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