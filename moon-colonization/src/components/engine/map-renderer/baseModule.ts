import type { ModuleModel } from '@/components/modules/modules'
import { Module } from '@/components/modules/modules'
import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/Addons.js'

const basePath = '/textures/modules/'

export class ModuleMesh {
  module_name: string
  mesh: THREE.Mesh

  constructor(module_name: string) {
    this.module_name = module_name
    // Создаём геометрию плейна
    const geometry = new THREE.PlaneGeometry(1, 1)
    // Загружаем текстуру
    const texture = new THREE.TextureLoader().load(basePath + module_name + '.png')
    // Создаём материал с текстурой
    const material = new THREE.MeshStandardMaterial({ map: texture, side: THREE.DoubleSide })
    // Создаём меш
    this.mesh = new THREE.Mesh(geometry, material)
    this.mesh.position.y = 0
    this.mesh.rotation.x = -Math.PI / 2
  }

  getMesh() {
    return this.mesh
  }
}
