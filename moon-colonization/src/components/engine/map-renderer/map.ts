import {
  Material,
  Mesh,
  MeshBasicMaterial,
  MeshStandardMaterial,
  PlaneGeometry,
  BoxGeometry,
  Scene,
  TextureLoader,
  Camera,
  WebGLRenderer,
  Raycaster,
  Vector2
} from "three";

class MoonCell {
  x: number;
  y: number;
  mesh: Mesh;
  material: Material;
  data: {
    height: number;
    type: string;
    resources: string;
    hasStructure: boolean;
    explored: boolean;
  };

  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
    
    // Генерация случайной высоты для ячейки
    const height = Math.random() * 0.5 + 0.1;
    
    // Создаем данные о ячейке
    this.data = {
      height: height,
      type: this.getRandomTerrainType(),
      resources: Math.random() > 0.8 ? this.getRandomResource() : '',
      hasStructure: false,
      explored: false
    };
    
    // Создаем геометрию блока с полученной высотой
    const geometry = new BoxGeometry(1, height, 1);
    
    // Генерация случайного оттенка серого для лунной поверхности
    const color = this.getLunarColor();
    this.material = new MeshStandardMaterial({ 
      color: color,
      roughness: 0.8,
      metalness: 0.1
    });
    
    this.mesh = new Mesh(geometry, this.material);
    
    // Поднимаем блок так, чтобы его нижняя грань была на уровне y=0
    this.mesh.position.y = height / 2;
    
    // Сохраняем данные в userData для доступа через raycaster
    this.mesh.userData = {
      x: x,
      y: y,
      cellReference: this,
      originalHeight: height
    };
  }

  // Генерация случайного типа местности
  getRandomTerrainType(): string {
    const types = ['Равнина', 'Кратер', 'Горы', 'Пыль', 'Камни'];
    return types[Math.floor(Math.random() * types.length)];
  }
  
  // Генерация случайного ресурса
  getRandomResource(): string {
    const resources = ['Вода (лед)', 'Титан', 'Железо', 'Гелий-3', 'Редкие металлы'];
    return resources[Math.floor(Math.random() * resources.length)];
  }
  
  // Генерация цвета лунной поверхности
  getLunarColor(): number {
    // Оттенки серого для лунной поверхности
    const baseValue = Math.floor(Math.random() * 30 + 60); // от 60 до 90
    const hexValue = baseValue.toString(16).repeat(3); // повторяем для R, G и B
    return parseInt(hexValue, 16);
  }
  
  // Метод для обновления данных ячейки
  updateData(newData: Partial<typeof this.data>): void {
    this.data = { ...this.data, ...newData };
    
    // Обновление визуального представления, если нужно
    if (newData.explored) {
      // Можно добавить визуальное выделение исследованных клеток
      (this.material as MeshStandardMaterial).color.setHex(
        parseInt(this.material.color.getHexString(), 16) + 0x111111
      );
    }
    
    this.material.needsUpdate = true;
  }
}

export class MoonMap {
  name: string;
  map: MoonCell[][];
  selectedCell: MoonCell | null = null;
  
  constructor(name: string = "Test") {
    this.name = name;
    this.map = Array(10).fill(null).map(() => Array(10).fill(null));
    
    // Генерация карты
    for(let i: number = 0; i < 10; i++) {
      for(let j: number = 0; j < 10; j++) {
        this.map[i][j] = new MoonCell(i, j);
      }
    }
  }

  getCellMesh(i: number, j: number): Mesh {
    return this.map[i][j].mesh;
  }
  
  // Получение данных о ячейке
  getCellData(x: number, z: number): any {
    if (this.isValidCoord(x, z)) {
      return this.map[x][z].data;
    }
    return null;
  }
  
  // Обновление данных о ячейке
  updateCellData(x: number, z: number, newData: Partial<MoonCell['data']>): boolean {
    if (this.isValidCoord(x, z)) {
      this.map[x][z].updateData(newData);
      return true;
    }
    return false;
  }
  
  // Проверка валидности координат
  isValidCoord(x: number, z: number): boolean {
    return x >= 0 && x < this.map.length && z >= 0 && z < this.map[0].length;
  }

