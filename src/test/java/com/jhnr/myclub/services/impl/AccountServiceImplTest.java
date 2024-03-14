package com.jhnr.myclub.services.impl;

import com.jhnr.myclub.entities.AppRole;
import com.jhnr.myclub.entities.AppUser;
import com.jhnr.myclub.exceptions.AccountException;
import com.jhnr.myclub.repositories.AppRoleRepository;
import com.jhnr.myclub.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class AccountServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppRoleRepository appRoleRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void addUser() {
        AppUser appUser = new AppUser();
        appUser.setPassword("test");
        when(passwordEncoder.encode(anyString())).thenReturn("test");

        when(appUserRepository.save(any())).thenReturn(appUser);

        AppUser result = accountService.addUser(appUser);

        assertNotNull(result);
        assertEquals(appUser, result);
        assertEquals("test",result.getPassword() );
        assertFalse(result.isProfileCompleted());
    }

    @Test
    void existUserByUserName() {
        when(appUserRepository.existsAppUserByUserName(any())).thenReturn(true);

        boolean result = accountService.existUserByUserName("test");
        assertTrue(result);
    }

    @Test
    void updateUser() throws AccountException {

        AppUser oldUser = new AppUser();
        oldUser.setUserName("oldUsername");
        oldUser.setEmail("old@example.com");
        oldUser.setImage("oldImage");
        oldUser.setFirstName("oldFirstName");
        oldUser.setLastName("oldLastName");
        oldUser.setPhoneNumber("oldPhoneNumber");

        AppUser newUser = new AppUser();
        newUser.setUserName("newUsername");
        newUser.setEmail("new@example.com");
        newUser.setImage("newImage");
        newUser.setFirstName("newFirstName");
        newUser.setLastName("newLastName");
        newUser.setPhoneNumber("newPhoneNumber");

        when(appUserRepository.existsAppUserByUserName(anyString())).thenReturn(false);

        AppUser updatedUser = accountService.updateUser(oldUser, newUser);

        assertEquals("newUsername", updatedUser.getUserName());
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("newImage", updatedUser.getImage());
        assertEquals("newFirstName", updatedUser.getFirstName());
        assertEquals("newLastName", updatedUser.getLastName());
        assertEquals("newPhoneNumber", updatedUser.getPhoneNumber());

        newUser.setUserName("oldUsername");
        when(appUserRepository.existsAppUserByUserName(anyString())).thenReturn(true);

        assertThrows(AccountException.class, () -> accountService.updateUser(oldUser, newUser));

    }

    @Test
    void getUser() {
        AppUser user = new AppUser();
        user.setUserName("oldUsername");
        user.setEmail("old@example.com");
        user.setImage("oldImage");
        user.setFirstName("oldFirstName");
        user.setLastName("oldLastName");
        user.setPhoneNumber("oldPhoneNumber");
        when(appUserRepository.findByUserName(anyString())).thenReturn(user);

        AppUser result = accountService.getUser("oldUsername");

        assertEquals(user, result);

    }

    @Test
    void getRole() {
        AppRole appRole = new AppRole();
        appRole.setRoleName("user");

        when(appRoleRepository.findByRoleName(anyString())).thenReturn(appRole);

        AppRole result = accountService.getRole("user");

        assertEquals(appRole, result);
    }

    @Test
    void addRole() {
        AppRole appRole = new AppRole();
        appRole.setId(4L);
        appRole.setRoleName("user");

        when(appRoleRepository.save(any())).thenReturn(appRole);

        AppRole result = accountService.addRole(appRole);

        assertEquals(appRole, result);
    }

    @Test
    void addRoleToUser() {


        AppUser user = new AppUser();
        user.setUserName("oldUsername");
        user.setEmail("old@example.com");
        user.setImage("oldImage");
        user.setFirstName("oldFirstName");
        user.setLastName("oldLastName");
        user.setPhoneNumber("oldPhoneNumber");


        when(appUserRepository.findByUserName(anyString())).thenReturn(user);

        AppRole appRole = new AppRole();
        appRole.setId(4L);
        appRole.setRoleName("user");

        when(appRoleRepository.findByRoleName(anyString())).thenReturn(appRole);


        accountService.addRoleToUser("oldUsername","user");
        assertTrue(user.getAppRoles().contains(appRole));
    }

    @Test
    void findUsers() {
        AppUser user = new AppUser();
        user.setUserName("oldUsername");
        user.setEmail("old@example.com");
        user.setImage("oldImage");
        user.setFirstName("oldFirstName");
        user.setLastName("oldLastName");
        user.setPhoneNumber("oldPhoneNumber");

        List<AppUser> appUserList = List.of(user);

        when(appUserRepository.findAll()).thenReturn(appUserList);

        List<AppUser> results = accountService.findUsers();

        assertEquals(appUserList, results);
    }

//    @Test
//    void updatePassword() throws AccountException {
//
//        AppUser user = new AppUser();
//        user.setPassword("test");
//
//        // Simuler le scénario où l'ancien mot de passe ne correspond pas
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
//        assertThrows(AccountException.class, () -> accountService.updatePassword(user, "test1", "test"));
//
//        // Simuler le scénario où l'ancien mot de passe correspond
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//
//        when(passwordEncoder.encode(anyString())).thenReturn("test1");
//
//        accountService.updatePassword(user, "test", "test1");
//
//        assertEquals("test1", user.getPassword());
//
//    }
}