package fr.yanis.superbecorpweapons.command;

import fr.yanis.superbecorpweapons.gui.ItemListGui;
import fr.yanis.superbecorpweapons.item.management.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandWeapons implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        switch (args.length){
            case 0:
                ItemListGui.inventory.open(player);
                return true;
            case 1:
                if(args[0].equalsIgnoreCase("all")){
                    for (ItemStack itemStack : ItemManager.getItems().keySet()) {
                        player.getInventory().addItem(itemStack);
                    }
                } else {
                    for (ItemManager i : ItemManager.getItems().values()) {
                        if(i.item().getKey().equalsIgnoreCase(args[0])){
                            player.getInventory().addItem(i.item().getItem());
                            return true;
                        }
                    }
                    player.sendMessage("§cL'item " + args[0] + " n'existe pas !");
                    return true;
                }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("all");
            for (ItemManager i : ItemManager.getItems().values()) {
                completions.add(i.item().getKey());
            }
        }
        return completions;
    }

}
