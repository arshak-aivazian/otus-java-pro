package ru.otus.java.pro.serialization.csvstrategy;

import lombok.RequiredArgsConstructor;
import ru.otus.java.pro.serialization.dto.ChatOutput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public class ChatOutputCsvStrategy implements CsvParsingStrategy<ChatOutput> {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final ChatOutput chatOutput;

    @Override
    public String[] getHeaders() {
        return new String[]{"chat_identifier", "members_last", "belong_number", "message_send_date", "text"};
    }

    @Override
    public List<String[]> getValues() {
        List<String[]> values = new ArrayList<>();
        for (ChatOutput.ChatSession session : chatOutput.getChatSessions()) {
            String chatIdentifier = session.getChatIdentifier();
            String members = session.getMembers().stream().map(it -> it.getLast()).collect(Collectors.joining(", "));
            List<ChatOutput.Message> messages = session.getMessageGroups().stream().flatMap(it -> it.getMessages().stream()).collect(Collectors.toList());
            for (ChatOutput.Message message : messages) {

                values.add(new String[]{
                        chatIdentifier,
                        members,
                        message.getBelongNumber(),
                        SIMPLE_DATE_FORMAT.format(message.getSendDate()),
                        message.getText()
                });
            }
        }
        return values;
    }

    @Override
    public ChatOutput createObject(List<String[]> source) {
        Map<String, List<ChatOutput.Message>> sessionMessages = getSessionMessages(source);
        Map<String, List<ChatOutput.Member>> sessionMembers = getSessionMembers(source);
        return createChatOutput(sessionMessages, sessionMembers);
    }

    private Map<String, List<ChatOutput.Member>> getSessionMembers(List<String[]> source) {
        Map<String, List<ChatOutput.Member>> sessionMembers = new HashMap<>();

        for (String[] stringValues : source) {
            String chatIdentifier = stringValues[0];
            String[] lastMembers = stringValues[1].split(", ");
            List<ChatOutput.Member> members = Arrays.stream(lastMembers).map(ChatOutput.Member::new).collect(Collectors.toList());
            sessionMembers.put(chatIdentifier, members);
        }
        return sessionMembers;
    }

    private Map<String, List<ChatOutput.Message>> getSessionMessages(List<String[]> source) {
        Map<String, List<ChatOutput.Message>> sessionMessages = new HashMap<>();

        for (String[] stringValues : source) {
            String chatIdentifier = stringValues[0];
            String belongNumber = stringValues[2];
            Date sendDate = null;
            try {
                sendDate = SIMPLE_DATE_FORMAT.parse(stringValues[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String text = stringValues[4];

            List<ChatOutput.Message> messages = sessionMessages.getOrDefault(chatIdentifier, new ArrayList<>());
            messages.add(new ChatOutput.Message(belongNumber, sendDate, text));
            sessionMessages.put(chatIdentifier, messages);
        }
        return sessionMessages;
    }

    private ChatOutput createChatOutput(Map<String, List<ChatOutput.Message>> sessionMessages,
                                        Map<String, List<ChatOutput.Member>> sessionMembers) {
        List<ChatOutput.ChatSession> chatSessions = new ArrayList<>();
        for (Map.Entry<String, List<ChatOutput.Message>> entry : sessionMessages.entrySet()) {
            String sessionId = entry.getKey();
            ChatOutput.ChatSession chatSession = createChatSession(sessionId, sessionMembers.get(sessionId), sessionMessages.get(sessionId));
            chatSessions.add(chatSession);
        }
        return new ChatOutput(chatSessions);
    }

    private ChatOutput.ChatSession createChatSession(String id, List<ChatOutput.Member> members, List<ChatOutput.Message> messages) {
        Map<String, List<ChatOutput.Message>> messageByNumber = messages.stream().collect(groupingBy(ChatOutput.Message::getBelongNumber));
        List<ChatOutput.MessageGroup> messageGroups = messageByNumber.entrySet().stream()
                .map(it -> new ChatOutput.MessageGroup(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
        return new ChatOutput.ChatSession(id, members, messageGroups);
    }
}
