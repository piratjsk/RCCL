package net.piratjsk.rccl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

final class RCCLCommand implements CommandExecutor {

    private final RCCL plugin;

    RCCLCommand(final RCCL plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender.hasPermission("rccl.reload") && args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.unregisterAllRecipes();
            plugin.reloadConfig();
            plugin.registerRecipesFromConfig();
            sender.sendMessage("[rccl] Plugin zostal przeladowany.");
            return true;
        }
        sender.sendMessage("[rccl] arr..");
        return true;
    }

}
