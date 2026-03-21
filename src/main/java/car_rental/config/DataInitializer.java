package car_rental.config;

import car_rental.model.Car;
import car_rental.model.CarType;
import car_rental.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CarRepository carRepository;

    public DataInitializer(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) {
        this.carRepository.save(new Car(CarType.SEDAN));
        this.carRepository.save(new Car(CarType.SEDAN));
        this.carRepository.save(new Car(CarType.SEDAN));
        this.carRepository.save(new Car(CarType.SUV));
        this.carRepository.save(new Car(CarType.SUV));
        this.carRepository.save(new Car(CarType.VAN));
    }
}
