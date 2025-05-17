interface Point {
  x: number
  y: number
}

interface ImageDimension {
  image_x: number
  image_y: number
}

export class ImagePoint implements Point {
  x: number
  y: number

  constructor(x: number, y: number) {
    this.x = x
    this.y = y
  }
}

export class ImageResizer implements ImageDimension {
  image_x: number
  image_y: number

  constructor(x: number, y: number) {
    this.image_x = x
    this.image_y = y
  }

  setImageX(x: number) {
    this.image_x = x
  }

  setImageY(y: number) {
    this.image_y = y
  }
  getPositionX(original_x: number, original_y: number) {
    return new ImagePoint(original_x / this.image_x, original_y / this.image_y)
  }
}
