/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edeventosapo;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author EduardoMGP
 */
public class Comandos implements CommandExecutor {

    private EDEventoSapo instance = EDEventoSapo.getPlugin(EDEventoSapo.class);
    private Funcoes f = new Funcoes();
    private int chamadas = instance.getInt("chamadasEvento");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eventosapo")) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (instance.getEventoAberto()) {
                    if (instance.getEventoIniciado()) {
                        p.sendMessage(instance.getString("eventoJaIniciado"));
                    } else {
                        if (instance.isPlayerInEvento(p)) {
                            p.sendMessage(instance.getString("voceJaEstaParticipando"));
                        } else {
                            instance.addPlayerToEvento(p);
                            p.teleport(f.getEntrada());
                            p.sendMessage(instance.getString("entrouNoEvento"));
                        }
                    }
                } else {
                    p.sendMessage(instance.getString("eventoNaoEstaAberto"));
                }
                return true;
            }

            switch (args[0]) {
                case "abrir":
                    if (p.hasPermission("edeventosapo.abrir")) {
                        if (f.getEntrada() == null) {
                            p.sendMessage(instance.getString("entradaNaoMarcada"));
                            break;
                        }
                        if (f.getSaida() == null) {
                            p.sendMessage(instance.getString("saidaNaoMarcada"));
                            break;
                        }
                        if (f.getPos1() == null || f.getPos2() == null) {
                            p.sendMessage(instance.getString("arenaNaoMarcada"));
                            break;
                        }
                        if (instance.getEventoAberto()) {
                            p.sendMessage(instance.getString("jaIniciado"));
                        } else {
                            chamadas = instance.getInt("chamadasEvento");
                            f.preparar(-1);
                            instance.setEventoAberto(true);
                            instance.setEventoIniciado(false);
                            instance.clearPlayersInEvento();
                            instance.setTaskID(
                                    Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
                                        @Override
                                        public void run() {
                                            if (chamadas <= 0) {
                                                instance.setEventoIniciado(true);
                                                f.iniciarEvento();
                                                Bukkit.getScheduler().cancelTask(instance.getTaskID());
                                            } else {
                                                for (String m : instance.getConfig().getStringList("chamadaEvento")) {

                                                    Bukkit.broadcastMessage(
                                                            m
                                                                    .replaceAll("%chamadas%", chamadas + "")
                                                                    .replaceAll("%jogadores%", instance.countPlayerInEvento() + "")
                                                                    .replaceAll("&", "§")
                                                    );

                                                }
                                            }
                                            chamadas--;
                                        }
                                    }, 0, instance.getInt("tempoChamadasSegundos") * 20L)
                            );
                        }

                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }

                    break;

                case "marcar":
                    if (p.hasPermission("edeventosapo.abrir")) {
                        ItemStack item = new ItemStack(Material.DIAMOND_AXE);
                        ItemMeta itemmeta = item.getItemMeta();
                        itemmeta.setDisplayName("§bEDFrog");
                        item.setItemMeta(itemmeta);
                        p.getInventory().addItem(item);
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }
                    break;

                case "remover":
                    if (p.hasPermission("edeventosapo.remover")) {
                        if (f.getPos1() != null && f.getPos2() != null) {
                            f.delArena();
                            p.sendMessage(instance.getString("arenaRemovida"));
                        } else {
                            p.sendMessage(instance.getString("arenaNaoMarcada"));
                        }
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }

                    break;

                case "fechar":
                    if (p.hasPermission("edeventosapo.fechar")) {
                        if (instance.getEventoAberto() == false) {
                            p.sendMessage(instance.getString("eventoNaoEstaAberto"));
                        } else {
                            chamadas = instance.getInt("chamadasEvento");
                            f.preparar(-1);
                            instance.setEventoAberto(false);
                            instance.setEventoIniciado(false);
                            for (Player players : instance.getEventoPlayers()) {
                                for (int i = 0; i < 9; i++) {
                                    players.getInventory().setItem(i, new ItemStack(Material.AIR));
                                }
                                players.teleport(f.getSaida());
                            }
                            instance.clearPlayersInEvento();
                            Bukkit.getScheduler().cancelTasks(instance);
                            Bukkit.broadcastMessage(instance.getString("eventoFinalizado").replaceAll("%staff%", p.getName()));
                        }
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }
                    break;

                case "sair":

                    if (instance.isPlayerInEvento(p)) {
                        instance.removePlayerFromEvento(p);
                        p.teleport(f.getSaida());
                        p.sendMessage(instance.getString("saiuDoEvento"));
                        Bukkit.broadcastMessage(instance.getString("saiuDoEvento")
                                .replaceAll("%player%", p.getName())
                        );
                        Bukkit.broadcastMessage(instance.getString("playersRestantes")
                                .replaceAll("%players%", instance.countPlayerInEvento() + " ")
                        );
                        for (int i = 0; i < 9; i++) {
                            p.getInventory().setItem(i, new ItemStack(Material.AIR));
                        }
                    } else {
                        p.sendMessage(instance.getString("naoEstaNoEvento"));
                    }

                    break;

                case "setentrada":
                    if (p.hasPermission("edeventosapo.setentrada")) {
                        f.setEntrada(p);
                        p.sendMessage(instance.getString("entradaMarcada"));
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }
                    break;

                case "setsaida":
                    if (p.hasPermission("edeventosapo.setsaida")) {
                        f.setSaida(p);
                        p.sendMessage(instance.getString("saidaMarcada"));
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }
                    break;

                case "entrada":
                    if (p.hasPermission("edeventosapo.entrada")) {
                        Location entrada = f.getEntrada();
                        if (entrada != null) {
                            p.teleport(entrada);
                            p.sendMessage(instance.getString("teleportouEntrada"));
                        } else {
                            p.sendMessage(instance.getString("entradaNaoMarcada"));
                        }
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }
                    break;

                case "preparar":
                    if (p.hasPermission("edeventosapo.preparar")) {
                        f.preparar(-1);
                    } else {
                        p.sendMessage(instance.getString("semPermissao"));
                    }

                    break;

                default:
                    p.sendMessage("argumento invalido");
            }
        }

        return true;
    }

}
