package com.magicscience.magicsciencemod.client.particles.aspecthandlers;

import com.magicscience.magicsciencemod.client.particles.MagicParticle;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.ConfigBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.block.CoreBlockFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.AttributeEntityFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.filters.entity.ConfigEntityFilter;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.BlockCollisionHandler;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.EntityCollisionHandler;
import com.magicscience.magicsciencemod.client.particles.aspecthandlers.handlers.ICollisionHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class AspectProcessor {
    private final @NotNull Collection<ICollisionHandler> handlers;

    public AspectProcessor(@NotNull MagicParticle particle) {
        var spellData = particle.getSpellData();
        ClientLevel level = particle.getLevel();

        var entityFilter = new ConfigEntityFilter()
            .and(new AttributeEntityFilter(spellData.attributeIds()));

        var blockFilter = new ConfigBlockFilter()
            .and(new CoreBlockFilter(spellData.coreId()));
        
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
