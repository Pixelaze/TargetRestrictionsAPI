/*
 * Copyright (c) 2022 Pixelaze
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.Pixelaze.TargetRestrictionsAPI;


import com.github.Pixelaze.TargetRestrictionsAPI.EventListeners.MythicMobsEventListener;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.TargetManager;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.MythicMobsTargetManager;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.TownyTargetManager;
import com.github.Pixelaze.TargetRestrictionsAPI.TargetManager.VanillaTargetManager;
import com.palmergames.bukkit.towny.TownyAPI;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class TargetRestrictionsAPI extends JavaPlugin {

    public static TargetRestrictionsAPI plugin;

    private MythicBukkit mythicMobs;
    private TownyAPI towny;

    private boolean HAS_TOWNY = false;
    private boolean HAS_MYTHIC_MOBS = false;
    private boolean HAS_WORLD_GUARD = false;
    private boolean HAS_CMI = false;
    private boolean HAS_FACTIONS = false;

    List<TargetManager> targetManagers = new ArrayList<>();

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Adding Vanilla Target Manager
        VanillaTargetManager vanillaTargetManager = new VanillaTargetManager();
        registerTargetManager(vanillaTargetManager);

        // Connecting to MythicMobs
        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            getLogger().info("Connected to MythicMobs!");
            HAS_MYTHIC_MOBS = true;
            mythicMobs = MythicBukkit.inst();
            Bukkit.getPluginManager().registerEvents(new MythicMobsEventListener(), this);

            // Adding MythicMobs Target Manager
            MythicMobsTargetManager mythicMobsTargetManager = new MythicMobsTargetManager();
            registerTargetManager(mythicMobsTargetManager);
        }

        // Connecting to Towny
        if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
            HAS_TOWNY = true;
            towny = TownyAPI.getInstance();
            getLogger().info("Connected to Towny!");

            // Adding Towny Target Manager
            TownyTargetManager townyTargetManager = new TownyTargetManager();
            registerTargetManager(townyTargetManager);
        }

        getLogger().info("TargetRestrictionsAPI enabled!");
    }

    public void registerTargetManager(TargetManager manager) {
        if (manager == null) {
            throw new NullPointerException("While trying to register TargetManager," +
                    "the manager has been null!");
        }

        for (TargetManager targetManager :
                targetManagers) {
            if (manager.getPrefix().equalsIgnoreCase(targetManager.getPrefix())) {
                throw new IllegalArgumentException("While trying to register TargetManager," +
                        "founded another manager with similar prefix: '" +
                        targetManager.getPrefix().toLowerCase() + "'!");
            }
        }

        targetManagers.add(manager);
        getLogger().info("Registered a new TargetManager with prefix " + manager.getPrefix());
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling TargetRestrictionsAPI...");
    }

    // hasPlugin functions
    public boolean hasTowny() {
        return HAS_TOWNY;
    }

    public boolean hasMythicMobs() {
        return HAS_MYTHIC_MOBS;
    }

    public boolean hasWorldGuard() {
        return HAS_WORLD_GUARD;
    }

    public boolean hasCMI() {
        return HAS_CMI;
    }

    public boolean hasFactions() {
        return HAS_FACTIONS;
    }

    // getters
    public TownyAPI getTowny() {
        if (!HAS_TOWNY) {
            throw new NullPointerException("Towny is not enabled for TownyRestrictionsAPI! " +
                    "For avoid this problem, use .hasTowny() or .getTownyOrNull().");
        }
        return towny;
    }

    public MythicBukkit getMythicMobs() {
        if (!HAS_MYTHIC_MOBS) {
            throw new NullPointerException("MythicMobs is not enabled for TownyRestrictionsAPI! " +
                    "For avoid this problem, use .hasMythicMobs() or .getMythicMobsOrNull().");
        }
        return mythicMobs;
    }

    public List<TargetManager> getTargetManagersList() {
        return targetManagers;
    }

    public TargetManager getTargetManagerByPrefix(String prefix) {
        TargetManager returnTargetManager = getTargetManagerByPrefixOrNull(prefix);
        if (returnTargetManager == null) {
            throw new IllegalArgumentException("No found TargetManager with prefix '" + prefix + "'!");
        }
        return returnTargetManager;
    }

    public static TargetRestrictionsAPI getInstance() {
        return plugin;
    }

    // getters with null
    @Nullable
    public TargetManager getTargetManagerByPrefixOrNull(String prefix) {
        for (TargetManager targetManager:
             targetManagers) {
            if (targetManager.getPrefix().equalsIgnoreCase(prefix)) {
                return targetManager;
            }
        }

        return null;
    }

    @Nullable
    public TownyAPI getTownyOrNull() {
        if (!HAS_TOWNY) {
            return null;
        }
        return towny;
    }

    @Nullable
    public MythicBukkit getMythicMobsOrNull() {
        if (!HAS_MYTHIC_MOBS) {
            return null;
        }
        return mythicMobs;
    }
}
