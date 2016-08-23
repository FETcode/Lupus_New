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

package com.FET.leonardo.scurcola.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.FET.leonardo.scurcola.Card;
import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.Player;
import com.FET.leonardo.scurcola.R;

import java.util.List;
import java.util.Random;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private ArrayAdapter<String> adapter;
    private DataProvider provider;
    private View root;
    private int lightBlue;
    private int darkBlue;
    private ValueAnimator nightAnimator;
    private ValueAnimator dayAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        final View v = inflater.inflate(R.layout.game, container, false);
        root = v.getRootView();
        ListView screen = (ListView) v.findViewById(R.id.screen);
        lightBlue = ContextCompat.getColor(getContext(), R.color.lightBlue);
        darkBlue = ContextCompat.getColor(getContext(), R.color.darkBlue);

        // Background Animation Day/Night
        dayAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), darkBlue, lightBlue);
        dayAnimator.setDuration(3000); // milliseconds
        dayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                root.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });


        nightAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), lightBlue, darkBlue);
        nightAnimator.setDuration(3000); // milliseconds
        nightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                root.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        // End Animation

        // Set first background to Night
        if (provider.getDayInsideCounter() > 1 || provider.getNightInsideCounter() == 1) {
            root.setBackgroundColor(lightBlue);
        }

        if (provider.getNightInsideCounter() > 1 || provider.getNightCounter() == 1 || !provider.isNight() && provider.getDayInsideCounter() == 1) {
            root.setBackgroundColor(darkBlue);
        }

        adapter = new ArrayAdapter<>(getActivity(), R.layout.listview_text_color, provider.getMessages());
        screen.setAdapter(adapter);
        if (provider.getNightCounter() == 1 && provider.getMessages().size() == 0) {
            greetings();
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextMessage:
                System.out.println("NextMessage - Start");
                game();
                System.out.println("NextMessage - End");
                break;
            case R.id.playersList:
                System.out.println("PlayersList - Start");
                lookUpPlayers();
                System.out.println("PlayersList - End");
                break;
            case R.id.restartButton:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getFragmentManager(), "RestartDialog");
                break;
        }
    }

    /* ---------- MAIN GAME ---------- */
    private void game() {
        if (provider.isNight()) {
            // Night starts
            night();
        } else {
            // Day starts
            day();
        }
    }

    /* ---------- Night mechanics ---------- */
    private void night() {
        switch (provider.getNightCounter()) {
            case 1: // First night, Master dies
                switch (provider.getNightInsideCounter()) {
                    case 1:
                        write(R.string.night, provider.getVillage()); // Say it's night, everybody close their eyes
                        provider.incrementNightInsideCounter();
                        break;
                    case 2:
                        masonsShow(); // Masons see each other
                        provider.incrementNightInsideCounter();
                        break;
                    case 3:
                        wolvesShow(); // Wolves see each other
                        provider.resetNightInsideCounter(); // Reset the internal counter
                        provider.incrementNightCounter(); // Increment the counter to switch to the 2nd night next time
                        provider.setNight(false); // Night has ended
                        break;
                }
                break;
            default: // All the other nights
                switch (provider.getNightInsideCounter()) {
                    case 1:
                        //Check if every Wolf has been killed
                        System.out.println("WOLVES LEFT " + provider.getWolvesLeft());
                        if (provider.getWolvesLeft() == 0) {
                            // Game over. Villagers win.
                            provider.setGoodEnd(true);
                            provider.getFragmentSwitcher().gameOver();
                        }
                        if (provider.getVillagersLeft() == provider.getWolvesLeft()) {
                            //Game over. Wolves win
                            provider.setGoodEnd(false);
                            provider.getFragmentSwitcher().gameOver();
                        }

                        nightAnimator.start();

                        write(R.string.night, provider.getVillage()); // Say it's night, everybody close their eyes
                        provider.getRecentlyKilledPlayers().clear(); // Clear all the recently killed players to get the new ones
                        provider.incrementNightInsideCounter();
                        break;
                    case 2:
                        clairvoyant(); // Clairvoyant's turn
                        provider.incrementNightInsideCounter();
                        break;
                    case 3:
                        if (provider.getClairvoyant() != null) {
                        /* --- Let him choose ---*/
                            provider.getFragmentSwitcher().clairvoyant();
                        /* --------------------- */
                        }
                        if (provider.getMedium() == null) {
                            provider.incrementNightInsideCounter();
                            provider.incrementNightInsideCounter();
                        }
                        if (provider.getGuard() == null) {
                            provider.incrementNightInsideCounter();
                            provider.incrementNightInsideCounter();
                        }
                        provider.incrementNightInsideCounter();
                        break;
                    case 4:
                        if (provider.getMedium() != null) {
                            medium(); // Medium's turn
                        }
                        provider.incrementNightInsideCounter();
                        break;
                    case 5:
                        if (provider.getMedium() != null) {
                            if (provider.getLastLynched().getCard() == Card.Wolf) { // Check if the last lynched player was a Wolf
                                // Say he is
                                write(provider.getLastLynched().getName() + " " + getString(R.string.isAWolf));
                            } else {
                                // Say he's not
                                write(provider.getLastLynched().getName() + " " + getString(R.string.isNotAWolf));
                            }
                        }
                        if (provider.getGuard() == null) {
                            provider.incrementNightInsideCounter();
                            provider.incrementNightInsideCounter();
                        }
                        provider.incrementNightInsideCounter();
                        break;
                    case 6:
                        if (provider.getGuard() != null) {
                            guard(); // Guard's turn
                        }
                        provider.incrementNightInsideCounter();
                        break;
                    case 7:
                        if (provider.getGuard() != null) {
                        /* -- Let him choose -- */
                            provider.getFragmentSwitcher().guard();
                        /* -------------------- */
                        }
                        provider.incrementNightInsideCounter();
                        break;
                    case 8:
                        wolves(); // Wolves have dinner :)
                        provider.incrementNightInsideCounter();
                        break;
                    case 9:
                        /* -- Let 'em choose -- */
                        provider.getFragmentSwitcher().wolves();
                        /* -------------------- */
                        provider.resetNightInsideCounter(); // Reset the internal counter
                        provider.incrementNightCounter(); // Increment the counter to switch to the next night
                        provider.setNight(false); // Night has ended
                        break;
                }
                break;
        }
    }

    /* ---------- Day mechanics ---------- */
    private void day() {
        switch (provider.getDayCounter()) {

            case 1: // First Day
                switch (provider.getDayInsideCounter()) {
                    case 1:
                        dayAnimator.start();
                        write(R.array.goodMorning, randInt(0, 7), provider.getVillage()); // It's morning
                        provider.incrementDayInsideCounter();
                        break;
                    case 2:
                        write(R.string.savagedMaster); // Display the Master's died
                        provider.incrementDayInsideCounter();
                        break;
                    case 3:
                        write(R.string.talk); // Players have a max of 3 minutes to discuss
                        provider.incrementDayInsideCounter();
                        break;
                    case 4:
                        provider.incrementDayInsideCounter();
                        /* -- Let 'em vote -- */
                        provider.getFragmentSwitcher().vote();
                         /* ----------------- */
                        break;
                    case 5:
                        write(R.string.speeches); // Those accused have their own speech
                        provider.incrementDayInsideCounter();
                        break;
                    case 6:
                        /* -- Let 'em choice -- */
                        provider.getFragmentSwitcher().lynch();
                        /* -------------------- */
                        provider.resetDayInsideCounter();
                        provider.incrementDayCounter();
                        provider.setNight(true);
                        break;
                }
                break;
            default: // All the other days
                switch (provider.getDayInsideCounter()) {
                    case 1:
                        if (provider.getVillagersLeft() == provider.getWolvesLeft()) {
                            //Game over. Wolves win
                            provider.setGoodEnd(false);
                            provider.getFragmentSwitcher().gameOver();
                        }
                        dayAnimator.start();

                        write(R.array.goodMorning, randInt(0, 7), provider.getVillage()); // It's morning
                        provider.incrementDayInsideCounter();
                        break;
                    case 2:
                        List<Player> recentlyKilled = provider.getRecentlyKilledPlayers();
                        if (recentlyKilled.size() == 0) {
                            write(getString(R.string.noKilled));
                        } else {
                            for (int i = 0; i < recentlyKilled.size(); i++) {
                                write(recentlyKilled.get(i).getName() + " " + getString(R.string.killed));
                            }
                        }
                        provider.incrementDayInsideCounter();
                        break;
                    case 3:
                        write(R.string.talk); // Players have a max of 3 minutes to discuss
                        provider.incrementDayInsideCounter();
                        break;
                    case 4:
                        provider.incrementDayInsideCounter();
                        /* -- Let 'em vote -- */
                        provider.getFragmentSwitcher().vote();
                        /* -------------------- */
                        break;
                    case 5:
                        write(R.string.speeches); // Those accused have their own speech
                        provider.incrementDayInsideCounter();
                        break;
                    case 6:
                        /* -- Let 'em choose -- */
                        provider.getFragmentSwitcher().lynch();
                        /* -------------------- */
                        provider.resetDayInsideCounter();
                        provider.incrementDayCounter();
                        provider.setNight(true);
                        break;
                }
                break;
        }
    }

    /* ---------- Characters call ---------- */
    private void masonsShow() {
        write(R.string.masonsCall);
    } // 1st night

    private void wolvesShow() {
        write(R.array.wolvesCall, 0);
    } // 1st night

    private void clairvoyant() {
        write(R.string.clairvoyantCall);
    }

    private void medium() {
        write(R.string.mediumCall);
    }

    private void guard() {
        write(R.string.guardCall);
    }

    private void wolves() {
        write(R.array.wolvesCall, 1);
    }

    /* ---------- Utilities ---------- */
    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void write(String msg) {
        provider.getMessages().add(msg);
        adapter.notifyDataSetChanged();
    }

    private void write(@StringRes int id) {
        write(getString(id));
    }

    private void write(@ArrayRes int id, int position) {
        String[] msg = getResources().getStringArray(id);
        write(msg[position]);
    }

    private void write(@ArrayRes int id, int position, String text) {
        String[] msg = getResources().getStringArray(id);
        write(String.format(msg[position], text));
    }

    private void write(@StringRes int id, String text) {
        write(getString(id, text));
    }

    private void greetings() {
        write(R.array.greetings, randInt(0, 3), provider.getVillage());
    }

    public void lookUpPlayers(){
        provider.getFragmentSwitcher().playersList();
    }

}
