package com.example.HuceBack.dto.Chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {
    private List<Long> userIds;
    private String chat_name;
    private String chat_image;
}
