package br.com.rabbithole.economy.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ShopKeeperNPC {
    public static final String NAME = "Sr. Smith, o Vendedor";
    private NPC npc;

    public ShopKeeperNPC() {
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if (npc.getName().equals(NAME)) {
                this.npc = npc;
                break;
            }
        }
    }

    public void createNPC(Player player) {
        if (this.npc == null) {
            this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NAME);
            this.npc.spawn(player.getLocation());
            this.npc.getOrAddTrait(LookClose.class).lookClose(true);
            this.npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);
            this.npc.getOrAddTrait(LookClose.class).setRandomLook(true);
        } else {
            this.npc.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        }
    }
}
