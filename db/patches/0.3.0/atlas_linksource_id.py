import mysql.connector

connection = mysql.connector.connect(host="localhost", user="kids_cademy", passwd="kids_cademy", database="atlas")
cursor = connection.cursor()

cursor.execute("SELECT id FROM atlascollection")
for collection in cursor.fetchall():
    collectionId = collection[0]

    cursor.execute("SELECT id,name FROM atlasobject WHERE collection_id=%d" % collectionId)
    for atlas_object in cursor.fetchall():
        print("-- processing object", atlas_object[1])

        cursor.execute("SELECT id,domain FROM atlasobject_links WHERE atlasobject_id=%d" % atlas_object[0])
        for link in cursor.fetchall():
            link_id = link[0]
            link_domain = link[1]

            cursor.execute("SELECT l.id FROM linksource l JOIN externalsource e ON l.externalsource_id=e.id WHERE l.atlascollection_id=%d AND e.domain='%s'" % (collectionId, link_domain))
            linksource_id = cursor.fetchone()[0]

            print("UPDATE `atlas`.`atlasobject_links` SET `linksource_id`=%d WHERE `id`=%d;" % (linksource_id, link_id))
