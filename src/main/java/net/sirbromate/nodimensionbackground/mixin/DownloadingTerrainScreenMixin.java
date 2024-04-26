package net.sirbromate.nodimensionbackground.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.text.Text;
import net.sirbromate.nodimensionbackground.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private Sprite getBackgroundSprite() {
        return null;
    }

    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawSprite(IIIIILnet/minecraft/client/texture/Sprite;)V"))
    private void injected(DrawContext context, int x, int y, int z, int width, int height, Sprite sprite) {
        if (Config.disableNetherBackground) {
            Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, x, y, 0.0f, 0.0f, this.width, this.height);
        } else {
            context.drawSprite(0, 0, -90, context.getScaledWindowWidth(), context.getScaledWindowHeight(), this.getBackgroundSprite());
        }
    }

    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillWithLayer(Lnet/minecraft/client/render/RenderLayer;IIIII)V"))
    private void injected(DrawContext context, RenderLayer layer, int startX, int startY, int endX, int endY, int z) {
        if (Config.disableEndBackground) {
            Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, startX, startY, 0.0f, 0.0f, this.width, this.height);
        } else {
            context.fillWithLayer(RenderLayer.getEndPortal(), 0, 0, this.width, this.height, 0);
        }
    }
}