import {
  Material,
  Mesh,
  MeshStandardMaterial,
  BoxGeometry,
  TextureLoader,
  Camera,
  WebGLRenderer,
  Raycaster,
  Vector2,
  Texture,
} from 'three'
import { mul } from 'three/tsl'
import { useModuleInfoStore } from '@/stores/moduleInfoStore'

export class MoonCell {
  x: number
  y: number
  mesh: Mesh
  material: Material

  constructor(x: number, y: number, texture?: Texture) {
    this.x = x
    this.y = y

    const height = 0.1
    const geometry = new BoxGeometry(1, height, 1)
    const color = this.getLunarColor()
    this.material = new MeshStandardMaterial({
      color: color,
      roughness: 0.8,
      metalness: 0.1,
      map: texture ?? undefined,
    })
    this.mesh = new Mesh(geometry, this.material)

    // Поднимаем блок так, чтобы его нижняя грань была на уровне y=0
    this.mesh.position.y = height / 2

    // Сохраняем данные в userData для доступа через raycaster
    this.mesh.userData = {
      x: x,
      y: y,
      cellReference: this,
      originalHeight: height,
    }
  }

  // Генерация цвета лунной поверхности
  getLunarColor(): number {
    // Оттенки серого для лунной поверхности
    const baseValue = Math.floor(Math.random() * 30 + 60) // от 60 до 90
    const hexValue = baseValue.toString(16).repeat(3) // повторяем для R, G и B
    return parseInt(hexValue, 16)
  }
}

export class MoonMap {
  name: string
  map: MoonCell[][]
  selectedCell: MoonCell | null = null
  selectedMesh: Mesh | null = null // Для хранения выделенного модуля

  constructor(name: string = 'Test') {
    this.name = name
    this.map = Array(10)
      .fill(null)
      .map(() => Array(10).fill(null))

    const textureLoader = new TextureLoader()
    const cellTexture = textureLoader.load('/textures/moon-texture.png')

    // Генерация карты
    for (let i: number = 0; i < 10; i++) {
      for (let j: number = 0; j < 10; j++) {
        this.map[i][j] = new MoonCell(i, j, cellTexture)
      }
    }
  }

  getCellMesh(i: number, j: number): Mesh {
    return this.map[i][j].mesh
  }

  // Получение данных о ячейке
  getCellData(x: number, z: number) {
    if (this.isValidCoord(x, z)) {
      return null //this.map[x][z].data
    }
    return null
  }

  // Обновление данных о ячейке
  updateCellData(x: number, z: number): boolean {
    if (this.isValidCoord(x, z)) {
      //this.map[x][z].updateData(newData)
      return true
    }
    return false
  }

  // Проверка валидности координат
  isValidCoord(x: number, z: number): boolean {
    return x >= 0 && x < this.map.length && z >= 0 && z < this.map[0].length
  }

  // Настройка обработчика клика с колбэком для UI
  setupClickHandler(
    camera: Camera,
    renderer: WebGLRenderer,
    multiCell?: (x: number, z: number) => boolean,
  ) {
    const raycaster = new Raycaster()
    const mouse = new Vector2()
    const moduleInfoStore = useModuleInfoStore()

    // Обработчик клика
    const onClick = (event: MouseEvent) => {
      // Получаем размеры и положение canvas
      const canvasRect = renderer.domElement.getBoundingClientRect()

      // Вычисляем координаты мыши относительно canvas
      mouse.x = ((event.clientX - canvasRect.left) / canvasRect.width) * 2 - 1
      mouse.y = -((event.clientY - canvasRect.top) / canvasRect.height) * 2 + 1

      // Установка направления луча от камеры через точку клика
      raycaster.setFromCamera(mouse, camera)

      // Получение всех мешей на карте для проверки пересечений
      const meshes = this.getAllMeshes()

      // Проверка пересечений
      const intersects = raycaster.intersectObjects(meshes)

      if (intersects.length > 0) {
        const clickedMesh = intersects[0].object as Mesh

        // --- Если клик по модульному мэшу ---
        if (clickedMesh.userData && clickedMesh.userData.module) {
          // Снимаем подсветку с предыдущего выделенного модуля
          if (this.selectedMesh) {
            this.unhighlightCell(this.selectedMesh)
            this.selectedMesh = null
          }
          // Подсвечиваем новый мэш
          this.highlightCell(clickedMesh)
          this.selectedMesh = clickedMesh
          // Передаём данные модуля в стор
          moduleInfoStore.setSelectedModule(clickedMesh.userData.module)
          return // Не продолжаем обработку как обычную ячейку
        }

        // Найдена ячейка, на которую кликнули
        const cell = this.findCellByMesh(clickedMesh)

        // Снимаем выделение с предыдущей выбранной области (если была)
        if (this.selectedCell) {
          // Снимаем подсветку со всех ячеек 2x2 вокруг предыдущей выбранной
          for (let dx = 0; dx < 2; dx++) {
            for (let dz = 0; dz < 2; dz++) {
              const prevX = this.selectedCell.x + dx
              const prevZ = this.selectedCell.y + dz
              if (this.isValidCoord(prevX, prevZ)) {
                this.unhighlightCell(this.map[prevX][prevZ])
              }
            }
          }
          this.selectedCell = null
        }

        // Вызов колбэка для обновления UI
        if (multiCell) {
          if (multiCell(cell.x, cell.y)) {
            // Подсвечиваем новую область 2x2
            for (let dx = 0; dx < 2; dx++) {
              for (let dz = 0; dz < 2; dz++) {
                const nx = cell.x + dx
                const nz = cell.y + dz
                if (this.isValidCoord(nx, nz)) {
                  this.highlightCell(this.map[nx][nz])
                }
              }
            }
            // Сохраняем левый верхний угол выделения
            this.selectedCell = cell
          } else {
            // Если колбэк не передан, выделяем только одну ячейку
            this.highlightCell(cell)
            this.selectedCell = cell
          }
        }
      }
    }

    // Добавление обработчика события к canvas
    renderer.domElement.addEventListener('click', onClick, false)

    // Возвращаем функцию для удаления обработчика при необходимости
    return () => {
      renderer.domElement.removeEventListener('click', onClick, false)
    }
  }

