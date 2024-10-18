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
import java.util.Objects;

public class CommandWeapons implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @Nullable String s, @Nullable String[] args) {
        if (!(sender instanceof Player)) return false;

        @NotNull Player player = (Player) sender;

        switch (args.length) {
            case 0:
                ItemListGui.inventory.open(player);
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("all")) {

                    for (byte id : ItemManager.getItems().keySet()) {
                        player.getInventory().addItem(Objects.requireNonNull(ItemManager.getItem(id), "Aucun item trouvé avec cet ID").item().getItem());
                    }

                } else {

                    for (@NotNull ItemManager i : ItemManager.getItems().values()) {
                        if (i.item().getKey().equalsIgnoreCase(args[0])) {
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @Nullable String alias, @Nullable String[] args) {
        @NotNull List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("all");
            for (@NotNull ItemManager i : ItemManager.getItems().values()) {
                completions.add(i.item().getKey());
            }
        }
        return completions;
    }

}