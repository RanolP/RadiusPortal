package me.ranol.rportal;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ranol.rportal.api.Portal;

public class RegionListener implements Listener {
	protected static final RegionListener INSTANCE = new RegionListener();
	private final HashMap<UUID, Location[]> portals = new HashMap<>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (isRegionMode(p)) {
			Location l = e.getClickedBlock()
				.getLocation();
			String loc = l.getX() + ", " + (l.getY() + 1) + ", " + l.getZ();
			switch (e.getAction()) {
			case LEFT_CLICK_BLOCK:
				p.sendMessage("§6 첫 번째 지점 설정 : §7[" + loc + "]");
				portals.get(p.getUniqueId())[0] = l.add(0.5, 1, 0.5);
				e.setCancelled(true);
				break;
			case RIGHT_CLICK_BLOCK:
				p.sendMessage("§6 두 번째 지점 설정 : §7[" + loc + "]");
				portals.get(p.getUniqueId())[1] = l.add(0.5, 1, 0.5);
				e.setCancelled(true);
				break;
			default:
				break;
			}
		}
	}

	public static Portal make(Player p, String name) {
		if (!isRegionMode(p)) return Portal.EMPTY;
		Location[] locs = INSTANCE.portals.get(p.getUniqueId());
		if (locs[0] == null || locs[1] == null) return Portal.EMPTY;
		Portal portal = new Portal(name);
		portal.setLocationA(locs[0]);
		portal.setLocationB(locs[1]);
		if (portal.isValid()) return portal;
		return Portal.EMPTY;
	}

	public static boolean isRegionMode(Player p) {
		return INSTANCE.portals.containsKey(p.getUniqueId());
	}

	public static void setRegionMode(Player p, boolean val) {
		if (val == isRegionMode(p)) return;
		if (val) INSTANCE.portals.put(p.getUniqueId(), new Location[2]);
		else INSTANCE.portals.remove(p.getUniqueId());
	}
}
