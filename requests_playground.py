import requests
response = requests.get('https://api.github.com')

# Check status code
## print(response.status_code)

# Check content
## print(response.content)

# print(list(response.__dict__.keys()))
## ['_content',
## '_content_consumed',
## '_next',
## 'status_code',
## 'headers',
## 'raw',
## 'url',
## 'encoding',
## 'history',
## 'reason',
## 'cookies',
## 'elapsed',
## 'request',
## 'connection']

def getJSONData(apiURL):
    response = requests.get(apiURL, verify = True)
    response.raise_for_status()
    return response.json()

gov_url1 = 'https://api.data.gov.sg/v1/environment/psi'
gov_url2 = 'https://api.data.gov.sg/v1/environment/air-temperature'
gov_url3 = 'https://api.data.gov.sg/v1/transport/taxi-availability'

gov_data1 = getJSONData(gov_url1)
gov_data2 = getJSONData(gov_url2)
gov_data3 = getJSONData(gov_url3)
## print(gov_data,end='\n\n')

import pandas as pd
from pandas import json_normalize
import plotly.express as px
import matplotlib.pyplot as plt

# pd.set_option('display.max_columns',None)
# pd.set_option('display.max_rows',None)
# pd.set_option('display.width',None)

def psi():
    print(list(gov_data1.keys()),end='\n\n')
    v1 = gov_data1['region_metadata']
    pd.set_option('display.max_columns',None)
    df1 = pd.DataFrame.from_dict(json_normalize(v1))
    print(df1,end='\n\n')

def air_temp():
    print(list(gov_data2.keys()),end='\n\n')
    v2 = gov_data2['metadata']
    pd.set_option('display.max_columns',None)
    df2 = pd.DataFrame.from_dict(json_normalize(v2['stations']), orient = "columns")
    print(df2,end='\n\n')

def taxi():
    print(list(gov_data3.keys()),end='\n\n')
    v3 = gov_data3['features'][0]
    pd.set_option('display.max_columns',None)
    df3 = pd.DataFrame(v3['geometry']['coordinates'],columns=['latitude','longitude'])
    print(df3,end='\n\n')

    data = v3['geometry']['coordinates']
    
    fig, ax = plt.subplots(figsize=(10,5))
    
    ax.scatter(tuple(map(lambda x:x[0],data)),
               tuple(map(lambda x:x[1],data)),
               s=8,
               c='red')
    ax.set_title("Taxi Availability (17 March 2021)")
    fig.show()
    # plot = px.scatter(df3,'longitude','latitude')
    # plot.show()

# psi()
# air_temp()
taxi()