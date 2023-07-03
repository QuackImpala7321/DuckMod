package net.quackimpala7321.duckmod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EggIncubatorBlock extends Block {
    public EggIncubatorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(HAS_EGG, false));
    }

    public static final BooleanProperty HAS_EGG = BooleanProperty.of("has_egg");

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            if(player.getStackInHand(Hand.MAIN_HAND).getItem() != Items.EGG) {
                return ActionResult.FAIL;
            }

            if(world.getBlockState(pos).get(HAS_EGG)) {
                return ActionResult.FAIL;
            }

            world.setBlockState(pos, state.with(HAS_EGG, true));
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_EGG);
    }
}
