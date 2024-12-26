package at.querlenker.commands;

import at.querlenker.eventtimer.EventTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!EventTimer.isTimerEnabled()) {
            sender.sendMessage(ChatColor.AQUA + "[EventTimer] " + ChatColor.GREEN
                    + "The Timer is not running!");
        } else {
            EventTimer.stopTimer();
            sender.sendMessage(ChatColor.AQUA + "[EventTimer] " + ChatColor.GREEN
                    + "Successfully stopped the Timer!");
        }
        return true;
    }

}
