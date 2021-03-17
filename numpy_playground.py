import numpy as np

a = [2,3,6,1,2,5,2,9,8]
b = np.array(a)

print(b.mean())
print(b.std())
print(b.cumsum())


import pylab as py
py.plot(b)