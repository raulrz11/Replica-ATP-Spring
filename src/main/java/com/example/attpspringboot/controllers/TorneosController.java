package com.example.attpspringboot.controllers;

import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.models.Torneo;
import com.example.attpspringboot.services.torneos.TorneosServiceImpl;
import com.example.attpspringboot.utils.PaginationLinks;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/torneos")
public class TorneosController {
    private final TorneosServiceImpl torneosService;
    private final PaginationLinks paginationLinks;

    @Autowired
    public TorneosController(TorneosServiceImpl torneosService, PaginationLinks paginationLinks) {
        this.torneosService = torneosService;
        this.paginationLinks = paginationLinks;
    }

    @Operation(summary = "Obtiene todos los torneos", description = "Lista de todos los torneos")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del torneo", example = "Wimbeldon"),
            @Parameter(name = "ubicacion", description = "Ubicacion del torneo", example = "Colombia"),
            @Parameter(name = "modalidad", description = "Modalidad del torneo", example = "INDIVIDUALES"),
            @Parameter(name = "categoria", description = "Categoria del torneo", example = "MASTER_1000"),
            @Parameter(name = "superficie", description = "Superficie del torneo", example = "ASFALTO"),
            @Parameter(name = "entradas", description = "Vacantes disponibles", example = "40"),
            @Parameter(name = "premio", description = "Premio del torneo", example = "10.00"),
            @Parameter(name = "fechaInicio", description = "Fecha del primer dia", example = "2024-09-08"),
            @Parameter(name = "fechaFinalizacion", description = "Fecha del ultimo dia", example = "2024-09-09"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "id"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "asc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de torneos"),
    })
    @GetMapping()
    public ResponseEntity<Page<TorneoRespose>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam() Optional<String> ubicacion,
            @RequestParam() Optional<Torneo.Modalidad> modalidad,
            @RequestParam() Optional<Torneo.Categoria> categoria,
            @RequestParam() Optional<Torneo.Superficie> superficie,
            @RequestParam() Optional<Integer> entradas,
            @RequestParam() Optional<Double> premio,
            @RequestParam() Optional<LocalDate> fechaInicio,
            @RequestParam() Optional<LocalDate> fechaFinalizacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<TorneoRespose> pageResult = torneosService.findAll(nombre, ubicacion, modalidad,
                categoria, superficie, entradas, premio, fechaInicio, fechaFinalizacion, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @Operation(summary = "Obtiene un torneo por su id", description = "Torneo con el id especificado")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<TorneoRespose> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(torneosService.findById(id));
    }

    @Operation(summary = "Crea un torneo", description = "Torneo nuevo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Torneo a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Torneo creado"),
            @ApiResponse(responseCode = "400", description = "Torneo no válido"),
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TorneoRespose> create(@Validated @RequestBody TorneoCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(torneosService.create(dto));
    }

    @Operation(summary = "Inscribe tenista en torneoo", description = "Inscrubur tenista")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre torneo", example = "Wimbeldon", required = true),
            @Parameter(name = "id", description = "Identificador unico tenista", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tenista inscrito"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
            @ApiResponse(responseCode = "404", description = "Tenista no encontrado")
    })
    @PostMapping("/{nombreTorneo}/inscribir/{idTenista}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TENISTA')")
    public ResponseEntity<Void> inscribirTenista(@PathVariable() String nombreTorneo, @PathVariable() Long idTenista){
        torneosService.inscribirTenista(nombreTorneo, idTenista);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualiza un torneo", description = "Actualizar torneo")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Torneo a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo actualizado"),
            @ApiResponse(responseCode = "400", description = "Torneo no válido"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TorneoRespose> update(@Validated @RequestBody TorneoUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(torneosService.update(dto, id));
    }

    @Operation(summary = "Actualiza la imagen de un torneo", description = "Actualiza la imagen de un torneo")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del producto", example = "1", required = true),
            @Parameter(name = "file", description = "Fichero a subir", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo actualizado"),
            @ApiResponse(responseCode = "400", description = "Torneo no válido"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
    })
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TorneoRespose> updateImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {

        // Lista de extensiones de archivos permitidos.
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");

        try {
            String contentType = file.getContentType();

            if (!file.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
                // Actualizamos el producto
                return ResponseEntity.ok(torneosService.updateImage(id, file, true));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el torneo valida o esta esta vacia");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
    }

    @Operation(summary = "Borra un torneo", description = "Eliminar torneo")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Torneo borrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        torneosService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Finaliza un torneo y reparte los puntos", description = "Finalizar torneo")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Torneo borrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
    })
    @DeleteMapping("/finalizarTorneo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> finalizarToneo(@PathVariable Long id){
        torneosService.finalizarTorneo(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //metodo que permite mostrar los mensajes de error
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
