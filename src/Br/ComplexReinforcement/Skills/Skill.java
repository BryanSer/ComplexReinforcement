/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 * @param <A>
 * @param <E>
 */
public class Skill<A extends Argments, E extends Event> {

    private final Plugin plugin;
    private final Class<A> ArgmentsClass;
    private final String Name;
    private final BiConsumer<A, Player> func;
    private final String[] Descriptions;
    private final SkillType<E> Type;

    public Skill(Plugin p, Class<A> cls, String name, SkillType<E> t, BiConsumer<A, Player> f) {
        this(p, cls, name, t, f, new String[0]);
    }

    public Skill(Plugin p, Class<A> cls, String name, SkillType<E> t, BiConsumer<A, Player> f, String... des) {
        this.plugin = p;
        this.ArgmentsClass = cls;
        this.Name = name;
        this.func = f;
        this.Descriptions = des;
        this.Type = t;
    }

    public void Cast(Player p, String s, E evt) {
        A result = Argments.AnalyzeLore(s, p, ArgmentsClass, this, evt);
        if (!result.canCast()) {
            p.sendMessage(result.cantCastMsg());
        } else {
            result.onCast();
            this.func.accept(result, p);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Class<A> getArgmentsClass() {
        return ArgmentsClass;
    }

    public String getName() {
        return Name;
    }

    public BiConsumer<A, Player> getFunc() {
        return func;
    }

    public String[] getDescriptions() {
        return Descriptions;
    }

    public SkillType<E> getType() {
        return Type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.plugin);
        hash = 71 * hash + Objects.hashCode(this.ArgmentsClass);
        hash = 71 * hash + Objects.hashCode(this.Name);
        hash = 71 * hash + Objects.hashCode(this.func);
        hash = 71 * hash + Arrays.deepHashCode(this.Descriptions);
        hash = 71 * hash + Objects.hashCode(this.Type);
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
        final Skill<?, ?> other = (Skill<?, ?>) obj;
        if (!Objects.equals(this.Name, other.Name)) {
            return false;
        }
        if (!Objects.equals(this.plugin, other.plugin)) {
            return false;
        }
        if (!Objects.equals(this.ArgmentsClass, other.ArgmentsClass)) {
            return false;
        }
        if (!Objects.equals(this.func, other.func)) {
            return false;
        }
        if (!Arrays.deepEquals(this.Descriptions, other.Descriptions)) {
            return false;
        }
        if (!Objects.equals(this.Type, other.Type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Skill{" + "plugin=" + plugin + ", ArgmentsClass=" + ArgmentsClass + ", Name=" + Name + ", func=" + func + ", Descriptions=" + Arrays.toString(Descriptions) + ", Type=" + Type + '}';
    }

}
