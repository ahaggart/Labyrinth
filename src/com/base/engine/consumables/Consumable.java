package com.base.engine.consumables;
import com.base.engine.world.Cell;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public interface Consumable 
{	
	public void use(World world, Player player, Cell occupied);
}
