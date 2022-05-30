import pandas as pd
import numpy as np
import json
import overpy

api = overpy.Overpass()

def count_results(query, bbox):
    return {'A': len(api.query(f'node["highway"="bus_stop"]{bbox};out;').nodes),
            'B': len(api.query(f'node["public_transport"="platform"]["bus"="yes"]{bbox};out;').nodes)}

with open('country-capitals.json', 'r') as f: #source: http://techslides.com/demos/country-capitals.json
    countries = json.load(f)
cities = {d['CapitalName']:(float(d['CapitalLatitude']), float(d['CapitalLongitude'])) for d in countries if d['ContinentName']=='Europe'}
bboxes = {k: (v[0]-0.1,v[1]-0.1,v[0]+0.1,v[1]+0.1) for k, v in cities.items()}
counts = {k: count_results(bbox) for k, bbox in bboxes.items()}
df = pd.DataFrame(counts).T
df['A-B'] = df.A - df.B




https://gis.stackexchange.com/questions/360338/how-to-most-consistently-get-all-bus-stops-from-overpass