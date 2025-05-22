<script setup lang="ts">
const emit = defineEmits(['cell-selected']);
import { ref, onMounted } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { MoonMap } from '../engine/map-renderer/map'
import { Mesh } from 'three'
import { RGBELoader } from 'three/examples/jsm/loaders/RGBELoader.js'
import { useCellStore } from '@/stores/cellStore'
import { useSelectedCellStore } from '@/stores/selectedCellStore'
import { useZoneStore } from '@/stores/zoneStore';

const cellStore = useCellStore()
const mapRef = ref<HTMLElement | null>(null)
let renderer: THREE.WebGLRenderer
let scene: THREE.Scene
let camera: THREE.PerspectiveCamera
let controls: OrbitControls
let map: MoonMap
let gridHelper: THREE.GridHelper
let axesHelper: THREE.AxesHelper
let directionalLight: THREE.DirectionalLight
const selectedCellStore = useSelectedCellStore()
const zoneStore = useZoneStore()

const multipleView = false

let hoveredMesh: Mesh | null = null;
let selectedMesh: Mesh | null = null;
let lastBorderMeshes: Mesh[] = [];

onMounted(() => {

  const zone = zoneStore.current_zone;
  if (!zone) {
    console.error('zone is null');
    return;
  }
  console.log(zone.name + ' size:' + zone.size);

  if (!mapRef.value) {
    console.error('mapRef is null')
    return
  }

  renderer = new THREE.WebGLRenderer({ antialias: true })
  renderer.setSize(window.innerWidth, window.innerHeight)
  renderer.shadowMap.enabled = true
  renderer.shadowMap.type = THREE.PCFSoftShadowMap
  mapRef.value.appendChild(renderer.domElement)

  scene = new THREE.Scene()
  scene.background = new THREE.Color(0x111111)

  const hdrLoader = new RGBELoader()
  hdrLoader.load(
    '/render-background.hdr',
    (texture) => {
      texture.mapping = THREE.EquirectangularReflectionMapping
      scene.background = texture
      scene.environment = texture
    },
    undefined,
    (error) => {
      console.error('Ошибка загрузки HDRI:', error)
    }
  )

  const aspect = window.innerWidth / window.innerHeight
  camera = new THREE.PerspectiveCamera(50, aspect, 0.1, 1000)
  camera.position.set(10, 10, 10)
  camera.lookAt(0, 0, 0)

  controls = new OrbitControls(camera, renderer.domElement)
  controls.mouseButtons = {
    LEFT: THREE.MOUSE.PAN,
    MIDDLE: THREE.MOUSE.DOLLY,
    RIGHT: THREE.MOUSE.ROTATE
  }
  controls.zoomSpeed = 2.5
  controls.enableRotate = true
  controls.enableDamping = false
  controls.dampingFactor = 0.05
  controls.screenSpacePanning = false

  // Ограничения камеры
  camera.position.y = Math.max(camera.position.y, 1)
  controls.minPolarAngle = Math.PI / 5
  controls.maxPolarAngle = Math.PI / 3.5
  controls.minDistance = 8
  controls.maxDistance = 20

  gridHelper = new THREE.GridHelper()
  scene.add(gridHelper)

  axesHelper = new THREE.AxesHelper(5)
  scene.add(axesHelper)

  const ambientLight = new THREE.AmbientLight(0xffffff, 1)
  ambientLight.position.set(10, 10, 10)
  scene.add(ambientLight)

  directionalLight = new THREE.DirectionalLight(0xffffff, 2)
  directionalLight.position.set(10, 15, 10)
  directionalLight.castShadow = true
  directionalLight.shadow.mapSize.width = 1024
  directionalLight.shadow.mapSize.height = 1024
  directionalLight.shadow.camera.near = 0.5
  directionalLight.shadow.camera.far = 50
  directionalLight.shadow.bias = -0.001
  directionalLight.shadow.camera.left = -10
  directionalLight.shadow.camera.right = 10
  directionalLight.shadow.camera.top = 10
  directionalLight.shadow.camera.bottom = -10
  scene.add(directionalLight)

  // Карта
  map = new MoonMap(zone.name, zone.size)
  const offset = map.size == 20 ? 9 : 4.5; // так как у нас только 10х10 и 20х20
  for (let i = 0; i < map.size; i++) {
    for (let j = 0; j < map.size; j++) {
      const mesh: Mesh = map.getCellMesh(i, j)
      mesh.userData = {
        i,
        j,
        originalColor: (mesh.material as THREE.MeshStandardMaterial).color.clone()
      }
      mesh.position.x = i - offset // если карта 10х10, то 4,5
      mesh.position.z = j - offset
      mesh.castShadow = true
      mesh.receiveShadow = true
      scene.add(mesh)
    }
  }

  renderer.domElement.addEventListener('mousemove', handleMeshHover);
  renderer.domElement.addEventListener('click', handleMeshClick)

  window.addEventListener('resize', onWindowResize)
  animate()
})

function resetHoverMesh() {
  if (hoveredMesh && hoveredMesh.userData && hoveredMesh.userData.originalColor) {
    (hoveredMesh.material as THREE.MeshStandardMaterial).color.copy(hoveredMesh.userData.originalColor);
    hoveredMesh = null;
  }
  // Если ячейка выбрана кликом, вернуть ей цвет выделения
  if (selectedMesh) {
    (selectedMesh.material as THREE.MeshStandardMaterial).color.set(0xbcfe37);
  }
  // Вернуть цвет границ, если они есть
  lastBorderMeshes.forEach(mesh => {
    if (mesh !== selectedMesh && mesh.userData && mesh.userData.originalColor) {
      (mesh.material as THREE.MeshStandardMaterial).color.copy(mesh.userData.originalColor);
    }
  });
}

