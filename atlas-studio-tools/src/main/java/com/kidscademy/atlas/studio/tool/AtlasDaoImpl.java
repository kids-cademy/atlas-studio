package com.kidscademy.atlas.studio.tool;

import java.util.List;

import javax.persistence.EntityManager;

import js.transaction.Immutable;
import js.transaction.Mutable;
import js.transaction.Transactional;

@Transactional
@Immutable
public class AtlasDaoImpl implements AtlasDao {
    private final EntityManager em;

    public AtlasDaoImpl(EntityManager em) {
	this.em = em;
    }

    @Override
    @Mutable
    public void saveObject(AtlasObject object) {
	System.out.println(object);
//	if (object.getId() == 0) {
//	    em.persist(object);
//	} else {
//	    em.merge(object);
//	}
    }

    @Override
    public List<AtlasObject> findPublishedObjects(String string) {
	// TODO Auto-generated method stub
	return null;
    }

}
