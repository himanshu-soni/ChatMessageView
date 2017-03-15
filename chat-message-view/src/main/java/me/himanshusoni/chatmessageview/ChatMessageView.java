package me.himanshusoni.chatmessageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.himanshusoni.chatmessageview.util.ViewUtil;

/**
 * Chat Message view to create chatting window view
 */
public class ChatMessageView extends RelativeLayout {
    private static final String TAG = "ChatMessageView";

    private ImageView arrowImage;
    private RelativeLayout containerLayout;

    private TintedBitmapDrawable normalDrawable, pressedDrawable;

    private float cornerRadius, contentPadding, arrowMargin;
    private boolean showArrow;
    private ArrowPosition arrowPosition;
    private ArrowGravity arrowGravity;
    private int backgroundColor, backgroundColorPressed;

    public ChatMessageView(Context context) {
        super(context);
        initialize(null, 0);
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs, 0);
    }

    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs, defStyleAttr);
    }

    private void initialize(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ChatMessageView, defStyleAttr, 0);

        showArrow = a.getBoolean(R.styleable.ChatMessageView_cmv_showArrow, true);
        arrowMargin = a.getDimension(R.styleable.ChatMessageView_cmv_arrowMargin, dip2px(5));
        cornerRadius = a.getDimension(R.styleable.ChatMessageView_cmv_cornerRadius, 0);
        contentPadding = a.getDimension(R.styleable.ChatMessageView_cmv_contentPadding, dip2px(10));
        backgroundColor = a.getColor(R.styleable.ChatMessageView_cmv_backgroundColor, 0);
        backgroundColorPressed = a.getColor(R.styleable.ChatMessageView_cmv_backgroundColorPressed, 0);

        int intPosition = a.getInt(R.styleable.ChatMessageView_cmv_arrowPosition, ArrowPosition.LEFT.getValue());
        arrowPosition = ArrowPosition.getEnum(intPosition);

        int intGravity = a.getInt(R.styleable.ChatMessageView_cmv_arrowGravity, ArrowGravity.START.getValue());
        arrowGravity = ArrowGravity.getEnum(intGravity);

        a.recycle();
        initContent();
    }

    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        if (!showArrow) {
            arrowImage.setVisibility(INVISIBLE);
        } else {
            arrowImage.setVisibility(VISIBLE);
        }
    }

    public void setArrowPosition(ArrowPosition position) {
        arrowPosition = position;
        updatePositionAndGravity();
    }

    public void setArrowGravity(ArrowGravity gravity) {
        arrowGravity = gravity;
        updatePositionAndGravity();
    }

    private void invalidateRules(LayoutParams params) {
        params.addRule(ALIGN_PARENT_LEFT, 0);
        params.addRule(ALIGN_PARENT_TOP, 0);
        params.addRule(ALIGN_PARENT_BOTTOM, 0);
        params.addRule(ALIGN_PARENT_RIGHT, 0);

        params.addRule(CENTER_HORIZONTAL, 0);
        params.addRule(CENTER_VERTICAL, 0);

        params.addRule(RIGHT_OF, 0);
        params.addRule(BELOW, 0);
        params.addRule(ABOVE, 0);
        params.addRule(LEFT_OF, 0);

        params.addRule(ALIGN_BOTTOM, 0);
        params.addRule(ALIGN_TOP, 0);
        params.addRule(ALIGN_LEFT, 0);
        params.addRule(ALIGN_RIGHT, 0);
    }


    private void updatePositionAndGravity() {
        LayoutParams arrowParams = (LayoutParams) arrowImage.getLayoutParams();
        LayoutParams containerParams = (LayoutParams) containerLayout.getLayoutParams();

        invalidateRules(arrowParams);
        invalidateRules(containerParams);

        int arrowRotation;
        switch (arrowPosition) {
            case TOP: {
                arrowRotation = 270;
                arrowParams.addRule(ALIGN_PARENT_TOP, TRUE);
                arrowParams.setMargins((int) arrowMargin, 0, (int) arrowMargin, 0);

                containerParams.addRule(BELOW, arrowImage.getId());
                break;
            }
            case BOTTOM: {
                arrowRotation = 90;
                //arrowParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
                arrowParams.setMargins((int) arrowMargin, 0, (int) arrowMargin, 0);

                arrowParams.addRule(BELOW, containerLayout.getId());
//                containerParams.addRule(ABOVE, arrowImage.getId());
                break;
            }
            case LEFT: {
                arrowRotation = 180;
                arrowParams.addRule(ALIGN_PARENT_LEFT, TRUE);
                arrowParams.setMargins(0, (int) arrowMargin, 0, (int) arrowMargin);

                containerParams.addRule(RIGHT_OF, arrowImage.getId());
                // arrowParams.addRule(LEFT_OF, containerLayout.getId());
                break;
            }
            default: {
                arrowRotation = 0;
                arrowParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
                arrowParams.setMargins(0, (int) arrowMargin, 0, (int) arrowMargin);
                containerParams.addRule(LEFT_OF, arrowImage.getId());
                break;
            }
        }

        switch (arrowGravity) {
            case START:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(ALIGN_LEFT, containerLayout.getId());
                } else {
                    arrowParams.addRule(ALIGN_TOP, containerLayout.getId());
                }
                break;
            case CENTER:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(CENTER_HORIZONTAL, TRUE);
                } else {
                    arrowParams.addRule(CENTER_VERTICAL, TRUE);
                }
                break;
            case END:
                if (arrowPosition == ArrowPosition.TOP || arrowPosition == ArrowPosition.BOTTOM) {
                    arrowParams.addRule(ALIGN_RIGHT, containerLayout.getId());
                } else {
                    arrowParams.addRule(ALIGN_BOTTOM, containerLayout.getId());
                }
                break;
            default:
                break;
        }

        Log.d(TAG, "updatePositionAndGravity: arrow: " + arrowParams.getRules().length);
        Log.d(TAG, "updatePositionAndGravity: container: " + containerParams.getRules().length);

        int arrowRes = R.drawable.cmv_arrow;
        Bitmap source = BitmapFactory.decodeResource(this.getResources(), arrowRes);
        Bitmap rotateBitmap = rotateBitmap(source, arrowRotation);

        normalDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, backgroundColor);
        pressedDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, backgroundColorPressed);

        arrowImage.setImageDrawable(normalDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            arrowImage.setImageTintList(ColorStateList.valueOf(backgroundColor));
        }


        arrowImage.setLayoutParams(arrowParams);
        containerLayout.setLayoutParams(containerParams);
    }

    public void setBackgroundColorRes(@ColorRes int bgColorRes, @ColorRes int bgPressedColorRes) {
        backgroundColorPressed = ContextCompat.getColor(getContext(), bgPressedColorRes);
        backgroundColor = ContextCompat.getColor(getContext(), bgColorRes);
        updateColors();
    }

    public void setBackgroundColor(@ColorInt int bgColorRes, @ColorInt int bgPressedColorRes) {
        backgroundColorPressed = bgPressedColorRes;
        backgroundColor = bgColorRes;
        updateColors();
    }

    public void setContentPadding(int contentPadding) {
        this.contentPadding = contentPadding;
        containerLayout.setPadding(contentPadding, contentPadding, contentPadding, contentPadding);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child != arrowImage && child != containerLayout) {
            removeView(child);
            containerLayout.addView(child);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initContent() {
        arrowImage = new ImageView(getContext());
        arrowImage.setId(ViewUtil.generateViewId());

        containerLayout = new RelativeLayout(getContext());
        containerLayout.setId(ViewUtil.generateViewId());

        setShowArrow(showArrow);
        setContentPadding((int) contentPadding);

        super.addView(arrowImage, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        super.addView(containerLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        updatePositionAndGravity();

        updateColors();
        this.setClickable(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateColors() {
        ChatMessageDrawable roundRectDrawable = new ChatMessageDrawable(backgroundColor, cornerRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            containerLayout.setBackground(roundRectDrawable);
        } else {
            containerLayout.setBackgroundDrawable(roundRectDrawable);
        }

        normalDrawable.setTint(backgroundColor);
        pressedDrawable.setTint(backgroundColorPressed);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            arrowImage.setImageTintList(ColorStateList.valueOf(backgroundColor));
        }

        Drawable stateDrawable = new ChatMessageStateDrawable(Color.TRANSPARENT) {
            @Override
            protected void onIsPressed(boolean isPressed) {
                ChatMessageDrawable conRlBackground = (ChatMessageDrawable) containerLayout.getBackground();
                if (isPressed) {
                    conRlBackground.setColor(backgroundColorPressed);
                    arrowImage.setImageDrawable(pressedDrawable);
                } else {
                    conRlBackground.setColor(backgroundColor);
                    arrowImage.setImageDrawable(normalDrawable);
                }
                containerLayout.invalidate();
                arrowImage.invalidate();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(stateDrawable);
        } else {
            setBackgroundDrawable(stateDrawable);
        }

    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public enum ArrowPosition {

        LEFT(0), RIGHT(1), TOP(2), BOTTOM(3);

        int value;

        ArrowPosition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ArrowPosition getEnum(int value) {
            switch (value) {
                case 0:
                    return LEFT;
                case 1:
                    return RIGHT;
                case 2:
                    return TOP;
                case 3:
                    return BOTTOM;
                default:
                    return LEFT;
            }
        }
    }

    public enum ArrowGravity {
        START(0), CENTER(1), END(2);

        int value;

        ArrowGravity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ArrowGravity getEnum(int value) {
            switch (value) {
                case 0:
                    return START;
                case 1:
                    return CENTER;
                case 2:
                    return END;
                default:
                    return START;
            }
        }

    }
}
