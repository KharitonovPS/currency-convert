package org.kps.currency.domain.users.service;

import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.users.entity.User;
import org.kps.currency.domain.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var chat = message.getChat();

            User user = new User();
            user.setChatId(message.getChatId());
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLasName(chat.getLastName());
            user.setRegistrationDate(Instant.now());
            user.setSubscription(null);
            user.setStatus("ACTIVE");

            userRepository.save(user);
            log.info("Registered user: {}", user);
        } else {
            log.info("User already exists: {}", userRepository.findById(message.getChatId()).orElse(null));
        }
    }

    public String deleteUser(Long chatId) {
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(optionalUser.get().getChatId());
            log.info("user with {} was deleted", chatId);
            return "Your personnel data was deleted";
        } else {
            return "Could not find user";
        }
    }
}
