package com.example.attpspringboot.controllers;

import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaResponse;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.exceptions.TenistaNotFoundException;
import com.example.attpspringboot.exceptions.TorneoNotFoundException;
import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import com.example.attpspringboot.services.tenistas.TenistasServiceImpl;
import com.example.attpspringboot.utils.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class TenistasControllerTest {
    private final String endpoint = "/tenistas";

    Tenista tenista1 = Tenista.builder()
            .nombre("Roger Federer")
            .ranking(5)
            .puntos(8760.5)
            .pais("Suiza")
            .fechaNacimiento(LocalDate.of(1981, 8, 8))
            .edad(42)
            .altura(1.85)
            .peso(85.5)
            .inicioProfesional(LocalDate.of(1998, 7, 29))
            .manoBuena(Tenista.Mano.DERECHA)
            .reves(Tenista.Reves.UNA_MANO)
            .entrenador("Ivan Ljubicic")
            .priceMoney(129946325.0)
            .bestRanking(1)
            .victorias(1242)
            .derrotas(274)
            .WL("84.3%")
            .imagen("https://via.placeholder.com/150")
            .build();

    TenistaResponse tenistaResponse1 = TenistaResponse.builder()
            .nombre("Roger Federer")
            .ranking(5)
            .puntos(8760.5)
            .pais("Suiza")
            .fechaNacimiento(LocalDate.of(1981, 8, 8))
            .edad(42)
            .altura(1.85)
            .peso(85.5)
            .inicioProfesional(LocalDate.of(1998, 7, 29))
            .manoBuena(Tenista.Mano.DERECHA)
            .reves(Tenista.Reves.UNA_MANO)
            .entrenador("Ivan Ljubicic")
            .priceMoney(129946325.0)
            .bestRanking(1)
            .victorias(1242)
            .derrotas(274)
            .WL("84.3%")
            .imagen("https://via.placeholder.com/150")
            .build();

    Tenista tenista2 = Tenista.builder()
            .nombre("Roger Federer")
            .ranking(5)
            .puntos(8760.5)
            .pais("Suiza")
            .fechaNacimiento(LocalDate.of(1981, 8, 8))
            .edad(42)
            .altura(1.85)
            .peso(85.5)
            .inicioProfesional(LocalDate.of(1998, 7, 29))
            .manoBuena(Tenista.Mano.DERECHA)
            .reves(Tenista.Reves.UNA_MANO)
            .entrenador("Ivan Ljubicic")
            .priceMoney(129946325.0)
            .bestRanking(1)
            .victorias(1242)
            .derrotas(274)
            .WL("84.3%")
            .imagen("https://via.placeholder.com/150")
            .build();

    TenistaResponse tenistaResponse2 = TenistaResponse.builder()
            .nombre("Roger Federer")
            .ranking(5)
            .puntos(8760.5)
            .pais("Suiza")
            .fechaNacimiento(LocalDate.of(1981, 8, 8))
            .edad(42)
            .altura(1.85)
            .peso(85.5)
            .inicioProfesional(LocalDate.of(1998, 7, 29))
            .manoBuena(Tenista.Mano.DERECHA)
            .reves(Tenista.Reves.UNA_MANO)
            .entrenador("Ivan Ljubicic")
            .priceMoney(129946325.0)
            .bestRanking(1)
            .victorias(1242)
            .derrotas(274)
            .WL("84.3%")
            .imagen("https://via.placeholder.com/150")
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private TenistasServiceImpl tenistasService;

    @Autowired
    public TenistasControllerTest(TenistasServiceImpl tenistasService) {
        this.tenistasService = tenistasService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void findAll() throws Exception {
        var lista = List.of(tenistaResponse1, tenistaResponse2);
        var pageable = PageRequest.of(0, 10, Sort.by("puntos").descending());
        var page = new PageImpl<>(lista);

        when(tenistasService.findAll(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(),  Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),
                Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<TenistaResponse> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findAll",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(tenistasService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(),  Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),
                Optional.empty(), Optional.empty(), pageable);

    }

    @Test
    void findById() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        when(tenistasService.findById(anyLong())).thenReturn(tenistaResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        TenistaResponse res = mapper.readValue(response.getContentAsString(), TenistaResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(tenistaResponse1, res)
        );

        verify(tenistasService, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        when(tenistasService.findById(anyLong())).thenThrow(new TenistaNotFoundException("El tenista con id 1 no existe"));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(404, response.getStatus());

        verify(tenistasService, times(1)).findById(anyLong());
    }

    @Test
    void create() throws Exception {
        TenistaCreateDto tenistaToCreate = TenistaCreateDto.builder()
                .nombre("Roger Federer")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .victorias(1242)
                .derrotas(274)
                .build();

        when(tenistasService.save(any(TenistaCreateDto.class))).thenReturn(tenistaResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tenistaToCreate)))
                .andReturn().getResponse();

        TenistaResponse res = mapper.readValue(response.getContentAsString(), TenistaResponse.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(tenistaResponse1, res)
        );

        verify(tenistasService, times(1)).save(any(TenistaCreateDto.class));
    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TenistaUpdateDto tenistaToUpdate = TenistaUpdateDto.builder()
                .nombre("Roger Federer")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .victorias(1242)
                .derrotas(274)
                .build();

        when(tenistasService.update(any(TenistaUpdateDto.class), anyLong())).thenReturn(tenistaResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tenistaToUpdate)))
                .andReturn().getResponse();

        TenistaResponse res = mapper.readValue(response.getContentAsString(), TenistaResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(tenistaResponse1, res)
        );

        verify(tenistasService, times(1)).update(any(TenistaUpdateDto.class), anyLong());
    }

    @Test
    void updateProductNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TenistaUpdateDto tenistaToUpdate = TenistaUpdateDto.builder()
                .nombre("Roger Federer")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .victorias(1242)
                .derrotas(274)
                .build();

        when(tenistasService.update(any(TenistaUpdateDto.class), anyLong())).thenThrow(new TenistaNotFoundException("El tenista con id 1 no existe"));

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tenistaToUpdate)))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void updateProductWithBadRequest() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TenistaUpdateDto tenistaToUpdate = TenistaUpdateDto.builder()
                .nombre("a")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(-129946325.0)
                .victorias(-1242)
                .derrotas(274)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(tenistaToUpdate)))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El PriceMoney no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("Las victorias no pueden ser negativas")),
                () -> assertTrue(response.getContentAsString().contains("El nombre tiene que tener entre 2 y 30 caracteres"))
        );
    }

    @Test
    void updateProductImage() throws Exception {
        var myLocalEndpoint = endpoint + "/imagen/1";

        when(tenistasService.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenReturn(tenistaResponse1);

        // Crear un archivo simulado
        MockMultipartFile file = new MockMultipartFile(
                "file", // Nombre del parámetro del archivo en el controlador
                "filename.jpg", // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido del archivo
                "contenido del archivo".getBytes() // Contenido del archivo
        );

        // Crear una solicitud PATCH multipart con el fichero simulado
        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        TenistaResponse res = mapper.readValue(response.getContentAsString(), TenistaResponse.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(tenistaResponse1, res)
        );

        verify(tenistasService, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    @Test
    void updateProductImage_noType() throws Exception {
        var myLocalEndpoint = endpoint + "/imagen/1";

        when(tenistasService.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen"));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        assertEquals(400, response.getStatus());

        verify(tenistasService, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    @Test
    void deleteProduct() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        doNothing().when(tenistasService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(tenistasService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProductNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        doThrow(new TenistaNotFoundException("El tenista con id 1 no existe")).when(tenistasService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(tenistasService, times(1)).deleteById(anyLong());
    }
}