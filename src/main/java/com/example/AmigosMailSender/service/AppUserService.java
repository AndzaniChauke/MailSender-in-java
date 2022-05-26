package com.example.AmigosMailSender.service;

import com.example.AmigosMailSender.model.AppUser;
import com.example.AmigosMailSender.model.ConfirmationToken;
import com.example.AmigosMailSender.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND="USER WITH EMAIL %S NOT FOUND";

    private final AppUserRepository appUserRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;



    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND,email)));
    }

    public String signUpUser(AppUser appUser){
        //We checking if email exists here with a boolean
        boolean userExists=appUserRepository.
                findByEmail(appUser.getEmail())
                .isPresent();

        if(userExists){
            throw new IllegalStateException("email already exist ");
        }

        String encodedPassword=bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();


        //Sending a confirmation token

        ConfirmationToken confirmationToken=new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser


        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);


        //Confirmation token=new C
        //TODO: SEND EMAIL
        return token;

    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
