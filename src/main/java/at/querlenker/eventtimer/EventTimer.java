package at.querlenker.eventtimer;

import at.querlenker.commands.TimerCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import at.querlenker.listeners.CommandTabListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventTimer extends JavaPlugin implements Listener {
    public List<Material> remaining = new ArrayList<>();
    public List<Material> remainingmobs = new ArrayList<>();

    @Getter
    private static EventTimer plugin;

    private boolean enabled;
    private static boolean timerEnabled = false;
    public FileConfiguration itemsConfig;
    public File itemsFile;
    private static ScheduledTask timerTask;
    private static long startTime;
    private static long duration;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        enabled = this.getConfig().getBoolean("activated", false);
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new CommandTabListener(), this);
        getCommand("timer").setExecutor(new TimerCommand());
        setDuration(getConfig().getLong("set-time", 60));

        if (getConfig().getBoolean("timer-running", false)) {
            continueTimer();
        }
    }

    @Override
    public void onDisable() {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public static boolean isTimerEnabled() {
        return timerEnabled;
    }

    public static void startTimer() {
        if (timerEnabled) return;

        timerEnabled = true;
        startTime = System.currentTimeMillis();

        plugin.getConfig().set("timer-running", true);
        plugin.getConfig().set("start-time", startTime);
        plugin.saveConfig();

        timerTask = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> {
            if (!timerEnabled) {
                task.cancel();
                return;
            }

            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            long remainingSeconds = duration - elapsedSeconds;

            if (remainingSeconds <= 0) {
                stopTimer();
                plugin.getServer().broadcast(net.kyori.adventure.text.Component.text("§6Timer has ended!"));
                return;
            }

            String timeDisplay = formatTime(remainingSeconds);

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.getServer().getRegionScheduler().execute(plugin, player.getLocation(), () -> {
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent("§6Verbleibende Zeit: §f" + timeDisplay)
                    );
                });
            }
        }, 1, 1);
    }

    public static void continueTimer() {
        timerEnabled = true;
        timerTask = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> {
            startTime = plugin.getConfig().getLong("start-time");
            if (!timerEnabled) {
                task.cancel();
                return;
            }

            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            long remainingSeconds = duration - elapsedSeconds;

            if (remainingSeconds <= 0) {
                stopTimer();
                plugin.getServer().broadcast(net.kyori.adventure.text.Component.text("§6Timer has ended!"));
                return;
            }

            String timeDisplay = formatTime(remainingSeconds);

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.getServer().getRegionScheduler().execute(plugin, player.getLocation(), () -> {
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent("§6Verbleibende Zeit: §f" + timeDisplay)
                    );
                });
            }
        }, 1, 1);
    }

    public static void stopTimer() {
        if (!timerEnabled) return;

        timerEnabled = false;
        if (timerTask != null) {
            timerTask.cancel();
        }

        plugin.getConfig().set("timer-running", false);
        plugin.getConfig().set("start-time", null);
        plugin.saveConfig();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            plugin.getServer().getRegionScheduler().execute(plugin, player.getLocation(), () -> {
                player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        new TextComponent("")
                );
            });
        }
    }

    private static String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void setDuration(long minutes) {
        plugin.getConfig().set("set-time", minutes);
        plugin.saveConfig();
        duration = minutes * 60;
    }

    public static long getDuration() {
        return duration / 60;
    }
}