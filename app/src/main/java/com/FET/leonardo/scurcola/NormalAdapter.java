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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.CoursesViewHolder> {

    public class CoursesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;

        CoursesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.textName);
        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    private ArrayList<Player> mArrayCourses;

    public NormalAdapter(ArrayList<Player> arrayCourses) {
        mArrayCourses = arrayCourses;
    }

    @Override
    public int getItemCount() {
        return mArrayCourses.size();
    }

    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.two_line_list_item_horizontal, parent, false);
        return new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CoursesViewHolder holder, int position) {
        Player player = mArrayCourses.get(position);
        holder.name.setText(player.getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }



}