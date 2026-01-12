package net.sirbromate.nodimensionbackground.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.text.Text;
import net.sirbromate.nodimensionbackground.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


@Environment(EnvType.CLIENT)
@Mixin(value = LevelLoadingScreen.class, priority = 1)
public class LevelLoadingScreenMixin extends Screen {
    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }
    @Shadow
    private LevelLoadingScreen.WorldEntryReason reason;

    @Shadow
    private Sprite getNetherPortalSprite() {
        return null;
    }

    /**
     * @author SirBroMate
     * @reason Wrap every case in condition
     */
    @Overwrite
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        switch (this.reason) {
            case NETHER_PORTAL:
                if (Config.disableNetherBackground) {
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                }
                else {
                    context.drawSpriteStretched( RenderPipelines.GUI_OPAQUE_TEX_BG, this.getNetherPortalSprite(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight());
                }
                break;
            case END_PORTAL:
                if (Config.disableEndBackground){
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                } else {
                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    AbstractTexture abstractTexture = textureManager.getTexture(AbstractEndPortalBlockEntityRenderer.SKY_TEXTURE);
                    AbstractTexture abstractTexture2 = textureManager.getTexture(AbstractEndPortalBlockEntityRenderer.PORTAL_TEXTURE);
                    TextureSetup textureSetup = TextureSetup.of(abstractTexture.getGlTextureView(), abstractTexture.getSampler(), abstractTexture2.getGlTextureView(), abstractTexture2.getSampler());
                    context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
                }
                break;
            case OTHER:
                if (Config.disableLoadingPanoramaBackground){
                    Screen.renderBackgroundTexture(context, Screen.MENU_BACKGROUND_TEXTURE, 0, 0, 0.0f, 0.0f, this.width, this.height);
                } else {
                    this.renderPanoramaBackground(context, deltaTicks);
                    this.applyBlur(context);
                    this.renderDarkening(context);
                }
        }
    }
}