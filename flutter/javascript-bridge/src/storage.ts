let map: Record<string, string> = {}

export const storage: Storage = {
  clear: () => map = {},
  getItem: key => map[key],
  key: index => Object.keys(map)[index],
  length: 0,
  removeItem: (key) => {
    delete map[key]
    // @ts-ignore
    storage.length--
  },
  setItem: (key, value) => {
    map[key] = value
    // @ts-ignore
    storage.length++
  }
}
