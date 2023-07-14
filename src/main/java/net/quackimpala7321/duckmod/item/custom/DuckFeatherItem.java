package net.quackimpala7321.duckmod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.entity.custom.DuckFeatherEntity;

public class DuckFeatherItem extends Item {
    public DuckFeatherItem(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createFeatherEntity(World world, LivingEntity shooter) {
        return new DuckFeatherEntity(world, shooter);
    }
}
