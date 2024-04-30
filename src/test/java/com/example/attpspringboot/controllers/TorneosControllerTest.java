package com.example.attpspringboot.controllers;

import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.exceptions.TorneoNotFoundException;
import com.example.attpspringboot.models.Torneo;
import com.example.attpspringboot.services.torneos.TorneosServiceImpl;
import com.example.attpspringboot.utils.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class TorneosControllerTest {
private final String endpoint = "/torneos";

    private final Torneo torneo1 = Torneo.builder()
            .id(1L)
            .nombre("Wimbledon")
            .ubicacion("Londres, Reino Unido")
            .modalidad(Torneo.Modalidad.INDIVIDUALES)
            .categoria(Torneo.Categoria.MASTER_1000)
            .superficie(Torneo.Superficie.HIERBA)
            .entradas(16)
            .premio(2000000.00)
            .fechaInicio(LocalDate.of(2024, 6, 23))
            .fechaFinalizacion(LocalDate.of(2024, 7, 6))
            .imagen("http://placeimg.com/640/480/people")
            .tenistas(new ArrayList<>())
            .build();

    private final TorneoRespose torneoResponse1 = TorneoRespose.builder()
            .nombre("Wimbledon")
            .ubicacion("Londres, Reino Unido")
            .modalidad(Torneo.Modalidad.INDIVIDUALES)
            .categoria(Torneo.Categoria.MASTER_1000)
            .superficie(Torneo.Superficie.HIERBA)
            .entradas(16)
            .premio(2000000.00)
            .fechaInicio(LocalDate.of(2024, 6, 23))
            .fechaFinalizacion(LocalDate.of(2024, 7, 6))
            .imagen("http://placeimg.com/640/480/people")
            .tenistas(new ArrayList<>())
            .build();
    private final Torneo torneo2 = Torneo.builder()
            .id(1L)
            .nombre("Wimbledon")
            .ubicacion("Londres, Reino Unido")
            .modalidad(Torneo.Modalidad.INDIVIDUALES)
            .categoria(Torneo.Categoria.MASTER_1000)
            .superficie(Torneo.Superficie.HIERBA)
            .entradas(16)
            .premio(2000000.00)
            .fechaInicio(LocalDate.of(2024, 6, 23))
            .fechaFinalizacion(LocalDate.of(2024, 7, 6))
            .imagen("http://placeimg.com/640/480/people")
            .tenistas(new ArrayList<>())
            .build();

    private final TorneoRespose torneoResponse2 = TorneoRespose.builder()
            .nombre("Wimbledon")
            .ubicacion("Londres, Reino Unido")
            .modalidad(Torneo.Modalidad.INDIVIDUALES)
            .categoria(Torneo.Categoria.MASTER_1000)
            .superficie(Torneo.Superficie.HIERBA)
            .entradas(16)
            .premio(2000000.00)
            .fechaInicio(LocalDate.of(2024, 6, 23))
            .fechaFinalizacion(LocalDate.of(2024, 7, 6))
            .imagen("http://placeimg.com/640/480/people")
            .tenistas(new ArrayList<>())
            .build();

    public ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private TorneosServiceImpl torneosService;

    @Autowired
    public TorneosControllerTest(TorneosServiceImpl torneosService) {
        this.torneosService = torneosService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void findAll() throws Exception {
        var lista = List.of(torneoResponse1, torneoResponse2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(lista);

        when(torneosService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        PageResponse<TorneoRespose> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertAll("findAll",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.content().size())
        );

        verify(torneosService, times(1)).findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

    }

    @Test
    void findById() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        when(torneosService.findById(anyLong())).thenReturn(torneoResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        TorneoRespose res = mapper.readValue(response.getContentAsString(), TorneoRespose.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(torneoResponse1, res)
        );

        verify(torneosService, times(1)).findById(anyLong());
    }

    @Test
    void findByIdNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        when(torneosService.findById(anyLong())).thenThrow(new TorneoNotFoundException("El torneo con id 1 no existe"));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertEquals(404, response.getStatus());

        verify(torneosService, times(1)).findById(anyLong());
    }

    @Test
    void createProduct() throws Exception {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TorneoCreateDto torneoToCreate = TorneoCreateDto.builder()
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .tenistas(new ArrayList<>())
                .build();

        when(torneosService.create(any(TorneoCreateDto.class))).thenReturn(torneoResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        post(endpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(torneoToCreate)))
                .andReturn().getResponse();

        TorneoRespose res = mapper.readValue(response.getContentAsString(), TorneoRespose.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(torneoResponse1, res)
        );

        verify(torneosService, times(1)).create(any(TorneoCreateDto.class));
    }

    @Test
    void createProductWithBadRequest() throws Exception {
        var torneoToCreate = TorneoCreateDto.builder()
                .nombre("a")
                .ubicacion("a")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(-2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .tenistas(new ArrayList<>())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        post(endpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(torneoToCreate)))
                .andReturn().getResponse();

         //System.out.println("Response is: " + response.getContentAsString());

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La ubicacion tiene que tener entre 2 y 30 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("El premio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El nombre tiene que tener entre 2 y 30 caracteres"))
        );


    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TorneoUpdateDto torneoToUpdate = TorneoUpdateDto.builder()
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(10000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .build();

        when(torneosService.update(any(TorneoUpdateDto.class), anyLong())).thenReturn(torneoResponse1);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(torneoToUpdate)))
                .andReturn().getResponse();

        TorneoRespose res = mapper.readValue(response.getContentAsString(), TorneoRespose.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(torneoResponse1, res)
        );

        verify(torneosService, times(1)).update(any(TorneoUpdateDto.class), anyLong());
    }

    @Test
    void updateProductNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TorneoUpdateDto torneoToUpdate = TorneoUpdateDto.builder()
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(10000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .build();

        when(torneosService.update(any(TorneoUpdateDto.class), anyLong())).thenThrow(new TorneoNotFoundException("El torneo con id 1 no existe"));

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(torneoToUpdate)))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    void updateProductWithBadRequest() throws Exception {
        var myLocalEndpoint = endpoint + "/1";
        TorneoUpdateDto torneoToUpdate = TorneoUpdateDto.builder()
                .nombre("a")
                .ubicacion("a")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(-10000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(torneoToUpdate)))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("La ubicacion tiene que tener entre 2 y 30 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("El premio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El nombre tiene que tener entre 2 y 30 caracteres"))
        );
    }

    @Test
    void deleteProduct() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        doNothing().when(torneosService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(torneosService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProductNotFound() throws Exception {
        var myLocalEndpoint = endpoint + "/1";

        doThrow(new TorneoNotFoundException("El torneo con id 1 no existe")).when(torneosService).deleteById(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());

        verify(torneosService, times(1)).deleteById(anyLong());
    }

    @Test
    void updateProductImage() throws Exception {
        var myLocalEndpoint = endpoint + "/imagen/1";

        when(torneosService.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenReturn(torneoResponse1);

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


        TorneoRespose res = mapper.readValue(response.getContentAsString(), TorneoRespose.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(torneoResponse1, res)
        );

        verify(torneosService, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    @Test
    void updateProductImage_noFile() throws Exception {
        var myLocalEndpoint = endpoint + "/imagen/1";

        when(torneosService.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el torneo valida o esta esta vacia"));

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

        verify(torneosService, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    @Test
    void updateProductImage_noType() throws Exception {
        var myLocalEndpoint = endpoint + "/imagen/1";

        when(torneosService.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen"));

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

        verify(torneosService, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    @Test
    void inscribirTenista() throws Exception {
        var myLocalEndpoint = endpoint + "/Wimbledon/inscribir/1";

        doNothing().when(torneosService).inscribirTenista(anyString(), anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(201, response.getStatus())
        );

        verify(torneosService, times(1)).inscribirTenista(anyString(), anyLong());
    }

    @Test
    void finalizarTorneo() throws Exception {
        var myLocalEndpoint = endpoint + "/finalizarTorneo/1";

        doNothing().when(torneosService).finalizarTorneo(anyLong());

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(torneosService, times(1)).finalizarTorneo(anyLong());
    }
}