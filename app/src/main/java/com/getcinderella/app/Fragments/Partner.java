package com.getcinderella.app.Fragments;

import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.util.ArrayList;

import com.getcinderella.app.R;
import com.getcinderella.app.Utils.DataHelper;
import com.getcinderella.app.Utils.SliderAdapter;

public class Partner extends Fragment {
    //vars
    private DataHelper dataHelper;
    private int currentPosition;
    private @ColorInt int color;
    private CardSliderLayoutManager layoutManger;
//    widgets
    private RelativeLayout empty_partner;
    private LinearLayout not_empty_partner;
    private RecyclerView recyclerView;
    private TextSwitcher nameSwitcher;
    private TextSwitcher charismaSwitcher;
    private TextSwitcher quoteSwitcher;
    private ArrayList<String> pics = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> quotes = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Long> charismas = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_partners, container, false);
        dataHelper=new DataHelper(getContext());
        TypedValue outValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, outValue, true);
        color = outValue.data;
        pics = dataHelper.getRemoteUserDps();
        ids=dataHelper.getRemoteUserIds();
        names = dataHelper.getRemoteUserNames();
        charismas = dataHelper.getRemoteUserCharismas();
        quotes=dataHelper.getRemoteUserQuotes();
        empty_partner =view.findViewById(R.id.empty_partners);
        not_empty_partner =view.findViewById(R.id.not_empty_partners);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        quoteSwitcher = view.findViewById(R.id.quote_switcher);
        nameSwitcher = view.findViewById(R.id.name_switcher);
        charismaSwitcher = view.findViewById(R.id.skill_switcher);
        final SliderAdapter sliderAdapter = new SliderAdapter(pics, pics.size(), new OnCardClickListener());
        Button call_partner= view.findViewById(R.id.call_partner);
        call_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();
                final int activeCardPosition = lm.getActiveCardPosition();
                new PartnerDialog.Builder(getContext(),pics.get(activeCardPosition),ids.get(activeCardPosition)).build().show();
            }
        });
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });
        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();
        new CardSnapHelper().attachToRecyclerView(recyclerView);
        if (dataHelper.getPartners()==0){
            return view;
        }else{
            empty_partner.setVisibility(View.GONE);
            not_empty_partner.setVisibility(View.VISIBLE);
            init();
        }
        return view;
    }

    private void init(){
        quoteSwitcher.setFactory(new TextViewFactory(R.style.QuoteTextView, false));
        String quote = quotes.get(0);
        String finalQuote = quote + " <large><font color = "+color+">\"</font></large>";
        quoteSwitcher.setCurrentText(Html.fromHtml(finalQuote));

        nameSwitcher.setFactory(new TextViewFactory(R.style.NameTextView, true));
        nameSwitcher.setCurrentText(names.get(0));
        charismaSwitcher.setFactory(new TextViewFactory(R.style.CharismalTextView, true));
        charismaSwitcher.setCurrentText(""+ charismas.get(0));
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int[] animH = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int[] animV = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};
        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }
        nameSwitcher.setInAnimation(getContext(), animH[0]);
        nameSwitcher.setOutAnimation(getContext(), animH[1]);
        nameSwitcher.setText(names.get(pos));

        charismaSwitcher.setInAnimation(getContext(), animV[0]);
        charismaSwitcher.setOutAnimation(getContext(), animV[1]);
        charismaSwitcher.setText(""+ charismas.get(pos));

        String quote=quotes.get(pos);
        String finalQuote =quote+" <large><font color = "+color +">\"</font></large>";
        quoteSwitcher.setInAnimation(getContext(), R.anim.fade_in);
        quoteSwitcher.setOutAnimation(getContext(), R.anim.fade_out);
        quoteSwitcher.setText(Html.fromHtml(finalQuote));
        currentPosition = pos;
    }


    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                new PartnerDialog.Builder(getContext(),pics.get(activeCardPosition),ids.get(activeCardPosition)).build().show();

            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

    private class TextViewFactory implements  ViewSwitcher.ViewFactory {

        @StyleRes
        final int styleId;
        final boolean center;

        TextViewFactory(@StyleRes int styleId, boolean center) {
            this.styleId = styleId;
            this.center = center;
        }

        @Override
        public View makeView() {
            final TextView textView = new TextView(getContext());

            if (center) {
                textView.setGravity(Gravity.CENTER);
            }
            TextViewCompat.setTextAppearance(textView,styleId);
            return textView;
        }

    }
}
