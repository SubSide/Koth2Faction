package subside.plugins.koth2factions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import subside.plugins.koth.events.KothCapEvent;
import subside.plugins.koth.events.KothLeftEvent;
import subside.plugins.koth.events.KothOpenChestEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

public class Koth2Factions extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onKothCap(KothCapEvent event) {
        String fName = MPlayer.get(Bukkit.getPlayer(event.getNextPlayerCapping())).getFactionName();
        if (fName != null && !fName.equalsIgnoreCase("")) {
            event.setNextPlayerCapping(fName);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKothLeft(KothLeftEvent event) {
        Faction prevFaction = FactionColl.get().getByName(event.getPlayerCapping());
        if (prevFaction != null) {
            for (Player player : prevFaction.getOnlinePlayers()) {
                if (event.getKoth().isInArea(player)) {
                    if (MPlayer.get(player).getFactionId().equalsIgnoreCase(prevFaction.getId())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKothOpenChest(KothOpenChestEvent event) {
        if (MPlayer.get(event.getPlayer()).getFactionName().equalsIgnoreCase(event.getKoth().getLastWinner())) {
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
        }

    }
}
