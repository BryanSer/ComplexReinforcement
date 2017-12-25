/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Equipments;

import Br.ComplexReinforcement.Logs;
import Br.ComplexReinforcement.Main;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 */
public class Equipment {
    public static Map<String, Equipment> Equipments = new HashMap<>();
    
    

    public static void init() {
        File folder = new File(Main.Plugin.getDataFolder(), File.separator + "Equipments" + File.separator);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (File f : folder.listFiles()) {
            try {
                Equipment e = Equipment.Load(f);
                Equipments.put(e.getName(), e);
            } catch (Throwable t) {
                Logs.Log(t);
            }
        }
    }

    private Equipment() {
    }

    public enum EquipType {
        Helmet(EntityEquipment::getHelmet), Chestplate(EntityEquipment::getChestplate), Leggings(EntityEquipment::getLeggings), Boots(EntityEquipment::getBoots);
        private Function<EntityEquipment, ItemStack> fun = null;

        private EquipType(Function<EntityEquipment, ItemStack> fun) {
            this.fun = fun;
        }

        public Function<EntityEquipment, ItemStack> getFunction() {
            return fun;
        }

    }

    private Map<EquipType, Item> Items = new EnumMap<>(EquipType.class);
    private String Name;
    private final Effect[] Effects = new Effect[]{null, null, null, null};
    private boolean Completed = false;

    public boolean isCompleted() {
        return Completed;
    }
    
    

    public static Equipment Load(File f) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        Equipment eq = new Equipment();
        eq.Name = config.getString("Name");
        for (EquipType et : EquipType.values()) {
            if (config.contains("Items." + et.name())) {
                Item i = (Item) config.get("Items." + et.name());
                i.setEquipmentName(eq.getName());
                eq.Items.put(et, i);
            } else {
                Logs.Log("[警告] 配置文件中缺少装备信息 如果不是刻意为之 请检查配置文件\n\tat " + eq.Name + " in " + f.getAbsolutePath() + "\n\t\tat Items." + et.name());
            }
        }
        for (int i = 0; i < 4; i++) {
            if (config.contains("Effect." + i)) {
                eq.Effects[i] = (Effect) config.get("Effect." + i);
            }
        }
        eq.Completed = config.getBoolean("Completed");
        return eq;
    }

    public void Save() throws IOException {
        File folder = new File(Main.Plugin.getDataFolder(), File.separator + "Equipments" + File.separator);
        File f = new File(folder, this.Name + ".yml");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        config.set("Name", this.Name);
        for (EquipType eq : EquipType.values()) {
            config.set("Items." + eq.name(), Items.get(eq));
        }
        for (int i = 0; i < Effects.length; i++) {
            Effect eff = Effects[i];
            if (eff == null) {
                continue;
            }
            config.set("Effect." + i, eff);
        }
        config.set("Completed", Completed);
        config.save(f);
    }

    public static Map<String, Equipment> getEquipments() {
        return Equipments;
    }

    public Map<EquipType, Item> getItems() {
        return Items;
    }

    public String getName() {
        return Name;
    }

    public Effect[] getEffects() {
        return Effects;
    }

    public int getWeared(Player p) {
        int s = 0;
        EntityEquipment eq = p.getEquipment();
        for (EquipType et : EquipType.values()) {
            ItemStack is = et.getFunction().apply(eq);
            if (is == null || !this.Items.containsKey(et)) {
                continue;
            }
            if (this.Items.get(et).isSame(is)) {
                s++;
            }
        }
        return s;
    }
}
