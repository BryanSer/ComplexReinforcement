/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import Br.ComplexReinforcement.Logs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Bryan_lzh
 */
public final class AttributeEvent extends Event implements Cancellable {

    private Player player;
//    private List<String> AttributesList = new ArrayList<>();
    private Attribute.Type Type;

    private Map<String, Attribute.Value<? extends Number>> Bases = new HashMap<>();
    private Map<String, List<Attribute.Value>> Advanceds = new HashMap<>();

    public AttributeEvent(Player p, Attribute.Type t) {
        this.player = p;
        this.Type = t;
    }

    public Map<String, Attribute.Value<? extends Number>> getBases() {
        return this.Bases;
    }

    public Map<String, List<Attribute.Value>> getAdvanceds() {
        return Advanceds;
    }

    public Map<String, Attribute.Value<? extends Number>> Combine(Map<String, Attribute.Value<? extends Number>> map) {
        for (Map.Entry<String, Attribute.Value<? extends Number>> E : map.entrySet()) {
            String key = E.getKey();
            Attribute.Value<? extends Number> value = E.getValue();
            if(this.Bases.containsKey(key)){
                Attribute.Value<? extends Number> v = Bases.get(key);
                v.add(value.getValue());
            }else {
                Bases.put(key, value);
            }
        }
        return getBases();
    }

//    private void Analze() {
//        Bases = new HashMap<>();
//        Advanceds = new HashMap<>();
//        Collection<Attribute> attrs = AttributesManager.getAttributes();
//        Outter:
//        for (String s : this.AttributesList) {
//            s = ChatColor.stripColor(s);
//            for (Attribute a : attrs) {
//                if (s.startsWith(a.getName())) {
//                    try {
//                        s = s.contains(":") ? s.split(":")[1] : s;
//                        if (a.getAttrType() == AttributeType.Base) {
//                            Attribute.Value v = null;
//                            if (Bases.containsKey(a.getName())) {
//                                v = Bases.get(a.getName());
//                            } else {
//                                v = new Attribute.Value<>();
//                            }
//                            v = a.AnalyzeLore(s, v);
//                            Bases.put(a.getName(), v);
//                        } else {
//                            List<Attribute.Value> values;
//                            if (Advanceds.containsKey(a.getName())) {
//                                values = Advanceds.get(a.getName());
//                            } else {
//                                values = new ArrayList<>();
//                            }
//                            values.add(a.AnalyzeLore(s, new Attribute.Value<>()));
//                            Advanceds.put(a.getName(), values);
//                        }
//                    } catch (Throwable e) {
//                        Logs.Log(e);
//                    }
//                    continue Outter;
//                }
//            }
//        }
//    }
    public Player getPlayer() {
        return player;
    }

    public Attribute.Type getType() {
        return Type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
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

}
