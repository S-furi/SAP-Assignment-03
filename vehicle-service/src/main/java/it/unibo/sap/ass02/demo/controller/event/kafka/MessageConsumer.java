package it.unibo.sap.ass02.demo.controller.event.kafka;

import it.unibo.sap.ass02.demo.controller.event.kafka.dto.Deserializers;
import it.unibo.sap.ass02.demo.service.EBikeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {

    private final EBikeService eBikeService;
    private final static String EBIKEID_KEY= "ebikeId";
    private final static  String X_POS_KEY = "x";
    private final static  String Y_POS_KEY = "y";
    private final static String DELTA_KEY = "delta";

    public MessageConsumer(final EBikeService eBikeService){
        this.eBikeService = eBikeService;
    }
    @KafkaListener(topics = "update-location")
    public void listenUpdateLocation(final String message) {
        Deserializers.deserializeUpdateLocationMessage(message,
                MessageConsumer.EBIKEID_KEY,
                MessageConsumer.X_POS_KEY,
                MessageConsumer.Y_POS_KEY).ifPresent(messageDto-> {

            this.eBikeService.updateLocation(
                    messageDto.ebikeId(),
                    messageDto.x(),
                    messageDto.y());
        });
    }

    @KafkaListener(topics = "update-battery")
    public void listenUpdateBatteryLevel(final String message) {
        Deserializers.deserializeUpdateBatteryMessage(message,
                MessageConsumer.EBIKEID_KEY,
                MessageConsumer.DELTA_KEY).ifPresent(messageDto -> {
                    this.eBikeService.updateBattery(messageDto.ebikeId(), messageDto.delta());
        });
    }
}
