package it.unibo.sap.ass02.demo.controller.event.dto;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.*;
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
        throw new IllegalArgumentException("Message formatted in a wrong way, some of the input keys are not present inside the JSON message.");
    }

    public static Optional<UpdateLocationMessageDTO> deserializeUpdateLocationMessage(final String msg, final String objectKey, final String xKey, final String yKey) {
        try {
            final Map<String, String> map = Deserializers.deserializeMessage(msg, objectKey, xKey, yKey);
            return Optional.of(new UpdateLocationMessageDTO(map.get(objectKey), Double.valueOf(map.get(xKey)), Double.valueOf(map.get(yKey))));
        }catch (JSONException exception ) {
            return Optional.empty();
        }
    }

    public static Optional<UpdateBatteryMessageDTO> deserializeUpdateBatteryMessage(final String msg, final String objectKey, final String deltaKey) {
        try {
            final Map<String, String> map = Deserializers.deserializeMessage(msg, objectKey, deltaKey);
            return Optional.of(new UpdateBatteryMessageDTO(map.get(objectKey), Integer.valueOf(map.get(deltaKey))));
        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}
