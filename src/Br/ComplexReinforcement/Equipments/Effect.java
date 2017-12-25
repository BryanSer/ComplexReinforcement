/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.ComplexReinforcement.Equipments;

import Br.API.Data.BrConfigurationSerializable;
import Br.ComplexReinforcement.Attributes.Attribute;
import Br.ComplexReinforcement.Attributes.AttributeType;
import Br.ComplexReinforcement.Attributes.AttributesManager;
import Br.ComplexReinforcement.Logs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Bryan_lzh
 */
public class Effect implements BrConfigurationSerializable {

    @Config
    private List<String> RawEffects;
    private Map<String, Attribute.Value<? extends Number>> Bases;
    private Map<String, List<Attribute.Value>> Advanceds;
    private List<String> Skills;
    
    

    public Effect(Map<String, Object> args) {
        BrConfigurationSerializable.deserialize(args, this);
        this.Analyze();
    }

    public final void Analyze() {
        if (this.Skills == null) {
            this.Skills = new ArrayList<>();
            this.Bases = new HashMap<>();
            this.Advanceds = new HashMap<>();
        } else {
            this.Skills.clear();
            this.Bases.clear();
            this.Advanceds.clear();
        }
        Outter:
        for (String s : RawEffects) {
            if (s.startsWith("Skill:")) {
                s = s.replaceFirst("Skill:", "");
                Skills.add(s);
                continue;
            }
            if (s.startsWith("Attributes:")) {
                s = s.replaceFirst("Attributes:", "");
                for (Attribute a : AttributesManager.getAttributes()) {
                    if (s.startsWith(a.getName())) {
                        try {
                            s = s.contains(":") ? s.split(":")[1] : s;
                            if (a.getAttrType() == AttributeType.Base) {
                                Attribute.Value v = null;
                                if (Bases.containsKey(a.getName())) {
                                    v = Bases.get(a.getName());
                                } else {
                                    v = new Attribute.Value<>();
                                }
                                v = a.AnalyzeLore(s, v);
                                Bases.put(a.getName(), v);
                            } else {
                                List<Attribute.Value> values;
                                if (Advanceds.containsKey(a.getName())) {
                                    values = Advanceds.get(a.getName());
                                } else {
                                    values = new ArrayList<>();
                                }
                                values.add(a.AnalyzeLore(s, new Attribute.Value<>()));
                                Advanceds.put(a.getName(), values);
                            }
                        } catch (Throwable e) {
                            Logs.Log(e);
                        }
                        continue Outter;
                    }
                }
            }

        }
    }

    public void doWithRawEffects(Consumer<List<String>> c) {
        c.accept(this.RawEffects);
        this.Analyze();
    }

    @Deprecated
    public List<String> getRawEffects() {
        return RawEffects;
    }

    public Map<String, Attribute.Value<? extends Number>> getBases() {
        if (this.Skills == null) {
            this.Analyze();
        }
        return Bases;
    }

    public Map<String, List<Attribute.Value>> getAdvanceds() {
        if (this.Skills == null) {
            this.Analyze();
        }
        return Advanceds;
    }

    public List<String> getSkills() {
        if (this.Skills == null) {
            this.Analyze();
        }
        return Skills;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.RawEffects);
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
        final Effect other = (Effect) obj;
        if (!Objects.equals(this.RawEffects, other.RawEffects)) {
            return false;
        }
        if (!Objects.equals(this.Bases, other.Bases)) {
            return false;
        }
        if (!Objects.equals(this.Advanceds, other.Advanceds)) {
            return false;
        }
        return Objects.equals(this.Skills, other.Skills);
    }
    
    
}
