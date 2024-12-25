package at.querlenker.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                if (sender.hasPermission("eventtimer.start")) {
                    return new StartCommand().onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (sender.hasPermission("eventtimer.stop")) {
                    return new StopCommand().onCommand(sender, command, label, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
            }
        }

        sender.sendMessage(ChatColor.AQUA + "[EventTimer] " + ChatColor.GREEN + "Usage: " + ChatColor.GOLD
                + "/timer /start/stop");
        return true;
    }
}
