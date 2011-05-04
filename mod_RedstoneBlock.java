package net.minecraft.src;

import java.util.Random;

public class mod_RedstoneBlock extends BaseMod
{
	public static Block redstoneBlock;
	public static int redstoneBlockId = 123;
	
	public mod_RedstoneBlock()
	{
		redstoneBlock = new BlockRedstone(redstoneBlockId, ModLoader.addOverride("/terrain.png", "/RedstoneBlock.png")).setBlockName("Redstone Block");

		ModLoader.AddName(redstoneBlock, "Redstone Block");

		ModLoader.RegisterBlock(redstoneBlock);

		ModLoader.AddRecipe(new ItemStack(redstoneBlock, 1), new Object[] {
			"###",
			"###",
			"###",
			Character.valueOf('#'), Item.redstone,
		});
	}

	public String Version()
	{
		return "1.5_01";
	}
}
