import type { ModuleModel } from '@/components/modules/modules'
import { Module } from '@/components/modules/modules'
import * as THREE from 'three'

export class BaseModule {
  mesh: THREE.Mesh
  moduleInfo: ModuleModel

  constructor(
    geometry: THREE.BufferGeometry = new THREE.BoxGeometry(1, 1, 1),
    material: THREE.Material = new THREE.MeshStandardMaterial({ color: 0x00ff00 }),
    module: Module = new Module(0, 0, 0, 0, 0),
  ) {
    this.mesh = new THREE.Mesh(geometry, material)
    this.mesh.castShadow = true
    this.mesh.receiveShadow = true
    this.moduleInfo = module
  }

  getMesh() {
    return this.mesh
  }
}
