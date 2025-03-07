package com.brandon3055.draconicevolution.items.equipment;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.ModuleCfg;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by brandon3055 on 21/5/20.
 */
public class ModularHoe extends HoeItem implements IModularTieredItem {
    private final TechLevel techLevel;
    private final DEItemTier itemTier;

    public ModularHoe(TechPropBuilder props) {
        super(new DEItemTier(props, EquipCfg::getHoeDmgMult, EquipCfg::getHoeSpeedMult), 0, 0, props.build().fireResistant());
        this.techLevel = props.techLevel;
        this.itemTier = (DEItemTier) getTier();
    }

    @Override
    public TechLevel getTechLevel() {
        return techLevel;
    }

    @Override
    public DEItemTier getItemTier() {
        return itemTier;
    }

    @Override
    public ModuleHostImpl createHost(ItemStack stack) {
        return new ModuleHostImpl(techLevel, ModuleCfg.toolWidth(techLevel), ModuleCfg.toolHeight(techLevel), "hoe", ModuleCfg.removeInvalidModules);
    }

    @Nullable
    @Override
    public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
        return new ModularOPStorage(host, EquipCfg.getBaseToolEnergy(techLevel), EquipCfg.getBaseToolTransfer(techLevel));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return IModularTieredItem.super.getDestroySpeed(stack, state);
    }

    @Override
    public float getBaseEfficiency() {
        return getTier().getSpeed();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addModularItemInformation(stack, worldIn, tooltip, flagIn);
    }
}
