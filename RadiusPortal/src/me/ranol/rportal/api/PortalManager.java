package me.ranol.rportal.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ranol.rportal.RadiusPortal;

public class PortalManager implements Serializable {
	private static final long serialVersionUID = -1107901553456081824L;
	private final Map<String, Portal> portals = new HashMap<>();

	private static class Singleton {
		private static final File f = new File(RadiusPortal.dataFolder(), "Portals.ser");
		private static final PortalManager instance = new PortalManager();
	}

	public static PortalManager getInstance() {
		return Singleton.instance;
	}

	public static Map<String, Portal> getPortals() {
		return new HashMap<>(getInstance().portals);
	}

	public static Portal getPortal(Player p) {
		for (Portal portal : getPortals().values()) {
			if (portal.canTeleport(p)) return portal;
		}
		return Portal.EMPTY;
	}

	public static Portal getPortal(Location loc) {
		for (Portal portal : getPortals().values()) {
			if (portal.canTeleport(loc)) return portal;
		}
		return Portal.EMPTY;
	}

	public static Portal getPortal(String name) {
		return getPortals().containsKey(name) ? getPortals().get(name) : Portal.EMPTY;
	}

	public static void registerPortal(Portal portal) {
		if (getPortals().keySet()
			.contains(portal.getName())) {
			throw new IllegalArgumentException("Each portal must have its unique name");
		}
		getInstance().portals.put(portal.getName(), portal);
		if (RadiusPortal.addWithSave()) savePortals();
	}

	public static void removePortal(String name) {
		if (getPortal(name).isValid()) {
			getInstance().portals.remove(name);
			if (RadiusPortal.addWithSave()) savePortals();
		}
	}

	public static void savePortals() {
		if (!Singleton.f.exists()) try {
			Singleton.f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Singleton.f))) {
			oos.writeObject(Singleton.instance);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadPortals() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Singleton.f))) {
			Singleton.instance.portals.putAll(((PortalManager) ois.readObject()).portals);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
