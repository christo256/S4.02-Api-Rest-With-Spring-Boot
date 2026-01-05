package cat.itacademy.s04.s02.n01.fruit.service;

import cat.itacademy.s04.s02.n01.fruit.dto.*;

import java.util.List;


public interface FruitService {

    FruitResponseDTO createFruit(FruitRequestDTO dto);

    List<FruitResponseDTO> findAllFruits();

    FruitResponseDTO findFruitById(Long id);

    FruitResponseDTO updateFruit(Long id, FruitRequestDTO dto);

    void deleteFruit(Long id);
}
