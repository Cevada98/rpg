package be.cevada.combat;

import be.cevada.data.GameRandom;
import be.cevada.models.Enemy.BehaviorType;

import java.util.Random;

public class CombatText {

    private static final Random RNG = GameRandom.get();

    private static final String[] ATTACK_BEAST    = { "You slash the beast with your blade!", "You strike it across the flank!", "You swing hard at it!" };
    private static final String[] ATTACK_UNDEAD   = { "You crack its ribcage!", "You smash it with full force!", "You strike — bones splinter!" };
    private static final String[] ATTACK_HUMANOID = { "You slash it across the arm!", "You land a solid blow!", "You drive your weapon in!" };
    private static final String[] ATTACK_VERMIN   = { "You stomp on it!", "You swat it aside!", "You strike it hard!" };

    private static final String[] ENEMY_ATTACK_BEAST    = { "It lunges and bites you!", "It claws at you savagely!", "It snaps its jaws at your arm!" };
    private static final String[] ENEMY_ATTACK_UNDEAD   = { "It rattles and swipes at you!", "It drives a bony fist into you!", "It slashes with a cracked blade!" };
    private static final String[] ENEMY_ATTACK_HUMANOID = { "It stabs you viciously!", "It swings a heavy weapon at you!", "It charges and headbutts you!" };
    private static final String[] ENEMY_ATTACK_VERMIN   = { "It gnaws at your leg!", "It leaps and scratches you!", "It hisses and bites!" };

    private static final String[] REACTION_BEAST    = { "It snarls in pain!", "It yelps!", "It snaps back at you!" };
    private static final String[] REACTION_UNDEAD   = { "It rattles!", "It clacks its teeth!", "Bones scatter from the impact!" };
    private static final String[] REACTION_HUMANOID = { "It grunts angrily!", "It spits and glares at you!", "It staggers back!" };
    private static final String[] REACTION_VERMIN   = { "It squeaks!", "It recoils!", "It hisses!" };

    private static final String[] SPECIAL_BEAST    = { "You unleash a powerful arc slash!" };
    private static final String[] SPECIAL_UNDEAD   = { "You shatter it with a charged strike!" };
    private static final String[] SPECIAL_HUMANOID = { "You overpower it with a fierce special attack!" };
    private static final String[] SPECIAL_VERMIN   = { "You bring your full force down on it!" };

    private static final String[] ACT_UNDEAD   = { "You tell a bone-chilling joke.", "You mime pulling a bone out of your sleeve.", "You ask if it's \"dead\" tired." };
    private static final String[] ACT_BEAST    = { "You crouch low and hold out your hand.", "You whistle softly.", "You toss a piece of dried meat." };
    private static final String[] ACT_HUMANOID = { "You taunt it by wiggling your ears.", "You point behind it and yell \"FREE GOLD!\"" };
    private static final String[] ACT_VERMIN   = { "You squeak back at it.", "You make big eyes at it. It ignores you." };

    public static String playerAttackText(BehaviorType type) {
        return pick(attackLines(type));
    }

    public static String playerSpecialText(BehaviorType type) {
        return pick(specialLines(type));
    }

    public static String enemyReactionText(BehaviorType type) {
        return pick(reactionLines(type));
    }

    public static String enemyAttackText(BehaviorType type) {
        return pick(enemyAttackLines(type));
    }

    public static String actText(BehaviorType type) {
        return pick(actLines(type));
    }

    private static String pick(String[] arr) {
        return arr[RNG.nextInt(arr.length)];
    }

    private static String[] attackLines(BehaviorType type) {
        return switch (type) {
            case BEAST    -> ATTACK_BEAST;
            case UNDEAD   -> ATTACK_UNDEAD;
            case HUMANOID -> ATTACK_HUMANOID;
            case VERMIN   -> ATTACK_VERMIN;
        };
    }

    private static String[] specialLines(BehaviorType type) {
        return switch (type) {
            case BEAST    -> SPECIAL_BEAST;
            case UNDEAD   -> SPECIAL_UNDEAD;
            case HUMANOID -> SPECIAL_HUMANOID;
            case VERMIN   -> SPECIAL_VERMIN;
        };
    }

    private static String[] reactionLines(BehaviorType type) {
        return switch (type) {
            case BEAST    -> REACTION_BEAST;
            case UNDEAD   -> REACTION_UNDEAD;
            case HUMANOID -> REACTION_HUMANOID;
            case VERMIN   -> REACTION_VERMIN;
        };
    }

    private static String[] enemyAttackLines(BehaviorType type) {
        return switch (type) {
            case BEAST    -> ENEMY_ATTACK_BEAST;
            case UNDEAD   -> ENEMY_ATTACK_UNDEAD;
            case HUMANOID -> ENEMY_ATTACK_HUMANOID;
            case VERMIN   -> ENEMY_ATTACK_VERMIN;
        };
    }

    private static String[] actLines(BehaviorType type) {
        return switch (type) {
            case UNDEAD   -> ACT_UNDEAD;
            case BEAST    -> ACT_BEAST;
            case HUMANOID -> ACT_HUMANOID;
            case VERMIN   -> ACT_VERMIN;
        };
    }
}
