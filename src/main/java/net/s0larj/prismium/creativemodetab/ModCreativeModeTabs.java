package net.s0larj.prismium.creativemodetab;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.block.ModBlocks;
import net.s0larj.prismium.item.ModItems;

public class ModCreativeModeTabs {

    public static final CreativeModeTab PRISMIUM_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "prismium_items"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PRISMIUM))
                    .title(Component.translatable("creativemodetab.prismium.prisimum_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.PRISMIUM);
                        output.accept(ModBlocks.PRISMIUM_BLOCK);
                        output.accept(ModBlocks.TEMP);
                        output.accept(ModItems.ANCHOR);
                    }).build());

    public static void registerModCreativeModeTabs() {
        Prismium.LOGGER.info("Registering Creative Mode Tabs for " + Prismium.MOD_ID);

    }
}
