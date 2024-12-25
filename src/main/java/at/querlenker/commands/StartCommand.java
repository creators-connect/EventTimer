package at.querlenker.commands;

import at.querlenker.eventtimer.EventTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (EventTimer.TimerEnabled) {
                sender.sendMessage(ChatColor.AQUA + "[EventTimer] " + ChatColor.GREEN
                        + "The Timer is already running!");
            } else {
                EventTimer.startTimer();
                sender.sendMessage(ChatColor.AQUA + "[EventTimer] " + ChatColor.GREEN
                        + "Successfully started the Timer!");
            }
            return true;
        }

}
