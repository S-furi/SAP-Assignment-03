package it.unibo.sap.ass02.demo.controller.event;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Deserializers {

    private Deserializers() {}
    public static Map<String, String> deserializeMessage(final String msg, final String... inputKeys) throws JSONException {
        final List<String> keys = Arrays.asList(inputKeys);
        final JSONObject jsonObject = new JSONObject(msg);
        if (keys.stream().allMatch(jsonObject::has)) {
            return keys.stream().collect(Collectors.toMap(key -> key, key -> {
                    try {
                        return jsonObject.getString(key);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }));
        }
        return Collections.emptyMap();
    }
}
