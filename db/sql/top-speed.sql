SELECT 
	c.display AS collection,
	o.display AS object,
    m.display AS feature,
    ROUND(CASE WHEN f.maximum > f.value THEN f.maximum ELSE f.value END * 3.6) AS speed
    
FROM atlascollection c
JOIN atlasobject o ON c.id=o.collection_id
JOIN feature f on o.id=f.atlasobject_id
JOIN featuremeta m on f.meta_id=m.id

WHERE m.quantity='SPEED'

ORDER by speed DESC;