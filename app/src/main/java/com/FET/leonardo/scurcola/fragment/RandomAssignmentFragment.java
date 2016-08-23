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

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.FET.leonardo.scurcola.Card;
import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.Player;
import com.FET.leonardo.scurcola.R;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class RandomAssignmentFragment extends Fragment implements View.OnClickListener {

    private DataProvider provider;

    private int count;

    private TextView card;
    private TextView name;
    private Button pick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.random_assignment, container, false);
        card = (TextView) v.findViewById(R.id.card);
        name = (TextView) v.findViewById(R.id.name);
        pick = (Button) v.findViewById(R.id.pick);
        assignRandomIfNeeded();
        return v;
    }

    private void assignRandomIfNeeded() {
        List<Player> players = provider.getAlivePlayers();
        if (!players.isEmpty() && players.get(0).getCard() == null) {
            List<Card> cards = provider.getCharacters();
            Random random = new Random(System.nanoTime());
            Collections.shuffle(cards, random);
            Collections.shuffle(players, random);
            for (int i = 0; i < players.size(); i++) {
                System.out.println("SIZE: " + players.size());
                System.out.println(players.get(i).getName() + " AND " + cards.get(i));
                players.get(i).setCard(cards.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restartButton:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getFragmentManager(), "RestartDialog");
                break;
            case R.id.pick:
                List<Player> players = provider.getAlivePlayers();
                if (count < players.size()) {
                    card.setText(players.get(count).getCard().name());
                    name.setText(players.get(count).getName());
                    count++;
                } else if (pick.getText().equals(getString(R.string.finish))) {
                    provider.getFragmentSwitcher().game();
                } else if (count == players.size()) {
                    pick.setText(R.string.finish);
                }
                break;
        }
    }
}
