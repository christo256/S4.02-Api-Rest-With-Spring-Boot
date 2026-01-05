package cat.itacademy.s04.s02.n01.fruit.service;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository repository;

    public FruitServiceImpl(FruitRepository repository) {
        this.repository = repository;
    }


    @Override
    public FruitResponseDTO createFruit(FruitRequestDTO dto) {
        Fruit fruit = new Fruit(null, dto.getName(), dto.getWeightInKilos());
        Fruit saved = repository.save(fruit);
        return mapToResponse(saved);
    }

    @Override
    public List<FruitResponseDTO> findAllFruits() {
        return repository.findAll()
                .stream()
                .map(fruit -> mapToResponse(fruit))
                .toList();
    }

    @Override
    public FruitResponseDTO findFruitById(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        return mapToResponse(fruit);
    }

    @Override
    public FruitResponseDTO updateFruit(Long id, FruitRequestDTO dto) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));

        fruit.setName(dto.getName());
        fruit.setWeightInKilos(dto.getWeightInKilos());

        return mapToResponse(repository.save(fruit));
    }

    @Override
    public void deleteFruit(Long id) {
        Fruit fruit = repository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        repository.delete(fruit);

    }

    private FruitResponseDTO mapToResponse(Fruit fruit) {
        return new FruitResponseDTO(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKilos()
        );
    }
}

