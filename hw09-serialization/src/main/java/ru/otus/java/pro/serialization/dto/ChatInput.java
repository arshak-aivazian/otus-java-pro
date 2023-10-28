package ru.otus.java.pro.serialization.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChatInput {
    @JsonProperty("chat_sessions")
    private List<ChatSession> chatSessions;

    @Data
    public static class ChatSession {
        @JsonProperty("chat_id")
        private Integer chatId;
        @JsonProperty("chat_identifier")
        private String chatIdentifier;
        @JsonProperty("display_name")
        private String displayName;
        @JsonProperty("is_deleted")
        private Integer isDeleted;
        private List<Member> members;
        private List<Message> messages;
    }

    @Data
    public static class Member {
        private String first;
        @JsonProperty("handle_id")
        private Integer handleId;
        @JsonProperty("image_path")
        private String imagePath;
        private String last;
        private String middle;
        @JsonProperty("phone_number")
        private String phoneNumber;
        private String service;
        @JsonProperty("thumb_path")
        private String thumbPath;
    }

    @Data
    public static class Message {
        @JsonProperty("ROWID")
        private Integer rowid;
        private String attributedBody;
        @JsonProperty("belong_number")
        private String belongNumber;
        private Long date;
        @JsonProperty("date_read")
        private Long dateRead;
        private String guid;
        @JsonProperty("handle_id")
        private Integer handleId;
        @JsonProperty("has_dd_results")
        private Integer hasDdResults;
        @JsonProperty("is_deleted")
        private Integer isDeleted;
        @JsonProperty("is_from_me")
        private Integer isFromMe;
        @JsonProperty("send_date")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")
        private Date sendDate;
        @JsonProperty("send_status")
        private Integer sendStatus;
        private String service;
        private String text;
    }
}
