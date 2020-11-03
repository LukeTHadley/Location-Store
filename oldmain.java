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

public class Main extends JavaPlugin implements Listener {

    private final File DATA_DIRECTORY_LOCATION = this.getDataFolder();

    private static final String pluginName = "LocationHelper";
    private static final String prefix = ChatColor.GRAY + "[" + pluginName + "]";
    private SavedLocations savedLocations;

    private void messageConsole(String str)
    {
        System.out.println("[ " + pluginName +  "] " + str);
    }

    private void messagePlayer(CommandSender player, String message)
    {
        player.sendMessage(prefix + message);
    }

    private void messageServer(String message)
    {
        getServer().broadcastMessage(message);
    }

    @Override
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this);
        messageConsole("Starting Plugin...");
        messageConsole("Checking that data directory exists...");
        if (checkDataFolder()) {
            messageConsole("\tDirectory Exists");
        } else {
            messageConsole("\tDirectory didn't exists; made a new one.");
        }

        messageConsole("DOING A TEXT FILE THINGY");
        savedLocations = new SavedLocations(DATA_DIRECTORY_LOCATION);


    }

    @Override
    public void onDisable(){
        messageConsole("Stopping Plugin...");
        savedLocations.save();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.getPlayer().sendMessage(ChatColor.GRAY + prefix + " Do '/local' to display your current co-ords in chat.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player){
            Player commandSender = this.getServer().getPlayer(sender.getName());
            Location playersLocation = commandSender.getLocation();
            if (command.getName().equalsIgnoreCase("locationhelper") || command.getName().equalsIgnoreCase("local") || command.getName().equalsIgnoreCase("here")) { //LocationHelper command called
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("help")) { //Help Command
                        helpCommand(commandSender);
                    }
                    if (this.getServer().getPlayer(args[0]) != null && this.getServer().getPlayer(args[0]).getName().equalsIgnoreCase(args[0])) {
                        Player targetPlayer = this.getServer().getPlayer(args[0]);
                        String currentLocationString = ChatColor.GRAY + targetPlayer.getDisplayName() + "> World: " + targetPlayer.getWorld().getName() + ", X: " + targetPlayer.getLocation().getBlockX() + ", Y: " + targetPlayer.getLocation().getBlockY() + ", Z: " + targetPlayer.getLocation().getBlockZ() + ".";
                        messageServer(currentLocationString);
                    }
                    if (args[0].equalsIgnoreCase("here")){ //Display the current players location to the server
                        String currentLocationString = ChatColor.GRAY + commandSender.getDisplayName() + "> World: " + playersLocation.getWorld().getName() + ", X: " + playersLocation.getBlockX() + ", Y: " + playersLocation.getBlockY() + ", Z: " + playersLocation.getBlockZ() + ".";
                        messageServer(currentLocationString);
                    }
                    if (args[0].equalsIgnoreCase("list")){ //Lists all saved locations from the list.
                        messageServer("STARTING LIST");
                        String[] locations = savedLocations.listAll();
                        for (String str : locations){
                            sender.sendMessage(ChatColor.GRAY + str);
                        }
                        messageServer("ENDING LIST");
                    }
                    if (args.length > 1) {
                        if (args[0].equalsIgnoreCase("add")){ //Add the players location to a list given a name
                            messageServer("STARTING ADD");
                            if (savedLocations.add(args[1], playersLocation)){
                                messagePlayer(commandSender,"Successfully added '" + args[1] + "' to the list.");
                            }
                            else {
                                messagePlayer(commandSender,"Could not add '" + args[1] + "' as the key is already being used.");
                            }
                            messageServer("ENDING ADD");
                        }
                        if (args[0].equalsIgnoreCase("remove")){ //Remove a location from the list given a valid index
                            if (savedLocations.remove(args[1])){
                                messagePlayer(commandSender,"Successfully removed '" + args[1] + "' from the list.");
                            }
                            else {
                                messagePlayer(commandSender,"Could not remove '" + args[1] + "' from the list as it is not a valid key.");
                            }
                        }
                        if (args[0].equalsIgnoreCase("get")) { //Remove a location from the list given a valid index
                            Location retrieved = savedLocations.get(args[1]);
                            messagePlayer(sender, ChatColor.GRAY + args[1] + "> World: " + retrieved.getWorld().getName() + ", X: " + retrieved.getBlockX() + ", Y: " + retrieved.getBlockY() + ", Z: " + retrieved.getBlockZ() + ".");
                        }
                    }
                }
                else {
                    String currentLocationString = ChatColor.GRAY + commandSender.getDisplayName() + "> World: " + playersLocation.getWorld().getName() + ", X: " + playersLocation.getBlockX() + ", Y: " + playersLocation.getBlockY() + ", Z: " + playersLocation.getBlockZ() + ".";
                    messageServer(currentLocationString);
                }
            }
        }
        else{
            messagePlayer(sender,"You are console you, you can't do this command.");
        }

        return false;
    }

    private void helpCommand(CommandSender player){
        player.sendMessage(ChatColor.GRAY + "-----");
        player.sendMessage(ChatColor.GRAY + " All Commands:");
        player.sendMessage(ChatColor.GRAY + "'/local' or '/local here' - displays your current location to all players online.");
        player.sendMessage(ChatColor.GRAY + "/local <playername>' - displays the current location of target player to all players online.");
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "'/local list' - lists all saved co-ordinates.");
        player.sendMessage( ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "'/local add <name>' - Saves your current location to a file given an unique string.");
        player.sendMessage( ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "'/local remove <name>' - Removes a location from the list given the string ID.");
        player.sendMessage(ChatColor.GRAY + "'/local help' - displays all commands.");
        player.sendMessage(ChatColor.GRAY + "-----");
    }

    private boolean checkDataFolder(){
        if (!DATA_DIRECTORY_LOCATION.exists()){
            DATA_DIRECTORY_LOCATION.mkdirs();
            return false;
        }
        return true;
    }





}
