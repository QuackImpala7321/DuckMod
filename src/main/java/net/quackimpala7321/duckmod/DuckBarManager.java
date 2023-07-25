package net.quackimpala7321.duckmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;
import net.quackimpala7321.duckmod.item.ModArmorMaterials;
import net.quackimpala7321.duckmod.statuseffect.ModStatusEffects;

import java.util.ArrayList;
import java.util.List;

public class DuckBarManager {
    public static final int DUCK_LEADER = 2;
    public static final int DUCK_ARMOR = 1;
    private final PlayerEntity player;

    private int slots;

    public DuckBarManager(PlayerEntity player) {
        this.player = player;
    }

    public void update() {
        this.slots = 0;

        List<ArmorMaterial> armorMaterials = new ArrayList<ArmorMaterial>();

        for(ItemStack itemStack : this.player.getArmorItems()) {
            if(!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armorItem) {
                armorMaterials.add(armorItem.getMaterial());
            } else {
                armorMaterials.add(null);
            }
        }

        int duckParts = (int) armorMaterials.stream()
                .filter(armorMaterial -> armorMaterial == ModArmorMaterials.DUCK_ARMOR_MATERIAL)
                .count();

        boolean hasDuckLeader = this.player.hasStatusEffect(ModStatusEffects.DUCK_LEADER);

        if(duckParts >= armorMaterials.size()) {
            this.slots += DUCK_ARMOR;
        } else if (duckParts == armorMaterials.size() - 1 && hasDuckLeader) {
            this.slots += DUCK_LEADER;
            this.slots += DUCK_ARMOR;
        } else if (hasDuckLeader) {
            this.slots += DUCK_LEADER;
        }
    }

    public List<DuckEntity> getDucks() {
        List<Entity> ducks = this.player.getPassengerList().stream()
                .filter(entity -> entity instanceof DuckEntity duckEntity && duckEntity.isOnOwner())
                .toList();

        List<DuckEntity> duckEntityList = new ArrayList<DuckEntity>();
        for(Entity entity : ducks) {
            duckEntityList.add((DuckEntity) entity);
        }

        return  duckEntityList;
    }

    public int getSlots() {
        return this.slots;
    }
}
