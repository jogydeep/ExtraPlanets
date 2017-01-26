package com.mjr.extraplanets.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.client.gui.overlay.Overlay;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.mjr.extraplanets.Constants;

@SideOnly(Side.CLIENT)
public class OverlayPressure extends Overlay
{
    private final static ResourceLocation guiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/gui.png");

    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    /**
     * Render the GUI that displays oxygen level in tanks
     */
    public static void renderPressureIndicator(int pressure, boolean right, boolean top)
    {
    	boolean invalid = Math.abs(pressure) >= 60;
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayPressure.minecraft, OverlayPressure.minecraft.displayWidth, OverlayPressure.minecraft.displayHeight);
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        
        GlStateManager.disableAlpha();
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(OverlayPressure.guiTexture);
        final Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int minLeftX = 0;
        int maxLeftX = 0;
        int minRightX = 0;
        int maxRightX = 0;
        double bottomY = 0;
        double topY = 0;
        double zLevel = -190.0D;

        if (right)
        {
            minLeftX = i - 75;
            maxLeftX = i - 30;
            minRightX = i - 29;
            maxRightX = i - 10;
        }
        else
        {
            minLeftX = 10;
            maxLeftX = 29;
            minRightX = 30;
            maxRightX = 49;
        }

        if (top)
        {
            topY = 10.5;
        }
        else
        {
            topY = j - 57;
        }

        bottomY = topY + 46.5;

        float texMod = 0.00390625F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(minLeftX, bottomY, zLevel).tex(66 * texMod, 47 * texMod).endVertex();
        worldRenderer.pos(minLeftX + 9, bottomY, zLevel).tex((66 + 9) * texMod, 47 * texMod).endVertex();
        worldRenderer.pos(minLeftX + 9, topY, zLevel).tex((66 + 9) * texMod, 47 * 2 * texMod).endVertex();
        worldRenderer.pos(minLeftX, topY, zLevel).tex(66 * texMod, 47 * 2 * texMod).endVertex();
        tessellator.draw();

        int pressureLevelScaled = Math.min(Math.max(pressure /2, 1), 45);
        int pressureLevelScaledMax = Math.min(pressureLevelScaled + 2, 45);
        int pressureLevelScaledMin = Math.max(pressureLevelScaledMax - 2, 0);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(minLeftX + 1, bottomY - pressureLevelScaledMin, zLevel).tex(85 * texMod, (48 + 45 - pressureLevelScaled) * texMod).endVertex();
        worldRenderer.pos(minLeftX + 8, bottomY - pressureLevelScaledMin, zLevel).tex((85 + 7) * texMod, (48 + 45 - pressureLevelScaled) * texMod).endVertex();
        worldRenderer.pos(minLeftX + 8, bottomY - pressureLevelScaledMax, zLevel).tex((85 + 7) * texMod, (48 + 45 - pressureLevelScaled) * texMod).endVertex();
        worldRenderer.pos(minLeftX + 1, bottomY - pressureLevelScaledMax, zLevel).tex(85 * texMod, (48 + 45 - pressureLevelScaled) * texMod).endVertex();
        tessellator.draw();
        
        if (invalid)
        {
            String value = GCCoreUtil.translate("gui.warning.high.pressure");
            OverlayPressure.minecraft.fontRendererObj.drawString(value, minLeftX - 20 - OverlayPressure.minecraft.fontRendererObj.getStringWidth(value), (int) bottomY - pressureLevelScaled - OverlayPressure.minecraft.fontRendererObj.FONT_HEIGHT / 2 + 5, ColorUtil.to32BitColor(255, 102, 178, 255));
        }
        GlStateManager.disableBlend();
    }
}
