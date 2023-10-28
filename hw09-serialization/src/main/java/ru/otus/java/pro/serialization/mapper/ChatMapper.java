package ru.otus.java.pro.serialization.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.otus.java.pro.serialization.dto.ChatInput;
import ru.otus.java.pro.serialization.dto.ChatOutput;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public abstract class ChatMapper {

    public static final ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    public abstract ChatOutput map(ChatInput source);

    @Mapping(target = "messageGroups", source = "messages", qualifiedByName = "messageGroup")
    abstract ChatOutput.ChatSession map(ChatInput.ChatSession source);

    abstract ChatOutput.Message map(ChatInput.Message source);

    @Named("messageGroup")
    List<ChatOutput.MessageGroup> map(List<ChatInput.Message> source) {
        Map<String, List<ChatOutput.Message>> messagesByNumber = new HashMap<>();

        for (ChatInput.Message inputMessage : source) {
            ChatOutput.Message outputMessage = map(inputMessage);
            List<ChatOutput.Message> messages = messagesByNumber.getOrDefault(outputMessage.getBelongNumber(), new ArrayList<>());
            messages.add(outputMessage);
            messagesByNumber.put(outputMessage.getBelongNumber(), messages);
        }

        for (Map.Entry<String, List<ChatOutput.Message>> entry : messagesByNumber.entrySet()) {
            List<ChatOutput.Message> messages = entry.getValue();
            messages.sort(Comparator.comparing(ChatOutput.Message::getSendDate));
        }

        return messagesByNumber.entrySet().stream()
                .map(entry -> {
                    var messageGroup = new ChatOutput.MessageGroup();
                    messageGroup.setPhone(entry.getKey());
                    messageGroup.setMessages(entry.getValue());
                    return messageGroup;
                })
                .collect(Collectors.toList());
    }
}
