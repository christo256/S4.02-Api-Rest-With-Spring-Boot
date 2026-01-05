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

    private final FruitService service;

    public FruitController(FruitService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FruitResponseDTO> create(
            @Valid @RequestBody FruitRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createFruit(dto));
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAllFruits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findFruitById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FruitRequestDTO dto) {

        return ResponseEntity.ok(service.updateFruit(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}
