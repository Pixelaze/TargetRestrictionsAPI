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

public enum RelationType {
    /**
     * Allies is any friends,
     * members of one guild,
     * members of WG regions, etc...
     */
    ALLY,
    /**
     * Neutral is default RelationType,
     * used when the entities don't have
     * a relationships between each other.
     */
    NEUTRAL,
    /**
     * Enemies are any enemies of the guild,
     * hostile mobs, etc...
     */
    ENEMY;

    /**
     * Checks, if for that interactions accepts an
     * a current RelationType between entities.
     * @param interaction   InteractionType to check
     * @return              true, if that InteractionType accepts that RelationType
     */
    public boolean checkInteractionType(InteractionType interaction) {
        if (this == ALLY) {
            return interaction.canTargetAlly();
        } else if (this == NEUTRAL) {
            return interaction.canTargetNeutral();
        } else if (this == ENEMY) {
            return interaction.canTargetEnemy();
        }
        //If we got here, then we have some troubles...
        throw new NullPointerException("RelationType is not found!");
    }
}
