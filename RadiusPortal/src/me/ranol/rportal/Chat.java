package me.ranol.rportal;

import java.util.List;

import org.bukkit.command.CommandSender;

public class Chat {

	public static void sendMessageList(CommandSender target, List<String> messages, int currentPage,
			String commandLabel, int viewSize) {
		if ((currentPage - 1) * viewSize + 1 > messages.size() || currentPage < 0) {
			target.sendMessage("§c목록을 찾을 수 없습니다.");
		}
		int max = messages.size() % viewSize == 0 ? messages.size() / viewSize : messages.size() / viewSize + 1;
		target.sendMessage("§6" + messages.size() + "개를 찾았습니다. §e" + currentPage + "§b/§a" + messages.size() / max);
		for (int i = (currentPage - 1) * viewSize; i < currentPage * viewSize; i++) {
			if (i >= messages.size()) break;
			if (currentPage * viewSize == i && messages.size() > i) {
				target.sendMessage("§b다음 목록을 보려면 &c/" + commandLabel + " " + (currentPage + 1));
				break;
			}
			target.sendMessage("§6" + messages.get(i));
		}
	}
}
