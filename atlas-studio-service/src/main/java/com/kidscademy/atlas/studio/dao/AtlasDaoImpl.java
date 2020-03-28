package com.kidscademy.atlas.studio.dao;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.kidscademy.atlas.studio.export.ExportItem;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasImages;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasLinks;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.AtlasRelated;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkMeta;
import com.kidscademy.atlas.studio.model.Release;
import com.kidscademy.atlas.studio.model.ReleaseChild;
import com.kidscademy.atlas.studio.model.ReleaseItem;
import com.kidscademy.atlas.studio.model.ReleaseParent;
import com.kidscademy.atlas.studio.model.SearchFilter;
import com.kidscademy.atlas.studio.model.Taxon;

import js.lang.BugError;
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
    public void removeAtlasCollection(int collectionId) {
	AtlasCollection collection = em.find(AtlasCollection.class, collectionId);
	if (collection != null) {
	    em.remove(collection);
	}
    }

    @Override
    public int getCollectionSize(int collectionId) {
	Number count = (Number) em.createQuery("select count(o) from AtlasObject o where o.collection.id=:id")
		.setParameter("id", collectionId).getSingleResult();
	return count.intValue();
    }

    @Override
    public int getReleaseSize(int releaseId) {
	Number count = (Number) em.createQuery("select count(r.children) from ReleaseParent r where r.id=:id")
		.setParameter("id", releaseId).getSingleResult();
	return count.intValue();
    }

    @Override
    public AtlasCollection getCollectionById(int collectionId) {
	return em.find(AtlasCollection.class, collectionId);
    }

    @Override
    public boolean uniqueCollectionName(AtlasCollection collection) {
	List<AtlasCollection> result = em
		.createQuery("select c from AtlasCollection c where c.id!=:id and c.name=:name", AtlasCollection.class)
		.setParameter("id", collection.getId()).setParameter("name", collection.getName()).getResultList();
	if (result.size() > 1) {
	    throw new BugError("Database inconsistency. Multiple collections with the same name.");
	}
	return result.size() == 0;
    }

    @Override
    public List<AtlasCollection> getCollections() {
	return em.createQuery("select c from AtlasCollection c order by c.display", AtlasCollection.class)
		.getResultList();
    }

    @Override
    public List<AtlasItem> getCollectionItems(SearchFilter filter, int collectionId) {
	return getCollectionItems(AtlasItem.class, filter, collectionId);
    }

    @Override
    public List<AtlasImages> getCollectionImages(SearchFilter filter, int collectionId) {
	return getCollectionItems(AtlasImages.class, filter, collectionId);
    }

    @Override
    public List<AtlasRelated> getCollectionRelated(SearchFilter filter, int collectionId) {
	return getCollectionItems(AtlasRelated.class, filter, collectionId);
    }

    @Override
    public List<AtlasLinks> getCollectionLinks(SearchFilter filter, int collectionId) {
	return getCollectionItems(AtlasLinks.class, filter, collectionId);
    }

    private <T> List<T> getCollectionItems(Class<T> type, SearchFilter filter, int collectionId) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select i from ");
	queryBuilder.append(type.getSimpleName());
	queryBuilder.append(" i where i.collection.id=?1 ");
	if (!filter.criterion("state").is(AtlasObject.State.NONE)) {
	    if (filter.criterion("negate").is(true)) {
		queryBuilder.append("and i.state!=?2 ");
	    } else {
		queryBuilder.append("and i.state=?2 ");
	    }
	}
	queryBuilder.append("order by i.display");

	TypedQuery<T> query = em.createQuery(queryBuilder.toString(), type);
	query.setParameter(1, collectionId);
	if (!filter.criterion("state").is(AtlasObject.State.NONE)) {
	    query.setParameter(2, filter.get("state", AtlasObject.State.class));
	}
	return query.getResultList();
    }

    @Override
    public List<AtlasItem> getCollectionItemsByTaxon(int collectionId, Taxon taxon) {
	List<Integer> ids = em.createQuery(
		"select o.id from AtlasObject o join o.taxonomy t where o.collection.id=:collectionId and t.name=:taxonName and t.value=:taxonValue",
		Integer.class).setParameter("collectionId", collectionId).setParameter("taxonName", taxon.getName())
		.setParameter("taxonValue", taxon.getValue()).getResultList();
	if (ids.isEmpty()) {
	    return Collections.emptyList();
	}
	return em.createQuery("select i from AtlasItem i where i.id in :ids", AtlasItem.class).setParameter("ids", ids)
		.getResultList();
    }

    @Override
    public List<ExportItem> getCollectionExportItems(int collectionId) {
	return em
		.createQuery("select i from ExportItem i where i.collection.id=?1 order by i.display", ExportItem.class)
		.setParameter(1, collectionId).getResultList();
    }

    @Override
    public List<ExportItem> getCollectionExportItemsByState(int collectionId, AtlasObject.State state) {
	return em.createQuery("select i from ExportItem i where i.collection.id=?1 and i.state=?2 order by i.display",
		ExportItem.class).setParameter(1, collectionId).setParameter(2, state).getResultList();
    }

    @Override
    public List<ExportItem> getAllExportItems() {
	return em.createQuery("select i from ExportItem i where i.state=?1 order by i.name", ExportItem.class)
		.setParameter(1, AtlasObject.State.PUBLISHED).getResultList();
    }

    @Override
    @Mutable
    public void saveAtlasCollection(AtlasCollection collection) {
	if (collection.getId() == 0) {
	    em.persist(collection);
	} else {
	    em.merge(collection).postMerge(collection);
	}
    }

    @Override
    @Mutable
    public void saveAtlasObject(AtlasObject object) {
	// atlas object features are @Emebddable mapped with @@ElementCollection
	// also mapping has @OrderColumn annotation meaning that implementation is
	// responsible for updating order column from database
	//
	// when update features list content, especially when reorder, current
	// eclipselink implementation generates a query that compare
	// for equality all persisted Feature class properties
	// the problem is when compare for null value:
	// generated SQL contains (maximum = ?) replaced with (maximum = NULL) which is
	// not valid SQL; it should be (maxim IS NULL)
	//
	// here is and example for generated SQL
	// UPDATE AtlasObject_FEATURES SET features_ORDER = ? WHERE ((AtlasObject_ID =
	// ?) AND ((VALUE = ?) AND ((QUANTITY = ?) AND ((NAME = ?) AND (MAXIMUM = ?)))))
	//
	// the problem is observed only on feature maximum value which is Double boxing
	// class that should be null if not used
	//
	// quick workaround is to remove all current features and reload!
	// see
	// com.kidscademy.atlas.studio.it.AtlasDaoWriteTest#saveAtlasObject_FeaturesReorder()
	//
	em.createNativeQuery("DELETE FROM atlasobject_features WHERE atlasobject_id=" + object.getId()).executeUpdate();

	object.setLastUpdated(new Timestamp(System.currentTimeMillis()));
	if (object.getId() == 0) {
	    em.persist(object);
	} else {
	    em.merge(object).postMerge(object);
	}
    }

    @Override
    @Mutable
    public void removeAtlasObject(int objectId) {
	AtlasObject object = em.find(AtlasObject.class, objectId);
	if (object != null) {
	    em.remove(object);
	}
    }

    @Override
    @Mutable
    public AtlasItem moveAtlasObject(int objectId, int collectionId) {
	AtlasItem object = em.find(AtlasItem.class, objectId);
	if (object != null) {
	    AtlasCollection collection = em.find(AtlasCollection.class, collectionId);
	    if (collection != null) {
		object.setCollection(collection);
		return object;
	    }
	}
	return null;
    }

    @Override
    public AtlasItem getAtlasItem(int objectId) {
	return em.find(AtlasItem.class, objectId);
    }

    @Override
    public AtlasObject getAtlasObject(int objectId) {
	return em.find(AtlasObject.class, objectId);
    }

    @Override
    public AtlasObject getObjectByName(String collectionName, String name) {
	return em
		.createQuery("select o from AtlasObject o where o.collection.name=:collectionName and o.name=:name",
			AtlasObject.class)
		.setParameter("collectionName", collectionName).setParameter("name", name).getSingleResult();
    }

    @Override
    public String getAtlasObjectName(int objectId) {
	return em.createQuery("select o.name from AtlasObject o where o.id=:id", String.class)
		.setParameter("id", objectId).getSingleResult();
    }

    @Override
    public List<Link> getObjectLinks(AtlasItem object) {
	return em
		.createQuery(
			"select o.links from AtlasObject o where o.collection.name=:collectionName and o.name=:name",
			Link.class)
		.setParameter("collectionName", object.getRepositoryName()).setParameter("name", object.getName())
		.getResultList();
    }

    @Override
    public List<LinkMeta> getLinksMeta() {
	return em.createQuery("select l from LinkMeta l order by l.display", LinkMeta.class).getResultList();
    }

    @Override
    public LinkMeta getLinkMetaById(int linkMetaId) {
	return em.find(LinkMeta.class, linkMetaId);
    }

    @Override
    public LinkMeta getLinkMetaByDomain(String domain) {
	List<LinkMeta> result = em.createQuery("select l from LinkMeta l where l.domain=:domain", LinkMeta.class)
		.setParameter("domain", domain).getResultList();
	if (result.size() > 1) {
	    throw new BugError("Database inconsistency. Not unique link meta domain.");
	}
	return result.size() == 1 ? result.get(0) : null;
    }

    @Override
    @Mutable
    public void saveLinkMeta(LinkMeta linkMeta) {
	if (linkMeta.getId() == 0) {
	    em.persist(linkMeta);
	} else {
	    em.merge(linkMeta).postMerge(linkMeta);
	}
    }

    @Override
    @Mutable
    public void removeLinkMeta(int linkMetaId) {
	LinkMeta linkMeta = em.find(LinkMeta.class, linkMetaId);
	if (linkMeta != null) {
	    em.remove(linkMeta);
	}
    }

    @Override
    public List<FeatureMeta> getFeaturesMeta() {
	return em.createQuery("select f from FeatureMeta f order by f.name", FeatureMeta.class).getResultList();
    }

    @Override
    @Mutable
    public void saveFeatureMeta(FeatureMeta featureMeta) {
	if (featureMeta.getId() == 0) {
	    em.persist(featureMeta);
	} else {
	    em.merge(featureMeta);
	}
    }

    @Override
    @Mutable
    public void removeFeatureMeta(int featureMetaId) {
	FeatureMeta featureMeta = em.find(FeatureMeta.class, featureMetaId);
	if (featureMeta != null) {
	    em.remove(featureMeta);
	}
    }

    @Override
    public List<AtlasItem> getRelatedAtlasObjects(int collectionId, List<String> relatedNames) {
	List<AtlasItem> items = new ArrayList<>();
	String jpql = "select i from AtlasItem i where i.collection.id=?1 and i.name=?2";
	for (String relatedName : relatedNames) {
	    List<AtlasItem> result = em.createQuery(jpql, AtlasItem.class).setParameter(1, collectionId)
		    .setParameter(2, relatedName).getResultList();
	    if (!result.isEmpty()) {
		items.add(result.get(0));
	    }
	}
	return items;
    }

    @Override
    @Mutable
    public void resetObjectSample(int objectId) {
	String jpql = "update AtlasObject o set o.sampleTitle=null,o.sampleName=null,o.waveformName=null where o.id=:id";
	em.createQuery(jpql).setParameter("id", objectId).executeUpdate();
    }

    @Override
    @Mutable
    public void removeObjectImage(int objectId, Image image) {
	AtlasObject object = (AtlasObject) em.createQuery("select o from AtlasObject o where o.id=:id")
		.setParameter("id", objectId).getSingleResult();
	// object entity is managed in this scope and altering it will be synchronized
	// on database
	object.getImages().remove(image.getImageKey());
    }

    @Override
    @Mutable
    public void addObjectImage(int objectId, Image image) {
	AtlasObject object = (AtlasObject) em.createQuery("select o from AtlasObject o where o.id=:id")
		.setParameter("id", objectId).getSingleResult();
	// object entity is managed in this scope and altering it will be synchronized
	// on database
	object.getImages().put(image.getImageKey(), image);
    }

    @Override
    public Image getImageByKey(int objectId, String imageKey) {
	String jpql = "select i from AtlasObject o join o.images i where o.id=:id and KEY(i)=:key";
	List<Image> images = em.createQuery(jpql, Image.class).setParameter("id", objectId)
		.setParameter("key", imageKey).getResultList();
	return images.isEmpty() ? null : images.get(0);
    }

    @Override
    public AtlasObject getNextAtlasObject(int objectId) {
	String jpql = "select o from AtlasObject o where o.id>?1 order by o.id asc";
	List<AtlasObject> objects = em.createQuery(jpql, AtlasObject.class).setParameter(1, objectId).setMaxResults(1)
		.getResultList();
	return objects.isEmpty() ? null : objects.get(0);
    }

    @Override
    public List<AtlasItem> getAtlasItems(List<Integer> objectIds) {
	return em.createQuery("select i from AtlasItem i where i.id in :ids", AtlasItem.class)
		.setParameter("ids", objectIds).getResultList();
    }

    @Override
    public List<ReleaseItem> getReleases() {
	return em.createQuery("select r from ReleaseItem r order by r.display", ReleaseItem.class).getResultList();
    }

    @Override
    public Release getReleaseById(int releaseId) {
	return em.find(Release.class, releaseId);
    }

    @Override
    public ReleaseParent getReleaseParentById(int releaseId) {
	return em.find(ReleaseParent.class, releaseId);
    }

    @Override
    @Mutable
    public void saveRelease(Release releaase) throws IOException {
	releaase.setLastUpdated(new Timestamp(System.currentTimeMillis()));
	if (releaase.getId() == 0) {
	    em.persist(releaase);
	} else {
	    em.merge(releaase).postMerge(releaase);
	}
    }

    @Override
    @Mutable
    public void removeRelease(int releaseId) {
	Release release = em.find(Release.class, releaseId);
	if (release != null) {
	    em.remove(release);
	}
    }

    @Override
    @Mutable
    public void addReleaseChild(int releaseId, int childId) {
	ReleaseParent release = em.find(ReleaseParent.class, releaseId);
	ReleaseChild child = em.find(ReleaseChild.class, childId);
	release.getChildren().add(child);
    }

    @Override
    @Mutable
    public void addReleaseChildren(int releaseId, List<Integer> childIds) {
	ReleaseParent release = em.find(ReleaseParent.class, releaseId);
	List<ReleaseChild> children = em
		.createQuery("select c from ReleaseChild c where c.id in :ids", ReleaseChild.class)
		.setParameter("ids", childIds).getResultList();
	release.getChildren().addAll(children);
    }

    @Override
    @Mutable
    public void removeReleaseChild(int releaseId, int childId) {
	ReleaseParent release = em.find(ReleaseParent.class, releaseId);
	ReleaseChild child = em.find(ReleaseChild.class, childId);
	release.getChildren().remove(child);
    }

    @Override
    public List<AtlasItem> getReleaseItems(int releaseId) {
	ReleaseParent release = em.find(ReleaseParent.class, releaseId);
	List<Integer> ids = new ArrayList<>();
	for (ReleaseChild child : release.getChildren()) {
	    ids.add(child.getId());
	}
	if (ids.isEmpty()) {
	    return Collections.emptyList();
	}
	return em.createQuery("select i from AtlasItem i where i.id in :ids", AtlasItem.class).setParameter("ids", ids)
		.getResultList();
    }
}
