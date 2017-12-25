/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Equipments;

import Br.ComplexReinforcement.Attributes.AttributeEvent;
import Br.ComplexReinforcement.Main;
import Br.ComplexReinforcement.Skills.InitiativeSkillEvent;
import Br.ComplexReinforcement.Skills.Skill;
import Br.ComplexReinforcement.Skills.SkillEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Bryan_lzh
 */
public class EquipmentManager implements Listener {

    public static void init() {
        Equipment.init();
        Bukkit.getPluginManager().registerEvents(new EquipmentManager(), Main.Plugin);
    }

    private static Map<String, Long> CacheTime = new HashMap<>();
    private static Map<String, List<Effect>> Cache = new HashMap<>();
    private static Map<String, Integer> LastHash = new HashMap<>();

    @EventHandler
    public void onSkill(SkillEvent evt) {
        for (Effect e : getEffect(evt.getPlayer())) {
            List<String> ski = e.getSkills();
            if (!ski.isEmpty()) {
                Map<String, Skill<?, ?>> map = evt.getType().getSkills();
                Outter:
                for (String s : ski) {
                    for (String key : map.keySet()) {
                        if (s.startsWith(key)) {
                            evt.getSkillDatas().add(new SkillEvent.SkillData<>(map.get(key), s));
                            continue Outter;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onAttr(AttributeEvent evt) {
        getEffect(evt.getPlayer()).forEach((e) -> {
            evt.getBases().putAll(evt.getBases());
            evt.getAdvanceds().putAll(e.getAdvanceds());
        });
    }

    public static List<Effect> getEffect(Player p) {

        if (CacheTime.containsKey(p.getName())) {
            long time = CacheTime.get(p.getName());
            if (System.currentTimeMillis() - time < 20000L) {
                return Cache.get(p.getName());
            }
        }
        List<Effect> effs = new ArrayList<>(4);
        List<String> msg = new ArrayList<>(4);
        Equipment.Equipments.values().stream().filter(Equipment::isCompleted).forEach((e) -> {
            int count = e.getWeared(p);
            if (count > 0) {
                if (e.getEffects()[count] != null) {
                    effs.add(e.getEffects()[count]);
                    msg.add(String.format("§e§l套装[%s] 已生效 套装效果等级: %d", e.getName(), count));
                }
            }
        });
        if (!LastHash.containsKey(p.getName())) {
            if (msg.isEmpty()) {
                p.sendMessage("§c你失去了全部的套装效果");
            } else {
                p.sendMessage(msg.toArray(new String[4]));
            }
            LastHash.put(p.getName(), effs.hashCode());
        } else if (!LastHash.get(p.getName()).equals(effs.hashCode())) {
            p.sendMessage(msg.toArray(new String[4]));
            LastHash.put(p.getName(), effs.hashCode());
        }
        CacheTime.put(p.getName(), System.currentTimeMillis());
        Cache.put(p.getName(), effs);
        return effs;
    }

    private EquipmentManager() {
    }

}
