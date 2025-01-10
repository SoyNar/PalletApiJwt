package com.example.SecuritySpring.Controller;

import com.example.SecuritySpring.Application.DtoRequest.LoginDtoRequest;
import com.example.SecuritySpring.Application.DtoRequest.UserRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.JwtResponse;
import com.example.SecuritySpring.Application.DtoResponse.UserResponseDto;
import com.example.SecuritySpring.Services.IUserService;
import com.example.SecuritySpring.Services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid  UserRequestDto userRequestDto) {
   try{
       UserResponseDto userResponse  = this.userService.registerUser(userRequestDto);
       return new ResponseEntity<>(userResponse,HttpStatus.CREATED);
   }catch (RuntimeException e){
       return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
   } catch (Exception e) {
       return new ResponseEntity<>("Ocurrio un error al registrar al usuario",HttpStatus.INTERNAL_SERVER_ERROR);
   }
   }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
   @PostMapping("/registerA")
   public ResponseEntity<?> registerByAdmin (@RequestBody @Valid UserRequestDto requestDto){
       try{
           UserResponseDto userResponse  = this.userService.registerByAdmin(requestDto);
           return new ResponseEntity<>(userResponse,HttpStatus.CREATED);
       }catch (RuntimeException e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
       } catch (Exception e) {
           return new ResponseEntity<>("Ocurrio un error al registrar al usuario",HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   //metodo para autenticacion de un usuario
  @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDtoRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }



}
