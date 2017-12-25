/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Bryan_lzh
 */
public class InitiativeSkillEvent extends SkillEvent<PlayerInteractEvent> {

    public InitiativeSkillEvent(Player p) {
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
    public SkillType<PlayerInteractEvent> getType() {
        return SkillType.Initiative;
    }

}
