package it.unibo.sap.ass02.demo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 *
 * 2-dimensional point
 * objects are completely state-less
 *
 */
@Data
@Entity
public class P2d {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double x = 0;
    private double y = 0;

    public P2d() {
        this.id = 0L;
    }

    public P2d(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public P2d sum(V2d v){
        return new P2d(x+v.x(),y+v.y());
    }

    public V2d sub(P2d v){
        return new V2d(x-v.x(),y-v.y());
    }

    public String toString(){
        return "P2d("+x+","+y+")";
    }

}
