package com.whiskels.notifier.util;

import com.whiskels.notifier.model.CustomerReceivable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomerReceivableUtil {
    public static final String CATEGORY_REVENUE = "Revenue";

    public static final Predicate<CustomerReceivable> IS_REVENUE =
            c -> c.getCategory().equalsIgnoreCase(CATEGORY_REVENUE);

    public static final Comparator<CustomerReceivable> AMOUNT_COMPARATOR = Comparator.comparing(CustomerReceivable::getAmountRub)
            .thenComparing(CustomerReceivable::getContractor);
}
