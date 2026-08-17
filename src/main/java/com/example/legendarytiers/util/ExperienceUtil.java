package com.example.legendarytiers.util;

public final class ExperienceUtil {

    private ExperienceUtil() {
    }

    /**
     * Базовый опыт, необходимый для первого уровня (1 lvl -> 2 lvl).
     */
    public static final int BASE_EXPERIENCE = 100;

    /**
     * Возвращает количество опыта, необходимое для перехода С указанного уровня НА следующий.
     * (Например: level 1 -> нужно 100 XP, level 2 -> 105 XP, level 26 -> 335 XP)
     */
    public static int getExpRequiredForLevel(int level) {
        if (level <= 1) {
            return BASE_EXPERIENCE;
        }

        double requiredExp = BASE_EXPERIENCE;

        for (int lvl = 2; lvl <= level; lvl++) {
            if (lvl <= 25) {
                requiredExp *= 1.05; // +5% до 25 уровня включительно
            } else {
                requiredExp *= 1.04; // +4% с 26 уровня
            }
        }

        // Округление в меньшую сторону до ближайшего кратного 5
        int roundedExp = (int) (Math.floor(requiredExp / 5.0) * 5);

        return Math.max(BASE_EXPERIENCE, roundedExp);
    }

    /**
     * Возвращает суммарное количество опыта, необходимое для достижения уровня c нуля.
     */
    public static int getExperienceForLevel(int level) {
        if (level <= 1) {
            return 0;
        }

        int total = 0;
        for (int l = 1; l < level; l++) {
            total += getExpRequiredForLevel(l);
        }
        return total;
    }

    /**
     * Возвращает текущий уровень по общему накопленному опыту.
     */
    public static int getLevel(int totalExperience) {
        if (totalExperience <= 0) {
            return 1;
        }

        int level = 1;
        int expLeft = totalExperience;

        while (true) {
            int req = getExpRequiredForLevel(level);
            if (expLeft >= req) {
                expLeft -= req;
                level++;
            } else {
                break;
            }
        }

        return level;
    }

    /**
     * Возвращает опыт, накопленный внутри ТЕКУЩЕГО уровня.
     */
    public static int getCurrentLevelExperience(int totalExperience) {
        int level = getLevel(totalExperience);
        int totalForCurrentLevel = getExperienceForLevel(level);
        return Math.max(0, totalExperience - totalForCurrentLevel);
    }

    /**
     * Возвращает количество опыта, требуемое для полного прохождения ТЕКУЩЕГО уровня
     * (по общему опыту предмета).
     */
    public static int getExperienceToNextLevelForExp(int totalExperience) {
        int level = getLevel(totalExperience);
        return getExpRequiredForLevel(level);
    }

    /**
     * Возвращает количество опыта, необходимое для перехода с указанного уровня.
     */
    public static int getExperienceToNextLevel(int level) {
        return getExpRequiredForLevel(level);
    }

    /**
     * Прогресс текущего уровня (значение от 0.0f до 1.0f для полоски прогресса в HUD/тултипе).
     */
    public static float getProgress(int totalExperience) {
        int level = getLevel(totalExperience);
        int currentExp = getCurrentLevelExperience(totalExperience);
        int requiredExp = getExpRequiredForLevel(level);

        return Math.min(1.0f, currentExp / (float) requiredExp);
    }

    /**
     * Множитель для атрибутов от уровня.
     */
    public static double getMultiplier(int experience) {
        int level = getLevel(experience);
        return 1.0 + (level - 1) * 0.01;
    }
}