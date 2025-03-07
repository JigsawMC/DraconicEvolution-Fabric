package com.brandon3055.draconicevolution.api.modules.data;

import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Map;

/**
 * Created by brandon3055 on 3/5/20.
 */
public class JumpData implements ModuleData<JumpData> {
    private final double multiplier;

    public JumpData(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public JumpData combine(JumpData other) {
        return new JumpData(multiplier + other.multiplier);
    }

    @Override
    public void addInformation(Map<ITextComponent, ITextComponent> map, ModuleContext context, boolean stack) {
        map.put(new TranslationTextComponent("module.draconicevolution.jump.name"), new TranslationTextComponent("module.draconicevolution.jump.value", (int)(multiplier * 100D)));
    }
}
