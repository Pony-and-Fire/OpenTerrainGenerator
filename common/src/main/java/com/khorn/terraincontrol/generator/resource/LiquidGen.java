package com.khorn.terraincontrol.generator.resource;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.exception.InvalidConfigException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiquidGen extends Resource
{
    private List<Integer> sourceBlocks;
    private int minAltitude;
    private int maxAltitude;

    @Override
    public void spawn(LocalWorld world, Random rand, boolean villageInChunk, int x, int z)
    {
        int y = rand.nextInt(maxAltitude - minAltitude) + minAltitude;

        if (!sourceBlocks.contains(world.getTypeId(x, y + 1, z)))
            return;
        if (!sourceBlocks.contains(world.getTypeId(x, y - 1, z)))
            return;

        if ((world.getTypeId(x, y, z) != 0) && (!sourceBlocks.contains(world.getTypeId(x, y, z))))
            return;

        int i = 0;
        int j = 0;

        int tempBlock = world.getTypeId(x - 1, y, z);

        i = (sourceBlocks.contains(tempBlock)) ? i + 1 : i;
        j = (tempBlock == 0) ? j + 1 : j;

        tempBlock = world.getTypeId(x + 1, y, z);

        i = (sourceBlocks.contains(tempBlock)) ? i + 1 : i;
        j = (tempBlock == 0) ? j + 1 : j;

        tempBlock = world.getTypeId(x, y, z - 1);

        i = (sourceBlocks.contains(tempBlock)) ? i + 1 : i;
        j = (tempBlock == 0) ? j + 1 : j;

        tempBlock = world.getTypeId(x, y, z + 1);

        i = (sourceBlocks.contains(tempBlock)) ? i + 1 : i;
        j = (tempBlock == 0) ? j + 1 : j;

        if ((i == 3) && (j == 1))
        {
            world.setBlock(x, y, z, blockId, 0, true, true, true);
            // this.world.f = true;
            // Block.byId[res.BlockId].a(this.world, x, y, z, this.rand);
            // this.world.f = false;
        }
    }

    @Override
    public void load(List<String> args) throws InvalidConfigException
    {
        assureSize(6, args);

        blockId = readBlockId(args.get(0));
        blockData = readBlockData(args.get(0));
        frequency = readInt(args.get(1), 1, 5000);
        rarity = readRarity(args.get(2));
        minAltitude = readInt(args.get(3), TerrainControl.worldDepth, TerrainControl.worldHeight);
        maxAltitude = readInt(args.get(4), minAltitude + 1, TerrainControl.worldHeight);
        sourceBlocks = new ArrayList<Integer>();
        for (int i = 5; i < args.size(); i++)
        {
            sourceBlocks.add(readBlockId(args.get(i)));
        }
    }

    @Override
    public String makeString()
    {
        return "Liquid(" + makeMaterial(blockId, blockData) + "," + frequency + "," + rarity + "," + minAltitude + "," + maxAltitude + makeMaterial(sourceBlocks) + ")";
    }

    @Override
    public boolean isAnalogousTo(Resource other)
    {
        return getClass() == other.getClass() && other.blockId == this.blockId && other.blockData == this.blockData;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + super.hashCode();
        hash = 17 * hash + this.minAltitude;
        hash = 17 * hash + this.maxAltitude;
        hash = 17 * hash + (this.sourceBlocks != null ? this.sourceBlocks.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other)
    {
        if (!super.equals(other))
            return false;
        if (other == null)
            return false;
        if (other == this)
            return true;
        if (getClass() != other.getClass())
            return false;
        final LiquidGen compare = (LiquidGen) other;
        return this.minAltitude == compare.minAltitude
               && this.maxAltitude == compare.maxAltitude
               && (this.sourceBlocks == null ? this.sourceBlocks == compare.sourceBlocks
                   : this.sourceBlocks.equals(compare.sourceBlocks));
    }

}