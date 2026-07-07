package net.s0larj.prismium.data;

import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModDataComponents {

    public static final DataComponentType<UUID> ANC = register(
            "anc", builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(Prismium.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataCompontents() {
        Prismium.LOGGER.info("Registering EntityTypes for " + Prismium.MOD_ID);
    }
}
