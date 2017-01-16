package me.ranol.rportal.api;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class WrappedLocation implements Serializable {
	private static final long serialVersionUID = -7074757370116505007L;
	private double x;
	private double y;
	private double z;
	private String world;
	private float yaw;
	private float pitch;

	public WrappedLocation() {
		this(0, 0, 0, "world");
	}

	public WrappedLocation(Location l) {
		this(l.getX(), l.getY(), l.getZ(), l.getWorld()
			.getName(), l.getYaw(), l.getPitch());
	}

	public WrappedLocation(double x, double y, double z, String world) {
		this(x, y, z, world, 0, 0);
	}

	public WrappedLocation(double x, double y, double z, String world, float yaw, float pitch) {
		apply(x, y, z, world, yaw, pitch);
	}

	public void apply(Location l) {
		apply(l.getX(), l.getY(), l.getZ(), l.getWorld()
			.getName(), l.getYaw(), l.getPitch());
	}

	public void apply(double x, double y, double z, String world) {
		apply(x, y, z, world, 0, 0);
	}

	public void apply(double x, double y, double z, String world, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public void setWorld(World world) {
		this.world = world.getName();
	}

	public WrappedLocation add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public WrappedLocation subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Block getBlock() {
		return getBukkitLocation().getBlock();
	}

	public Location getBukkitLocation() {
		return new Location(getWorld(), x, y, z, yaw, pitch);
	}

	public Vector getDirection() {
		return getBukkitLocation().getDirection();
	}

	public Vector toVector() {
		return getBukkitLocation().toVector();
	}

	public double distanceSquared(WrappedLocation l) {
		return getBukkitLocation().distanceSquared(l.getBukkitLocation());
	}

	public double distanceSquared(Location l) {
		return getBukkitLocation().distanceSquared(l);
	}
}
