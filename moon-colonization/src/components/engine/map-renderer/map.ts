import type { Module } from '@/components/modules/modules';
import { Material, Mesh, MeshStandardMaterial, BoxGeometry, TextureLoader, Texture } from 'three'
import * as THREE from 'three'
import { createMeshFromGLTF } from './baseModule';
import { Cell } from 'jspdf-autotable';

class TypeM{
  name: string
  size:{width: number, height: number}
  Y: number

  constructor(name: string, size:{width: number, height: number}, y: number){
    this.name = name
    this.size = size
    this.Y = y
  }
}

const modulesType = [
  new TypeM("livx", {width: 2, height: 1}, -1),
  new TypeM("livey", {width: 1, height: 2}, -1),
  new TypeM( "admin.glb", {width: 2, height: 2}, -1),
  new TypeM( "sport", {width: 1, height: 1}, -1),
  new TypeM( "med", {width: 1, height: 1}, -1),
  new TypeM("plantations.glb", {width: 3, height: 3}, -1),
  new TypeM( "search", {width: 1, height: 1}, -1),
  new TypeM( "search", {width: 1, height: 1}, -1),
  new TypeM( "search", {width: 1, height: 1}, -1),
  new TypeM( "search", {width: 1, height: 1}, -1),
  new TypeM("hallway", {width: 1, height: 1}, -1),
  new TypeM( "AM", {width: 2, height: 2}, 0),
  new TypeM( "solar", {width: 1, height: 1}, 0),
  new TypeM( "construction.glb", {width: 2, height: 2}, 0),
  new TypeM( "cosmic_base.glb", {width: 6, height: 6}, 0),
  new TypeM( "tower", {width: 1, height: 1}, 0),
  new TypeM( "trashcan.glb", {width: 2, height: 2}, 0),
  new TypeM( "trashcan.glb", {width: 2, height: 2}, 0),
  new TypeM( "manufacture.glb", {width: 2, height: 2}, 0),
  new TypeM( "manufacture.glb", {width: 2, height: 2}, 0),
  new TypeM( "asronomic", {width: 2, height: 2}, 0),
  new TypeM( "mine_base.glb", {width: 2, height: 2}, 0),
  new TypeM( "warehouse", {width: 2, height: 2}, 0),
  new TypeM( "warehouse", {width: 2, height: 2}, 0),
  new TypeM( "warehouse", {width: 2, height: 2}, 0),
  new TypeM( "warehouse", {width: 2, height: 2}, 0)
]

export class Module3D {
  id: number;
  type: number;
  model: THREE.Object3D;
  occupiedCells: {x: number, y: number}[]; // Ячейки, которые занимает модуль
  size: {width: number, height: number}; // Размеры в ячейках

  constructor(module: Module) {
    this.id = module.id
    this.type = module.moduleType;
    // this.model = createMeshFromGLTF(modulesType[module.typeModule].name);
    this.size = modulesType[module.moduleType].size;
    this.occupiedCells = this.calculateOccupiedCells({x: module.x, y:module.y});
    this.model = new THREE.Object3D();
  }
  static async create(module: Module): Promise<Module3D> {
    const instance = new Module3D(module);
    await instance.loadModel();
    return instance;
  }

  async loadModel() {
    try {
      const modelPath = modulesType[this.type].name;
      const model = await createMeshFromGLTF(modelPath);
      
      // Масштабируем модель под размер в ячейках
      this.scaleModelToCellSize(model);
      
      // Центрируем модель относительно занимаемых ячеек
      this.positionModel(model);
      
      this.model.add(model);
    } catch (error) {
      console.error(`Failed to load model for module ${this.id}:`, error);
      this.createFallbackModel();
    }
  }

