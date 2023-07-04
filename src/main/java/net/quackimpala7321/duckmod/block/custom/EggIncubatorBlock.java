package net.quackimpala7321.duckmod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.quackimpala7321.duckmod.item.ModItems;

public class EggIncubatorBlock extends Block {
    public static final int MAX_AGE = 3;

    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    private static final BooleanProperty HAS_EGG = BooleanProperty.of("has_egg");
    public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);

    public EggIncubatorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(HAS_EGG, false)
                .with(AGE, 0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(hasEgg(state)) {
            if(getAge(state) < MAX_AGE)
                return ActionResult.FAIL;

            world.setBlockState(pos, state.with(HAS_EGG, false).with(AGE, 0));
            player.giveItemStack(new ItemStack(ModItems.DUCK_EGG, 1));

            return ActionResult.SUCCESS;
        }

        if(player.getStackInHand(Hand.MAIN_HAND).getItem() != Items.EGG)
            return ActionResult.FAIL;

        world.setBlockState(pos, state.with(HAS_EGG, true));

        if(!player.getAbilities().creativeMode)
            player.getStackInHand(Hand.MAIN_HAND).decrement(1);

        return ActionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextInt(5) > 0) return;

        world.setBlockState(pos, state.with(AGE, getAge(state) + 1));
    }

    private int getAge(BlockState state) {
        return state.get(AGE);
    }

    private boolean hasEgg(BlockState state) {
        return state.get(HAS_EGG);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return getAge(state) < MAX_AGE && hasEgg(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_EGG, AGE);
    }
}
