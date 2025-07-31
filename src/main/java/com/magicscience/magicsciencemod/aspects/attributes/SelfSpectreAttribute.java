package com.magicscience.magicsciencemod.aspects.attributes;

import com.magicscience.magicsciencemod.aspects.attributes.unique.IFilterMagicAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

public class SelfSpectreAttribute extends BaseMagicAttribute implements IFilterMagicAttribute {
    public SelfSpectreAttribute(int manaCost, int stack) {
        super(manaCost, stack);
    }

    public SelfSpectreAttribute() {
        this(10, 1);
    }

    public SelfSpectreAttribute(int stack) {
        this(10, stack);
    }

    @Override
    @NotNull
    @OnlyIn(Dist.CLIENT)
    public Predicate<Entity> getEntityFilter(@NotNull Collection<Integer> targetIds) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player==null)
            return entity -> true;

        return entity -> !(player.getId()== entity.getId());
    }

    @Override
    public IMagicAttribute cloneWithArguments(Object... args) {
        return new SelfSpectreAttribute((int) args[0]);
    }
}
