/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Bryan_lzh
 */
public class PremanentSkillEvent extends SkillEvent<RuntimeEvent>{
    
    public PremanentSkillEvent(Player p) {
        super(p);
    }
    

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public SkillType<RuntimeEvent> getType() {
        return SkillType.Premanent;
    }
}
