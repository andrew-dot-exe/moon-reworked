/* eslint-disable @typescript-eslint/no-explicit-any */
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMapStore = defineStore('mapStore', () => {
  const mapRender = ref(null)
  function setRender(renderRef: any) {
    mapRender.value = renderRef.value
  }
  return { mapRender, setRender }
})
