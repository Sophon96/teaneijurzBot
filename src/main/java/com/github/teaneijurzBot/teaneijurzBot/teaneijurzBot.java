package com.github.teaneijurzBot.teaneijurzBot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.util.ApplicationCommandOptionType;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

public class teaneijurzBot {

    // ID to use for slash commands
    private static final long TEEN_GUILD_ID = 812850829044351016L;

    private static final Logger log = Loggers.getLogger(teaneijurzBot.class);

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

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().contains("yuh"))
                .flatMap(message -> message.addReaction(ReactionEmoji.codepoints("U+1F60E")))
                .subscribe();


        /*
         * Prisoner's Dilemma
         */
        ApplicationCommandRequest prisonersDilemmaGame = ApplicationCommandRequest.builder()
                .name("pdgame")
                .description("Play a game based on Prisoner's Dilemma")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("opponent")
                        .description("The person you want to play against")
                        .type(ApplicationCommandOptionType.USER.getValue())
                        .required(true)
                        .build())
                .build();

        RestClient restClient = client.getRestClient();

        long applicationId = restClient.getApplicationId().block();

        restClient.getApplicationService()
                .createGuildApplicationCommand(applicationId, TEEN_GUILD_ID, prisonersDilemmaGame)
                .doOnError(e -> log.warn("Unable to create guild command", e))
                .onErrorResume(e -> Mono.empty())
                .block();

        client.on(new ReactiveEventAdapter() {
            @Override
            public Publisher<?> onSlashCommand(SlashCommandEvent event) {
                // TODO: Implement actual logic
                return event.reply("Not yet implemented, Coming Soon:tm:.");
            }
        }).blockLast();


        client.onDisconnect().block();
    }

}
