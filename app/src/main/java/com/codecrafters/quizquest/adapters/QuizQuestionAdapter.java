package com.codecrafters.quizquest.adapters;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.activities.admin.QuizSetClickListener;
import com.codecrafters.quizquest.models.QuizQuestion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.ViewHolder> {

//    private static final Context context;
    private static Context context;
    private final List<QuizQuestion> quizQuestions;


    public QuizQuestionAdapter(Context context, List<QuizQuestion> quizQuestions) {
        this.context = context;
        this.quizQuestions = quizQuestions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_question, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizQuestion question = quizQuestions.get(position);

        // Set the question text
        if (question.getQuestionText() != null) {
            holder.questionTextView.setText(question.getQuestionText());
        } else {
            holder.questionTextView.setText("Question not available");
        }

        // Format and set the answer options
        holder.answersTextView.setText(formatOptions(question));
        // Set correct answer
        holder.correctAnswerTextView.setText("Correct Answer: " + (question.getCorrectAnswer() != null ? question.getCorrectAnswer() : "Not set"));
    }

    private String formatOptions(QuizQuestion question) {
        StringBuilder formattedOptions = new StringBuilder();
        formattedOptions.append("A) ").append(question.getQuizQuesAnsA()).append("\n");
        formattedOptions.append("B) ").append(question.getQuizQuesAnsB()).append("\n");
        formattedOptions.append("C) ").append(question.getQuizQuesAnsC()).append("\n");
        formattedOptions.append("D) ").append(question.getQuizQuesAnsD());
        return formattedOptions.toString();
    }


    @Override
    public int getItemCount() {
        return quizQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answersTextView;
        TextView correctAnswerTextView;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answersTextView = itemView.findViewById(R.id.answersTextView);
            correctAnswerTextView = itemView.findViewById(R.id.correctAnswerTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Implement edit functionality
                    editQuestion(quizQuestions.get(position));
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Implement delete functionality
                    confirmAndDeleteQuestion(quizQuestions.get(position));
                }
            });

        }


        private void confirmAndDeleteQuestion(QuizQuestion question) {
            // Show a confirmation dialog
            // If confirmed, delete the question from Firebase and notify the adapter
            new AlertDialog.Builder(context)
                    .setTitle("Delete Question")
                    .setMessage("Are you sure you want to delete this question?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteQuestion(question))
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }

        private void deleteQuestion(QuizQuestion question) {
            DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("QuizQuestions").child(question.getQuestionId());
            questionRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the question from the list and notify the adapter
                        // ...
                        Toast.makeText(context, "Question deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete question: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        private void editQuestion(QuizQuestion question) {
            // Create a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.edit_question_dialog, null);
            builder.setView(dialogView);

            EditText editQuestionText = dialogView.findViewById(R.id.editQuestionText);
            EditText editOptionA = dialogView.findViewById(R.id.editOptionA);
            EditText editOptionB = dialogView.findViewById(R.id.editOptionB);
            EditText editOptionC = dialogView.findViewById(R.id.editOptionC);
            EditText editOptionD = dialogView.findViewById(R.id.editOptionD);
            EditText editCorrectAnswer = dialogView.findViewById(R.id.editCorrectAnswer);

            // Pre-populate the dialog with existing data
            editQuestionText.setText(question.getQuestionText());
            editOptionA.setText(question.getQuizQuesAnsA());
            editOptionB.setText(question.getQuizQuesAnsB());
            editOptionC.setText(question.getQuizQuesAnsC());
            editOptionD.setText(question.getQuizQuesAnsD());
            editCorrectAnswer.setText(question.getCorrectAnswer());

            builder.setPositiveButton("Save", (dialog, which) -> {
                // Get the updated text from the EditTexts
                String updatedQuestionText = editQuestionText.getText().toString();
                String updatedOptionA = editOptionA.getText().toString();
                String updatedOptionB = editOptionB.getText().toString();
                String updatedOptionC = editOptionC.getText().toString();
                String updatedOptionD = editOptionD.getText().toString();
                String updatedCorrectAnswer = editCorrectAnswer.getText().toString();

                // Update the question object
                question.setQuestionText(updatedQuestionText);
                question.setQuizQuesAnsA(updatedOptionA);
                question.setQuizQuesAnsB(updatedOptionB);
                question.setQuizQuesAnsC(updatedOptionC);
                question.setQuizQuesAnsD(updatedOptionD);
                question.setCorrectAnswer(updatedCorrectAnswer);

                // Save the updated question back to Firebase
                updateQuestionInFirebase(question);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void updateQuestionInFirebase(QuizQuestion question) {
            if (question.getQuestionId() == null) {
                Toast.makeText(context, "Error: Question ID is null", Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference().child("QuizQuestions").child(question.getQuestionId());
            questionRef.setValue(question)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Question updated successfully", Toast.LENGTH_SHORT).show();
                        // Notify the adapter that data has changed
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to update question: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }


    }
}
