class MapUtils {
  static dynamic get(Map<String, dynamic> map, String path) {
    final parts = path.split('.');
    var result = map;
    for (var i = 0; i < parts.length; i++) {
      if (!result.containsKey(parts[i])) {
        break;
      }
      result = result[parts[i]];
    }
    return result;
  }
}
