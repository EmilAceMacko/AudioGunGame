package Game;

import processing.core.PImage;
import processing.core.PVector;
import processing.sound.*;

import static processing.core.PApplet.map;

public class Mixer implements Constants {
    // Mixer variables:
    public int waveform;
    public int frequency;
    public CheckBox[] checkbox;
    public float sliderPos, sliderGrabOffset;
    public boolean sliderGrabbed;
    public SinOsc sinOsc, sinOsc2;
    public Env sinEnv, sinEnv2;
    public Reverb sinReverb;
    public HighPass sinHighPass;
    public SqrOsc sqrOsc, sqrOsc2;
    public Env sqrEnv, sqrEnv2;
    public Reverb sqrReverb;
    public HighPass sqrHighPass;
    public SawOsc sawOsc, sawOsc2;
    public Env sawEnv, sawEnv2;
    public Reverb sawReverb;
    public HighPass sawHighPass;
    public boolean playing;
    public int playingOsc;
    public Waveform waveSpectrum;
    public SoundFile music;
    public boolean[] playedOsc;
    public int waveSpectrumAnalyzing;
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
    public static final int FREQ_MIN = 220; // The lowest frequency of the mixer.
    public static final int FREQ_MAX = 880; // The highest frequency of the mixer.
    public static final float FREQ_OVERTONE_MULTIPLIER = 2f; // The highest overtone frequency multiplier, which is built towards when an object loses "health".
    public static final float VOLUME_SINE = 0.2f; // Volume of the oscillators (from 0 to 1).
    public static final float VOLUME_SQUARE = 0.1f; // Volume of the oscillators (from 0 to 1).
    public static final float VOLUME_SAW = 0.1f; // Volume of the oscillators (from 0 to 1).
    public static final float VOLUME_OVERTONE_MULTIPLIER = 0.3f; // How loud the overtone oscillators should be in relation to the primary oscillators.
    public static final float VOLUME_MUSIC = 0.05f; // The volume of the background music (from 0 to 1).
    public static final int WINDOW_OUTER_X = 3 * SCALE; // The X coordinate of the outer (left) edge of the waveform window.
    public static final int WINDOW_OUTER_Y = 3 * SCALE + GUI_Y; // The Y coordinate of the outer (top) edge of the waveform window.
    public static final int WINDOW_INNER_X = 4 * SCALE; // The X coordinate of the inner (left) edge of the waveform window.
    public static final int WINDOW_INNER_Y = 4 * SCALE + GUI_Y; // The Y coordinate of the inner (top) edge of the waveform window.
    public static final int WINDOW_INNER_WIDTH = 56 * SCALE; // The width inside the waveform window.
    public static final int WINDOW_INNER_HEIGHT = 40 * SCALE; // The height inside the waveform window.
    public static final float ENV_ATTACK = 0.01f; // The attack time of the oscillator envelopes.
    public static final float ENV_SUSTAIN = 0.004f; // The sustain time of the oscillator envelopes.
    public static final float ENV_DECAY = 0.3f; // The decay level of the oscillator envelopes.
    public static final float ENV_RELEASE = 0.5f; // The release time of the oscillator envelopes.
    public static final float REVERB_ROOM_MAX = 1.0f; // The maximum reverb room size (at max distance away from target).
    public static final float REVERB_DAMP_MAX = 1.0f; // The maximum reverb dampening (at max distance away from target).
    public static final float REVERB_WET_MAX = 0.5f; // The maximum reverb wetness (at max distance away from target).
    public static final int waveSamples = 400; // How many samples will be retrieved when analyzing.

    // Constructor:
    public Mixer() {
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
        playing = false; // Whether the oscillator(s) should be playing.
        playingOsc = WAVE_NONE; // Which oscillator is currently playing.
        waveSpectrum = new Waveform(Sketch.processing, waveSamples);
        waveSpectrumAnalyzing = WAVE_NONE; // The waveform that is currently being analyzed.
        music = Game.getSound(SND_MUSIC); // The music to play in the background.
        playedOsc = new boolean[4]; // Stores whether each of the three oscillators have been played at least once (index zero is "WAVE_NONE").

        // Play the music:
        music.amp(VOLUME_MUSIC); // Set volume.
        music.loop(); // Play and loop.

        // Oscillators:
        sinOsc = new SinOsc(Sketch.processing); // Sine Oscillator.
        sinOsc2 = new SinOsc(Sketch.processing); // Sine Oscillator (Overtone).
        sinEnv = new Env(Sketch.processing); // Sine Envelope.
        sinEnv2 = new Env(Sketch.processing); // Sine Envelope (Overtone).
        sinReverb = new Reverb(Sketch.processing); // Sine Reverb.
        sinHighPass = new HighPass(Sketch.processing); // Sine High-Pass (Overtone).

        sqrOsc = new SqrOsc(Sketch.processing); // Square Oscillator.
        sqrOsc2 = new SqrOsc(Sketch.processing); // Square Oscillator (Overtone).
        sqrEnv = new Env(Sketch.processing); // Square Envelope.
        sqrEnv2 = new Env(Sketch.processing); // Square Envelope (Overtone).
        sqrReverb = new Reverb(Sketch.processing); // Square Reverb.
        sqrHighPass = new HighPass(Sketch.processing); // Square High-Pass (Overtone).

        sawOsc = new SawOsc(Sketch.processing); // Saw Oscillator.
        sawOsc2 = new SawOsc(Sketch.processing); // Saw Oscillator (Overtone).
        sawEnv = new Env(Sketch.processing); // Saw Envelope.
        sawEnv2 = new Env(Sketch.processing); // Saw Envelope (Overtone).
        sawReverb = new Reverb(Sketch.processing); // Saw Reverb.
        sawHighPass = new HighPass(Sketch.processing); // Saw High-Pass (Overtone).

        if (SOUNDFUL) {
            sinReverb.process(sinOsc);
            sqrReverb.process(sqrOsc);
            sawReverb.process(sawOsc);
            sinHighPass.process(sinOsc2);
            sqrHighPass.process(sqrOsc2);
            sawHighPass.process(sawOsc2);
        }
    }

