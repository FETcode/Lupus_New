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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FET.leonardo.scurcola.Card;
import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.Player;
import com.FET.leonardo.scurcola.R;
import com.FET.leonardo.scurcola.master.Master;

import java.util.Arrays;
import java.util.List;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class NameSelectionFragment extends Fragment implements View.OnClickListener {
    private DataProvider provider;
    private Button next;
    private Button back;
    private Button finish;
    private EditText namesDisplay; // Where the user enters the names
    private TextView whoIsMaster; // Our question to the user
    private TextView playersLeft;
    private Master master;
    List<Player> players;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        System.out.println("[---------------------------------------------------------]");
        System.out.println("[-------------------- DEBUGGING LUPUS --------------------]");
        System.out.println("[----------------- NameSelectionFragment -----------------]");
        System.out.println("[---------------------------------------------------------]");

        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.name_selection, container, false);
        namesDisplay = (EditText) v.findViewById(R.id.names);

        namesDisplay.setText(provider.getLastTextInput());
        namesDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                provider.setLastTextInput(s.toString());
            }
        });

        next = (Button) v.findViewById(R.id.next);
        finish = (Button) v.findViewById(R.id.finish);
        back = (Button) v.findViewById(R.id.back);
        whoIsMaster = (TextView) v.findViewById(R.id.whoMaster);
        playersLeft = (TextView) v.findViewById(R.id.playersLeft);
        players = provider.getAlivePlayers();
        setPlayersLeft();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                addPlayer();
                break;
            case R.id.back:
                removePlayer();
                break;
            case R.id.finish:
                toNextActivity();
                break;
            case R.id.restartButton:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getFragmentManager(), "RestartDialog");
                break;
        }
    }

    // ### NEXT, BACK and FINISH buttons ### //

    // Next
    private void addPlayer() {
        String name = namesDisplay.getText().toString(); // Get the name entered by the user

        if (name.equals("") && players.size() == 0) {
            Toast.makeText(getActivity(), "Inserisci il nome del Master", Toast.LENGTH_SHORT).show();
        } else if (name.equals("")) {
            Toast.makeText(getActivity(), "Inserisci un nome", Toast.LENGTH_SHORT).show();
        } else {
            if (master == null) { // If there are no names chosen yet,
                next.setVisibility(View.VISIBLE); // we want to see the Next button,
                finish.setVisibility(View.GONE); // and hide the Finish one.
                back.setEnabled(true);
                whoIsMaster.setText(R.string.whoIsPlaying); // and edit our question properly.
                namesDisplay.setHint(R.string.player);

                master = new Master(getActivity());// Create the Master
                master.setName(name);
                namesDisplay.setText(""); // Clear the text area

                System.out.println("RUN");

                playersLeft.setText(String.valueOf(provider.getPlayerCount() - (master == null ? 0 : 1)));
            } else { // If the names are being chosen,
                back.setEnabled(true); // we want to be able to remove previous names,
                whoIsMaster.setText(R.string.whoIsPlaying); // and edit our question properly.
                namesDisplay.setHint(R.string.player);

                Player player = new Player();
                player.setName(name);
                players.add(player); // Add the name to the list
                namesDisplay.setText(""); // Clear the text area
                setPlayersLeft();
            }
            if (players.size() == provider.getPlayerCount() - 1) { // If all the names have been chosen,
                next.setVisibility(View.GONE); // we want to hide the Next button,
                finish.setVisibility(View.VISIBLE); // see the Finish one,
                namesDisplay.setVisibility(View.INVISIBLE); // and disable the EditText.
                playersLeft.setVisibility(View.INVISIBLE);
                provider.hideSoftKeyBoard(namesDisplay); // Dismiss the keyboard
            }
            provider.setLastTextInput("");
        }
    }

    private void setPlayersLeft() {

        System.out.println("[+] PLAYERS: " + provider.getPlayerCount());
        System.out.println("[+] PLAYERS LIST SIZE: " + players.size());
        System.out.println("[---------------------------------------------------------]");

        playersLeft.setText(String.valueOf(provider.getPlayerCount() - players.size() - (master == null ? 0 : 1)));
    }

    // Back
    private void removePlayer() {
        if (players.size() == 0) { // If we're back to 0,
            back.setEnabled(false); // disable the Back button,
            whoIsMaster.setText(R.string.whoMaster); // and change the question
            namesDisplay.setHint(R.string.master);
            playersLeft.setText(String.valueOf(provider.getPlayerCount())); // Display the default value
            master = null;
        } else {
            next.setVisibility(View.VISIBLE);
            finish.setVisibility(View.GONE);
            namesDisplay.setVisibility(View.VISIBLE);
            playersLeft.setVisibility(View.VISIBLE);
            players.remove(players.size() - 1); // Remove the last added name
            playersLeft.setText(String.valueOf(provider.getPlayerCount() - players.size() - (master == null ? 0 : 1))); // Display the players left
        }

    }

    // Finish
    private void toNextActivity() {
        if (provider.getPlayerCount() == 9) { // If there are 9 players only
            provider.getCharacters().clear();
            provider.getCharacters().addAll(Arrays.asList(Card.Villager, Card.Villager,
                    Card.Villager, Card.Villager, Card.Villager,
                    Card.Clairvoyant, Card.Wolf, Card.Wolf));

            provider.getFragmentSwitcher().randomAssignment();
            provider.hideSoftKeyBoard(namesDisplay);
        } else { // If there are more than 9 players
            provider.getFragmentSwitcher().characterSelection();
            provider.hideSoftKeyBoard(namesDisplay);

        }
    }
}
