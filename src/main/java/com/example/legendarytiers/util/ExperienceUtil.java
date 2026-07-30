package com.example.legendarytiers.util;

public final class ExperienceUtil {

    private ExperienceUtil() {
    }

    /**
     * Опыт, необходимый для одного уровня.
     */
    public static final int EXPERIENCE_PER_LEVEL = 100;


    /**
     * Возвращает текущий уровень.
     */
    public static int getLevel(int experience) {

        return Math.max(0, experience / EXPERIENCE_PER_LEVEL);

    }


    /**
     * Возвращает опыт внутри текущего уровня.
     */
    public static int getCurrentLevelExperience(int experience) {

        return Math.max(0, experience % EXPERIENCE_PER_LEVEL);

    }


    /**
     * Возвращает количество опыта,
     * необходимое для следующего уровня.
     */
    public static int getExperienceToNextLevel() {

        return EXPERIENCE_PER_LEVEL;

    }


    /**
     * Возвращает прогресс уровня.
     * Значение от 0 до 1.
     */
    public static float getProgress(int experience) {

        return getCurrentLevelExperience(experience) / (float) EXPERIENCE_PER_LEVEL;

    }

}