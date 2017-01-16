package me.ranol.rportal.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PortalReuseEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();

	public PortalReuseEvent(Player who) {
		super(who);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public HandlerList getHandlerList() {
		return handlers;
	}

}
