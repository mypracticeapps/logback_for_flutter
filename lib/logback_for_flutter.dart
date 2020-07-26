import 'dart:async';

import 'package:flutter/services.dart';
import 'package:logback_for_flutter/log.message.dart';

class LogbackForFlutter {
  String name;

  LogbackForFlutter(this.name);

  trace(String message) async {
    LogMessage logMessage = LogMessage();
    logMessage.name = name;
    logMessage.level = "TRACE";
    logMessage.message = message;
    await _channel.invokeMethod('log', logMessage.toJson());
  }

  debug(String message) async {
    LogMessage logMessage = LogMessage();
    logMessage.name = name;
    logMessage.level = "DEBUG";
    logMessage.message = message;
    await _channel.invokeMethod('log', logMessage.toJson());
  }

  info(String message) async {
    LogMessage logMessage = LogMessage();
    logMessage.name = name;
    logMessage.level = "INFO";
    logMessage.message = message;
    await _channel.invokeMethod('log', logMessage.toJson());
  }

  warn(String message) async {
    LogMessage logMessage = LogMessage();
    logMessage.name = name;
    logMessage.level = "WARN";
    logMessage.message = message;
    await _channel.invokeMethod('log', logMessage.toJson());
  }

  error(String message) async {
    LogMessage logMessage = LogMessage();
    logMessage.name = name;
    logMessage.level = "ERROR";
    logMessage.message = message;
    await _channel.invokeMethod('log', logMessage.toJson());
  }

  static const MethodChannel _channel =
      const MethodChannel('logback_for_flutter');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future config(String xmlContent) async {
    print("Sending log config message to android");
    await _channel.invokeMethod('config', xmlContent);
  }
}
