package ru.otus.java.pro.serialization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatOutput {
    @JsonProperty("chat_sessions")
    private List<ChatSession> chatSessions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatSession {
        @JsonProperty("chat_identifier")
        private String chatIdentifier;
        private List<Member> members;
        @JsonProperty("message_groups")
        private List<MessageGroup> messageGroups;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageGroup {
        private String phone;
        private List<Message> messages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member {
        private String last;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        @JsonProperty("belong_number")
        private String belongNumber;
        @JsonProperty("send_date")
        private Date sendDate;
        private String text;
    }
}