function handleMeshHover(event: MouseEvent) {
  if (!renderer || !camera || !map) return;

  const rect = renderer.domElement.getBoundingClientRect();
  const mouse = new THREE.Vector2(
    ((event.clientX - rect.left) / rect.width) * 2 - 1,
    -((event.clientY - rect.top) / rect.height) * 2 + 1
  );

  const raycaster = new THREE.Raycaster();
  raycaster.setFromCamera(mouse, camera);

  const meshes: Mesh[] = [];
  for (let i = 0; i < map.size; i++) {
    for (let j = 0; j < map.size; j++) {
      meshes.push(map.getCellMesh(i, j));
    }
  }

  const intersects = raycaster.intersectObjects(meshes);

  if (intersects.length > 0) {
    const mesh = intersects[0].object as Mesh;
    if (hoveredMesh && hoveredMesh !== mesh && hoveredMesh !== selectedMesh && hoveredMesh.userData.originalColor) {
      (hoveredMesh.material as THREE.MeshStandardMaterial).color.copy(hoveredMesh.userData.originalColor);
    }
    if (mesh !== selectedMesh) {
      (mesh.material as THREE.MeshStandardMaterial).color.set(0xbcfe37); // hover color
    }
    hoveredMesh = mesh;
    // Подсветка границ вокруг наведённой ячейки (пример: 3x3)
    //highlightBorderMeshes(mesh, 2, 2); // цвет для границы при hover
  } else {
    resetHoverMesh();
    // Сбросить подсветку границ
    highlightBorderMeshes({ userData: {} } as Mesh, 0, 0);
  }
}

function handleMeshClick(event: MouseEvent) {
  if (!renderer || !camera || !map) return;

  const rect = renderer.domElement.getBoundingClientRect();
  const mouse = new THREE.Vector2(
    ((event.clientX - rect.left) / rect.width) * 2 - 1,
    -((event.clientY - rect.top) / rect.height) * 2 + 1
  );

  const raycaster = new THREE.Raycaster();
  raycaster.setFromCamera(mouse, camera);

  const meshes: Mesh[] = [];
  for (let i = 0; i < map.size; i++) {
    for (let j = 0; j < map.size; j++) {
      meshes.push(map.getCellMesh(i, j));
    }
  }

  const intersects = raycaster.intersectObjects(meshes);

  if (intersects.length > 0) {
    const mesh = intersects[0].object as Mesh;
    // Сбросить предыдущий выделенный
    if (selectedMesh && selectedMesh.userData.originalColor) {
      (selectedMesh.material as THREE.MeshStandardMaterial).color.copy(selectedMesh.userData.originalColor);
    }
    selectedMesh = mesh;
    (mesh.material as THREE.MeshStandardMaterial).color.set(0xbcfe37); // выделение
    emit('cell-selected', mesh.userData.i, mesh.userData.j);
    // Подсветка границ вокруг выбранной ячейки (пример: 3x3)
    highlightBorderMeshes(mesh, 3, 3, 0xbcfe37); // цвет для границы при клике
  } else {
    if (selectedMesh && selectedMesh.userData.originalColor) {
      (selectedMesh.material as THREE.MeshStandardMaterial).color.copy(selectedMesh.userData.originalColor);
    }
    selectedMesh = null;
    emit('cell-selected', -1, -1);
    // Сбросить подсветку границ
    highlightBorderMeshes({ userData: {} } as Mesh, 0, 0);
  }
}

function highlightBorderMeshes(centerMesh: Mesh, n: number, m: number, borderColor = 0xbcfe37) {
  // Сбросить цвет всем, кто был подсвечен ранее, кроме выбранной
  lastBorderMeshes.forEach(mesh => {
    if (mesh !== selectedMesh && mesh.userData && mesh.userData.originalColor) {
      (mesh.material as THREE.MeshStandardMaterial).color.copy(mesh.userData.originalColor);
    }
  });

  // Получаем новые граничные меши
  const borderMeshes = getBorderMeshesAround(centerMesh, n, m);

  // Сохраняем новые подсвеченные меши
  lastBorderMeshes = borderMeshes;
  borderMeshes.forEach(mesh => {
    if (mesh !== selectedMesh) {
      (mesh.material as THREE.MeshStandardMaterial).color.set(borderColor);
    }
  });
}

function getBorderMeshesAround(centerMesh: Mesh, n: number, m: number): Mesh[] {
  if (!centerMesh.userData) return [];
  const { i: i0, j: j0 } = centerMesh.userData;
  const borderMeshes: Mesh[] = [];

  const iStart = Math.max(0, i0 - Math.floor((n - 1) / 2));
  const iEnd = Math.min(map.size - 1, iStart + n - 1);
  const jStart = Math.max(0, j0 - Math.floor((m - 1) / 2));
  const jEnd = Math.min(map.size - 1, jStart + m - 1);

  for (let i = iStart; i <= iEnd; i++) {
    for (let j = jStart; j <= jEnd; j++) {
      if (i === iStart || i === iEnd || j === jStart || j === jEnd) {
        borderMeshes.push(map.getCellMesh(i, j));
      }
    }
  }
  return borderMeshes;
}

function onWindowResize() {
  const aspect = window.innerWidth / window.innerHeight
  camera.aspect = aspect
  camera.updateProjectionMatrix()
  renderer.setSize(window.innerWidth, window.innerHeight)
}

function animate() {
  requestAnimationFrame(animate)
  controls.update()
  renderer.render(scene, camera)
}

defineExpose({
})
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
