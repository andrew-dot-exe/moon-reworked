/*
some features for debug ui
*/
import type { CameraHelper, DirectionalLight } from "three";
import { GUI } from "three/examples/jsm/libs/lil-gui.module.min.js";

export class DebugGUI {
  gui: GUI;

  constructor() {
    this.gui = new GUI();
        // Position the GUI at the top-right corner
        this.gui.domElement.style.position = 'absolute';
        this.gui.domElement.style.top = '0px';
        this.gui.domElement.style.right = '0px';
  }

  debugLights( directionalLight: DirectionalLight,
    shadowCameraHelper: CameraHelper) 
    {
    // Create a folder for the directional light
    const lightFolder = this.gui.addFolder("Directional Light");

    // Position controls
    const lightPosition = {
      x: directionalLight.position.x,
      y: directionalLight.position.y,
      z: directionalLight.position.z,
    };

    lightFolder.add(lightPosition, "x", -20, 20, 0.5).onChange(() => {
      directionalLight.position.x = lightPosition.x;
      shadowCameraHelper.update();
    });
    lightFolder.add(lightPosition, "y", 0, 30, 0.5).onChange(() => {
      directionalLight.position.y = lightPosition.y;
      shadowCameraHelper.update();
    });
    lightFolder.add(lightPosition, "z", -20, 20, 0.5).onChange(() => {
      directionalLight.position.z = lightPosition.z;
      shadowCameraHelper.update();
    });

    // Intensity control
    lightFolder.add(directionalLight, "intensity", 0, 5, 0.1);
  }

debugShadows(directionalLight: DirectionalLight, 
                        shadowCameraHelper: CameraHelper,
                        ) {
    // Shadow controls
    const shadowFolder = this.gui.addFolder('Shadow Settings');
    shadowFolder.add(directionalLight.shadow, 'bias', -0.01, 0.01, 0.001);
    shadowFolder.add(shadowCameraHelper, 'visible').name('Show Shadow Camera');

    // Use provided lightFolder or create a new one
    const targetLightFolder = this.gui.addFolder("Directional Light");

    // Add target controls
    const targetPosition = {
            x: 0,
            y: 0,
            z: 0
    };

    if (!directionalLight.target.position.equals(directionalLight.target.position.clone().set(0, 0, 0))) {
            targetPosition.x = directionalLight.target.position.x;
            targetPosition.y = directionalLight.target.position.y;
            targetPosition.z = directionalLight.target.position.z;
    }

    const targetFolder = targetLightFolder.addFolder('Light Target');
    targetFolder.add(targetPosition, 'x', -10, 10, 0.5).onChange(() => {
            directionalLight.target.position.x = targetPosition.x;
            directionalLight.target.updateMatrixWorld();
            shadowCameraHelper.update();
    });
    targetFolder.add(targetPosition, 'y', -10, 10, 0.5).onChange(() => {
            directionalLight.target.position.y = targetPosition.y;
            directionalLight.target.updateMatrixWorld();
            shadowCameraHelper.update();
    });
    targetFolder.add(targetPosition, 'z', -10, 10, 0.5).onChange(() => {
            directionalLight.target.position.z = targetPosition.z;
            directionalLight.target.updateMatrixWorld();
            shadowCameraHelper.update();
    });

    // Add a button to reset the light position
    targetLightFolder.add({
            reset: () => {
                    // Create a reference to light position
                    const lightPosition = {
                            x: 10,
                            y: 15,
                            z: 10
                    };
                    directionalLight.position.set(10, 15, 10);
                    targetPosition.x = 0;
                    targetPosition.y = 0;
                    targetPosition.z = 0;
                    directionalLight.target.position.set(0, 0, 0);
                    directionalLight.target.updateMatrixWorld();
                    shadowCameraHelper.update();
                    // Update all controllers
                    for (const controller of targetLightFolder.controllers) {
                            controller.updateDisplay();
                    }
                    for (const controller of targetFolder.controllers) {
                            controller.updateDisplay();
                    }
            }
    }, 'reset').name('Reset Light');
}
}
