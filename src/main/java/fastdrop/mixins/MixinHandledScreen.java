package fastdrop.mixins;


import com.mojang.blaze3d.systems.RenderSystem;
import fastdrop.FastDrop;
import fastdrop.Timer;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = {HandledScreen.class})
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {

    @Unique
    private final Timer delayTimer = new Timer();

    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slotIn, double mouseX, double mouseY);

    @Shadow
    protected abstract void onMouseClick(Slot slotIn, int slotId, int mouseButton, SlotActionType type);

    @Inject(method = "render", at = @At("HEAD"))
    private void drawScreenHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (int i1 = 0; i1 < MinecraftClient.getInstance().player.currentScreenHandler.slots.size(); ++i1) {
            Slot slot = MinecraftClient.getInstance().player.currentScreenHandler.slots.get(i1);
            if (isPointOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                if(shit()) {
                    if (attack() && delayTimer.passedMs(90)) {
                        this.onMouseClick(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
                        delayTimer.reset();
                    }
                }
            }
        }
    }

    private boolean shit() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
    }

    private boolean attack() {
        return FastDrop.hold_mouse0;
    }
}
