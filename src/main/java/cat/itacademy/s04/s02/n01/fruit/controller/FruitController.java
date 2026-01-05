package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.service.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitResponseDTO> create(
            @Valid @RequestBody FruitRequestDTO dto) {

        FruitResponseDTO created = fruitService.createFruit(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> findAll() {
        return ResponseEntity.ok(fruitService.findAllFruits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fruitService.findFruitById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FruitRequestDTO dto) {

        FruitResponseDTO updated = fruitService.updateFruit(id, dto);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}
