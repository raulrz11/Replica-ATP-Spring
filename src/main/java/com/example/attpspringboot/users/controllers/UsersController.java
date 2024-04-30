package com.example.attpspringboot.users.controllers;

import com.example.attpspringboot.users.dtos.UserCreateDto;
import com.example.attpspringboot.users.dtos.UserResponse;
import com.example.attpspringboot.users.dtos.UserUpdateDto;
import com.example.attpspringboot.users.services.UsersServiceImpl;
import com.example.attpspringboot.utils.PaginationLinks;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersServiceImpl usersService;
    private final PaginationLinks paginationLinks;

    public UsersController(UsersServiceImpl usersService, PaginationLinks paginationLinks) {
        this.usersService = usersService;
        this.paginationLinks = paginationLinks;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam() Optional<String> username,
            @RequestParam() Optional<String> email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(nombre, username, email, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.ceate(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> update(@Validated @RequestBody UserUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(usersService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        usersService.deleteById(id);
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
