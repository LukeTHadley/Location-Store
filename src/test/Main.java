package test;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class Main extends JavaPlugin {

    private final File DATA_DIRECTORY_LOCATION = this.getDataFolder();

    private static final String pluginName = "LocationHelper";
    private static final String prefix = ChatColor.GRAY + "[" + pluginName + "]";
    private SavedLocations savedLocations;

    private void messageConsole(String str)
    {
        System.out.println("[ " + pluginName +  "] " + str);
    }

    @Override
    public void onEnable(){
        messageConsole("Starting The Plugin...");
        messageConsole("Checking that data directory exists...");
        if (checkDataFolder()) {
            messageConsole("\tDirectory Exists");
        } else {
            messageConsole("\tDirectory didn't exists; made a new one.");
        }

        messageConsole("Creating SavedLocations Object");
        savedLocations = new SavedLocations(DATA_DIRECTORY_LOCATION);

        //messageConsole("Adding spawnpoint to the saved locations list");
        //savedLocations.add("spawn", this.getServer().getWorld("world").getSpawnLocation());

        //messageConsole("getting the location of 'spawn'");
        //messageConsole(savedLocations.get("spawn").getWorld().toString());

        messageConsole("STARTING LIST ALL");
        String[] all = savedLocations.listAll();
        for (String str : all){
            messageConsole(str);
        }
        messageConsole("ENDING LIST ALL");

    }

    @Override
    public void onDisable(){
        messageConsole("Stopping Plugin...");
        savedLocations.save();
    }




    private boolean checkDataFolder(){
        if (!DATA_DIRECTORY_LOCATION.exists()){
            DATA_DIRECTORY_LOCATION.mkdirs();
            return false;
        }
        return true;
    }





}
