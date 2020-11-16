package com.swordfish.lemuroid.lib.saves

import com.swordfish.lemuroid.lib.library.CoreID
import com.swordfish.lemuroid.lib.library.db.entity.Game

/*
   Why does this class exist? Because shit happens and we want to make sure we are prepared.
   This is the issue:

   User enables autosave, plays pokemon, disables autosave, plays pokemon for 10h, saves in game.
   Reenables autosave and he loses 10h worth of game.

   If we detect a more recent SRAM file, we basically avoid loading the state. This is also handy,
   if different cores share the same SRAM file. */
class SavesCoherencyEngine(val savesManager: SavesManager, val statesManager: StatesManager) {

    fun hasMoreRecentInGameFile(game: Game, coreID: CoreID): Boolean {
        val autoSRAM = savesManager.getSaveRAMInfo(game)
        val autoSave = statesManager.getAutoSaveInfo(game, coreID)
        return autoSRAM.date > autoSave.date + TOLERANCE
    }

    companion object {
        private const val TOLERANCE = 30L * 1000L
    }
}