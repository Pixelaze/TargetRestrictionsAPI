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

import org.bukkit.GameMode;
import org.bukkit.entity.*;

public class VanillaTargetManager implements TargetManager {
    @Override
    public String getPrefix() {
        return "vanilla";
    }

    /**
     * On any target-player always return true.
     * <p>
     * On any non-AI targets always return false.
     * <p>
     * If caster is a player, he will be in allies relations
     * with villager, wandering traders and
     * tameable mobs, if owner is the caster,
     * in enemies relations with any hostile mobs, and
     * in neutral with others.
     * <p>
     * If caster is hostile mob, always return true for offensive
     * interactions, with others will be in allied relations with other
     * hostile mobs and enemies with others.
     * <p>
     * If caster is peaceful mob will be in allied relations with
     * other peaceful mobs and enemies with hostile.
     */
    @Override
    public boolean canTarget(LivingEntity caster, LivingEntity target, InteractionType interactionType) {
        // Always true, if target is a player
        if (target instanceof Player) {
            return true;
        }

        // Always false, if target not have AI
        if (!(target.hasAI())) {
            return false;
        }

        RelationType relations = RelationType.NEUTRAL;

        // If caster is a player
        if (caster instanceof Player) {
            // Always allied with villagers and wandering traders
            if (target instanceof Villager || target instanceof WanderingTrader) {
                relations = RelationType.ALLY;
            }
            // Always allied with tamed creatures
            else if (target instanceof Tameable) {
                AnimalTamer owner = ((Tameable) target).getOwner();
                if (owner.getUniqueId().equals(target.getUniqueId())) {
                    relations = RelationType.ALLY;
                }
            }
            // Always enemy with hostile creature
            else if (target instanceof Monster) {
                relations = RelationType.ENEMY;
            }
        }
        // If caster is a hostile mob
        else if (caster instanceof Monster) {
            // Hostile always enemy with all,
            // except the other hostiles
            relations = RelationType.ENEMY;

            // If offensive action, always true
            if (interactionType.canTargetEnemy()) {
                return true;
            }

            // If target is a monster, it will be allied relations
            if (target instanceof Monster) {
                relations = RelationType.ALLY;
            }
        }
        // If caster is not hostile mob and not player( -> peaceful mob)
        else {
            // Peaceful always ally with all,
            // except the hostile mobs.
            relations = RelationType.ALLY;

            // If target is a monster, it will be enemy relations
            if (target instanceof Monster) {
                relations = RelationType.ENEMY;
            }
        }

        return relations.checkInteractionType(interactionType);
    }

    /**
     * Dead entities, Players in creative
     * or spectator mode is not targetable,
     * other always targetable.
     */
    @Override
    public boolean isTargetable(LivingEntity target) {
        if(target.isDead()) {
            return false;
        }

        if (target instanceof Player) {
            GameMode gameMode = ((Player) target).getGameMode();
            return !(gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR);
        }
        return true;
    }
}
