/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edeventosapo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author EduardoMGP
 */
public class Eventos implements Listener {

    private EDEventoSapo instance = EDEventoSapo.getPlugin(EDEventoSapo.class);
    private Funcoes f = new Funcoes();

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (instance.isPlayerInEvento(event.getPlayer())) {
            event.getPlayer().teleport(f.getSaida());
            instance.removePlayerFromEvento(event.getPlayer());
            Bukkit.broadcastMessage(instance.getString("desconectouEvento")
                    .replaceAll("%player%", event.getPlayer().getName())
            );
            Bukkit.broadcastMessage(instance.getString("playersRestantes")
                    .replaceAll("%players%", instance.countPlayerInEvento() + " ")
            );
            for (int i = 0; i < 9; i++) {
                event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
            }
            f.checkGanhador();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (instance.isPlayerInEvento((Player) event.getWhoClicked())) {
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            p.sendMessage(instance.getString("acoesBloqueadas"));

        }
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        if (instance.getEventoIniciado()) {
            if (instance.isPlayerInEvento(event.getPlayer())) {
                if (event.getPlayer().isFlying()) {
                    event.getPlayer().setFlying(false);
                    event.getPlayer().sendMessage(instance.getString("eventoRestritoVoo"));
                }
                if (event.getTo().getBlock().getType() == Material.STATIONARY_WATER || event.getTo().getBlock().getType() == Material.WATER) {
                    event.getPlayer().teleport(f.getSaida());
                    instance.removePlayerFromEvento(event.getPlayer());
                    Bukkit.broadcastMessage(instance.getString("perdeuEvento")
                            .replaceAll("%player%", event.getPlayer().getName())
                    );
                    Bukkit.broadcastMessage(instance.getString("playersRestantes")
                            .replaceAll("%players%", instance.countPlayerInEvento() + " ")
                    );
                    for (int i = 0; i < 9; i++) {
                        event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                    }
                    f.checkGanhador();
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_AXE) && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) {
            if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§bEDFrog")) {
                Player p = event.getPlayer();
                if (p.hasPermission("edeventosapo.marcar")) {
                    event.setCancelled(true);
                    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (f.getPos1() == null || f.getPos2() == null) {
                            f.setPos1(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), p.getWorld().getName());
                            p.sendMessage(instance.getString("pos1Marcado"));
                        } else {
                            p.sendMessage(instance.getString("arenaJaMarcada"));
                        }
                    }

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (f.getPos1() == null || f.getPos2() == null) {
                            f.setPos2(event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ(), p.getWorld().getName());
                            p.sendMessage(instance.getString("pos2Marcado"));
                        } else {
                            p.sendMessage(instance.getString("arenaJaMarcada"));
                        }
                    }
                } else {
                    p.sendMessage(instance.getString("semPermissao"));
                }
            }
        }
    }
}