  private scaleModelToCellSize(model: THREE.Object3D) {
    // 1. Сбросить текущий масштаб модели
    model.scale.set(1, 1, 1);
    
    // 2. Получить ограничивающую рамку модели в мировых координатах
    const bbox = new THREE.Box3().setFromObject(model);
    const size = new THREE.Vector3();
    bbox.getSize(size);
    
    // 3. Если модель имеет нулевой размер (на всякий случай)
    if (size.x === 0 || size.y === 0 || size.z === 0) {
      size.set(1, 1, 1); // Устанавливаем минимальный размер
    }
    
    // 4. Рассчитать масштаб для растягивания по ячейкам
    const targetWidth = this.size.width;  // Ширина в ячейках
    const targetDepth = this.size.height; // Глубина в ячейках
    
    // Сохраняем пропорции высоты (Y), растягиваем только по X и Z
    const scaleX = targetWidth / size.x;
    const scaleZ = targetDepth / size.z;
    
    // Применяем масштаб
    model.scale.set(scaleX, 1, scaleZ);
    
    // 5. Обновляем ограничивающую рамку после масштабирования
    bbox.setFromObject(model);
    bbox.getSize(size);
    
    // 6. Дополнительная корректировка положения
    model.position.y = -bbox.min.y; // Ставим модель "на землю"
  }

  private positionModel(model: THREE.Object3D) {
    // Получаем новые размеры после масштабирования
    const bbox = new THREE.Box3().setFromObject(model);
    const size = new THREE.Vector3();
    bbox.getSize(size);
    
    // Центрируем модель относительно занимаемых ячеек
    model.position.set(
      (this.size.width - size.x) / 2 - 0.5,  // Центрирование по X
      modulesType[this.type].Y,                               // Высота (Y)
      (this.size.height - size.z) / 2 - 0.5 // Центрирование по Z
    );
  }

  private createFallbackModel() {
    // Создаём простой куб как заглушку, масштабированный под размер модуля
    const geometry = new THREE.BoxGeometry(1, 1, 1);
    const material = new THREE.MeshBasicMaterial({ 
      color: 0xff0000,
      transparent: true,
      opacity: 0.7
    });
    const cube = new THREE.Mesh(geometry, material);
    
    this.scaleModelToCellSize(cube);
    this.positionModel(cube);
    
    this.model.add(cube);
  }

  private calculateOccupiedCells(position: {x: number, y: number}): {x: number, y: number}[] {
    const cells = [];
    for (let x = position.x; x < position.x + this.size.width; x++) {
      for (let y = position.y; y < position.y + this.size.height; y++) {
        cells.push({x, y});
      }
    }
    return cells;
  }
}

export class MoonCell {
  x: number
  y: number
  mesh: Mesh
  material: Material
  module: Module3D | null = null; // Ссылка на модуль

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
  modules: Module3D[] = []; 

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
    
  async addModule(module: Module3D) {
    // Проверяем, свободны ли все ячейки
    for (const cell of module.occupiedCells) {
      if (cell.x >= this.size || cell.y >= this.size || this.map[cell.x][cell.y].module) {
        return false; // Нельзя разместить модуль
      }
    }
    
    // Загружаем модель
    await module.loadModel();

    // Размещаем модуль
    this.modules.push(module);
    for (const cell of module.occupiedCells) {
      this.map[cell.x][cell.y].module = module;
     // this.map[cell.x][cell.y] = new MoonCell(0,0)
    }

    // Позиционируем модель модуля
    const offset = this.size == 20 ? 9 : 4.5;
    module.model.position.x = module.occupiedCells[0].x - offset + module.size.width / 2;
    module.model.position.z = module.occupiedCells[0].y - offset + module.size.height / 2;
    module.model.position.y = 0.1; // Немного выше поверхности

  }

  getCellMesh(i: number, j: number): Mesh {
    return this.map[i][j].mesh
  }
  getModuleAt(x: number, y: number): Module3D | null {
    if (x < 0 || y < 0 || x >= this.size || y >= this.size) return null;
    return this.map[x][y].module;
  }
}
