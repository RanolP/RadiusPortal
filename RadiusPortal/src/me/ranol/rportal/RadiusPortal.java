package me.ranol.rportal;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ranol.rportal.api.PortalManager;

public class RadiusPortal extends JavaPlugin {
	private static RadiusPortal instance;
	private YamlConfiguration config;
	private PlayerListener l;
	private boolean addWithSave;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		config = (YamlConfiguration) getConfig();
		l = new PlayerListener();
		Bukkit.getPluginManager()
			.registerEvents(l, this);
		Bukkit.getPluginManager()
			.registerEvents(RegionListener.INSTANCE, this);
		CmdRadiusPortal cmd = new CmdRadiusPortal();
		getCommand("radiusportal").setExecutor(cmd);
		loadConfig();
		PortalManager.loadPortals();
	}

	@Override
	public void onDisable() {
		PortalManager.savePortals();
	}

	public void loadConfig() {
		addWithSave = config.getBoolean("add-with-save", true);
		PlayerData.setResetDistance(config.getInt("distance.reset", 2));
		l.setSendMessage(config.getBoolean("effect.sendMessage", true));
	}

	public static boolean addWithSave() {
		return instance.addWithSave;
	}

	public static File dataFolder() {
		return instance.getDataFolder();
	}
}
