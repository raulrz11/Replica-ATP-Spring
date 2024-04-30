package com.example.attpspringboot.controllers;

import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaResponse;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.services.tenistas.TenistasServiceImpl;
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

import java.util.*;

@RestController
@RequestMapping("/tenistas")
public class TenistasController {
    private final TenistasServiceImpl tenistasService;
    private final PaginationLinks paginationLinks;

    @Autowired
    public TenistasController(TenistasServiceImpl tenistasService, PaginationLinks paginationLinks) {
        this.tenistasService = tenistasService;
        this.paginationLinks = paginationLinks;
    }

    @Operation(summary = "Obtiene todos los tenistas", description = "Lista de todos los tenistas")
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del tenista", example = "Raul"),
            @Parameter(name = "ranking", description = "Ranking actual", example = "4"),
            @Parameter(name = "puntos", description = "Puntos actuales", example = "20.8"),
            @Parameter(name = "pais", description = "Pais de nacimiento", example = "Hungria"),
            @Parameter(name = "fechaNacimiento", description = "Fecha de nacimiento", example = "2002-03-04"),
            @Parameter(name = "edad", description = "Edad del tenista", example = "40"),
            @Parameter(name = "altura", description = "Altura dell tenista", example = "90.5"),
            @Parameter(name = "peso", description = "Peso del tenista", example = "60.5"),
            @Parameter(name = "inicioProfesional", description = "Fecha de comienzo a profesional", example = "2024-03-04"),
            @Parameter(name = "entrenador", description = "Entrenador", example = "Javi"),
            @Parameter(name = "manoBuena", description = "Mano buena del tenista", example = "DERECHA"),
            @Parameter(name = "reves", description = "Reves del tenista", example = "DOS_MANOS"),
            @Parameter(name = "priceMoney", description = "Dinero total del tenista", example = "20.00"),
            @Parameter(name = "bestRanking", description = "Mejor ranking", example = "2"),
            @Parameter(name = "victorias", description = "Victorias del tenista", example = "8"),
            @Parameter(name = "derrotas", description = "Derrotas del tenista", example = "3"),
            @Parameter(name = "WL", description = "Porcentaje victorias y derrotas", example = "68%/45%"),
            @Parameter(name = "page", description = "Número de página", example = "0"),
            @Parameter(name = "size", description = "Tamaño de la página", example = "10"),
            @Parameter(name = "sortBy", description = "Campo de ordenación", example = "puntos"),
            @Parameter(name = "direction", description = "Dirección de ordenación", example = "desc")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de tenistas"),
    })
    @GetMapping()
    public ResponseEntity<Page<TenistaResponse>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam() Optional<Integer> ranking,
            @RequestParam() Optional<Double> puntos,
            @RequestParam() Optional<String> pais,
            @RequestParam() Optional<Date> fechaNacimiento,
            @RequestParam() Optional<Integer> edad,
            @RequestParam() Optional<Double> altura,
            @RequestParam() Optional<Double> peso,
            @RequestParam() Optional<Date> inicioProfesional,
            @RequestParam() Optional<String> entrenador,
            @RequestParam() Optional<Tenista.Mano> manoBuena,
            @RequestParam() Optional<Tenista.Reves> reves,
            @RequestParam() Optional<Double> priceMoney,
            @RequestParam() Optional<Integer> bestRanking,
            @RequestParam() Optional<Integer> victorias,
            @RequestParam() Optional<Integer> derrotas,
            @RequestParam() Optional<String> WL,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "puntos") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<TenistaResponse> pageResult = tenistasService.findAll(nombre, ranking, puntos,
                pais, fechaNacimiento, edad, altura, peso, inicioProfesional,
                entrenador, manoBuena, reves, priceMoney, bestRanking, WL, victorias, derrotas, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @Operation(summary = "Obtiene un tenista por su id", description = "Tenista con el id especificado")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenista encontrado"),
            @ApiResponse(responseCode = "404", description = "Tenista no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<TenistaResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(tenistasService.findById(id));
    }

    @Operation(summary = "Crea un tenista", description = "Tenista nuevo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tenista a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenista creado"),
            @ApiResponse(responseCode = "400", description = "Tenista no válido"),
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenistaResponse> create(@Validated @RequestBody TenistaCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tenistasService.save(dto));
    }

    @Operation(summary = "Actualiza un tenista", description = "Actualizar tenista")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tenista a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenista actualizado"),
            @ApiResponse(responseCode = "400", description = "Tenista no válido"),
            @ApiResponse(responseCode = "404", description = "Tenista no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenistaResponse> update(@Validated @RequestBody TenistaUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(tenistasService.update(dto, id));
    }

    @Operation(summary = "Actualiza la imagen de un tenista", description = "Actualiza la imagen de un tenista")
    @Parameters({
            @Parameter(name = "id", description = "Identificador del producto", example = "1", required = true),
            @Parameter(name = "file", description = "Fichero a subir", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenista actualizado"),
            @ApiResponse(responseCode = "400", description = "Tenista no válido"),
            @ApiResponse(responseCode = "404", description = "Tenista no encontrado"),
    })
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenistaResponse> updateImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {

        // Lista de extensiones de archivos permitidos.
        List<String> permittedContentTypes = List.of("image/png", "image/jpg", "image/jpeg", "image/gif");

        try {
            String contentType = file.getContentType();

            if (!file.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
                // Actualizamos el producto
                return ResponseEntity.ok(tenistasService.updateImage(id, file, true));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto válida o esta está vacía");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede saber el tipo de la imagen");
        }
    }

    @Operation(summary = "Borra un tenista", description = "Eliminar tenista")
    @Parameters({
            @Parameter(name = "id", description = "Identificador unico", example = "1", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tenista borrado"),
            @ApiResponse(responseCode = "404", description = "Tenista no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        tenistasService.deleteById(id);
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
