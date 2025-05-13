<script lang="ts">
import { GUI } from 'three/examples/jsm/libs/lil-gui.module.min.js';
import * as THREE from 'three';
import { defineComponent, onMounted, ref } from 'vue';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { MoonMap } from '../engine/map-renderer/map';
import { DebugGUI } from '../engine/debug';
import { Mesh } from 'three';

export default defineComponent({
    emits: ['cell-selected'],
    
    setup(props, { emit }) {
        const mapRef = ref<HTMLElement | null>(null);
        let renderer: THREE.WebGLRenderer;
        let scene: THREE.Scene;
        let camera: THREE.PerspectiveCamera;
        let controls: OrbitControls;
        let map: MoonMap;
        let gridHelper: THREE.GridHelper;
        let axesHelper: THREE.AxesHelper;
        let directionalLight: THREE.DirectionalLight;
        let shadowCameraHelper: THREE.CameraHelper;

        // Метод для внешнего доступа к переключению wireframe
        const toggleWireframe = (value: boolean) => {
            scene.traverse((child) => {
                if (child instanceof THREE.Mesh && child.material) {
                    child.material.wireframe = value;
                }
            });
        };
        
        // Метод для внешнего доступа к переключению сетки
        const toggleGrid = (value: boolean) => {
            gridHelper.visible = value;
            axesHelper.visible = value;
        };
        
        // Метод для сброса камеры
        const resetCamera = () => {
            camera.position.set(10, 10, 10);
            camera.lookAt(0, 0, 0);
            controls.update();
        };
        
        // Метод для добавления структуры на карту
        const addStructure = (x: number, z: number, type: string) => {
            // Создаем простую модель сооружения
            let structure;
            
            if (type === 'base') {
                const geometry = new THREE.CylinderGeometry(0.3, 0.4, 0.5, 6);
                const material = new THREE.MeshStandardMaterial({ 
                    color: 0x3399ff,
                    roughness: 0.5,
                    metalness: 0.7
                });
                structure = new THREE.Mesh(geometry, material);
            } else {
                const geometry = new THREE.BoxGeometry(0.4, 0.4, 0.4);
                const material = new THREE.MeshStandardMaterial({ 
                    color: 0xff5533,
                    roughness: 0.7,
                    metalness: 0.2
                });
                structure = new THREE.Mesh(geometry, material);
            }
            
            // Получаем высоту ячейки для размещения структуры
            const cellHeight = 0.5; // По умолчанию
            
            // Позиционируем структуру на карте
            structure.position.set(x - 4.5, cellHeight + 0.25, z - 4.5);
            structure.castShadow = true;
            structure.receiveShadow = true;
            
            scene.add(structure);
            
            return structure;
        };
        
        // Метод для обновления данных ячейки
        const updateCellData = (x: number, z: number, data: any) => {
            return map.updateCellData(x, z, data);
        };

        onMounted(() => {
            if (!mapRef.value) {
                console.error('mapRef is null');
                return;
            }

            // Initialize renderer
            console.log('Initializing renderer');
            renderer = new THREE.WebGLRenderer({ antialias: true });
            renderer.setSize(window.innerWidth, window.innerHeight);
            
            // shadows
            renderer.shadowMap.enabled = true;
            renderer.shadowMap.type = THREE.PCFSoftShadowMap;
            mapRef.value.appendChild(renderer.domElement);

            // Create scene
            scene = new THREE.Scene();
            scene.background = new THREE.Color(0x111111); // Тёмный фон для лучшей видимости
            console.log('Scene created');

            // Создаем перспективную камеру для изометрического вида
            const aspect = window.innerWidth / window.innerHeight;
            camera = new THREE.PerspectiveCamera(50, aspect, 0.1, 1000);

            // Расположение камеры для изометрического вида
            camera.position.set(10, 10, 10);
            camera.lookAt(0, 0, 0);

            // Добавляем OrbitControls для удобства навигации
            controls = new OrbitControls(camera, renderer.domElement);
            // Configure controls to use left mouse button for panning instead of middle
            controls.mouseButtons = {
                LEFT: THREE.MOUSE.PAN,
                MIDDLE: THREE.MOUSE.DOLLY,
                RIGHT: THREE.MOUSE.ROTATE
            };
            controls.zoomSpeed = 2.5;
            controls.enableRotate = true;
            controls.enableDamping = false;
            controls.dampingFactor = 0.05;
            controls.screenSpacePanning = false;

            const debug: boolean = true;

            if(!debug){
                // Ограничение вращения камеры
                camera.position.y = Math.max(camera.position.y, 1);
                controls.minPolarAngle = Math.PI / 5;
                controls.maxPolarAngle = Math.PI / 3.5;
                controls.addEventListener('change', () => {
                    console.log('Camera position:', {
                        x: camera.position.x.toFixed(2),
                        y: camera.position.y.toFixed(2),
                        z: camera.position.z.toFixed(2)
                    });
                    console.log('Distance from center:', camera.position.length().toFixed(2));
                });
                controls.minDistance = 8;
                controls.maxDistance = 20;
            }

            // Добавляем координатную сетку и оси
            gridHelper = new THREE.GridHelper();
            scene.add(gridHelper);
            
            axesHelper = new THREE.AxesHelper(5);
            scene.add(axesHelper);

            // Добавляем освещение
            const ambientLight = new THREE.AmbientLight(0xffffff, 1);
            ambientLight.position.set(10, 10, 10);
            scene.add(ambientLight);
            
            directionalLight = new THREE.DirectionalLight(0xffffff, 2);
            directionalLight.position.set(10, 15, 10);
            directionalLight.castShadow = true;
            directionalLight.shadow.mapSize.width = 1024;
            directionalLight.shadow.mapSize.height = 1024;
            directionalLight.shadow.camera.near = 0.5;
            directionalLight.shadow.camera.far = 50;
            directionalLight.shadow.bias = -0.001;
            directionalLight.shadow.camera.left = -10;
            directionalLight.shadow.camera.right = 10;
            directionalLight.shadow.camera.top = 10;
            directionalLight.shadow.camera.bottom = -10;
            scene.add(directionalLight);
            
            shadowCameraHelper = new THREE.CameraHelper(directionalLight.shadow.camera);
            scene.add(shadowCameraHelper);

            // Настраиваем объекты карты
            map = new MoonMap("Test");
      
            for (let i: number = 0; i < 10; i++) {
                for (let j: number = 0; j < 10; j++) {
                    let mesh : Mesh = map.getCellMesh(i, j);
                    mesh.position.x = i - 4.5;
                    mesh.position.z = j - 4.5;
                    mesh.castShadow = true;
                    mesh.receiveShadow = true;
                    scene.add(mesh);
                }
            }

            // Модифицируем обработчик клика, чтобы он эмитил события
            map.setupClickHandler(camera, renderer, (x: number, z: number, cellData: any) => {
                console.log(`Clicked on cell: ${x}, ${z}`, cellData);
                emit('cell-selected', x, z, cellData);
            });

            const dbgMenu :DebugGUI = new DebugGUI();
            dbgMenu.debugLights(directionalLight, shadowCameraHelper);
            dbgMenu.debugShadows(directionalLight, shadowCameraHelper);
            
            // Handle window resize
            window.addEventListener('resize', onWindowResize);
            // Start animation loop
            animate();
        });

        const onWindowResize = () => {
            const aspect = window.innerWidth / window.innerHeight;
            camera.aspect = aspect;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        };

        const animate = () => {
            requestAnimationFrame(animate);
            controls.update();
            renderer.render(scene, camera);
        };

        return {
            mapRef,
            toggleWireframe,
            toggleGrid,
            resetCamera,
            addStructure,
            updateCellData
        };
    }
});
</script>

<template>
    <div ref="mapRef" class="map-container"></div>
</template>

<style scoped>
.map-container {
    width: 100%;
    height: 100vh;
    background-color: #000;
}
</style>