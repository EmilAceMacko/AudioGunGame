package Game;

import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SinOsc;
import processing.sound.SqrOsc;
import processing.sound.SawOsc;

public class Mixer implements Constants {
    // Mixer variables:
    public int waveform;
    public int frequency;
    public CheckBox[] checkbox;
    public float sliderPos, sliderGrabOffset;
    public boolean sliderGrabbed;
    public SinOsc sinOsc;
    public SqrOsc sqrOsc;
    public SawOsc sawOsc;
    public boolean playing;
    public int playingOsc;
    // Mixer constants:
    public static final int GUI_Y = ROOM_HEIGHT; // The y-coordinate of the top of the mixer.
    public static final int CHECKBOX_X = 146 * SCALE;
    public static final int CHECKBOX_Y = GUI_Y + 2 * SCALE;
    public static final int CHECKBOX_SPACING = 32 * SCALE;
    public static final int GAUGE_X = 147 * SCALE;
    public static final int GAUGE_Y = GUI_Y + 19 * SCALE;
    public static final int GAUGE_WIDTH = 106 * SCALE;
    public static final int GAUGE_HEIGHT = 10 * SCALE;
    public static final int SLIDER_Y = GAUGE_Y - 4 * SCALE;
    public static final int SLIDER_WIDTH = 8 * SCALE;
    public static final int SLIDER_HEIGHT = 12 * SCALE;
    public static final int FREQ_MIN = 110; // The mixer's lowest frequency.
    public static final int FREQ_MAX = 880; // The mixer's highest frequency.
    public static final int VOLUME_SINE = 20; // Volume of the oscillators (from 0 to 100).
    public static final int VOLUME_SQUARE = 10; // Volume of the oscillators (from 0 to 100).
    public static final int VOLUME_SAW = 10; // Volume of the oscillators (from 0 to 100).

    Mixer() {
        // Default values:
        waveform = WAVE_SINE;
        frequency = FREQ_MIN + (FREQ_MAX - FREQ_MIN) / 2;
        checkbox = new CheckBox[4]; // The zeroth index is just null for the "WAVE_NONE waveform".
        checkbox[WAVE_SINE] =   new CheckBox(CHECKBOX_X, CHECKBOX_Y, true);
        checkbox[WAVE_SQUARE] = new CheckBox(CHECKBOX_X + CHECKBOX_SPACING, CHECKBOX_Y, false);
        checkbox[WAVE_SAW] =    new CheckBox(CHECKBOX_X + 2 * CHECKBOX_SPACING, CHECKBOX_Y, false);
        sliderPos = 0.5f; // The starting positon of the slider (between 0 and 1).
        sliderGrabOffset = 0.0f; // The horizontal offset between the slider and the cursor upon grab.
        sliderGrabbed = false; // Whether the slider is currently grabbed by the cursor.
        sinOsc = new SinOsc(Sketch.processing); // Sine Oscillator.
        sqrOsc = new SqrOsc(Sketch.processing); // Square Oscillator.
        sawOsc = new SawOsc(Sketch.processing); // Saw Oscillator.
        playing = false; // Whether the oscillator(s) should be playing.
        playingOsc = WAVE_NONE; // Which oscillator is currently playing.
    }

    public void update() {
        // Checkboxes:
        for (int i = 1; i < checkbox.length; i++) {
            if (Game.pointInArea(Game.mouse, checkbox[i].pos.x, checkbox[i].pos.y, checkbox[i].width, checkbox[i].height)) { // If the mouse is above the checkbox:
                if (Game.getInput(MB_LEFT, PRESS)) { // If the left mouse button is being pressed:
                    resetCheckBoxes(); // Uncheck all checkboxes.
                    stopOscillators(); // Stop oscillators.
                    checkbox[i].checked = true; // Check the checkbox.
                    waveform = i; // Set the current waveform to match the checkbox' waveform.
                }
            }
        }
        // Checkboxes via keys/buttons 1, 2, 3:
        if(Game.getInput(B_1, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SINE].checked = true; // Check the checkbox.
            waveform = WAVE_SINE; // Set the current waveform to match the checkbox' waveform.
        }
        if(Game.getInput(B_2, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SQUARE].checked = true; // Check the checkbox.
            waveform = WAVE_SQUARE; // Set the current waveform to match the checkbox' waveform.
        }
        if(Game.getInput(B_3, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SAW].checked = true; // Check the checkbox.
            waveform = WAVE_SAW; // Set the current waveform to match the checkbox' waveform.
        }

        // Slider:
        if(sliderGrabbed) {
            if (Game.getInput(MB_LEFT, NONE)) { // If the left mouse button is not being held:
                sliderGrabbed = false; // Let go of the slider.
            } else { // Left mouse button is still being held:
                // Follow the cursor:
                sliderPos = (Game.mouse.x - sliderGrabOffset - GAUGE_X) / GAUGE_WIDTH;
                // Constrain slider:
                if (sliderPos < 0.0f) sliderPos = 0.0f;
                else if (sliderPos > 1.0f) sliderPos = 1.0f;
                // Set frequency based on the slider position:
                frequency = FREQ_MIN + (int)((FREQ_MAX-FREQ_MIN) * sliderPos);
            }
        } else { // Slider is not grabbed:
            if (Game.pointInArea(Game.mouse, GAUGE_X + sliderPos * GAUGE_WIDTH - SLIDER_WIDTH / 2f, SLIDER_Y, SLIDER_WIDTH, SLIDER_HEIGHT)) { // If the mouse is above the slider:
                if (Game.getInput(MB_LEFT, PRESS)) { // If the left mouse button is being pressed:
                    // Grab the slider:
                    sliderGrabbed = true;
                    sliderGrabOffset = Game.mouse.x - (GAUGE_X + sliderPos * GAUGE_WIDTH);
                }
            }
        }
        // Slider with mouse wheel:
        if (!sliderGrabbed) {
            if (Game.mouseWheel != 0) {
                // Slide the slider according to the mouse wheel:
                sliderPos += Game.mouseWheel * 0.05;
                // Constrain slider:
                if (sliderPos < 0.0f) sliderPos = 0.0f;
                else if (sliderPos > 1.0f) sliderPos = 1.0f;
                // Set frequency based on the slider position:
                frequency = FREQ_MIN + (int)((FREQ_MAX-FREQ_MIN) * sliderPos);
            }
        }

        // Oscillators:
        if (playing) {
            playOscillators();
            updateOscillators();
        } else {
            stopOscillators();
        }
    }

