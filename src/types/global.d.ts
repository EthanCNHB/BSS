// src/types/global.d.ts
import { ComponentCustomProperties } from 'vue'

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $formatTime: (value: string | Date, pattern?: string) => string
  }
}

export {}
