package services.intefraces;

public interface QueryConditions {
    String allPostCond = "from Post p where p.moderationStatus = 'ACCEPTED' and p.isActive = '1' and p.time <= :today ";
}
