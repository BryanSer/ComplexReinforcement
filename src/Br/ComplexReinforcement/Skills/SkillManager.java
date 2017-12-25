/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import Br.API.Utils;
import Br.ComplexReinforcement.Logs;
import Br.ComplexReinforcement.Main;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 */
public class SkillManager implements Listener {

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new SkillManager(), Main.Plugin);
        Bukkit.getScheduler().runTaskTimer(Main.Plugin, () -> Bukkit.getPluginManager().callEvent(new RuntimeEvent()), 20, 20);
        Bukkit.getPluginManager().registerEvents(new SkillManager(), Main.Plugin);
        Logs.Log("SkillManager >> 初始化 >> 完成");
    }

    public static <A extends Argments, E extends Event> void RegisterSkill(Skill<A, E> s) {
        s.getType().getSkills().put(s.getName(), s);
        Logs.Log("SkillManager >> 注册 >> " + s.toString());
    }

    @EventHandler
    public void onRunTime(RuntimeEvent evt) {
        for (Player p : Utils.getOnlinePlayers()) {
            PremanentSkillEvent PSE = new PremanentSkillEvent(p);
            Bukkit.getPluginManager().callEvent(PSE);
            if (PSE.isCancelled()) {
                continue;
            }
            for (SkillEvent.SkillData<RuntimeEvent> sd : PSE.getSkillDatas()) {
                this.CastEvent(p, sd.getSkill(), sd.getArgments(), evt);
            }
            EntityEquipment e = p.getEquipment();
            for (ItemStack is : e.getArmorContents()) {
                CastItem(is, p, SkillType.Premanent, evt);
            }
            try {
                CastItem(e.getItemInOffHand(), p, SkillType.Premanent, evt);
                CastItem(e.getItemInMainHand(), p, SkillType.Premanent, evt);
            } catch (Throwable t) {
                CastItem(p.getItemInHand(), p, SkillType.Premanent, evt);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if (evt.getEntity() instanceof Player) {
            Player p = (Player) evt.getEntity();
            PassiveSkillEvent CPSE = new PassiveSkillEvent(p, SkillType.PassiveDEF);
            Bukkit.getPluginManager().callEvent(CPSE);
            if (CPSE.isCancelled()) {
                return;
            }
            CPSE.getSkillDatas().forEach((sd) -> {
                this.CastEvent(p, sd.getSkill(), sd.getArgments(), evt);
            });
            EntityEquipment e = p.getEquipment();
            for (ItemStack is : e.getArmorContents()) {
                CastItem(is, p, SkillType.PassiveDEF, evt);
            }
            try {
                CastItem(e.getItemInOffHand(), p, SkillType.PassiveDEF, evt);
                CastItem(e.getItemInMainHand(), p, SkillType.PassiveDEF, evt);
            } catch (Throwable t) {
                CastItem(p.getItemInHand(), p, SkillType.PassiveDEF, evt);
            }
        }
        if(evt.getDamager() instanceof Player){
            Player p = (Player) evt.getDamager();
            PassiveSkillEvent CPSE = new PassiveSkillEvent(p, SkillType.PassiveATK);
            Bukkit.getPluginManager().callEvent(CPSE);
            if (CPSE.isCancelled()) {
                return;
            }
            CPSE.getSkillDatas().forEach((sd) -> {
                this.CastEvent(p, sd.getSkill(), sd.getArgments(), evt);
            });
            EntityEquipment e = p.getEquipment();
            for (ItemStack is : e.getArmorContents()) {
                CastItem(is, p, SkillType.PassiveATK, evt);
            }
            try {
                CastItem(e.getItemInOffHand(), p, SkillType.PassiveATK, evt);
                CastItem(e.getItemInMainHand(), p, SkillType.PassiveATK, evt);
            } catch (Throwable t) {
                CastItem(p.getItemInHand(), p, SkillType.PassiveATK, evt);
            }
            
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent evt) {
        Player p = evt.getPlayer();
        if (!Utils.hasItemInMainHand(p) || Utils.hasItemInOffHand(p)) {
            return;
        }
        InitiativeSkillEvent ISE = new InitiativeSkillEvent(p);
        Bukkit.getPluginManager().callEvent(ISE);
        if (ISE.isCancelled()) {
            return;
        }
        for (SkillEvent.SkillData<PlayerInteractEvent> sd : ISE.getSkillDatas()) {
            this.CastEvent(p, sd.getSkill(), sd.getArgments(), evt);
        }
        ItemStack is = p.getItemInHand();
        if (!is.hasItemMeta() || !is.getItemMeta().hasLore()) {
            return;
        }
        CastItem(is, evt.getPlayer(), SkillType.Initiative, evt);
    }

    public <E extends Event> void CastItem(ItemStack is, Player p, SkillType<E> st, E evt) {
        if (is == null) {
            return;
        }
        if(!is.hasItemMeta() || !is.getItemMeta().hasLore()){
            return;
        }
        Map<String, Skill<?, E>> map = st.getSkills();
        Outter:
        for (String s : is.getItemMeta().getLore()) {
            if (s.contains("（")) {
                s = s.replaceAll("（", "(").replaceAll("）", ")");
            }
            if (s.contains("(")) {
                for (String key : map.keySet()) {
                    if (s.startsWith(key)) {
                        CastEvent(p, map.get(key), s, evt);
                        continue Outter;
                    }
                }
            }
        }
    }

    public <E extends Event> void CastEvent(Player p, Skill<?, E> ski, String r, E evt) {
        r = ChatColor.stripColor(r);
        r = r.split("\\(")[1].replaceAll("\\)", "");
        ski.Cast(p, r, evt);
    }

}
