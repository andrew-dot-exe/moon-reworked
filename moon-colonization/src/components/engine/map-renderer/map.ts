import {
  Material,
  Mesh,
  MeshBasicMaterial,
  MeshStandardMaterial,
  PlaneGeometry,
  Scene,
  TextureLoader,
  Camera,
  Raycaster,
  Vector2
} from "three";


class MoonCell {
  x: number;
  y: number;

  mesh: Mesh;
  material: Material;

  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
    
    // Generate a random color for the material
    const randomColor = '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0'); // текстура
    this.material = new MeshBasicMaterial({ color: randomColor });
    this.mesh = new Mesh(new PlaneGeometry(1,1), this.material); // изменение размера не работает, надо делать адекватный ресайз
    this.mesh.rotation.x = -Math.PI / 2; // распологаем карту
        // Load the height map texture
    // const textureLoader = new TextureLoader();
    // textureLoader.load(
    //   "/heightmap.png", // Path to your height map image
    //   (texture: any) => {
    //     (this.material as MeshStandardMaterial).displacementMap = texture;
    //     // You might want to update the material after setting the texture
    //     this.material.needsUpdate = true;
    //   }
    // );
  }

  
}

export class MoonMap {
  name: String;
  map: MoonCell[][]; // карта, состоящая из ячеек

  constructor(name: String | "Test") {
    this.name = name;
    this.map = Array(10).fill(null).map(() => Array(10).fill(null));
    // map generation
    for(let i : number = 0; i < 10; i++){
      for(let j : number = 0; j < 10; j++){
        this.map[i][j] = new MoonCell(i, j);
      }
    }
  }


  getCellMesh(i: number, j: number){
    return this.map[i][j].mesh;
  }

  setupClickHandler(camera: Camera, renderer: any) {
    const raycaster = new Raycaster();
    const mouse = new Vector2();
  
    // Обработчик клика
    const onClick = (event: MouseEvent) => {
      // Получаем размеры и положение canvas
      const canvasRect = renderer.domElement.getBoundingClientRect();
      
      // Вычисляем координаты мыши относительно canvas
      mouse.x = ((event.clientX - canvasRect.left) / renderer.domElement.width) * 2 - 1;
      mouse.y = -((event.clientY - canvasRect.top) / renderer.domElement.height) * 2 + 1;
  
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
          // handler bind here
          this.handleDoubleCell(cell);
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

  // Обработка клика по ячейке
  handleCellClick(cell: MoonCell) {
    console.log(`Clicked on cell at position [${cell.x}, ${cell.y}] in coords (${cell.mesh.position.x},${cell.mesh.position.z})`);
    // Здесь вы можете добавить любую логику обработки клика
    // Например, изменение цвета ячейки:
    (cell.material as MeshBasicMaterial).color.set('#212121');
    cell.material.needsUpdate = true;
  }

  handleDoubleCell(cell: MoonCell){
    // todo: граничные ячейки
    try{
      ((this.getCellMesh(cell.x, cell.y - 1)).material as MeshBasicMaterial).color.set('#212121');
    }
    catch(error){
      
    }
    (cell.material as MeshBasicMaterial).color.set('#212121');
    cell.material.needsUpdate = true;

  }
}
