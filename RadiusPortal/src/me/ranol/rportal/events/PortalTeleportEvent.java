package me.ranol.rportal.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PortalTeleportEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private Location to;
	private boolean cancel = false;

	public PortalTeleportEvent(Player who, Location to) {
		super(who);
		this.to = to;
	}

	public Location getTo() {
		return to;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean val) {
		this.cancel = val;
	}
}
