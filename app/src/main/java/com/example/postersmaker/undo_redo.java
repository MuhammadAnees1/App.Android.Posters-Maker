package com.example.postersmaker;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class undo_redo {
static Context context1;

    static void undo(Context context) {
        context1 = context;
        if (Track.list.size() > 0) {
            if(Track.list.get(Track.list.size() - 1).getPosition()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combineditem.getImageLayout().getId(),combineditem.getImageLayout().getFrameLayout().getX(),combineditem.getImageLayout().getFrameLayout().getY(),true));
                            combineditem.getImageLayout().setX(Track.list.get(Track.list.size() - 1).getPositionX());
                            combineditem.getImageLayout().setY(Track.list.get(Track.list.size() - 1).getPositionY());
                            Track.list.remove(Track.list.size() - 1);

                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()) {
                            Track.list2.add(new Track(combineditem.getTextlayout2().getId(),combineditem.getTextlayout2().getFrameLayout().getX(), combineditem.getTextlayout2().getFrameLayout().getY(), true));
                            combineditem.getTextlayout2().setX(Track.list.get(Track.list.size() - 1).getPositionX());
                            combineditem.getTextlayout2().setY(Track.list.get(Track.list.size() - 1).getPositionY());
                            Track.list.remove(Track.list.size() - 1);

                            break;}

                    }

                }
            }
            else if(Track.list.get(Track.list.size() - 1).isRotate()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combineditem.getImageLayout().getId(), combineditem.getImageLayout().getFrameLayout().getRotation(), true));
                            combineditem.getImageLayout().getFrameLayout().setRotation(Track.list.get(Track.list.size() - 1).getRotation());
                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()) {
                            Track.list2.add(new Track(combineditem.getTextlayout2().getId(), combineditem.getTextlayout2().getFrameLayout().getRotation(), true));

                            combineditem.getTextlayout2().getFrameLayout().setRotation(Track.list.get(Track.list.size() - 1).getRotation());
                            break;}
                    }

                }
                Track.list.remove(Track.list.size() - 1);

            }
            else if (Track.list.get(Track.list.size()-1).isResize()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combineditem.getImageLayout().getId(), combineditem.getImageLayout().getFrameLayout().getWidth(), combineditem.getImageLayout().getFrameLayout().getHeight(),combineditem.getImageLayout().getImageView().getWidth(),combineditem.getImageLayout().getImageView().getHeight(),0, true));

                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) combineditem.getImageLayout().getFrameLayout().getLayoutParams();
                            layoutParams.width = Track.list.get(Track.list.size() - 1).getWidth();
                            layoutParams.height = Track.list.get(Track.list.size() - 1).getHeight();
                            combineditem.getImageLayout().getFrameLayout().setLayoutParams(layoutParams);
                            ViewGroup.LayoutParams layoutParams1 = combineditem.getImageLayout().getImageView().getLayoutParams();
                            layoutParams1.width =  Track.list.get(Track.list.size() - 1).getImgwidth();
                            layoutParams1.height =  Track.list.get(Track.list.size() - 1).getImgheight();
                            combineditem.getImageLayout().getImageView().setLayoutParams(layoutParams1);
                            Track.list.remove(Track.list.size() - 1);

                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()) {
                            Track.list2.add(new Track(combineditem.getTextlayout2().getId(), combineditem.getTextlayout2().getBorderLayout().getWidth(), combineditem.getTextlayout2().getBorderLayout().getHeight(),combineditem.getTextlayout2().getTextView().getWidth(),0,combineditem.getTextlayout2().getTextView().getTextSize(), true));

                            combineditem.getTextlayout2().getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,Track.list.get(Track.list.size() - 1).getTextSize());
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) combineditem.getTextlayout2().getBorderLayout().getLayoutParams();
                            layoutParams.width = Track.list.get(Track.list.size() - 1).getWidth();
                            layoutParams.height = Track.list.get(Track.list.size() - 1).getHeight();
                            combineditem.getTextlayout2().getBorderLayout().setLayoutParams(layoutParams);
                            combineditem.getTextlayout2().getTextView().setWidth(Track.list.get(Track.list.size() - 1).getImgwidth());
                            Track.list.remove(Track.list.size() - 1);

                            break;}
                    }
                }
            }
            else if (Track.list.get(Track.list.size()-1).isIstextcolor()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getTextlayout2() != null){
                        if(combineditem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combineditem.getTextlayout2().getId(), true, combineditem.getTextlayout2().getTextView().getCurrentTextColor()));
                            combineditem.getTextlayout2().getTextView().setTextColor(Track.list.get(Track.list.size() - 1).getTextColor());
                            break;}
                    }
                }
                Track.list.remove(Track.list.size() - 1);

            }
            else if(Track.list.get(Track.list.size()-1).isShadowOn()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getTextlayout2() != null){
                        if(combineditem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combineditem.getTextlayout2().getId(),
                                    combineditem.getTextlayout2().getTextView().getShadowRadius(),
                                    combineditem.getTextlayout2().getTextView().getShadowDx(),
                                    combineditem.getTextlayout2().getTextView().getShadowDy(),
                                    true
                            ));
                            combineditem.getTextlayout2().getTextView().setShadowLayer(Track.list.get(Track.list.size() - 1).getShadow(), Track.list.get(Track.list.size() - 1).getShadowDx(), Track.list.get(Track.list.size() - 1).getShadowDy(), Color.BLACK);
                            Track.list.remove(Track.list.size() - 1);
                            if(HomeFragment1.seekBar.getVisibility() == View.VISIBLE){
                                HomeFragment1.seekBar.setProgress((int) (combineditem.getTextlayout2().getTextView().getShadowRadius()*5));

                            }
                            break;
                        }

                    }

                }


            }
            else if(Track.list.get(Track.list.size()-1).isFontR()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combinedItem.getTextlayout2().getId(),true,combinedItem.getTextlayout2().getTextView().getTypeface()));
                            combinedItem.getTextlayout2().getTextView().setTypeface(Track.list.get(Track.list.size() - 1).getTypeface(), Typeface.NORMAL);
                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }
                    }
                }

            }
            else if(Track.list.get(Track.list.size()-1).isIssize()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(true,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getTextSize()));

                            combinedItem.getTextlayout2().getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,Track.list.get(Track.list.size() - 1).getTextSize());
                            HomeFragment1.sizeSeekBar.setProgress((int) (Track.list.get(Track.list.size() - 1).getTextSize()-10f));
                            Track.list.remove(Track.list.size() - 1);
                            break;

                        }
                    }

                }
            }
            else if(Track.list.get(Track.list.size()-1).isSpace()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combinedItem.getTextlayout2().getTextView().getLetterSpacing(),true,combinedItem.getTextlayout2().getId()));

                            combinedItem.getTextlayout2().getTextView().setLetterSpacing(Track.list.get(Track.list.size() - 1).getSpacing());
                            HomeFragment1.spaceSeekBar.setProgress((int) combinedItem.getTextlayout2().getTextView().getLetterSpacing()*pxTodp(33));

                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list.get(Track.list.size()-1).isEdittext()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){

                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            int  index1 = MainActivity.textLayoutList2.indexOf(combinedItem.getTextlayout2());
                            Track.list2.add(new Track(true,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getText().toString(),index1));

                            combinedItem.getTextlayout2().getTextView().setText(Track.list.get(Track.list.size() - 1).getText());
                            if(Track.list.get(Track.list.size() - 1).getTextView() != null){

                                int index =-1;

                                if (index != -1) {
                                    TextHandlerClass.textList.set(index, Track.list.get(Track.list.size() - 1).getText());
                                }
                            }
                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }}}}

            else if(Track.list.get(Track.list.size()-1).isImgOpacity()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getImageLayout()!=null){
                        if(combinedItem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combinedItem.getImageLayout().getId(),true,combinedItem.getImageLayout().getImageView().getAlpha()));

                            combinedItem.getImageLayout().getImageView().setAlpha(Track.list.get(Track.list.size() - 1).getImgopacity1());
                            HomeFragment1.opacitySeekBar.setProgress((int) (Track.list.get(Track.list.size() - 1).getImgopacity1()*100));
                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list.get(Track.list.size()-1).isFlip()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getImageLayout()!=null){
                        if(combinedItem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(combinedItem.getImageLayout().getImageView(),combinedItem.getImageLayout().getId(),combinedItem.getImageLayout().getImageView().getScaleX(),true));

                            combinedItem.getImageLayout().getImageView().setScaleX(Track.list.get(Track.list.size() - 1).getFlipx());
                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }
                    }
                    else if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            Track.list2.add(new Track(null,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getScaleX(),true));
                            combinedItem.getTextlayout2().getTextView().setScaleX(Track.list.get(Track.list.size() - 1).getFlipx());
                            Track.list.remove(Track.list.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list.get(Track.list.size()-1).isUnderline()){
                for(CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            if(Track.list.get(Track.list.size()-1).isIsunderline()){
                                Track.list2.add(new Track(combinedItem.getTextlayout2().getId(),false,true));
                                combinedItem.getTextlayout2().getTextView().setPaintFlags(combinedItem.getTextlayout2().getTextView().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            }
                            else {
                                Track.list2.add(new Track(combinedItem.getTextlayout2().getId(),true,true));

                                combinedItem.getTextlayout2().getTextView().setPaintFlags(combinedItem.getTextlayout2().getTextView().getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                            }
                        }
                        Track.list.remove(Track.list.size() - 1);
                        break;
                    }
                }
            }
            else if(Track.list.get(Track.list.size()-1).isAllign()){
                for(CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list.get(Track.list.size() - 1).getTid()){
                            int a =  combinedItem.getTextlayout2().getTextView().getTextAlignment();
                            int b = 0;
                            if (a == View.TEXT_ALIGNMENT_TEXT_START){ b = 1;}
                            else if (a == View.TEXT_ALIGNMENT_CENTER){ b = 2;}
                            else if (a == View.TEXT_ALIGNMENT_TEXT_END){ b = 3;}
                            Track.list2.add(new Track(combinedItem.getTextlayout2().getId(),b ,true));

                            int c = Track.list.get(Track.list.size() - 1).getTextAlignment();
                            if(c==1){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);}
                            else if(c==2){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);}
                            else if(c==3){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);}
                           String currentText = combinedItem.getTextlayout2().getTextView().getText().toString();
                            combinedItem.getTextlayout2().getTextView().setText(currentText);
                        }
                        Track.list.remove(Track.list.size() - 1);
                        break;
                    }
                }
            }
            else if(Track.list.get(Track.list.size()-1).item == "hue"){
                for(CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getImageLayout()!=null){
                        if(combinedItem.getImageLayout().getId() == Track.list.get(Track.list.size() - 1).getTid()){
//                            Track.list2.add(new Track(combinedItem.getImageLayout().getId(),combinedItem.getImageLayout().getImageView().getHue(),true));
                            HueAdapter.applyHueToImageView(combinedItem.getImageLayout().getImageView().getDrawable(), Track.list.get(Track.list.size() - 1).getHueValue());
                            break;
                        }
                    }
                }
            }
        }
        else {
            Toast.makeText(context, "size 0", Toast.LENGTH_SHORT).show();}
    }
    static void redo(Context context) {
        context1 = context;
        if (Track.list2.size() > 0) {
            if(Track.list2.get(Track.list2.size() - 1).getPosition()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combineditem.getImageLayout().getId(),combineditem.getImageLayout().getFrameLayout().getX(),combineditem.getImageLayout().getFrameLayout().getY(),true));
                            combineditem.getImageLayout().setX(Track.list2.get(Track.list2.size() - 1).getPositionX());
                            combineditem.getImageLayout().setY(Track.list2.get(Track.list2.size() - 1).getPositionY());
                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()) {
                            Track.list.add(new Track(combineditem.getTextlayout2().getId(),combineditem.getTextlayout2().getFrameLayout().getX(), combineditem.getTextlayout2().getFrameLayout().getY(), true));
                            combineditem.getTextlayout2().setX(Track.list2.get(Track.list2.size() - 1).getPositionX());
                            combineditem.getTextlayout2().setY(Track.list2.get(Track.list2.size() - 1).getPositionY());
                            break;}

                    }

                }
                Track.list2.remove(Track.list2.size() - 1);
            }
            else if(Track.list2.get(Track.list2.size() - 1).isRotate()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combineditem.getImageLayout().getId(), combineditem.getImageLayout().getFrameLayout().getRotation(), true));
                            combineditem.getImageLayout().getFrameLayout().setRotation(Track.list2.get(Track.list2.size() - 1).getRotation());
                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()) {
                            Track.list.add(new Track(combineditem.getTextlayout2().getId(), combineditem.getTextlayout2().getFrameLayout().getRotation(), true));
                            combineditem.getTextlayout2().getFrameLayout().setRotation(Track.list2.get(Track.list2.size() - 1).getRotation());
                            break;}
                    }

                }
                Track.list2.remove(Track.list2.size() - 1);

            }
            else if (Track.list2.get(Track.list2.size()-1).isResize()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getImageLayout() != null){
                        if(combineditem.getImageLayout().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combineditem.getImageLayout().getId(), combineditem.getImageLayout().getFrameLayout().getWidth(), combineditem.getImageLayout().getFrameLayout().getHeight(),combineditem.getImageLayout().getImageView().getWidth(),combineditem.getImageLayout().getImageView().getHeight(),0, true));

                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) combineditem.getImageLayout().getFrameLayout().getLayoutParams();
                            layoutParams.width = Track.list2.get(Track.list2.size() - 1).getWidth();
                            layoutParams.height = Track.list2.get(Track.list2.size() - 1).getHeight();
                            combineditem.getImageLayout().getFrameLayout().setLayoutParams(layoutParams);
                            ViewGroup.LayoutParams layoutParams1 = combineditem.getImageLayout().getImageView().getLayoutParams();
                            layoutParams1.width =  Track.list2.get(Track.list2.size() - 1).getImgwidth();
                            layoutParams1.height =  Track.list2.get(Track.list2.size() - 1).getImgheight();
                            combineditem.getImageLayout().getImageView().setLayoutParams(layoutParams1);
                            Track.list2.remove(Track.list2.size() - 1);

                            break;}
                    }
                    else if (combineditem.getTextlayout2() != null) {
                        if(combineditem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()) {
                            Track.list.add(new Track(combineditem.getTextlayout2().getId(), combineditem.getTextlayout2().getBorderLayout().getWidth(), combineditem.getTextlayout2().getBorderLayout().getHeight(),combineditem.getTextlayout2().getTextView().getWidth(),0,combineditem.getTextlayout2().getTextView().getTextSize(), true));

                            combineditem.getTextlayout2().getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,Track.list2.get(Track.list2.size() - 1).getTextSize());
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) combineditem.getTextlayout2().getBorderLayout().getLayoutParams();
                            layoutParams.width = Track.list2.get(Track.list2.size() - 1).getWidth();
                            layoutParams.height = Track.list2.get(Track.list2.size() - 1).getHeight();
                            combineditem.getTextlayout2().getBorderLayout().setLayoutParams(layoutParams);
                            combineditem.getTextlayout2().getTextView().setWidth(Track.list2.get(Track.list2.size() - 1).getImgwidth());
                            Track.list2.remove(Track.list2.size() - 1);

                            break;}
                    }
                }
            }
            else if (Track.list2.get(Track.list2.size()-1).isIstextcolor()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getTextlayout2() != null){
                        if(combineditem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combineditem.getTextlayout2().getId(), true, combineditem.getTextlayout2().getTextView().getCurrentTextColor()));
                            combineditem.getTextlayout2().getTextView().setTextColor(Track.list2.get(Track.list2.size() - 1).getTextColor());
                            break;}
                    }
                }
                Track.list2.remove(Track.list2.size() - 1);

            }
            else if(Track.list2.get(Track.list2.size()-1).isShadowOn()){
                for (CombinedItem combineditem : MainActivity.combinedItemList) {
                    if(combineditem.getTextlayout2() != null){
                        if(combineditem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combineditem.getTextlayout2().getId(),
                                    combineditem.getTextlayout2().getTextView().getShadowRadius(),
                                    combineditem.getTextlayout2().getTextView().getShadowDx(),
                                    combineditem.getTextlayout2().getTextView().getShadowDy(),
                                    true
                            ));
                            combineditem.getTextlayout2().getTextView().setShadowLayer(Track.list2.get(Track.list2.size() - 1).getShadow(), Track.list2.get(Track.list2.size() - 1).getShadowDx(), Track.list2.get(Track.list2.size() - 1).getShadowDy(), Color.BLACK);
                            Track.list2.remove(Track.list2.size() - 1);
                            if(HomeFragment1.seekBar.getVisibility() == View.VISIBLE){
                                HomeFragment1.seekBar.setProgress((int) (combineditem.getTextlayout2().getTextView().getShadowRadius()*5));

                            }
                            break;
                        }

                    }

                }


            }
            else if(Track.list2.get(Track.list2.size()-1).isFontR()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combinedItem.getTextlayout2().getId(),true,combinedItem.getTextlayout2().getTextView().getTypeface()));
                            combinedItem.getTextlayout2().getTextView().setTypeface(Track.list2.get(Track.list2.size() - 1).getTypeface(), Typeface.NORMAL);
                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }
                    }
                }

            }
            else if(Track.list2.get(Track.list2.size()-1).isIssize()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(true,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getTextSize()));

                            combinedItem.getTextlayout2().getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,Track.list2.get(Track.list2.size() - 1).getTextSize());
                            HomeFragment1.sizeSeekBar.setProgress((int) (Track.list2.get(Track.list2.size() - 1).getTextSize()-10f));
                            Track.list2.remove(Track.list2.size() - 1);
                            break;

                        }
                    }

                }
            }
            else if(Track.list2.get(Track.list2.size()-1).isSpace()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combinedItem.getTextlayout2().getTextView().getLetterSpacing(),true,combinedItem.getTextlayout2().getId()));
                            combinedItem.getTextlayout2().getTextView().setLetterSpacing(Track.list2.get(Track.list2.size() - 1).getSpacing());
                            HomeFragment1.spaceSeekBar.setProgress((int) combinedItem.getTextlayout2().getTextView().getLetterSpacing()*pxTodp(33));

                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list2.get(Track.list2.size()-1).isEdittext()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            int  index1 = MainActivity.textLayoutList2.indexOf(combinedItem.getTextlayout2());
                            Track.list.add(new Track(true,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getText().toString(),index1));

                            combinedItem.getTextlayout2().getTextView().setText(Track.list2.get(Track.list2.size() - 1).getText());
                            if(Track.list2.get(Track.list2.size() - 1).getTextView() != null){

                                int index =-1;
                                for(int i=0;i<MainActivity.textLayoutList2.size();i++){
                                    TextLayout textLayout = MainActivity.textLayoutList2.get(i);
                                    if(textLayout.getTextView() == Track.list2.get(Track.list2.size() - 1).getTextView()){
                                        index = i;
                                        break;
                                    }}
                                if (index != -1) {
                                    TextHandlerClass.textList.set(index, Track.list2.get(Track.list2.size() - 1).getText());
                                }
                            }
                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }}}}

            else if(Track.list2.get(Track.list2.size()-1).isImgOpacity()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getImageLayout()!=null){
                        if(combinedItem.getImageLayout().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combinedItem.getImageLayout().getId(),true,combinedItem.getImageLayout().getImageView().getAlpha()));
                            combinedItem.getImageLayout().getImageView().setAlpha(Track.list2.get(Track.list2.size() - 1).getImgopacity1());
                            HomeFragment1.opacitySeekBar.setProgress((int) (Track.list2.get(Track.list2.size() - 1).getImgopacity1()*100));
                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list2.get(Track.list2.size()-1).isFlip()){
                for (CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getImageLayout()!=null){
                        if(combinedItem.getImageLayout().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(combinedItem.getImageLayout().getImageView(),combinedItem.getImageLayout().getId(),combinedItem.getImageLayout().getImageView().getScaleX(),true));
                            combinedItem.getImageLayout().getImageView().setScaleX(Track.list2.get(Track.list2.size() - 1).getFlipx());
                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }
                    }
                    else if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            Track.list.add(new Track(null,combinedItem.getTextlayout2().getId(),combinedItem.getTextlayout2().getTextView().getScaleX(),true));
                            combinedItem.getTextlayout2().getTextView().setScaleX(Track.list2.get(Track.list2.size() - 1).getFlipx());
                            Track.list2.remove(Track.list2.size() - 1);
                            break;
                        }
                    }
                }
            }
            else if(Track.list2.get(Track.list2.size()-1).isUnderline()){
                for(CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            if(Track.list2.get(Track.list2.size()-1).isIsunderline()){
                                Track.list.add(new Track(combinedItem.getTextlayout2().getId(),false,true));
                                combinedItem.getTextlayout2().getTextView().setPaintFlags(combinedItem.getTextlayout2().getTextView().getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            }
                            else {
                                Track.list.add(new Track(combinedItem.getTextlayout2().getId(),true,true));

                                combinedItem.getTextlayout2().getTextView().setPaintFlags(combinedItem.getTextlayout2().getTextView().getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                            }
                        }
                        Track.list2.remove(Track.list2.size() - 1);
                        break;
                    }
                }
            }
            else if(Track.list2.get(Track.list2.size()-1).isAllign()){
                for(CombinedItem combinedItem: MainActivity.combinedItemList){
                    if(combinedItem.getTextlayout2()!=null){
                        if(combinedItem.getTextlayout2().getId() == Track.list2.get(Track.list2.size() - 1).getTid()){
                            int a =  combinedItem.getTextlayout2().getTextView().getTextAlignment();
                            int b = 0;
                            if (a == View.TEXT_ALIGNMENT_TEXT_START){ b = 1;}
                            else if (a == View.TEXT_ALIGNMENT_CENTER){ b = 2;}
                            else if (a == View.TEXT_ALIGNMENT_TEXT_END){ b = 3;}
                            Track.list.add(new Track(combinedItem.getTextlayout2().getId(),b ,true));

                            int c = Track.list2.get(Track.list2.size() - 1).getTextAlignment();
                            if(c==1){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);}
                            else if(c==2){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);}
                            else if(c==3){combinedItem.getTextlayout2().getTextView().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);}
                            String currentText = combinedItem.getTextlayout2().getTextView().getText().toString();
                            combinedItem.getTextlayout2().getTextView().setText(currentText);
                        }
                        Track.list2.remove(Track.list2.size() - 1);
                        break;
                    }
                }
            }
        }
        else {
            Toast.makeText(context, "size 0", Toast.LENGTH_SHORT).show();}
    }
    public static int pxTodp(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context1.getResources().getDisplayMetrics());
    }
}
