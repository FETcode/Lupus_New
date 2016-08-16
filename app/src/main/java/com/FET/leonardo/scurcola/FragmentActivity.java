
/*
 * Copyright 2016 by FET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *  Project created by FET on 6th November 2015
 *  Code updated on 4th August 2016
 */


/*
 * Copyright 2016 by F43nd1r (https://github.com/F43nd1r)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.FET.leonardo.scurcola;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 20.07.2016.
 *
 * @author F43nd1r
 */
public class FragmentActivity extends AppCompatActivity implements DataProvider, View.OnClickListener {

    private static final String VILLAGE = "VILLAGE";
    private static final String PLAYERS = "PLAYERS";
    private static final String RECENTLY_KILLED = "RECENTLY_KILLED";
    private static final String HIGHEST = "HIGHEST";
    private static final String MESSAGES = "MESSAGES";

    private static final String LAST_LYNCHED = "LAST_LYNCHED";

    private static final String NIGHT_COUNTER = "NIGHT_COUNTER";
    private static final String DAY_COUNTER = "DAY_COUNTER";
    private static final String NIGHT_INSIDE_COUNTER = "NIGHT_INSIDE_COUNTER";
    private static final String DAY_INSIDE_COUNTER = "DAY_INSIDE_COUNTER";
    private static final String NIGHT = "NIGHT";

    private static final String GOOD_WIN = "GOOD_WIN";
    private static final String LAST_TEXT = "LAST_TEXT";
    private static final String PLAYER_COUNT = "PLAYER_COUNT";
    private static final String CHARACTERS = "CHARACTERS";

    private static final String FRAGMENT_MANAGER = "FRAGMENT_HISTORY";


    private List<Player> players; // All the active players
    private List<Player> recentlyKilled;
    private List<Player> highest;

    private String village;

    private ArrayList<String> messages;

    // Players
    private Player lastLynched;

    // Counters
    private int nightCounter;
    private int dayCounter;
    private int nightInsideCounter;
    private int dayInsideCounter;
    private boolean night;

    private boolean isGoodEnd;
    private String lastText;
    private int playerCount;
    private List<Card> characters;

    private FragmentSwitcher fragmentSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentSwitcher = new FragmentSwitcher(getSupportFragmentManager());

        // Probably initialize members with default values for a new instance
        initializeVariables();

        SharedPreferences prefs = getSharedPreferences("X", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        // Restore value of members from saved state
        village = prefs.getString(VILLAGE, "");

        // Lists
        String playersJSON = prefs.getString(PLAYERS, null);
        String recentlyKilledJSON = prefs.getString(RECENTLY_KILLED, null);
        String highestJSON = prefs.getString(HIGHEST, null);
        String messagesJSON = prefs.getString(MESSAGES, null);

        String lastLynchedJSON = prefs.getString(LAST_LYNCHED, null);

        Type type = new TypeToken<List<Player>>() {
        }.getType();
        Type type1 = new TypeToken<Player>() {
        }.getType();
        Type arrayListString = new TypeToken<ArrayList<String>>() {
        }.getType();
        Type cardList = new TypeToken<ArrayList<Card>>() {
        }.getType();

        if (playersJSON != null) {
            players = gson.fromJson(playersJSON, type);
        }
        if (recentlyKilledJSON != null) {
            recentlyKilled = gson.fromJson(recentlyKilledJSON, type);
        }
        if (highestJSON != null) {
            highest = gson.fromJson(highestJSON, type);
        }
        if (messagesJSON != null) {
            messages = gson.fromJson(messagesJSON, arrayListString);
        }
        // Players
        if (lastLynchedJSON != null) {
            lastLynched = gson.fromJson(lastLynchedJSON, type1);
        }

        // Counters
        nightCounter = prefs.getInt(NIGHT_COUNTER, 1);
        dayCounter = prefs.getInt(DAY_COUNTER, 1);
        nightInsideCounter = prefs.getInt(NIGHT_INSIDE_COUNTER, 1);
        dayInsideCounter = prefs.getInt(DAY_INSIDE_COUNTER, 1);
        night = prefs.getBoolean(NIGHT, true);

        isGoodEnd = prefs.getBoolean(GOOD_WIN, false);
        lastText = prefs.getString(LAST_TEXT, "");
        playerCount = prefs.getInt(PLAYER_COUNT, 9);
        String charactersJSON = prefs.getString(CHARACTERS, null);
        if (charactersJSON != null) {
            characters = gson.fromJson(charactersJSON, cardList);
        }

        fragmentSwitcher.restore(gson, prefs, FRAGMENT_MANAGER);
    }


