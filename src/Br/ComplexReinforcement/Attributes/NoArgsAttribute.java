/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author Bryan_lzh
 */
public abstract class NoArgsAttribute extends Attribute<String>{

    public NoArgsAttribute(org.bukkit.plugin.Plugin p, String name, Type tp, String... des) {
        super(p, name, AttributeType.Advanced, tp, des);
    }

    @Override
    public Value<String> AnalyzeLore(String s, Value<String> v) {
        return null;
    }

    @Override
    double onCall(Player p, LivingEntity t, Value<String> s, double damage) {
        return this.onCall(p, t, damage);
    }
    
    public abstract double onCall(Player p, LivingEntity t,double d);
    
}
