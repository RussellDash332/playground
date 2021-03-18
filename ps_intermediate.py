def getMaxArea(w, h, isVertical, distance):
    result = []
    
    xpoint = [0]
    ypoint = [0]
    
    for i in range(len(distance)):
        if not isVertical[i]:
            ypoint.append(distance[i])
            j = len(ypoint)-1
            while j > 0 and ypoint[j-1] > ypoint[j]:
                ypoint[j-1], ypoint[j] = ypoint[j], ypoint[j-1]
                j -= 1
        else:
            xpoint.append(distance[i])
            j = len(xpoint)-1
            while j > 0 and xpoint[j-1] > xpoint[j]:
                xpoint[j-1], xpoint[j] = xpoint[j], xpoint[j-1]
                j -= 1
        
        maxw,maxh = 0,0
        for k in range(1,len(xpoint)):
            if maxw < xpoint[k]-xpoint[k-1]:
                maxw = xpoint[k]-xpoint[k-1]
        for k in range(1,len(ypoint)):
            if maxh < ypoint[k]-ypoint[k-1]:
                maxh = ypoint[k]-ypoint[k-1]
        maxw = max(maxw, w-xpoint[-1])
        maxh = max(maxh, h-ypoint[-1])
        result.append(maxw*maxh)
                
    return result

def authEvents(events):
    def hash(x):
        hv = 0
        for i in x:
            hv *= 131
            hv += ord(i)
        return hv % (10**9+7)
    curr = 0
    result = []
    for e,p in events:
        if e == 'setPassword':
            curr = hash(p)
        else:
            bound = 131*curr % (10**9+7)
            if int(p) == curr or bound < int(p) < (bound+131):
                result.append(1)
            else:
                result.append(0)
    return result