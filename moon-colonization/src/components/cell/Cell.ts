import { h } from "vue"

  
  export class Cell {
    angle: number
    widthSecond: number
    longitudeSecond: number
    height: number
  
    constructor(angle: number, widthSecond: number, longitudeSecond: number, height: number) {
      this.angle = angle
      this.widthSecond = widthSecond
      this.longitudeSecond = longitudeSecond
      this.height = height
    }
  }
 