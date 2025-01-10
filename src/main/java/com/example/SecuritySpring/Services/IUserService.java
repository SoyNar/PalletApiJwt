package com.example.SecuritySpring.Services;

import com.example.SecuritySpring.Application.DtoRequest.UserRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.UserResponseDto;
import com.example.SecuritySpring.Application.Model.Pallet;
import com.example.SecuritySpring.Application.Model.User;

import java.util.List;

public interface IUserService {
    public UserResponseDto registerUser (UserRequestDto userRequest);
    public User findByUsername(String username);
    public User updateUser(Long id, UserRequestDto userRequest);
   public UserResponseDto deleteUser(Long id);
   public UserResponseDto registerByAdmin(UserRequestDto userRequest);
    List<Pallet> getCarrierPallets(Long carrierId);
    public List<UserResponseDto> getAllUsers();
    //metodo para devolver todos los usuarios de tipo carrier
    public List<UserResponseDto> getAllCarrier();


}
