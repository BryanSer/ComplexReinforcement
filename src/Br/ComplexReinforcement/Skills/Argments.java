/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import Br.ComplexReinforcement.Logs;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 *
 * @author Bryan_lzh
 */
public abstract class Argments {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Argment {

        String Key();
    }
    protected Event evt;

    protected Player player;

    public static <T extends Argments, E extends Event> T AnalyzeLore(String r, Player p, Class<T> cls, Skill<T, E> ski, E evt) {
        try {
            String s[] = r.contains(" ") ? r.split(" ") : new String[]{r};
            T t = cls.newInstance();
            t.player = p;
            t.evt = evt;
            System.out.println("r: " + r);
            for (Field f : getAllField(cls)) {
                f.setAccessible(true);
                Argment a = f.getAnnotation(Argment.class);
                for (String str : s) {
                    if (str.startsWith(a.Key())) {
                        str = str.replaceFirst(a.Key() + ":", "");
                        System.out.println("set " + a.Key() + " " + str);
                        setValue(f, t, str);
                        break;
                    }
                }
            }
            return t;
        } catch (Throwable ex) {
            Logs.Log(ex);
        }
        return null;
    }

    public static List<Field> getAllField(Class<? extends Argments> cls) {
        List<Field> result = new ArrayList<>();
        Class<?> c = cls;
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(Argment.class)) {
                    result.add(f);
                }
            }
            c = c.getSuperclass();
        }
        return result;
    }

    private static void setValue(Field f, Object obj, String s) throws IllegalArgumentException, IllegalAccessException {
        Class<?> cls = f.getType();
        if (cls == int.class || cls == Integer.class) {
            s = s.replaceAll("[^0-9.]", "").replaceAll("\\.(.+)", "");
            f.set(obj, Integer.parseInt(s));
        } else if (cls == String.class) {
            f.set(obj, s);
        } else if (cls == float.class || cls == Float.class) {
            s = s.replaceAll("[^0-9.]", "");
            f.set(obj, Float.parseFloat(s));
        } else if (cls == double.class || cls == Double.class) {
            s = s.replaceAll("[^0-9.]", "");
            f.set(obj, Double.parseDouble(s));
        } else if (cls == short.class || cls == Short.class) {
            s = s.replaceAll("[^0-9.]", "").replaceAll("\\.(.+)", "");
            f.set(obj, Short.parseShort(s));
        } else if (cls == char.class || cls == Character.class) {
            f.set(obj, s.charAt(0));
        } else if (cls == byte.class || cls == Byte.class) {
            s = s.replaceAll("[^0-9.]", "").replaceAll("\\.(.+)", "");
            f.set(obj, Byte.parseByte(s));
        }
    }

    public <E extends Event> E getEvent(SkillType<E> e) {
        return (E) this.evt;
    }

    /**
     * 该玩家是否能够使用此技能
     *
     * @return 该玩家是否能够使用此技能
     */
    public abstract boolean canCast();

    /**
     * 不能释放的时候显示给玩家的信息
     *
     * @return 不能释放的时候显示给玩家的信息
     */
    public abstract String cantCastMsg();

    /**
     * 是否技能时调用本方法(无需在这里写技能内容 只需要写和参数有关的操作)
     */
    public abstract void onCast();

    /**
     * @return 返回格式化后的技能描述 如果需要在套装中使用的话必须覆盖
     */
    public String toString() {
        return super.toString();
    }
}
