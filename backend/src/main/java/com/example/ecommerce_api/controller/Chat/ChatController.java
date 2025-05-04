package com.example.ecommerce_api.controller.Chat;

import java.security.Principal;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.featuresDTO.ChatRequest;
import com.example.ecommerce_api.dto.featuresDTO.ChatResponse;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
import com.example.ecommerce_api.services.Chat.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    public ChatController(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request, Principal principal) {
        String email = principal.getName();
    
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return new ChatResponse("User not found.");
        }
    
        User user = userOpt.get();
    
        if (!(user instanceof Customer)) {
            return new ChatResponse("Access denied: only customers can use this feature.");
        }
    
        Customer customer = (Customer) user;
        String reply = chatService.generateReply(request.getMessage(), customer);
        return new ChatResponse(reply);
    }

}
