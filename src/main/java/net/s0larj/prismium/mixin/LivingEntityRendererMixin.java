package net.s0larj.prismium.mixin;

import net.minecraft.Optionull;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.s0larj.prismium.attachment.ModAttachments;
import net.s0larj.prismium.entity.ModCustomEntityClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V")
    public void extractRenderStateMixin(T entity, S state, float partialTicks, CallbackInfo ci) {
        state.setData(ModCustomEntityClient.ANCHOR_KEY, entity.getAttached(ModAttachments.HOOKED));
        state.setData(ModCustomEntityClient.ENTITY_ID, entity.getId());
    }

}


