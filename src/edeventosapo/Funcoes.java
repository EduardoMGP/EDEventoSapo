/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edeventosapo;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author EduardoMGP
 */
public class Funcoes {

    private EDEventoSapo instance = EDEventoSapo.getPlugin(EDEventoSapo.class);
    private int timer = 15;
    private int tempo = 15;
    private int cor = 0;

    public Location getEntrada() {
        try {
            String world = instance.getConfig().getString("entrada.world");
            int x = instance.getConfig().getInt("entrada.x");
            int z = instance.getConfig().getInt("entrada.z");
            int y = instance.getConfig().getInt("entrada.y");
            float yaw = Float.parseFloat(instance.getConfig().getString("entrada.yaw"));
            float pitch = Float.parseFloat(instance.getConfig().getString("entrada.pitch"));
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }

    public Location getSaida() {
        try {
            String world = instance.getConfig().getString("saida.world");
            int x = instance.getConfig().getInt("saida.x");
            int z = instance.getConfig().getInt("saida.z");
            int y = instance.getConfig().getInt("saida.y");
            float yaw = Float.parseFloat(instance.getConfig().getString("saida.yaw"));
            float pitch = Float.parseFloat(instance.getConfig().getString("saida.pitch"));
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }

    public Location getPos1() {
        try {
            String world = instance.getConfig().getString("pos1.world");
            int x = instance.getConfig().getInt("pos1.x");
            int z = instance.getConfig().getInt("pos1.z");
            int y = instance.getConfig().getInt("pos1.y");
            return new Location(Bukkit.getWorld(world), x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    public Location getPos2() {
        try {
            String world = instance.getConfig().getString("pos2.world");
            int x = instance.getConfig().getInt("pos2.x");
            int z = instance.getConfig().getInt("pos2.z");
            int y = instance.getConfig().getInt("pos2.y");
            return new Location(Bukkit.getWorld(world), x, y, z);
        } catch (Exception e) {
            return null;
        }
    }

    public void setPos1(int x, int y, int z, String world) {
        instance.getConfig().set("pos1.world", world);
        instance.getConfig().set("pos1.x", x);
        instance.getConfig().set("pos1.z", z);
        instance.getConfig().set("pos1.y", y);
        instance.saveConfig();
        instance.reloadConfig();

    }

    public void setPos2(int x, int y, int z, String world) {
        instance.getConfig().set("pos2.world", world);
        instance.getConfig().set("pos2.x", x);
        instance.getConfig().set("pos2.z", z);
        instance.getConfig().set("pos2.y", y);
        instance.saveConfig();
        instance.reloadConfig();

    }

    public void delArena() {
        for (int i = 1; i < 3; i++) {
            instance.getConfig().set("pos" + i + ".x", null);
            instance.getConfig().set("pos" + i + ".y", null);
            instance.getConfig().set("pos" + i + ".z", null);
            instance.getConfig().set("pos" + i + ".world", null);
        }
        instance.saveConfig();
        instance.reloadConfig();
    }

    public void setEntrada(Player p) {
        instance.getConfig().set("entrada.x", p.getLocation().getBlockX());
        instance.getConfig().set("entrada.y", p.getLocation().getBlockY());
        instance.getConfig().set("entrada.z", p.getLocation().getBlockZ());
        instance.getConfig().set("entrada.yaw", p.getLocation().getYaw());
        instance.getConfig().set("entrada.pitch", p.getLocation().getPitch());
        instance.getConfig().set("entrada.world", p.getLocation().getWorld().getName());
        instance.saveConfig();
        instance.reloadConfig();
    }

    public void setSaida(Player p) {
        instance.getConfig().set("saida.x", p.getLocation().getBlockX());
        instance.getConfig().set("saida.y", p.getLocation().getBlockY());
        instance.getConfig().set("saida.z", p.getLocation().getBlockZ());
        instance.getConfig().set("saida.yaw", p.getLocation().getYaw() + "");
        instance.getConfig().set("saida.pitch", p.getLocation().getPitch() + "");
        instance.getConfig().set("saida.world", p.getLocation().getWorld().getName());
        instance.saveConfig();
        instance.reloadConfig();
    }

    public void preparar(int data) {
        if (getPos1() != null && getPos2() != null) {
            Location pos1 = getPos1();
            Location pos2 = getPos2();
            int x1, x2, z1, z2 = 0;
            if (pos1.getBlockX() > pos2.getBlockX()) {
                x1 = pos2.getBlockX();
                x2 = pos1.getBlockX();
            } else {
                x2 = pos2.getBlockX();
                x1 = pos1.getBlockX();
            }
            if (pos1.getBlockZ() > pos2.getBlockZ()) {
                z1 = pos2.getBlockZ();
                z2 = pos1.getBlockZ();
            } else {
                z2 = pos2.getBlockZ();
                z1 = pos1.getBlockZ();
            }
            if (data >= 0) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        Location loc = new Location(pos1.getWorld(), x, pos1.getBlockY(), z);
                        Random random = new Random();
                        if (loc.getBlock().getData() != (byte) data) {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }

            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
                @Override
                public void run() {
                    Location pos1 = getPos1();
                    Location pos2 = getPos2();
                    int x1, x2, z1, z2 = 0;
                    if (pos1.getBlockX() > pos2.getBlockX()) {
                        x1 = pos2.getBlockX();
                        x2 = pos1.getBlockX();
                    } else {
                        x2 = pos2.getBlockX();
                        x1 = pos1.getBlockX();
                    }
                    if (pos1.getBlockZ() > pos2.getBlockZ()) {
                        z1 = pos2.getBlockZ();
                        z2 = pos1.getBlockZ();
                    } else {
                        z2 = pos2.getBlockZ();
                        z1 = pos1.getBlockZ();
                    }
                    int i = 0;
                    for (int x = x1; x <= x2; x++) {
                        for (int z = z1; z <= z2; z++) {
                            Random random = new Random();
                            int dataItem = random.nextInt(15);
                            Location loc1 = new Location(pos1.getWorld(), x + 1, pos1.getBlockY(), z);
                            Location loc2 = new Location(pos1.getWorld(), x - 1, pos1.getBlockY(), z);
                            i++;
                            if (!(i % 7 == 0)) {
                                while (cor == dataItem) {
                                    dataItem = random.nextInt(15);
                                }
                            }
                            Location loc = new Location(pos1.getWorld(), x, pos1.getBlockY(), z);
                            loc.getBlock().setTypeIdAndData(159, (byte) dataItem, true);
                        }
                    }
                }
            }, 15);

        }
    }

    public void checkGanhador() {
        if (instance.countPlayerInEvento() == 0) {
            instance.setEventoAberto(false);
            instance.setEventoIniciado(false);
            instance.clearPlayersInEvento();
            preparar(-1);
            Bukkit.broadcastMessage(instance.getString("ninguemGanhou"));
            Bukkit.getScheduler().cancelTask(instance.getTaskID());
            return;
        }
        if (instance.countPlayerInEvento() == 1) {
            instance.setEventoAberto(false);
            instance.setEventoIniciado(false);
            for (Player p : instance.getEventoPlayers()) {
                preparar(-1);
                Bukkit.broadcastMessage(instance.getString("ganhouEvento").replaceAll("%player%", p.getName()));
                p.teleport(getSaida());
                for (int i = 0; i < 9; i++) {
                    p.getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
            instance.clearPlayersInEvento();
            preparar(-1);
            Bukkit.getScheduler().cancelTask(instance.getTaskID());
        }
    }

    public void iniciarEvento() {
        Random random = new Random();
        cor = random.nextInt(15);
        instance.setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                checkGanhador();
                if (timer > 0) {
                    timer--;
                    ArrayList<Player> players = instance.getEventoPlayers();
                    for (Player p : players) {
                        for (int i = 0; i < 9; i++) {
                            p.getInventory().setItem(i, new ItemStack(Material.STAINED_CLAY, timer, (short) cor));
                        }
                    }
                } else {
                    if (tempo > 2) {
                        tempo = tempo - 1;
                        timer = tempo;
                    } else {
                        timer = tempo;
                    }
                    preparar(cor);
                    cor = random.nextInt(15);
                }
            }
        }, 0, 20));
    }
}
