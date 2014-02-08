package com.carethy.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carethy.R;

/**
 * Fragment that appears in the "content_frame"
 */
public class ContentFragment extends Fragment {
    public static final String ARG_MENU_ITEM_INDEX = "menu_item_index";
	
    public ContentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        int i = getArguments().getInt(ARG_MENU_ITEM_INDEX);
        String title = getResources().getStringArray(R.array.drawer_menu_array)[i];
        getActivity().setTitle(title);   

    
        return rootView;
    }
}