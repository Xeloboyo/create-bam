package com.xelo.createbam.mixin;

import com.simibubi.create.content.contraptions.RotationPropagator;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.DirectionalShaftHalvesTileEntity;
import com.simibubi.create.content.contraptions.relays.gearbox.GearboxTileEntity;
import com.xelo.createbam.CreateBam;

import com.xelo.createbam.content.blocks.misc.IdlerGearboxTileEntity;

import net.minecraft.client.main.GameConfig;

import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {
	@Inject(method = "getAxisModifier(Lcom/simibubi/create/content/contraptions/base/KineticTileEntity;Lnet/minecraft/core/Direction;)F", at = @At("TAIL"), cancellable = true)
	private static void getAxisModifier(KineticTileEntity te, Direction direction, CallbackInfoReturnable<Float> cir) {
		if (te instanceof IdlerGearboxTileEntity t){
			Direction source = ((DirectionalShaftHalvesTileEntity) te).getSourceFacing();
			cir.setReturnValue( direction.getAxis() == source.getAxis() ? direction == source ? 1f : -1f
					: direction.getAxisDirection() != source.getAxisDirection() ? -1f : 1f);

		}
	}
}
