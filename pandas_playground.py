import pandas as pd
import matplotlib.pyplot as plt

def display(df):
    print(df)
    print()

def title(msg):
    print(msg)
    print('='*30)

def part1():
    cars = {'Brand': ['Honda','Toyota','Ford'],
            'Price': [22000,25000,27000],
            }
    cars_df = pd.DataFrame(cars,
                           columns = cars.keys(),
                           index = ['Car '+str(i+1) for i in range(3)])
    title('First display of cars_df')
    display(cars_df)


    year = [2010,2011,2008]
    cars_df['Year'] = year
    title('Add year to cars_df')
    display(cars_df)


    cars_df.insert(1, 'Model', # index of placement and name of column
                   ['Civic','Prius','Focus'],
                   True) # allow duplicate columns (with the same name)
    title('Insert model on index 1 to cars_df')
    display(cars_df)


    new_car_1 = {'Brand':'Hyundai',
                 'Model':'Avante',
                 'Price':20000,
                 'Year':2010}
    #cars_df = cars_df.append(new_car_1,ignore_index=True)
    cars_df.loc['Car 4'] = list(new_car_1.values())
    cars_df.loc['Car 3'] = ['Suzuki','Swift',26000,2013]
    title('Add a new car and modify Car 3 to cars_df')
    display(cars_df)


    cars_df['Discount'] = 0.1*cars_df['Price']
    title('Add discount column to cars_df')
    display(cars_df)


    cars_df['Discount Price'] = cars_df['Price']-cars_df['Discount']
    title('Add discount price to cars_df')
    display(cars_df)

def part2():
    #pd.set_option('display.max_rows',None)
    pd.set_option('display.max_columns',None)
    pd.set_option('display.width',None)
    zoom_logs = pd.read_csv("datasets/zoom_logs.csv")
    title("Display zoom logs")
    display(zoom_logs)


    new_zoom_logs = zoom_logs.loc[:,['name','join_time','leave_time']]
    title("Display only name, join_time, leave_time")
    display(new_zoom_logs)


    new_zoom_logs = zoom_logs.loc[:200,['name','join_time','leave_time']]
    final_zoom_logs = new_zoom_logs.rename(columns={'name':'Participant',
                                                    'join_time':'Join Time',
                                                    'leave_time':'Leave Time'
                                                    }
                                           )
    title("Only the first 200 people and rename the columns into final_zoom_logs")
    display(final_zoom_logs)


    filter1 = zoom_logs[zoom_logs.record_consent!="yes"]
    title("Filter original zoom_logs with no record consent")
    display(filter1)


    sorted_zl = zoom_logs.sort_values('name',ascending=True)
    title("Sort zoom_logs by name")
    display(sorted_zl)

import os
cwd = os.getcwd()

def laptops():
    laptops_df = pd.read_csv(os.getcwd()+'/datasets/laptops.csv',encoding='latin-1')
    laptops_df = laptops_df.drop(laptops_df.columns[0],axis=1)
    print(laptops_df,end='\n\n')
    laptops_df['Count']=1

    laptops_df2 = laptops_df.loc[:,['Company','Price_euros','Count']].groupby('Company').sum()
    laptops_df2['Average_sales']=laptops_df2['Price_euros']/laptops_df2['Count']
    laptops_df2 = laptops_df2.sort_values(['Average_sales','Price_euros'],ascending=[False,False])
    print(laptops_df2,end='\n\n')

def covid_data():
    covid_df = pd.read_csv(os.getcwd()+'/datasets/covid_19_clean_complete.csv')
    print(covid_df,end='\n\n')

    ll_data = covid_df.loc[:,['Long','Lat']]
    print(ll_data,end='\n\n')

    pd.set_option('display.max_rows',None)
    indo_covid = covid_df[covid_df['Country/Region'] == "Indonesia"].loc[:,covid_df.columns[1:-1]].reset_index().loc[39:].reset_index().drop(['index','level_0','Lat','Long'],axis=1)
    print(indo_covid,end='\n\n')

    fig, ax = plt.subplots(figsize=(10,5))
    ax.scatter(x=ll_data["Long"],
               y=ll_data["Lat"],
               c='green',
               s=18)
    fig.show()

# part1()
# part2()

# laptops()
covid_data()