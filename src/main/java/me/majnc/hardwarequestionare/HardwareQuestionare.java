package me.majnc.hardwarequestionare;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Array;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

public final class HardwareQuestionare extends JavaPlugin implements Listener {

    // You can change these
    int delay = 7; // time for each question
    int countdown = 20; // time for choosing nickname countdown

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class QuestionStruct {
        private String question;
        private String ansA;
        private String ansB;
        private String ansC;
        private String ansD;
        private int correct;


        public QuestionStruct(String question, String ansA, String ansB, String ansC, String ansD, int correct) {
            this.question = question;
            this.ansA = ansA;
            this.ansB = ansB;
            this.ansC = ansC;
            this.ansD = ansD;
            this.correct = correct;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnsA() {
            return ansA;
        }

        public String getAnsB() {
            return ansB;
        }

        public String getAnsC() {
            return ansC;
        }

        public String getAnsD() {
            return ansD;
        }

        public int getCorrect() {
            return correct;
        }
    }

    public class PlayerData {
        private UUID playerUUID;
        private String nickname;
        private boolean hasResponded;
        private int[] answers;
        private int score;

        public PlayerData(UUID playerUUID, String nickname, int size) {
            this.playerUUID = playerUUID;
            this.nickname = nickname;
            answers = new int[size];
            score = 0;
        }

        public int[] getAnswers() {
            return answers;
        }

        public void setAnswers(int[] intArray) {
            this.answers = intArray;
        }

        public void setAnswersValueAtIndex(int index, int value) {
            answers[index] = value;
        }

        public void addScore(int value) {
            score += value;
        }

        public int getScore() {
            return score;
        }

        public int getAnswersValueAtIndex(int index) {
            return answers[index];
        }

        public UUID getPlayerUUID() {
            return playerUUID;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setHasResponded(boolean hasResponded) {
            this.hasResponded = hasResponded;
        }
    }
    List<PlayerData> playerDatas = new ArrayList<>();
    List<QuestionStruct> questionStructs = new ArrayList<>();

    private static void CLearChat() {
        for (int i=0; i<20; i++)
        {
            Bukkit.broadcastMessage("");
        }
    }

    // DO NOT CHANGE THESE
    int run = 0; // sets if the starting sequence should be run (==0) or if the quiz is already in progress (>0)
    int questionNumber = 0; // what question are we on

    ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
    Objective objective85 = scoreboard.registerNewObjective("Test", "dummy", ChatColor.BLUE + "Title");

    private void RefreshLeaderboard() {
        objective85.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective85.setDisplayName(ChatColor.BLUE + "Žebříček hráčů");
        List<Score> scores = new ArrayList<>();

        int[] scoresInt = new int[playerDatas.size()];
        for (int l = 0; l < playerDatas.size(); l++) {
            scoresInt[l] = playerDatas.get(l).getScore();
        }
        Arrays.sort(scoresInt);

        for (int l = scoresInt.length - 1; l >= 0; l--) {
            for (int z = 0; z < playerDatas.size(); z++) {
                if (playerDatas.get(z).getScore() == scoresInt[l]) {
                    Score score = objective85.getScore(playerDatas.get(z).getNickname());
                    score.setScore(playerDatas.get(z).getScore());
                    scores.add(score);
                }
            }
        }

        for (Player player1 : Bukkit.getOnlinePlayers()) {
            player1.setScoreboard(scoreboard);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // runs when the executed command is "start"
        if (command.getName().equalsIgnoreCase("start") && sender.isOp()) {
            if (run == 0) {
                // setting up the quiz - making questions and creating List with all players playing
                questionStructs.add(new QuestionStruct("Jaký je hlavní rozdíl mezi sluchátky In-ear a Over-ear?", "Větší komfort", "Menší hlučnost", "Větší kapacita baterie", "Lepší kvalita zvuku", 4));
                questionStructs.add(new QuestionStruct("Proč jsou některá sluchátka vybavena aktivním potlačením šumu?", "Pro lepší kvalitu zvuku", "Pro lepší komunikaci s okolím", "Pro větší pohodlí", "Pro vyšší cenu", 2));
                questionStructs.add(new QuestionStruct("K čemu slouží funkce 'touch controls' u sluchátek?", "K ovládání hlasitosti", "K přijímání hovorů", "K přepínání mezi skladbami", "K vypínání sluchátek", 2));
/*                questionStructs.add(new QuestionStruct("Jaký typ sluchátek je vhodný pro sportovce?", "Over-ear", "In-ear", "On-ear", "Supraaurální", 2));
                questionStructs.add(new QuestionStruct("Co znamená pojem 'Hi-Res Audio'?", "Audio s vysokým rozlišením", "Audio s nízkým rozlišením", "Audio s velkým počtem kanálů", "Audio s malým počtem kanálů", 1));
                questionStructs.add(new QuestionStruct("Jaký typ sluchátek se používá pro profesionální poslech hudby?", "Over-ear", "In-ear", "On-ear", "Supraaurální", 1));
                questionStructs.add(new QuestionStruct("Jaký je výhodou sluchátek s aktivním potlačením šumu oproti sluchátkům bez této funkce?", "Nižší cena", "Větší pohodlí", "Lepší kvalita zvuku", "Lepší komunikace s okolím", 4));
                questionStructs.add(new QuestionStruct("Který z následujících typů sluchátek nabízí nejlepší zvukový projev?", "Over-ear", "In-ear", "On-ear", "Supraaurální", 1));*/


                // creates List of all players online and shows starting title to all of them
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title " + player.getName() + " title {\"text\":\"KVÍZ ZAČNE ZA NEMALOU CHVÍLY\", \"bold\":true, \"italic\":false, \"color\":\"red\", \"stay\":200}");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title " + player.getName() + " subtitle {\"text\":\"Nastavte si jména\", \"italic\":false, \"color\":\"blue\", \"stay\":200}");
                    PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName(), questionStructs.size());
                    playerDatas.add(playerData);
                    player.sendMessage("Vyberte si své jméno:");
                }

                // set default answers for when the player didn't answer
                for (int j = 0; j < playerDatas.size(); j++) {
                    for (int i = 0; i < questionStructs.size(); i++) {
                        playerDatas.get(j).setAnswersValueAtIndex(i,1);
                    }
                }

                // runs countdown command that starts countdown for the quiz and starts the quiz
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "countdown");
            } else if (run == 1) {
                // this section handles every question
                CLearChat();
                // prints the question into chat
                Bukkit.broadcastMessage(ChatColor.AQUA + "--Otázka " + (questionNumber + 1) + "--");
                Bukkit.broadcastMessage(questionStructs.get(questionNumber).getQuestion());
                Bukkit.broadcastMessage(ChatColor.AQUA + "a) " + ChatColor.WHITE + questionStructs.get(questionNumber).getAnsA());
                Bukkit.broadcastMessage(ChatColor.AQUA + "b) " + ChatColor.WHITE + questionStructs.get(questionNumber).getAnsB());
                Bukkit.broadcastMessage(ChatColor.AQUA + "c) " + ChatColor.WHITE + questionStructs.get(questionNumber).getAnsC());
                Bukkit.broadcastMessage(ChatColor.AQUA + "d) " + ChatColor.WHITE + questionStructs.get(questionNumber).getAnsD());
                // so that players can answer
                for (PlayerData playerData : playerDatas) {
                    playerData.setHasResponded(false);
                }

                // runs this section again but the next question
                if (questionNumber < questionStructs.size()-1) {
                    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                        @Override
                        public void run() {
                            questionNumber++;
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "start");
                        }
                    }, 20 * delay);
                } else {
                // ends the quiz
                    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title {\"text\":\"KONEC KVÍZU   \", \"bold\":true, \"italic\":false, \"color\":\"red\", \"stay\":200}");
                        }
                    }, 20 * 10);
                }

                // says if your answer was wrong or not before the next question is ran (hence the '-40' in delay
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < playerDatas.size(); i++) {
                            if (playerDatas.get(i).getAnswersValueAtIndex(questionNumber) == questionStructs.get(questionNumber).getCorrect()) {
                                Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.GREEN + "Tvá odpověď byla správná");
                                playerDatas.get(i).addScore(50);
                            } else {
                                Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.RED + "Tvá odpověď byla špatná");
                            }
                            playerDatas.get(i).setHasResponded(true);
                        }
                        RefreshLeaderboard();
                    }
                }, (20 * delay) - 40);
            }
        } else if (command.getName().equalsIgnoreCase("countdown")) {
            // checks if it's still countdowning or if it's the end of countdown
            if (countdown > 0) {
                // countdown titles
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title {\"text\":\"KVÍZ ZAČNE ZA NEMALOU CHVÍLY\", \"bold\":true, \"italic\":false, \"color\":\"red\", \"stay\":200}");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a subtitle {\"text\":\"" + countdown +"\", \"italic\":false, \"color\":\"blue\", \"stay\":200}");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "playsound minecraft:block.dispenser.launch master @a ~ ~ ~ 1 1.75 1");
                // runs this command again in one second but with lower countdown (remaining seconds to the start of the quiz)
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "countdown");
                    }
                }, 1 * 20);
            } else {
                // if the countdown is at the end
                // starts the quiz
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a title {\"text\": \"\"}");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "title @a subtitle {\"text\": \"\"}");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "playsound minecraft:item.goat_horn.sound.0 master @a 0 0 0 1 1 1");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "playsound minecraft:music_disc.otherside master @a 0 0 0 2 1 1");
                run++;
                boolean settingNicknames = false;
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "start");
            }
            countdown--;
        }
        return false;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (run == 0) {
            for (PlayerData playerData : playerDatas) {
                if (playerData.getPlayerUUID().equals(player.getUniqueId())) {
                    playerData.setNickname(message);
                    player.sendMessage("Tvé jméno bylo nastaveno na " + message);
                    break;
                }
            }
        } else {
            for (int i = 0; i < playerDatas.size(); i++) {
                if (playerDatas.get(i).getPlayerUUID().equals(player.getUniqueId())) {
                    if (!playerDatas.get(i).hasResponded) {
                        if (message.equals("a")) {
                            playerDatas.get(i).setHasResponded(true);
                            playerDatas.get(i).setAnswersValueAtIndex(questionNumber, 1);
                            Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.GREEN + "Tvá odpověď byla zaznamenána");
                        } else if (message.equals("b")) {
                            playerDatas.get(i).setHasResponded(true);
                            playerDatas.get(i).setAnswersValueAtIndex(questionNumber, 2);
                            Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.GREEN + "Tvá odpověď byla zaznamenána");
                        } else if (message.equals("c")) {
                            playerDatas.get(i).setHasResponded(true);
                            playerDatas.get(i).setAnswersValueAtIndex(questionNumber, 3);
                            Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.GREEN + "Tvá odpověď byla zaznamenána");
                        } else if (message.equals("d")) {
                            playerDatas.get(i).setHasResponded(true);
                            playerDatas.get(i).setAnswersValueAtIndex(questionNumber, 4);
                            Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.GREEN + "Tvá odpověď byla zaznamenána");
                        } else {
                            Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.RED + "Možnost " + message + " neexistuje.");
                        }
                    } else {
                        Bukkit.getPlayer(playerDatas.get(i).getPlayerUUID()).sendMessage(ChatColor.RED + "Svojí odpověď již nemůžeš změnit.");
                    }
                }
            }
        }

        event.setCancelled(true);
    }

}
