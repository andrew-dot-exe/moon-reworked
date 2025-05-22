import { Material, Mesh, MeshStandardMaterial, BoxGeometry, TextureLoader, Texture } from 'three'

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
      originalColor: color,
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
  size: number

  constructor(name: string = 'Test', size: number = 20) {
    this.name = name
    this.size = size
    this.map = Array(this.size)
      .fill(null)
      .map(() => Array(this.size).fill(null))

    const textureLoader = new TextureLoader()
    const cellTexture = textureLoader.load('/textures/moon-texture.png')

    // Генерация карты
    for (let i: number = 0; i < this.size; i++) {
      for (let j: number = 0; j < this.size; j++) {
        this.map[i][j] = new MoonCell(i, j, cellTexture)
      }
    }
  }

  getCellMesh(i: number, j: number): Mesh {
    return this.map[i][j].mesh
  }
}
