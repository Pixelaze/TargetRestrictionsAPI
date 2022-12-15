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

import org.jetbrains.annotations.NotNull;

public enum InteractionType {
    /**
     * Any interaction, that can target only
     * enemies, but not allies or neutral.
     */
    FULLY_OFFENSE,
    /**
     * Any interaction, that can target enemies
     * and neutral, but not allies.
     */
    OFFENSE,
    /**
     * Any interaction, that can target only
     * neutral, but not allies or enemies.
     */
    NEUTRAL,
    /**
     * Any interaction, that can target allies
     * and neutral, but not enemies.
     */
    SUPPORT,
    /**
     * Any interaction, that can target only
     * allies, but not enemies or neutral.
     */
    FULLY_SUPPORT;

    /**
     * Checks, if that InteractionType can target a enemy.
     * @return                  true, if this InteractionType allows to target enemy
     */
    public boolean canTargetEnemy() {
        return (this == FULLY_OFFENSE || this == OFFENSE);
    }

    /**
     * Checks, if that InteractionType can target a neutral
     * @return                  true, if this InteractionType allows to target neutral
     */
    public boolean canTargetNeutral() {
        return (this == OFFENSE || this == NEUTRAL || this == SUPPORT);
    }

    /**
     * Checks, if that InteractionType can target an ally
     * @return                  true, if this InteractionType allows to target ally
     */
    public boolean canTargetAlly() {
        return (this == SUPPORT || this == FULLY_SUPPORT);
    }

    /**
     * Returns InteractionType, based on current string
     *
     * @param interactionName   must be in (ignoring case): {FULLY_OFFENSE, OFFENSE, NEUTRAL, SUPPORT, FULLY_SUPPORT}
     * @return                  interaction, based on current string
     */
    public static InteractionType convertFromString(@NotNull String interactionName) {
        if (interactionName.equalsIgnoreCase("FULLY_OFFENSE")) {
            return InteractionType.FULLY_OFFENSE;
        } else if (interactionName.equalsIgnoreCase("OFFENSE")) {
            return InteractionType.OFFENSE;
        } else if (interactionName.equalsIgnoreCase("NEUTRAL")) {
            return InteractionType.NEUTRAL;
        } else if (interactionName.equalsIgnoreCase("SUPPORT")) {
            return InteractionType.SUPPORT;
        } else if (interactionName.equalsIgnoreCase("FULLY_SUPPORT")) {
            return InteractionType.FULLY_SUPPORT;
        }
        throw new IllegalArgumentException("Can not get InteractionType for string '" +
                interactionName + "'! Please, specify it for next enum: " +
                "FULLY_OFFENSE OFFENSE NEUTRAL SUPPORT FULLY_SUPPORT");
    }
}
