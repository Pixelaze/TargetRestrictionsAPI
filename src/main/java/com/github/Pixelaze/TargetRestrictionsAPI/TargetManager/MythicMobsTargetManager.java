/*
 * Copyright (c) 2022 Pixelaze
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.Pixelaze.TargetRestrictionsAPI.TargetManager;

import com.github.Pixelaze.TargetRestrictionsAPI.TargetRestrictionsAPI;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class MythicMobsTargetManager implements TargetManager {

    @Override
    public String getPrefix() {
        return "mythicmobs";
    }

    /**
     * Always true, if caster and target is not MythicMobs.
     * <p>
     * Allied relations, if mobs in one faction,
     * or if one mob parent/owner of other.
     * <p>
     * Enemy relations, if one mob has target other.
     * <p>
     * Otherwise, will be always neutral.
     */
    @Override
    public boolean canTarget(LivingEntity caster, LivingEntity target, InteractionType interactionType) {
        if (!(TargetRestrictionsAPI.getInstance().hasMythicMobs())) {
            TargetRestrictionsAPI.getInstance().getLogger().warning("Tried to use MythicMobsTargetManager" +
                    "when MythicMobs disabled!");
            return true;
        }

        ActiveMob targetMythicMob = MythicBukkit.inst().getMobManager()
                .getActiveMob(caster.getUniqueId()).orElse(null);
        ActiveMob casterMythicMob = MythicBukkit.inst().getMobManager()
                .getActiveMob(target.getUniqueId()).orElse(null);

        if (targetMythicMob == null && casterMythicMob == null) {
            return true;
        }

        RelationType relations = RelationType.NEUTRAL;

        if (targetMythicMob != null && casterMythicMob != null) {
            if (isParentWithOther(casterMythicMob, target) ||
                    isParentWithOther(targetMythicMob, caster) ||
                    isInFactionWithEachOther(targetMythicMob, casterMythicMob)) {
                relations = RelationType.ALLY;
            } else if (isTargetOneOther(targetMythicMob, caster) || isTargetOneOther(casterMythicMob, target)) {
                relations = RelationType.ENEMY;
            }
        } else {
            ActiveMob first = (targetMythicMob == null) ? casterMythicMob : targetMythicMob;
            LivingEntity second = (targetMythicMob == null) ? target : caster;

            if (isParentWithOther(first, second)) {
                relations = RelationType.ALLY;
            } else if (isTargetOneOther(first, second)) {
                relations = RelationType.ENEMY;
            }
        }

        return relations.checkInteractionType(interactionType);
    }

    /**
     * Only invincible MythicMobs creatures will be
     * non-targetable.
     */
    @Override
    public boolean isTargetable(LivingEntity target) {
        if (!(TargetRestrictionsAPI.getInstance().hasMythicMobs())) {
            TargetRestrictionsAPI.getInstance().getLogger().warning("Tried use MythicMobsTargetManager" +
                    "when MythicMobs is disabled!");
            return true;
        }

        ActiveMob mythicMob = MythicBukkit.inst().getMobManager().getActiveMob(target.getUniqueId()).orElse(null);

        if (mythicMob == null) {
            return true;
        }

        return !(mythicMob.getType().getIsInvincible());
    }

    private boolean isParentWithOther(ActiveMob first, LivingEntity second) {
        UUID ownerUUID = first.getOwner().orElse(null);
        return (ownerUUID != null && (ownerUUID.equals(second.getUniqueId())));
    }

    private boolean isInFactionWithEachOther(ActiveMob first, ActiveMob second) {
        return (first.hasFaction() &&
                second.hasFaction() &&
                first.getFaction().equalsIgnoreCase(second.getFaction()));
    }

    private boolean isTargetOneOther(ActiveMob first, LivingEntity second) {
        if (!first.hasTarget()) {
            return false;
        }
        if (first.hasThreatTable()) {
            AbstractEntity target = first.getThreatTable().getTopThreatHolder();
            return (target != null && target.getUniqueId().equals(second.getUniqueId()));
        }
        AbstractEntity target = first.getEntity().getTarget();
        return (target != null && target.getUniqueId().equals(second.getUniqueId()));
    }
}