    private void initializeVariables() {
        messages = new ArrayList<>();
        nightCounter = 1;
        dayCounter = 1;
        nightInsideCounter = 1;
        dayInsideCounter = 1;
        night = true;
        recentlyKilled = new ArrayList<>();
        highest = new ArrayList<>();
        players = new ArrayList<>();
        characters = new ArrayList<>();
        village = null;
        isGoodEnd = false;
        lastText = "";
        playerCount = 9;
        lastLynched = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("X", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        editor.putString(VILLAGE, village);

        // Lists
        String playersJSON = gson.toJson(players);
        String recentlyKilledJSON = gson.toJson(recentlyKilled);
        String highestJSON = gson.toJson(highest);
        String messagesJSON = gson.toJson(messages);

        editor.putString(PLAYERS, playersJSON);
        editor.putString(RECENTLY_KILLED, recentlyKilledJSON);
        editor.putString(HIGHEST, highestJSON);
        editor.putString(MESSAGES, messagesJSON);

        // Players
        editor.putString(LAST_LYNCHED, gson.toJson(lastLynched));

        // Counters
        editor.putInt(NIGHT_COUNTER, nightCounter);
        editor.putInt(DAY_COUNTER, dayCounter);
        editor.putInt(NIGHT_INSIDE_COUNTER, nightInsideCounter);
        editor.putInt(DAY_INSIDE_COUNTER, dayInsideCounter);
        editor.putBoolean(NIGHT, night);

        editor.putBoolean(GOOD_WIN, isGoodEnd);
        editor.putString(LAST_TEXT, lastText);
        editor.putInt(PLAYER_COUNT, playerCount);
        editor.putString(CHARACTERS, gson.toJson(characters));


        fragmentSwitcher.save(gson, editor, FRAGMENT_MANAGER);
        editor.apply();
    }

    @Override
    public List<Player> getAlivePlayers() {
        return players;
    }

    @Override
    public List<Player> getRecentlyKilledPlayers() {
        return recentlyKilled;
    }

    @Override
    public List<Player> getHighestVotedPlayers() {
        return highest;
    }

    @Override
    public Player getLastLynched() {
        return lastLynched;
    }

    @Override
    public void lynch(Player player) {
        lastLynched = player;
        // Kill the player
        lastLynched.setLifeStatus(false);
        players.remove(player);
    }

    @Override
    public boolean savage(Player player) {
        if (player.getCard() == Card.Werehamster || player.getProtected()) {
            player.setProtected(false);
            return false;
        }
        player.setLifeStatus(false);
        recentlyKilled.add(player);
        players.remove(player);
        return true;
    }

    @Override
    public boolean probe(Player player) {
        if (player.getCard() == Card.Werehamster) {
            player.setLifeStatus(false);
            players.remove(player);
            recentlyKilled.add(player);
        }
        return player.getCard() == Card.Wolf;
    }

    @Override
    public Player getClairvoyant() {
        for (Player p : players) {
            if (p.getCard() == Card.Clairvoyant) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Player getGuard() {
        for (Player p : players) {
            if (p.getCard() == Card.Guard) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Player getMedium() {
        for (Player p : players) {
            if (p.getCard() == Card.Medium) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String getVillage() {
        return village;
    }

    @Override
    public void setVillage(String village) {
        this.village = village;
    }

    @Override
    public List<String> getMessages() {
        return messages;
    }

    @Override
    public int getNightCounter() {
        return nightCounter;
    }

    @Override
    public void incrementNightCounter() {
        nightCounter++;
    }

    @Override
    public int getDayCounter() {
        return dayCounter;
    }

    @Override
    public void incrementDayCounter() {
        dayCounter++;
    }

    @Override
    public int getNightInsideCounter() {
        return nightInsideCounter;
    }

    @Override
    public void incrementNightInsideCounter() {
        nightInsideCounter++;
    }

    @Override
    public void resetNightInsideCounter() {
        nightInsideCounter = 1;
    }

    @Override
    public int getDayInsideCounter() {
        return dayInsideCounter;
    }

    @Override
    public void incrementDayInsideCounter() {
        dayInsideCounter++;
    }

    @Override
    public void resetDayInsideCounter() {
        dayInsideCounter = 1;
    }

    @Override
    public int getWolvesLeft() {
        int wolvesLeft = 0;
        for (Player player : players) {
            if (player.getCard() == Card.Wolf) {
                wolvesLeft++;
            }
        }
        return wolvesLeft;
    }

    @Override
    public int getVillagersLeft() {
        int villagersLeft = 0;
        for (Player player : players) {
            if (player.getCard() != Card.Wolf) {
                villagersLeft++;
            }
        }
        return villagersLeft;
    }

    @Override
    public boolean isNight() {
        return night;
    }

    @Override
    public void setNight(boolean night) {
        this.night = night;
    }

    @Override
    public boolean isGoodEnd() {
        return isGoodEnd;
    }

    @Override
    public void setGoodEnd(boolean good) {
        isGoodEnd = good;
    }

    @Override
    public String getLastTextInput() {
        return lastText;
    }

    @Override
    public void setLastTextInput(String input) {
        lastText = input;
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public void setPlayerCount(int count) {
        playerCount = count;
    }

    @Override
    public List<Card> getCharacters() {
        return characters;
    }

    @Override
    public FragmentSwitcher getFragmentSwitcher() {
        return fragmentSwitcher;
    }

    @Override
    public void reset() {
        getSharedPreferences("X", Context.MODE_PRIVATE).edit().clear().apply();
        initializeVariables();
    }

    @Override
    public void hideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = fragmentSwitcher.getCurrentFragment();
        if (fragment instanceof View.OnClickListener) {
            ((View.OnClickListener) fragment).onClick(v);

        }
    }
}