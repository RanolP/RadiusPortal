package me.ranol.rportal.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import me.ranol.rportal.api.Portal;

public class PortalActivateEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Portal portal;

	public PortalActivateEvent(Player who, Portal portal) {
		super(who);
		this.portal = portal;
	}

	public String getPortalName() {
		return portal.getName();
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean val) {
		this.cancel = val;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
