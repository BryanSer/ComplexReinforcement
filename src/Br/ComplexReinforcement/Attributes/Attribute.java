/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import Br.ComplexReinforcement.Logs;
import java.util.Arrays;
import java.util.Objects;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 */
public abstract class Attribute<V> {

    public enum Type {
        Passive, Attack, Defense;
    }

    private final Plugin Plugin;
    private final String Name;
    private final String[] Descriptions;
    private final AttributeType AttrType;
    private final Type Type;

    public static class Value<V> {

        private V Value;

        public V getValue() {
            return Value;
        }

        public void setValue(V v) {
            this.Value = v;
        }

        public void add(Number v) {
            try {
                if (v instanceof Integer) {
                    Integer integer = (Integer) v + (Integer) Value;
                    Value = (V) integer;
                }
                Double d = (Double) v + (Double) Value;
                Value = (V) d;
            } catch (Throwable t) {
                Logs.Log(t);
            }
        }

        @Override
        public String toString() {
            return "Value{" + "Value=" + Value + '}';
        }
    }

    protected Attribute(Plugin p, String name, AttributeType t, Type tp, String... des) {
        this.Plugin = p;
        this.Name = name;
        this.Descriptions = des;
        this.AttrType = t;
        this.Type = tp;
    }

    public Plugin getPlugin() {
        return Plugin;
    }

    public AttributeType getAttrType() {
        return AttrType;
    }

    public String getName() {
        return Name;
    }

    public String[] getDescriptions() {
        return Descriptions;
    }

    public Type getType() {
        return Type;
    }

    public abstract Value<V> AnalyzeLore(String s, Value<V> v);

    /**
     * 若 Type == Passive t damage将为null和0
     *
     * @param p 触发事件的玩家
     * @param t 触发事件的另外一个实体
     * @param s 属性参数
     * @param damage 事件伤害
     * @return 更新后的事件伤害
     */
    abstract double onCall(Player p, LivingEntity t, Value<V> s, double damage);

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.Plugin);
        hash = 37 * hash + Objects.hashCode(this.Name);
        hash = 37 * hash + Arrays.deepHashCode(this.Descriptions);
        hash = 37 * hash + Objects.hashCode(this.AttrType);
        hash = 37 * hash + Objects.hashCode(this.Type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attribute<?> other = (Attribute<?>) obj;
        if (!Objects.equals(this.Name, other.Name)) {
            return false;
        }
        if (!Objects.equals(this.Plugin, other.Plugin)) {
            return false;
        }
        if (!Arrays.deepEquals(this.Descriptions, other.Descriptions)) {
            return false;
        }
        if (this.AttrType != other.AttrType) {
            return false;
        }
        if (this.Type != other.Type) {
            return false;
        }
        return true;
    }

}