    public void resetCheckBoxes() {
        for (int i = 1; i < checkbox.length; i++) checkbox[i].checked = false;
    }

    public void playOscillators() {
        if(playingOsc == WAVE_NONE) {
            switch (waveform) {
                case (WAVE_SINE) -> sinOsc.play();
                case (WAVE_SQUARE) -> sqrOsc.play();
                case (WAVE_SAW) -> sawOsc.play();
            }
            playingOsc = waveform;
        }
    }
    public void updateOscillators() {
        // Update volume and frequency of oscillators:
        if(playingOsc != WAVE_NONE) {
            switch (waveform) {
                case (WAVE_SINE) -> {
                    sinOsc.freq(frequency);
                    sinOsc.amp(VOLUME_SINE / 100f);
                }
                case (WAVE_SQUARE) -> {
                    sqrOsc.freq(frequency);
                    sqrOsc.amp(VOLUME_SQUARE / 100f);
                }
                case (WAVE_SAW) -> {
                    sawOsc.freq(frequency);
                    sawOsc.amp(VOLUME_SAW / 100f);
                }
            }
        }
    }
    public void stopOscillators() {
        switch (playingOsc) {
            case (WAVE_SINE) -> sinOsc.stop();
            case (WAVE_SQUARE) -> sqrOsc.stop();
            case (WAVE_SAW) -> sawOsc.stop();
        }
        playingOsc = WAVE_NONE;
    }

    public void display() {
        Game.drawSprite(SPR_GUI_BACKGROUND, 0, GUI_Y); // Draw background.
        for (int i = 1; i < checkbox.length; i++) checkbox[i].display(); // Draw checkboxes.

        // Draw the frequency slider:
        int width = GAUGE_WIDTH / SCALE;
        int cutX = (int)(sliderPos * width);
        // Gauge (left):
        if (cutX > 0) {
            PImage gLeft = Game.assetMgr.spriteSheet[SPR_GUI_GAUGE_START + 1].get(0, 0, cutX, GAUGE_HEIGHT / SCALE);
            Sketch.processing.image(gLeft, GAUGE_X, GAUGE_Y, gLeft.width * SCALE, gLeft.height * SCALE);
        }
        if (cutX < GAUGE_WIDTH / SCALE) {
            PImage gRight = Game.assetMgr.spriteSheet[SPR_GUI_GAUGE_START].get(cutX, 0, width - cutX, GAUGE_HEIGHT / SCALE);
            Sketch.processing.image(gRight, GAUGE_X + cutX * SCALE, GAUGE_Y, gRight.width * SCALE, gRight.height * SCALE);
        }
        // Slider knob:
        Game.drawSprite(SPR_GUI_SLIDER, GAUGE_X + sliderPos * GAUGE_WIDTH - SLIDER_WIDTH / 2f, SLIDER_Y);
    }

    // Nested class for waveform checkboxes:
    public static class CheckBox {
        public PVector pos;
        public int width, height;
        public boolean checked;

        public static final int SPR_X_OFF = -2 * SCALE;
        public static final int SPR_Y_OFF = -2 * SCALE;

        public CheckBox(int x, int y, boolean _checked) {
            this(new PVector(x, y), _checked);
        }
        public CheckBox(PVector _pos, boolean _checked) {
            pos = _pos;
            checked = _checked;
            width = 12 * SCALE;
            height = 12 * SCALE;
        }

        public void display() {
            Game.drawSprite(SPR_GUI_CHECKBOX_START + (checked ? 1 : 0), pos.x + SPR_X_OFF, pos.y + SPR_Y_OFF);
        }
    }
}
