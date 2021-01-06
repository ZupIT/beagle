import 'package:beagle/interface/storage.dart';
import 'package:flutter/cupertino.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DefaultStorage implements Storage {
  @override
  Future<void> clear() async {
    debugPrint('Storage: clearing');
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
  }

  @override
  Future<String> getItem(String key) async {
    debugPrint('Storage: getting item $key');
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(key);
  }

  @override
  Future<void> removeItem(String key) async {
    debugPrint('Storage: removing item $key');
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(key);
  }

  @override
  Future<void> setItem(String key, String value) async {
    debugPrint('Storage: setting item $key $value');
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(key, value);
  }
}
