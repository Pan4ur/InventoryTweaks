package fastdrop.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Unique
    private boolean pauseListening = false;

    @Inject(method = "clickSlot", at = @At("HEAD"))
    public void clickSlotHook(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if ((isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))
                && (isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL))
                && actionType == SlotActionType.THROW
                && !pauseListening) {
            Item copy = MinecraftClient.getInstance().player.currentScreenHandler.slots.get(slotId).getStack().getItem();
            pauseListening = true;
            for (int i2 = 0; i2 < MinecraftClient.getInstance().player.currentScreenHandler.slots.size(); ++i2) {
                if (MinecraftClient.getInstance().player.currentScreenHandler.slots.get(i2).getStack().getItem() == copy)
                    MinecraftClient.getInstance().interactionManager.clickSlot(MinecraftClient.getInstance().player.currentScreenHandler.syncId, i2, 1, SlotActionType.THROW, MinecraftClient.getInstance().player);
            }
            pauseListening = false;
        }
    }

    @Unique
    public boolean isKeyPressed(int button) {
        if (button == -1)
            return false;
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), button);
    }
}
