package Game;

import processing.core.PImage;
import processing.core.PVector;
import processing.sound.*;

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
    public LowPass lowPass;
    public boolean playing;
    public int playingOsc;
    public float[] samples;
    public float waveScale;
    public float waveCycle;
    public float audioDist;
    public SoundFile music;
    // Mixer constants:
    public static final int GUI_Y = ROOM_HEIGHT; // The y-coordinate of the top of the mixer.
    public static final int CHECKBOX_X = 146 * SCALE; // The starting X of the leftmost checkbox.
    public static final int CHECKBOX_Y = 2 * SCALE + GUI_Y; // The Y coordinate of the checkboxes.
    public static final int CHECKBOX_SPACING = 32 * SCALE; // The space between the checkboxes.
    public static final int GAUGE_X = 147 * SCALE; // The X coordinate of the frequency gauge (from its upper left corner).
    public static final int GAUGE_Y = 19 * SCALE + GUI_Y; // The Y coordinate of the frequency gauge (from its upper left corner).
    public static final int GAUGE_WIDTH = 106 * SCALE; // The width of the frequency gauge.
    public static final int GAUGE_HEIGHT = 10 * SCALE; // The height of the frequency gauge.
    public static final int SLIDER_Y = GAUGE_Y - 4 * SCALE; // The Y coordinate of the frequency slider (from its upper left corner).
    public static final int SLIDER_WIDTH = 8 * SCALE; // The width of the frequency slider.
    public static final int SLIDER_HEIGHT = 12 * SCALE; // The height of the frequency slider.
    public static final int FREQ_MIN = 110; // The mixer's lowest frequency.
    public static final int FREQ_MAX = 880; // The mixer's highest frequency.
    public static final float VOLUME_SINE = 0.2f; // Volume of the oscillators (from 0 to 1).
    public static final float VOLUME_SQUARE = 0.1f; // Volume of the oscillators (from 0 to 1).
    public static final float VOLUME_SAW = 0.1f; // Volume of the oscillators (from 0 to 1).
    public static final int WINDOW_OUTER_X = 3 * SCALE; // The X coordinate of the outer (left) edge of the waveform window.
    public static final int WINDOW_OUTER_Y = 3 * SCALE + GUI_Y; // The Y coordinate of the outer (top) edge of the waveform window.
    public static final int WINDOW_INNER_X = 4 * SCALE; // The X coordinate of the inner (left) edge of the waveform window.
    public static final int WINDOW_INNER_Y = 4 * SCALE + GUI_Y; // The Y coordinate of the inner (top) edge of the waveform window.
    public static final int WINDOW_INNER_WIDTH = 56 * SCALE; // The width inside the waveform window.
    public static final int WINDOW_INNER_HEIGHT = 40 * SCALE; // The height inside the waveform window.
    public static final float VOLUME_MUSIC = 0.05f; // The volume of the background music (from 0 to 1).
    // Constructor:
    Mixer() {
        // Default values:
        waveform = WAVE_SINE; // The currently selected waveform.
        frequency = FREQ_MIN + (FREQ_MAX - FREQ_MIN) / 2; // The currently selected waveform (starts in the middle).
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
        lowPass = new LowPass(Sketch.processing); // Low-Pass Effect.
        playing = false; // Whether the oscillator(s) should be playing.
        playingOsc = WAVE_NONE; // Which oscillator is currently playing.
        samples = new float[WINDOW_INNER_WIDTH]; // The array carrying each audio sample to display in the waveform window.
        waveScale = 100; // The horizontal scale of the waveform (in Hz).
        waveCycle = 0; // Carries the wave-cycle offset for each frame.
        audioDist = 0; // The distance between the audio gun and the target that gets hit by the audio ray.
        music = Game.getSound(SND_MUSIC); // The music to play in the background.

        // Play the music:
        music.amp(VOLUME_MUSIC); // Set volume.
        music.loop(); // Play and loop.
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
        if (Game.getInput(B_1, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SINE].checked = true; // Check the checkbox.
            waveform = WAVE_SINE; // Set the current waveform to match the checkbox' waveform.
        }
        if (Game.getInput(B_2, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SQUARE].checked = true; // Check the checkbox.
            waveform = WAVE_SQUARE; // Set the current waveform to match the checkbox' waveform.
        }
        if (Game.getInput(B_3, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            stopOscillators(); // Stop oscillators.
            checkbox[WAVE_SAW].checked = true; // Check the checkbox.
            waveform = WAVE_SAW; // Set the current waveform to match the checkbox' waveform.
        }
        // Slider:
        if (sliderGrabbed) {
            if (Game.getInput(MB_LEFT, NONE)) { // If the left mouse button is not being held:
                sliderGrabbed = false; // Let go of the slider.
            } else { // Left mouse button is still being held:
                // Follow the cursor:
                sliderPos = (Game.mouse.x - sliderGrabOffset - GAUGE_X) / GAUGE_WIDTH;
                // Constrain slider:
                if (sliderPos < 0.0f) sliderPos = 0.0f;
                else if (sliderPos > 1.0f) sliderPos = 1.0f;
                // Set frequency based on the slider position:
                frequency = FREQ_MIN + (int) ((FREQ_MAX - FREQ_MIN) * sliderPos);
            }
        } else { // Slider is not grabbed:
            if (Game.pointInArea(Game.mouse, GAUGE_X + sliderPos * GAUGE_WIDTH - SLIDER_WIDTH / 2f, SLIDER_Y, SLIDER_WIDTH, SLIDER_HEIGHT)) { // If the mouse is above the slider:
                if (Game.getInput(MB_LEFT, PRESS)) { // If the left mouse button is being pressed:
                    sliderGrabbed = true; // Grab the slider.
                    sliderGrabOffset = Game.mouse.x - (GAUGE_X + sliderPos * GAUGE_WIDTH); // Set the grab-offset to the difference between the cursor and the slider position.
                }
            }
        }
        // Slider with mouse wheel:
        if (!sliderGrabbed) {
            if (Game.mouseWheel != 0) {
                // Slide the slider according to the mouse wheel:
                sliderPos -= Game.mouseWheel * 0.05;
                // Constrain slider:
                if (sliderPos < 0.0f) sliderPos = 0.0f;
                else if (sliderPos > 1.0f) sliderPos = 1.0f;
                // Set frequency based on the slider position:
                frequency = FREQ_MIN + (int) ((FREQ_MAX - FREQ_MIN) * sliderPos);
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
    // Uncheck all checkboxes:
    public void resetCheckBoxes() {
        for (int i = 1; i < checkbox.length; i++) checkbox[i].checked = false;
    }
    // Play the oscillator that corresponds to the current waveform.
    public void playOscillators() {
        if (playingOsc == WAVE_NONE) {
            switch (waveform) {
                case (WAVE_SINE) -> {
                    sinOsc.play();
                    lowPass.process(sinOsc);
                }
                case (WAVE_SQUARE) -> {
                    sqrOsc.play();
                    lowPass.process(sqrOsc);
                }
                case (WAVE_SAW) -> {
                    sawOsc.play();
                    lowPass.process(sawOsc);
                }
            }
            playingOsc = waveform;
        }
    }
    // Update the frequency and amplitude of the currently playing oscillator:
    public void updateOscillators() {
        // Update volume and frequency of oscillators:
        if (playingOsc != WAVE_NONE) {
            switch (waveform) {
                case (WAVE_SINE) -> {
                    sinOsc.freq(frequency);
                    sinOsc.amp(VOLUME_SINE);
                }
                case (WAVE_SQUARE) -> {
                    sqrOsc.freq(frequency);
                    sqrOsc.amp(VOLUME_SQUARE);
                }
                case (WAVE_SAW) -> {
                    sawOsc.freq(frequency);
                    sawOsc.amp(VOLUME_SAW);
                }
            }
            float fac = audioDist / RAY_LENGTH;
            lowPass.freq(FREQ_MAX * 2f + fac * (FREQ_MIN - FREQ_MAX) * 2f);
        }
    }
    // Stop oscillators (if one is playing):
    public void stopOscillators() {
        if (playingOsc != WAVE_NONE) {
            lowPass.stop();
            switch (playingOsc) {
                case (WAVE_SINE) -> sinOsc.stop();
                case (WAVE_SQUARE) -> sqrOsc.stop();
                case (WAVE_SAW) -> sawOsc.stop();
            }
            playingOsc = WAVE_NONE;
        }
    }
    // Get the low/mid/high representation of the current frequency:
    public int getFrequencyScale() {
        int freqSpectrum = FREQ_MAX - FREQ_MIN;
        if (frequency < freqSpectrum / 4) return FREQ_LOW;
        else if(frequency < 3 * freqSpectrum / 4) return FREQ_MID;
        else return FREQ_HIGH;
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
        // Gauge (right):
        if (cutX < GAUGE_WIDTH / SCALE) {
            PImage gRight = Game.assetMgr.spriteSheet[SPR_GUI_GAUGE_START].get(cutX, 0, width - cutX, GAUGE_HEIGHT / SCALE);
            Sketch.processing.image(gRight, GAUGE_X + cutX * SCALE, GAUGE_Y, gRight.width * SCALE, gRight.height * SCALE);
        }
        // Slider knob:
        Game.drawSprite(SPR_GUI_SLIDER, GAUGE_X + sliderPos * GAUGE_WIDTH - SLIDER_WIDTH / 2f, SLIDER_Y);
        // Waveform Window:
        Game.drawSprite(SPR_GUI_WINDOW, WINDOW_OUTER_X, WINDOW_OUTER_Y);
        drawWaveform();
    }

    public void drawWaveform() {
        Sketch.processing.strokeWeight(2);
        Sketch.processing.stroke(255);
        Sketch.processing.noFill();
        int w = WINDOW_INNER_WIDTH;
        int h = WINDOW_INNER_HEIGHT / 2;
        int x = WINDOW_INNER_X;
        int y = WINDOW_INNER_Y + h;
        int f = (int)(frequency * waveScale);
        // Generate visual audio waveforms:
        if (playing) {
            for (int i = 0; i < samples.length; i++) {
                float freq = (waveScale * w) / frequency;
                // Generate waveform samples mathematically:
                switch (waveform) {
                    case (WAVE_SINE) ->     samples[i] = (float) Math.sin(waveCycle / freq * 2 * Math.PI);
                    case (WAVE_SQUARE) ->   samples[i] = (waveCycle < freq / 2) ? 1.0f : -1.0f;
                    case (WAVE_SAW) ->      samples[i] = 1 - 2 * waveCycle / freq;
                }
                waveCycle += 1.0f;
                if (waveCycle >= freq) waveCycle -= freq;
            }
            // Draw samples as a point-line:
            Sketch.processing.beginShape();
            for (int i = 0; i < samples.length; i++) {
                Sketch.processing.vertex(x + i, y + samples[i] * 2 * h / 3);
            }
            Sketch.processing.endShape();
        } else {
            // Draw a blank waveform (line):
            Sketch.processing.line(x, y, x+w, y);
        }
    }

    // Nested class for waveform checkboxes:
    public static class CheckBox {
        // Variables:
        public PVector pos;
        public int width, height;
        public boolean checked;
        // Constants:
        public static final int SPR_X_OFF = -2 * SCALE;
        public static final int SPR_Y_OFF = -2 * SCALE;
        // Constructors:
        public CheckBox(int x, int y, boolean _checked) {
            this(new PVector(x, y), _checked);
        }
        public CheckBox(PVector _pos, boolean _checked) {
            // Default values:
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
