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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.FET.leonardo.scurcola.fragment.CharacterSelectionFragment;
import com.FET.leonardo.scurcola.fragment.GameFragment;
import com.FET.leonardo.scurcola.fragment.GameOverFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersClairvoyantFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersGuardFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersLynchFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersVoteFragment;
import com.FET.leonardo.scurcola.fragment.ListPlayersWolvesFragment;
import com.FET.leonardo.scurcola.fragment.MainFragment;
import com.FET.leonardo.scurcola.fragment.NameSelectionFragment;
import com.FET.leonardo.scurcola.fragment.PlayerSelectionFragment;
import com.FET.leonardo.scurcola.fragment.RandomAssignmentFragment;
import com.FET.leonardo.scurcola.fragment.VillageFragment;


/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class FragmentSwitcher {

    private final FragmentManager fragmentManager;
    private Fragment currentFragment;

    public FragmentSwitcher(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void clairvoyant() {
        currentFragment = new ListPlayersClairvoyantFragment();
        transact();
    }

    public void guard() {
        currentFragment = new ListPlayersGuardFragment();
        transact();
    }

    public void wolves() {
        currentFragment = new ListPlayersWolvesFragment();
        transact();
    }

    public void vote() {
        currentFragment = new ListPlayersVoteFragment();
        transact();
    }

    public void lynch() {
        currentFragment = new ListPlayersLynchFragment();
        transact();
    }

    public void playersList() {
        currentFragment = new ListPlayersFragment();
        transact();
    }

    public void gameOver() {
        currentFragment = new GameOverFragment();
        transact();
    }

    public void game() {
        currentFragment = new GameFragment();
        transact();
    }

    public void main() {
        currentFragment = new MainFragment();
        transact();
    }

    public void village() {
        currentFragment = new VillageFragment();
        transact();
    }

    public void playerSelection() {
        currentFragment = new PlayerSelectionFragment();
        transact();
    }

    public void nameSelection() {
        currentFragment = new NameSelectionFragment();
        transact();
    }

    public void randomAssignment() {
        currentFragment = new RandomAssignmentFragment();
        transact();
    }

    public void characterSelection() {
        currentFragment = new CharacterSelectionFragment();
        transact();
    }

    public void back(){
        fragmentManager.popBackStackImmediate();
    }

    private void transact() {
//TODO add animation here

        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).replace(android.R.id.content, currentFragment).addToBackStack(null).commit();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
