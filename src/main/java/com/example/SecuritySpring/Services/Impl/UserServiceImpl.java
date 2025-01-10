package com.example.SecuritySpring.Services.Impl;

import com.example.SecuritySpring.Application.DtoRequest.UserRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.UserResponseDto;
import com.example.SecuritySpring.Application.Model.Pallet;
import com.example.SecuritySpring.Application.Model.Role;
import com.example.SecuritySpring.Application.Model.User;
import com.example.SecuritySpring.Exceptions.UserNotFoundException;
import com.example.SecuritySpring.Exceptions.ValidateErrors;
import com.example.SecuritySpring.Exceptions.ValidationException;
import com.example.SecuritySpring.Repository.IUserRepository;
import com.example.SecuritySpring.Services.IAuditLogService;
import com.example.SecuritySpring.Services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

@Autowired
private IUserRepository userRepository;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private IAuditLogService auditLogService;


//registrar un usuario en la base de datos
    // en este solo los transportadores pueden inscribirse
    @Override
    public UserResponseDto registerUser(UserRequestDto userRequest) {



        // buscar al usuario en la base de datos usando el
        // metodo del repositorio que busca por email
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
           throw  new UserNotFoundException(" el usuario ya existe");
        }


        // Usar el builder para crear el User
        User userModel = User.builder()
                .username(userRequest.getUsername())
                .fullname(userRequest.getFullname())
                .password(passwordEncoder.encode(userRequest.getPassword()))  // Encriptar la contraseña
                .role(Role.CARRIER)  //Asigna rol carrier
                .build();

        // Guardar el usuario en la base de datos
        userModel = userRepository.save(userModel);

        // Devolver el UserResponseDto con la información del usuario
        return UserResponseDto.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .fullname(userModel.getFullname())
                .role(userModel.getRole())
                .build();
    }

    //encontrar un usauario por correo electronico
    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(()
                        ->new UsernameNotFoundException("Usuario no encontrado"));
    }

    //actualizar los datos de un usuario
    @Override
    public User updateUser(Long id, UserRequestDto userRequest) {

        //buscar al usuario por Id
        User user = userRepository.findById(id)
                .orElseThrow(()
                ->new UserNotFoundException("USuario no existe"));
          if (userRequest.getUsername() != null &&
                !user.getUsername().equals(userRequest.getUsername()) &&
                userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Ese correo esta siendo usando por otro usuario");
        }
//actualizar los datos si el campo no es null
        if (userRequest.getUsername() != null) user.setUsername(userRequest.getUsername());
        if (userRequest.getPassword() != null) user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if (userRequest.getFullname() != null) user.setFullname(userRequest.getFullname());

        User savedUser = userRepository.save(user);
        auditLogService.logAction("UPDATE", "USER", savedUser.getId(), "Updated user information");

        return savedUser;
    }

    //borrar un usuario
    @Transactional
    @Override
    public UserResponseDto deleteUser(Long id) {
     //buscar usario por id
        User user = this.userRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException("Usuario no existe"));
        //auditoria
        auditLogService.logAction("DELETE", "USER", id, "Deleted user");
        userRepository.delete(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    // en este metodo, el admin ouede inscribir a cualquier usuario
    @Override
    public UserResponseDto registerByAdmin(UserRequestDto userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw  new UserNotFoundException(" el usuario ya existe");
        }

        if(userRequest.getRole() == null){
            List<ValidateErrors> errors = List.of(new ValidateErrors("RoleNull", "Proporcione un rol"));
            throw new ValidationException(errors);
        }
        // Usar el builder para crear el User
        User userModel = User.builder()
                .username(userRequest.getUsername())
                .fullname(userRequest.getFullname())
                .password(passwordEncoder.encode(userRequest.getPassword()))  // Encriptar la contraseña
                .role(userRequest.getRole())  //Asigna un rol existente en el enum ADMIN/CARRIER
                .build();

        // Guardar el usuario en la base de datos
        userModel = userRepository.save(userModel);

        // Devolver el UserResponseDto con la información del usuario
        return UserResponseDto.builder()
                .id(userModel.getId())
                .username(userModel.getUsername())
                .fullname(userModel.getFullname())
                .role(userModel.getRole())
                .build();

    }


    // obtener todos los pallets de un usuario de tipo carrier en su rol
    @Override
    public List<Pallet> getCarrierPallets(Long carrierId) {
        //buscar usuario por id
        User carrier = this.userRepository.findById(carrierId)
                .orElseThrow(()
                        ->new UserNotFoundException("No encontrado"));
        //verificar si el rol no es diferente de carrier
        if(carrier.getRole() != Role.CARRIER){
            throw new IllegalArgumentException("El usuario no es un transportista");
        }
        return carrier.getAssignedPallets();
    }
    //metodo para obtener todos los usuarios
    @Override
    public List<UserResponseDto> getAllUsers() {
      List<User> users = this.userRepository.findAll();
        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .fullname(user.getFullname())
                        .role(user.getRole())
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAllCarrier() {

        List<User> carrier = this.userRepository.findAllByRole(Role.CARRIER);
        return carrier.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .fullname(user.getFullname())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .build()
                ).collect(Collectors.toList());
    }


}
