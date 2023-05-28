package com.xelo.createbam.content.blocks.misc;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;

import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.EnumMap;
import java.util.Map;

public class IdlerGearboxInstance extends KineticTileInstance<IdlerGearboxTileEntity> {
	protected final EnumMap<Direction, RotatingData> keys;
	protected Direction sourceFacing;

	RotatingData topcog, bottomcog;

	public IdlerGearboxInstance(MaterialManager modelManager, IdlerGearboxTileEntity tile) {
		super(modelManager, tile);
		keys = new EnumMap<>(Direction.class);

		final Direction.Axis boxAxis = blockState.getValue(BlockStateProperties.AXIS);

		int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
		int skyLight = world.getBrightness(LightLayer.SKY, pos);
		updateSourceFacing();

		Material<RotatingData> rotatingMaterial = getRotatingMaterial();

		for (Direction direction : Iterate.directions) {
			final Direction.Axis axis = direction.getAxis();
			if (boxAxis == axis)
				continue;
			Instancer<RotatingData> shaft = rotatingMaterial.getModel(AllBlockPartials.SHAFT_HALF, blockState, direction);

			RotatingData key = shaft.createInstance();

			key.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
					.setRotationalSpeed(getSpeed(direction))
					.setRotationOffset(getRotationOffset(axis)).setColor(tile)
					.setPosition(getInstancePosition())
					.setBlockLight(blockLight)
					.setSkyLight(skyLight);

			keys.put(direction, key);
		}
		float offset = 4.5f/16f;
		Direction topdir = Direction.get(Direction.AxisDirection.POSITIVE, boxAxis);
		topcog = getCogModel(topdir).createInstance();
		topcog.setRotationalSpeed(getTileSpeed()*-0.5f);
		topcog.setRotationAxis(topdir.step());
		topcog.setRotationOffset(getRotationOffset(boxAxis));
		topcog.setPosition(getInstancePosition());
		topcog.setBlockLight(blockLight);
		topcog.setSkyLight(skyLight);
		topcog.nudge(offset*topdir.getStepX(), offset*topdir.getStepY(), offset*topdir.getStepZ());
		offset *= -1;
		bottomcog = getCogModel(topdir.getOpposite()).createInstance();
		bottomcog.setRotationalSpeed(getTileSpeed()*0.5f);
		bottomcog.setRotationAxis(topdir.step());
		bottomcog.setRotationOffset(getRotationOffset(boxAxis));
		bottomcog.setPosition(getInstancePosition());
		bottomcog.setBlockLight(blockLight);
		bottomcog.setSkyLight(skyLight);
		bottomcog.nudge(offset*topdir.getStepX(), offset*topdir.getStepY(), offset*topdir.getStepZ());

	}

	protected Instancer<RotatingData> getCogModel(Direction facing) {
		return materialManager.defaultSolid()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(AllBlockPartials.SHAFTLESS_COGWHEEL, blockEntity.getBlockState(),facing, () -> {
					PoseStack poseStack = new PoseStack();
					TransformStack.cast(poseStack)
							.centre()
							.rotateToFace(facing)
							.multiply(Vector3f.XN.rotationDegrees(90))
							.unCentre();
					return poseStack;
				});
	}

	private float getSpeed(Direction direction) {
		float speed = blockEntity.getSpeed();

		if (speed != 0 && sourceFacing != null) {
			if (sourceFacing.getAxis() == direction.getAxis())
				speed *= sourceFacing == direction ? 1 : -1;
			else if (sourceFacing.getAxisDirection() != direction.getAxisDirection())
				speed *= -1;
			return speed;
		}
		return speed;
	}

	protected void updateSourceFacing() {
		if (blockEntity.hasSource()) {
			BlockPos source = blockEntity.source.subtract(pos);
			sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
		} else {
			sourceFacing = null;
		}
	}

	@Override
	public void update() {
		updateSourceFacing();
		for (Map.Entry<Direction, RotatingData> key : keys.entrySet()) {
			Direction direction = key.getKey();
			Direction.Axis axis = direction.getAxis();

			updateRotation(key.getValue(), axis, getSpeed(direction));
		}
		final Direction.Axis boxAxis = blockState.getValue(BlockStateProperties.AXIS);
		updateRotation(topcog, getRotationAxis(),  getTileSpeed()*-0.5f);
		updateRotation(bottomcog, getRotationAxis(),  getTileSpeed()*0.5f);
	}

	@Override
	public void updateLight() {
		relight(pos, keys.values().stream());
		relight(pos, topcog);
		relight(pos, bottomcog);
	}

	@Override
	public void remove() {
		keys.values().forEach(InstanceData::delete);
		keys.clear();
		topcog.delete();
		bottomcog.delete();
	}
}
