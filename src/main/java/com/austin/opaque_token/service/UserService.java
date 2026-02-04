package com.austin.opaque_token.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.austin.opaque_token.common.Role;
import com.austin.opaque_token.dto.user.UserCreateRequest;
import com.austin.opaque_token.dto.user.UserPasswordRequest;
import com.austin.opaque_token.dto.user.UserResponse;
import com.austin.opaque_token.dto.user.UserResponseWithCredentials;
import com.austin.opaque_token.dto.user.UserUpdateRequest;
import com.austin.opaque_token.entity.UserEntity;
import com.austin.opaque_token.exception.NotFoundException;
import com.austin.opaque_token.mapper.UserMapper;
import com.austin.opaque_token.repository.UserRepository;
import com.austin.opaque_token.security.exception.ApplicationAuthenticationException;
import com.austin.opaque_token.security.user.AuthUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final String defaultAdminUsername;

    private final String defaultAdminPassword;

    private UserRepository userRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;


    public UserService(
      @Value("${admin.default.username}") String defaultAdminUsername,
      @Value("${admin.default.password}") String defaultAdminPassword,
      UserRepository userRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder) {
        this.defaultAdminUsername = defaultAdminUsername;
        this.defaultAdminPassword = defaultAdminPassword;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
  }

  public UserResponse createUser(UserCreateRequest userCreateRequest) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(userCreateRequest.username());
    userEntity.setPasswordHash(passwordEncoder.encode(userCreateRequest.password()));
    userEntity.setFirstName(userCreateRequest.firstName());
    userEntity.setLastName(userCreateRequest.lastName());
    userEntity.setRoles(Set.of(Role.ROLE_USER));
    userEntity.setActive(true);

    UserEntity savedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(savedEntity);
}

public void changeUserPassword(UserPasswordRequest passwordUpdateRequest, AuthUser authUser){
    UserEntity userEntity = getUserEntity(authUser.userId());

    if(!passwordEncoder.matches(passwordUpdateRequest.oldPassword(), userEntity.getPasswordHash())) {
       throw new ApplicationAuthenticationException("Old password is incorrect");
}
    userEntity.setPasswordHash(passwordEncoder.encode(passwordUpdateRequest.newPassword()));
    userRepository.save(userEntity);
}

 
  public UserResponse updateUser(String userId, UserUpdateRequest userUpdateRequest) {

    UserEntity userEntity = getUserEntity(userId);

    userEntity.setUsername(userUpdateRequest.userName());
    userEntity.setFirstName(userUpdateRequest.firstName());
    userEntity.setLastName(userUpdateRequest.lastName());

    UserEntity updatedEntity = userRepository.save(userEntity);

    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse getUserById(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    return userMapper.toResponse(userEntity);
  }

  public UserResponseWithCredentials getUserCredentialsByUsername(String username) {

    UserEntity userEntity =
        userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    return new UserResponseWithCredentials(
        userMapper.toResponse(userEntity), userEntity.getPasswordHash());
  }

  public Page<UserResponse> listUsers(Pageable pageable) {

    return userRepository.findAll(pageable).map(userMapper::toResponse);
  }

  public UserResponse activateUser(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.setActive(true);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse deactivateUser(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.setActive(false);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public UserResponse promoteUserToAdmin(String userId) {

    UserEntity userEntity = getUserEntity(userId);
    userEntity.getRoles().add(Role.ROLE_ADMIN);

    UserEntity updatedEntity = userRepository.save(userEntity);
    return userMapper.toResponse(updatedEntity);
  }

  public void createDefaultAdminIfNotExist() {

    boolean anyAdminExist = userRepository.isAnyAdminExist();

    if (anyAdminExist) {
      log.info("Admin already exist. Skipping creation of default admin user");
      return;
    }

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(defaultAdminUsername);
    userEntity.setPasswordHash(passwordEncoder.encode(defaultAdminPassword));
    userEntity.setFirstName("Admin");
    userEntity.setLastName("Admin");
    userEntity.setRoles(Set.of(Role.ROLE_ADMIN));
    userEntity.setActive(true);

    UserEntity savedUser = userRepository.save(userEntity);

    log.info("Default admin user was created with id={}", savedUser.getId());
  }

  private UserEntity getUserEntity(String userId) {

    return userRepository.findById(userId).orElseThrow(NotFoundException::new);
  }

}
