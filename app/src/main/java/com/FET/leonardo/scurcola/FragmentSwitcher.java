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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class FragmentSwitcher{

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

    public void back() {
        fragmentManager.popBackStackImmediate();
        currentFragment = getFragmentByClassName(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName());
    }

    public void restore(Gson gson, SharedPreferences prefs, String key) {
        String saved = prefs.getString(key, null);
        if (saved != null) {
            List<String> classNames = gson.fromJson(saved, new TypeToken<List<String>>() {
            }.getType());
            for (String className : classNames){
                currentFragment = getFragmentByClassName(className);
                if(currentFragment != null) {
                    transact(false);
                }
            }
        }
        if(currentFragment == null){
            main();
        }
    }

    public void save(Gson gson, SharedPreferences.Editor prefEditor, String key) {
        List<String> classNames = new ArrayList<>();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            classNames.add(fragmentManager.getBackStackEntryAt(i).getName());
        }
        prefEditor.putString(key, gson.toJson(classNames));
    }

    private Fragment getFragmentByClassName(String className) {
        Fragment fragment = fragmentManager.findFragmentByTag(className);
        if (fragment == null) {
            try {
                fragment = (Fragment) Class.forName(className).newInstance();
            } catch (Exception ignored) {
            }
        }
        return fragment;
    }

    private void transact() {
        transact(true);
    }

    private void transact(boolean animate) {
        String className = currentFragment.getClass().getName();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(animate) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        transaction.replace(R.id.menuFragment, currentFragment, className)
                .addToBackStack(className)
                .commit();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}