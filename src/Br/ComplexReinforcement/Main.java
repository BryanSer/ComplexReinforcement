/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement;

import Br.ComplexReinforcement.Attributes.AttributesManager;
import Br.ComplexReinforcement.Equipments.Effect;
import Br.ComplexReinforcement.Equipments.EquipmentManager;
import Br.ComplexReinforcement.Equipments.Item;
import Br.ComplexReinforcement.Skills.SkillManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
        Logs.Log("插件加载完成");
    }

    @Override
    public void onDisable() {
        Logs.Save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }
    
    

}
