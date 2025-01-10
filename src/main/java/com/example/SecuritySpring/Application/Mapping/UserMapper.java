//package com.example.SecuritySpring.Application.Mapping;
//
//import com.example.SecuritySpring.Application.DtoRequest.UserRequestDto;
//import com.example.SecuritySpring.Application.DtoResponse.UserResponseDto;
//import com.example.SecuritySpring.Application.Model.User;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//   @Mapping(target = "password", ignore = true)
//   User UserDtoToUser (UserRequestDto requestDto);
//   @Mapping(target = "password", source = "password")
//   UserResponseDto UserToUserDto (User user);
//}
