package com.xelo.createbam.content;

import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.relays.gearbox.GearboxBlock;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.xelo.createbam.CreateBam;
import com.xelo.createbam.content.blocks.misc.IdlerGearboxBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {

	private static final CreateRegistrate REGISTRATE = CreateBam.registrate()
			.creativeModeTab(() -> CreateBam.CreativeCategory);

	public static final BlockEntry<IdlerGearboxBlock> IDLER_GEARBOX = REGISTRATE.block("idler_gearbox", IdlerGearboxBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(BlockStressDefaults.setNoImpact())
			.transform(axeOrPickaxe())
			.blockstate((c, p) -> axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true))
			.item()
			.transform(customItemModel())
			.register();


	public static void register() {
		Create.REGISTRATE.addToSection(IDLER_GEARBOX, AllSections.KINETICS);
	}
}
