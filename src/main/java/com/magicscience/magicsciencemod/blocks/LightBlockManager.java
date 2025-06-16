package com.magicscience.magicsciencemod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraftforge.fml.common.Mod;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mod.EventBusSubscriber
public class LightBlockManager {
    // Лучше использовать потокобезопасную Map для работы на сервере
    private static final Map<UUID, BlockPos> activeLightBlockPositions = new ConcurrentHashMap<>();

    private static final int LIGHT_LEVEL = 12;

    /**
     * Флаги для обновления блока в мире:
     * 1 (FLAG_BLOCK_UPDATE) — обновить блок (отправить обновление клиентам).
     * 2 (FLAG_NOTIFY_NEIGHBORS) — уведомить соседние блоки об изменении.
     * 4 (FLAG_SEND_TO_CLIENT) — отправить изменение на клиент (может дублировать 1).
     * 8 (FLAG_NO_RERENDER) — не перерисовывать блок визуально.
     **/
    private static final int FLAG_UPDATE = 3;

    public static void placeLightBlock(ServerLevel level, UUID particleId, BlockPos newPos) {
        BlockPos oldPos = activeLightBlockPositions.get(particleId);

        // Убираем старый, если он есть
        removeLightBlockAtPos(level, oldPos);

        if (!level.getBlockState(newPos).isAir()) return;

        // Ставим новый блок
        level.setBlock(
                newPos,
                Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, LIGHT_LEVEL),
                FLAG_UPDATE
        );

        // Обновляем карту
        activeLightBlockPositions.put(particleId, newPos);
    }

    public static void removeLightBlock(ServerLevel level, UUID particleId) {
        removeLightBlockById(level, particleId);
    }

    public static void removeAllLights(ServerLevel level) {
        for (Map.Entry<UUID, BlockPos> entry : activeLightBlockPositions.entrySet()) {
            removeLightBlockAtPos(level, entry.getValue());
        }
        activeLightBlockPositions.clear();
    }

    // Удаляет световой блок по UUID, если он существует
    private static void removeLightBlockById(ServerLevel level, UUID particleId) {
        BlockPos pos = activeLightBlockPositions.remove(particleId);
        removeLightBlockAtPos(level, pos);
    }

    // Удаляет световой блок на указанной позиции, если там действительно LightBlock
    private static void removeLightBlockAtPos(ServerLevel level, BlockPos pos) {
        if (pos != null && level.getBlockState(pos).getBlock() instanceof LightBlock) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), FLAG_UPDATE);
        }
    }
}
