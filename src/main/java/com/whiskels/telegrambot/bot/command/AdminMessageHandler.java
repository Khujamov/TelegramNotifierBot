package com.whiskels.telegrambot.bot.command;

import com.whiskels.telegrambot.model.User;
import com.whiskels.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static com.whiskels.telegrambot.bot.command.Command.ADMIN_MESSAGE;
import static com.whiskels.telegrambot.util.TelegramUtils.createMessageTemplate;

@Component
@Slf4j
public class AdminMessageHandler extends AbstractBaseHandler {
    private final UserService userService;

    public AdminMessageHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> operate(User admin, String message) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = userService.getUsers()
                .stream()
                .map(user -> createMessageTemplate(user)
                        .setText(message))
                .collect(Collectors.toList());

        messagesToSend.add(createMessageTemplate(admin)
                .setText(String.format("Notified %d users", messagesToSend.size())));

        return messagesToSend;
    }

    @Override
    public Command supportedCommand() {
        return ADMIN_MESSAGE;
    }
}