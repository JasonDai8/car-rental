package car_rental.dto;

import car_rental.model.CarType;

import java.time.LocalDateTime;

public class ReservationRequest {

    private CarType carType;
    private LocalDateTime startDateTime;
    private int durationDays;

    public ReservationRequest() {}

    public ReservationRequest(CarType carType, LocalDateTime startDateTime, int durationDays) {
        this.carType = carType;
        this.startDateTime = startDateTime;
        this.durationDays = durationDays;
    }

    public CarType getCarType() {
        return this.carType;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public int getDurationDays() {
        return this.durationDays;
    }
}
