package it.unibo.sap.ass02.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Ebike class, used to represent a generic ebike, this ebikes can be used for multiple purpose such as rides")
public class EBikeImpl implements EBike<String>{
    @Id
    @Schema(description = "Unique identifier for the ebike", example = "UU1")
    private String id;
    @JsonProperty("state")
    @Schema(description = "Ebike state")
    private EBikeState state;
    @JsonProperty("location")
    @Schema(description = "Global ebike's position", example = "x:0, y:0")
    @ManyToOne
    private P2d loc = null;
    @JsonIgnore()
    @Transient
    private V2d direction;
    @JsonProperty("speed")
    @Schema(description = "Ebike's speed")
    private double speed;
    @JsonProperty("battery")
    @Min(0)
    @Max(100)
    @Size(min = 0, max = 100)
    @Schema(description = "Ebike's battery level")
    private int batteryLevel;  /* 0..100 */

    public EBikeImpl(String id) {
        this(id, EBikeState.AVAILABLE, new P2d(0, 0), 0, 100);
    }

    public EBikeImpl(String id,
                     EBikeState state,
                     P2d loc,
                     double speed,
                     int batteryLevel) {
        this.id = id;
        this.state = state;
        this.loc = loc;
        this.direction = new V2d(1,0);;
        this.speed = speed;
        this.batteryLevel = batteryLevel;
    }

    @Override
    public EBikeState getState() {
        return state;
    }

    @Override
    public void rechargeBattery() {
        batteryLevel = 100;
        state = EBikeState.AVAILABLE;
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void decreaseBatteryLevel(int delta) {
        batteryLevel -= delta;
        if (batteryLevel < 0) {
            batteryLevel = 0;
            state = EBikeState.MAINTENANCE;
        }
        // NOTE: call the persistence layer
    }


    @Override
    public boolean isAvailable() {
        return state.equals(EBikeState.AVAILABLE);
    }

    @Override
    public void updateState(EBikeState state) {
        this.state = state;
    }

    @Override
    public void updateLocation(P2d newLoc) {
        loc = newLoc;
    }

    @Override
    public void updateSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void updateDirection(V2d dir) {
        this.direction = dir;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public V2d getDirection() {
        return direction;
    }

    @Override
    public P2d getLocation(){
        return loc;
    }

    public String toString() {
        return "{ id: " + id + ", loc: " + loc + ", batteryLevel: " + batteryLevel + ", state: " + state + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EBikeImpl eBike = (EBikeImpl) o;
        return Objects.equals(id, eBike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String getID() {
        return this.id;
    }
}
