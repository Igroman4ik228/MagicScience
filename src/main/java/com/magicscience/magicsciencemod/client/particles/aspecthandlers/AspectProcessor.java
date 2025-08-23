package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.ConfigBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.CoreBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.ConfigEntityFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.BlockCollisionHandler;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.EntityCollisionHandler;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.ICollisionHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

public class AspectProcessor {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull Collection<ICollisionHandler> handlers;

    public AspectProcessor(@NotNull MagicParticle particle) {
        var spellData = particle.getSpellData();
        ClientLevel level = particle.getLevel();

        var entityFilter = new ConfigEntityFilter()
            .and(new AttributeEntityFilter(spellData.attributeIds()));

        var blockFilter = new ConfigBlockFilter()
            .and(new CoreBlockFilter(spellData.coreId()));


        // !Position warning
        this.handlers = List.of(
            new BlockCollisionHandler(particle, spellData, blockFilter, level),
            new EntityCollisionHandler(particle, spellData, entityFilter, level)
        );
    }

    public void process() {
        for (var handler : this.handlers) {
            handler.handleCollision();
        }
    }
}
