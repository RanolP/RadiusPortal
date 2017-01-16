package me.ranol.rportal;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ranol.rportal.events.PortalReuseEvent;

public class PlayerData {
	private static int distance = 4;
	private static boolean resetMsg = true;
	private static final HashMap<UUID, Location> lastLocation = new HashMap<>();

	public static void setResetDistance(int distance) {
		PlayerData.distance = distance * distance;
	}

	public static void setResetMessage(boolean msg) {
		PlayerData.resetMsg = msg;
	}

	public static void setLastLocation(Player p, Location l) {
		lastLocation.put(p.getUniqueId(), l);
	}

	public static void update(Player p) {
		if (getLastLocation(p).distanceSquared(p.getLocation()) >= distance) {
			lastLocation.remove(p.getUniqueId());
			PortalReuseEvent e = new PortalReuseEvent(p);
			Bukkit.getPluginManager()
				.callEvent(e);
			if (resetMsg) p.sendMessage(" §a포탈을 다시 사용하실 수 있습니다.");
		}
	}

	public static boolean hasLastLocation(Player p) {
		update(p);
		return lastLocation.containsKey(p.getUniqueId());
	}

	public static Location getLastLocation(Player p) {
		return lastLocation.getOrDefault(p.getUniqueId(), p.getLocation());
	}
}
