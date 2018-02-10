/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 * @param <V> 可以为Integer或Double 其他类型将强制用Double运算
 */
public class BaseAttribute<V extends Number> extends Attribute<V> {

    @FunctionalInterface
    public interface AttributeFunction<V> {

        /**
         * 若 Type == Passive t damage将为null和0
         *
         * @param p 触发事件的玩家
         * @param t 触发事件的另外一个实体
         * @param v 属性参数
         * @param damage 事件伤害
         * @return 更新后的事件伤害
         */
        double onCall(Player p, LivingEntity t, Value<V> v, double damage);
    }

    AttributeFunction Function;

    public BaseAttribute(Plugin p, String name, Attribute.Type t, AttributeFunction<V> func, String... des) {
        super(p, name, AttributeType.Base, t, des);
        this.Function = func;
    }

    @Override
    public Value<V> AnalyzeLore(String s, Value<V> v) {
        v = v == null ? new Value<>() : v;
        v.setValue(BaseAttribute.add(v.getValue(), s));
        return v;
    }

    @Override
    double onCall(Player p, LivingEntity t, Value<V> s, double damage) {
        return this.Function.onCall(p, t, s, damage);
    }

    private static <N extends Number> N add(N n, String s) {
        if (n == null) {
            boolean div = false;
            if (s.contains("%")) {
                div = true;
            }
            if (s.matches("(.?)+[^0-9.]")) {
                s = s.replaceAll("[^0-9.]", "");
            }
            Double d = Double.parseDouble(s) / (div ? 100d : 1);
            return (N) d;
        }
        if (n instanceof Integer) {
            s = s.replaceAll("[^0-9.]", "").replaceAll("\\.(.+)", "");
            Integer i = n.intValue() + Integer.parseInt(s);
            return (N) i;
        }
        if (s.matches("(.?)+[^0-9.]")) {
            boolean div = false;
            if (s.contains("%")) {
                div = true;
            }
            s = s.replaceAll("[^0-9.]", "");
            Double d = n.doubleValue() + Double.parseDouble(s) / 100d;
            return (N) d;
        }
        Double d = n.doubleValue() + Double.parseDouble(s);
        return (N) d;
    }

}
