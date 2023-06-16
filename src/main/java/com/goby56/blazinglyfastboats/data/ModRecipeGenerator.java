package com.goby56.blazinglyfastboats.data;

import com.goby56.blazinglyfastboats.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.MOTORBOAT)
                .pattern("IFI")
                .pattern("PII")
                .input('I', Items.IRON_INGOT)
                .input('F', Items.FURNACE)
                .input('P', Items.PISTON)
                .criterion(FabricRecipeProvider.hasItem(Items.PISTON), FabricRecipeProvider.conditionsFromItem(Items.PISTON))
                .criterion(FabricRecipeProvider.hasItem(Items.BLAST_FURNACE), FabricRecipeProvider.conditionsFromItem(Items.BLAST_FURNACE))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(ModItems.MOTORBOAT)));
    }

}
