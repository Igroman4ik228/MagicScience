package com.magicscience.magicsciencemod.mixins;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class CollisionMixin {
    /**
     * Оптимизированная версия collideBoundingBox для частиц.
     * Убираем сложную обработку всех VoxelShape,
     * оставляем только землю и границу мира.
     *
     * @author -
     * @reason test
     */
//    @Overwrite
//    public static @NotNull Vec3 collideBoundingBox(@Nullable Entity entity,
//                                                   Vec3 deltaMovement,
//                                                   AABB collisionBox,
//                                                   @NotNull Level level,
//                                                   List<?> potentialHits) {
//        // Проверка на границу мира
//        WorldBorder worldBorder = level.getWorldBorder();
//        if (entity!=null && !worldBorder.isWithinBounds(collisionBox.expandTowards(deltaMovement))) {
//            return magicScience$clampToWorldBorder(deltaMovement, collisionBox, worldBorder);
//        }
//
//        // Зона поиска блоков
//        AABB expanded = collisionBox.expandTowards(deltaMovement).inflate(0.001D);
//        int minX = (int) Math.floor(expanded.minX);
//        int maxX = (int) Math.floor(expanded.maxX);
//        int minY = (int) Math.floor(expanded.minY);
//        int maxY = (int) Math.floor(expanded.maxY);
//        int minZ = (int) Math.floor(expanded.minZ);
//        int maxZ = (int) Math.floor(expanded.maxZ);
//
//        double dx = deltaMovement.x;
//        double dy = deltaMovement.y;
//        double dz = deltaMovement.z;
//
//        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
//
//        for (int x = minX; x <= maxX; x++) {
//            for (int y = minY; y <= maxY; y++) {
//                for (int z = minZ; z <= maxZ; z++) {
//                    pos.set(x, y, z);
//                    BlockState state = level.getBlockState(pos);
//
//                    if (state.isAir()) continue;
//
//                    // Оптимизация: если полный куб, используем сразу AABB
//                    if (state.isCollisionShapeFullBlock(level, pos)) {
//                        AABB blockBox = new AABB(x, y, z, x + 1, y + 1, z + 1);
//                        dx = magicScience$collideAxis(collisionBox, dx, 1, 0, 0, blockBox);
//                        dy = magicScience$collideAxis(collisionBox, dy, 0, 1, 0, blockBox);
//                        dz = magicScience$collideAxis(collisionBox, dz, 0, 0, 1, blockBox);
//                        continue;
//                    }
//
//                    // Получаем форму блока (мгновенно пустая для воздуха/проницаемых)
//                    VoxelShape shape = state.getCollisionShape(level, pos);
//                    if (shape.isEmpty()) continue;
//
//                    // Для нестандартных блоков — точные AABB
//                    for (AABB blockBox : shape.toAabbs()) {
//                        blockBox = blockBox.move(x, y, z);
//                        dx = magicScience$collideAxis(collisionBox, dx, 1, 0, 0, blockBox);
//                        dy = magicScience$collideAxis(collisionBox, dy, 0, 1, 0, blockBox);
//                        dz = magicScience$collideAxis(collisionBox, dz, 0, 0, 1, blockBox);
//                    }
//                }
//            }
//        }
//
//        return new Vec3(dx, dy, dz);
//    }
//
//    /**
//     * Подрезка движения вдоль одной оси.
//     */
//    @Unique
//    private static double magicScience$collideAxis(AABB entityBox,
//                                                   double movement,
//                                                   int axisX, int axisY, int axisZ,
//                                                   AABB blockBox) {
//        if (movement==0.0D) return 0.0D;
//
//        AABB moved = entityBox.move(axisX * movement, axisY * movement, axisZ * movement);
//        if (!moved.intersects(blockBox)) {
//            return movement; // не сталкиваемся
//        }
//
//        if (movement > 0.0D) {
//            if (axisX==1) return Math.min(movement, blockBox.minX - entityBox.maxX);
//            if (axisY==1) return Math.min(movement, blockBox.minY - entityBox.maxY);
//            return Math.min(movement, blockBox.minZ - entityBox.maxZ);
//        } else {
//            if (axisX==1) return Math.max(movement, blockBox.maxX - entityBox.minX);
//            if (axisY==1) return Math.max(movement, blockBox.maxY - entityBox.minY);
//            return Math.max(movement, blockBox.maxZ - entityBox.minZ);
//        }
//    }
//
//    /**
//     * Ограничение движения по границе мира (XZ).
//     */
//    @Unique
//    private static @NotNull Vec3 magicScience$clampToWorldBorder(Vec3 delta, AABB box, WorldBorder border) {
//        double dx = delta.x;
//        double dy = delta.y;
//        double dz = delta.z;
//
//        if (box.minX + dx < border.getMinX() || box.maxX + dx > border.getMaxX()) dx = 0.0D;
//        if (box.minZ + dz < border.getMinZ() || box.maxZ + dz > border.getMaxZ()) dz = 0.0D;
//
//        return new Vec3(dx, dy, dz);
//    }
}
