<script lang="ts">
            import { GUI } from 'three/examples/jsm/libs/lil-gui.module.min.js';
import * as THREE from 'three';
import { defineComponent, onMounted, ref } from 'vue';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { MoonMap } from '../engine/map-renderer/map';
import { DebugGUI } from '../engine/debug';
import { Mesh } from 'three';

export default defineComponent({
    setup() {
        const mapRef = ref<HTMLElement | null>(null);
        let renderer: THREE.WebGLRenderer;
        let scene: THREE.Scene;
        let camera: THREE.PerspectiveCamera; // Используем перспективную камеру
        let controls: OrbitControls;
        let map: MoonMap;

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
            // Равное расстояние по всем осям создает истинный изометрический вид
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
            controls.zoomSpeed = 2.5; // скорость приближения
            controls.enableRotate = true;
            controls.enableDamping = false; // true для плавного движения камеры
                                            // но слишком желейно и неудобно  
            controls.dampingFactor = 0.05;  // оставлю фактор, но мне лень с ним играться 
            controls.screenSpacePanning = false;

            const debug: boolean = true;

            if(!debug){
            // Ограничение вращения камеры, чтобы пользователь не мог смотреть под карту
            camera.position.y = Math.max(camera.position.y, 1); // Keep camera above the map
            controls.minPolarAngle = Math.PI / 5; // Minimum angle (from top), about 30 degrees
            controls.maxPolarAngle = Math.PI / 3.5; // Slightly less than 90 degrees to prevent looking from below
            controls.addEventListener('change', () => {
            console.log('Camera position:', {
                x: camera.position.x.toFixed(2),
                y: camera.position.y.toFixed(2),
                z: camera.position.z.toFixed(2)
            });
            console.log('Distance from center:', camera.position.length().toFixed(2));
            });
            // Add event listener to log camera position when controls are changed

            // Add zoom limits to prevent getting too close or too far from the map
            controls.minDistance = 8; // Minimum distance (can't get closer than this)
            controls.maxDistance = 20; // Maximum distance (can't zoom out further than this)
            }

            // Добавляем координатную сетку для лучшей ориентации
            const gridHelper = new THREE.GridHelper();
            scene.add(gridHelper);

            // Добавляем координатные оси
            const axesHelper = new THREE.AxesHelper(5);
            scene.add(axesHelper);
            // Map setup

            // Добавляем освещение 
            const ambientLight = new THREE.AmbientLight(0xffffff, 1);
            ambientLight.position.set(10,10, 10);
            scene.add(ambientLight);

            

            const directionalLight = new THREE.DirectionalLight(0xffffff, 2);
            directionalLight.position.set(10, 15, 10);
            // Настраиваем тени для света
            directionalLight.castShadow = true;
            // Улучшаем качество теней
            directionalLight.shadow.mapSize.width = 1024;
            directionalLight.shadow.mapSize.height = 1024;
            directionalLight.shadow.camera.near = 0.5;
            directionalLight.shadow.camera.far = 50;
            directionalLight.shadow.bias = -0.001;
            // Настраиваем область для теней
            directionalLight.shadow.camera.left = -10;
            directionalLight.shadow.camera.right = 10;
            directionalLight.shadow.camera.top = 10;
            directionalLight.shadow.camera.bottom = -10;
            scene.add(directionalLight);

            const shadowCameraHelper = new THREE.CameraHelper(directionalLight.shadow.camera);
scene.add(shadowCameraHelper);

            // Настраиваем объекты карты для отбрасывания теней
            map = new MoonMap("Test");
      
            for (let i: number = 0; i < 10; i++) {
                for (let j: number = 0; j < 10; j++) {
                    let mesh : Mesh = map.getCellMesh(i, j);
                    mesh.position.x = i - 4.5;
                    mesh.position.z = j - 4.5;
                    // shadow setup
                    mesh.castShadow = true;
                    mesh.receiveShadow = true;
                    // shadows must be here, too
                    scene.add(mesh);
                    
                }

            }

            // Передаем renderer вместо mapRef.value
            map.setupClickHandler(camera, renderer);
            // Устанавливаем castShadow и receiveShadow для всех дочерних объектов карты
            // mapObj.traverse((child) => {
            //     if (child instanceof THREE.Mesh) {
            //         child.castShadow = true;     // Меш отбрасывает тени
            //         child.receiveShadow = true;  // И тот же меш принимает тени

            //         // Убедимся, что материал поддерживает тени
            //         if (child.material) {
            //             // Для MeshBasicMaterial тени не работают хорошо
            //             // Если это базовый материал, заменим его на стандартный
            //             if (child.material instanceof THREE.MeshBasicMaterial) {
            //                 const color = child.material.color;
            //                 child.material = new THREE.MeshStandardMaterial({
            //                     color: color,
            //                     roughness: 0.7,
            //                     metalness: 0.2
            //                 });
            //             }
            //         }
            //     }
            // });
            //scene.add(mapObj);

            // Add a simple cube to the scene
            const cubeGeometry = new THREE.BoxGeometry(1, 1, 1);
            const cubeMaterial = new THREE.MeshStandardMaterial({ 
                color: 0x00ff00,
                roughness: 0.7,
                metalness: 0.2
            });
            const cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
            cube.position.set(0, 0.5, 0); // Position the cube at (0, 0.5, 0) so it sits on the grid
            cube.castShadow = true;
            cube.receiveShadow = true;
            //scene.add(cube);

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
            // Обновляем параметры камеры
            camera.aspect = aspect;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        };

        const animate = () => {
            requestAnimationFrame(animate);
            // Обновляем контролы
            controls.update();
            renderer.render(scene, camera);
        };

        return {
            mapRef
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