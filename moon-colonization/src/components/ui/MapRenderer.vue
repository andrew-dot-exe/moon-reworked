<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/addons/controls/OrbitControls.js'
import { MoonMap } from '../engine/map-renderer/map'
import { Mesh } from 'three'
import { RGBELoader } from 'three/examples/jsm/loaders/RGBELoader.js'
import { BaseModule } from '../engine/map-renderer/baseModule'
import { useCellStore } from '@/stores/cellStore'

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
  mesh.position.y = cellHeight - meshBaseY

  mesh.castShadow = true
  mesh.receiveShadow = true

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

  // Обработчик клика
  map.setupClickHandler(camera, renderer, (x: number, z: number) => {
    // console.log(x, z)
    // const sampleCell = new BaseModule()
    // placeMeshOnCell(sampleCell.getMesh(), x, z)
    cellStore.selectCell(x, z);
    //emit('cell-selected', x, z)
  })

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
