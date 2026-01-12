package net.sirbromate.nodimensionbackground.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.text.Text;
import net.sirbromate.nodimensionbackground.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Environment(EnvType.CLIENT)
@Mixin(value = DownloadingTerrainScreen.class, priority = 1)
public class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private Sprite getBackgroundSprite() {
        return null;
    }

    @Final
    @Shadow
    private DownloadingTerrainScreen.WorldEntryReason worldEntryReason;

    /**
     * @author SirBroMate
     * @reason Wrap every case in condition
     */
    @Overwrite
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        switch (this.worldEntryReason.ordinal()) {
            case 0:
                if (Config.disableNetherBackground) {
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                } else {
                    context.drawSpriteStretched(RenderPipelines.GUI_OPAQUE_TEX_BG, this.getBackgroundSprite(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight());
                }
                break;
            case 1:
                if (Config.disableEndBackground) {
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                } else {
                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    TextureSetup textureSetup = TextureSetup.of(textureManager.getTexture(EndPortalBlockEntityRenderer.SKY_TEXTURE).getGlTextureView(), textureManager.getTexture(EndPortalBlockEntityRenderer.PORTAL_TEXTURE).getGlTextureView());
                    context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
                }
                break;
            case 2:
                if (Config.disableLoadingPanoramaBackground) {
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                } else {
                    this.renderPanoramaBackground(context, deltaTicks);
                    this.applyBlur(context);
                    this.renderDarkening(context);
                }
        }
    }
}