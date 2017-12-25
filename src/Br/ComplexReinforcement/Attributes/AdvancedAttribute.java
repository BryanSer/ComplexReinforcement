/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Attributes;

import org.bukkit.plugin.Plugin;

/**
 *
 * @author Bryan_lzh
 */
public abstract class AdvancedAttribute<V> extends Attribute<V> {

    public AdvancedAttribute(Plugin p, String name, Attribute.Type t, String... des) {
        super(p, name, AttributeType.Advanced, t, des);
    }
}
