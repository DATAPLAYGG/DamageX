package rx5600xt.sxqe.damagex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class Damage extends JavaPlugin implements Listener {

    private double damageMultiplier;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        loadConfig();
        getCommand("dmgset").setExecutor(this);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        damageMultiplier = config.getDouble("damageMultiplier", 1.33);
    }

    @Override
    public void onDisable() {
        getConfig().set("damageMultiplier", damageMultiplier);
        saveConfig();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.isCritical()) {
            Player player = (Player) event.getDamager();

            double originalDamage = event.getDamage();
            double newDamage = originalDamage * damageMultiplier;
            event.setDamage(newDamage);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("dmgset")) {
            if (sender instanceof Player && sender.hasPermission("Damagex.set") && args.length > 0) {
                try {
                    double newMultiplier = Double.parseDouble(args[0]);
                    damageMultiplier = newMultiplier;
                    sender.sendMessage("伤害倍率已设置为: " + newMultiplier);
                } catch (NumberFormatException e) {
                    sender.sendMessage("无效的输入。请提供一个有效的数字。");
                }
                return true;
            } else {
                sender.sendMessage("你没有权限执行这个命令或者输入了无效的参数。");
                return true;
            }
        }
        return false;
    }
}
