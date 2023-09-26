package br.com.rabbithole.economy.events;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.npc.ShopKeeperNPC;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class OnRightClickEvent implements Listener {
    final Plugin plugin;

    public OnRightClickEvent(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        if (npc.getName().equals(ShopKeeperNPC.NAME)) {
            Economy.getCommon().getMessages().sendSuccess(event.getClicker(), "Olá " + event.getClicker().getName());
        }
    }
}
