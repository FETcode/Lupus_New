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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.FET.leonardo.scurcola.CoursesAdapter;
import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.Player;
import com.FET.leonardo.scurcola.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Updated on 20.07.2016.
 *
 * @author F43nd1r
 */

public class ListPlayersLynchFragment extends Fragment {

    private DataProvider provider;
    private int votes = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        View v = inflater.inflate(R.layout.list_players_lynch, container, false);

        final ArrayList<Player> voters = new ArrayList<>(provider.getAlivePlayers());
        final ArrayList<Player> highest = new ArrayList<>(provider.getHighestVotedPlayers());
        Iterator<Player> i = voters.iterator();
        while (i.hasNext()) {
            Player player = i.next(); // must be called before you can call i.remove()
            player.setCount(0);
            if (highest.contains(player)) {
                i.remove();
            }
        }
        RecyclerView myList = (RecyclerView) v.findViewById(R.id.playersLynch);
        myList.setLayoutManager(new LinearLayoutManager(getActivity()));
        final CoursesAdapter adapter = new CoursesAdapter(highest);
        adapter.setOnEntryClickListener(new CoursesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                Player player = highest.get(position);
                player.incrementCount();
                adapter.notifyDataSetChanged();
                votes++;

                if (votes == voters.size()) {
                    Collections.sort(highest, new Comparator<Player>() {
                        @Override
                        public int compare(Player lhs, Player rhs) {
                            return Integer.compare(lhs.getCount(), rhs.getCount());
                        }
                    });
                    provider.lynch(highest.get(highest.size() - 1));
                    provider.getMessages().add(provider.getLastLynched().getName() + " è stato linciato dai popolani.");
                    for (Player v : highest){
                        v.setCount(0);
                    }
                    provider.getFragmentSwitcher().back();
                }
            }
        });
        myList.setAdapter(adapter);
        return v;
    }
}
