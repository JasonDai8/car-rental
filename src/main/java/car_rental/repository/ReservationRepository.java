package car_rental.repository;

import car_rental.model.CarType;
import car_rental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//same as carRepository
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(
            "SELECT r FROM Reservation r " +
                    "WHERE r.carType = :carType " +
                    "AND r.startDateTime < :endDateTime " +
                    "AND :startDateTime < r.endDateTime"
    )
    List<Reservation> findOverlappingReservations(
            @Param("carType") CarType carType,
            @Param("startDateTime")
            LocalDateTime startDateTime,
            @Param("endDateTime")
            LocalDateTime endDateTime
    );
}
