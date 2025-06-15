//package com.magicscience.magicsciencemod.particle;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.network.FriendlyByteBuf;
//
//public class EnergyParticleData implements ParticleOptions {
//
//    public static final Deserializer<EnergyParticleData> DESERIALIZER = new Deserializer<>() {
//        @Override
//        public EnergyParticleData fromCommand(ParticleType<EnergyParticleData> type, StringReader reader) throws CommandSyntaxException {
//            reader.expect(' ');
//            float damage = reader.readFloat();
//            reader.expect(' ');
//            float radius = reader.readFloat();
//            reader.expect(' ');
//            int ownerId = reader.readInt();
//            return new EnergyParticleData(damage, radius, ownerId);
//        }
//
//        @Override
//        public EnergyParticleData fromNetwork(ParticleType<EnergyParticleData> type, FriendlyByteBuf buf) {
//            return new EnergyParticleData(buf.readFloat(), buf.readFloat(), buf.readInt());
//        }
//    };
//
//    private final float damage;
//    private final float radius;
//    private final int ownerId;
//
//    public EnergyParticleData(float damage, float radius, int ownerId) {
//        this.damage = damage;
//        this.radius = radius;
//        this.ownerId = ownerId;
//    }
//
//    public float getDamage() { return damage; }
//    public float getRadius() { return radius; }
//    public int getOwnerId() { return ownerId; }
//
//    @Override
//    public ParticleType<EnergyParticleData> getType() {
//        return ModParticles.ENERGY_PARTICLE.get();
//    }
//
//    @Override
//    public void writeToNetwork(FriendlyByteBuf buf) {
//        buf.writeFloat(damage);
//        buf.writeFloat(radius);
//        buf.writeInt(ownerId);
//    }
//
//    @Override
//    public String writeToString() {
//        return String.format("%f %f %d", damage, radius, ownerId);
//    }
//}
//
