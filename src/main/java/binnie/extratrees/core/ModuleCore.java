package binnie.extratrees.core;

import binnie.Binnie;
import binnie.Constants;
import binnie.core.Mods;
import binnie.core.item.ItemMisc;
import binnie.core.liquid.ManagerLiquid;
import binnie.extratrees.ExtraTrees;
import binnie.extratrees.block.BlockHops;
import binnie.extratrees.item.*;
import binnie.extratrees.modules.ExtraTreesModuleUIDs;
import binnie.modules.BinnieModule;
import binnie.modules.Module;
import forestry.api.core.Tabs;
import forestry.api.fuels.EngineBronzeFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.recipes.ICarpenterManager;
import forestry.api.recipes.IStillManager;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@BinnieModule(moduleID = ExtraTreesModuleUIDs.CORE, moduleContainerID = Constants.EXTRA_TREES_MOD_ID, coreModule = true, name = "Core", unlocalizedDescription = "extratrees.module.core")
public class ModuleCore extends Module {
	public static ItemMisc itemMisc;
	public static Item itemFood;
	public static Item itemHammer;
	public static Item itemDurableHammer;
	public static Item itemHops;

	public static BlockHops hops;

	@Override
	public void registerItemsAndBlocks() {
		itemMisc = new ItemMisc(Tabs.tabArboriculture, ExtraTreeItems.values());
		ExtraTrees.proxy.registerItem(itemMisc);

		Binnie.LIQUID.createLiquids(ExtraTreeLiquid.values());
		itemFood = new ItemETFood();
		ExtraTrees.proxy.registerItem(itemFood);

		itemHammer = new ItemHammer(false);
		ExtraTrees.proxy.registerItem(itemHammer);
		itemDurableHammer = new ItemHammer(true);
		ExtraTrees.proxy.registerItem(itemDurableHammer);

		hops = new BlockHops();
		ExtraTrees.proxy.registerBlock(hops);

		itemHops = new ItemHops(hops, Blocks.FARMLAND);
		ExtraTrees.proxy.registerItem(itemHops);

		OreDictionary.registerOre("pulpWood", ExtraTreeItems.Sawdust.get(1));
		Food.registerOreDictionary();
		OreDictionary.registerOre("cropApple", Items.APPLE);
		OreDictionary.registerOre("cropHops", itemHops);
		OreDictionary.registerOre("seedWheat", Items.WHEAT_SEEDS);
		OreDictionary.registerOre("seedWheat", ExtraTreeItems.GrainWheat.get(1));
		OreDictionary.registerOre("seedBarley", ExtraTreeItems.GrainBarley.get(1));
		OreDictionary.registerOre("seedCorn", ExtraTreeItems.GrainCorn.get(1));
		OreDictionary.registerOre("seedRye", ExtraTreeItems.GrainRye.get(1));
		OreDictionary.registerOre("seedRoasted", ExtraTreeItems.GrainRoasted.get(1));

		OreDictionary.registerOre("gearWood", ExtraTreeItems.ProvenGear.get(1));
	}

	@Override
	public void preInit() {
	}

