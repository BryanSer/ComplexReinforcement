/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Bryan_lzh
 * @param <E>
 */
public class SkillType<E extends Event> {

    public static final SkillType<PlayerInteractEvent> Initiative = new SkillType<>();
    public static final SkillType<EntityDamageByEntityEvent> PassiveDEF = new SkillType<>();
    public static final SkillType<EntityDamageByEntityEvent> PassiveATK = new SkillType<>();
    public static final SkillType<RuntimeEvent> Premanent = new SkillType<>();
    private final Map<String, Skill<?, E>> Skills = new HashMap<>();

    private SkillType() {
    }

    public Map<String, Skill<?, E>> getSkills() {
        return Skills;
    }

}
