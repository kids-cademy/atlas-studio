package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;

@Entity
@IdClass(TranslationKey.class)
public class Translation {
    @Id
    private Discriminator discriminator;
    @Id
    private int objectId;
    @Id
    private String language;

    private String text;

    public void setText(String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }
}