	@Override
	public void init() {
		Food.CRABAPPLE.addJuice(10, 150, 10);
		Food.ORANGE.addJuice(10, 400, 15);
		Food.KUMQUAT.addJuice(10, 300, 10);
		Food.LIME.addJuice(10, 300, 10);
		Food.WILD_CHERRY.addOil(20, 50, 5);
		Food.SOUR_CHERRY.addOil(20, 50, 3);
		Food.BLACK_CHERRY.addOil(20, 50, 5);
		Food.Blackthorn.addJuice(10, 50, 5);
		Food.CHERRY_PLUM.addJuice(10, 100, 60);
		Food.ALMOND.addOil(20, 80, 5);
		Food.APRICOT.addJuice(10, 150, 40);
		Food.GRAPEFRUIT.addJuice(10, 500, 15);
		Food.PEACH.addJuice(10, 150, 40);
		Food.SATSUMA.addJuice(10, 300, 10);
		Food.BUDDHA_HAND.addJuice(10, 400, 15);
		Food.CITRON.addJuice(10, 400, 15);
		Food.FINGER_LIME.addJuice(10, 300, 10);
		Food.KEY_LIME.addJuice(10, 300, 10);
		Food.MANDERIN.addJuice(10, 400, 10);
		Food.NECTARINE.addJuice(10, 150, 40);
		Food.POMELO.addJuice(10, 300, 10);
		Food.TANGERINE.addJuice(10, 300, 10);
		Food.PEAR.addJuice(10, 300, 20);
		Food.SAND_PEAR.addJuice(10, 200, 10);
		Food.HAZELNUT.addOil(20, 150, 5);
		Food.BUTTERNUT.addOil(20, 180, 5);
		Food.BEECHNUT.addOil(20, 100, 4);
		Food.PECAN.addOil(29, 50, 2);
		Food.BANANA.addJuice(10, 100, 30);
		Food.RED_BANANA.addJuice(10, 100, 30);
		Food.PLANTAIN.addJuice(10, 100, 40);
		Food.BRAZIL_NUT.addOil(20, 20, 2);
		Food.FIG.addOil(20, 50, 3);
		Food.ACORN.addOil(20, 50, 3);
		Food.ELDERBERRY.addJuice(10, 100, 5);
		Food.OLIVE.addOil(20, 50, 3);
		Food.GINGKO_NUT.addOil(20, 50, 5);
		Food.COFFEE.addOil(15, 20, 2);
		Food.OSANGE_ORANGE.addJuice(10, 300, 15);
		Food.CLOVE.addOil(10, 25, 2);
		Food.COCONUT.addOil(20, 300, 25);
		Food.CASHEW.addJuice(10, 150, 15);
		Food.AVACADO.addJuice(10, 300, 15);
		Food.NUTMEG.addOil(20, 50, 10);
		Food.ALLSPICE.addOil(20, 50, 10);
		Food.CHILLI.addJuice(10, 100, 10);
		Food.STAR_ANISE.addOil(20, 100, 10);
		Food.MANGO.addJuice(10, 400, 20);
		Food.STARFRUIT.addJuice(10, 300, 10);
		Food.CANDLENUT.addJuice(20, 50, 10);
	}

