package vazkii.alquimia.client.lexicon;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.alquimia.common.lib.LibMisc;
import vazkii.alquimia.common.util.ItemStackUtils;
import vazkii.alquimia.common.util.ItemStackUtils.StackWrapper;

public class LexiconEntry implements Comparable<LexiconEntry> {

	String name, icon, category;
	boolean priority = false;
	LexiconPage[] pages;
	String[] relations;
	
	transient LexiconCategory lcategory = null;
	transient ItemStack iconItem = null;
	transient List<StackWrapper> relevantStacks = new LinkedList();
	
	public String getName() {
		return name;
	}

	public LexiconPage[] getPages() {
		return pages;
	}
	
	public String[] getRelations() {
		return relations;
	}
	
	public boolean isPriority() {
		return priority;
	}
	
	public ItemStack getIconItem() {
		if(iconItem == null)
			iconItem = ItemStackUtils.loadStackFromString(icon);
		
		return iconItem;
	}
	
	public LexiconCategory getCategory() {
		if(lcategory == null) {
			if(category.contains(":"))
				lcategory = LexiconRegistry.INSTANCE.categories.get(new ResourceLocation(category));
			else lcategory = LexiconRegistry.INSTANCE.categories.get(new ResourceLocation(LibMisc.MOD_ID, category));
		}
		
		return lcategory;
	}
	
	@Override
	public int compareTo(LexiconEntry o) {
		if(o.priority != this.priority)
			return this.priority ? -1 : 1;
		
		return this.name.compareTo(o.name);
	}
	
	public void build() {
		for(int i = 0; i < pages.length; i++)
			pages[i].build(this, i);
	}
	
	public void addRelevantStack(ItemStack stack, int page) {
		StackWrapper wrapper = ItemStackUtils.wrapStack(stack);
		relevantStacks.add(wrapper);
		
		if(!LexiconRegistry.INSTANCE.recipeMappings.containsKey(wrapper))
			LexiconRegistry.INSTANCE.recipeMappings.put(wrapper, Pair.of(this, page / 2));
	}
	
	public boolean isStackRelevant(ItemStack stack) {
		return relevantStacks.contains(ItemStackUtils.wrapStack(stack));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LexiconEntry[");
		builder.append(name);
		builder.append(" / Pages:");
		builder.append(pages.length);
		for(LexiconPage page : pages) {
			builder.append(" ");
			if(page == null)
				builder.append("NULL");
			else builder.append(page.type);
		}
		builder.append("]");
		
		return builder.toString();
	}

}
