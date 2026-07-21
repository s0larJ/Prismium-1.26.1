package net.s0larj.prismium.mixin;

import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ModelPart.class)
public interface ModelPartAccessorMixin {

    @Accessor("cubes")
    List<ModelPart.Cube> prismium$getCubes();
}
