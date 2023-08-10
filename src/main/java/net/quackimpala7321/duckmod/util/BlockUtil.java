package net.quackimpala7321.duckmod.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {
    public static List<BlockPos> scanBoxForBlock(Block block, Box box, World world) {
        List<BlockPos> blockPosList = new ArrayList<>();

        for (int x = (int) box.minX; x <= box.maxX; x++) {
            for (int z = (int) box.minZ; z <= box.maxZ; z++) {
                for (int y = (int) box.minY; y <= box.maxY; y++) {
                    BlockPos target = new BlockPos(x, y, z);
                    if (world.getBlockState(target).getBlock() != block) continue;

                    blockPosList.add(target);
                }
            }
        }

        return blockPosList;
    }
}
