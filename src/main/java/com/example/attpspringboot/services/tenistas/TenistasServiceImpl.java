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
import com.example.attpspringboot.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@CacheConfig(cacheNames = {"tenistas"})
public class TenistasServiceImpl implements TenistasService {
    private final TenistasRepository tenistasRepository;
    private final TenistasMapper tenistasMapper;
    private  final FileSystemStorageService storageService;
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TenistasServiceImpl(TenistasRepository tenistasRepository, TenistasMapper tenistasMapper, FileSystemStorageService storageService, AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder) {
        this.tenistasRepository = tenistasRepository;
        this.tenistasMapper = tenistasMapper;
        this.storageService = storageService;
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<TenistaResponse> findAll(Optional<String> nombre, Optional<Integer> ranking,
                                 Optional<Double> puntos, Optional<String> pais,
                                 Optional<Date> fechaNacimiento, Optional<Integer> edad,
                                 Optional<Double> altura, Optional<Double> peso,
                                 Optional<Date> inicioProfesional, Optional<String> entrenador,
                                 Optional<Tenista.Mano> manoBuena, Optional<Tenista.Reves> reves,
                                 Optional<Double> priceMoney, Optional<Integer> bestRanking,
                                 Optional<String> WL, Optional<Integer> victorias,
                                 Optional<Integer> derrotas, Pageable pageable) {

        //Specification para Strings
        Specification<Tenista> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%" ))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specPais = (root, query, criteriaBuilder) ->
                pais.map(p -> criteriaBuilder.like(criteriaBuilder.lower(root.get("pais")), "%" + p.toLowerCase() + "%" ))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specEntrenador = (root, query, criteriaBuilder) ->
                entrenador.map(e -> criteriaBuilder.like(criteriaBuilder.lower(root.get("entrenador")), "%" + e.toLowerCase() + "%" ))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specWL = (root, query, criteriaBuilder) ->
                WL.map(wl -> criteriaBuilder.like(criteriaBuilder.lower(root.get("WL")), "%" + wl.toLowerCase() + "%" ))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Specification para todo lo demas
        Specification<Tenista> specRanking = (root, query, criteriaBuilder) ->
                ranking.map(r -> criteriaBuilder.lessThanOrEqualTo(root.get("ranking"), r))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specPuntos = (root, query, criteriaBuilder) ->
                puntos.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("puntos"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specFechaNacimiento = (root, query, criteriaBuilder) ->
                fechaNacimiento.map(f -> criteriaBuilder.lessThanOrEqualTo(root.get("fechaNacimiento"), f))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specEdad = (root, query, criteriaBuilder) ->
                edad.map(e -> criteriaBuilder.lessThanOrEqualTo(root.get("edad"), e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specAltura = (root, query, criteriaBuilder) ->
                altura.map(a -> criteriaBuilder.lessThanOrEqualTo(root.get("altura"), a))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specPeso = (root, query, criteriaBuilder) ->
                peso.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("peso"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specInicioProfesional = (root, query, criteriaBuilder) ->
                inicioProfesional.map(i -> criteriaBuilder.lessThanOrEqualTo(root.get("inicioProfesional"), i))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specPriceMoney = (root, query, criteriaBuilder) ->
                priceMoney.map(pm -> criteriaBuilder.lessThanOrEqualTo(root.get("priceMoney"), pm))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specBestRanking = (root, query, criteriaBuilder) ->
                bestRanking.map(br -> criteriaBuilder.lessThanOrEqualTo(root.get("bestRanking"), br))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specManoBuena = (root, query, criteriaBuilder) ->
                manoBuena.map(mb -> criteriaBuilder.lessThanOrEqualTo(root.get("manoBuena"), mb))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specReves = (root, query, criteriaBuilder) ->
                reves.map(r -> criteriaBuilder.lessThanOrEqualTo(root.get("reves"), r))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specVictorias = (root, query, criteriaBuilder) ->
                victorias.map(v -> criteriaBuilder.lessThanOrEqualTo(root.get("victorias"), v))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tenista> specDerrotas = (root, query, criteriaBuilder) ->
                derrotas.map(d -> criteriaBuilder.lessThanOrEqualTo(root.get("derrotas"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Tenista> criterios = Specification.where(specNombre).and(specPais)
                .and(specEntrenador).and(specWL).and(specRanking).and(specPuntos)
                .and(specFechaNacimiento).and(specEdad).and(specAltura).and(specPeso)
                .and(specInicioProfesional).and(specPriceMoney).and(specBestRanking)
                .and(specManoBuena).and(specReves).and(specVictorias).and(specDerrotas);

        actualizarRanking();
        calcularPorcentajeVictoriasDerrotas();

        Page<TenistaResponse> lista = tenistasRepository.findAll(criterios, pageable).map(tenistasMapper::toDto);
        return lista;
    }

    @Override
    @Cacheable
    public TenistaResponse findById(Long id) {
        Tenista existingTenista = tenistasRepository.findById(id).orElse(null);
        if (existingTenista != null){
            actualizarRanking();
            calcularPorcentajeVictoriasDerrotas();
            return tenistasMapper.toDto(existingTenista);
        }else {
            throw new TenistaNotFoundException("El tenista con id " + id + " no existe");
        }
    }

    @Override
    @CachePut
    public TenistaResponse save(TenistaCreateDto dto) {
        Tenista tenistaMapped = tenistasMapper.toEntity(dto);
        Tenista newTenista = tenistasRepository.save(tenistaMapped);
        User newUserTenista = User.builder()
                .nombre(dto.getNombre())
                .username(dto.getNombre().toLowerCase())
                .email(dto.getNombre().toLowerCase() + "@gmail.com")
                .password(passwordEncoder.encode(dto.getNombre() + "1"))
                .rols(Stream.of(User.Rol.TENISTA).collect(Collectors.toSet()))
                .build();
        authUsersRepository.save(newUserTenista);
        actualizarRanking();
        calcularPorcentajeVictoriasDerrotas();
        return tenistasMapper.toDto(newTenista);
    }

    @Override
    @CachePut
    public TenistaResponse update(TenistaUpdateDto dto, Long id) {
        Tenista existingTenista = tenistasRepository.findById(id).orElse(null);
        if (existingTenista != null){
            Tenista tenistaMapped = tenistasMapper.toEntity(dto, existingTenista);
            Tenista updateTenista = tenistasRepository.save(tenistaMapped);
            actualizarRanking();
            calcularPorcentajeVictoriasDerrotas();
            return tenistasMapper.toDto(updateTenista);
        }else {
            throw new TenistaNotFoundException("El tenista con id " + id + " no existe");
        }
    }

    @Override
    @Transactional
    @CachePut
    public TenistaResponse updateImage(Long id, MultipartFile image, Boolean withUrl) {
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var existingTenista = tenistasRepository.findById(id).orElseThrow(() -> new TenistaNotFoundException("El tenista con id " + id + " no existe"));
        // Borramos la imagen anterior si existe y no es la de por defecto
        if (existingTenista.getImagen() != null && !existingTenista.getImagen().equals(Tenista.IMAGE_DEFAULT)) {
            storageService.delete(existingTenista.getImagen());
        }
        String imageStored = storageService.store(image);
        // Si quiero la url completa
        String imageUrl = !withUrl ? imageStored : storageService.getUrl(imageStored);
        //storageService.getUrl(imageStored); // Si quiero la url completa
        // Clonamos el producto con la nueva imagen, porque inmutabilidad de los objetos
        var tenistaActualizado = Tenista.builder()
                .id(existingTenista.getId())
                .nombre(existingTenista.getNombre())
                .ranking(existingTenista.getRanking())
                .puntos(existingTenista.getPuntos())
                .pais(existingTenista.getPais())
                .fechaNacimiento(existingTenista.getFechaNacimiento())
                .edad(existingTenista.getEdad())
                .altura(existingTenista.getAltura())
                .peso(existingTenista.getPeso())
                .inicioProfesional(existingTenista.getInicioProfesional())
                .manoBuena(existingTenista.getManoBuena())
                .reves(existingTenista.getReves())
                .entrenador(existingTenista.getEntrenador())
                .imagen(imageUrl)
                .priceMoney(existingTenista.getPriceMoney())
                .bestRanking(existingTenista.getBestRanking())
                .WL(existingTenista.getWL())
                .torneo(existingTenista.getTorneo())
                .build();

        // Lo guardamos en el repositorio
        var tenistaUpdated = tenistasRepository.save(tenistaActualizado);
        // Devolvemos el producto actualizado
        return tenistasMapper.toDto(tenistaUpdated);
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        Tenista existingTenista = tenistasRepository.findById(id).orElse(null);
        if (existingTenista != null){
            tenistasRepository.deleteById(id);
            actualizarRanking();
        }else {
            throw new TenistaNotFoundException("El tenista con id " + id + " no existe");
        }
    }

    public void actualizarRanking(){
        List<Tenista> tenistas = tenistasRepository.findAll();
        List<Tenista> tenistasOrdenados = tenistas.stream()
                .sorted(Comparator.comparingDouble(Tenista::getPuntos).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < tenistasOrdenados.size(); i++){
            Tenista tenista = tenistasOrdenados.get(i);
            tenista.setRanking(i + 1);
            if (tenista.getBestRanking() == null || tenista.getRanking() < tenista.getBestRanking()){
                tenista.setBestRanking(tenista.getRanking());
            }
            tenistasRepository.save(tenista);
        }
    }

    public void calcularPorcentajeVictoriasDerrotas(){
        List<Tenista> tenistas = tenistasRepository.findAll();
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");
        for (Tenista tenista : tenistas) {
            var victorias = tenista.getVictorias();
            var derrotas = tenista.getDerrotas();
            var totalPartidos = victorias + derrotas;
            double procentajeVictorias = ((double) victorias / totalPartidos) * 100;
            double procentajeDerrotas = 100 - procentajeVictorias;
            tenista.setWL(formatoDecimal.format(procentajeVictorias) + "% / " + formatoDecimal.format(procentajeDerrotas) + "%");
            tenistasRepository.save(tenista);
        }
    }
}
