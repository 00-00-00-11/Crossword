package me.repocord.crosswordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrosswordBot extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        try {
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (args[0].equalsIgnoreCase("!crossword"))
                if (args.length != 2) {
                    event.getChannel().sendMessage("Wrong usage!").queue();
                } else {
                    for (String string : match(args[1])) {
                        event.getChannel().sendMessage(string).queue();
                    }
                }
        } catch (Exception e) {
            System.out.println("lmao");
        }
    }

    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault(System.getenv("token")).build();
        jda.addEventListener(new CrosswordBot());

    }
    public static List<String> match(String crossword) throws Exception {
        Scanner file = new Scanner(new File("message.txt"));
        List<String> wordsList = new ArrayList<String>();
        while (file.hasNextLine()) {
            wordsList.add(file.nextLine());
        }
        List<String> nextWords = new ArrayList<String>();
        List<String> foundMatches = new ArrayList<String>();
        int wordLength = crossword.length();
        for (String word : wordsList) {
            if (word.length() == wordLength) {
                nextWords.add(word);
            }
        }
        char[] wordAsChars;
        char[] crosswordAsChars = crossword.toCharArray();
        boolean matchNotBroken;
        for (String word : nextWords) {
            wordAsChars = word.toCharArray();
            matchNotBroken = true;
            for (int i = 0; i < word.length() && i < crossword.length() && matchNotBroken; i++) {
                if (wordAsChars[i] != crosswordAsChars[i]) {
                    if (crosswordAsChars[i] != '_') matchNotBroken = false;
                }
            }
            if (matchNotBroken) foundMatches.add(word);
        }
        file.close();
        return foundMatches;
    }
}
