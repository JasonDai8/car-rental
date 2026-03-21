package car_rental.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    //same as in car, we auto generate id for reservation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int durationDays;

    //same as car, need empty
    public Reservation() {}

    public Reservation(CarType type, LocalDateTime startDateTime, int durationDays) {
        this.carType = type;
        this.startDateTime = startDateTime;
        this.durationDays = durationDays;
        this.endDateTime = startDateTime.plusDays(durationDays);
    }

    public Long getId() {
        return this.id;
    }

    public CarType getCarType() {
        return this.carType;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public int getDurationDays() {
        return this.durationDays;
    }
}
