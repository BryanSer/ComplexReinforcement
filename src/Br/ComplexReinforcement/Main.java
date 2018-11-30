/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement;

import Br.API.Utils;
import Br.ComplexReinforcement.Attributes.AttributesManager;
import Br.ComplexReinforcement.Equipments.Effect;
import Br.ComplexReinforcement.Equipments.Equipment;
import Br.ComplexReinforcement.Equipments.Equipment.EquipType;
import Br.ComplexReinforcement.Equipments.EquipmentManager;
import Br.ComplexReinforcement.Equipments.Item;
import Br.ComplexReinforcement.Skills.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件分三部分<br>
 * [1] 属性<br>
 * [2] 技能<br>
 * [3] 套装<br>
 *
 * @author Bryan_lzh
 */
public class Main extends JavaPlugin {

    public static Main Plugin;

    @Override
    public void onEnable() {
        Plugin = this;
        Logs.init();
        ConfigurationSerialization.registerClass(Effect.class);
        ConfigurationSerialization.registerClass(Item.class);
        Logs.Log("ConfigurationSerializable注册完成");
        EquipmentManager.init();
        SkillManager.init();
        AttributesManager.init();
        this.ComplexReinforcementEquipment = Bukkit.getPluginCommand("ComplexReinforcementEquipment");
        Logs.Log("插件加载完成");

    }

    private Command ComplexReinforcementEquipment;

    @Override
    public void onDisable() {
        Logs.Save();
        Equipment.saveAll();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command != ComplexReinforcementEquipment) {
            return false;
        }
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage("§c参数不足 请输入/" + label + " help 查看帮助");
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            Equipment eq = Equipment.createEquipment(args[1]);
            if (eq == null) {
                sender.sendMessage("§c已有同名装备");
                return true;
            }
            sender.sendMessage("§6创建成功");
            return true;
        }
        Equipment eq = Equipment.getEquipment(args[1]);
        if (eq == null) {
            sender.sendMessage("§c找不到这个套装");
            return true;
        }
        if (args[0].equalsIgnoreCase("effect") && args.length == 5) {
            int level = 1;
            try {
                level = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c等级填写错误");
                return true;
            }
            Effect eff = eq.getEffects()[level - 1];
            if (eff == null) {
                eff = new Effect();
                eq.getEffects()[level - 1] = eff;
            }
            String save = null;
            if (args[3].equalsIgnoreCase("attr")) {
                save = "Attributes:" + args[4].replaceAll("_", " ");
            } else if (args[3].equalsIgnoreCase("skill")) {
                save = "Skill:" + args[4].replaceAll("_", " ");
            }
            if (save == null) {
                sender.sendMessage("§c效果类型错误");
                return true;
            }
            eff.getRawEffects().add(save);
            eff.Analyze();
            sender.sendMessage("§6设置成功");
            return true;
        }
        if (args[0].equalsIgnoreCase("set") && args.length >= 3 && (sender instanceof Player)) {
            Player p = (Player) sender;
            if (!Utils.hasItemInMainHand(p)) {
                p.sendMessage("§c你的手上毛都没有");
                return true;
            }
            EquipType et = EquipType.getEquipType(args[2]);
            ItemStack is = null;
            try {
                is = p.getInventory().getItemInMainHand();
            } catch (Throwable t) {
                is = p.getItemInHand();
            }
            Item i = new Item(is);
            i.setEquipmentName(eq.getName());
            eq.getItems().put(et, i);
            sender.sendMessage("§c设置成功");
            return true;
        }
        if (args[0].equalsIgnoreCase("get")) {
            Player p = (Player) sender;
            for (EquipType et : EquipType.values()) {
                Utils.safeGiveItem(p, eq.getItems().get(et).toItemStack());
            }
            p.sendMessage("§6你收到了多个套装装备");
            return true;
        }
        if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            Player target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                sender.sendMessage("§c找不到玩家");
                return true;
            }
            if (args.length > 3) {
                EquipType et = EquipType.getEquipType(args[3]);
                if (et == null) {
                    sender.sendMessage("§c部位代号出错");
                    return true;
                }
                Utils.safeGiveItem(target, eq.getItems().get(et).toItemStack());
                target.sendMessage("§6你收到了一个套装装备");
                return true;
            } else {
                for (EquipType et : EquipType.values()) {
                    Utils.safeGiveItem(target, eq.getItems().get(et).toItemStack());
                }
                target.sendMessage("§6你收到了多个套装装备");
                return true;
            }
        }
        return false;
    }

}
