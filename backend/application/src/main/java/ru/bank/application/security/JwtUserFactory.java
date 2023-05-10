package ru.bank.application.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.bank.persistence.entity.user.User;

import java.util.Collections;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }
    public static JwtUser create(User user){
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
    
}
