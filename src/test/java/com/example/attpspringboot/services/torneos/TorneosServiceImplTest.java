package com.example.attpspringboot.services.torneos;

import com.example.attpspringboot.config.websockets.WebSocketConfig;
import com.example.attpspringboot.config.websockets.WebSocketHandler;
import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.exceptions.TenistaBadRequestException;
import com.example.attpspringboot.exceptions.TenistaNotFoundException;
import com.example.attpspringboot.exceptions.TorneoBadRequestException;
import com.example.attpspringboot.exceptions.TorneoNotFoundException;
import com.example.attpspringboot.mappers.NotificationMapper;
import com.example.attpspringboot.mappers.TorneosMapper;
import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import com.example.attpspringboot.repositories.TenistasRepository;
import com.example.attpspringboot.repositories.TorneosRepository;
import com.example.attpspringboot.services.tenistas.TenistasServiceImpl;
import com.example.attpspringboot.storage.services.FileSystemStorageService;
import com.example.attpspringboot.websockets.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneosServiceImplTest {

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

    @InjectMocks
    private TorneosServiceImpl torneosService;
    @Mock
    private TenistasServiceImpl tenistasService;
    @Mock
    private TorneosRepository torneosRepository;
    @Mock
    private TorneosMapper torneosMapper;
    @Mock
    private TenistasRepository tenistasRepository;
    @Mock
    private FileSystemStorageService storageService;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private NotificationMapper notificationMapper;

    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);

    @BeforeEach
    void setUp() {
        torneosService.setWebSocketService(webSocketHandlerMock);
    }

    @Test
    void findAll() {
        List<Torneo> expectedTorneos = Arrays.asList(torneo1, torneo2);
        List<TorneoRespose> expectedResponseTenistas = Arrays.asList(torneoResponse1, torneoResponse2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending()); // ejemplo de creación de un objeto Pageable
        Page<Torneo> expectedPage = new PageImpl<>(expectedTorneos);


        when(torneosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(torneosMapper.toDto(any(Torneo.class))).thenReturn(torneoResponse2);

        Page<TorneoRespose> actualPage = torneosService.findAll(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), pageable);

        System.out.println(actualPage.getTotalElements());

        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(torneosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(torneosMapper, times(2)).toDto(any(Torneo.class));
    }

    @Test
    void findAll_returnProductsByNombre_whenNombreProvided() {
        Optional<String> nombre = Optional.of("Wimbeldon");
        List<Torneo> expectedProducts = List.of(torneo2);
        List<TorneoRespose> expectedResponseProducts = List.of(torneoResponse2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Torneo> expectedPage = new PageImpl<>(expectedProducts);


        when(torneosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(torneosMapper.toDto(any(Torneo.class))).thenReturn(torneoResponse2);

        Page<TorneoRespose> actualPage = torneosService.findAll(nombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        assertAll("findAllWithMarca",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertTrue(actualPage.getTotalElements() > 0)
        );

        verify(torneosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(torneosMapper, times(1)).toDto(any(Torneo.class));
    }

    @Test
    void findById_returnTorneoResponse_whenValidId() {
        Long id = 1L;
        when(torneosRepository.findById(id)).thenReturn(Optional.of(torneo1));
        when(torneosMapper.toDto(torneo1)).thenReturn(torneoResponse1);

        TorneoRespose actualTorneo = torneosService.findById(id);

        assertEquals(torneoResponse1, actualTorneo);

    }

    @Test
    void findById_throwException_whenInvalidId() {
        Long id = 1L;
        when(torneosRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TorneoNotFoundException.class, () -> torneosService.findById(id));
        assertEquals("El torneo con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void create_saveTorneoAndReturnTorneoResponse_whenValidTorneoCreateDto() {
        TorneoCreateDto torneoToSave = TorneoCreateDto.builder()
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

        when(torneosMapper.toEntity(torneoToSave)).thenReturn(torneo1);
        when(torneosRepository.save(torneo1)).thenReturn(torneo1);
        when(torneosMapper.toDto(torneo1)).thenReturn(torneoResponse1);

        TorneoRespose actualTorneo = torneosService.create(torneoToSave);

        assertEquals(torneoResponse1, actualTorneo);

    }

    @Test
    void update_updateAndReturnTorneo_whenValidIdAndTorneoUpdateDto() {
        Long id = 1L;
        Torneo existingTorneo = torneo1;
        TorneoRespose expectedTorneo = torneoResponse1;

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

        when(torneosRepository.findById(id)).thenReturn(Optional.of(existingTorneo));
        when(torneosMapper.toEntity(torneoToUpdate, existingTorneo)).thenReturn(existingTorneo);
        when(torneosRepository.save(existingTorneo)).thenReturn(existingTorneo);
        when(torneosMapper.toDto(existingTorneo)).thenReturn(expectedTorneo);

        TorneoRespose actualTorneo = torneosService.update(torneoToUpdate, id);

        assertEquals(expectedTorneo, actualTorneo);
    }

    @Test
    void update_throwException_whenInvalidId() {
        Long id = 1L;
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

        when(torneosRepository.findById(id)).thenReturn(Optional.empty());



        var exception = assertThrows(TorneoNotFoundException.class, () -> torneosService.update(torneoToUpdate, id));
        assertEquals("El torneo con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void updateImage() {
        String imageUrl = "http://placeimg.com/640/480/people";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(torneosRepository.findById(torneo1.getId())).thenReturn(Optional.of(torneo1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when(torneosRepository.save(any(Torneo.class))).thenReturn(torneo1);
        when(torneosMapper.toDto(any(Torneo.class))).thenReturn(torneoResponse1);

        TorneoRespose updatedTorneo = torneosService.updateImage(torneo1.getId(), multipartFile, false);

        assertEquals(updatedTorneo.getImagen(), imageUrl);

    }

    @Test
    void deleteById_deleteTorneo_whenValidId() {
        Long id = 1L;
        Torneo existingTorneo = torneo1;

        when(torneosRepository.findById(id)).thenReturn(Optional.of(existingTorneo));

        torneosService.deleteById(id);
    }

    @Test
    void deleteById_throwException_whenInvalidId() {
        Long id = 1L;

        when(torneosRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TorneoNotFoundException.class, () -> torneosService.deleteById(id));
        assertEquals("El torneo con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void inscribirTenista_addTenistaToToeneo_whenValidConditions()  {
        Long id = 1L;
        Torneo exitingTorneo = torneo1;
        Tenista existingTenista = Tenista.builder()
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

        when(torneosRepository.findByNombreEqualsIgnoreCase(exitingTorneo.getNombre())).thenReturn(Optional.of(exitingTorneo));
        when(tenistasRepository.findById(id)).thenReturn(Optional.of(existingTenista));
        when(torneosRepository.save(exitingTorneo)).thenReturn(exitingTorneo);

        torneosService.inscribirTenista(exitingTorneo.getNombre(), id);

        assertAll("inscribirTenista",
                () -> assertTrue(exitingTorneo.getTenistas().contains(existingTenista)),
                () -> assertEquals(15, exitingTorneo.getEntradas()),
                () -> assertEquals(exitingTorneo, existingTenista.getTorneo())
        );
    }

    @Test
    void inscribirTenista_throwException_whenInvalidNameTorneo() {
        Long id = 1L;
        Torneo existingTorneo = torneo1;

        when(torneosRepository.findByNombreEqualsIgnoreCase(torneo1.getNombre())).thenReturn(Optional.empty());

        var exception = assertThrows(TorneoNotFoundException.class, () -> torneosService.inscribirTenista(torneo1.getNombre(), id));
        assertEquals("El torneo " + existingTorneo.getNombre() + " no existe", exception.getMessage());
    }

    @Test
    void inscribirTenista_throwException_whenInvalidIdTenista() {
        Long id = 1L;
        Torneo existingTorneo = torneo1;

        when(torneosRepository.findByNombreEqualsIgnoreCase(torneo1.getNombre())).thenReturn(Optional.of(torneo1));
        when(tenistasRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TenistaNotFoundException.class, () -> torneosService.inscribirTenista(torneo1.getNombre(), id));
        assertEquals("El tenista con id " + id + " no existe", exception.getMessage());
    }

    @Test
    void inscribirTenista_throwException_whenInvalidAmountEntradas() {
        Long id = 1L;
        Torneo existingTorneo = Torneo.builder()
                .id(1L)
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(0)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .imagen("http://placeimg.com/640/480/people")
                .tenistas(new ArrayList<>())
                .build();
        Tenista existingTenista = Tenista.builder()
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

        when(torneosRepository.findByNombreEqualsIgnoreCase(existingTorneo.getNombre())).thenReturn(Optional.of(existingTorneo));
        when(tenistasRepository.findById(id)).thenReturn(Optional.of(existingTenista));

        var exception = assertThrows(TorneoBadRequestException.class, () -> torneosService.inscribirTenista(existingTorneo.getNombre(), id));
        assertEquals("No quedan vacantes libres", exception.getMessage());

    }

    @Test
    void inscribirTenista_throwException_whenTenistaExistsInTorneo() {
        Long id = 1L;
        Tenista existingTenista = Tenista.builder()
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
        Torneo existingTorneo = Torneo.builder()
                .id(1L)
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(0)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .imagen("http://placeimg.com/640/480/people")
                .tenistas(new ArrayList<>())
                .build();

        existingTorneo.getTenistas().add(existingTenista);

        when(torneosRepository.findByNombreEqualsIgnoreCase(existingTorneo.getNombre())).thenReturn(Optional.of(existingTorneo));
        when(tenistasRepository.findById(id)).thenReturn(Optional.of(existingTenista));

        var exception = assertThrows(TenistaBadRequestException.class, () -> torneosService.inscribirTenista(existingTorneo.getNombre(), id));
        assertEquals("El tenista ya esta inscrito", exception.getMessage());

    }

    @Test
    void finalizarTorneo_deleteTorneoAndGivePointsAndPrice_whenMaster1000() {
        Long idTorneo = 1L;
        Torneo torneo = new Torneo();
        torneo.setId(idTorneo);
        torneo.setCategoria(Torneo.Categoria.MASTER_1000);
        torneo.setPremio(10000.00); // Por ejemplo
        List<Tenista> listaTenistas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tenista tenista = new Tenista();
            tenista.setAltura(180.00 + i); // Altura diferente para cada tenista
            tenista.setPuntos(0.00);
            tenista.setPriceMoney(0.00);
            listaTenistas.add(tenista);
        }
        torneo.setTenistas(listaTenistas);

        when(torneosRepository.findById(idTorneo)).thenReturn(Optional.of(torneo));

        torneosService.finalizarTorneo(idTorneo);

        verify(torneosRepository, times(1)).findById(idTorneo);
        verify(torneosRepository, times(1)).deleteById(idTorneo);
        verify(tenistasService, times(1)).actualizarRanking();

        List<Tenista> tenistasActualizados = torneo.getTenistas();
        for (int i = 0; i < 4; i++) {
            Tenista tenista = tenistasActualizados.get(i);
            if (torneo.getCategoria() == Torneo.Categoria.MASTER_1000) {
                double expectedPuntos = 1000.0 / (i + 1);
                double expectedPremio = 10000.0 / (i + 1);
                assert (tenista.getPuntos() == expectedPuntos);
                assert (tenista.getPriceMoney() == expectedPremio);
            }
        }
    }

    @Test
    void finalizarTorneo_deleteTorneoAndGivePointsAndPrice_whenMaster500() {
        Long idTorneo = 1L;
        Torneo torneo = new Torneo();
        torneo.setId(idTorneo);
        torneo.setCategoria(Torneo.Categoria.MASTER_500); // Cambiar a MASTER_500 para probar ese caso
        torneo.setPremio(10000.00); // Por ejemplo
        List<Tenista> listaTenistas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tenista tenista = new Tenista();
            tenista.setAltura(180.00 + i); // Altura diferente para cada tenista
            tenista.setPuntos(0.00);
            tenista.setPriceMoney(0.00);
            listaTenistas.add(tenista);
        }
        torneo.setTenistas(listaTenistas);

        when(torneosRepository.findById(idTorneo)).thenReturn(Optional.of(torneo));

        torneosService.finalizarTorneo(idTorneo);

        verify(torneosRepository, times(1)).findById(idTorneo);
        verify(torneosRepository, times(1)).deleteById(idTorneo);
        verify(tenistasService, times(1)).actualizarRanking();

        List<Tenista> tenistasActualizados = torneo.getTenistas();
        for (int i = 0; i < 4; i++) {
            Tenista tenista = tenistasActualizados.get(i);
            if (torneo.getCategoria() == Torneo.Categoria.MASTER_500) {
                double expectedPuntos = 500.0 / (i + 1); // Modificar aquí para el caso MASTER_500
                double expectedPremio = 10000.0 / (i + 1);
                assert (tenista.getPuntos() == expectedPuntos);
                assert (tenista.getPriceMoney() == expectedPremio);
            }
        }
    }@Test
    void finalizarTorneo_deleteTorneoAndGivePointsAndPrice_whenMaster250() {
        Long idTorneo = 1L;
        Torneo torneo = new Torneo();
        torneo.setId(idTorneo);
        torneo.setCategoria(Torneo.Categoria.MASTER_250); // Cambiar a MASTER_250 para probar ese caso
        torneo.setPremio(10000.00); // Por ejemplo
        List<Tenista> listaTenistas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tenista tenista = new Tenista();
            tenista.setAltura(180.00 + i); // Altura diferente para cada tenista
            tenista.setPuntos(0.00);
            tenista.setPriceMoney(0.00);
            listaTenistas.add(tenista);
        }
        torneo.setTenistas(listaTenistas);

        when(torneosRepository.findById(idTorneo)).thenReturn(Optional.of(torneo));

        torneosService.finalizarTorneo(idTorneo);

        verify(torneosRepository, times(1)).findById(idTorneo);
        verify(torneosRepository, times(1)).deleteById(idTorneo);
        verify(tenistasService, times(1)).actualizarRanking();

        List<Tenista> tenistasActualizados = torneo.getTenistas();
        for (int i = 0; i < 4; i++) {
            Tenista tenista = tenistasActualizados.get(i);
            if (torneo.getCategoria() == Torneo.Categoria.MASTER_250) {
                double expectedPuntos = 250.0 / (i + 1); // Modificar aquí para el caso MASTER_250
                double expectedPremio = 10000.0 / (i + 1);
                assert (tenista.getPuntos() == expectedPuntos);
                assert (tenista.getPriceMoney() == expectedPremio);
            }
        }
    }

    @Test
    void finalizarTorneo_trhowsException_whenInvalidId() {
        Long id = 1L;

        when(torneosRepository.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(TorneoNotFoundException.class, () -> torneosService.finalizarTorneo(id));
        assertEquals("El torneo con id " + id + " no existe", exception.getMessage());

        verify(torneosRepository, times(1)).findById(id);
        verify(torneosRepository, never()).deleteById(anyLong());
        verify(tenistasService, never()).actualizarRanking();
    }

    @Test
    void onChange_ShouldSendMessage_WhenValidDataProvided() throws IOException {
        // Arrange
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));

        // Act
        torneosService.onChange(Notification.Tipo.CREATE, any(Tenista.class));
    }
}