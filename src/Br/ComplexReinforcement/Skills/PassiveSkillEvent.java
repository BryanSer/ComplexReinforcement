/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 *
 * @author Bryan_lzh
 */
public class PassiveSkillEvent extends SkillEvent<EntityDamageByEntityEvent>{
    private SkillType<EntityDamageByEntityEvent> t;
    
    public PassiveSkillEvent(Player p,SkillType<EntityDamageByEntityEvent> t){
        super(p);
        this.t = t;
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
    public SkillType<EntityDamageByEntityEvent> getType() {
        return t;
    }
    
}
