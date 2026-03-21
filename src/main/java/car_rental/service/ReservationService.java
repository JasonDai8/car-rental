package car_rental.service;

import car_rental.model.CarType;
import car_rental.model.Reservation;
import car_rental.repository.CarRepository;
import car_rental.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(CarRepository carRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    public Reservation createReservation(CarType carType, LocalDateTime startDateTime, int durationDays) {
        //first, get all cars of cartype, and jot that number down
        //then, get all resrvations of cartype, and that overlaps with the timeline, jot that number down
        //then compare the two, if car number > reservation number, create reservation. If not, throw error
        LocalDateTime endDateTime = startDateTime.plusDays(durationDays);
        int totalCarType = this.carRepository.findByCarType(carType).size();
        int overlapReservations = this.reservationRepository.findOverlappingReservations(carType, startDateTime, endDateTime).size();

        if (totalCarType > overlapReservations) {
            Reservation reservation = new Reservation(carType, startDateTime, durationDays);
            return reservationRepository.save(reservation);
        }
        else {
            throw new IllegalStateException("No available cars of type: " + carType);
        }
    }
}