	@Override
	public void postInit() {
		MinecraftForge.addGrassSeed(new ItemStack(itemHops), 5);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemDurableHammer, 1, 0), "wiw", " s ", " s ", 'w', Blocks.OBSIDIAN, 'i', Items.GOLD_INGOT, 's', Items.STICK));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemHammer, 1, 0), "wiw", " s ", " s ", 'w', "plankWood", 'i', Items.IRON_INGOT, 's', Items.STICK));
		GameRegistry.addRecipe(new ShapedOreRecipe(ExtraTreeItems.Yeast.get(8), " m ", "mbm", 'b', Items.BREAD, 'm', Blocks.BROWN_MUSHROOM));
		GameRegistry.addRecipe(new ShapedOreRecipe(ExtraTreeItems.LagerYeast.get(8), "mbm", " m ", 'b', Items.BREAD, 'm', Blocks.BROWN_MUSHROOM));
		GameRegistry.addRecipe(ExtraTreeItems.GrainWheat.get(5), " s ", "sss", " s ", 's', Items.WHEAT_SEEDS);
		GameRegistry.addRecipe(new ShapedOreRecipe(ExtraTreeItems.GrainBarley.get(3), false, " s ", "s  ", " s ", 's', ExtraTreeItems.GrainWheat.get(1)));
		GameRegistry.addRecipe(new ShapedOreRecipe(ExtraTreeItems.GrainCorn.get(3), false, " s ", "  s", " s ", 's', ExtraTreeItems.GrainWheat.get(1)));
		GameRegistry.addRecipe(ExtraTreeItems.GrainRye.get(3), "   ", "s s", " s ", 's', ExtraTreeItems.GrainWheat.get(1));
		GameRegistry.addRecipe(ExtraTreeItems.ProvenGear.get(1), " s ", "s s", " s ", 's', Mods.Forestry.stack("oakStick"));
		GameRegistry.addRecipe(ExtraTreeItems.GlassFitting.get(6), "s s", " i ", "s s", 'i', Items.IRON_INGOT, 's', Items.STICK);
		GameRegistry.addSmelting(ExtraTreeItems.GrainWheat.get(1), ExtraTreeItems.GrainRoasted.get(1), 0.0f);
		GameRegistry.addSmelting(ExtraTreeItems.GrainRye.get(1), ExtraTreeItems.GrainRoasted.get(1), 0.0f);
		GameRegistry.addSmelting(ExtraTreeItems.GrainCorn.get(1), ExtraTreeItems.GrainRoasted.get(1), 0.0f);
		GameRegistry.addSmelting(ExtraTreeItems.GrainBarley.get(1), ExtraTreeItems.GrainRoasted.get(1), 0.0f);
		try {
			final Item minium = (Item) Class.forName("com.pahimar.ee3.lib.ItemIds").getField("minium_shard").get(null);
			GameRegistry.addRecipe(new ShapelessOreRecipe(Food.PAPAYIMAR.get(1), minium, "cropPapaya"));
		} catch (Exception ignored) {
		}
		ICarpenterManager carpenterManager = RecipeManagers.carpenterManager;
		IStillManager stillManager = RecipeManagers.stillManager;
		stillManager.addRecipe(25, ExtraTreeLiquid.Resin.get(5), ExtraTreeLiquid.Turpentine.get(3));
		carpenterManager.addRecipe(25, ExtraTreeLiquid.Turpentine.get(50), null, itemMisc.getStack(ExtraTreeItems.WoodWax, 4), "x", 'x', Mods.Forestry.stack("beeswax"));
		FluidStack creosoteOil = Binnie.LIQUID.getFluidStack(ManagerLiquid.CREOSOTE, 50);
		if (creosoteOil != null) {
			carpenterManager.addRecipe(25, creosoteOil, null, itemMisc.getStack(ExtraTreeItems.WoodWax, 1), "x", 'x', Mods.Forestry.stack("beeswax"));
		}

		FuelManager.bronzeEngineFuel.put(ExtraTreeLiquid.Sap.get(1).getFluid(), new EngineBronzeFuel(ExtraTreeLiquid.Sap.get(1).getFluid(), 20, 10000, 1));
		FuelManager.bronzeEngineFuel.put(ExtraTreeLiquid.Resin.get(1).getFluid(), new EngineBronzeFuel(ExtraTreeLiquid.Resin.get(1).getFluid(), 30, 10000, 1));
	/*	if (BinnieCore.getBinnieProxy().isDebug()) {
			try {
				final PrintWriter outputSpecies = new PrintWriter(new FileWriter("data/species.html"));
				final PrintWriter outputLogs = new PrintWriter(new FileWriter("data/logs.html"));
				final PrintWriter outputPlanks = new PrintWriter(new FileWriter("data/planks.html"));
				final PrintWriter outputFruit = new PrintWriter(new FileWriter("data/fruit.html"));
				final PrintWriter outputDesigns = new PrintWriter(new FileWriter("data/designs.html"));
				final Queue<IAlleleTreeSpecies> speciesQueue = new LinkedList<>();
				for (final ETTreeDefinition s : ETTreeDefinition.values()) {
					speciesQueue.add(s.getSpecies());
				}
				final Queue<IWoodType> logQueue = new LinkedList<>();
				Collections.addAll(logQueue, EnumETLog.values());
				final Queue<IDesignMaterial> plankQueue = new LinkedList<>();
				Collections.addAll(plankQueue, PlankType.ExtraTreePlanks.values());
				final Queue<AlleleETFruit> fruitQueue = new LinkedList<>();
				fruitQueue.addAll(AlleleETFruit.values());
				final Queue<IDesign> designQueue = new LinkedList<>();
				designQueue.addAll(CarpentryManager.carpentryInterface.getSortedDesigns());
				fruitQueue.remove(AlleleETFruit.Apple);
				outputSpecies.println("<table style=\"width: 100%;\">");
				while (!speciesQueue.isEmpty()) {
					outputSpecies.println("<tr>");
					for (int i = 0; i < 4; ++i) {
						final IAlleleTreeSpecies species = speciesQueue.poll();
						outputSpecies.println("<td>" + ((species == null) ? "" : species.getAlleleName()) + "</td>");
					}
					outputSpecies.println("</tr>");
				}
				outputSpecies.println("</table>");
				outputLogs.println("<table style=\"width: 100%;\">");
				while (!logQueue.isEmpty()) {
					outputLogs.println("<tr>");
					for (int i = 0; i < 4; ++i) {
						final EnumETLog wood5 = (EnumETLog) logQueue.poll();
						if (wood5 == null) {
							outputLogs.println("<td></td>");
						} else {
							final String img = "<img alt=\"" + wood5.getName() + "\" src=\"images/logs/" + wood5.toString().toLowerCase() + "Bark.png\">";
							outputLogs.println("<td>" + img + " " + wood5.getName() + "</td>");
						}
					}
					outputLogs.println("</tr>");
				}
				outputLogs.println("</table>");
				outputPlanks.println("<table style=\"width: 100%;\">");
				while (!plankQueue.isEmpty()) {
					outputPlanks.println("<tr>");
					for (int i = 0; i < 4; ++i) {
						final IDesignMaterial wood2 = plankQueue.poll();
						if (wood2 == null) {
							outputPlanks.println("<td></td>");
						} else {
							final String img = "<img alt=\"" + wood2.getDesignMaterialName() + "\" src=\"images/planks/" + wood2.getDesignMaterialName() + ".png\">";
							outputPlanks.println("<td>" + img + " " + wood2.getDesignMaterialName() + "</td>");
						}
					}
					outputPlanks.println("</tr>");
				}
				outputPlanks.println("</table>");
				outputFruit.println("<table style=\"width: 100%;\">");
				while (!fruitQueue.isEmpty()) {
					outputFruit.println("<tr>");
					for (int i = 0; i < 4; ++i) {
						final AlleleETFruit wood6 = fruitQueue.poll();
						if (wood6 == null) {
							outputFruit.println("<td></td>");
						} else {
							final String fruit = wood6.getNameOfFruit();
							final String img2 = "<img alt=\"" + wood6.getAlleleName() + "\" src=\"images/fruits/" + fruit + ".png\">";
							outputFruit.println("<td>" + img2 + " " + wood6.getAlleleName() + "</td>");
						}
					}
					outputFruit.println("</tr>");
				}
				outputFruit.println("</table>");
				outputDesigns.println("<table style=\"width: 100%;\">");
				while (!designQueue.isEmpty()) {
					outputDesigns.println("<tr>");
					for (int i = 0; i < 4; ++i) {
						final IDesign wood4 = designQueue.poll();
						if (wood4 == null) {
							outputDesigns.println("<td></td>");
						} else {
							final String texture = wood4.getTopPattern().getPattern().toString().toLowerCase();
							final String img2 = "<img alt=\"" + texture + "\" src=\"images/pattern/" + texture + ".png\">";
							outputDesigns.println("<td>" + img2 + " " + wood4.getName() + "</td>");
						}
					}
					outputDesigns.println("</tr>");
				}
				outputDesigns.println("</table>");
				outputSpecies.close();
				outputLogs.close();
				outputPlanks.close();
				outputFruit.close();
				outputDesigns.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	@Override
	public boolean canBeDisabled() {
		return false;
	}
}
