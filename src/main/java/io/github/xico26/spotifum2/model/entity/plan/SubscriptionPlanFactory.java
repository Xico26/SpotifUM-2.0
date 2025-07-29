package io.github.xico26.spotifum2.model.entity.plan;

public class SubscriptionPlanFactory {
    public static ISubscriptionPlan createPlan(String subscriptionPlan) {
        switch (subscriptionPlan) {
            case "FREE": return new FreePlan();
            case "PLUS": return new PlusPlan();
            case "PREMIUM": return new PremiumPlan();
            default: throw new IllegalArgumentException("Invalid subscription plan: " + subscriptionPlan);
        }
    }
}
