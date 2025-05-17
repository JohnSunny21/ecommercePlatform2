package com.devtale.ecommerceplatform2.service.user;

import com.devtale.ecommerceplatform2.dto.UserDto;
import com.devtale.ecommerceplatform2.model.User;
import com.devtale.ecommerceplatform2.request.CreateUserRequest;
import com.devtale.ecommerceplatform2.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request,Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
