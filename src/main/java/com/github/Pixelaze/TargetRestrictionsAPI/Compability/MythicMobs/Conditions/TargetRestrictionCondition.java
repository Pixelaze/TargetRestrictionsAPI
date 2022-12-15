/*
 * Copyright (c) 2022 Pixelaze
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.Pixelaze.TargetRestrictionsAPI.Compability.MythicMobs.Conditions;

import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.TargetManager;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.bukkit.utils.lib.lang3.Validate;
import io.lumine.mythic.core.skills.SkillCondition;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.InteractionType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class TargetRestrictionCondition extends SkillCondition implements IEntityComparisonCondition {
    private final InteractionType interactionType;
    private final TargetManager targetManager;

    public TargetRestrictionCondition(String line, MythicLineConfig config, TargetManager targetManager) {
        super(line);

        this.targetManager = targetManager;

        String interactionString = config.getString(new String[] {"interactionType", "it",
                "interaction", "type"}, "NEUTRAL");
        this.interactionType = InteractionType.convertFromString(interactionString);
    }

    @Override
    public boolean check(AbstractEntity caster, AbstractEntity target) {
        Entity casterEntity = caster.getBukkitEntity();
        Entity targetEntity = target.getBukkitEntity();

        Validate.isTrue(casterEntity instanceof LivingEntity, "Caster '" + caster.getName() + "' need to be a LivingEntity!");
        Validate.isTrue(targetEntity instanceof LivingEntity, "Target '" + target.getName() + "'need to be a LivingEntity!");

        return (targetManager.isTargetable((LivingEntity) casterEntity) &&
                targetManager.canTarget((LivingEntity) casterEntity, (LivingEntity) targetEntity, interactionType));
    }
}
