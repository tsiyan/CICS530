package com.carethy.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carethy.R;

/**
 * Abstract Fragment that appears in the "content_frame".
 * ContentFragmentFactory will return concrete subclasses. 
 */
public abstract class AbstractContentFragment extends Fragment {
    public static final String ARG_MENU_ITEM_INDEX = "menu_item_index";
    private String title=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        int i = getArguments().getInt(ARG_MENU_ITEM_INDEX);
        
        title = getResources().getStringArray(R.array.drawer_menu_item_titles)[i];
        getActivity().setTitle(title);   
        
    
        return rootView;
    }
    
}