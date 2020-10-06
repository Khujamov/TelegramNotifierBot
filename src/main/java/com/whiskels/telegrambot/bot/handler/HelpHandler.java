package com.whiskels.telegrambot.bot.handler;

import com.whiskels.telegrambot.bot.BotCommand;
import com.whiskels.telegrambot.bot.builder.MessageBuilder;
import com.whiskels.telegrambot.model.User;
import com.whiskels.telegrambot.security.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/**
 * Shows help message and inline keyboard based on user role
 * <p>
 * Available to: everyone
 */
@Component
@Slf4j
@BotCommand(command = {"/HELP", "/START"})
public class HelpHandler extends AbstractBaseHandler {
    @Value("${bot.test.name}")
    private String botUsername;

    private List<AbstractBaseHandler> handlers;

    private AuthorizationService authorizationService;

    public HelpHandler(List<AbstractBaseHandler> handlers, AuthorizationService authorizationService) {
        this.handlers = handlers;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<BotApiMethod<Message>> handle(User user, String message) {
        log.debug("Preparing /HELP");
        MessageBuilder builder = MessageBuilder.create(user)
                .line("Hello. I'm *%s*", botUsername)
                .line("Here are your available commands")
                .line("Use [/help] command to display this message");

        // Dynamically creates buttons if handler has message and is available to user
        for (AbstractBaseHandler handler : handlers) {
            String msg = handler.getClass().getAnnotation(BotCommand.class).message();
            if (!msg.isEmpty() && authorizationService.authorize(handler.getClass(), user)) {
                builder.row()
                        .button(msg, handler.getClass().getAnnotation(BotCommand.class).command()[0]);
            }
        }

        return List.of(builder.build());
    }
}
