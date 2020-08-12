import mysql.connector

connection = mysql.connector.connect(host="localhost", user="kids_cademy", passwd="kids_cademy", database="atlas")
cursor = connection.cursor()

cursor.execute("SELECT atlasobject_id AS id,GROUP_CONCAT(aliases) AS aliases FROM atlasobject_aliases GROUP BY atlasobject_id")
object_aliases = cursor.fetchall()

for aliases in object_aliases:
    print(aliases)
    cursor.execute("UPDATE atlasobject SET aliases='%s' WHERE id=%d" % (connection.converter.escape(aliases[1]), aliases[0]))
    connection.commit()

