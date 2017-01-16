package me.ranol.rportal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.ranol.rportal.api.Portal;
import me.ranol.rportal.api.PortalManager;

public class CmdRadiusPortal implements TabExecutor {

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
		List<String> result = new ArrayList<>();
		if (a.length <= 1) {
			result.addAll(TabCompletor.complete(a[a.length - 1], "help", "region", "make", "list", "del"));
		} else if (a.length == 2 && a[0].equalsIgnoreCase("del")) {
			result.addAll(PortalManager.getPortals()
				.keySet());
		}
		return result;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (a.length == 0 || a[0].equalsIgnoreCase("help")) {
			help(s, l);
			return true;
		} else if (a[0].equalsIgnoreCase("region")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§c[!] §6게임 안의 플레이어만 이용 가능한 명령어입니다!");
				return true;
			}
			Player p = (Player) s;
			if (RegionListener.isRegionMode(p)) {
				s.sendMessage("§a 포탈 좌표 선택 모드를 비활성화합니다.");
				RegionListener.setRegionMode(p, false);
			} else {
				s.sendMessage("§a 포탈 좌표 선택 모드를 활성화합니다.");
				RegionListener.setRegionMode(p, true);
			}
			return true;
		} else if (a[0].equalsIgnoreCase("make")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§c[!] §6게임 안의 플레이어만 이용 가능한 명령어입니다!");
				return true;
			}
			if (a.length <= 1) {
				s.sendMessage("§c[!] §6이름을 입력해주세요!");
				return true;
			}
			Player p = (Player) s;
			if (!RegionListener.isRegionMode(p)) {
				s.sendMessage("§c[!] §6좌표 선택 모드가 아닙니다!");
				return true;
			}
			String name = TabCompletor.getArgs(a, 1);
			Portal portal = RegionListener.make(p, name);
			if (portal.isValid()) {
				try {
					PortalManager.registerPortal(portal);
					s.sendMessage("§a 포탈을 성공적으로 만들었습니다!");
					RegionListener.setRegionMode(p, false);
				} catch (IllegalArgumentException e) {
					s.sendMessage("§c[!] §6이미 지정된 이름입니다!");
				}
			} else {
				s.sendMessage("§c[!] §6포탈의 위치가 전부 설정되어 있지 않습니다.");
				return true;
			}
		} else if (a[0].equalsIgnoreCase("list")) {
			Chat.sendMessageList(s, new ArrayList<>(PortalManager.getPortals()
				.keySet()), a.length == 2 && a[1].matches("[0-9]+") ? Integer.parseInt(a[1]) : 1, l + " list", 8);
		} else if (a[0].equalsIgnoreCase("del")) {
			if (a.length <= 1) {
				s.sendMessage("§c[!] §6삭제할 포탈의 이름을 입력해주세요!");
				return true;
			}
			String name = a[1];
			if (PortalManager.getPortal(name)
				.isValid()) {
				PortalManager.removePortal(name);
				s.sendMessage("§a 포탈을 성공적으로 삭제했습니다!");
			} else {
				s.sendMessage("§c[!] §6존재하지 않는 포탈입니다!");
			}
			return true;
		}
		return false;
	}

	void help(CommandSender s, String l) {
		s.sendMessage("§e!==========§8[§b§lRadius§5§lPortal§8]§e==========!");
		s.sendMessage("§6 * /" + l + " help §f- §a도움말을 띄웁니다.");
		s.sendMessage("§6 * /" + l + " region §f- §a포탈 좌표 선택 모드로 변경합니다.");
		s.sendMessage("§6 * /" + l + " make [이름] §f- §a포탈을 생성합니다.");
		s.sendMessage("§6 * /" + l + " list [페이지] §f- §a포탈 목록을 띄웁니다.");
		s.sendMessage("§6 * /" + l + " del [이름] §f- §a포탈을 삭제합니다.");
	}

}
