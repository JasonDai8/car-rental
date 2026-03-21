package car_rental.model;

import jakarta.persistence.*;

//tags help JPA recognize this as entity, and then maps the name to the table
@Entity
@Table(name = "cars")
public class Car {

    //tells JPA this is primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    //we never use this, JPA just needs empty one
    public Car() {}

    public Car(CarType carType) {
        this.carType = carType;
    }

    public Long getId() {
        return this.id;
    }

    public CarType getCarType() {
        return this.carType;
    }
}
