package com.example.postersmaker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
public class EmojiFragment extends BottomSheetDialogFragment {

    private EmojiListener mEmojiListener;

    public interface EmojiListener {
        void onEmojiClick(String emojiUnicode);
    }
    public EmojiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emoji, container, false);

        RecyclerView rvEmoji = view.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        rvEmoji.setLayoutManager(gridLayoutManager);
        rvEmoji.setAdapter(new EmojiAdapter());

        return view;
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        mEmojiListener = emojiListener;
    }

    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
        private final int[] EMOJI_UNICODE_VALUES = {
                0x1F600, 0x1F601, 0x1F602, 0x1F603, 0x1F604,
                0x1F605, 0x1F606, 0x1F607, 0x1F608, 0x1F609,
                0x1F60A, 0x1F60B, 0x1F60C, 0x1F60D, 0x1F60E,
                0x1F60F, 0x1F610, 0x1F611, 0x1F612, 0x1F613,
                0x1F614, 0x1F615, 0x1F616, 0x1F617, 0x1F618,
                0x1F619, 0x1F61A, 0x1F61B, 0x1F61C, 0x1F61D,
                0x1F61E, 0x1F61F, 0x1F620, 0x1F621, 0x1F622,
                0x1F623, 0x1F624, 0x1F625, 0x1F626, 0x1F627,
                0x1F628, 0x1F629, 0x1F62A, 0x1F62B, 0x1F62C,
                0x1F62D, 0x1F62E, 0x1F62F, 0x1F630, 0x1F631,
                0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F636,
                0x1F637, 0x1F638, 0x1F639, 0x1F63A, 0x1F63B,
                0x1F63C, 0x1F63D, 0x1F63E, 0x1F63F, 0x1F640,
                0x1F641, 0x1F642, 0x1F643, 0x1F644, 0x1F645,
                0x1F646, 0x1F647, 0x1F648, 0x1F649, 0x1F64A,
                0x1F64B, 0x1F64C, 0x1F64D, 0x1F64E, 0x1F64F,
                0x1F680, 0x1F681, 0x1F682, 0x1F683, 0x1F684,
                0x1F685, 0x1F686, 0x1F687, 0x1F688, 0x1F689,
                0x1F68A, 0x1F68B, 0x1F68C, 0x1F68D, 0x1F68E,
                0x1F68F, 0x1F690, 0x1F691, 0x1F692, 0x1F693,
                0x1F694, 0x1F695, 0x1F696, 0x1F697, 0x1F698,
                0x1F699, 0x1F69A, 0x1F69B, 0x1F69C, 0x1F69D,
                0x1F69E, 0x1F69F, 0x1F6A0, 0x1F6A1, 0x1F6A2,};

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emoji, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtEmoji.setText(getEmojiByUnicode(EMOJI_UNICODE_VALUES, position));
        }

        @Override
        public int getItemCount() {
            return EMOJI_UNICODE_VALUES.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(View itemView) {
                super(itemView);
                txtEmoji = itemView.findViewById(R.id.txtEmoji);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && mEmojiListener != null) {
                            mEmojiListener.onEmojiClick(getEmojiByUnicode(EMOJI_UNICODE_VALUES, position));
                        }
                        dismiss();
                    }
                });
            }
        }

        private String getEmojiByUnicode(int[] unicodeValues, int position) {
            return new String(Character.toChars(unicodeValues[position]));
        }
    }
}