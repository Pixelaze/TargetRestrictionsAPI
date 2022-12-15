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

import com.palmergames.bukkit.towny.object.*;
import com.palmergames.bukkit.towny.utils.CombatUtil;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetRestrictionsAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class TownyTargetManager implements TargetManager {

    @Override
    public String getPrefix() {
        return "towny";
    }

    /**
     * Always false, if Towny will prevent damage and
     * interaction is offensive.
     * <p>
     * Always true, if Towny will not prevent damage/
     * interaction is not offensive and target or caster
     * is not players.
     * <p>
     * Members of one town, nation, or allies in
     * nations be in allies relationships.
     * <p>
     * Members of enemy nations and outlaws be in
     * enemy relationships.
     * <p>
     * Other will be neutral.
     */
    @Override
    public boolean canTarget(LivingEntity caster, LivingEntity target, InteractionType interactionType) {
        if (!(TargetRestrictionsAPI.getInstance().hasTowny())) {
            TargetRestrictionsAPI.getInstance().getLogger().warning("Tried use TownyTargetManager.canTarget()," +
                    "when Towny is not enabled to TargetRestrictionsAPI!");
            return true;
        }

        EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

        // If interaction is offensive, and Towny will prevent
        // damage call, need to stop it.
        if (interactionType.canTargetEnemy() && CombatUtil.preventDamageCall(caster, target, cause)) {
            return false;
        }

        // If target is not player, or caster is not player, always true for interaction.
        if(!(target instanceof Player) || !(caster instanceof Player)) {
            return true;
        }

        // In default, all relations is neutral
        RelationType relations = RelationType.NEUTRAL;

        Resident casterResident, targetResident;
        casterResident = TargetRestrictionsAPI.getInstance().getTowny().getResident((Player) caster);
        targetResident = TargetRestrictionsAPI.getInstance().getTowny().getResident((Player) target);

        Town casterTown, targetTown;
        casterTown = casterResident.getTownOrNull();
        targetTown = targetResident.getTownOrNull();

        Nation casterNation, targetNation;
        casterNation = casterTown.getNationOrNull();
        targetNation = targetTown.getNationOrNull();

        // If caster has a town
        if (casterTown != null) {
            // Members of one town is always ally
            if (casterTown.equals(targetTown)) {
                relations = RelationType.ALLY;
            }
            // Outlaws in town is always enemies
            else if (casterTown.hasOutlaw(targetResident)) {
                relations = RelationType.ENEMY;
            }
            // If caster and target town has a nation
            else if (casterNation != null && targetNation != null) {
                // Members of one nation is always allied
                // Allies of two nations is always allied
                if (casterNation.equals(targetNation) ||
                        casterNation.hasAlly(targetNation)) {
                    relations = RelationType.ALLY;
                }
                // Enemies in nations is always enemy
                else if (casterNation.hasEnemy(targetNation)) {
                    relations = RelationType.ENEMY;
                }
            }
        }

        return relations.checkInteractionType(interactionType);
    }

    /**
     * Towny has always targetable entities.
     */
    @Override
    public boolean isTargetable(LivingEntity target) {
        if (!(TargetRestrictionsAPI.getInstance().hasTowny())) {
            TargetRestrictionsAPI.getInstance().getLogger().warning("Tried use TownyTargetManager.isTargetable()," +
                    "when Towny is not enabled to TargetRestrictionsAPI!");
            return true;
        }
        return true;
    }
}
