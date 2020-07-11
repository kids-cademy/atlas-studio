import mysql.connector

connection = mysql.connector.connect(host="localhost", user="kids_cademy", passwd="kids_cademy", database="atlas")
cursor = connection.cursor()

cursor.execute("SELECT id,name FROM atlascollection ORDER BY name ASC")
collections = cursor.fetchall()

for collection in collections:
    print("-- processing collection", collection[1])
    collectionId = collection[0]

    cursor.execute("SELECT DISTINCT(l.domain) FROM atlasobject o JOIN atlasobject_links l ON o.id=l.atlasobject_id WHERE o.collection_id=%d ORDER BY l.domain" % collectionId)
    domains = cursor.fetchall()

    for order, domain in enumerate(domains):
        cursor.execute("SELECT id,apis FROM externalsource WHERE domain='%s'" % domain)
        for row in cursor:
            print('INSERT INTO `atlas`.`linksource` (`atlascollection_id`, `linksources_order`, `externalsource_id`, `apis`) VALUES(%d,%d,%d,"%s");' % (collectionId, order, row[0], row[1]))
