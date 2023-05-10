package ru.bank.application.service.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bank.application.dto.user.SaveUserDto;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.repository.user.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public User registration(SaveUserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPatronymic(userDto.getPatronymic());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return user;
    }



    public User loadUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }


}
