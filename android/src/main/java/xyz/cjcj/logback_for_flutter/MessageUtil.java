package xyz.cjcj.logback_for_flutter;

import com.google.gson.Gson;

import io.flutter.plugin.common.MethodCall;

public class MessageUtil {
    static final Gson gson = new Gson();

    public static <T> T parseJson(MethodCall call, Class<T> cls) {
        Object map = call.arguments;
        String json = gson.toJson(map);
        return gson.fromJson(json, cls);
    }
}
