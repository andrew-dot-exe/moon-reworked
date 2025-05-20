<script setup lang="ts">
const emit = defineEmits(['cell-selected']);
import { ref, onMounted } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { MoonMap } from '../engine/map-renderer/map'
import { Mesh } from 'three'
import { RGBELoader } from 'three/examples/jsm/loaders/RGBELoader.js'
import { BaseModule } from '../engine/map-renderer/baseModule'
import { useCellStore } from '@/stores/cellStore'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js'
import type { TypeModule } from '@/components/typeModules/typeModules'
import { useModuleInfoStore } from '@/stores/moduleInfoStore'
import { useSelectedCellStore } from '@/stores/selectedCellStore'

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
let selectedMesh: THREE.Object3D | null = null
const selectedCellStore = useSelectedCellStore()

function placeModule() {
  // Функция размещения модуля на карту
  // и добавления информации о нем в стор

}

function placeMeshOnCell(mesh: THREE.Mesh, x: number, z: number) {
  // Получаем высоту ячейки (если нужно разместить объект на поверхности)
  const cell = map.map[x][z]
  const cellHeight = cell ? cell.mesh.position.y : 0

  mesh.geometry.computeBoundingBox?.()
  const bbox = mesh.geometry.boundingBox
  const meshBaseY = bbox ? bbox.min.y : 0


  // Смещаем mesh в нужную позицию
  mesh.position.x = x - 4.5
  mesh.position.z = z - 4.5
  mesh.position.y = 0.11 // настройка для каждого плоского мэша (plane, etc)

  console.log(mesh.position.y)



  scene.add(mesh)
}


function canPlaceMultiCellModule(x: number, z: number, width: number, height: number): boolean {
  // Проверяем, что все ячейки внутри карты
  if (
    x < 0 ||
    z < 0 ||
    x + width > map.map.length ||
    z + height > map.map[0].length
  ) {
    return false
  }
  return true
}

function placeMultiCellMeshOnGrid(mesh: THREE.Mesh, x: number, z: number, width: number, height: number) {
  if (!canPlaceMultiCellModule(x, z, width, height)) {
    console.warn('Модуль не помещается на карте по этим координатам')
    return
  }
  // Найдём максимальную высоту среди всех ячеек, которые занимает модуль
  let maxCellHeight = 0
  for (let dx = 0; dx < width; dx++) {
    for (let dz = 0; dz < height; dz++) {
      const cell = map.map[x + dx]?.[z + dz]
      if (cell && cell.mesh.position.y > maxCellHeight) {
        maxCellHeight = cell.mesh.position.y
      }
    }
  }

  mesh.geometry.computeBoundingBox?.()
  const bbox = mesh.geometry.boundingBox
  const meshBaseY = bbox ? bbox.min.y : 0

  // Центрируем mesh относительно занимаемой области
  mesh.position.x = x + (width - 1) / 2 - 4.5
  mesh.position.z = z + (height - 1) / 2 - 4.5
  mesh.position.y = maxCellHeight - meshBaseY

  mesh.castShadow = true
  mesh.receiveShadow = true

  scene.add(mesh)
}

function placeManufactureModule(x: number, z: number, moduleData: TypeModule, width = 1, height = 1) {
  const loader = new GLTFLoader()
  loader.load('/models/manufacture.glb', (gltf) => {
    gltf.scene.userData.module = moduleData
    gltf.scene.userData.cellX = x
    gltf.scene.userData.cellZ = z
    // Вычисляем bounding box всей сцены
    const box = new THREE.Box3().setFromObject(gltf.scene)
    const size = new THREE.Vector3()
    box.getSize(size)
    // Коэффициенты для подгонки под нужное количество ячеек
    const scaleX = width / (size.x || 1)
    const scaleZ = height / (size.z || 1)
    // Масштаб по Y сохраняет пропорцию исходной модели
    gltf.scene.scale.set(scaleX, scaleX, scaleZ)
    // Центрируем относительно области
    gltf.scene.position.x = x + (width - 1) / 2 - 4.5
    gltf.scene.position.z = z + (height - 1) / 2 - 4.5
    gltf.scene.position.y = 0.11
    gltf.scene.traverse((obj) => {
      if (obj instanceof THREE.Mesh) {
        obj.castShadow = true
        obj.receiveShadow = true
        obj.userData.module = moduleData
      }
    })
    scene.add(gltf.scene)
  })
}

function handleMeshClick(event: MouseEvent) {
  if (!renderer || !camera) return
  const rect = renderer.domElement.getBoundingClientRect()
  const mouse = new THREE.Vector2(
    ((event.clientX - rect.left) / rect.width) * 2 - 1,
    -((event.clientY - rect.top) / rect.height) * 2 + 1
  )
  const raycaster = new THREE.Raycaster()
  raycaster.setFromCamera(mouse, camera)
  const intersects = raycaster.intersectObjects(scene.children, true)
  if (intersects.length > 0) {
    const obj = intersects[0].object
    // Снимаем выделение с предыдущего
    if (selectedMesh) {
      selectedMesh.traverse((o: THREE.Object3D) => {
        if (o instanceof THREE.Mesh && o.material && 'emissive' in o.material) {
          (o.material as THREE.MeshStandardMaterial).emissive.set(0x000000)
        }
      })
    }
    // Если клик по модулю
    if (obj.userData && obj.userData.module) {
      obj.traverse((o: THREE.Object3D) => {
        if (o instanceof THREE.Mesh && o.material && 'emissive' in o.material) {
          (o.material as THREE.MeshStandardMaterial).emissive.set(0x333333)
        }
      })
      selectedMesh = obj
      // Сохраняем в стор
      const moduleInfoStore = useModuleInfoStore()
      moduleInfoStore.setSelectedModule(obj.userData.module)
      // Если есть координаты ячейки — выбрать ячейку
      if (typeof obj.userData.cellX === 'number' && typeof obj.userData.cellZ === 'number' && map) {
        const cell = map.map[obj.userData.cellX]?.[obj.userData.cellZ]
        if (cell) {
          selectedCellStore.setSelectedCell(cell)
        }
      }
    } else {
      selectedMesh = null
      // Если клик не по модулю — снять выделение
      const moduleInfoStore = useModuleInfoStore()
      moduleInfoStore.clearSelectedModule()
      selectedCellStore.clearSelectedCell()
    }
  }
}

onMounted(() => {
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
  map = new MoonMap('Test')
  for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 10; j++) {
      const mesh: Mesh = map.getCellMesh(i, j)
      mesh.position.x = i - 4.5
      mesh.position.z = j - 4.5
      mesh.castShadow = true
      mesh.receiveShadow = true
      scene.add(mesh)
    }
  }

  map.setupClickHandler(camera, renderer, (x, z) => {
    // Например, выбранный модуль 2x2
    const width = 2, height = 2
    if (canPlaceMultiCellModule(x, z, width, height)) {
      cellStore.selectCell(x, z) // сохранить координаты
      return true
    }
    return false
  })

  // Обработчик клика
  map.setupClickHandler(camera, renderer, (x: number, z: number) => {
    // кастомные методы
    cellStore.selectCell(x, z);
    emit('cell-selected', x, z);
    return false
  })

  renderer.domElement.addEventListener('click', handleMeshClick)

  window.addEventListener('resize', onWindowResize)
  animate()
})

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


// импорт внешних методов проходил так, скорее всего, больше не нужно
defineExpose({
  placeMeshOnCell,
  placeManufactureModule,
  placeMultiCellMeshOnGrid
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
