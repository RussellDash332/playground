class Polynomial(): # Immutable polynomial

    def __init__(self, terms):
        self.terms = list(terms)

    def degree(self):
        return len(self.terms)-1

    def coeff(self,d):
        return self.terms[d]

    def coefficients(self): # Helper method - returns [a_0, a_1, ..., a_n]
        result = []
        for i in range(self.degree()+1):
            result.append(self.coeff(i))
        return result

    def add(self, p):
        res = []
        if p.degree() < self.degree():
            for i in range(p.degree()+1):
                res.append(p.coeff(i)+self.coeff(i))
            for j in range(p.degree()+1,self.degree()+1):
                res.append(self.coeff(j))
        else:
            for i in range(self.degree()+1):
                res.append(p.coeff(i)+self.coeff(i))
            for j in range(self.degree()+1,p.degree()+1):
                res.append(p.coeff(j))
        return Polynomial(res)

    def minus(self, p):
        res = []
        if p.degree() < self.degree():
            for i in range(p.degree()+1):
                res.append(-p.coeff(i)+self.coeff(i))
            for j in range(p.degree()+1,self.degree()+1):
                res.append(self.coeff(j))
        else:
            for i in range(self.degree()+1):
                res.append(-p.coeff(i)+self.coeff(i))
            for j in range(self.degree()+1,p.degree()+1):
                res.append(-p.coeff(j))
        while len(res) >= 2 and res[-1] == 0:
            res.pop()
        return Polynomial(res)
    
    def times(self,p):
        term = Polynomial([0])
        for i in range(p.degree()+1):
            term = term.add(Polynomial([0]*i+list(map(lambda t:t*p.coeff(i),self.terms))))
        return term

    def quotient(self,p):
        term = Polynomial([0])
        copy = self
        while p.degree() <= copy.degree():
            highest_deg = copy.degree()-p.degree()
            highest_coeff = copy.coeff(copy.degree())/p.coeff(p.degree())
            mini = Polynomial([0]*highest_deg+[highest_coeff])
            term = term.add(mini)
            copy = copy.minus(p.times(mini))
        return term

    def remainder(self,p):
        return self.minus(self.quotient(p).times(p))

p1 = Polynomial((1,0,1))  # x^2+1
p2 = Polynomial((1,1))  # x+1
p3 = p1.add(p2)
p4 = p1.minus(p2)
p5 = p1.times(p2)
p6 = Polynomial((-1,1))  # x-1
p7 = p5.times(p6)
p8 = Polynomial((-1,0,0,0,1))  # x^4-1
p9 = p7.quotient(p6) # x^3+x^2+x+1
p10 = p7.remainder(p6)
p11 = Polynomial((1,0,1,0,1))  # x^4+x^2+1
p12 = p11.quotient(p2)
p13 = p11.remainder(p2)

p = (p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13)
for poly in p:
    print("p%d.coefficients() :"%(p.index(poly)+1),poly.coefficients())

print("==========")
print("p1.coefficients():", p1.coefficients() == [1, 0, 1])
print("p2.coefficients():", p2.coefficients() == [1, 1])
print("p3.coefficients():", p3.coefficients() == [2, 1, 1])
print("p4.coefficients():", p4.coefficients() == [0, -1, 1])
print("p5.coefficients():", p5.coefficients() == [1, 1, 1, 1])
print("p7.coefficients():", p7.coefficients() == [-1, 0, 0, 0, 1])
print("p9.coefficients():", p9.coefficients() == [1.0, 1.0, 1.0, 1.0])
print("p10.coefficients():", p10.coefficients() == [0])
print("p12.coefficients():", p12.coefficients() == [-2.0, 2.0, -1.0, 1.0])
print("p13.coefficients():", p13.coefficients()==[3.0])
