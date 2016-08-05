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

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.FET.leonardo.scurcola.Card;
import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.R;

import java.util.List;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class CharacterSelectionFragment extends Fragment implements View.OnClickListener {

    private DataProvider provider;
    private Button demoniac;
    private Button guard;
    private Button masons;
    private Button medium;
    private Button villager;
    private Button werehamster;
    private Button add;
    private Button remove;
    private Button finish;
    private TextView countText;
    private boolean isAdding;
    private List<Button> buttonList;

    private int demoniacCounter;
    private int guardCounter;
    private int masonsCounter;
    private int mediumCounter;
    private int villagerCounter;
    private int werehamsterCounter;
    private int clairvoyantCounter;
    private int wolfCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.character_selection, container, false);
        initializeVariables();
        demoniac = (Button) v.findViewById(R.id.demoniac);
        guard = (Button) v.findViewById(R.id.guard);
        masons = (Button) v.findViewById(R.id.masons);
        medium = (Button) v.findViewById(R.id.medium);
        villager = (Button) v.findViewById(R.id.villager);
        werehamster = (Button) v.findViewById(R.id.werehamster);
        add = (Button) v.findViewById(R.id.add);
        remove = (Button) v.findViewById(R.id.remove);
        finish = (Button) v.findViewById(R.id.finish);
        countText = (TextView) v.findViewById(R.id.count);
        updateViews();
        return v;
    }

    private void initializeVariables() {
        // The default characters
        demoniacCounter = 0;
        guardCounter = 0;
        masonsCounter = 0;
        mediumCounter = 0;
        villagerCounter = 5;
        werehamsterCounter = 0;
        clairvoyantCounter = 1;
        wolfCounter = 2;
        if (provider.getPlayerCount() >= 16) {
            wolfCounter++;
        }

        isAdding = true;

        buttonList.add(demoniac);
        buttonList.add(guard);
        buttonList.add(masons);
        buttonList.add(medium);
        buttonList.add(villager);
        buttonList.add(werehamster);
        buttonList.add(add);

    }

    private void updateViews() {
        demoniac.setEnabled(demoniacCounter == 0 == isAdding);
        guard.setEnabled(guardCounter == 0 == isAdding);
        masons.setEnabled(masonsCounter == 0 == isAdding);
        medium.setEnabled(mediumCounter == 0 == isAdding);
        werehamster.setEnabled(werehamsterCounter == 0 == isAdding);
        villager.setEnabled(demoniacCounter + guardCounter + masonsCounter + mediumCounter
                + villagerCounter + werehamsterCounter < provider.getPlayerCount() && isAdding
                || villagerCounter > 5);
        add.setEnabled(!isAdding);
        remove.setEnabled(isAdding);
        finish.setVisibility(demoniacCounter + guardCounter + masonsCounter + mediumCounter
                + villagerCounter + werehamsterCounter == provider.getPlayerCount()
                ? View.VISIBLE : View.GONE);
        countText.setText(String.valueOf(provider.getPlayerCount() - demoniacCounter - guardCounter
                - masonsCounter - mediumCounter - villagerCounter - werehamsterCounter));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                isAdding = true;
                break;
            case R.id.remove:
                isAdding = false;
                break;
            case R.id.demoniac:
                demoniacCounter += isAdding ? 1 : -1;
                break;
            case R.id.guard:
                guardCounter += isAdding ? 1 : -1;
                break;
            case R.id.masons:
                masonsCounter += isAdding ? 2 : -2;
                break;
            case R.id.medium:
                mediumCounter += isAdding ? 1 : -1;
                break;
            case R.id.werehamster:
                werehamsterCounter += isAdding ? 1 : -1;
                break;
            case R.id.villager:
                villagerCounter += isAdding ? 1 : -1;
                break;
            case R.id.finish:
                List<Card> characters = provider.getCharacters();
                characters.clear();
                for (int i = 0; i < demoniacCounter; i++) characters.add(Card.Demoniac);
                for (int i = 0; i < guardCounter; i++) characters.add(Card.Guard);
                for (int i = 0; i < masonsCounter; i++) characters.add(Card.Masons);
                for (int i = 0; i < mediumCounter; i++) characters.add(Card.Medium);
                for (int i = 0; i < villagerCounter; i++) characters.add(Card.Villager);
                for (int i = 0; i < werehamsterCounter; i++) characters.add(Card.Werehamster);
                for (int i = 0; i < clairvoyantCounter; i++) characters.add(Card.Clairvoyant);
                for (int i = 0; i < wolfCounter; i++) characters.add(Card.Wolf);
                provider.getFragmentSwitcher().randomAssignment();
                break;
            default:
                if(countText.getText().toString().equals("0")){ // If amount of characters picked == 0, disable every button and add the finish one
                    for(Button b : buttonList) {
                        b.setEnabled(false);
                    }
                    finish.setVisibility(View.VISIBLE);
                }
                break;
        }
        updateViews();
    }
}
