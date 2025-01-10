package com.example.SecuritySpring.Controller;

import com.example.SecuritySpring.Application.DtoRequest.UserRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.UserResponseDto;
import com.example.SecuritySpring.Application.Model.User;
import com.example.SecuritySpring.Exceptions.ErrorResponse;
import com.example.SecuritySpring.Exceptions.UserNotFoundException;
import com.example.SecuritySpring.Services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class CarrierController {

    private static final Logger logger = LoggerFactory.getLogger(CarrierController.class);

    @Autowired
    private IUserService userService;

    //Econtrar un usuario por correo electronico por parametro
@GetMapping("/find")

    public ResponseEntity<?> findByUsername(@RequestParam String username) {
    logger.info("Buscando usuario con username: {}", username);
    try {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    } catch (UserNotFoundException ex) {
        logger.error("Usuario no encontrado: {}", username);
        throw ex;
    }
  }


  //actualizar un usuario
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto userDto ) {
        try {
            User user = this.userService.updateUser(id, userDto);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (UserNotFoundException ex) {
            // Si el usuario no es encontrado,404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                    "Usuario no encontrado",
                    "USER_NOT_FOUND",
                    HttpStatus.NOT_FOUND.value()
            ));
        } catch (IllegalArgumentException ex) {
            // Si el correo ya existe (Bad Request)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                    ex.getMessage(),
                    "BAD_REQUEST",
                    HttpStatus.BAD_REQUEST.value()
            ));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    e.getMessage(),
                    "INTERNAL_SERVER_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }
//eliminar un usuario usando el id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(

            @PathVariable Long id){
    try{
        UserResponseDto user = this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    } catch (UserNotFoundException ex) {
        logger.error("Usuario no encontrado: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                ex.getMessage(),
                "NOT_FOUND",
                HttpStatus.NOT_FOUND.value()
        ));
     }
    }

    //metodo para obtener todos los usuarios
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAllUsers(){

       List<UserResponseDto> userAll = this.userService.getAllUsers();
       //retonas  la dara que devolveras en el body del json
        // en este caso es una data personalizada en nuestra clase Response
       return ResponseEntity.status(HttpStatus.OK).body(userAll);
    }

    //metodo para traer todos los transportistas solo con el rol transportista
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/getCarrier")
    public ResponseEntity<List<UserResponseDto>> getAllCarrier(){
     List<UserResponseDto> users = this.userService.getAllCarrier();
     return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
