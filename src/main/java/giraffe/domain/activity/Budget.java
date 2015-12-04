package giraffe.domain.activity;

import giraffe.domain.GiraffeEntity;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@NodeEntity
final public class Budget extends GiraffeEntity {

    private double quantity;

    private String currency;


    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return Double.compare(budget.quantity, quantity) == 0 &&
                Objects.equals(currency, budget.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, currency);
    }
}
