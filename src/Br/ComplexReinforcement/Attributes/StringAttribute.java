/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import Br.ComplexReinforcement.Attributes.BaseAttribute.AttributeFunction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 */
public class StringAttribute extends AdvancedAttribute<String> {

    private AttributeFunction<String> Function;

    public StringAttribute(Plugin p, String name, Attribute.Type t, AttributeFunction<String> f, String... des) {
        super(p, name, t, des);
        this.Function = f;
    }

    @Override
    public Value<String> AnalyzeLore(String s, Value<String> v) {
        v.setValue(s);
        return v;
    }

    @Override
    public double onCall(Player p, LivingEntity t, Value<String> s, double damage) {
        return Function.onCall(p, t, s, damage);
    }

}
