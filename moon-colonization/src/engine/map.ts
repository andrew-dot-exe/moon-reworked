/**
 * Represents a cell in the isometric map
 */
export class Cell {
  id: string;
  x: number;
  y: number;
  height: number;
  type: string;
  isSelected: boolean;

  constructor(x: number, y: number, height: number = 0, type: string = 'default') {
    this.id = `cell-${x}-${y}`;
    this.x = x;
    this.y = y;
    this.height = height;
    this.type = type;
    this.isSelected = false;
  }

  /**
   * Get the isometric coordinates for rendering
   */
  getIsoCoordinates(cellWidth: number, cellHeight: number): { x: number, y: number } {
    // Convert grid coordinates to isometric coordinates
    const isoX = (this.x - this.y) * cellWidth / 2;
    const isoY = (this.x + this.y) * cellHeight / 2 - (this.height * 5); // Adjust height for elevation effect
    
    return { x: isoX, y: isoY };
  }
}

/**
 * Represents an isometric map with a grid of cells
 */
export class IsometricMap {
  width: number;
  height: number;
  cells: Cell[][];
  cellWidth: number;
  cellHeight: number;

  constructor(width: number = 10, height: number = 10, cellWidth: number = 64, cellHeight: number = 32) {
    this.width = width;
    this.height = height;
    this.cellWidth = cellWidth;
    this.cellHeight = cellHeight;
    this.cells = [];
    
    // Initialize all cells
    this.initializeCells();
  }

  /**
   * Initialize the grid of cells
   */
  private initializeCells(): void {
    for (let x = 0; x < this.width; x++) {
      this.cells[x] = [];
      for (let y = 0; y < this.height; y++) {
        // Create cells with random heights for demonstration
        const randomHeight = Math.floor(Math.random() * 3);
        this.cells[x][y] = new Cell(x, y, randomHeight);
      }
    }
  }

  /**
   * Get a specific cell by its grid coordinates
   */
  getCell(x: number, y: number): Cell | null {
    if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
      return this.cells[x][y];
    }
    return null;
  }

  /**
   * Get all cells as a flat array
   */
  getAllCells(): Cell[] {
    const allCells: Cell[] = [];
    for (let x = 0; x < this.width; x++) {
      for (let y = 0; y < this.height; y++) {
        allCells.push(this.cells[x][y]);
      }
    }
    return allCells;
  }

  /**
   * Select a cell at the given coordinates
   */
  selectCell(x: number, y: number): void {
    const cell = this.getCell(x, y);
    if (cell) {
      // Deselect all cells first
      this.deselectAllCells();
      cell.isSelected = true;
    }
  }

  /**
   * Deselect all cells
   */
  deselectAllCells(): void {
    for (let x = 0; x < this.width; x++) {
      for (let y = 0; y < this.height; y++) {
        this.cells[x][y].isSelected = false;
      }
    }
  }

  /**
   * Convert screen coordinates to grid coordinates
   */
  screenToGrid(screenX: number, screenY: number, offsetX: number = 0, offsetY: number = 0): { x: number, y: number } | null {
    // Adjust for offset
    const adjX = screenX - offsetX;
    const adjY = screenY - offsetY;
    
    // Inverse isometric transformation
    const gridX = Math.floor((adjY / (this.cellHeight / 2) + adjX / (this.cellWidth / 2)) / 2);
    const gridY = Math.floor((adjY / (this.cellHeight / 2) - adjX / (this.cellWidth / 2)) / 2);
    
    if (gridX >= 0 && gridX < this.width && gridY >= 0 && gridY < this.height) {
      return { x: gridX, y: gridY };
    }
    
    return null;
  }
}

/**
 * Create and return a default 10x10 isometric map
 */
export function createDefaultMap(): IsometricMap {
  return new IsometricMap(10, 10);
}