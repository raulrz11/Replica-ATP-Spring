package com.example.attpspringboot.services.tenistas;

import com.example.attpspringboot.auth.repositories.AuthUsersRepository;
import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaResponse;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.exceptions.TenistaNotFoundException;
import com.example.attpspringboot.mappers.TenistasMapper;
import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.repositories.TenistasRepository;
import com.example.attpspringboot.storage.services.FileSystemStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenistasServiceImplTest {

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

    @InjectMocks
    private TenistasServiceImpl tenistasService;
    @Mock
    private TenistasRepository tenistasRepository;
    @Mock
    private TenistasMapper tenistasMapper;
    @Mock
    private FileSystemStorageService storageService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthUsersRepository authUsersRepository;

    @Test
    void findAll() {
        List<Tenista> expectedTenistas = Arrays.asList(tenista1, tenista2);
        List<TenistaResponse> expectedResponseTenistas = Arrays.asList(tenistaResponse1, tenistaResponse2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("ranking").ascending());
        Page<Tenista> expectedPage = new PageImpl<>(expectedTenistas);

        when(tenistasRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(tenistasMapper.toDto(any(Tenista.class))).thenReturn(tenistaResponse2);
        when(tenistasRepository.findAll()).thenReturn(expectedTenistas);

        Page<TenistaResponse> actualPage = tenistasService.findAll(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(),  Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(),Optional.empty(),
                Optional.empty(), Optional.empty(), pageable);

        System.out.println(actualPage.getTotalElements());

        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(tenistasRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(tenistasMapper, times(2)).toDto(any(Tenista.class));
    }

    @Test
    void findById_returnTenistaResponse_whenValidId() {
        Long id = 1L;

        when(tenistasRepository.findById(id)).thenReturn(Optional.of(tenista1));
        when(tenistasMapper.toDto(tenista1)).thenReturn(tenistaResponse1);

        TenistaResponse actualTenista = tenistasService.findById(id);

        assertEquals(tenistaResponse1, actualTenista);
    }

    @Test
    void findById_throwException_whenInvalidId() {
        Long id = 1L;

        when(tenistasRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TenistaNotFoundException.class, () -> tenistasService.findById(id));
        assertEquals("El tenista con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void save_saveTenistaAndReturnTenistaResponse_whenValidTenistaCreateDto() {
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

        when(tenistasMapper.toEntity(tenistaToCreate)).thenReturn(tenista1);
        when(tenistasRepository.save(tenista1)).thenReturn(tenista1);
        when(tenistasMapper.toDto(tenista1)).thenReturn(tenistaResponse1);

        TenistaResponse actualTenista = tenistasService.save(tenistaToCreate);

        assertEquals(tenistaResponse1, actualTenista);
    }

    @Test
    void update_updateAndReturnTenista_whenValidIdAndTenistaUpdateDto() {
        Long id = 1L;
        Tenista existingTenista = tenista1;
        TenistaResponse expectedTenista = tenistaResponse1;

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

        when(tenistasRepository.findById(id)).thenReturn(Optional.of(existingTenista));
        when(tenistasMapper.toEntity(tenistaToUpdate, existingTenista)).thenReturn(existingTenista);
        when(tenistasRepository.save(existingTenista)).thenReturn(existingTenista);
        when(tenistasMapper.toDto(existingTenista)).thenReturn(expectedTenista);

        TenistaResponse actualTenista = tenistasService.update(tenistaToUpdate, id);

        assertEquals(expectedTenista, actualTenista);
    }

    @Test
    void update_throwException_whenInvalidId() {
        Long id = 1L;
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

        when(tenistasRepository.findById(id)).thenReturn(Optional.empty());



        var exception = assertThrows(TenistaNotFoundException.class, () -> tenistasService.update(tenistaToUpdate, id));
        assertEquals("El tenista con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void updateImage() {
        String imageUrl = "https://via.placeholder.com/150";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(tenistasRepository.findById(tenista1.getId())).thenReturn(Optional.of(tenista1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when(tenistasRepository.save(any(Tenista.class))).thenReturn(tenista1);
        when(tenistasMapper.toDto(any(Tenista.class))).thenReturn(tenistaResponse1);

        TenistaResponse updatedTenista = tenistasService.updateImage(tenista1.getId(), multipartFile, false);

        assertEquals(updatedTenista.getImagen(), imageUrl);
    }

    @Test
    void deleteById_deleteTenista_whenValidId() {
        Long id = 1L;
        Tenista existingTenista = tenista1;

        when(tenistasRepository.findById(id)).thenReturn(Optional.of(existingTenista));

        tenistasService.deleteById(id);
    }

    @Test
    void deleteById_throwException_whenInvalidId() {
        Long id = 1L;

        when(tenistasRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TenistaNotFoundException.class, () -> tenistasService.deleteById(id));
        assertEquals("El tenista con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void actualizarRanking() {
        List<Tenista> lista = Arrays.asList(tenista1, tenista2);

        when(tenistasRepository.findAll()).thenReturn(lista);

        tenistasService.actualizarRanking();

        verify(tenistasRepository, times(lista.size())).save(any(Tenista.class));
    }

    @Test
    void name() {
        List<Tenista> tenistas = new ArrayList<>();

        tenista1.setPuntos(10.0);
        tenista1.setRanking(1);
        tenista1.setBestRanking(5);
        tenista1.setPuntos(5.0);
        tenista2.setRanking(2);
        tenista2.setBestRanking(4);

        tenistas.add(tenista1);
        tenistas.add(tenista2);

        if (tenista1.getBestRanking() == null || tenista1.getRanking() < tenista1.getBestRanking()){
            tenista1.setBestRanking(tenista1.getRanking());
        }

        // Comportamiento simulado del repositorio
        when(tenistasRepository.findAll()).thenReturn(tenistas);

        // Llama al método a probar
        tenistasService.actualizarRanking();

        // Verifica que se llamó al método save para cada tenista
        verify(tenistasRepository, times(tenistas.size())).save(any(Tenista.class));
    }

    @Test
    void calcularPorcentajeVictoriasDerrotas() {
        List<Tenista> lista = Arrays.asList(tenista1, tenista2);

        when(tenistasRepository.findAll()).thenReturn(lista);

        tenistasService.calcularPorcentajeVictoriasDerrotas();

        verify(tenistasRepository, times(lista.size())).save(any(Tenista.class));
    }
}