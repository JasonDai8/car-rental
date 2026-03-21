package car_rental.repository;

import car_rental.model.Car;
import car_rental.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//extending jparepository allows us to generate basic db operations for free
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(
            "SELECT c FROM Car c " +
                    "WHERE c.carType = :carType"
    )
    List<Car> findByCarType(
            @Param("carType") CarType carType
    );
}
