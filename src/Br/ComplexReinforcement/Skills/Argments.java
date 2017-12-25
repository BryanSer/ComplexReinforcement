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
import org.bukkit.ChatColor;
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
            for (Field f : t.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Argment.class)) {
                    Argment a = f.getAnnotation(Argment.class);
                    for (String str : s) {
                        if (str.startsWith(a.Key())) {
                            str = str.replaceFirst(a.Key() + ":", "");
                            setValue(f, t, str);
                            break;
                        }
                    }
                }
            }
            return t;
        } catch (Throwable ex) {
            Logs.Log(ex);
        }
        return null;
    }

    private static void setValue(Field f, Object obj, String s) throws IllegalArgumentException, IllegalAccessException {
        Class<?> cls = f.getType();
        if (cls == int.class || cls == Integer.class) {
            f.set(obj, Integer.parseInt(s));
        } else if (cls == String.class) {
            f.set(obj, s);
        } else if (cls == float.class || cls == Float.class) {
            f.set(obj, Float.parseFloat(s));
        } else if (cls == double.class || cls == Double.class) {
            f.set(obj, Double.parseDouble(s));
        } else if (cls == short.class || cls == Short.class) {
            f.set(obj, Short.parseShort(s));
        } else if (cls == char.class || cls == Character.class) {
            f.set(obj, s.charAt(0));
        } else if (cls == byte.class || cls == Byte.class) {
            f.set(obj, Byte.parseByte(s));
        }
    }
    
    public <E extends Event> E getEvent(SkillType<E> e){
        return (E) this.evt;
    }

    public abstract boolean canCast();

    public abstract String cantCastMsg();

    public abstract void onCast();
    /**
     * @return 返回格式化后的技能描述  如果需要在套装中使用的话必须覆盖
     */
    public String toString(){
        return super.toString();
    }
}
