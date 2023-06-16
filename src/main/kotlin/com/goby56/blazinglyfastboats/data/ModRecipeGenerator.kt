package com.goby56.blazinglyfastboats.data

import com.goby56.blazinglyfastboats.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.util.Identifier
import java.util.function.Consumer

class ModRecipeGenerator(output: FabricDataOutput?) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>?) {
//        offerChestBoatRecipe(exporter, Items.ACACIA_CHEST_BOAT, Items.ACACIA_BOAT)
//        offerChestBoatRecipe(exporter, Items.BIRCH_CHEST_BOAT, Items.BIRCH_BOAT)
//        offerChestBoatRecipe(exporter, Items.DARK_OAK_CHEST_BOAT, Items.DARK_OAK_BOAT)
//        offerChestBoatRecipe(exporter, Items.JUNGLE_CHEST_BOAT, Items.JUNGLE_BOAT)
//        offerChestBoatRecipe(exporter, Items.OAK_CHEST_BOAT, Items.OAK_BOAT)
//        offerChestBoatRecipe(exporter, Items.SPRUCE_CHEST_BOAT, Items.SPRUCE_BOAT)
//        offerChestBoatRecipe(exporter, Items.MANGROVE_CHEST_BOAT, Items.MANGROVE_BOAT)
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.MOTORBOAT)
            .pattern("RF")
            .pattern("PB")
            .input(Character.valueOf('R'), Items.REDSTONE)
            .input(Character.valueOf('F'), Items.FURNACE)
            .input(Character.valueOf('P'), Items.PISTON)
            .input(Character.valueOf('B'), Items.ACACIA_BOAT)
            .offerTo(exporter, Identifier(FabricRecipeProvider.getRecipeName(ModItems.MOTORBOAT)))
    }

}