  // Получение всех мешей карты для raycaster
  getAllMeshes(): Mesh[] {
    const meshes: Mesh[] = []
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        meshes.push(this.map[i][j].mesh)
      }
    }
    return meshes
  }

  // Поиск ячейки по мешу
  findCellByMesh(mesh: Mesh): MoonCell | null {
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        if (this.map[i][j].mesh === mesh) {
          return this.map[i][j]
        }
      }
    }
    return null
  }

  // Подсветка выбранной ячейки или мэша
  highlightCell(cellOrMesh: MoonCell | Mesh): void {
    const mat =
      cellOrMesh instanceof Mesh
        ? (cellOrMesh.material as MeshStandardMaterial)
        : (cellOrMesh.material as MeshStandardMaterial)
    mat.emissive.set(0x333333)
    mat.needsUpdate = true
  }

  // Снятие подсветки с ячейки или мэша
  unhighlightCell(cellOrMesh: MoonCell | Mesh): void {
    const mat =
      cellOrMesh instanceof Mesh
        ? (cellOrMesh.material as MeshStandardMaterial)
        : (cellOrMesh.material as MeshStandardMaterial)
    mat.emissive.set(0x000000)
    mat.needsUpdate = true
  }

  // Обработка клика по ячейке (для обратной совместимости)
  handleCellClick(cell: MoonCell) {
    console.log(
      `Clicked on cell at position [${cell.x}, ${cell.y}] in coords (${cell.mesh.position.x},${cell.mesh.position.z})`,
    )
    this.highlightCell(cell)
  }

  // Обработка двойного клика с эффектом на соседних клетках (старый метод)
  handleDoubleCell(cell: MoonCell) {
    // Обработка соседних ячеек с проверкой границ
    if (this.isValidCoord(cell.x, cell.y - 1)) {
      const neighborCell = this.map[cell.x][cell.y - 1]
      ;(neighborCell.material as MeshStandardMaterial).color.set('#212121')
      neighborCell.material.needsUpdate = true
    }

    ;(cell.material as MeshStandardMaterial).color.set('#212121')
    cell.material.needsUpdate = true
  }

  // Метод для обновления высоты всех ячеек (для UI контролов)
  updateCellHeight(heightScale: number) {
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        const cell = this.map[i][j]
        const originalHeight = cell.mesh.userData.originalHeight

        // Обновляем масштаб по Y для изменения высоты
        cell.mesh.scale.y = heightScale

        // Обновляем позицию Y, чтобы основание оставалось на уровне земли
        cell.mesh.position.y = (originalHeight * heightScale) / 2
      }
    }
  }

  // Получение всей карты
  getMap(): MoonCell[][] {
    return this.map
  }

  // Метод для получения соседних ячеек
  getNeighbors(x: number, z: number): MoonCell[] {
    const neighbors: MoonCell[] = []

    // Проверяем все 4 соседние клетки (можно расширить до 8)
    const directions = [
      { dx: 0, dz: -1 }, // верх
      { dx: 1, dz: 0 }, // право
      { dx: 0, dz: 1 }, // низ
      { dx: -1, dz: 0 }, // лево
    ]

    for (const dir of directions) {
      const newX = x + dir.dx
      const newZ = z + dir.dz

      if (this.isValidCoord(newX, newZ)) {
        neighbors.push(this.map[newX][newZ])
      }
    }

    return neighbors
  }
}
