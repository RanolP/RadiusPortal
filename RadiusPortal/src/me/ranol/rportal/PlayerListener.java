package me.ranol.rportal;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.ranol.rportal.api.Portal;
import me.ranol.rportal.api.Portal.PortalResult;
import me.ranol.rportal.api.PortalManager;
import me.ranol.rportal.events.PortalActivateEvent;

public class PlayerListener implements Listener {
	private boolean sendMsg;

	protected PlayerListener() {
	}

	public void setSendMessage(boolean b) {
		sendMsg = b;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		PlayerData.update(e.getPlayer());
		Portal p = PortalManager.getPortal(e.getTo());
		if (p.isValid()) {
			PortalActivateEvent ev = new PortalActivateEvent(e.getPlayer(), p);
			Bukkit.getPluginManager()
				.callEvent(ev);
			if (!ev.isCancelled()) {
				PortalResult result = p.makeTeleport(e);
				if (sendMsg && result.isSuccess()) {
					e.getPlayer()
						.sendMessage("§8[§b§lRadius§5§lPortal§8] §6포탈 이동에 성공하였습니다.");
				}
			}
		}
	}
}
