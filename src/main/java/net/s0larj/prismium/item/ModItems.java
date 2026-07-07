package net.s0larj.prismium.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.item.custom.AnchorItem;

import java.util.function.Function;

public class ModItems {
    public static final Item PRISMIUM = registerItem("prismium", Item::new);
    public static final Item ANCHOR = registerItem("anchor_item", properties -> new AnchorItem(properties.durability(32)));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Prismium.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Prismium.MOD_ID, name)))));
    }

    public static void registerModItems() {
        Prismium.LOGGER.info("Registering Mod Items for " + Prismium.MOD_ID);

    }
}