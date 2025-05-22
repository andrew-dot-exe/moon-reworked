import type { ModuleModel } from '@/components/modules/modules'
import { Module } from '@/components/modules/modules'
import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/Addons.js'

const basePath = '/models/'

export class ModuleMesh {
  loader: GLTFLoader
  basePath: string

  constructor(name: string) {
    this.loader = new GLTFLoader()
    this.basePath = basePath + name
    // мб здесь будет модуль, но не знаю
  }

  async createMeshFromGLTF(gltfFile: string): Promise<THREE.Object3D> {
    return new Promise((resolve, reject) => {
      this.loader.load(
        this.basePath + gltfFile,
        (gltf) => {
          resolve(gltf.scene)
        },
        undefined,
        (error) => {
          reject(error)
        },
      )
    })
  }
}
