package xyz.cjcj.logback_for_flutter;

import androidx.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** LogbackForFlutterPlugin */
public class LogbackForFlutterPlugin implements FlutterPlugin, MethodCallHandler {
  private static final Logger log = LoggerFactory.getLogger(LogbackForFlutterPlugin.class);
  private static final Object lock = new Object();
  private Map<String, Logger> loggerMap = new HashMap<>();
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "logback_for_flutter");
    channel.setMethodCallHandler(this);
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "logback_for_flutter");
    channel.setMethodCallHandler(new LogbackForFlutterPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("config")) {
      try {
        configure(call);
        result.success("success");
      } catch (Exception e) {
        e.printStackTrace();
        result.error(e.getMessage(), e.getMessage(), e.getMessage());
      }
    } else if (call.method.equals("log")) {
      try {
        log(call);
        result.success("success");
      } catch (Exception e) {
        e.printStackTrace();
        result.error(e.getMessage(), e.getMessage(), e.getMessage());
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void configure(MethodCall methodCall) throws IOException, JoranException {
    String xmlContent = MessageUtil.parseJson(methodCall, String.class);

    log.info("Configuring logger");
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.reset();
    JoranConfigurator configurator = new JoranConfigurator();
    InputStream configStream = new ByteArrayInputStream(xmlContent.getBytes());
    configurator.setContext(loggerContext);
    configurator.doConfigure(configStream); // loads logback file
    configStream.close();
    log.info("Logger configuration success INFO");
    log.debug("Logger configuration success DEBUG");
    log.trace("Logger configuration success TRACE");
  }

  private void log(MethodCall methodCall) throws IOException {
    LogMessage logMessage = MessageUtil.parseJson(methodCall, LogMessage.class);
    Logger log = getLogger(logMessage.name);

    if (logMessage.level.equals("TRACE")) {
      log.trace(logMessage.message);
    } else if (logMessage.level.equals("DEBUG")) {
      log.debug(logMessage.message);
    } else if (logMessage.level.equals("DEBUG")) {
      log.debug(logMessage.message);
    } else if (logMessage.level.equals("INFO")) {
      log.info(logMessage.message);
    } else if (logMessage.level.equals("WARN")) {
      log.warn(logMessage.message);
    } else if (logMessage.level.equals("ERROR")) {
      log.error(logMessage.message);
    }
  }

  private Logger getLogger(String name) {
    Logger log = loggerMap.get(name);
    if (log == null) {
      synchronized (lock) {
        if (log == null) {
          log = LoggerFactory.getLogger(name);
          loggerMap.put(name, log);
        } else {
          log = loggerMap.get(name);
        }
      }
    }
    return log;
  }
}
