package net.quackimpala7321.duckmod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.registry.ModStatusEffects;

public class DuckNestItem extends ArmorItem {

    public DuckNestItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(slot != EquipmentSlot.HEAD.getEntitySlotId()) return;

        if(entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DUCK_LEADER, 3, 0, true, false));
        }
    }
}
