/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Skills;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bryan_lzh
 */
public abstract class CDArgments extends Argments {

    private Map<String, Long> LastCast = new HashMap<>();

    @Argment(Key = "CD")
    private double CD;

    @Override
    public final boolean canCast() {
        if (!LastCast.containsKey(super.player.getName())) {
            return true;
        }
        long get = System.currentTimeMillis() - LastCast.get(super.player.getName());
        double time = get / 1000d;
        return time >= this.CD;
    }

    private final static DecimalFormat DF = new DecimalFormat("#.##");

    @Override
    public final String cantCastMsg() {
        long get = System.currentTimeMillis() - LastCast.get(super.player.getName());
        double time = get / 1000d;
        if (time > this.CD) {
            return "冷却完毕";
        }
        time = this.CD - time;
        return String.format("技能冷却中 还需要<%s>秒", DF.format(time));
    }

    @Override
    public final void onCast() {
        LastCast.put(super.player.getName(), System.currentTimeMillis());
    }

}
