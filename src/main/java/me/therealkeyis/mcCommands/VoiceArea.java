package me.therealkeyis.mcCommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.therealkeyis.MinecraftPlugin;

public class VoiceArea implements CommandExecutor {
    public static String ItemName = ChatColor.DARK_PURPLE + "AreaDefiner";
    public static NamespacedKey use_count;
    public static NamespacedKey x1;
    public static NamespacedKey z1;

    public VoiceArea(MinecraftPlugin plug) {
        use_count = new NamespacedKey(plug, "use_count");
        x1 = new NamespacedKey(plug, "x1");
        z1 = new NamespacedKey(plug, "z1");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        if (args.length < 1)
            return false;
        var player = (Player) sender;
        var item = new ItemStack(Material.STICK, 1);
        var meta = item.getItemMeta();
        meta.setDisplayName(ItemName + " " + args[0]);
        meta.getPersistentDataContainer().set(use_count, PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        return true;
    }
}
