package cat.itacademy.s04.s02.n01.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FruitRequestDTO {

    @NotBlank(message = "Fruit name cannot be blank")
    private String name;

    @Positive(message = "Weight must be greater than zero")
    private int weightInKilos;
}
