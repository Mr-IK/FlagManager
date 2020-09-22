package jp.mkserver.flagmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagAPI {

    private static FlagManager instance;

    protected static void init(FlagManager instance){
        FlagAPI.instance = instance;
    }

    public static boolean isPlayerHasFlag(Player p, String name){
        return instance.isPlayerHasFlag(p,name);
    }

    public static void playerFlagUp(Player p, String name){
        Bukkit.getScheduler().runTaskAsynchronously(instance,()-> instance.addPlayerFlag(p,name));
    }

    public static void playerFlagDown(Player p, String name){
        Bukkit.getScheduler().runTaskAsynchronously(instance,()-> instance.removePlayerFlag(p,name));
    }

    public static int getPlayerCount(Player p, String counter_name){
        return instance.getCount(p,counter_name);
    }

    public static void createPlayerCount(Player p, String counter_name,int count){
        Bukkit.getScheduler().runTaskAsynchronously(instance,()-> {
            if (!instance.hasCount(p, counter_name)) {
                instance.countUpdate(p, counter_name, count);
            }
        });
    }

    public static void updatePlayerCount(Player p, String counter_name,int count){
        Bukkit.getScheduler().runTaskAsynchronously(instance,()-> {
            if (instance.hasCount(p, counter_name)) {
                instance.countUpdate(p, counter_name, count);
            }
        });
    }

    public static void forceUpdatePlayerCount(Player p, String counter_name,int count){
        Bukkit.getScheduler().runTaskAsynchronously(instance,()-> {
            instance.countUpdate(p, counter_name, count);
        });
    }

    public static boolean isPlayerHasCount(Player p,String counter_name){
        return instance.hasCount(p,counter_name);
    }
}
