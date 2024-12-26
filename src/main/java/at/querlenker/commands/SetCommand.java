package at.querlenker.commands;

import at.querlenker.eventtimer.EventTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                long minutes = Long.parseLong(args[1]);
                EventTimer.setDuration(minutes);
                sender.sendMessage(ChatColor.GREEN + "Der Timer wurde auf" + minutes + " Minuten gesetzt.");
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid number format. Please enter a valid number of minutes.");
            }
        }
        return true;
    }
}
