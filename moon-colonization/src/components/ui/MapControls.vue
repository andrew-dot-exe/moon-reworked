<template>
    <div class="map-controls">
        <h3>Колонизация Луны</h3>
        <div class="control-panel">
            <div class="section">
                <h4>Управление</h4>
                <div class="button-grid">
                    <button @click="$emit('zoom-in')">
                        <i class="icon zoom-in"></i> Приблизить
                    </button>
                    <button @click="$emit('zoom-out')">
                        <i class="icon zoom-out"></i> Отдалить
                    </button>
                    <button @click="$emit('reset-view')">
                        <i class="icon reset"></i> Сбросить вид
                    </button>
                </div>
            </div>
            
            <div class="section">
                <h4>Информация</h4>
                <div class="info-panel">
                    <div class="info-row">
                        <span class="label">Координаты:</span>
                        <span class="value">{{ coords.x }}, {{ coords.z }}</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Тип поверхности:</span>
                        <span class="value">{{ cellData.surfaceType || 'N/A' }}</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Ресурсы:</span>
                        <span class="value">{{ cellData.resources || 'Неизвестно' }}</span>
                    </div>
                </div>
            </div>
            
            <div class="section">
                <h4>Действия</h4>
                <div class="button-grid">
                    <button @click="$emit('build')" :disabled="!isSelected">
                        Построить
                    </button>
                    <button @click="$emit('analyze')" :disabled="!isSelected">
                        Анализировать
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue';

export default defineComponent({
    props: {
        coords: {
            type: Object,
            default: () => ({ x: 0, z: 0 })
        },
        cellData: {
            type: Object,
            default: () => ({})
        }
    },
    setup(props) {
        const isSelected = computed(() => 
            props.cellData && Object.keys(props.cellData).length > 0
        );
        
        return { isSelected };
    },
    emits: ['zoom-in', 'zoom-out', 'reset-view', 'build', 'analyze']
});
</script>

<style scoped>
.map-controls {
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 15px;
    border-radius: 8px;
    width: 280px;
}

h3 {
    margin-top: 0;
    margin-bottom: 10px;
    text-align: center;
    color: #aaccff;
    font-size: 1.2em;
}

h4 {
    margin-top: 5px;
    margin-bottom: 5px;
    color: #88bbff;
    font-size: 1em;
    border-bottom: 1px solid rgba(255,255,255,0.2);
}

.section {
    margin-bottom: 15px;
}

.button-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 8px;
    margin-top: 5px;
}

button {
    padding: 6px 10px;
    border: none;
    border-radius: 4px;
    background-color: #335577;
    color: white;
    cursor: pointer;
    font-size: 0.9em;
}

button:hover {
    background-color: #446688;
}

button:disabled {
    background-color: #334455;
    color: #aaaaaa;
    cursor: not-allowed;
}

.info-panel {
    background-color: rgba(20, 20, 20, 0.5);
    padding: 8px;
    border-radius: 4px;
    margin-top: 5px;
}

.info-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 4px;
}

.label {
    color: #aaccff;
    font-size: 0.9em;
}

.value {
    font-weight: bold;
}
</style>