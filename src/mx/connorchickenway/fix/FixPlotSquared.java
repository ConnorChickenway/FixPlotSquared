package mx.connorchickenway.fix;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.plotsquared.bukkit.listeners.PlotPlusListener;

/*
 * <FixPlotSquared - Fix the flag heal and feed on PlotSquared​>
    Copyright (C) <2018>  <ConnorChickenway>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

public class FixPlotSquared extends JavaPlugin implements Listener{

	private HashMap<?, ?> feedHash = null, healHash = null;

	@Override
	public void onEnable() {
		PluginManager manager = Bukkit.getPluginManager();
       		if (!manager.isPluginEnabled("PlotSquared")) {
           		manager.disablePlugin(this);
            		return;
        	}
        	getCommand("fixplotsquared").setExecutor(this);
        	manager.registerEvents(this, this);
        	registerHashMaps();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
    	public void onChangeWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
        	if (feedHash.containsKey(name)) {
            		feedHash.remove(name);
        	}
        	if (healHash.containsKey(name)) {
            		healHash.remove(name);
        	}
    	}
    
    	@EventHandler(priority = EventPriority.LOWEST)
    	public void onPlayerDeath(PlayerDeathEvent event) {
        	Player player = event.getEntity();
        	String name = player.getName();
        	if (feedHash.containsKey(name)) {
        		feedHash.remove(name);
        	}
        	if (healHash.containsKey(name)) {
            	healHash.remove(name);
        	}
    	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("§fPlugin created by §aConnorChickenway");
		return false;
	}
	
	private void registerHashMaps() {
		if(feedHash != null || healHash != null) {
			return;
		}
		String[] fields = {"healRunnable", "feedRunnable"};
		for(int i = 0; i < fields.length; i++) {
			try {
				Field field = PlotPlusListener.class.getDeclaredField(fields[i]);
				field.setAccessible(true);
				if (fields[i].equals("healRunnable")) {
                    			this.healHash = (HashMap<?, ?>)field.get(null);
                		}
               			 else {
                    			this.feedHash = (HashMap<?, ?>)field.get(null);
                		}
			} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
