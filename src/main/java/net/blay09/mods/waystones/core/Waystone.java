package net.blay09.mods.waystones.core;

import net.blay09.mods.waystones.api.IWaystone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class Waystone implements IWaystone {

    private final UUID waystoneUid;
    private final boolean wasGenerated;

    private RegistryKey<World> dimension;
    private BlockPos pos;

    private String name;
    private boolean isGlobal;
    private UUID ownerUid;

    public Waystone(UUID waystoneUid, RegistryKey<World> dimension, BlockPos pos, boolean wasGenerated, @Nullable UUID ownerUid) {
        this.waystoneUid = waystoneUid;
        this.dimension = dimension;
        this.pos = pos;
        this.wasGenerated = wasGenerated;
        this.ownerUid = ownerUid;
    }

    @Override
    public UUID getWaystoneUid() {
        return waystoneUid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public RegistryKey<World> getDimension() {
        return dimension;
    }

    @Override
    public boolean wasGenerated() {
        return wasGenerated;
    }

    @Override
    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    @Override
    public boolean isOwner(PlayerEntity player) {
        return ownerUid == null || player.getGameProfile().getId().equals(ownerUid) || player.abilities.isCreativeMode;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public UUID getOwnerUid() {
        return ownerUid;
    }

    public void setDimension(RegistryKey<World> dimension) {
        this.dimension = dimension;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public static IWaystone read(PacketBuffer buf) {
        UUID waystoneUid = buf.readUniqueId();
        String name = buf.readString();
        boolean isGlobal = buf.readBoolean();
        RegistryKey<World> dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(buf.readString(250)));
        BlockPos pos = buf.readBlockPos();

        Waystone waystone = new Waystone(waystoneUid, dimension, pos, false, null);
        waystone.setName(name);
        waystone.setGlobal(isGlobal);
        return waystone;
    }

    public static void write(PacketBuffer buf, IWaystone waystone) {
        buf.writeUniqueId(waystone.getWaystoneUid());
        buf.writeString(waystone.getName());
        buf.writeBoolean(waystone.isGlobal());
        buf.writeResourceLocation(waystone.getDimension().getLocation());
        buf.writeBlockPos(waystone.getPos());
    }

}
