package car_rental.controller;

import car_rental.dto.ReservationRequest;
import car_rental.model.CarType;
import car_rental.model.Reservation;
import car_rental.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return this.reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return this.reservationService.getReservationById(id);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {
       Reservation reservation = reservationService.createReservation(reservationRequest.getCarType(), reservationRequest.getStartDateTime(), reservationRequest.getDurationDays());
       return ResponseEntity.status(201).body(reservation);
    }
}
