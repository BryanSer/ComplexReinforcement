/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import Br.API.Utils;
import Br.ComplexReinforcement.Logs;
import Br.ComplexReinforcement.Main;
import Br.ComplexReinforcement.Skills.RuntimeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 */
public class AttributesManager implements Listener {

    private static Map<String, Attribute> Attributes = new HashMap<>();

    public static Collection<Attribute> getAttributes() {
        return Collections.unmodifiableCollection(Attributes.values());
    }

    public static Attribute getAttribute(String name) {
        return Attributes.get(name);
    }

    public static Map<String, Integer> EventPrioritys = new HashMap<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new AttributesManager(), Main.Plugin);
        Logs.Log("AttributesManager >> 初始化 >> 完成");
    }

    public static Collection<String> getKeys() {
        return Attributes.keySet();
    }

    public static void RegisterAttribute(Attribute<?> attr, int ep) {
        if (ep > 10) {
            ep = 10;
        }
        if (ep < 0) {
            ep = 0;
        }
        Attributes.put(attr.getName(), attr);
        EventPrioritys.put(attr.getName(), ep);
        Logs.Log("AttributesManager >> 注册 >> " + attr.toString());
    }

    @EventHandler
    public void onRun(RuntimeEvent evt) {
        for (Player p : Utils.getOnlinePlayers()) {
            AttributeEvent AE = new AttributeEvent(p, Attribute.Type.Passive);
            Bukkit.getPluginManager().callEvent(AE);
            if (!AE.isCancelled()) {
                this.LoadToMemory(p);
                Map<String, Attribute.Value<? extends Number>> Bases = BasesCache.get(p.getName());
                Map<String, List<Attribute.Value>> Advanceds = AdvancedsCache.get(p.getName());
                BasesCaster.Cast(Bases, -1, p, null, null, Attribute.Type.Passive);
                BasesCaster.Cast(AE.getBases(), -1, p, null, null, Attribute.Type.Passive);//Lambda代替
                AdvancedsCaster.Cast(Advanceds, -1, p, null, null, Attribute.Type.Passive);
                AdvancedsCaster.Cast(AE.getAdvanceds(), -1, p, null, null, Attribute.Type.Passive);
            }
        }
    }

    @FunctionalInterface
    private interface Consumer<M, EVT> {

        void Cast(M m, int ep, Player p, LivingEntity e, EVT evt, Attribute.Type t);
    }

    private final Consumer<Map<String, Attribute.Value<? extends Number>>, EntityDamageByEntityEvent> BasesCaster = (Bases, ep, p, e, evt, t)
            -> Bases.entrySet().stream().filter((E)
                    -> (ep == EventPrioritys.get(E.getKey())) || ep == -1)
                    .forEachOrdered((E)
                            -> {
                        Attribute a = Attributes.get(E.getKey());
                        if (a.getType() == t) {
                            if (evt == null) {
                                a.onCall(p, e, E.getValue(), 0);
                            } else {
                                evt.setDamage(a.onCall(p, e, E.getValue(), evt.getDamage()));
                            }
                        }
                    });

    private final Consumer<Map<String, List<Attribute.Value>>, EntityDamageByEntityEvent> AdvancedsCaster = (Advanceds, ep, p, e, evt, t)
            -> Advanceds.entrySet().stream().filter((E)
                    -> (ep == EventPrioritys.get(E.getKey())) || ep == -1)
                    .forEachOrdered((E)
                            -> {
                        Attribute a = Attributes.get(E.getKey());
                        if (a.getType() == t) {
                            if (evt == null) {
                                E.getValue().forEach((av) -> {
                                    a.onCall(p, e, av, 0);
                                });
                            } else {
                                E.getValue().forEach((av) -> {
                                    evt.setDamage(a.onCall(p, e, av, evt.getDamage()));
                                });
                            }
                        }
                    });

    private void Handle(EntityDamageByEntityEvent evt) {
        if (!(evt.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity e = (LivingEntity) evt.getEntity();
        LivingEntity d = null;
        if (evt.getDamager() instanceof LivingEntity) {
            d = (LivingEntity) evt.getDamager();
        } else if (evt.getDamager() instanceof Projectile) {
            Projectile p = (Projectile) evt.getDamager();
            if (p.getShooter() instanceof Player) {
                d = (LivingEntity) p.getShooter();
            }
        }
        if ((d != null) && (d instanceof Player)) {
            Player p = (Player) d;
            AttributeEvent AE = new AttributeEvent(p, Attribute.Type.Attack);
            Bukkit.getPluginManager().callEvent(AE);
            if (!AE.isCancelled()) {
                this.LoadToMemory(p);
                Map<String, Attribute.Value<? extends Number>> Bases = BasesCache.get(p.getName());
                Map<String, List<Attribute.Value>> Advanceds = AdvancedsCache.get(p.getName());
                Bases = AE.Combine(Bases);
                for (int i = 0; i <= 10; i++) {
                    BasesCaster.Cast(Bases, i, p, e, evt, Attribute.Type.Attack);
                    AdvancedsCaster.Cast(Advanceds, i, p, e, evt, Attribute.Type.Attack);
                    AdvancedsCaster.Cast(AE.getAdvanceds(), i, p, e, evt, Attribute.Type.Attack);
                }
                EndOfAttackEvent EOAE = new EndOfAttackEvent(p);
                Bukkit.getPluginManager().callEvent(EOAE);
            }
        }
        if (e instanceof Player) {
            Player p = (Player) e;
            AttributeEvent AE = new AttributeEvent(p, Attribute.Type.Defense);
            Bukkit.getPluginManager().callEvent(AE);
            if (!AE.isCancelled()) {
                this.LoadToMemory(p);
                Map<String, Attribute.Value<? extends Number>> Bases = BasesCache.get(p.getName());
                Map<String, List<Attribute.Value>> Advanceds = AdvancedsCache.get(p.getName());
                Bases = AE.Combine(Bases);
                for (int i = 0; i <= 10; i++) {
                    BasesCaster.Cast(Bases, i, p, d, evt, Attribute.Type.Defense);
                    AdvancedsCaster.Cast(Advanceds, i, p, d, evt, Attribute.Type.Defense);
                    AdvancedsCaster.Cast(AE.getAdvanceds(), i, p, e, evt, Attribute.Type.Defense);
                }
                EndOfDefenceEvent EODE = new EndOfDefenceEvent(p);
                Bukkit.getPluginManager().callEvent(EODE);
            }
        }

    }

    public static Map<String, Long> CacheTime = new HashMap<>();
    public static Map<String, Map<String, Attribute.Value<? extends Number>>> BasesCache = new HashMap<>();
    public static Map<String, Map<String, List<Attribute.Value>>> AdvancedsCache = new HashMap<>();

    private void LoadToMemory(Player p) {
        Map<String, Attribute.Value<? extends Number>> Bases = null;
        Map<String, List<Attribute.Value>> Advanceds = null;
        if (CacheTime.containsKey(p.getName())) {
            long last = CacheTime.get(p.getName());
            if (System.currentTimeMillis() - last < 2000L) {
                return;
            }
        }
        Bases = new HashMap<>();
        Advanceds = new HashMap<>();
        Outter:
        for (String s : getAllLore(p)) {
            s = s.replaceAll("§(.)", "");
            for (String key : Attributes.keySet()) {
                if (s.contains(key)) {
                    Attribute a = Attributes.get(key);
                    try {
                        s = s.replaceFirst(key, "");
                        if (a.getAttrType() == AttributeType.Base) {
                            Attribute.Value v = null;
                            if (Bases.containsKey(a.getName())) {
                                v = Bases.get(a.getName());
                            } else {
                                v = new Attribute.Value<>();
                            }
                            v = a.AnalyzeLore(s, v);
                            Bases.put(a.getName(), v);
                        } else {
                            List<Attribute.Value> values;
                            if (Advanceds.containsKey(a.getName())) {
                                values = Advanceds.get(a.getName());
                            } else {
                                values = new ArrayList<>();
                            }
                            Attribute.Value v = a.AnalyzeLore(s, new Attribute.Value<>());
                            values.add(v);
                            Advanceds.put(a.getName(), values);
                        }
                    } catch (Throwable e) {
                        Logs.Log(e);
                    }
                    continue Outter;
                }
            }
        }
        CacheTime.put(p.getName(), System.currentTimeMillis());
        BasesCache.put(p.getName(), Bases);
        AdvancedsCache.put(p.getName(), Advanceds);
    }

    private List<String> getAllLore(Player p) {
        List<String> lores = new ArrayList<>(50);
        EntityEquipment eq = p.getEquipment();
        for (ItemStack is : eq.getArmorContents()) {
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                int level = getLevelLimit(is.getItemMeta().getLore());
                if (p.getLevel() >= level) {
                    lores.addAll(is.getItemMeta().getLore());
                }
            }
        }
        try {
            ItemStack is = eq.getItemInOffHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                int level = getLevelLimit(is.getItemMeta().getLore());
                if (p.getLevel() >= level) {
                    lores.addAll(is.getItemMeta().getLore());
                }
            }
            is = eq.getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                int level = getLevelLimit(is.getItemMeta().getLore());
                if (p.getLevel() >= level) {
                    lores.addAll(is.getItemMeta().getLore());
                }
            }
        } catch (Throwable t) {
            ItemStack is = p.getItemInHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                int level = getLevelLimit(is.getItemMeta().getLore());
                if (p.getLevel() >= level) {
                    lores.addAll(is.getItemMeta().getLore());
                }
            }
        }
        return lores;
    }

    public int getLevelLimit(List<String> lore) {
        for (String s : lore) {
            s = ChatColor.stripColor(s);
            if (s.startsWith("等级限制: ")) {
                s = s.replaceAll("[^0-9]", "");
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage_NORMAL(EntityDamageByEntityEvent evt) {
        Logs.Log("Event Damage_Before: " + evt.getDamage());
        this.Handle(evt);
        Logs.Log("Event Damage_After: " + evt.getDamage());
    }
}
