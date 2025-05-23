import { defineStore } from 'pinia'

const useLayOutSettingStore = defineStore('SettingStore', {
  state: () => ({
    fold: false,
    refsh: false,
  }),
  actions: {
    toggleRefsh() {
      this.refsh = !this.refsh
    },
  },
})

export default useLayOutSettingStore
