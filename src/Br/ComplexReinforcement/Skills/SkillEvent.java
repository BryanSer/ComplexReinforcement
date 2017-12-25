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

/**
 *
 * @author Bryan_lzh
 * @param <E>
 */
public abstract class SkillEvent<E extends Event> extends Event implements Cancellable{

    public static class SkillData<E extends Event> {

        private Skill<?, E> ski;
        private String s;

        public SkillData(Skill<?, E> ski, String s) {
            this.ski = ski;
            this.s = s;
        }

        public Skill<?, E> getSkill() {
            return this.ski;
        }

        public String getArgments() {
            return this.s;
        }
    }

    public SkillEvent(Player p) {
        this.player = p;
    }

    private Player player;
    private List<SkillData<E>> SkillDatas = new ArrayList<>(5);

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public List<SkillData<E>> getSkillDatas() {
        return SkillDatas;
    }
    
    private boolean cancel = false;
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    public abstract SkillType<E> getType();

}
