package jp.mkserver.flagmanager;

import jp.mkserver.flagmanager.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FlagManager extends JavaPlugin {

    private SQLManager sql;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
            }else if (args.length == 3) {
                switch (args[0]) {
                    case "up": {
                        Player pl = Bukkit.getPlayerExact(args[2]);
                        if (pl == null) {
                            return true;
                        }
                        addPlayerFlag(pl, args[1]);
                        break;
                    }
                    case "down": {
                        Player pl = Bukkit.getPlayerExact(args[2]);
                        if (pl == null) {
                            return true;
                        }
                        removePlayerFlag(pl, args[1]);
                        break;
                    }
                    case "uncount": {
                        Player pl = Bukkit.getPlayerExact(args[2]);
                        if (pl == null) {
                            return true;
                        }
                        countDelete(pl, args[1]);
                        break;
                    }
                }
            }else if (args.length == 4) {
                switch (args[0]) {
                    case "count": {
                        Player pl = Bukkit.getPlayerExact(args[3]);
                        if (pl == null) {
                            return true;
                        }
                        int i = 0;
                        try{
                            i = Integer.parseInt(args[2]);
                        }catch (NumberFormatException e){
                            return true;
                        }
                        countUpdate(pl,args[1],i);
                        break;
                    }
                }
            }
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("flagmanager.command")) {
            p.sendMessage("Unknown command. Type \"/help\" for help.");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("/flag up <フラグ名> <プレイヤー名> : フラグを上げる");
            p.sendMessage("/flag down <フラグ名> <プレイヤー名> : フラグを下げる");
            p.sendMessage("/flag count <カウンタ名> <数字> <プレイヤー名> : カウンタをアップデート/作成する");
            p.sendMessage("/flag uncount <カウンタ名> <プレイヤー名> : カウンタを削除する");
        }else if (args.length == 3) {
            switch (args[0]) {
                case "up": {
                    Player pl = Bukkit.getPlayerExact(args[2]);
                    if (pl == null) {
                        p.sendMessage("プレイヤーがオフラインまたは存在しない");
                        return true;
                    }
                    addPlayerFlag(pl, args[1]);
                    break;
                }
                case "down": {
                    Player pl = Bukkit.getPlayerExact(args[2]);
                    if (pl == null) {
                        p.sendMessage("プレイヤーがオフラインまたは存在しない");
                        return true;
                    }
                    removePlayerFlag(pl, args[1]);
                    break;
                }
                case "uncount": {
                    Player pl = Bukkit.getPlayerExact(args[2]);
                    if (pl == null) {
                        p.sendMessage("プレイヤーがオフラインまたは存在しない");
                        return true;
                    }
                    countDelete(pl, args[1]);
                    break;
                }
            }
            p.sendMessage("/flag up <フラグ名> <プレイヤー名> : フラグを上げる");
            p.sendMessage("/flag down <フラグ名> <プレイヤー名> : フラグを下げる");
            p.sendMessage("/flag count <カウンタ名> <数字> <プレイヤー名> : カウンタをアップデート/作成する");
            p.sendMessage("/flag uncount <カウンタ名> <プレイヤー名> : カウンタを削除する");
        }else if (args.length == 4) {
            switch (args[0]) {
                case "count": {
                    Player pl = Bukkit.getPlayerExact(args[3]);
                    if (pl == null) {
                        p.sendMessage("プレイヤーがオフラインまたは存在しない");
                        return true;
                    }
                    int i = 0;
                    try{
                        i = Integer.parseInt(args[2]);
                    }catch (NumberFormatException e){
                        p.sendMessage("数字ではない");
                        return true;
                    }
                    countUpdate(pl,args[1],i);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        FlagAPI.init(this);
        getCommand("flag").setExecutor(this);
        sql = new SQLManager(this,"FlagManager");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    protected boolean isPlayerHasFlag(Player p, String name){
        SQLManager.Query query = sql.query("SELECT * FROM flagdata WHERE uuid = '"+p.getUniqueId().toString()+"' AND flag = '"+name+"';");
        ResultSet rs = query.getRs();
        if(rs==null){
            query.close();
            return false;
        }
        try {
            if(rs.next()){
                query.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return false;
    }

    protected int getCount(Player p, String name){
        SQLManager.Query query = sql.query("SELECT * FROM counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND name = '"+name+"';");
        ResultSet rs = query.getRs();
        if(rs==null){
            query.close();
            return -1;
        }
        try {
            if(rs.next()){
                int i = rs.getInt("count");
                query.close();
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return  -1;
    }

    protected boolean hasCount(Player p, String name){
        SQLManager.Query query = sql.query("SELECT * FROM counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND name = '"+name+"';");
        ResultSet rs = query.getRs();
        if(rs==null){
            query.close();
            return false;
        }
        try {
            if(rs.next()){
                query.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query.close();
        return false;
    }

    protected void countUpdate(Player p, String name, int count){
        if(getCount(p,name)!=-1){
            sql.execute("UPDATE counter SET count = "+count+" WHERE uuid = '"+p.getUniqueId().toString()+"' AND name = '"+name+"';");
            return;
        }
        sql.execute("INSERT INTO counter (player,uuid,name,count)  VALUES ('"+p.getName()+"','"+p.getUniqueId().toString()+"','"+name+"',"+count+");");
    }

    protected void countDelete(Player p, String name){
        sql.execute("DELETE FROM counter WHERE uuid = '"+p.getUniqueId().toString()+"' AND name = '" + name + "';");
    }

    protected void addPlayerFlag(Player p, String name){
        if(isPlayerHasFlag(p,name)){
            return;
        }
        sql.execute("INSERT INTO flagdata (player,uuid,flag)  VALUES ('"+p.getName()+"','"+p.getUniqueId().toString()+"','"+name+"');");
    }

    protected void removePlayerFlag(Player p, String name){
        sql.execute("DELETE FROM flagdata WHERE uuid = '" + p.getUniqueId().toString() + "' AND flag = '"+name+"';");
    }
}
