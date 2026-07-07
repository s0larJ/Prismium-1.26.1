package net.s0larj.prismium;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.s0larj.prismium.datagen.ModBlockLootTableProvider;
import net.s0larj.prismium.datagen.ModBlockTagsProvider;
import net.s0larj.prismium.datagen.ModModelProvider;
import net.s0larj.prismium.datagen.ModRecipeProvider;

public class PrismiumDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
