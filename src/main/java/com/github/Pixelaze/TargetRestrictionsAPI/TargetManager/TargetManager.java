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

import org.bukkit.entity.LivingEntity;

public interface TargetManager {

    /**
     * Used for get a prefix of current Target Manager.
     * Prefix specifically used for MythicMobs conditions system,
     * e.g. Target Manager with prefix towny will be called with
     * MythicMob condition townyCanTargetRestrictions.
     *
     * @return prefix of current Target Manager
     */
    String getPrefix();

    /**
     * Called, when one entity tries to target another entity.
     * This should be used to check, if one entity can target
     * another entity(like allies can't target each other to
     * make harm interaction).
     *
     * @param caster            entity, that tries to target another
     * @param target            targeted entity
     * @param interactionType   type of interaction
     * @return                  true, if interaction is right to target and caster
     */
    boolean canTarget(LivingEntity caster, LivingEntity target, InteractionType interactionType);

    /**
     * Called to check, if that entity can be a target, for instance
     * you can't target god-mode users, creative-mode, etc...
     *
     * @param target    target to check
     * @return          true, if this entity can be targeted by any interaction
     */
    boolean isTargetable(LivingEntity target);
}
