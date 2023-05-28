package com.xelo.createbam.content;


import com.simibubi.create.content.contraptions.relays.gearbox.GearboxRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.xelo.createbam.CreateBam;
import com.xelo.createbam.content.blocks.misc.IdlerGearboxInstance;
import com.xelo.createbam.content.blocks.misc.IdlerGearboxTileEntity;



public class ModTileEntities {

	private static final CreateRegistrate REGISTRATE = CreateBam.registrate()
			.creativeModeTab(() -> CreateBam.CreativeCategory);

	public static final BlockEntityEntry<IdlerGearboxTileEntity> IDLER_GEARBOX = REGISTRATE
			.tileEntity("idler_gearbox", IdlerGearboxTileEntity::new)
			.instance(() -> IdlerGearboxInstance::new, false)
			.validBlocks(ModBlocks.IDLER_GEARBOX)
			.renderer(() -> GearboxRenderer::new)
			.register();
	public static void register() {

	}
}
