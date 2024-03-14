package com.jhnr.myclub.controllers;


import com.jhnr.myclub.constants.RoleConstants;
import com.jhnr.myclub.controllers.form.*;
import com.jhnr.myclub.entities.AppRole;
import com.jhnr.myclub.entities.AppUser;
import com.jhnr.myclub.exceptions.AccountException;
import com.jhnr.myclub.services.AccountService;
import com.jhnr.myclub.services.AmazonClientService;
import com.jhnr.myclub.utils.FileUtil;
import com.jhnr.myclub.utils.JwtUtils;
import com.jhnr.myclub.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@Slf4j
public class AccountRestController {

    private AccountService accountService;
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;

    private AmazonClientService amazonClientService;

    public AccountRestController(AccountService accountService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, AmazonClientService amazonClientService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.amazonClientService = amazonClientService;
    }

    @DeleteMapping(path = "/user/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppUser> deleteUser(@PathVariable("userName") String userName) {

        AppUser user = accountService.getUser(userName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountService.deleteUser(user));
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<AppUser>> appUsers() {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountService.findUsers());
    }

    @GetMapping(path = "/user/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<AppUser> appUser(@PathVariable("userName") String userName) {
        return ResponseEntity.ok(accountService.getUser(userName));
    }

    @PutMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<AppUser> updateUser(Authentication authentication, @RequestBody UserUpdateForm userUpdateForm) {

        String oldUserName = authentication.getName();

        AppUser appUser = accountService.getUser(oldUserName);

        //Ca match on continue
        AppUser newUser = UserUtils.appUserRegisterToAppUser(userUpdateForm);
        try {
            AppUser userUpdated = accountService.updateUser(appUser, newUser);
            return ResponseEntity.ok(userUpdated);
        } catch (AccountException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping(path = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppRole> addAppRole(@RequestBody AppRole appRole) {
        return ResponseEntity.ok(accountService.addRole(appRole));
    }

    @PostMapping(path = "/add-user-role", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppUser> addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUserName(), roleUserForm.getRoleName());
        return ResponseEntity.ok(accountService.getUser(roleUserForm.getUserName()));
    }

    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public Authentication profile(Authentication authentication) {
        return authentication;
    }

    @PostMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppUser> addUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok(accountService.addUser(appUser));
    }

    @GetMapping(path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> refreshToken(Authentication authentication) {

        String userName = authentication.getName();

        AppUser appUser = accountService.getUser(userName);
        String authorities = appUser.getAppRoles()
                .stream()
                .map(AppRole::getRoleName)
                .collect(Collectors.joining(" "));

        String jwtAccessToken = JwtUtils.generateAccessToken(appUser.getUserName(), authorities, jwtEncoder);
        Map<String, String> result = new HashMap<>();
        result.put("token_type", OAuth2AccessToken.TokenType.BEARER.getValue());
        result.put("access_token", jwtAccessToken);
        result.put("user_name", userName);

        return result;

    }

    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUser> signUp(@RequestBody UserRegisterForm userRegisterForm) {
        AppUser appUser = new AppUser();
        appUser.setUserName(userRegisterForm.getUserName());
        appUser.setPassword(userRegisterForm.getPassword());
        appUser.setEmail(userRegisterForm.getEmail());

        AppRole roleUser = accountService.getRole(RoleConstants.ROLE_USER);
        appUser.setAppRoles(List.of(roleUser));

        try {
            appUser = accountService.addUser(appUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(appUser);
    }



    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@RequestBody UserLoginForm userLoginForm) {
        String userName = userLoginForm.getUserName();
        String password = userLoginForm.getPassword();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password)
        );


        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String jwtAccessToken = JwtUtils.generateAccessToken(userName, authorities, jwtEncoder);

        String jwtRefreshToken = JwtUtils.generateRefreshToken(userName, jwtEncoder);

        Map<String, String> result = new HashMap<>();
        result.put("access_token", jwtAccessToken);
        result.put("refresh_token", jwtRefreshToken);
        result.put("token_type", OAuth2AccessToken.TokenType.BEARER.getValue());

        return result;
    }


    @PutMapping(path = "/update-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUser> updatePassword(@RequestBody UserUpdatePasswordForm userUpdatePasswordForm, Authentication authentication) {

        try {
            AppUser user = accountService.getUser(authentication.getName());
            accountService.updatePassword(user, userUpdatePasswordForm.getOldPassword(), userUpdatePasswordForm.getNewPassword());
            return ResponseEntity.ok(user);
        } catch (AccountException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/upload-image", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<AppUser> uploadImage(@RequestPart(value = "file") MultipartFile multipartFile, Authentication authentication) {

        String userName = authentication.getName();
        AppUser user = accountService.getUser(userName);
        String fileName = user.getId() + "/profile" + FileUtil.getExtension(multipartFile.getOriginalFilename());

        try {
            File file = FileUtil.convertMultiPartToFile(multipartFile);
            this.amazonClientService.putObject(fileName, file);

            AppUser appUser = accountService.getUser(userName);
            accountService.updateUserImage(appUser, fileName);

            return ResponseEntity.ok(appUser);
        } catch (IOException e) {
            log.error("Error during convert multipartfile to file");
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping(path = "/profile-image/{userName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getProfileImage(@PathVariable("userName") String userName) {

        AppUser appUser = accountService.getUser(userName);

        try {
            File tempFile = Files.createTempFile(String.valueOf(appUser.getId()), "").toFile();
            URL url = new URL(amazonClientService.getURLFromObjectKey(appUser.getImage(), String.valueOf(appUser.getId())));
            FileUtils.copyURLToFile(url, tempFile);
            byte[] fileContent = Files.readAllBytes(tempFile.toPath());

            String extension = FileUtil.getExtension(appUser.getImage());
            return ResponseEntity.ok().contentType(
                FileUtil.extensionToMediaType(extension)
            ).body(fileContent);
        } catch (IOException e) {
            log.error("Error during recovering file from image field");
        }
        return ResponseEntity.internalServerError().build();
    }
}
