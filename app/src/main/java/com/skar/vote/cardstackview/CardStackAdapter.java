package com.skar.vote.cardstackview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.skar.vote.R;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    public String choice;
    private LayoutInflater inflater;
    private List<Question> questions;
    private List<String> interestedTopics = new ArrayList<>();
    private Integer questionNumber = 0;
    private Question question;

    public CardStackAdapter(Context context, List<Question> questions) {
        this.inflater = LayoutInflater.from(context);
        this.questions = questions;
        Log.d("Qs rcv", questions.toString());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_choice_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        question = questions.get(questionNumber);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // commands to be executed when card is clicked
                // previously used for displaying question in toast
            }
        });

        holder.rgChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                holder.radioSelected = holder.itemView.findViewById(holder.rgChoice.getCheckedRadioButtonId());
                holder.choiceLayout.setVisibility(View.GONE);

                Animation animationLinearLayoutFadeOut = AnimationUtils.loadAnimation(group.getContext(), R.anim.fade_out);
                holder.choiceLayout.startAnimation(animationLinearLayoutFadeOut);
                holder.pieChart.setVisibility(View.VISIBLE);
                holder.pieChart.animateY(1400, Easing.EaseInOutQuad);

                ArrayList<PieEntry> yValues = new ArrayList<>();
                yValues.add(new PieEntry(question.choice_1_choosers, question.choice_1));
                yValues.add(new PieEntry(question.choice_2_choosers, question.choice_2));
                yValues.add(new PieEntry(question.choice_3_choosers, question.choice_3));

                PieDataSet dataSet = new PieDataSet(yValues, "Choices");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData pieData = new PieData(dataSet);
                dataSet.setValueTextSize(10f);
                dataSet.getValueTextColor(Color.YELLOW);

                holder.pieChart.setData(pieData);
                if (holder.radioSelected != null) {
                    if (holder.radioSelected.getText() != null)
                        choice = holder.radioSelected.getText().toString(); // get the text of selected radiobutton
                    if (holder.radioSelected.getTag(R.id.TAG_CHOICE_NUMBER) != null)
                        holder.selectedChoiceNumber = (String) holder.radioSelected.getTag(R.id.TAG_CHOICE_NUMBER); // get corresponding choice number of selected radiobutton
                }
            }
        });

    }

    public void incrementQuestionNumber() {
        questionNumber++;
    }



    @Override
    public int getItemCount() {
        return questions.size();
    }

    public Question getCurrentQuestion() {
        return questions.get(questionNumber);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RadioGroup rgChoice;
        RadioButton radioSelected;
        PieChart pieChart;
        RelativeLayout choiceLayout;
        String selectedChoiceNumber;

        ViewHolder(final View view) {
            super(view);

            rgChoice = view.findViewById(R.id.choice);
            pieChart = view.findViewById(R.id.pieChart);
            choiceLayout = view.findViewById(R.id.choice_layout);

            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(android.R.color.white);
            pieChart.setTransparentCircleRadius(0f);
            pieChart.getLegend().setEnabled(false);
        }
    }
}
