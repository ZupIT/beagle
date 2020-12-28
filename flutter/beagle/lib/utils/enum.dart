class EnumUtils {
  static T fromString<T>(List<T> values, String str) {
    return values.firstWhere(
        (item) =>
            item.toString().split('.')[1].toUpperCase() == str.toUpperCase(),
        orElse: () => null);
  }
}
