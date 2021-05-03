package Game;

public interface Assets {
    // Directory of the graphics files:
    String graphicsDir = "resources/graphics/";
    // Filename of the tile-sheet image:
    String tileFile = "tiles.png";
    // Filenames of the sprite images:
    String[] spriteFiles = {
            "spr_error.png", // Spr no. 0
            // CHARACTERS:
            "chars/spr_char_00.png",
            "chars/spr_char_01.png",
            "chars/spr_char_02.png",
            "chars/spr_char_03.png",
            "chars/spr_char_04.png",
            "chars/spr_char_05.png",
            "chars/spr_char_06.png",
            "chars/spr_char_07.png",
            "chars/spr_char_08.png",
            "chars/spr_char_09.png", // Spr no. 10
            "chars/spr_char_10.png",
            "chars/spr_char_11.png",
            "chars/spr_char_12.png",
            "chars/spr_char_13.png",
            "chars/spr_char_14.png",
            "chars/spr_char_15.png",
            "chars/spr_char_16.png",
            "chars/spr_char_17.png",
            "chars/spr_char_18.png",
            "chars/spr_char_19.png", // Spr no. 20
            "chars/spr_char_20.png",
            "chars/spr_char_21.png",
            "chars/spr_char_22.png",
            "chars/spr_char_23.png",
            "chars/spr_char_24.png",
            "chars/spr_char_25.png",
            "chars/spr_char_26.png",
            "chars/spr_char_27.png",
            "chars/spr_char_28.png",
            "chars/spr_char_29.png", // Spr no. 30
            "chars/spr_char_30.png",
            "chars/spr_char_31.png",
            "chars/spr_char_32.png",
            "chars/spr_char_33.png",
            "chars/spr_char_34.png",
            "chars/spr_char_35.png",
            "chars/spr_char_36.png",
            "chars/spr_char_37.png",
            "chars/spr_char_38.png",
            "chars/spr_char_39.png", // Spr no. 40
            "chars/spr_char_40.png",
            "chars/spr_char_41.png",
            "chars/spr_char_42.png",
            "chars/spr_char_43.png",
            "chars/spr_char_44.png",
            "chars/spr_char_45.png",
            "chars/spr_char_46.png",
            "chars/spr_char_47.png",
            "chars/spr_char_48.png",
            "chars/spr_char_49.png", // Spr no. 50
            "chars/spr_char_50.png",
            "chars/spr_char_51.png",
            "chars/spr_char_52.png",
            "chars/spr_char_53.png",
            "chars/spr_char_54.png",
            "chars/spr_char_55.png",
            "chars/spr_char_56.png",
            "chars/spr_char_57.png",
            "chars/spr_char_58.png",
            "chars/spr_char_59.png", // Spr no. 60
            "chars/spr_char_60.png",
            "chars/spr_char_61.png",
            "chars/spr_char_62.png",
            "chars/spr_char_63.png",
            "chars/spr_char_64.png",
            "chars/spr_char_65.png",
            "chars/spr_char_66.png",
            "chars/spr_char_67.png",
            "chars/spr_char_68.png",
            "chars/spr_char_69.png", // Spr no. 70
            "chars/spr_char_70.png",
            "chars/spr_char_71.png",
            "chars/spr_char_72.png",
            "chars/spr_char_73.png",
            "chars/spr_char_74.png",
            "chars/spr_char_75.png",
            "chars/spr_char_76.png",
            "chars/spr_char_77.png",
            "chars/spr_char_78.png",
            "chars/spr_char_79.png", // Spr no. 80
            "chars/spr_char_80.png",
            "chars/spr_char_81.png",
            "chars/spr_char_82.png",
            "chars/spr_char_83.png",
            "chars/spr_char_84.png",
            "chars/spr_char_85.png",
            "chars/spr_char_86.png",
            "chars/spr_char_87.png",
            "chars/spr_char_88.png",
            "chars/spr_char_89.png", // Spr no. 90
            "chars/spr_char_90.png",
            "chars/spr_char_91.png",
            "chars/spr_char_92.png",
            "chars/spr_char_93.png",
            "chars/spr_char_94.png",
            // DEEJAY:
            "deejay/spr_deejay_idle_l.png",
            "deejay/spr_deejay_walk_1_l.png",
            "deejay/spr_deejay_walk_2_l.png",
            "deejay/spr_deejay_walk_3_l.png",
            "deejay/spr_deejay_gun_l.png", // Spr no. 100
            "deejay/spr_deejay_idle_r.png",
            "deejay/spr_deejay_walk_1_r.png",
            "deejay/spr_deejay_walk_2_r.png",
            "deejay/spr_deejay_walk_3_r.png",
            "deejay/spr_deejay_gun_r.png",
            // ENEMY - SLIME:
            "enemies/slime/spr_slime_1.png",
            "enemies/slime/spr_slime_2.png",
            "enemies/slime/spr_slime_3.png",
            "enemies/slime/spr_slime_4.png",
            "enemies/slime/spr_slime_5.png", // Spr no. 110
            "enemies/slime/spr_slime_6.png",
            "enemies/slime/spr_slime_7.png",
            "enemies/slime/spr_slime_8.png",
            "enemies/slime/spr_slime_9.png",
            "enemies/slime/spr_slime_10.png",
            "enemies/slime/spr_slime_11.png",
            "enemies/slime/spr_slime_12.png",
            // NPCs:
            "npcs/spr_npc_1_idle_1.png",
            "npcs/spr_npc_1_idle_2.png",
            "npcs/spr_npc_1_happy_1.png", // Spr no. 120
            "npcs/spr_npc_1_happy_2.png",
            "npcs/spr_npc_1_mad_1.png",
            "npcs/spr_npc_1_mad_2.png",
            "npcs/spr_npc_2_idle_1.png",
            "npcs/spr_npc_2_idle_2.png",
            "npcs/spr_npc_2_happy_1.png",
            "npcs/spr_npc_2_happy_2.png",
            "npcs/spr_npc_2_mad_1.png",
            "npcs/spr_npc_2_mad_2.png",
            "npcs/spr_npc_3_idle_1.png", // Spr no. 130
            "npcs/spr_npc_3_idle_2.png",
            "npcs/spr_npc_3_happy_1.png",
            "npcs/spr_npc_3_happy_2.png",
            "npcs/spr_npc_3_mad_1.png",
            "npcs/spr_npc_3_mad_2.png",
            // MIXER GUI:
            "gui/spr_background.png",
            "gui/spr_checkbox_01.png",
            "gui/spr_checkbox_02.png",
            "gui/spr_gauge_01.png",
            "gui/spr_gauge_02.png", // Spr no. 140
            "gui/spr_slider.png",
            "gui/spr_window.png",
            // DOOR - TREE:
            "doors/spr_tree_1.png",
            "doors/spr_tree_2.png",
            // CURSOR:
            "gui/spr_cursor_1.png",
            "gui/spr_cursor_2.png",
            // DOOR - GATE:
            "doors/spr_gate_1.png",
            "doors/spr_gate_2.png",
            // DIALOGUE CONTEXT GUI:
            "gui/spr_dialogue_next.png",
            "gui/spr_dialogue_last.png", // Spr no. 150
            // NPC OLD MAN:
            "npcs/spr_npc_4_idle_1.png",
            "npcs/spr_npc_4_idle_2.png"
    };
    // Filenames of the sound effects:
    String[] soundFiles = {};
}