  // Настройка обработчика клика с колбэком для UI
  setupClickHandler(
    camera: Camera, 
    renderer: WebGLRenderer,
    callback?: (x: number, z: number, cellData: any) => void
  ) {
    const raycaster = new Raycaster();
    const mouse = new Vector2();
  
    // Обработчик клика
    const onClick = (event: MouseEvent) => {
      // Получаем размеры и положение canvas
      const canvasRect = renderer.domElement.getBoundingClientRect();
      
      // Вычисляем координаты мыши относительно canvas
      mouse.x = ((event.clientX - canvasRect.left) / canvasRect.width) * 2 - 1;
      mouse.y = -((event.clientY - canvasRect.top) / canvasRect.height) * 2 + 1;
  
      // Установка направления луча от камеры через точку клика
      raycaster.setFromCamera(mouse, camera);
  
      // Получение всех мешей на карте для проверки пересечений
      const meshes = this.getAllMeshes();
      
      // Проверка пересечений
      const intersects = raycaster.intersectObjects(meshes);
  
      if (intersects.length > 0) {
        // Найдена ячейка, на которую кликнули
        const clickedMesh = intersects[0].object as Mesh;
        const cell = this.findCellByMesh(clickedMesh);
        
        if (cell) {
          // Снимаем выделение с предыдущей выбранной ячейки
          if (this.selectedCell && this.selectedCell !== cell) {
            this.unhighlightCell(this.selectedCell);
          }
          
          // Устанавливаем новую выбранную ячейку и подсвечиваем её
          this.selectedCell = cell;
          this.highlightCell(cell);
          
          // Вызов колбэка для обновления UI
          if (callback) {
            callback(cell.x, cell.y, cell.data);
          }
        }
      }
    };
  
    // Добавление обработчика события к canvas
    renderer.domElement.addEventListener('click', onClick, false);
    
    // Возвращаем функцию для удаления обработчика при необходимости
    return () => {
      renderer.domElement.removeEventListener('click', onClick, false);
    };
  }

  // Получение всех мешей карты для raycaster
  getAllMeshes(): Mesh[] {
    const meshes: Mesh[] = [];
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        meshes.push(this.map[i][j].mesh);
      }
    }
    return meshes;
  }

  // Поиск ячейки по мешу
  findCellByMesh(mesh: Mesh): MoonCell | null {
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        if (this.map[i][j].mesh === mesh) {
          return this.map[i][j];
        }
      }
    }
    return null;
  }

  // Подсветка выбранной ячейки
  highlightCell(cell: MoonCell): void {
    (cell.material as MeshStandardMaterial).emissive.set(0x333333);
    cell.material.needsUpdate = true;
  }
  
  // Снятие подсветки с ячейки
  unhighlightCell(cell: MoonCell): void {
    (cell.material as MeshStandardMaterial).emissive.set(0x000000);
    cell.material.needsUpdate = true;
  }

  // Обработка клика по ячейке (для обратной совместимости)
  handleCellClick(cell: MoonCell) {
    console.log(`Clicked on cell at position [${cell.x}, ${cell.y}] in coords (${cell.mesh.position.x},${cell.mesh.position.z})`);
    this.highlightCell(cell);
  }

  // Обработка двойного клика с эффектом на соседних клетках (старый метод)
  handleDoubleCell(cell: MoonCell) {
    // Обработка соседних ячеек с проверкой границ
    if (this.isValidCoord(cell.x, cell.y - 1)) {
      const neighborCell = this.map[cell.x][cell.y - 1];
      (neighborCell.material as MeshStandardMaterial).color.set('#212121');
      neighborCell.material.needsUpdate = true;
    }
    
    (cell.material as MeshStandardMaterial).color.set('#212121');
    cell.material.needsUpdate = true;
  }
  
  // Метод для обновления высоты всех ячеек (для UI контролов)
  updateCellHeight(heightScale: number) {
    for (let i = 0; i < this.map.length; i++) {
      for (let j = 0; j < this.map[i].length; j++) {
        const cell = this.map[i][j];
        const originalHeight = cell.mesh.userData.originalHeight;
        
        // Обновляем масштаб по Y для изменения высоты
        cell.mesh.scale.y = heightScale;
        
        // Обновляем позицию Y, чтобы основание оставалось на уровне земли
        cell.mesh.position.y = originalHeight * heightScale / 2;
      }
    }
  }
  
  // Метод для добавления структуры на карту
  addStructure(x: number, z: number, structureMesh: Mesh): boolean {
    if (!this.isValidCoord(x, z)) {
      return false;
    }
    
    const cell = this.map[x][z];
    if (cell.data.hasStructure) {
      return false; // На клетке уже есть структура
    }
    
    // Устанавливаем флаг наличия структуры
    cell.updateData({ hasStructure: true });
    
    return true;
  }
  
  // Получение всей карты
  getMap(): MoonCell[][] {
    return this.map;
  }
  
  // Метод для получения соседних ячеек
  getNeighbors(x: number, z: number): MoonCell[] {
    const neighbors: MoonCell[] = [];
    
    // Проверяем все 4 соседние клетки (можно расширить до 8)
    const directions = [
      { dx: 0, dz: -1 }, // верх
      { dx: 1, dz: 0 },  // право
      { dx: 0, dz: 1 },  // низ
      { dx: -1, dz: 0 }  // лево
    ];
    
    for (const dir of directions) {
      const newX = x + dir.dx;
      const newZ = z + dir.dz;
      
      if (this.isValidCoord(newX, newZ)) {
        neighbors.push(this.map[newX][newZ]);
      }
    }
    
    return neighbors;
  }
}