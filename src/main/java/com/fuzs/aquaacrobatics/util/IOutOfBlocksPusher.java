package com.fuzs.aquaacrobatics.util;

import com.fuzs.aquaacrobatics.util.math.CubeCoordinateIterator;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiPredicate;

public interface IOutOfBlocksPusher {

    static void setPlayerOffsetMotion(Entity entity, double x, double z) {

        BlockPos blockpos = new BlockPos(x, entity.posY, z);
        if (shouldBlockPushPlayer(entity, blockpos)) {

            double d0 = x - (double) blockpos.getX();
            double d1 = z - (double) blockpos.getZ();
            EnumFacing direction = null;
            double d2 = Double.MAX_VALUE;
            EnumFacing[] xzPlane = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH};

            for (EnumFacing direction1 : xzPlane) {

                double d3 = getCoordinateFromAxis(direction1.getAxis(), d0, 0.0, d1);
                double d4 = direction1.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 1.0 - d3 : d3;
                if (d4 < d2 && !shouldBlockPushPlayer(entity, blockpos.offset(direction1))) {

                    d2 = d4;
                    direction = direction1;
                }
            }

            if (direction != null) {

                if (direction.getAxis() == EnumFacing.Axis.X) {

                    entity.motionX = 0.1 * (double) direction.getDirectionVec().getX();
                } else {

                    entity.motionZ = 0.1 * (double) direction.getDirectionVec().getZ();
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    static double getCoordinateFromAxis(EnumFacing.Axis axis, double x, double y, double z) {

        switch (axis) {

            case X:
                return x;

            case Y:
                return y;

            case Z:
                return z;
        }

        throw new Error("Someone's been tampering with the universe!");
    }

    static boolean shouldBlockPushPlayer(Entity entity, BlockPos pos) {

        double minY = entity.getEntityBoundingBox().minY;
        double maxY = entity.getEntityBoundingBox().maxY;
        AxisAlignedBB blockBoundingBox = new AxisAlignedBB(pos.getX(), minY, pos.getZ(), (double) pos.getX() + 1.0, maxY, (double) pos.getZ() + 1.0);

//        return this.world.collidesWithAnyBlock(playerBoundingBox);
//        return ((IWorldAccessor) this.world).callGetCollisionBoxes(this, blockBoundingBox, false, Lists.newArrayList());

//        BiPredicate<IBlockAccess, BlockPos> statePositionPredicate = (world, pos) -> {
//
//            IBlockState iblockstate = world.getBlockState(pos);
//            return iblockstate.causesSuffocation() && iblockstate.getCollisionBoundingBox(world, pos) != null;
//        };

        return doesEntityCollideWithAABB(entity.world, createCubeIterator(blockBoundingBox), entity, (world, pos1) -> true);
    }

    static CubeCoordinateIterator createCubeIterator(AxisAlignedBB aabb) {

        int startX = MathHelper.floor(aabb.minX) - 1;
        int endX = MathHelper.floor(aabb.maxX) + 1;
        int startY = MathHelper.floor(aabb.minY) - 1;
        int yHeight = MathHelper.floor(aabb.maxY) + 1;
        int startZ = MathHelper.floor(aabb.minZ) - 1;
        int endZ = MathHelper.floor(aabb.maxZ) + 1;

        return new CubeCoordinateIterator(startX, startY, startZ, endX, yHeight, endZ);
    }

    static boolean doesEntityCollideWithAABB(World world, CubeCoordinateIterator cubeCoordinateIterator, Entity entity, BiPredicate<IBlockAccess, BlockPos> statePositionPredicate) {

        AxisAlignedBB aabb = entity.getEntityBoundingBox();
        BlockPos.PooledMutableBlockPos mutablePos = BlockPos.PooledMutableBlockPos.retain();
        while (cubeCoordinateIterator.hasNext()) {

            int x = cubeCoordinateIterator.getX();
            int y = cubeCoordinateIterator.getY();
            int z = cubeCoordinateIterator.getZ();

            int boundariesTouched = cubeCoordinateIterator.numBoundariesTouched();
            if (boundariesTouched == 3 || !world.isBlockLoaded(mutablePos.setPos(x, 64, z))) {

                continue;
            }

            mutablePos.setPos(x, y, z);
            IBlockState iblockstate = world.getBlockState(mutablePos);
            if (!statePositionPredicate.test(world, mutablePos) || boundariesTouched == 2 && iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {

                continue;
            }

            if (iblockstate.isFullCube()) {

                if (aabb.intersects(Block.FULL_BLOCK_AABB.offset(mutablePos))) {

                    mutablePos.release();
                    return true;
                }
            }

            List<AxisAlignedBB> collidingBoxes = Lists.newArrayList();
            iblockstate.addCollisionBoxToList(world, mutablePos, aabb, collidingBoxes, entity, false);
            if (!collidingBoxes.isEmpty()) {

                mutablePos.release();
                return true;
            }
        }

        mutablePos.release();
        return false;
    }

}