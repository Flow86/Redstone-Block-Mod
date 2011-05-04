package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.util.Random;

public class BlockRedstone extends Block
{
	public boolean blockIsPowered = false;
	
	public BlockRedstone(int i, int j)
	{
		super(i,j, Material.ground);
		setTickOnLoad(true);
	}

	public boolean canProvidePower()
	{
		return false;
	}
	
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		System.out.printf("isPoweringTo: %d/%d/%d #%d\n", i, j, k, l);
		System.out.printf("isPowered: %s\n", blockIsPowered ? "yes" : "no");
		
		return blockIsPowered;
	}
	
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
	{
		System.out.printf("isIndirectlyPoweringTo: %d/%d/%d #%d\n", i, j, k, l);

		return isPoweringTo(world, i, j, k, l);
	}
	
	public int idDropped(int i, Random random)
	{
		//System.out.printf("idDropped: %d\n", i);
		return blockID;
	}

	public int tickRate()
	{
		return 2;
	}
	
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		updatePowerState(world, i, j, k);
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		System.out.printf("onNeighborBlockChange: %d/%d/%d #%d\n", i, j, k, l);
		if(world.multiplayerWorld)
			return;
		
		//updatePowerState(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());

		super.onNeighborBlockChange(world, i, j, k, l);
	}
	
	public void updatePowerState(World world, int i, int j, int k)
	{
		boolean prevBlockIsPowered = blockIsPowered;
		
		System.out.printf("updatePowerState: %d/%d/%d #%d\n", i, j, k, world.getBlockId(i, j, k));
		
		blockIsPowered = false;
		blockIsPowered = ( world.isBlockIndirectlyGettingPowered(i, j, k) && 
				   (world.getBlockId(i - 1, j, k) > 0 && Block.blocksList[world.getBlockId(i - 1, j, k)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i - 1, j, k)) || 
				   (world.getBlockId(i + 1, j, k) > 0 && Block.blocksList[world.getBlockId(i + 1, j, k)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i + 1, j, k)) || 
				   (world.getBlockId(i, j, k + 1) > 0 && Block.blocksList[world.getBlockId(i, j, k + 1)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j, k + 1)) || 
				   (world.getBlockId(i, j, k - 1) > 0 && Block.blocksList[world.getBlockId(i, j, k - 1)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j, k - 1)) || 
				   (world.getBlockId(i, j + 1, k) > 0 && Block.blocksList[world.getBlockId(i, j + 1, k)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j + 1, k)) || 
				   (world.getBlockId(i, j - 1, k) > 0 && Block.blocksList[world.getBlockId(i, j - 1, k)].canProvidePower() && world.isBlockIndirectlyGettingPowered(i, j - 1, k))
				);
		
		if(prevBlockIsPowered != blockIsPowered)
			notifyBlocks(world, i, j, k);
			
		System.out.printf("new blockIsPowered: %s\n", blockIsPowered ? "on" : "off");
	}
	
	public void notifyBlocks(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);

		if(!world.isBlockOpaqueCube(i, j + 1, k))
		{
			if(world.getBlockId(i + 1, j + 1, k) == Block.redstoneWire.blockID)
				world.notifyBlocksOfNeighborChange(i + 1, j + 1, k, blockID);
			if(world.getBlockId(i - 1, j + 1, k) == Block.redstoneWire.blockID)
				world.notifyBlocksOfNeighborChange(i - 1, j + 1, k, blockID);
			if(world.getBlockId(i + 1, j + 1, k + 1) == Block.redstoneWire.blockID)
				world.notifyBlocksOfNeighborChange(i, j + 1, k + 1, blockID);
			if(world.getBlockId(i + 1, j + 1, k - 1) == Block.redstoneWire.blockID)
				world.notifyBlocksOfNeighborChange(i, j + 1, k - 1, blockID);
		}
		else
			world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);

		/*if(!world.isBlockOpaqueCube(i, j - 1, k))
		{
			world.notifyBlocksOfNeighborChange(i + 1, j - 1, k, blockID);
			world.notifyBlocksOfNeighborChange(i - 1, j - 1, k, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k + 1, blockID);
			world.notifyBlocksOfNeighborChange(i, j - 1, k - 1, blockID);
		}
		else*/
			world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
	}
	
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		if(blockIsPowered)
		{
			double d = 0.0625D;
			for(int l = 0; l < 6; l++)
			{
				double d1 = (float)i + random.nextFloat();
				double d2 = (float)j + random.nextFloat();
				double d3 = (float)k + random.nextFloat();
				if(l == 0 && !world.isBlockOpaqueCube(i, j + 1, k))
				{
					d2 = (double)(j + 1) + d;
				}
				if(l == 1 && !world.isBlockOpaqueCube(i, j - 1, k))
				{
					d2 = (double)(j + 0) - d;
				}
				if(l == 2 && !world.isBlockOpaqueCube(i, j, k + 1))
				{
					d3 = (double)(k + 1) + d;
				}
				if(l == 3 && !world.isBlockOpaqueCube(i, j, k - 1))
				{
					d3 = (double)(k + 0) - d;
				}
				if(l == 4 && !world.isBlockOpaqueCube(i + 1, j, k))
				{
					d1 = (double)(i + 1) + d;
				}
				if(l == 5 && !world.isBlockOpaqueCube(i - 1, j, k))
				{
					d1 = (double)(i + 0) - d;
				}
				if(d1 < (double)i || d1 > (double)(i + 1) || d2 < 0.0D || d2 > (double)(j + 1) || d3 < (double)k || d3 > (double)(k + 1))
				{
					world.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
		
	public void onBlockAdded(World world, int i, int j, int k)
	{
		System.out.printf("onBlockAdded: %d/%d/%d\n", i, j, k);
		super.onBlockAdded(world, i, j, k);

		if(world.multiplayerWorld)
			return;

		updatePowerState(world, i, j, k);
		//notifyBlocks(world, i, j, k);
	}
	
	public void onBlockRemoval(World world, int i, int j, int k)
	{
		System.out.printf("onBlockRemoval: %d/%d/%d\n", i, j, k);

		notifyBlocks(world, i, j, k);
	}
}
