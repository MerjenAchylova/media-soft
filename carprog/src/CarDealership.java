import java.util.*;
import java.util.stream.Collectors;

public class CarDealership {
    private String name;
    private Set<Car> cars;

    public CarDealership(String name) {
        this.name = name;
        this.cars = new HashSet<>();
    }

    public boolean addCar(Car car) {
        return cars.add(car);
    }

    public List<Car> findByManufacturer(String manufacturer) {
        return cars.stream()
                .filter(c -> c.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public double getAveragePriceByType(CarType type) {
        return cars.stream()
                .filter(c -> c.getType() == type)
                .mapToDouble(Car::getPrice)
                .average()
                .orElse(0);
    }

    public List<Car> getCarsSortedByYear() {
        return cars.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<Car> getCars() {
        return cars;
    }

    public int getCarsCount() {
        return cars.size();
    }
}