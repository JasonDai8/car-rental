package car_rental;

import car_rental.model.Car;
import car_rental.model.CarType;
import car_rental.model.Reservation;
import car_rental.repository.CarRepository;
import car_rental.repository.ReservationRepository;
import car_rental.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int durationDays;

    @BeforeEach
    void setUp() {
        startDateTime = LocalDateTime.of(
                2026, 3, 20, 10, 0
        );
        durationDays = 3;
        endDateTime = startDateTime.plusDays(durationDays);

    }


    //test for basic functionality, if car is available, reservation should work
    @Test
    void shouldCreateReservationWhenCarIsAvailable() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN)));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(List.of());
        //when we save to repo, get the same item back/first item back
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reservation result = reservationService.createReservation(CarType.SEDAN, startDateTime, 3 );

        assertNotNull(result);
        assertEquals(CarType.SEDAN, result.getCarType());
    }

    //test for no cars of that type, reservation fails
    @Test
    void shouldRejectReservationWhenNoCarOfTypeExists() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(CarType.SEDAN, startDateTime, 3);
        });
    }

    //test for when all cars of that type are booked, reservation fails
    @Test
    void shouldRejectReservationWhenAllCarsAreBooked() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN), new Car(CarType.SEDAN)));
        when(reservationRepository.findOverlappingReservations(CarType.SEDAN, startDateTime, endDateTime)).thenReturn(List.of(new Reservation(CarType.SEDAN, startDateTime, 3), new Reservation(CarType.SEDAN, startDateTime, 3)));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(CarType.SEDAN, startDateTime, 3);
        });
    }

    //test for when cars are booked except last one, reservation goes through
    @Test
    void shouldAllowReservationsForLastAvailableCar() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN), new Car(CarType.SEDAN), new Car(CarType.SEDAN)));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(List.of(new Reservation(CarType.SEDAN, startDateTime, 3), new Reservation(CarType.SEDAN, startDateTime, 3)));
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reservation result = reservationService.createReservation(CarType.SEDAN, startDateTime, 3 );

        assertNotNull(result);
        assertEquals(CarType.SEDAN, result.getCarType());
    }

    //test when one type of car is booked, but other is available, reseravtion goes through
    @Test
    void shouldAllowReservationsForDifferentTypeWhenOneIsFull() {
        lenient().when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN), new Car(CarType.SEDAN), new Car(CarType.SEDAN)));
        when(carRepository.findByCarType(CarType.SUV)).thenReturn(List.of(new Car(CarType.SUV)));
        lenient().when(reservationRepository.findOverlappingReservations(CarType.SEDAN, startDateTime, endDateTime)).thenReturn(List.of(new Reservation(CarType.SEDAN, startDateTime, 3), new Reservation(CarType.SEDAN, startDateTime, 3)));
        when(reservationRepository.findOverlappingReservations(CarType.SUV, startDateTime, endDateTime)).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reservation result = reservationService.createReservation(CarType.SUV, startDateTime, 3 );

        assertNotNull(result);
        assertEquals(CarType.SUV, result.getCarType());
    }

    //verify all reservation fields are correct
    @Test
    void verifyReservationHasCorrectFields() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN)));
        when(reservationRepository.findOverlappingReservations(any(), any(), any())).thenReturn(List.of());
        //when we save to repo, get the same item back/first item back
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Reservation result = reservationService.createReservation(CarType.SEDAN, startDateTime, 3 );

        assertNotNull(result);
        assertEquals(CarType.SEDAN, result.getCarType());
        assertEquals(startDateTime, result.getStartDateTime());
        assertEquals(durationDays, result.getDurationDays());
    }

    //reject when all cars of all type are booked
    @Test
    void shouldRejectReservationWhenAllTypesAreBooked() {
        when(carRepository.findByCarType(CarType.SEDAN)).thenReturn(List.of(new Car(CarType.SEDAN)));
        when(carRepository.findByCarType(CarType.SUV)).thenReturn(List.of(new Car(CarType.SUV)));
        when(carRepository.findByCarType(CarType.VAN)).thenReturn(List.of(new Car(CarType.VAN)));

        when(reservationRepository.findOverlappingReservations(CarType.SEDAN, startDateTime, endDateTime)).thenReturn(List.of(new Reservation(CarType.SEDAN, startDateTime, durationDays)));
        when(reservationRepository.findOverlappingReservations(CarType.SUV, startDateTime, endDateTime)).thenReturn(List.of(new Reservation(CarType.SUV, startDateTime, durationDays)));
        when(reservationRepository.findOverlappingReservations(CarType.VAN, startDateTime, endDateTime)).thenReturn(List.of(new Reservation(CarType.VAN, startDateTime, durationDays)));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(CarType.SEDAN, startDateTime, 3);
        });
        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(CarType.SUV, startDateTime, 3);
        });
        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(CarType.VAN, startDateTime, 3);
        });
    }

}