    public void update() {
        // Checkboxes:
        for (int i = 1; i < checkbox.length; i++) {
            if (Game.pointInArea(Game.mouse, checkbox[i].pos.x, checkbox[i].pos.y, checkbox[i].width, checkbox[i].height)) { // If the mouse is above the checkbox:
                if (Game.getInput(MB_LEFT, PRESS)) { // If the left mouse button is being pressed:
                    resetCheckBoxes(); // Uncheck all checkboxes.
                    //stopOscillators(); // Stop oscillators.
                    checkbox[i].checked = true; // Check the checkbox.
                    waveform = i; // Set the current waveform to match the checkbox' waveform.
                }
            }
        }
        // Checkboxes via keys/buttons 1, 2, 3:
        if (Game.getInput(B_1, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            checkbox[WAVE_SINE].checked = true; // Check the checkbox.
            waveform = WAVE_SINE; // Set the current waveform to match the checkbox' waveform.
        }
        if (Game.getInput(B_2, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
            checkbox[WAVE_SQUARE].checked = true; // Check the checkbox.
            waveform = WAVE_SQUARE; // Set the current waveform to match the checkbox' waveform.
        }
        if (Game.getInput(B_3, PRESS)) {
            resetCheckBoxes(); // Uncheck all checkboxes.
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

        // Analyze the currently playing waveform in order to visualize the sample data:
        if(playedOsc[waveform]) waveSpectrum.analyze();
    }
    // Uncheck all checkboxes:
    public void resetCheckBoxes() {
        for (int i = 1; i < checkbox.length; i++) checkbox[i].checked = false;
    }

    // Analyze a specific waveform:
    public void analyzeWaveform(int waveform) {
        if(waveSpectrumAnalyzing != waveform) { // If not already analyzing the waveform:
            waveSpectrumAnalyzing = waveform; // Set the waveform as being analyzed.
            switch(waveform) { // Set the related oscillator as the input for the wave analyzer:
                case (WAVE_SINE): waveSpectrum.input(sinOsc); break;
                case (WAVE_SQUARE): waveSpectrum.input(sqrOsc); break;
                case (WAVE_SAW): waveSpectrum.input(sawOsc); break;
            }
        }
    }

    // Play the oscillator that corresponds to the current waveform:
    public void playOscillators(float audioDist, boolean audioAffecting, float overtonePitchMultiplier) {
        playedOsc[waveform] = true; // The current oscillator/waveform has now been played at least once.
        overtonePitchMultiplier = map(overtonePitchMultiplier, 0f, 1f, 1f, FREQ_OVERTONE_MULTIPLIER); // Get the overtone frequency multiplier (between 1 and the max multiplier).
        switch (waveform) {
            case (WAVE_SINE): { // Play the sine wave oscillator:
                sinOsc.play(frequency, VOLUME_SINE);
                sinEnv.play(sinOsc, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                if (audioAffecting && SOUNDFUL) { // If SOUNDFUL and the audio affects the target, play the overtone:
                    sinOsc2.play(frequency * overtonePitchMultiplier, VOLUME_SINE * VOLUME_OVERTONE_MULTIPLIER);
                    sinEnv2.play(sinOsc2, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                }
                break;
            }
            case (WAVE_SQUARE): { // Play the square wave oscillator:
                sqrOsc.play(frequency, VOLUME_SQUARE);
                sqrEnv.play(sqrOsc, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                if (audioAffecting && SOUNDFUL) { // If SOUNDFUL and the audio affects the target, play the overtone:
                    sqrOsc2.play(frequency * overtonePitchMultiplier, VOLUME_SQUARE * VOLUME_OVERTONE_MULTIPLIER);
                    sqrEnv2.play(sqrOsc2, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                }
                break;
            }
            case (WAVE_SAW): { // Play the saw wave oscillator:
                sawOsc.play(frequency, VOLUME_SAW);
                sawEnv.play(sawOsc, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                if (audioAffecting && SOUNDFUL) { // If SOUNDFUL and the audio affects the target, play the overtone:
                    sawOsc2.play(frequency * overtonePitchMultiplier, VOLUME_SAW * VOLUME_OVERTONE_MULTIPLIER);
                    sawEnv2.play(sawOsc2, ENV_ATTACK, ENV_SUSTAIN, ENV_DECAY, ENV_RELEASE);
                }
                break;
            }
        }
        // Analyze the waveform being played:
        analyzeWaveform(waveform);

        if(SOUNDFUL) {
            // Distance reverberation:
            float fac = audioDist / RAY_LENGTH;
            if (fac > 1.0f) fac = 1.0f;
            else if (fac < 0.0f) fac = 0.0f;
            sinReverb.set(fac * REVERB_ROOM_MAX, fac * REVERB_DAMP_MAX, fac * REVERB_WET_MAX);
            sqrReverb.set(fac * REVERB_ROOM_MAX, fac * REVERB_DAMP_MAX, fac * REVERB_WET_MAX);
            sawReverb.set(fac * REVERB_ROOM_MAX, fac * REVERB_DAMP_MAX, fac * REVERB_WET_MAX);
            // General high-pass filter for the overtone(s):
            sinHighPass.freq(frequency * 0.5f);
            sqrHighPass.freq(frequency * 0.5f);
            sawHighPass.freq(frequency * 0.5f);
        }
    }

    // Get the low/mid/high representation of the current frequency:
    public int getFrequencyScale() {
        int freqSpectrum = FREQ_MAX - FREQ_MIN; // The distance between min and max frequency.
        int localFrequency = frequency - FREQ_MIN; // Where the current frequency sits between min and max.
        if (localFrequency < freqSpectrum / 4) return FREQ_LOW; // If below the bottom quarter of the spectrum, the frequency is low.
        else if(localFrequency < 3 * freqSpectrum / 4) return FREQ_MID; // Otherwise, if below the top quarter of the spectrum, the frequency is mid.
        else return FREQ_HIGH; // Otherwise, the frequency is above (or equal to) the top quarter of the spectrum, and is therefore high.
    }

    public void display() {
        Game.drawSprite(SPR_GUI_BACKGROUND, 0, GUI_Y); // Draw background.
        for (int i = 1; i < checkbox.length; i++) checkbox[i].display(); // Draw checkboxes.
        // Draw the frequency slider:
        int width = GAUGE_WIDTH / SCALE;
        int cutX = (int)(sliderPos * width);
        // Gauge (left part):
        if (cutX > 0) {
            PImage gLeft = Game.assetMgr.spriteSheet[SPR_GUI_GAUGE_START + 1].get(0, 0, cutX, GAUGE_HEIGHT / SCALE);
            Sketch.processing.image(gLeft, GAUGE_X, GAUGE_Y, gLeft.width * SCALE, gLeft.height * SCALE);
        }
        // Gauge (right part):
        if (cutX < GAUGE_WIDTH / SCALE) {
            PImage gRight = Game.assetMgr.spriteSheet[SPR_GUI_GAUGE_START].get(cutX, 0, width - cutX, GAUGE_HEIGHT / SCALE);
            Sketch.processing.image(gRight, GAUGE_X + cutX * SCALE, GAUGE_Y, gRight.width * SCALE, gRight.height * SCALE);
        }
        // Slider knob:
        Game.drawSprite(SPR_GUI_SLIDER, GAUGE_X + sliderPos * GAUGE_WIDTH - SLIDER_WIDTH / 2f, SLIDER_Y);
        // Waveform window:
        Game.drawSprite(SPR_GUI_WINDOW, WINDOW_OUTER_X, WINDOW_OUTER_Y);
        // Waveform graph:
        drawWaveform();
    }
    // Draw a graph of the waveform that is being analyzed:
    public void drawWaveform() {
        Sketch.processing.strokeWeight(2);
        Sketch.processing.stroke(255);
        Sketch.processing.noFill();
        int w = WINDOW_INNER_WIDTH;
        int h = WINDOW_INNER_HEIGHT;
        int x = WINDOW_INNER_X;
        int y = WINDOW_INNER_Y;
        float volume = 0;
        switch (waveform) {
            case (WAVE_SINE): volume = VOLUME_SINE; break;
            case (WAVE_SQUARE): volume = VOLUME_SQUARE; break;
            case (WAVE_SAW): volume = VOLUME_SAW; break;
        }
        if(playedOsc[waveform]) { // Playing the current oscillator/waveform:
            Sketch.processing.beginShape();
            for (int i = 0; i < waveSamples; i++) {
                float vx = map(i, 0, waveSamples, x, x + w);
                float vy = map(waveSpectrum.data[i], -volume, volume, y, y + h);
                if (vy < y) vy = y;
                else if (vy > y + h) vy = y + h;
                Sketch.processing.vertex(vx, vy);
            }
            Sketch.processing.endShape();
        } else { // Nothing is being played:
            // Draw a blank "waveform" (line):
            Sketch.processing.line(x, y + h / 2f, x + w, y + h / 2f);
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
