package cat.itacademy.s04.s02.n01.fruit.controller;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitRequestDTO;
import cat.itacademy.s04.s02.n01.fruit.dto.FruitResponseDTO;
import cat.itacademy.s04.s02.n01.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.service.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private FruitService fruitService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateFruit_whenRequestIsValid() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO();
        request.setName("Apple");
        request.setWeightInKilos(10);
        FruitResponseDTO response = new FruitResponseDTO(1L, "Apple", 10);

        when(fruitService.createFruit(any(FruitRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(10));
    }

    @Test
    void shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO();
        request.setName("");
        request.setWeightInKilos(-5);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.weightInKilos").exists());
    }

    @Test
    void shouldReturnAllFruits() throws Exception {
        List<FruitResponseDTO> fruits = List.of(
                new FruitResponseDTO(1L, "Apple", 10),
                new FruitResponseDTO(2L, "Banana", 5)
        );

        when(fruitService.findAllFruits()).thenReturn(fruits);

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnFruitById_whenExists() throws Exception {
        FruitResponseDTO response = new FruitResponseDTO(1L, "Apple", 10);

        when(fruitService.findFruitById(1L)).thenReturn(response);

        mockMvc.perform(get("/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void shouldReturnNotFound_whenFruitDoesNotExist() throws Exception {
        when(fruitService.findFruitById(99L))
                .thenThrow(new FruitNotFoundException(99L));

        mockMvc.perform(get("/fruits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateFruit_whenRequestIsValid() throws Exception {
        FruitRequestDTO request = new FruitRequestDTO();
        request.setName("Green Apple");
        request.setWeightInKilos(12);
        FruitResponseDTO response = new FruitResponseDTO(1L, "Green Apple", 12);

        when(fruitService.updateFruit(eq(1L), any(FruitRequestDTO.class)))
                .thenReturn(response);


        mockMvc.perform(put("/fruits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Green Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(12));
    }

    @Test
    void shouldDeleteFruit_whenExists() throws Exception {
        mockMvc.perform(delete("/fruits/1"))
                .andExpect(status().isNoContent());
    }
}

