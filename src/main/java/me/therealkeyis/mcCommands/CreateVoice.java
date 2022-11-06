package me.therealkeyis.mcCommands;

import java.util.logging.Logger;

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

/**
 * The voice area command handler
 */
public class CreateVoice implements CommandExecutor {
    /**
     * The stick item name
     */
    public static String ItemName = ChatColor.DARK_PURPLE + "AreaDefiner";
    /**
     * The name of the field use_count
     */
    public static NamespacedKey useCount;
    /**
     * The name of the x1 field
     */
    public static NamespacedKey x1;
    /**
     * The name of the z1 field
     */
    public static NamespacedKey z1;
    /**
     * The plugin logger
     */
    private Logger log;

    /**
     * Creates a new voice area
     * 
     * @param plug The minecraft plugin
     * @param log  The plugin logger
     */
    public CreateVoice(MinecraftPlugin plug, Logger log) {
        useCount = new NamespacedKey(plug, "use_count");
        x1 = new NamespacedKey(plug, "x1");
        z1 = new NamespacedKey(plug, "z1");
        this.log = log;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        if (args.length < 1)
            return false;
        if (label.equals("voicearea"))
            sender.sendMessage(
                    "Warning: This command is deprecated and will be removed in a future update. Use /createvoice instead.");
        var player = (Player) sender;
        var item = new ItemStack(Material.STICK, 1);
        var meta = item.getItemMeta();
        var channelName = String.join(" ", args);
        log.info(channelName);
        meta.setDisplayName(ItemName + " " + channelName);
        meta.getPersistentDataContainer().set(useCount, PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        return true;
    }
}
