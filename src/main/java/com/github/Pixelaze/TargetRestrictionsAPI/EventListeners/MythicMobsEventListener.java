/*
 * Copyright (c) 2022 Pixelaze
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.Pixelaze.TargetRestrictionsAPI.EventListeners;

import com.github.Pixelaze.TargetRestrictionsAPI.Compability.MythicMobs.Conditions.TargetRestrictionCondition;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.TargetManager;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetRestrictionsAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsEventListener implements Listener {
    @EventHandler
    public void mythicConditionLoadEvent(MythicConditionLoadEvent event) {
        String conditionName = event.getConditionName().toUpperCase();
        int conditionLen = event.getConditionName().indexOf(" ");
        if (conditionLen > 0) {
            conditionName = conditionName.substring(0, conditionLen);
        }

        if (conditionName.endsWith("CANTARGETRESTRICTIONS")) {
            TargetManager conditionTargetManager = null;
            for (TargetManager targetManager:
            TargetRestrictionsAPI.getInstance().getTargetManagersList()) {
                if (conditionName.startsWith(targetManager.getPrefix().toUpperCase())) {
                    conditionTargetManager = targetManager;
                    break;
                }
            }

            if (conditionTargetManager == null) {
                throw new IllegalArgumentException("Can't load Target Manager for condition '" + conditionName + "'!");
            }

            event.register(new TargetRestrictionCondition(event.getConfig().getLine(),
                    event.getConfig(), conditionTargetManager));
        }
    }
}
