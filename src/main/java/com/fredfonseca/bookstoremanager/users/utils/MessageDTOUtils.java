package com.fredfonseca.bookstoremanager.users.utils;

import com.fredfonseca.bookstoremanager.users.dto.MessageDTO;
import com.fredfonseca.bookstoremanager.users.entity.Users;

public class MessageDTOUtils {

    public static MessageDTO creationMessage(Users createdUser) {
        return returnMessage(createdUser, "created");
    }

    public static MessageDTO updatedMessage(Users updatedUser) {
        return returnMessage(updatedUser, "updated");
    }

    private static MessageDTO returnMessage(Users updatedUser, String action) {
        String createdName = updatedUser.getName();
        Long createdId = updatedUser.getId();
        String createdUserMessage = String.format("User %s with ID %s successfully %s", createdName, createdId, action);
        return MessageDTO.builder()
                .message(createdUserMessage)
                .build();
    }
}
