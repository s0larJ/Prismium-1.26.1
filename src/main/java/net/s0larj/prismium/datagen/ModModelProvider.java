package net.s0larj.prismium.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.s0larj.prismium.block.ModBlocks;
import net.s0larj.prismium.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(ModBlocks.PRISMIUM_BLOCK);
        blockModelGenerators.createTrivialCube(ModBlocks.TEMP);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModItems.PRISMIUM, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.declareCustomModelItem(ModItems.ANCHOR);
    }
}
