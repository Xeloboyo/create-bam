package com.xelo.createbam;

import com.simibubi.create.Create;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;

import com.xelo.createbam.content.ModBlocks;

import com.xelo.createbam.content.ModTileEntities;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBam implements ModInitializer {
	public static final String ID = "createbam";
	public static final String NAME = "Create Bam";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(CreateBam.ID);

	public static CreativeModeTab CreativeCategory = FabricItemGroupBuilder.build(new ResourceLocation(CreateBam.ID, "main"), () -> new ItemStack(Items.ACACIA_DOOR));

	@Override
	public void onInitialize() {
		LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", NAME, Create.VERSION);
		LOGGER.info(EnvExecutor.unsafeRunForDist(
				() -> () -> "{} is accessing Porting Lib from the client!",
				() -> () -> "{} is accessing Porting Lib from the server!"
		), NAME);

		ModBlocks.register();
		ModTileEntities.register();
		REGISTRATE.get().register();
		LOGGER.info("REGISTRATE id is "+REGISTRATE.get().getModid());
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
}
