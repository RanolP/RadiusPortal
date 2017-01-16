package me.ranol.rportal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TabCompletor {

	public static List<String> complete(String message, List<String> args) {
		return complete(message, args.toArray(new String[args.size()]));
	}

	public static List<String> complete(String message, String... args) {
		return Arrays.stream(args)
			.filter(a -> a.toLowerCase()
				.startsWith(message.toLowerCase()))
			.collect(Collectors.toList());
	}

	public static void addPlayers(List<String> addto, boolean offline) {
		if (offline) Arrays.stream(Bukkit.getOfflinePlayers())
			.map(OfflinePlayer::getName)
			.filter(s -> !addto.contains(s) && s != null)
			.forEach(addto::add);
		else Bukkit.getOnlinePlayers()
			.stream()
			.map(Player::getName)
			.filter(s -> !addto.contains(s) && s != null)
			.forEach(addto::add);
		;
	}

	public static String getArgs(String[] args, int start) {
		return Arrays.stream(args)
			.skip(start)
			.collect(Collectors.joining(" "))
			.trim();
	}
}
