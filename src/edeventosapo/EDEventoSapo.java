/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edeventosapo;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * @author EduardoMGP
 */
public class EDEventoSapo extends JavaPlugin {

    private boolean eventoAberto = false;
    private boolean eventoIniciado = false;
    private ArrayList<Player> jogadoresNoEvento = new ArrayList<>();
    private int TaskID = 0;
    
    
    @Override
    public void onEnable() {
        getCommand("eventosapo").setExecutor(new Comandos());
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new Eventos(), this);
        Bukkit.getConsoleSender().sendMessage("§6-----§f - §l" + this.getName() + " §f- §6-----");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§7Plugin §ahabilitado §7com sucesso");
        Bukkit.getConsoleSender().sendMessage("§7Versao: §a" + this.getServer().getPluginManager().getPlugin(this.getName()).getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§7Desenvolvedor: §aEduardoMGP");
        Bukkit.getConsoleSender().sendMessage("§7Site: §ahttps://uaibits.com.br");
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("§6-----§f - §l" + this.getName() + " §f- §6-----");
    }

    @Override
    public void onDisable() {

    }
    
    public boolean getEventoAberto(){
        return eventoAberto;
    }
    public int getTaskID(){
        return TaskID;
    }
    public void setTaskID(int id){
        TaskID = id;
    }
    public boolean getEventoIniciado(){
        return eventoIniciado;
    }
    public void setEventoIniciado(boolean eventoIniciado){
        this.eventoIniciado = eventoIniciado;
    }
    public void setEventoAberto(boolean eventoAberto){
        this.eventoAberto = eventoAberto;
    }
    public void addPlayerToEvento(Player player){
        jogadoresNoEvento.add(player);
    }
    public void removePlayerFromEvento(Player player){
        jogadoresNoEvento.remove(player);
    }
    public boolean isPlayerInEvento(Player player){
        if(jogadoresNoEvento.contains(player)){
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<Player> getEventoPlayers(){
        return jogadoresNoEvento;
    }
    public int countPlayerInEvento(){
        return jogadoresNoEvento.size();
    }
    public void clearPlayersInEvento(){
        jogadoresNoEvento.clear();
    }

    public String getString(String m) {
        return getConfig().getString("prefixo").replaceAll("&", "§") + getConfig().getString(m).replaceAll("&", "§");
    }

    public int getInt(String m) {
        return getConfig().getInt(m);
    }

    public double getDouble(String m) {
        return getConfig().getDouble(m);
    }

    public void setScoreboard(Player p) {
        ScoreboardManager score = Bukkit.getScoreboardManager();
        Scoreboard board = score.getNewScoreboard();
        Objective o = board.registerNewObjective("Teste", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("Teste");
        Score gold = o.getScore("Teste: " + "aaa");
        gold.setScore(1);
        p.setScoreboard(board);
    }

}
