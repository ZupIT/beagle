/* Validate a possible object i.e., o = { "a": 2 } */
export const isJSONObject = (o: Record<string, any>): boolean =>
  !!o && (typeof o === 'object') && !Array.isArray(o) &&
  ((): boolean => { try { return Boolean(JSON.stringify(o)) } catch { return false } })()

/* Validate a possible JSON object represented as string i.e., s = '{ "a": 3 }' */
export const isJSONObjectString = (s: string): boolean => {
  try {
    const o = JSON.parse(s)
    return !!o && (typeof o === 'object') && !Array.isArray(o)
  } catch {
    return false
  }
}

// Code Explanation:
// - !!o - Not falsy (excludes null, which registers as typeof 'object')
// - (typeof o === 'object') - Excludes boolean, number, and string
// - !Array.isArray(o) - Exclude arrays (which register as typeof 'object')
// - try ... JSON.stringify / JSON.parse - Asks JavaScript engine to determine if valid JSON


export const flattenJSONObject = (data: Record<string, any>): Record<string, any> => {
  if (!isJSONObject(data)) {
    throw TypeError('Cannot flatten non JSON arguments')
  }
  const result = {}
  function recurse(cur, prop): void {
    if (Object(cur) !== cur) {
      result[prop] = cur
    } else if (Array.isArray(cur)) {
      const l = cur.length
      for (let i = 0; i < l; i++)
        recurse(cur[i], prop + '[' + i + ']')
      if (l === 0)
        result[prop] = []
    } else {
      let isEmpty = true
      for (const p in cur) {
        isEmpty = false
        recurse(cur[p], prop ? prop + '.' + p : p)
      }
      if (isEmpty && prop)
        result[prop] = {}
    }
  }
  recurse(data, '')
  return result
}

export const filterBy = (items: Array<string>, filter: string): Array<string> => {
  return items.filter(item => new RegExp('^' + filter.replace(/\*/g, '.*') + '$').test(item))
}

export const getPOSIXString = (data: string): string => {
  if (data.match(/^[0-9]/))
    data = '_'.concat(data)
  return data.replace(/[^a-zA-Z0-9_]/g, '_').toUpperCase()
}
