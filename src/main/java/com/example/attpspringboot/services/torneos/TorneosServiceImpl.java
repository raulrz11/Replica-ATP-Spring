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
import com.example.attpspringboot.websockets.TenistaNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"torneos"})
public class TorneosServiceImpl implements TorneosService{
    private final TenistasServiceImpl tenistasService;
    private final TorneosRepository torneosRepository;
    private final TorneosMapper torneosMapper;
    private final TenistasRepository tenistasRepository;
    private  final FileSystemStorageService storageService;

    private final NotificationMapper notificationMapper;
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper objectMapper;
    private WebSocketHandler webSocketHandler;

    @Autowired
    public TorneosServiceImpl(TenistasServiceImpl tenistasService, TorneosRepository torneosRepository, TorneosMapper torneosMapper, TenistasRepository tenistasRepository, NotificationMapper notificationMapper, WebSocketConfig webSocketConfig, FileSystemStorageService storageService) {
        this.tenistasService = tenistasService;
        this.torneosRepository = torneosRepository;
        this.torneosMapper = torneosMapper;
        this.tenistasRepository = tenistasRepository;
        this.notificationMapper = notificationMapper;
        this.webSocketConfig = webSocketConfig;
        webSocketHandler = webSocketConfig.webSocketTenistasHandler();
        this.storageService = storageService;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Page<TorneoRespose> findAll(Optional<String> nombre, Optional<String> ubicacion,
                                       Optional<Torneo.Modalidad> modalidad, Optional<Torneo.Categoria> categoria,
                                       Optional<Torneo.Superficie> superficie, Optional<Integer> entradas, Optional<Double> premio,
                                       Optional<LocalDate> fechaInicio, Optional<LocalDate> fechaFinalizacion,
                                       Pageable pageable) {

        //Specification para Strings
        Specification<Torneo> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specUbicacion = (root, query, criteriaBuilder) ->
                ubicacion.map(u -> criteriaBuilder.like(criteriaBuilder.lower(root.get("ubicacion")), "%" + u.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Specification para todo lo demas
        Specification<Torneo> specModalidad = (root, query, criteriaBuilder) ->
                modalidad.map(m -> criteriaBuilder.equal(root.get("modalidad"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specCategoria = (root, query, criteriaBuilder) ->
                categoria.map(c -> criteriaBuilder.equal(root.get("categoria"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specSuperficie = (root, query, criteriaBuilder) ->
                superficie.map(s -> criteriaBuilder.equal(root.get("superficie"), s))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specEntradas = (root, query, criteriaBuilder) ->
                entradas.map(e -> criteriaBuilder.lessThanOrEqualTo(root.get("entradas"), e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specPremio = (root, query, criteriaBuilder) ->
                premio.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("premio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specFechaInicio = (root, query, criteriaBuilder) ->
                fechaInicio.map(fi -> criteriaBuilder.equal(root.get("fechaInicio"), fi))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Torneo> specFechaFinalizacion = (root, query, criteriaBuilder) ->
                fechaFinalizacion.map(ff -> criteriaBuilder.equal(root.get("fechaFinalizacion"), ff))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Torneo> criterios = Specification.where(specNombre).and(specUbicacion).and(specModalidad).and(specCategoria)
                .and(specSuperficie).and(specEntradas).and(specPremio).and(specFechaInicio).and(specFechaFinalizacion);

        Page<TorneoRespose> lista = torneosRepository.findAll(criterios, pageable).map(torneosMapper::toDto);
        return lista;
    }

    @Override
    @Cacheable
    public TorneoRespose findById(Long id) {
        Torneo existingTorneo = torneosRepository.findById(id).orElse(null);
        if (existingTorneo != null){
            return torneosMapper.toDto(existingTorneo);
        }else {
            throw new TorneoNotFoundException("El torneo con id " + id + " no existe");
        }
    }

    @Override
    @CachePut
    public TorneoRespose create(TorneoCreateDto dto) {
        Torneo torneoMapped = torneosMapper.toEntity(dto);
        Torneo newTorneo = torneosRepository.save(torneoMapped);
        return torneosMapper.toDto(newTorneo);
    }

    @Override
    @CachePut
    public TorneoRespose update(TorneoUpdateDto dto, Long id) {
        Torneo existingTorneo = torneosRepository.findById(id).orElse(null);
        Torneo torneoMapped = torneosMapper.toEntity(dto, existingTorneo);
        if (existingTorneo != null){
            Torneo updateTorneo = torneosRepository.save(torneoMapped);
            return torneosMapper.toDto(updateTorneo);
        }else {
            throw new TorneoNotFoundException("El torneo con id " + id + " no existe");
        }
    }

    @Override
    @Transactional
    @CachePut
    public TorneoRespose updateImage(Long id, MultipartFile image, Boolean withUrl) {
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var productoActual = torneosRepository.findById(id).orElseThrow(() -> new TorneoNotFoundException("El tornoneo con id " + id + " no existe"));
        // Borramos la imagen anterior si existe y no es la de por defecto
        if (productoActual.getImagen() != null && !productoActual.getImagen().equals(Torneo.IMAGE_DEFAULT)) {
            storageService.delete(productoActual.getImagen());
        }
        String imageStored = storageService.store(image);
        // Si quiero la url completa
        String imageUrl = !withUrl ? imageStored : storageService.getUrl(imageStored);
        //storageService.getUrl(imageStored); // Si quiero la url completa
        // Clonamos el producto con la nueva imagen, porque inmutabilidad de los objetos
        var productoActualizado = Torneo.builder()
                .id(productoActual.getId())
                .nombre(productoActual.getNombre())
                .ubicacion(productoActual.getUbicacion())
                .modalidad(productoActual.getModalidad())
                .categoria(productoActual.getCategoria())
                .superficie(productoActual.getSuperficie())
                .entradas(productoActual.getEntradas())
                .premio(productoActual.getPremio())
                .fechaInicio(productoActual.getFechaInicio())
                .fechaFinalizacion(productoActual.getFechaFinalizacion())
                .imagen(imageUrl)
                .tenistas(productoActual.getTenistas())
                .build();

        // Lo guardamos en el repositorio
        var productoUpdated = torneosRepository.save(productoActualizado);
        // Devolvemos el producto actualizado
        return torneosMapper.toDto(productoUpdated);
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        Torneo existingTorneo = torneosRepository.findById(id).orElse(null);
        if (existingTorneo != null){
            torneosRepository.deleteById(id);
        }else {
            throw new TorneoNotFoundException("El torneo con id " + id + " no existe");
        }
    }

    @CachePut
    public void inscribirTenista(String nombreTorneo, Long idTenista){
        Tenista existingTenista = tenistasRepository.findById(idTenista).orElse(null);
        Torneo existingTorneo = torneosRepository.findByNombreEqualsIgnoreCase(nombreTorneo).orElse(null);
        if (existingTorneo != null){
            if (existingTenista != null){
                if (existingTorneo.getTenistas().contains(existingTenista)){
                    throw new TenistaBadRequestException("El tenista ya esta inscrito");
                }
                if (existingTorneo.getEntradas() != 0){
                    existingTorneo.getTenistas().add(existingTenista);
                    existingTorneo.setEntradas(existingTorneo.getEntradas() - 1);
                    existingTenista.setTorneo(existingTorneo);
                    torneosRepository.save(existingTorneo);
                    onChange(Notification.Tipo.CREATE, existingTenista);
                }else {
                    throw new TorneoBadRequestException("No quedan vacantes libres");
                }
            }else {
                throw new TenistaNotFoundException("El tenista con id " + idTenista + " no existe");
            }
        }else {
            throw new TorneoNotFoundException("El torneo " + nombreTorneo + " no existe");
        }
    }

    @CacheEvict
    public void finalizarTorneo(Long id){
        Torneo existingTorneo = torneosRepository.findById(id).orElse(null);
        if (existingTorneo != null){
            List<Tenista> listaGanadores = existingTorneo.getTenistas();
            List<Tenista> tenistasOrdenados = listaGanadores.stream()
                    .sorted(Comparator.comparingDouble(Tenista::getAltura))
                    .collect(Collectors.toList());
            for (int i = 0; i < Math.min(4, tenistasOrdenados.size()); i++){
                Tenista tenista = tenistasOrdenados.get(i);
                if (existingTorneo.getCategoria() == Torneo.Categoria.MASTER_1000){
                    tenista.setPuntos(tenista.getPuntos() + (1000. / (i + 1)));
                    tenista.setPriceMoney(tenista.getPriceMoney() + (existingTorneo.getPremio() / (i + 1)));
                    tenista.setTorneo(null);
                } else if (existingTorneo.getCategoria() == Torneo.Categoria.MASTER_500) {
                    tenista.setPuntos(tenista.getPuntos() + (500. / (i + 1)));
                    tenista.setPriceMoney(tenista.getPriceMoney() + (existingTorneo.getPremio() / (i + 1)));
                    tenista.setTorneo(null);
                } else if (existingTorneo.getCategoria() == Torneo.Categoria.MASTER_250) {
                    tenista.setPuntos(tenista.getPuntos() + (250. / (i + 1)));
                    tenista.setPriceMoney(tenista.getPriceMoney() + (existingTorneo.getPremio() / (i + 1)));
                    tenista.setTorneo(null);
                }
            }

            tenistasService.actualizarRanking();

            torneosRepository.deleteById(id);
        }else {
            throw new TorneoNotFoundException("El torneo con id " + id + " no existe");
        }
    }

    void onChange(Notification.Tipo tipo, Tenista data) {

        if (webSocketHandler == null) {
            webSocketHandler = this.webSocketConfig.webSocketTenistasHandler();
        }

        try {
            Notification<TenistaNotification> notificacion = new Notification<>(
                    "Tenista",
                    tipo,
                    notificationMapper.toNotification(data),
                    LocalDateTime.now().toString()
            );

            String json = objectMapper.writeValueAsString((notificacion));

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketHandler.sendMessage(json + " Tenista inscrito");
                } catch (Exception e) {
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Para los test
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketHandler = webSocketHandlerMock;
    }
}
