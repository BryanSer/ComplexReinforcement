/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Equipments;

import Br.API.Data.BrConfigurationSerializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Bryan_lzh
 */
public class Item implements BrConfigurationSerializable {

    @Config
    private Material Material;
    @Config
    private short Durability;
    @Config
    private String DisplayName;
    @Config
    private List<String> Lore;
    @Config
    private List<String> ItemFlagList;
    private String EquipmentName;
    private ItemFlag[] ItemFlags = new ItemFlag[0];

    public Item(Map<String, Object> args) {
        BrConfigurationSerializable.deserialize(args, this);
        if (Lore == null) {
            Lore = new ArrayList<>(1);
        }
        Set<ItemFlag> IF = new HashSet<>();
        this.ItemFlagList.forEach((s) -> IF.add(ItemFlag.valueOf(s)));
        ItemFlags = IF.toArray(ItemFlags);
    }

    public Material getMaterial() {
        return Material;
    }

    public short getDurability() {
        return Durability;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public List<String> getLore() {
        return Lore;
    }

    public ItemFlag[] getItemFlags() {
        return ItemFlags;
    }

    public void setMaterial(Material Material) {
        this.Material = Material;
    }

    public void setDamage(short Damage) {
        this.Durability = Damage;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public void setLore(List<String> Lore) {
        this.Lore = Lore;
    }

    public void SortItemFlags() {
        Set<ItemFlag> IF = new HashSet<>();
        this.ItemFlagList.forEach((s) -> IF.add(ItemFlag.valueOf(s)));
        ItemFlags = IF.toArray(ItemFlags);
    }

    public ItemStack toItemStack() {
        ItemStack is = new ItemStack(this.Durability, 1, this.Durability);
        ItemMeta im = is.getItemMeta();
        if (this.DisplayName != null) {
            im.setDisplayName(this.DisplayName);
        }
        List<String> lore = new ArrayList<>(this.Lore.size() + 1);
        lore.add(this.toCode());
        if (this.Lore != null && !this.Lore.isEmpty()) {
            lore.addAll(this.Lore);
        }
        im.setLore(lore);
        im.addItemFlags(this.ItemFlags);
        is.setItemMeta(im);
        return is;
    }

    public boolean isSame(ItemStack is) {
        if (is.getType() != this.getMaterial() || is.getDurability() != this.getDurability()) {
            return false;
        }
        if (!is.hasItemMeta() || (!is.getItemMeta().hasDisplayName() && this.getDisplayName() != null) || !this.getDisplayName().equals(is.getItemMeta().getDisplayName())) {
            return false;
        }
        if (!is.getItemMeta().hasLore()) {
            return false;
        }
        List<String> lore = is.getItemMeta().getLore();
        return lore.size() < 1 ? false : ChatColor.stripColor(lore.get(0)).equals(this.getEquipmentName());
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String EquipmentName) {
        this.EquipmentName = EquipmentName;
    }

    public String toCode() {
        char c[] = this.getEquipmentName().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char d : c) {
            sb.append('§').append(d);
        }
        return sb.toString();
    }
}
