package com.hms.controller;


import com.hms.entity.AppUser;
import com.hms.payload.LoginDto;
import com.hms.payload.TokenDto;
import com.hms.repository.AppUserRepository;
import com.hms.service.OTPService;
import com.hms.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private AppUserRepository appUserRepository;
     private UserService userService;
     private OTPService otpService;

    public  UserController(AppUserRepository appUserRepository, UserService userService ){
        this.appUserRepository = appUserRepository;
        this.userService = userService;
    }
    @PostMapping("/signup")
    public ResponseEntity<?> createUser (@RequestBody AppUser user){

        Optional<AppUser> opUsername;
        opUsername = appUserRepository.findByUsername(user.getUsername());

        if (opUsername.isPresent()) {
            return new ResponseEntity<>("Username already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }



        Optional<AppUser>  opEmail;
        opEmail = appUserRepository.findByEmail(user.getEmail());

        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(encryptedPassword);
        user.setRole("ROLE_USER");

        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public String getMessage(){
        return "hello";
    }


    //problem in this login code
    //http://localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto dto
    ) {
      String token = userService.verifyLogin(dto);
      //Optional<AppUser> opUser = appUserRepository.findByMobile();
        if (token != null){
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);

        }else {
            return new ResponseEntity<>("Invalid username/password", HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/signup-property-owner")
    public ResponseEntity<?> createPropertyOwnerUser (@RequestBody AppUser user){

        Optional<AppUser> opUsername;
        opUsername = appUserRepository.findByUsername(user.getUsername());

        if (opUsername.isPresent()) {
            return new ResponseEntity<>("Username already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }



        Optional<AppUser>  opEmail;
        opEmail = appUserRepository.findByEmail(user.getEmail());

        if (opEmail.isPresent()) {
            return new ResponseEntity<>("Email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(encryptedPassword);
        user.setRole("ROLE_OWNER");

        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/generate-login-otp")
    public String login(@RequestParam String mobileNumber) {
     otpService.generateAndSendOTP(mobileNumber);
     return "Otp Generated Successfully";
    }

    @PostMapping("/validate-login-otp")
    public String validateOTP(@RequestParam String mobileNumber, @RequestParam String otp) {
        //validate OTP
        boolean isValid = otpService.validateOTP(mobileNumber, otp);
        if (isValid) {
            return "Login Successful";
        }else {
            return "Invalid or Expired OTP";
        }
    }
}

