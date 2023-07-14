package net.quackimpala7321.duckmod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.entity.ModEntities;
import net.quackimpala7321.duckmod.item.ModItems;

public class DuckFeatherEntity extends PersistentProjectileEntity {

    public DuckFeatherEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public DuckFeatherEntity(World world, double x, double y, double z) {
        super(ModEntities.DUCK_FEATHER_ENTITY, x, y, z, world);
    }

    public DuckFeatherEntity(World world, LivingEntity owner) {
        super(ModEntities.DUCK_FEATHER_ENTITY, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.DUCK_FEATHER_ITEM);
    }
}
