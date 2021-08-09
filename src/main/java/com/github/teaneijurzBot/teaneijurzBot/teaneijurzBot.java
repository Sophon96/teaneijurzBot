package com.github.teaneijurzBot.teaneijurzBot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

public class teaneijurzBot {

    public static void main(String[] args) {
        final GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build()
                .login()
                .block();

        assert client != null;

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().contains("js"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("fuck js"))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> user.getId().asLong() == 302050872383242240L).orElse(false))
                .filter(message -> message.getEmbeds().get(0).getDescription().orElse("").contains("Bump done"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("boomp success :sunglasses:"))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> user.getId().asLong() == 302050872383242240L).orElse(false))
                .filter(message -> message.getEmbeds().get(0).getDescription().orElse("").contains("Please wait"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("boomp fail"))
                .subscribe();

        client.onDisconnect().block();
    }

}
