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
import android.widget.SeekBar;
import android.widget.TextView;

import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.R;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class PlayerSelectionFragment extends Fragment implements View.OnClickListener {

    private DataProvider provider;
    private int seekBarProgress;
    private TextView playersDisplay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.player_selection, container, false);
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        playersDisplay = (TextView) v.findViewById(R.id.playersAmount);
        seekBar.setProgress(provider.getPlayerCount() - 9);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                seekBarProgress = progressValue + 9; //Lowest amount possible is 9
                playersDisplay.setText(String.format("%s", seekBarProgress)); //Display the amount
                provider.setPlayerCount(seekBarProgress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextPlayerSelection:
                provider.getFragmentSwitcher().nameSelection();
                break;
            case R.id.restartButton:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getFragmentManager(), "RestartDialog");
                break;
        }

    }
}
