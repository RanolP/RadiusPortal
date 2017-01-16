package me.ranol.rportal.api;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ranol.rportal.PlayerData;
import me.ranol.rportal.events.PortalTeleportEvent;

public class Portal implements Serializable {
	private static final long serialVersionUID = 7041501190849563146L;
	private WrappedLocation locA = new WrappedLocation();
	private WrappedLocation locB = new WrappedLocation();
	private String name;
	public static final Portal EMPTY = new Portal("RadiusPortal::EmptyPortal");
	private static final double REQ_DISTANCE = 0.25;

	public static enum PortalResult {
		TO_A(true),
		TO_B(true),
		CANT_TELEPORT(false),
		BEFORE_TELEPORT(false),
		MISSING_LOCATION(false),
		CANCELLED(false);

		boolean success;

		private PortalResult(boolean success) {
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}
	}

	public Portal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLocationA(Location l) {
		this.locA.apply(l);
	}

	public void setLocationB(Location l) {
		this.locB.apply(l);
	}

	public boolean isMissingLocation() {
		return locA == null || locB == null;
	}

	public boolean canTeleport(Player p) {
		return canTeleport(p.getLocation());
	}

	public boolean canTeleport(Location l) {
		return isValid() && (locA.distanceSquared(l) <= REQ_DISTANCE || locB.distanceSquared(l) <= REQ_DISTANCE);
	}

	private boolean a(Player p) {
		Location l = p.getLocation();
		return locA.distanceSquared(l) < locB.distanceSquared(l);
	}

	public PortalResult makeTeleport(PlayerMoveEvent event) {
		if (isMissingLocation()) return PortalResult.MISSING_LOCATION;
		if (!canTeleport(event.getTo())) return PortalResult.CANT_TELEPORT;
		if (PlayerData.hasLastLocation(event.getPlayer())) return PortalResult.BEFORE_TELEPORT;
		if (a(event.getPlayer())) {
			PortalTeleportEvent e = new PortalTeleportEvent(event.getPlayer(), locB.getBukkitLocation());
			Bukkit.getPluginManager()
				.callEvent(e);
			if (!e.isCancelled()) {
				PlayerData.setLastLocation(event.getPlayer(), e.getTo());
				event.setTo(e.getTo());
				return PortalResult.TO_B;
			}
			return PortalResult.CANCELLED;
		}
		PortalTeleportEvent e = new PortalTeleportEvent(event.getPlayer(), locA.getBukkitLocation());
		Bukkit.getPluginManager()
			.callEvent(e);
		if (!e.isCancelled()) {
			PlayerData.setLastLocation(event.getPlayer(), e.getTo());
			event.setTo(e.getTo());
			return PortalResult.TO_A;
		}
		return PortalResult.CANCELLED;
	}

	public PortalResult makeTeleport(Player p) {
		if (isMissingLocation()) return PortalResult.MISSING_LOCATION;
		if (!canTeleport(p)) return PortalResult.CANT_TELEPORT;
		if (PlayerData.hasLastLocation(p)) return PortalResult.BEFORE_TELEPORT;
		if (a(p)) {
			PortalTeleportEvent e = new PortalTeleportEvent(p, locB.getBukkitLocation());
			Bukkit.getPluginManager()
				.callEvent(e);
			if (!e.isCancelled()) {
				PlayerData.setLastLocation(p, e.getTo());
				p.teleport(e.getTo());
				return PortalResult.TO_B;
			}
			return PortalResult.CANCELLED;
		}
		PortalTeleportEvent e = new PortalTeleportEvent(p, locA.getBukkitLocation());
		Bukkit.getPluginManager()
			.callEvent(e);
		if (!e.isCancelled()) {
			PlayerData.setLastLocation(p, e.getTo());
			p.teleport(e.getTo());
			return PortalResult.TO_A;
		}
		return PortalResult.CANCELLED;
	}

	@Override
	public int hashCode() {
		int result = 17;
		int c = locA.hashCode();
		c += locB.hashCode();
		c += name.hashCode();
		return 31 * result + c;
	}

	@Override
	public String toString() {
		return "Portal [name=" + name + ", A=" + locA.toString() + ", B=" + locB.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Portal)) return false;
		Portal o = (Portal) obj;
		if (!o.locA.equals(locA)) return false;
		if (!o.locB.equals(locB)) return false;
		return true;
	}

	public boolean isValid() {
		return !getName().equals("RadiusPortal::EmptyPortal") && !isMissingLocation();
	}
}
