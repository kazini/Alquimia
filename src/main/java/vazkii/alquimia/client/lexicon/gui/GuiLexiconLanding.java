package vazkii.alquimia.client.lexicon.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import vazkii.alquimia.client.base.PersistentData;
import vazkii.alquimia.client.gui.GuiAdvancementsExt;
import vazkii.alquimia.client.gui.GuiFactory;
import vazkii.alquimia.client.lexicon.LexiconCategory;
import vazkii.alquimia.client.lexicon.LexiconRegistry;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonCategory;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonIndex;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconAdvancements;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconConfig;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconEdit;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconHistory;
import vazkii.alquimia.client.lexicon.gui.button.GuiButtonLexiconResize;
import vazkii.alquimia.common.item.ItemLexicon;
import vazkii.alquimia.common.item.ModItems;

public class GuiLexiconLanding extends GuiLexicon {

	LexiconTextRenderer text;
	int loadedCategories = 0;
	
	@Override
	public void initGui() {
		super.initGui();
		
		text = new LexiconTextRenderer(this, I18n.translateToLocal("alquimia.gui.lexicon.landing_info"), LEFT_PAGE_X, TOP_PADDING + 25);

		int x = bookLeft + 20;
		int y = bookTop + FULL_HEIGHT - 62;
		int dist = 15;
		buttonList.add(new GuiButtonLexiconResize(this, x + dist * 0, y, true));
		buttonList.add(new GuiButtonLexiconHistory(this, x + dist * 1, y));
		buttonList.add(new GuiButtonLexiconAdvancements(this, x + dist * 2, y));
		buttonList.add(new GuiButtonLexiconConfig(this, x + dist * 3, y));
		buttonList.add(new GuiButtonLexiconEdit(this, x + dist * 4, y));
		
		int i = 0;
		List<LexiconCategory> categories = new ArrayList(LexiconRegistry.INSTANCE.categories.values());
		Collections.sort(categories);
		
		for(LexiconCategory category : categories) {
			if(category.getParentCategory() != null)
				continue;
			
			addCategoryButton(i, category);
			i++;
		}
		addCategoryButton(i, null);
		loadedCategories = i + 1;
	}
	
	void addCategoryButton(int i, LexiconCategory category) {
		int x = RIGHT_PAGE_X + 10 + (i % 4) * 24;
		int y = TOP_PADDING + 25 + (i /4) * 24;
		
		if(category == null)
			buttonList.add(new GuiButtonIndex(this, x, y));	
		else buttonList.add(new GuiButtonCategory(this, x, y, category));
	}
	
	@Override
	void drawForegroundElements(int mouseX, int mouseY, float partialTicks) {
		text.render(mouseX, mouseY);
		
		drawCenteredStringNoShadow(I18n.translateToLocal("alquimia.gui.lexicon.categories"), RIGHT_PAGE_X + PAGE_WIDTH / 2, TOP_PADDING, 0x333333);
		
		int topSeparator = TOP_PADDING + 12;
		int bottomSeparator = topSeparator + 25 + 24 * (loadedCategories / 4 + 1);
		
		drawHeader();
		drawSeparator(RIGHT_PAGE_X, topSeparator);
		drawSeparator(RIGHT_PAGE_X, bottomSeparator);
		
		if(LexiconRegistry.INSTANCE.isErrored())
			drawCenteredStringNoShadow(I18n.translateToLocal("alquimia.gui.lexicon.loading_error"), RIGHT_PAGE_X + PAGE_WIDTH / 2, bottomSeparator + 12, 0xFF0000);
		
		drawProgressBar(mouseX, mouseY, (e) -> true);
	}
	
	void drawHeader() {
		GlStateManager.color(1F, 1F, 1F, 1F);
		drawFromTexture(-8, 12, 0, 180, 140, 31);

		int color = 0xFFDD00;
		boolean unicode = fontRenderer.getUnicodeFlag();
		fontRenderer.drawString(new ItemStack(ModItems.lexicon).getDisplayName(), 13, 16, color);
		fontRenderer.setUnicodeFlag(true);
		fontRenderer.drawString(ItemLexicon.getEdition(), 24, 24, color);
		fontRenderer.setUnicodeFlag(unicode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		text.click(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if(button instanceof GuiButtonIndex)
			displayLexiconGui(new GuiLexiconIndex(), true);
		else if(button instanceof GuiButtonCategory)
			displayLexiconGui(new GuiLexiconCategory(((GuiButtonCategory) button).getCategory()), true);
		else if(button instanceof GuiButtonLexiconHistory)
			displayLexiconGui(new GuiLexiconHistory(), true);
		else if(button instanceof GuiButtonLexiconConfig)
			mc.displayGuiScreen(new GuiFactory.GuiAlquimiaConfig(this));
		else if(button instanceof GuiButtonLexiconAdvancements)
			mc.displayGuiScreen(new GuiAdvancementsExt(mc.player.connection.getAdvancementManager(), this));
		else if(button instanceof GuiButtonLexiconEdit) {
			if(isCtrlKeyDown()) {
				long time = System.currentTimeMillis();
				LexiconRegistry.INSTANCE.reloadLexiconRegistry();
				displayLexiconGui(new GuiLexiconLanding(), false);
				mc.player.sendMessage(new TextComponentTranslation("alquimia.gui.lexicon.reloaded", (System.currentTimeMillis() - time)));
			} else displayLexiconGui(new GuiLexiconWriter(), true);
		} else if(button instanceof GuiButtonLexiconResize) {
			if(PersistentData.data.lexiconGuiScale >= maxScale)
				PersistentData.data.lexiconGuiScale = 2;
			else PersistentData.data.lexiconGuiScale = Math.max(2, PersistentData.data.lexiconGuiScale + 1);

			PersistentData.save();
			displayLexiconGui(this, false);
		}
	}

}
