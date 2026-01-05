package cat.itacademy.s04.s02.n01.fruit.service;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void shouldCreateFruit() {
        FruitRequestDTO request = new FruitRequestDTO();
        request.setName("Apple");
        request.setWeightInKilos(10);

        Fruit savedFruit = new Fruit(1L, "Apple", 10);

        when(fruitRepository.save(any(Fruit.class)))
                .thenReturn(savedFruit);

        FruitResponseDTO response = fruitService.createFruit(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Apple", response.getName());
        assertEquals(10, response.getWeightInKilos());

        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    void shouldReturnAllFruits() {
        List<Fruit> fruits = List.of(
                new Fruit(1L, "Apple", 10),
                new Fruit(2L, "Banana", 5)
        );

        when(fruitRepository.findAll()).thenReturn(fruits);

        List<FruitResponseDTO> result = fruitService.findAllFruits();

        assertEquals(2, result.size());
        verify(fruitRepository).findAll();
    }

    @Test
    void shouldReturnFruitById_whenExists() {
        Fruit fruit = new Fruit(1L, "Apple", 10);

        when(fruitRepository.findById(1L))
                .thenReturn(Optional.of(fruit));

        FruitResponseDTO response = fruitService.findFruitById(1L);

        assertEquals("Apple", response.getName());
        assertEquals(10, response.getWeightInKilos());
    }

    @Test
    void shouldThrowException_whenFruitDoesNotExist() {
        when(fruitRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                FruitNotFoundException.class,
                () -> fruitService.findFruitById(99L)
        );
    }

    @Test
    void shouldDeleteFruit_whenExists() {
        Fruit fruit = new Fruit(1L, "Apple", 10);

        when(fruitRepository.findById(1L))
                .thenReturn(Optional.of(fruit));

        fruitService.deleteFruit(1L);

        verify(fruitRepository).delete(fruit);
    }
}
