package com.jhnr.myclub.controllers;

import com.jhnr.myclub.controllers.form.RoleUserForm;
import com.jhnr.myclub.controllers.form.UserLoginForm;
import com.jhnr.myclub.controllers.form.UserRegisterForm;
import com.jhnr.myclub.controllers.form.UserUpdateForm;
import com.jhnr.myclub.entities.AppRole;
import com.jhnr.myclub.entities.AppUser;
import com.jhnr.myclub.exceptions.AccountException;
import com.jhnr.myclub.services.AccountService;
import com.jhnr.myclub.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountRestControllerTest {
    @InjectMocks
    private AccountRestController accountRestController;
    @Mock
    private AccountService accountService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private  Authentication authentication;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtUtils jwtUtils;


    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    void deleteUser() {
        // Mocking
        when(accountService.getUser(anyString())).thenReturn(new AppUser());
        when(accountService.deleteUser(any())).thenReturn(new AppUser());

        // Test
        ResponseEntity<AppUser> response = accountRestController.deleteUser("testUser");

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    void appUsers() {
        // Mocking
        when(accountService.findUsers()).thenReturn(List.of(new AppUser(), new AppUser()));

        // Test
        ResponseEntity<List<AppUser>> response = accountRestController.appUsers();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }


    @Test
    @WithMockUser(username = "testUser", authorities = {"SCOPE_USER"})
    void updateUser() throws AccountException {
        // Mocking
        when(accountService.getUser(anyString())).thenReturn(new AppUser());
        when(accountService.updateUser(any(), any())).thenReturn(new AppUser());

        // Test
        ResponseEntity<AppUser> response = accountRestController.updateUser(mock(Authentication.class), new UserUpdateForm());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    void addAppRole() {
        // Mocking
        when(accountService.addRole(any())).thenReturn(new AppRole());

        // Test
        ResponseEntity<AppRole> response = accountRestController.addAppRole(new AppRole());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    void addRoleToUser() {
        // Mocking
        doNothing().when(accountService).addRoleToUser(anyString(), anyString());
        when(accountService.getUser(anyString())).thenReturn(new AppUser());

        RoleUserForm roleUserForm= new RoleUserForm();
        roleUserForm.setRoleName("test");
        roleUserForm.setUserName("testUser");

        // Test
        ResponseEntity<AppUser> response = accountRestController.addRoleToUser(roleUserForm);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_USER"})
    void profile() {
        // Mocking
        Authentication authentication = mock(Authentication.class);

        // Test
        Authentication response = accountRestController.profile(authentication);

        // Assertions
        assertNotNull(response);
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_ADMIN"})
    void addUser() {
        // Mocking
        when(accountService.addUser(any())).thenReturn(new AppUser());

        // Test
        ResponseEntity<AppUser> response = accountRestController.addUser(new AppUser());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @WithMockUser(authorities = {"SCOPE_USER"})
    void refreshToken() {
        // Mocking
        AppUser appUser = new AppUser();

        appUser.setUserName("testUser");
        Collection<AppRole> roles = new ArrayList<>();
        AppRole appRole= new AppRole(4L, "test");
        roles.add(appRole);
        appUser.setAppRoles(roles);

        when(authentication.getName()).thenReturn("testUser");
        when(accountService.getUser(any())).thenReturn(appUser);


        Jwt mockedJwt = mock(Jwt.class);
        when(jwtEncoder.encode(any())).thenReturn(mockedJwt);
        try (MockedStatic mockStatic = mockStatic(JwtUtils.class)) {

            mockStatic.when(() -> jwtUtils.generateAccessToken(anyString(), any(), any()))
                    .thenReturn("mockedAccessToken");

            Authentication auth = mock(Authentication.class);
            Map<String, String> response = accountRestController.refreshToken(auth);

            // Assertions
            assertNotNull(response);
            assertEquals("Bearer", response.get("token_type"));
            assertEquals("mockedAccessToken", response.get("access_token"));
        }

        // Test

    }



    @Test
    void signUp() {
        // Mocking
        when(accountService.getRole(anyString())).thenReturn(new AppRole());
        when(accountService.addUser(any())).thenReturn(new AppUser());

        // Test
        ResponseEntity<AppUser> response = accountRestController.signUp(new UserRegisterForm());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void login() {
        // Mocking
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        Jwt mockedJwt = mock(Jwt.class);
        when(jwtEncoder.encode(any())).thenReturn(mockedJwt);
        try (MockedStatic mockStatic = mockStatic(JwtUtils.class)) {

            mockStatic.when(() -> jwtUtils.generateAccessToken(anyString(), any(), any()))
                    .thenReturn("mockedAccessToken");

            mockStatic.when(() -> jwtUtils.generateRefreshToken(anyString(), any()))
                    .thenReturn("mockedRefreshToken");
            UserLoginForm userLoginForm = new UserLoginForm();
            userLoginForm.setUserName("testUser");
            userLoginForm.setPassword("test");
            Map<String, String> response = accountRestController.login(userLoginForm);

            // Assertions
            assertNotNull(response);
            assertEquals("Bearer", response.get("token_type"));
            assertEquals("mockedAccessToken", response.get("access_token"));
            assertEquals("mockedRefreshToken", response.get("refresh_token"));
        }
    }
}