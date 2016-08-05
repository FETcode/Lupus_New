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

import java.util.List;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public interface DataProvider {

    List<Player> getAlivePlayers();
    List<Player> getRecentlyKilledPlayers();
    List<Player> getHighestVotedPlayers();

    Player getLastLynched();
    void lynch(Player player);
    boolean savage(Player player);
    boolean probe(Player player);

    Player getClairvoyant();
    Player getGuard();
    Player getMedium();

    String getVillage();
    void setVillage(String village);

    List<String> getMessages();

    int getNightCounter();
    void incrementNightCounter();

    int getDayCounter();
    void incrementDayCounter();

    int getNightInsideCounter();
    void incrementNightInsideCounter();
    void resetNightInsideCounter();

    int getDayInsideCounter();
    void incrementDayInsideCounter();
    void resetDayInsideCounter();

    int getWolvesLeft();
    int getVillagersLeft();

    boolean isNight();
    void setNight(boolean night);

    boolean isGoodEnd();
    void setGoodEnd(boolean good);

    String getLastTextInput();
    void setLastTextInput(String input);

    int getPlayerCount();
    void setPlayerCount(int count);

    List<Card> getCharacters();

    FragmentSwitcher getFragmentSwitcher();

}
