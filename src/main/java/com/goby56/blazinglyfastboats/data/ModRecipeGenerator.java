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
                .pattern("RF")
                .pattern("PB")
                .input('R', Items.REDSTONE)
                .input('F', Items.FURNACE)
                .input('P', Items.PISTON)
                .input('B', Items.ACACIA_BOAT)
                .criterion(FabricRecipeProvider.hasItem(Items.PISTON), FabricRecipeProvider.conditionsFromItem(Items.PISTON))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(ModItems.MOTORBOAT)));
    }

}
