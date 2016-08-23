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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.R;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */
public class VillageFragment extends Fragment implements View.OnClickListener {

    private DataProvider provider;
    private EditText villageText;
    private ImageButton restartButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.village, container, false);

        restartButton = (ImageButton) getActivity().findViewById(R.id.restartButton);
        villageText = (EditText) v.findViewById(R.id.village);

        villageText.setText(provider.getLastTextInput());
        villageText.addTextChangedListener(new TextWatcher() {
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
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restartButton:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getFragmentManager(), "RestartDialog");
                break;
            case R.id.next:
                String village = villageText.getText().toString();
                if (village.equals("")) {
                    Toast.makeText(getActivity(), "Inserisci il nome del villaggio!", Toast.LENGTH_SHORT).show();
                } else {
                    provider.setVillage(village);
                    provider.setLastTextInput("");
                    provider.getFragmentSwitcher().playerSelection();
                    provider.hideSoftKeyBoard(restartButton);
                }
                break;
        }

    }
}
