import csv

with open("featuremeta.csv") as csv_file:
    csv_reader = csv.reader(csv_file)
    next(csv_reader)    # skip header
    for row in csv_reader:
        print("UPDATE `atlas`.`featuremeta` SET display='%s' WHERE name='%s';" % (row[1], row[0]))
