<script setup lang="ts">
import { TypeModule } from '@/components/typeModules/typeModules';
import { computed } from 'vue';
import { useBuildModuleStore } from '@/stores/buildModuleStore'

const buildModuleStore = useBuildModuleStore()

function selectForBuild() {
  const baseName = props.data.name.split('(')[0].trim().toLowerCase()
  buildModuleStore.setBuildModule(props.data, baseName + '.gltf')
  console.log('Выбран модуль для постройки:', props.data, 'GLTF:', baseName + '.gltf')
}

const props = defineProps({
  data: {
    type: TypeModule,
    required: true
  }
});

const moduleImage = computed(() => {
  const baseName = props.data.name.split('(')[0].trim().toLowerCase();
  try {
    return new URL(`/textures/modules/${baseName}.png`, import.meta.url).href;
  } catch {
    return '/textures/modules/default.png';
  }
});
</script>

<template>
  <div class="object" @click="selectForBuild">
    <div class="master-object-block">
      <div class="icon">
        <div class="icon-object">
          <img :src="moduleImage" alt="">
        </div>
      </div>
      <div class="information-container">
        <div class="Heading">
          <h1>{{ data.name }}</h1>
          <div class="line"></div>
        </div>
        <div class="Paragraph">
          <div class="Built-all">
            <p class="Built">{{ data.people }}</p>
            <!--<p class="All">/</p>
                        <p class="All">0</p>-->
          </div>
          <div class="Cost-container">
            <div class="Text">
              <div class="Number">
                <p class="number">{{ data.cost }}</p>
                <div class="Unit">
                  <p class="unit">кг</p>
                </div>
              </div>
              <div class="Icon">
                <div class="icon-material">
                  <svg xmlns="http://www.w3.org/2000/svg" width="22" height="15" viewBox="0 0 22 15" fill="none">
                    <path
                      d="M14.497 0.5L2.10545 7.0862L7.76632 10.0746L20.1207 3.54988L14.497 0.5H14.497ZM16.721 2.8186L18.0526 3.49468L7.78744 8.88775L4.30828 7.03603L5.6268 6.34039L7.78749 7.49175L16.7212 2.81865L16.721 2.8186ZM20.6961 4.34276L8.14683 10.9712L7.84602 15.2973L22 7.7835L20.6961 4.34276ZM1.49894 7.8646L0 11.8599L6.85916 15.5L7.17937 10.8627L1.49889 7.86465L1.49894 7.8646Z"
                      fill="white" />
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

</template>

<style scoped>
.object {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  flex: 1 0 0;
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
