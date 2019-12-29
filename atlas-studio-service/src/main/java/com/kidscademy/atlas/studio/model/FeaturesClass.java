package com.kidscademy.atlas.studio.model;

import java.util.ArrayList;
import java.util.List;

public enum FeaturesClass {
    NONE(none()), BIRD(bird());

    /** A feature without value is used as a template for real feature creation. */
    private final List<Feature> templates;

    private FeaturesClass(List<Feature> templates) {
	this.templates = templates;
    }

    public List<Feature> getTemplates() {
	return templates;
    }

    // --------------------------------------------------------------------------------------------

    private static final List<Feature> none() {
	return new ArrayList<>();
    }

    private static final List<Feature> bird() {
	List<Feature> templates = new ArrayList<>();
	templates.add(new Feature("lifespan", PhysicalQuantity.TIME));
	templates.add(new Feature("lifespan.captivity", PhysicalQuantity.TIME));
	templates.add(new Feature("wingspan", PhysicalQuantity.LENGTH));
	templates.add(new Feature("length", PhysicalQuantity.LENGTH));
	templates.add(new Feature("length.bill", PhysicalQuantity.LENGTH));
	templates.add(new Feature("length.tail", PhysicalQuantity.LENGTH));
	templates.add(new Feature("height", PhysicalQuantity.LENGTH));
	templates.add(new Feature("weight", PhysicalQuantity.MASS));
	templates.add(new Feature("speed", PhysicalQuantity.SPEED));
	templates.add(new Feature("speed.full", PhysicalQuantity.SPEED));
	templates.add(new Feature("speed.gliding", PhysicalQuantity.SPEED));
	templates.add(new Feature("speed.diving", PhysicalQuantity.SPEED));
	return templates;
    }
}
